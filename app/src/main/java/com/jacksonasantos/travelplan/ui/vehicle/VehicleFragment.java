package com.jacksonasantos.travelplan.ui.vehicle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jacksonasantos.travelplan.DAO.Vehicle;
import com.jacksonasantos.travelplan.R;

import java.util.List;

public class VehicleFragment extends Fragment {

    private VehicleViewModel vehicleViewModel;

    @Nullable
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_vehicle, container, false);
        ListView vehicleList = (ListView) root.findViewById(R.id.listVehicles);
        List<Vehicle> vehicles = null;

        VehicleAdapter vehicleAdapter = new VehicleAdapter(this.getActivity(), vehicles);
        vehicleList.setAdapter(vehicleAdapter);
        return root;
    }
}