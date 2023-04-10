package com.jacksonasantos.travelplan.ui.vehicle;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.MaintenanceItem;
import com.jacksonasantos.travelplan.dao.MaintenancePlan;
import com.jacksonasantos.travelplan.dao.VehicleHasPlanQuery;
import com.jacksonasantos.travelplan.dao.general.Database;

import java.util.List;

public class VehiclePlanListAdapter extends RecyclerView.Adapter<VehiclePlanListAdapter.MyViewHolder> {

    private final List<VehicleHasPlanQuery> mVehicleHasPlan;
    final Context context;
    String[] measureArray;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final LinearLayout llService;
        public final ImageView imServiceType;
        public final TextView txtDescription;
        public final TextView txtExpirationDefault;
        public final TextView txtExpiration;
        public final ImageButton btnDelete;

        public MyViewHolder(View v) {
            super(v);
            llService = v.findViewById(R.id.llService);
            imServiceType = v.findViewById(R.id.imServiceType);
            txtDescription = v.findViewById(R.id.txtDescription);
            txtExpirationDefault = v.findViewById(R.id.txtExpirationDefault);
            txtExpiration = v.findViewById(R.id.txtExpiration);
            btnDelete = v.findViewById(R.id.btnDelete);
            llService.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }
    }

    public VehiclePlanListAdapter(List<VehicleHasPlanQuery> vehicleHasPlans, Context context) {
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
        final VehicleHasPlanQuery vehicleHasPlanQuery = mVehicleHasPlan.get(position);
        final MaintenancePlan maintenancePlan = Database.mMaintenancePlanDao.fetchMaintenancePlanById(vehicleHasPlanQuery.getMaintenance_plan_id());
        final MaintenanceItem maintenanceItem = new MaintenanceItem();

        holder.imServiceType.setImageResource(maintenanceItem.getServiceTypeImage(maintenancePlan.getService_type()));
        holder.txtDescription.setText(maintenancePlan.getDescription());
        holder.txtExpiration.setText(vehicleHasPlanQuery.getExpiration()==0?measureArray[maintenancePlan.getMeasure()]:vehicleHasPlanQuery.getExpiration()+" "+measureArray[maintenancePlan.getMeasure()]);
        holder.txtExpirationDefault.setText(String.valueOf(vehicleHasPlanQuery.getExpiration_default()==0?"":vehicleHasPlanQuery.getExpiration_default()));

        holder.llService.setOnClickListener (v -> {
            Intent intent = new Intent(context, VehicleHasPlanActivity.class);
            intent.putExtra("vehicle_has_plan_id", vehicleHasPlanQuery.getId());
            intent.putExtra("vehicle_has_plan_maintenance_plan_id", vehicleHasPlanQuery.getMaintenance_plan_id());
            intent.putExtra("vehicle_has_plan_vehicle_id", vehicleHasPlanQuery.getVehicle_id());
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

        holder.btnDelete.setOnClickListener (v -> new AlertDialog.Builder(v.getContext())
                .setTitle(R.string.Vehicle_Plan_Deleting)
                .setMessage(R.string.Msg_Confirm)
                .setPositiveButton(R.string.Yes, (dialogInterface, i) -> {
                    try {
                        Database.mVehicleHasPlanDao.deleteVehicleHasPlan(vehicleHasPlanQuery.getId());
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