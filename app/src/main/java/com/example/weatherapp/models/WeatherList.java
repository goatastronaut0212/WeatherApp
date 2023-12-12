package com.example.weatherapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class WeatherList {
    @SerializedName("list")
    public CurrentWeather[] weatherData;

    public WeatherList(CurrentWeather[] weatherData) {
        this.weatherData = weatherData;
    }

    @Override
    public String toString() {
        return "WeatherList{" +
                "weatherData=" + Arrays.toString(weatherData) +
                '}';
    }
}
