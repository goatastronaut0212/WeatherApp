package com.example.weatherapp.utils;

public class StringToNumberUtils {
    public static float StringToFloat(String string) {
        try {
            // Chuyển đổi chuỗi thành số thực
            Float floatValue = Float.valueOf(string);

            return floatValue;
        } catch (NumberFormatException e) {
            System.out.println("Không thể chuyển đổi chuỗi thành số thực.");
            e.printStackTrace();
            return 0.0F;
        }
    }
}
