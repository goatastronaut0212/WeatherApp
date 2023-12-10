package com.example.weatherapp.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.weatherapp.Domains.Hourly;
import com.example.weatherapp.R;
import com.example.weatherapp.models.CurrentWeather;
import com.example.weatherapp.utils.GsonRequest;
import com.example.weatherapp.utils.RandomAPIKey;
import com.example.weatherapp.utils.StringToNumberUtils;
import com.example.weatherapp.utils.WeatherUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ChooseLocationFragment extends Fragment {

    private static final String ARG_LOCATION = "location";
    private static final String ARG_LATITUDE = "latitude";
    private static final String ARG_LONGTITUDE = "longtitude";

    // Giá trị từ Location qua
    private String location;
    private float latitude;
    private float longtitude;

    // GUI components
    private TextView textviewLocation;
    private TextView temperatureTV, highLowTV, humidTV, rainTV, windTV, weatherDescTV;
    private ImageView weatherIconIV;
    private LinearLayout rainLayout;
    private RecyclerView recyclerView;

    private RecyclerView.Adapter adapterHourly;
    private CurrentWeather currentWeather;
    private RequestQueue queue;

    public ChooseLocationFragment() {
        // Required empty public constructor
    }

    public static ChooseLocationFragment newInstance(String paramLocation, String paramLatitude, String paramLongtitude) {
        ChooseLocationFragment fragment = new ChooseLocationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LOCATION, paramLocation);
        args.putString(ARG_LATITUDE, paramLatitude);
        args.putString(ARG_LONGTITUDE, paramLongtitude);
        fragment.setArguments(args);
        return fragment;
    }

    private void addControls(View view) {
        textviewLocation = view.findViewById(R.id.textViewLocation);
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

    private void receiveLocation() {
        String url = WeatherUtils.getCurrentWeatherURL(latitude, longtitude);
        GsonRequest<CurrentWeather> currentWeatherGsonRequest = new GsonRequest<>(url, CurrentWeather.class, null, response -> {
            currentWeather = response;
            setWeather();
        }, error -> {
            Log.d("Volley error", String.valueOf(error));
        });
        queue.add(currentWeatherGsonRequest);
    }

    public static Drawable getImage(Context c, String ImageName) {
        return c.getResources().getDrawable(c.getResources().getIdentifier(ImageName, "drawable", c.getPackageName()));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String latitudeString, longtitudeString;
            location = getArguments().getString(ARG_LOCATION);
            latitudeString = getArguments().getString(ARG_LATITUDE);
            longtitudeString = getArguments().getString(ARG_LONGTITUDE);

            // Chuyển đổi string thành float
            latitude = StringToNumberUtils.StringToFloat(latitudeString);
            longtitude = StringToNumberUtils.StringToFloat(longtitudeString);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo RequestQueue
        queue = Volley.newRequestQueue(requireContext());

        addControls(view);
        textviewLocation.setText(location + ", " + latitude + ", " + longtitude);
        receiveLocation();
    }
}