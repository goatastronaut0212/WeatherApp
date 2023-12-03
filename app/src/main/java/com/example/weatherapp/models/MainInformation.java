package com.example.weatherapp.models;

import com.google.gson.annotations.SerializedName;

public class MainInformation {
    @SerializedName("temp")
    public double temperature;
    @SerializedName("feels_like")
    public double actualTemperature;
    @SerializedName("temp_min")
    public double minTemp;
    @SerializedName("temp_max")
    public double maxTemp;
    public int pressure;
    public int humidity;
    @SerializedName("sea_level")
    public int seaLevel;
    @SerializedName("grnd_level")
    public int groundLevel;

    public MainInformation(double temperature, double actualTemperature, double minTemp, double maxTemp, int pressure, int humidity, int seaLevel, int groundLevel) {
        this.temperature = temperature;
        this.actualTemperature = actualTemperature;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.pressure = pressure;
        this.humidity = humidity;
        this.seaLevel = seaLevel;
        this.groundLevel = groundLevel;
    }

    @Override
    public String toString() {
        return "MainInformation{" +
                "temperature=" + temperature +
                ", actualTemperature=" + actualTemperature +
                ", minTemp=" + minTemp +
                ", maxTemp=" + maxTemp +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                ", seaLevel=" + seaLevel +
                ", groundLevel=" + groundLevel +
                '}';
    }
}
