package com.example.weatherapp.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.weatherapp.Adapter.WeatherAdapter;
import com.example.weatherapp.R;
import com.example.weatherapp.models.CurrentWeather;
import com.example.weatherapp.models.WeatherList;
import com.example.weatherapp.utils.GsonRequest;
import com.example.weatherapp.utils.StringToNumberUtils;
import com.example.weatherapp.utils.WeatherUtils;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class ChooseLocationFragment extends Fragment {

    private static final String ARG_LOCATION = "location";
    private static final String ARG_LATITUDE = "latitude";
    private static final String ARG_LONGTITUDE = "longtitude";

    // Giá trị từ Location qua
    private String location;
    private float latitude;
    private float longtitude;

    // GUI components
    private TextView textviewLocation;
    private TextView temperatureTV, highLowTV, humidTV, rainTV, windTV, weatherDescTV;
    private ImageView weatherIconIV;
    private LinearLayout rainLayout;
    private RecyclerView recyclerView;

    CurrentWeather[] weatherList;
    private WeatherAdapter weatherAdapter;
    private CurrentWeather currentWeather;
    private RequestQueue queue;

    public ChooseLocationFragment() {
        // Required empty public constructor
    }

    public static ChooseLocationFragment newInstance(String paramLocation, String paramLatitude, String paramLongtitude) {
        ChooseLocationFragment fragment = new ChooseLocationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LOCATION, paramLocation);
        args.putString(ARG_LATITUDE, paramLatitude);
        args.putString(ARG_LONGTITUDE, paramLongtitude);
        fragment.setArguments(args);
        return fragment;
    }

    private void addControls(View view) {
        textviewLocation = view.findViewById(R.id.textViewLocation);
        temperatureTV = view.findViewById(R.id.textViewTemperature);
        highLowTV = view.findViewById(R.id.textViewHighLow);
        humidTV = view.findViewById(R.id.textViewHumid);
        rainTV = view.findViewById(R.id.textViewRain);
        rainLayout = view.findViewById(R.id.layoutRain);
        windTV = view.findViewById(R.id.textViewWind);
        weatherDescTV = view.findViewById(R.id.textViewWeatherDesc);

        weatherIconIV = view.findViewById(R.id.imageViewWeatherIcon);

        recyclerView = view.findViewById(R.id.viewWeather);
    }

    private void setWeather() {
        temperatureTV.setText(String.format("%s°C", Math.round(currentWeather.main.temperature)));
        highLowTV.setText(String.format("H: %s°C, L: %s°C", Math.round(currentWeather.main.maxTemp), Math.round(currentWeather.main.minTemp)));
        humidTV.setText(String.format("%d%%", currentWeather.main.humidity));
        windTV.setText(String.format("%skm/h", Math.round(currentWeather.wind.windSpeed)));
        weatherDescTV.setText(currentWeather.weathers[0].main);

        if (currentWeather.rain != null) {
            rainLayout.setVisibility(View.VISIBLE);
            rainTV.setText(String.format("%smm", currentWeather.rain.lastOneHourVolume));
        } else {
            rainLayout.setVisibility(View.GONE);
        }
        String icon = currentWeather.weathers[0].icon;


        if (icon.contains("03"))
            weatherIconIV.setImageDrawable(getImage(getContext(), "icon_03"));
        else if (icon.contains("04"))
            weatherIconIV.setImageDrawable(getImage(getContext(), "icon_04"));
        else
            weatherIconIV.setImageDrawable(getImage(getContext(), "icon_" + icon));
    }

    private void receiveLocation() {
        String url = WeatherUtils.getCurrentWeatherURL(latitude, longtitude);
        GsonRequest<CurrentWeather> currentWeatherGsonRequest = new GsonRequest<>(url, CurrentWeather.class, null, response -> {
            currentWeather = response;
            setWeather();
        }, error -> {
            Log.d("Volley error", String.valueOf(error));
        });
        queue.add(currentWeatherGsonRequest);
    }

    private void initRecyclerView() {
        String url = WeatherUtils.getForecastWeatherURL(latitude, longtitude);
        Log.d("Lat & lon", "" + latitude + ", " + longtitude);
        GsonRequest<WeatherList> currentWeatherGsonRequest = new GsonRequest<>(url, WeatherList.class, null, response -> {
            weatherList = response.weatherData; // Lấy danh sách từ WeatherList

            if (weatherList != null) {
                Log.d("Dữ liệu weatherList", Arrays.toString(weatherList));
                // Create WeatherAdapter instance and set it to RecyclerView
                weatherAdapter = new WeatherAdapter(getContext(), weatherList);
                recyclerView.setAdapter(weatherAdapter);
            } else {
                Log.d("Dữ liệu weatherList", "weatherList is null");
            }
        }, error -> {
            Log.d("Volley error", String.valueOf(error));
        });

        queue.add(currentWeatherGsonRequest);
    }

    public static Drawable getImage(Context c, String ImageName) {
        return c.getResources().getDrawable(c.getResources().getIdentifier(ImageName, "drawable", c.getPackageName()));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String latitudeString, longtitudeString;
            location = getArguments().getString(ARG_LOCATION);
            latitudeString = getArguments().getString(ARG_LATITUDE);
            longtitudeString = getArguments().getString(ARG_LONGTITUDE);

            // Chuyển đổi string thành float
            latitude = StringToNumberUtils.StringToFloat(latitudeString);
            longtitude = StringToNumberUtils.StringToFloat(longtitudeString);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo RequestQueue
        queue = Volley.newRequestQueue(requireContext());

        addControls(view);
        textviewLocation.setText(location);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        receiveLocation();
        initRecyclerView();
    }
}
