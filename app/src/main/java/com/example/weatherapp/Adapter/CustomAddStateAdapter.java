package com.example.weatherapp.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.weatherapp.R;
import com.example.weatherapp.models.State;
import com.example.weatherapp.utils.LocationUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CustomAddStateAdapter extends RecyclerView.Adapter<CustomAddStateAdapter.ViewHolder> {
    private final List<State> states;

    public CustomAddStateAdapter(List<State> states) {
        this.states = states;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_state_recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        State state = states.get(position);
        String location = String.format("%s, %s", state.getName(), state.getCountry_name());
        holder.stateNameTextView.setText(location);
        holder.addLocationButton.setOnClickListener(l -> {

        });
    }

    @Override
    public int getItemCount() {
        return states.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView stateNameTextView;
        Button addLocationButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            stateNameTextView = itemView.findViewById(R.id.textViewStateName);
            addLocationButton = itemView.findViewById(R.id.buttonAddLocation);
        }
    }
}
