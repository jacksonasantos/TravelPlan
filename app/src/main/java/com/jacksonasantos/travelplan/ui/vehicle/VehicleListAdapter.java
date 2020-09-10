package com.jacksonasantos.travelplan.ui.vehicle;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.DAO.Database;
import com.jacksonasantos.travelplan.DAO.Vehicle;
import com.jacksonasantos.travelplan.R;

import java.util.List;

public class VehicleListAdapter extends RecyclerView.Adapter<VehicleListAdapter.MyViewHolder> {

    private List<Vehicle> mVehicle;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imType;
        public TextView txtPlate;
        public TextView txtShortName;
        public TextView txtAVG;
        public ImageButton btnEdit;
        public ImageButton btnDelete;

        public MyViewHolder(View v) {
            super(v);
            imType = v.findViewById(R.id.imType);
            txtPlate = (TextView) v.findViewById(R.id.txtPlate);
            txtShortName = (TextView) v.findViewById(R.id.txtShortName);
            txtAVG = (TextView) v.findViewById(R.id.txtAVG);
            btnEdit = (ImageButton) v.findViewById(R.id.btnEdit);
            btnDelete = (ImageButton) v.findViewById(R.id.btnDelete);
            btnEdit.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
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

    @Override
    public VehicleListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vehicleView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_vehicle_item, parent, false);

        return new MyViewHolder(vehicleView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Vehicle vehicle = mVehicle.get(position);

        int S = Math.toIntExact(vehicle.getType());
        switch(S) {
            case 2131296525:
                holder.imType.setImageResource(R.drawable.ic_vehicle_car);
                break;
            case 2131296526:
                holder.imType.setImageResource(R.drawable.ic_vehicle_motorcycle);
                break;
            case 2131296527:
                holder.imType.setImageResource(R.drawable.ic_vehicle_suv);
                break;
            default:
                break;
        }
        holder.txtPlate.setText(vehicle.getLicense_plate());
        holder.txtShortName.setText(vehicle.getShort_name());
        holder.txtAVG.setText(Double.toString(vehicle.getAvg_consumption())+" Km/L");

        // btnEdit
        holder.btnEdit.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), VehicleActivity.class);
                intent.putExtra("id", vehicle.getId());
                intent.putExtra("type", vehicle.getType());
                intent.putExtra("name", vehicle.getName());
                intent.putExtra("short_name", vehicle.getShort_name());
                intent.putExtra("license_plate", vehicle.getLicense_plate());
                intent.putExtra("brand", vehicle.getBrand());
                intent.putExtra("type_fuel", vehicle.getType_fuel());
                intent.putExtra("full_capacity", vehicle.getFull_capacity());
                intent.putExtra("avg_consumption", vehicle.getAvg_consumption());
                intent.putExtra("dt_acquisition", vehicle.getDt_acquisition());
                intent.putExtra("dt_sale", vehicle.getDt_sale());
                intent.putExtra("dt_odometer", vehicle.getDt_odometer());
                intent.putExtra("odometer", vehicle.getOdometer());
                context.startActivity(intent);
                notifyDataSetChanged();
            }
        });

        // btnDelete
        holder.btnDelete.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle(R.string.Vehicle_Deleting)
                        .setMessage(R.string.Msg_Confirm)
                        .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Database.mVehicleDao.deleteVehicle((int)vehicle.getId());  // invoca a deleção do registro
                                mVehicle.remove(position);
                                notifyItemRemoved(position);
                            }
                        }).setNegativeButton(R.string.No, null)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVehicle.size();
    }
}