package com.example.weatherapp.models;

import com.google.gson.annotations.SerializedName;

public class Cloud {
    @SerializedName("all")
    public int cloudiness;

    public Cloud(int cloudiness) {
        this.cloudiness = cloudiness;
    }

    @Override
    public String toString() {
        return "Cloud{" +
                "cloudiness=" + cloudiness +
                '}';
    }
}
