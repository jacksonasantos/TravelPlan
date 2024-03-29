package com.jacksonasantos.travelplan.ui.general;

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
import com.jacksonasantos.travelplan.dao.MaintenancePlanHasVehicleType;
import com.jacksonasantos.travelplan.dao.general.Database;

import java.util.List;

public class MaintenancePlanListAdapter extends RecyclerView.Adapter<MaintenancePlanListAdapter.MyViewHolder> {

    private final List<MaintenancePlan> mMaintenancePlan;
    final Context context;
    String[] measureArray;


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView imServiceType;
        public final TextView txtDescription;
        public final TextView txtExpiration_default;
        public final ImageButton btnEdit;
        public final ImageButton btnDelete;

        public MyViewHolder(View v) {
            super(v);
            imServiceType = v.findViewById(R.id.imServiceType);
            txtDescription = v.findViewById(R.id.txtDescription);
            txtExpiration_default = v.findViewById(R.id.txtExpiration_default);
            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
            btnEdit.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }
    }

    public MaintenancePlanListAdapter(List<MaintenancePlan> maintenancePlan, Context context) {
        this.mMaintenancePlan = maintenancePlan;
        this.context = context;

        Database mdb = new Database(context);
        mdb.open();
    }

    @NonNull
    @Override
    public MaintenancePlanListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View maintenancePlanView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_maintenance_plan, parent, false);
        measureArray = parent.getResources().getStringArray(R.array.measure_plan);

        return new MyViewHolder(maintenancePlanView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final MaintenancePlan maintenancePlan = mMaintenancePlan.get(position);
        final MaintenanceItem maintenanceItem = new MaintenanceItem();

        holder.imServiceType.setImageResource(maintenanceItem.getServiceTypeImage(maintenancePlan.getService_type()));
        holder.txtDescription.setText(maintenancePlan.getDescription());
        holder.txtExpiration_default.setText(maintenancePlan.getExpiration_default()==0?measureArray[maintenancePlan.getMeasure()]:maintenancePlan.getExpiration_default()+" "+measureArray[maintenancePlan.getMeasure()]);

        // btnEdit
        holder.btnEdit.setOnClickListener (v -> {
            Intent intent = new Intent (v.getContext(), MaintenancePlanActivity.class);
            intent.putExtra("maintenance_plan_id", maintenancePlan.getId());
            context.startActivity(intent);
            notifyItemChanged(position);
        });

        // btnDelete
        holder.btnDelete.setOnClickListener (v -> new AlertDialog.Builder(v.getContext())
                .setTitle(R.string.Maintenance_Plan_Deleting)
                .setMessage(R.string.Msg_Confirm)
                .setPositiveButton(R.string.Yes, (dialogInterface, i) -> {
                    try {
                        for ( MaintenancePlanHasVehicleType e : Database.mMaintenancePlanHasVehicleTypeDAO.fetchMaintenancePlanHasVehicleTypeByPlan(maintenancePlan.getId())){
                            Database.mMaintenancePlanHasVehicleTypeDAO.deleteMaintenancePlanHasVehicleType(e.getId());
                        }
                        Database.mMaintenancePlanDao.deleteMaintenancePlan(maintenancePlan.getId());
                        mMaintenancePlan.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mMaintenancePlan.size());
                    } catch (Exception e) {
                        Toast.makeText(context, context.getString(R.string.Error_Deleting_Data) + "\n" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton(R.string.No, null)
                .show());
    }

    @Override
    public int getItemCount() {
        return mMaintenancePlan.size();
    }
}