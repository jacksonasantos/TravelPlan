package com.jacksonasantos.travelplan.ui.vehicle;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.NextMaintenanceItem;
import com.jacksonasantos.travelplan.dao.VehicleMaintenanceItem;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.List;

public class VehicleMaintenanceItemListAdapter extends RecyclerView.Adapter<VehicleMaintenanceItemListAdapter.MyViewHolder> {

    public final List<VehicleMaintenanceItem> mVehicle_maintenance_item;
    final Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView imServiceType;
        public final TextView txtDate;
        public final TextView txtDescription;
        public final TextView txtOdometer;
        public final TextView txtNote;

        public MyViewHolder(View v) {
            super(v);
            imServiceType = v.findViewById(R.id.imServiceType);
            txtDate = v.findViewById(R.id.txtDate);
            txtDescription = v.findViewById(R.id.txtDescription);
            txtOdometer = v.findViewById(R.id.txtOdometer);
            txtNote = v.findViewById(R.id.txtNote);
        }

        @Override
        public void onClick(View view) {
        }
    }

    public VehicleMaintenanceItemListAdapter(List<VehicleMaintenanceItem> vehicle_maintenance_item, Context context) {
        this.mVehicle_maintenance_item = vehicle_maintenance_item;
        this.context = context;

        Database mdb = new Database(context);
        mdb.open();
    }

    @NonNull
    @Override
    public VehicleMaintenanceItemListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vehicleMaintenanceItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_vehicle_maintenance_item, parent, false);

        return new MyViewHolder(vehicleMaintenanceItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final VehicleMaintenanceItem vehicle_maintenance_item = mVehicle_maintenance_item.get(position);

        holder.imServiceType.setImageResource(NextMaintenanceItem.getServiceTypeImage(vehicle_maintenance_item.getService_type()));
        holder.txtDate.setText(Utils.dateToString(vehicle_maintenance_item.getDate()));
        holder.txtDescription.setText(vehicle_maintenance_item.getDescription());
        holder.txtOdometer.setText(String.valueOf(vehicle_maintenance_item.getOdometer()));
        holder.txtNote.setText(vehicle_maintenance_item.getNote());
    }

    @Override
    public int getItemCount() {
        return mVehicle_maintenance_item.size();
    }
}