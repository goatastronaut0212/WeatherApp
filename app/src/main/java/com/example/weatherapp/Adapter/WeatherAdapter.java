package com.example.weatherapp.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R; // Replace with your actual package name
import com.example.weatherapp.models.CurrentWeather;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    private Context context;
    private CurrentWeather[] weatherList; // Thêm biến weatherList

    public WeatherAdapter(Context context, CurrentWeather[] weatherList) {
        this.context = context;
        this.weatherList = weatherList;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_hourly, parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        CurrentWeather currentWeather = weatherList[position];

        // Chuyển đổi Unix time thành định dạng giờ:phút:giây
        String formattedTime = convertUnixTimeToTimeString(currentWeather.dateTime);

        // Set văn bản cho hourlyTextView
        holder.hourlyTextView.setText(formattedTime);

        // Set ảnh
        String icon = currentWeather.weathers[0].icon;
        if (icon.contains("03"))
            holder.picImageView.setImageDrawable(getImage(context, "icon_03"));
        else if (icon.contains("04"))
            holder.picImageView.setImageDrawable(getImage(context, "icon_04"));
        else
            holder.picImageView.setImageDrawable(getImage(context, "icon_" + icon));

        // Set nhiêt độ
        holder.temperatureTextView.setText(String.format("%s°C", Math.round(currentWeather.main.temperature)));
    }

    @Override
    public int getItemCount() {
        return weatherList.length;
    }

    public class WeatherViewHolder extends RecyclerView.ViewHolder {

        private TextView hourlyTextView;
        private ImageView picImageView;
        private TextView temperatureTextView;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);

            hourlyTextView = itemView.findViewById(R.id.hourTxt);
            picImageView = itemView.findViewById(R.id.pic);
            temperatureTextView = itemView.findViewById(R.id.tempTxt);
        }
    }

    // Phương thức chuyển đổi Unix time thành định dạng giờ:phút:giây
    private String convertUnixTimeToTimeString(long unixTime) {
        // Tạo một đối tượng Date từ Unix time
        Date date = new Date(unixTime * 1000L); // Unix time được tính bằng giây, cần nhân 1000 để chuyển thành mili giây

        // Sử dụng SimpleDateFormat để định dạng thời gian
        SimpleDateFormat sdf = new SimpleDateFormat("E HH:mm", Locale.getDefault());

        // Trả về thời gian đã định dạng
        return sdf.format(date);
    }

    public Drawable getImage(Context c, String ImageName) {
        return c.getResources().getDrawable(c.getResources().getIdentifier(ImageName, "drawable", c.getPackageName()));
    }
}
