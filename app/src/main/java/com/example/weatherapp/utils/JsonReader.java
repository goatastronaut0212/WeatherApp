package com.example.weatherapp.utils;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class JsonReader {
    public static <T> ArrayList<T> readJson(Context context, int rawResourceId, Class<T> type) {
        try {
            // Mở tệp JSON từ thư mục raw
            InputStream inputStream = context.getResources().openRawResource(rawResourceId);
            InputStreamReader reader = new InputStreamReader(inputStream);

            // Sử dụng Gson để chuyển đổi JSON thành danh sách các đối tượng T
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<T>>() {
            }.getType();
            ArrayList<T> result = gson.fromJson(reader, listType);

            // Đóng luồng đọc
            reader.close();

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
