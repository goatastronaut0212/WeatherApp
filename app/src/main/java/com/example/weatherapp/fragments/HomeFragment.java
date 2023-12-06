package com.example.weatherapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.weatherapp.Domains.Hourly;
import com.example.weatherapp.R;
import com.example.weatherapp.models.CurrentWeather;
import com.example.weatherapp.utils.GsonRequest;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    TextView temperatureTV, highLowTV, humidTV, rainTV, windTV, weatherDescTV;
    ImageView weatherIconIV;
    LinearLayout rainLayout;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapterHourly;
    private static final String CURRENT_WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";
    private static final String FORECAST_WEATHER_URL = "https://api.openweathermap.org/data/2.5/forecast";
    private static final String API_KEY = "1b7ce001fbf4143a1618c866d8204c56";
    private static final int MIN_DISTANCE_THRESHOLD = 500;
    private static final long MIN_TIME_THRESHOLD = 20 * 60 * 1000; // 20 phút

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private SettingsClient settingsClient;
    private LocationRequest locationRequest;
    private LocationSettingsRequest locationSettingsRequest;
    private Location lastLocation;
    private CurrentWeather currentWeather;

    RequestQueue queue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Viết code vào
        addControls(view);
        queue = Volley.newRequestQueue(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        initRecyclerView();
        getLocation();
    }

    private void addControls(View view) {
        temperatureTV = view.findViewById(R.id.textViewTemperature);
        highLowTV = view.findViewById(R.id.textViewHighLow);
        humidTV = view.findViewById(R.id.textViewHumid);
        rainTV = view.findViewById(R.id.textViewRain);
        rainLayout = view.findViewById(R.id.layoutRain);
        windTV = view.findViewById(R.id.textViewWind);
        weatherDescTV = view.findViewById(R.id.textViewWeatherDesc);

        weatherIconIV = view.findViewById(R.id.imageViewWeatherIcon);

        recyclerView = view.findViewById(R.id.viewWeather);


    }

    private void setWeather() {
        temperatureTV.setText(String.format("%s°C", Math.round(currentWeather.main.temperature)));
        highLowTV.setText(String.format("H: %s°C, L: %s°C", Math.round(currentWeather.main.maxTemp), Math.round(currentWeather.main.minTemp)));
        humidTV.setText(String.format("%d%%", currentWeather.main.humidity));
        windTV.setText(String.format("%skm/h", Math.round(currentWeather.wind.windSpeed)));
        weatherDescTV.setText(currentWeather.weathers[0].main);

        if (currentWeather.rain != null) {
            rainLayout.setVisibility(View.VISIBLE);
            rainTV.setText(String.format("%smm", currentWeather.rain.lastOneHourVolume));
        } else {
            rainLayout.setVisibility(View.GONE);
        }
        String icon = currentWeather.weathers[0].icon;


        if (icon.contains("03"))
            weatherIconIV.setImageDrawable(getImage(getContext(), "icon_03"));
        else if (icon.contains("04"))
            weatherIconIV.setImageDrawable(getImage(getContext(), "icon_04"));
        else
            weatherIconIV.setImageDrawable(getImage(getContext(), "icon_" + icon));
    }


    private void initRecyclerView() {
        ArrayList<Hourly> items = new ArrayList<>();
        items.add(new Hourly("10 pm", 28, "cloudy"));
        items.add(new Hourly("11 pm", 29, "sun"));
        items.add(new Hourly("12 pm", 30, "wind"));
        items.add(new Hourly("1 am", 29, "rainy"));
        items.add(new Hourly("2 am", 27, "storm"));

        adapterHourly = new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 0;
            }
        };
    }

    private void getLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        settingsClient = LocationServices.getSettingsClient(getContext());
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull @NotNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                receiveLocation(locationResult);
            }
        };
        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setMinUpdateIntervalMillis(1000)
                .build();

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        locationSettingsRequest = builder.build();
        startLocationUpdates();
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        settingsClient.checkLocationSettings(locationSettingsRequest).addOnSuccessListener(locationSettingsResponse -> {
            Log.d("fused provider", "Location settings okay");
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        }).addOnFailureListener(e -> {
            int statusCode = ((ApiException) e).getStatusCode();
            Log.d("fused provider", "inside error ->" + statusCode);
        });

    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback).addOnCompleteListener(task -> {
            Log.d("fused provider", "stop location updates");
        });
    }

    private boolean canChangeLocation(Location newLocation) {
        if (lastLocation == null) {
            return true;
        }

        float distance = newLocation.distanceTo(lastLocation);

        long timeElapsed = System.currentTimeMillis() - lastLocation.getTime();

        return distance > MIN_DISTANCE_THRESHOLD || timeElapsed > MIN_TIME_THRESHOLD;
    }

    private void receiveLocation(LocationResult locationResult) {
        if (canChangeLocation(locationResult.getLastLocation())) {
            lastLocation = locationResult.getLastLocation();
            String url = buildWeatherURL(CURRENT_WEATHER_URL, lastLocation.getLatitude(), lastLocation.getLongitude());
            GsonRequest<CurrentWeather> currentWeatherGsonRequest = new GsonRequest<>(url, CurrentWeather.class, null, response -> {
                currentWeather = response;
                setWeather();
            }, error -> {
                Log.d("Volley error", String.valueOf(error));
            });
            queue.add(currentWeatherGsonRequest);
        }
    }

    private String buildWeatherURL(String url, double latitude, double longitude) {
        Uri weatherURI = Uri.parse(url).buildUpon()
                .appendQueryParameter("lat", String.valueOf(latitude))
                .appendQueryParameter("lon", String.valueOf(longitude))
                .appendQueryParameter("appid", API_KEY)
                .appendQueryParameter("units", "metric")
                .build();
        return weatherURI.toString();
    }

    public static Drawable getImage(Context c, String ImageName) {
        return c.getResources().getDrawable(c.getResources().getIdentifier(ImageName, "drawable", c.getPackageName()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }
}