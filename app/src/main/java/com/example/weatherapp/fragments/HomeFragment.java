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
import com.example.weatherapp.Adapter.WeatherAdapter;
import com.example.weatherapp.R;
import com.example.weatherapp.models.CurrentWeather;
import com.example.weatherapp.models.WeatherList;
import com.example.weatherapp.utils.GsonRequest;
import com.example.weatherapp.utils.WeatherUtils;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class HomeFragment extends Fragment {
    TextView temperatureTV, highLowTV, humidTV, rainTV, windTV, weatherDescTV;
    ImageView weatherIconIV;
    LinearLayout rainLayout;
    RecyclerView recyclerView;
    CurrentWeather[] weatherList;
    WeatherAdapter weatherAdapter;
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

    private void initRecyclerView(Location location) {
        String url = WeatherUtils.getForecastWeatherURL(location.getLatitude(), location.getLongitude());
        GsonRequest<WeatherList> currentWeatherGsonRequest = new GsonRequest<>(url, WeatherList.class, null, response -> {
            weatherList = response.weatherData; // Lấy danh sách từ WeatherList

            if (weatherList != null) {
                Log.d("Dữ liệu weatherList", Arrays.toString(weatherList));
                // Create WeatherAdapter instance and set it to RecyclerView
                weatherAdapter = new WeatherAdapter(getContext(), weatherList);
                recyclerView.setAdapter(weatherAdapter);
            } else {
                Log.d("Dữ liệu weatherList", "weatherList is null");
            }
        }, error -> {
            Log.d("Volley error", String.valueOf(error));
        });

        queue.add(currentWeatherGsonRequest);
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
            String url = WeatherUtils.getCurrentWeatherURL(lastLocation.getLatitude(), lastLocation.getLongitude());
            GsonRequest<CurrentWeather> currentWeatherGsonRequest = new GsonRequest<>(url, CurrentWeather.class, null, response -> {
                currentWeather = response;
                setWeather();
            }, error -> {
                Log.d("Volley error", String.valueOf(error));
            });
            queue.add(currentWeatherGsonRequest);

            initRecyclerView(lastLocation);
        }
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