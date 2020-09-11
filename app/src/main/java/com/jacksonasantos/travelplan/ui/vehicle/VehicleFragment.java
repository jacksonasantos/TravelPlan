package com.jacksonasantos.travelplan.ui.vehicle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.dao.Database;
import com.jacksonasantos.travelplan.R;

public class VehicleFragment extends Fragment  {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       return inflater.inflate(R.layout.fragment_vehicle, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        Database mDb = new Database(getContext());
        mDb.open();

        RecyclerView listVehicles = (RecyclerView) this.getView().findViewById(R.id.listVehicles);
        VehicleListAdapter adapter = new VehicleListAdapter(Database.mVehicleDao.fetchAllVehicles(), getContext());
        listVehicles.setAdapter(adapter);
        listVehicles.setLayoutManager(new LinearLayoutManager(getContext()));

        mDb.close();
        adapter.notifyDataSetChanged();
    }

}
