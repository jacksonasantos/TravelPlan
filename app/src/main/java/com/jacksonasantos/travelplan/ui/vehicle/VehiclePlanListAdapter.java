package com.jacksonasantos.travelplan.ui.vehicle;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.MaintenanceItem;
import com.jacksonasantos.travelplan.dao.MaintenancePlan;
import com.jacksonasantos.travelplan.dao.VehicleHasPlan;
import com.jacksonasantos.travelplan.dao.general.Database;

import java.util.List;

public class VehiclePlanListAdapter extends RecyclerView.Adapter<VehiclePlanListAdapter.MyViewHolder> {

    private final List<VehicleHasPlan> mVehicleHasPlan;
    private Integer nrSpinService_description = 0;
    final Context context;
    String[] measureArray;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView imServiceType;
        public final TextView txtDescription;
        public final TextView txtExpiration;
        public final ImageButton btnEdit;
        public final ImageButton btnDelete;

        public MyViewHolder(View v) {
            super(v);
            imServiceType = v.findViewById(R.id.imServiceType);
            txtDescription = v.findViewById(R.id.txtDescription);
            txtExpiration = v.findViewById(R.id.txtExpiration);
            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
            btnEdit.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }
    }

    public VehiclePlanListAdapter(List<VehicleHasPlan> vehicleHasPlans, Context context) {
        this.mVehicleHasPlan = vehicleHasPlans;
        this.context = context;
    }

    @NonNull
    @Override
    public VehiclePlanListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vehicleView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_vehicle_plan, parent, false);
        measureArray = parent.getResources().getStringArray(R.array.measure_plan);
        return new MyViewHolder(vehicleView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final VehicleHasPlan vehicleHasPlan = mVehicleHasPlan.get(position);
        final MaintenancePlan maintenancePlan = Database.mMaintenancePlanDao.fetchMaintenancePlanById(vehicleHasPlan.getMaintenance_plan_id());
        final MaintenanceItem maintenanceItem = new MaintenanceItem();

        holder.imServiceType.setImageResource(maintenanceItem.getServiceTypeImage(maintenancePlan.getService_type()));
        holder.txtDescription.setText(maintenancePlan.getDescription());
        holder.txtExpiration.setText(vehicleHasPlan.getExpiration()==0?measureArray[maintenancePlan.getMeasure()]:vehicleHasPlan.getExpiration()+" "+measureArray[maintenancePlan.getMeasure()]);

        // btnEdit
        holder.btnEdit.setOnClickListener (v -> {
            Intent intent = new Intent(context, VehicleHasPlanActivity.class);
            intent.putExtra("vehicle_has_plan_id", vehicleHasPlan.getId());
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

        // btnDelete
        holder.btnDelete.setOnClickListener (v -> new AlertDialog.Builder(v.getContext())
                .setTitle(R.string.Vehicle_Plan_Deleting)
                .setMessage(R.string.Msg_Confirm)
                .setPositiveButton(R.string.Yes, (dialogInterface, i) -> {
                    try {
                        Database.mVehicleHasPlanDao.deleteVehicleHasPlan(vehicleHasPlan.getId());
                        mVehicleHasPlan.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mVehicleHasPlan.size());
                    } catch (Exception e) {
                        Toast.makeText(context, context.getString(R.string.Error_Deleting_Data) + "\n" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton(R.string.No, null)
                .show());
    }

    @Override
    public int getItemCount() {
        return mVehicleHasPlan.size();
    }
}