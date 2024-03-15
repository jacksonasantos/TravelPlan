package com.jacksonasantos.travelplan.ui.vehicle;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Maintenance;
import com.jacksonasantos.travelplan.dao.Vehicle;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class MaintenanceListAdapter extends RecyclerView.Adapter<MaintenanceListAdapter.MyViewHolder> {

    private final List<Maintenance> mMaintenance;
    final Context context;

    final Globals g = Globals.getInstance();
    final Locale locale = new Locale(g.getLanguage(), g.getCountry());
    final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
    final NumberFormat numberFormatter = NumberFormat.getNumberInstance(locale);

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ConstraintLayout clMaintenance;
        public final TextView txtMaintenanceDate;
        public final TextView txtVehicleName;
        public final TextView txtMaintenanceOdometer;
        public final TextView txtMaintenanceValue;
        public final TextView txtMaintenanceDetail;
        public final ImageButton btnDelete;

        public MyViewHolder(View v) {
            super(v);
            clMaintenance = v.findViewById(R.id.clMaintenance);
            txtMaintenanceDate = v.findViewById(R.id.txtMaintenanceDate);
            txtVehicleName = v.findViewById(R.id.txtVehicleName);
            txtMaintenanceOdometer = v.findViewById(R.id.txtMaintenanceOdometer);
            txtMaintenanceValue = v.findViewById(R.id.txtMaintenanceValue);
            txtMaintenanceDetail = v.findViewById(R.id.txtMaintenanceDetail);
            btnDelete = v.findViewById(R.id.btnDelete);
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
                .inflate(R.layout.fragment_item_maintenance, parent, false);

        return new MyViewHolder(maintenanceView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Maintenance maintenance = mMaintenance.get(position);

        Vehicle v = Database.mVehicleDao.fetchVehicleById(maintenance.getVehicle_id());
        holder.txtMaintenanceDate.setText(Utils.dateToString(maintenance.getDate()));
        holder.txtVehicleName.setText(v.getName());
        holder.txtMaintenanceValue.setText(currencyFormatter.format(maintenance.getValue()));
        holder.txtMaintenanceOdometer.setText(numberFormatter.format(maintenance.getOdometer()));
        holder.txtMaintenanceDetail.setText(maintenance.getDetail());

        // Edit
        holder.clMaintenance.setOnClickListener (v12 -> {
            Intent intent = new Intent (v12.getContext(), MaintenanceActivity.class);
            intent.putExtra("maintenance_id", maintenance.getId());
            context.startActivity(intent);
            notifyItemChanged(position);
        });

        // Delete
        holder.btnDelete.setOnClickListener (v1 -> new AlertDialog.Builder(v1.getContext())
                .setTitle(R.string.Maintenance_Deleting)
                .setMessage(R.string.Msg_Confirm)
                .setPositiveButton(R.string.Yes, (dialogInterface, i) -> {
                    try {
                        Database.mMaintenanceDao.deleteMaintenance(maintenance.getId());
                        mMaintenance.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mMaintenance.size());
                    } catch (Exception e) {
                        Toast.makeText(context, context.getString(R.string.Error_Deleting_Data) + "\n" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton(R.string.No, null)
                .show());
    }

    @Override
    public int getItemCount() {
        return mMaintenance.size();
    }
}