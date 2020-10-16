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

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Database;
import com.jacksonasantos.travelplan.dao.Maintenance;
import com.jacksonasantos.travelplan.dao.Vehicle;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class MaintenanceListAdapter extends RecyclerView.Adapter<MaintenanceListAdapter.MyViewHolder> {

    private List<Maintenance> mMaintenance;
    Context context;
    String[] serviceArray;

    Locale locale = new Locale("pt", "BR"); // TODO - disponibilizar local dinamico

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtMaintenanceDate;
        public TextView txtVehicleName;
        public TextView txtType;
        public ImageView imServiceType;
        public TextView txtMaintenanceValue;
        public ImageButton btnEdit;
        public ImageButton btnDelete;

        public MyViewHolder(View v) {
            super(v);
            txtMaintenanceDate = v.findViewById(R.id.txtMaintenanceDate);
            txtVehicleName = v.findViewById(R.id.txtVehicleName);
            txtType = v.findViewById(R.id.txtType);
            imServiceType = v.findViewById(R.id.imServiceType);
            txtMaintenanceValue = v.findViewById(R.id.txtMaintenanceValue);
            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
            btnEdit.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }
    }

    public MaintenanceListAdapter(List<Maintenance> maintenance, Context context) {
        this.mMaintenance = maintenance;
        this.context = context;

        Database mdb = new Database(context);
        mdb.open();
    }

    @NonNull
    @Override
    public MaintenanceListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View maintenanceView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_maintenance_item, parent, false);
        serviceArray = parent.getResources().getStringArray(R.array.vehicle_services);

        return new MyViewHolder(maintenanceView);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Maintenance maintenance = mMaintenance.get(position);

        holder.txtMaintenanceDate.setText(Utils.dateToString(maintenance.getDate()));
        Vehicle v = Database.mVehicleDao.fetchVehicleById(maintenance.getVehicle_id());
        holder.txtVehicleName.setText(v.getName());
        holder.txtType.setText(serviceArray[maintenance.getType()]);
        holder.imServiceType.setImageResource(maintenance.getTypeImage(maintenance.getType()));
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        holder.txtMaintenanceValue.setText(currencyFormatter.format(maintenance.getValue()));

        // btnEdit
        holder.btnEdit.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), MaintenanceActivity.class);
                intent.putExtra("maintenance_id", maintenance.getId());
                context.startActivity(intent);
                notifyDataSetChanged();
            }
        });

        // btnDelete
        holder.btnDelete.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle(R.string.Maintenance_Deleting)
                        .setMessage(R.string.Msg_Confirm)
                        .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Database.mMaintenanceDao.deleteMaintenance(maintenance.getId());
                                mMaintenance.remove(position);
                                notifyItemRemoved(position);
                            }
                        }).setNegativeButton(R.string.No, null)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMaintenance.size();
    }
}