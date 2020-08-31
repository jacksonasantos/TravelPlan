package com.jacksonasantos.travelplan.ui.vehicle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.DAO.Vehicle;
import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.ui.travel.RandomNumListAdapter;

import java.util.List;


public class VehicleFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_vehicle, container, false);

        //RecyclerView vehicleList = root.findViewById(R.id.listVehicles);
        //vehicleList.setHasFixedSize(true);
        //vehicleList.setLayoutManager(new LinearLayoutManager(vehicleList.getContext()));
        //vehicleList.setAdapter(new RandomNumListAdapter(1234));

        //List<Vehicle> vehicles = null;
        //VehicleAdapter vehicleAdapter = new VehicleAdapter(this.getActivity(), vehicles);
        //vehicleList.setAdapter(vehicleAdapter);
        return root;
    }
}