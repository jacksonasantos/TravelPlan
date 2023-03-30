package com.jacksonasantos.travelplan.ui.general;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.MaintenancePlanHasVehicleType;
import com.jacksonasantos.travelplan.dao.general.Database;

import java.util.List;

public class MaintenancePlanHasVehicleTypeListAdapter extends RecyclerView.Adapter<MaintenancePlanHasVehicleTypeListAdapter.MyViewHolder> {

    private final List<MaintenancePlanHasVehicleType> mMaintenancePlanHasVehicleType;
    final Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView txtVehicleType;
        public final SwitchMaterial cbRecurringService;

        public MyViewHolder(View v) {
            super(v);
            txtVehicleType = v.findViewById(R.id.txtVehicleType);
            cbRecurringService = v.findViewById(R.id.cbRecurringService);
        }

        @Override
        public void onClick(View view) {
        }
    }

    public MaintenancePlanHasVehicleTypeListAdapter(List<MaintenancePlanHasVehicleType> maintenancePlanHasVehicleType, Context context) {
        this.mMaintenancePlanHasVehicleType = maintenancePlanHasVehicleType;
        this.context = context;

        Database mdb = new Database(context);
        mdb.open();
    }

    @NonNull
    @Override
    public MaintenancePlanHasVehicleTypeListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View maintenancePlanHasVehicleTypeView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_maintenance_plan_item_list, parent, false);

        return new MyViewHolder(maintenancePlanHasVehicleTypeView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final MaintenancePlanHasVehicleType maintenancePlanHasVehicleType = mMaintenancePlanHasVehicleType.get(position);

        holder.txtVehicleType.setText(context.getResources().getStringArray(R.array.vehicle_type_array)[maintenancePlanHasVehicleType.getService_type()]);
        holder.cbRecurringService.setChecked(maintenancePlanHasVehicleType.getRecurring_service()==1);
        holder.cbRecurringService.setOnClickListener (v -> mMaintenancePlanHasVehicleType.get(position).setRecurring_service(!holder.cbRecurringService.isChecked()?0:1));
    }

    @Override
    public int getItemCount() {
        return mMaintenancePlanHasVehicleType.size();
    }
}