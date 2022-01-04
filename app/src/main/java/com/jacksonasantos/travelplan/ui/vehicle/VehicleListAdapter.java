package com.jacksonasantos.travelplan.ui.vehicle;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Vehicle;
import com.jacksonasantos.travelplan.dao.general.Database;

import java.util.List;

public class VehicleListAdapter extends RecyclerView.Adapter<VehicleListAdapter.MyViewHolder> {

    public final List<Vehicle> mVehicle;
    final Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView imVehicleType;
        public final TextView txtPlate;
        public final TextView txtShortName;
        public final ImageButton btnEdit;
        public final ImageButton btnDelete;
        public final ImageButton btnRefuel;
        public final ImageButton btnPlan;

        public MyViewHolder(View v) {
            super(v);
            imVehicleType = v.findViewById(R.id.imVehicleType);
            txtPlate = v.findViewById(R.id.txtPlate);
            txtShortName = v.findViewById(R.id.txtShortName);
            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
            btnRefuel = v.findViewById(R.id.btnRefuel);
            btnPlan = v.findViewById(R.id.btnPlan);
            btnEdit.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
            btnRefuel.setOnClickListener(this);
            btnPlan.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }
    }

    public VehicleListAdapter(List<Vehicle> vehicles, Context context) {
        this.mVehicle = vehicles;
        this.context = context;

        Database mdb = new Database(context);
        mdb.open();
    }

    @NonNull
    @Override
    public VehicleListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vehicleView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_vehicle, parent, false);

        return new MyViewHolder(vehicleView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Vehicle vehicle = mVehicle.get(position);

        holder.imVehicleType.setColorFilter(vehicle.getColor_code());
        holder.imVehicleType.setImageResource(vehicle.getVehicleTypeImage(vehicle.getVehicle_type()));
        holder.txtPlate.setText(vehicle.getLicense_plate());
        holder.txtShortName.setText(vehicle.getShort_name());

        // btnPlan
        holder.btnPlan.setOnClickListener (v -> {
            Intent intent = new Intent (v.getContext(), VehiclePlanActivity.class);
            intent.putExtra("vehicle_id", vehicle.getId());
            context.startActivity(intent);
        });

        // btnRefuel
        holder.btnRefuel.setOnClickListener (v -> {
            Intent intent = new Intent (v.getContext(), FuelSupplyActivity.class);
            intent.putExtra("vehicle_id", vehicle.getId());
            context.startActivity(intent);
        });

        // btnEdit
        holder.btnEdit.setOnClickListener (v -> {
            Intent intent = new Intent (v.getContext(), VehicleActivity.class);
            intent.putExtra("id", vehicle.getId());
            context.startActivity(intent);
            notifyItemChanged(position);
        });

        // btnDelete
        holder.btnDelete.setOnClickListener (v -> new AlertDialog.Builder(v.getContext())
                .setTitle(R.string.Vehicle_Deleting)
                .setMessage(R.string.Msg_Confirm)
                .setPositiveButton(R.string.Yes, (dialogInterface, i) -> {
                    try {
                        Database.mVehicleDao.deleteVehicle(vehicle.getId());
                        mVehicle.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mVehicle.size());
                    } catch (Exception e) {
                        Toast.makeText(context, context.getString(R.string.Error_Deleting_Data) + "\n" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton(R.string.No, null)
                .show());
    }

    @Override
    public int getItemCount() {
        return mVehicle.size();
    }
}