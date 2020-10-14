package com.jacksonasantos.travelplan.ui.home;

import android.app.Activity;
import android.app.assist.AssistStructure;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Database;
import com.jacksonasantos.travelplan.dao.Vehicle;
import com.jacksonasantos.travelplan.ui.utility.Globals;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private TextView tvLicencePlate;
    private ImageView imVehicleType;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        Database mDb = new Database(getActivity());
        mDb.open();

        Spinner spVehicle = this.getView().findViewById(R.id.spVehicle);
        tvLicencePlate = this.getView().findViewById(R.id.tvLicencePlate);
        imVehicleType = this.getView().findViewById(R.id.imVehicleType);

        List<Vehicle> vehicles =  Database.mVehicleDao.fetchArrayVehicles();
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, vehicles);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spVehicle.setAdapter(adapter);
        spVehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Vehicle vehicle = (Vehicle) parent.getSelectedItem();
                tvLicencePlate.setText(vehicle.getLicense_plate());
                imVehicleType.setImageResource(vehicle.getTypeImage(vehicle.getType()));

                Globals g = Globals.getInstance();
                g.setIdVehicle(vehicle.getId());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mDb.close();
        adapter.notifyDataSetChanged();
    }
}