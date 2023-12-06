package com.example.weatherapp.fragments;

import android.annotation.SuppressLint;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.Domains.Hourly;
import com.example.weatherapp.R;
import com.example.weatherapp.models.CurrentWeather;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private static final String CURRENT_WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";
    private static final String FORECAST_WEATHER_URL = "https://api.openweathermap.org/data/2.5/forecast";
    private static final String API_KEY = "1b7ce001fbf4143a1618c866d8204c56";
    private static final int MIN_DISTANCE_THRESHOLD = 500;
    private static final long MIN_TIME_THRESHOLD = 20 * 60 * 1000; // 20 phút
    private RecyclerView.Adapter adapterHourly;
    private RecyclerView recyclerView;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private SettingsClient settingsClient;
    private LocationRequest locationRequest;
    private LocationSettingsRequest locationSettingsRequest;
    private Location lastLocation;
    private CurrentWeather currentWeather;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getLocation();
        return inflater.inflate(R.layout.fragment_home, container, false);


    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Viết code vào
        recyclerView = getView().findViewById(R.id.view1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        initRecyclerView();


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
            buildWeatherURL(CURRENT_WEATHER_URL, lastLocation.getLatitude(), lastLocation.getLongitude());
        }
    }

    private String buildWeatherURL(String url, double latitude, double longitude) {
        Uri weatherURI = Uri.parse(url).buildUpon()
                .appendQueryParameter("lat", String.valueOf(latitude))
                .appendQueryParameter("lon", String.valueOf(longitude))
                .appendQueryParameter("appid", API_KEY)
                .build();
        return weatherURI.toString();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }
}