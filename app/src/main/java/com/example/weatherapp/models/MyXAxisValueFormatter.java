package com.example.weatherapp.models;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyXAxisValueFormatter extends ValueFormatter {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM HH:mm", Locale.getDefault());

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        // Chuyển đổi giá trị từ Unix Time thành ngày giờ
        long unixTime = (long) value;
        Date date = new Date(unixTime * 1000L); // convert to milliseconds
        return dateFormat.format(date);
    }
}
