package com.example.weatherapp.models;

import com.google.gson.annotations.SerializedName;

public class Wind {
    @SerializedName("speed")
    public double windSpeed;
    @SerializedName("deg")
    public int windDirection;
    @SerializedName("gust")
    public double windGust;

    public Wind(double windSpeed, int windDirection, double windGust) {
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.windGust = windGust;
    }

    @Override
    public String toString() {
        return "Wind{" +
                "windSpeed=" + windSpeed +
                ", windDirection=" + windDirection +
                ", windGust=" + windGust +
                '}';
    }
}
