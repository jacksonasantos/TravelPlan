package com.jacksonasantos.travelplan.ui.travel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.vehicle.FuelSupplyActivity;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class VehicleTravelListAdapter extends RecyclerView.Adapter<VehicleTravelListAdapter.MyViewHolder> {

    private final List<VehicleHasTravel> mVehicleHasTravel;
    Context context;
    public String form;

    Globals g = Globals.getInstance();
    Locale locale = new Locale(g.getLanguage(), g.getCountry());
    NumberFormat numberFormatter = NumberFormat.getNumberInstance(locale);

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtVehicle;
        public TextView txtAvgConsumption;
        public ImageButton btnDelete;
        public ImageButton btnRefuel;

        public MyViewHolder(View v) {
            super(v);
            txtVehicle = v.findViewById(R.id.txtVehicle);
            txtAvgConsumption = v.findViewById(R.id.txtAvgConsumption);
            btnDelete = v.findViewById(R.id.btnDelete);
            btnRefuel = v.findViewById(R.id.btnRefuel);

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
    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final VehicleHasTravel vehicleHasTravel = mVehicleHasTravel.get(position);
        final Vehicle vehicle = Database.mVehicleDao.fetchVehicleById(vehicleHasTravel.getVehicle_id());

        holder.txtVehicle.setText(vehicle.getName());
        holder.txtAvgConsumption.setText(numberFormatter.format(vehicle.getAvg_consumption())+ " " +g.getMeasureConsumption());

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

        holder.btnRefuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), FuelSupplyActivity.class);
                intent.putExtra("vehicle_id", vehicleHasTravel.getVehicle_id());
                intent.putExtra("travel_id", vehicleHasTravel.getTravel_id());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVehicleHasTravel.size();
    }
}