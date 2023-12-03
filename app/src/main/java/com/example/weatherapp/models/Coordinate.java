package com.example.weatherapp.models;

import com.google.gson.annotations.SerializedName;

public class Coordinate {
    @SerializedName("lon")
    private double longitude;
    @SerializedName("lat")
    private double latitude;

    public Coordinate(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
