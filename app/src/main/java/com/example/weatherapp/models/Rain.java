package com.example.weatherapp.models;

import com.google.gson.annotations.SerializedName;

public class Rain {
    @SerializedName("1h")
    public double lastOneHourVolume;

    public Rain(double lastOneHourVolume) {
        this.lastOneHourVolume = lastOneHourVolume;
    }

    @Override
    public String toString() {
        return "Rain{" +
                "lastOneHourVolume=" + lastOneHourVolume +
                '}';
    }
}
