package com.example.weatherapp.utils;

import android.os.Environment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class LocationUtils {
    static String appName = "WeatherApp";
    static String farvoriteName = "favorite.json";
    static File directory = new File(Environment.getExternalStorageDirectory() + "/" + appName);
    public static void createDir() {

        // Tạo thư mục nếu chưa tồn tại
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public static void createFavorite(String location, String latitude, String longtitude) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("location", location);
            jsonObject.put("latitude", latitude);
            jsonObject.put("longtitude", longtitude);

            // Ghi dữ liệu vào file JSON
            FileWriter fileWriter = new FileWriter(directory.getPath() + farvoriteName);
            fileWriter.write(jsonObject.toString());
            fileWriter.flush();
            fileWriter.close();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteFavorite(String location, String latitude, String longtitude) {
        try {
            // Đọc dữ liệu từ tệp JSON hiện tại
            File file = new File(directory.getPath() + farvoriteName);
            JSONObject jsonObject;

            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    stringBuilder.append(line);
                }

                br.close();
                fis.close();

                // Chuyển đổi nội dung JSON thành đối tượng JSONObject
                jsonObject = new JSONObject(stringBuilder.toString());

                // Xóa dữ liệu dựa trên điều kiện (ví dụ: "location")
                jsonObject.remove(location);

                // Lưu lại dữ liệu đã xóa vào tệp JSON
                FileWriter fileWriter = new FileWriter(directory.getPath() + farvoriteName);
                fileWriter.write(jsonObject.toString());
                fileWriter.flush();
                fileWriter.close();
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }
}
