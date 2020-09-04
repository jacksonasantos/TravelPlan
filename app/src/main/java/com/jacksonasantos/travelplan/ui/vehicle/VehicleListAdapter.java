package com.jacksonasantos.travelplan.ui.vehicle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.DAO.Vehicle;
import com.jacksonasantos.travelplan.R;

import java.util.List;

public class VehicleListAdapter extends RecyclerView.Adapter<VehicleListAdapter.MyViewHolder> {

    public class MyViewHolder extends RecyclerView.ViewHolder {
    // each data item is just a string in this case
        public TextView textView;

        public MyViewHolder(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.txtNome);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    private List<Vehicle> mVehicle;

    public VehicleListAdapter(List<Vehicle> vehicles) {
        mVehicle = vehicles;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public VehicleListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        Context context = parent.getContext();

        // create a new view
        LayoutInflater inflater = LayoutInflater.from(context);

        View vehicleView = inflater.inflate(R.layout.fragment_vehicle_item, parent, false);
        MyViewHolder vh = new MyViewHolder(vehicleView);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Vehicle vehicle = mVehicle.get(position);

        TextView textView = holder.textView;
        textView.setText(vehicle.getName());

        //buttons

    }

    @Override
    public int getItemCount() {
        return mVehicle.size();
    }
}