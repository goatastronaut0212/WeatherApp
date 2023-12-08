package com.example.weatherapp.Activity;

import android.graphics.Color;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.weatherapp.Adapter.CustomAddStateAdapter;
import com.example.weatherapp.R;
import com.example.weatherapp.models.State;
import com.example.weatherapp.utils.CustomJsonReader;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AddLocationActivity extends AppCompatActivity {
    ArrayList<State> states;
    SearchView searchView;
    RecyclerView recyclerView;
    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        searchView = findViewById(R.id.searchViewAddLocation);
        recyclerView = findViewById(R.id.recyclerViewAddLocation);
        backButton = findViewById(R.id.imageButtonBack);

        backButton.setOnClickListener(l -> {
            finish();
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        states = CustomJsonReader.readJson(this, R.raw.states, State.class);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                List<State> filteredStates = states.stream().filter(state -> state.getName().contains(query)).collect(Collectors.toList());
                CustomAddStateAdapter adapter = new CustomAddStateAdapter(filteredStates);
                recyclerView.setAdapter(adapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }
}