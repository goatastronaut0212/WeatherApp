package com.example.weatherapp.utils;

import android.net.Uri;

public class WeatherUtils {
    private static final String CURRENT_WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";
    private static final String FORECAST_WEATHER_URL = "https://api.openweathermap.org/data/2.5/forecast";

    public static String getCurrentWeatherURL(double latitude, double longitude) {
        Uri weatherURI = Uri.parse(CURRENT_WEATHER_URL).buildUpon()
                .appendQueryParameter("lat", String.valueOf(latitude))
                .appendQueryParameter("lon", String.valueOf(longitude))
                .appendQueryParameter("appid", RandomAPIKey.getRandomKey())
                .appendQueryParameter("units", "metric")
                .build();
        return weatherURI.toString();
    }

    public static String getForecastWeatherURL(double latitude, double longitude) {
        Uri weatherURI = Uri.parse(FORECAST_WEATHER_URL).buildUpon()
                .appendQueryParameter("lat", String.valueOf(latitude))
                .appendQueryParameter("lon", String.valueOf(longitude))
                .appendQueryParameter("appid", RandomAPIKey.getRandomKey())
                .appendQueryParameter("units", "metric")
                .build();
        return weatherURI.toString();
    }
}
