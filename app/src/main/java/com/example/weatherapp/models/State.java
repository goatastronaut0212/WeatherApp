package com.example.weatherapp.models;

import com.google.gson.annotations.SerializedName;

public class State {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("country_name")
    private String country_name;
    @SerializedName("latitude")
    private float latitude;
    @SerializedName("longitude")
    private float longtitude;

    public State(int id, String name, String country_name, float latitude, float longtitude) {
        this.id = id;
        this.name = name;
        this.country_name = country_name;
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountry_name() {
        return country_name;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongtitude() {
        return longtitude;
    }

    public String toString() {
        return String.format("%s, %s", name, country_name);
    }
}
