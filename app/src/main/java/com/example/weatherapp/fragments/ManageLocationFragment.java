package com.example.weatherapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.weatherapp.Activity.AddLocationActivity;
import com.example.weatherapp.R;
import com.example.weatherapp.models.State;

import com.example.weatherapp.utils.LocationUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ManageLocationFragment extends Fragment {
    private ArrayList<State> states;
    private SearchView searchView;
    private ListView listView;
    private FloatingActionButton addLocationFab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manage_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchView = view.findViewById(R.id.searchviewLocation);
        listView = view.findViewById(R.id.listviewLocation);
        addLocationFab = view.findViewById(R.id.floatingActionButtonAddLocation);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Lấy ra đối tượng State được chọn
                State selectedState = states.get(position);

                // Tạo một Bundle để truyền dữ liệu từ ManageLocationFragment đến ChooseLocationFragment
                Bundle bundle = new Bundle();
                bundle.putSerializable("location", selectedState.toString());
                bundle.putSerializable("latitude", "" + selectedState.getLatitude());
                bundle.putSerializable("longtitude", "" + selectedState.getLongtitude());


                // Tạo một instance của ChooseLocationFragment
                ChooseLocationFragment chooseLocationFragment = new ChooseLocationFragment();
                chooseLocationFragment.setArguments(bundle);

                // Thêm ChooseLocationFragment vào fragment stack
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, chooseLocationFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        addLocationFab.setOnClickListener(l -> {
            startActivity(new Intent(getContext(), AddLocationActivity.class));
        });
        ArrayAdapter<State> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, states);
        listView.setAdapter(adapter);
    }

    public void setStates(ArrayList<State> states) {
        this.states = states;
    }

    @Override
    public void onResume() {
        super.onResume();
        states = LocationUtils.loadFavourite(getContext());
        ArrayAdapter<State> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, states);
        listView.setAdapter(adapter);
    }
}