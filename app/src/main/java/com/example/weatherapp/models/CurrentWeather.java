package com.example.weatherapp.models;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class CurrentWeather {
    @SerializedName("coord")
    private Coordinate coordinate;
    @SerializedName("weather")
    public Weather[] weathers;
    public MainInformation main;
    public int visibility;
    public Wind wind;
    public Rain rain;
    public Cloud clouds;
    @SerializedName("dt")
    public long dateTime;
    public int timezone;
    @SerializedName("id")
    public int cityID;
    @SerializedName("name")
    public String cityName;

    public CurrentWeather(Coordinate coordinate, Weather[] weathers, MainInformation main, int visibility, Wind wind, Rain rain, Cloud clouds, long dateTime, int timezone, int cityID, String cityName) {
        this.coordinate = coordinate;
        this.weathers = weathers;
        this.main = main;
        this.visibility = visibility;
        this.wind = wind;
        this.rain = rain;
        this.clouds = clouds;
        this.dateTime = dateTime;
        this.timezone = timezone;
        this.cityID = cityID;
        this.cityName = cityName;
    }

    @NotNull
    @Override
    public String toString() {
        return "CurrentWeather{" +
                "coordinate=" + coordinate +
                ", weathers=" + Arrays.toString(weathers) +
                ", main=" + main +
                ", visibility=" + visibility +
                ", wind=" + wind +
                ", rain=" + rain +
                ", clouds=" + clouds +
                ", dateTime=" + dateTime +
                ", timezone=" + timezone +
                ", cityID=" + cityID +
                ", cityName='" + cityName + '\'' +
                '}';
    }
}
