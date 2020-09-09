package com.jacksonasantos.travelplan.ui.vehicle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.DAO.Database;
import com.jacksonasantos.travelplan.DAO.Vehicle;
import com.jacksonasantos.travelplan.R;

import java.util.ArrayList;

public class VehicleFragment extends Fragment  {
    ArrayList<Vehicle> vehicles;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        Database mDb = new Database(getContext());
        mDb.open();

        super.onCreate(savedInstanceState);

        View root = inflater.inflate(R.layout.fragment_vehicle, container, false);
        RecyclerView listVehicles = (RecyclerView) root.findViewById(R.id.listVehicles);

        VehicleListAdapter adapter = new VehicleListAdapter(Database.mVehicleDao.fetchAllVehicles(), getContext());
        listVehicles.setAdapter(adapter);
        listVehicles.setLayoutManager(new LinearLayoutManager(getContext()));

        mDb.close();
        return root;
    }
}
