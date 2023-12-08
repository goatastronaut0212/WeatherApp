package com.example.weatherapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.weatherapp.models.State;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class LocationUtils {
    private static final String PREF_NAME = "location preferences";
    private static final String FAVOURITE_LOCATION = "favourite location";
    private static final Gson gson = new Gson();

    public static void saveFavourite(Context context, ArrayList<State> states) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String favouriteJson = gson.toJson(states);
        editor.putString(FAVOURITE_LOCATION, favouriteJson);
        editor.apply();
    }

    public static ArrayList<State> loadFavourite(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String favouriteJson = sharedPreferences.getString(FAVOURITE_LOCATION, null);
        Type type = new TypeToken<ArrayList<State>>() {
        }.getType();

        if (favouriteJson == null)
            return new ArrayList<>();

        return gson.fromJson(favouriteJson, type);
    }
}
