package com.jacksonasantos.travelplan.ui.travel;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Database;
import com.jacksonasantos.travelplan.dao.Vehicle;
import com.jacksonasantos.travelplan.dao.VehicleHasTravel;

import java.util.List;

public class VehicleTravelListAdapter extends RecyclerView.Adapter<VehicleTravelListAdapter.MyViewHolder> {

    private final List<VehicleHasTravel> mVehicleHasTravel;
    Context context;
    public String form;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtVehicle;
        public ImageButton btnDelete;

        public MyViewHolder(View v) {
            super(v);
            txtVehicle = v.findViewById(R.id.txtVehicle);
            btnDelete = v.findViewById(R.id.btnDelete);
            if (form.equals("Home")) {
                btnDelete.setVisibility(View.INVISIBLE);
            }
            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }
    }

    public VehicleTravelListAdapter(List<VehicleHasTravel> vehicleHasTravels, Context context, String form) {
        this.mVehicleHasTravel = vehicleHasTravels;
        this.context = context;
        this.form = form;
    }

    @NonNull
    @Override
    public VehicleTravelListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vehicleView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_vehicle_travel, parent, false);
        return new MyViewHolder(vehicleView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final VehicleHasTravel vehicleHasTravel = mVehicleHasTravel.get(position);
        final Vehicle vehicle = Database.mVehicleDao.fetchVehicleById(vehicleHasTravel.getVehicle_id());

        holder.txtVehicle.setText(vehicle.getName());

        // btnDelete
        holder.btnDelete.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle(R.string.Vehicle_Travel_Deleting)
                        .setMessage(R.string.Msg_Confirm)
                        .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    Database.mVehicleHasTravelDao.deleteVehicleHasTravel(vehicleHasTravel.getVehicle_id(),vehicleHasTravel.getTravel_id());
                                    mVehicleHasTravel.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position,mVehicleHasTravel.size());
                                } catch (Exception e) {
                                    Toast.makeText(context, context.getString(R.string.Error_Deleting_Data) + "\n" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }).setNegativeButton(R.string.No, null)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVehicleHasTravel.size();
    }
}