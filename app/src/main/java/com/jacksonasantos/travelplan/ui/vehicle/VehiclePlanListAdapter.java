package com.jacksonasantos.travelplan.ui.vehicle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
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
            LayoutInflater li = LayoutInflater.from(v.getContext());
            View promptsView = li.inflate(R.layout.dialog_vehicle_plan, null);

            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
            alertDialogBuilder.setView(promptsView);

            final Spinner spinService_description = promptsView.findViewById(R.id.spinService_description);
            final TextView tvRecommendation = promptsView.findViewById(R.id.tvRecommendation);
            final TextView tvMeasure = promptsView.findViewById(R.id.tvMeasure);
            final TextView tvExpirationNumber = promptsView.findViewById(R.id.tvExpirationNumber);
            final EditText etExpirationNumber = promptsView.findViewById(R.id.etExpirationNumber);

            final List<MaintenancePlan> maintenancePlans =  Database.mMaintenancePlanDao.fetchArrayMaintenancePlan();
            maintenancePlans.add(0, new MaintenancePlan());
            maintenancePlans.get(0).setId(0);
            ArrayAdapter<MaintenancePlan> adapterT = new ArrayAdapter<>(li.getContext(), android.R.layout.simple_spinner_item, maintenancePlans);
            adapterT.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
            spinService_description.setAdapter(adapterT);

            spinService_description.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long idx) {
                    MaintenancePlan mp1 = (MaintenancePlan) parent.getSelectedItem();
                    nrSpinService_description = mp1.getId();
                    tvRecommendation.setText(mp1.getRecommendation());
                    tvExpirationNumber.setText(String.valueOf(mp1.getExpiration_default()));
                    tvMeasure.setText(context.getResources().getStringArray(R.array.measure_plan)[mp1.getMeasure()]);
                    spinService_description.setSelection(position);
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    nrSpinService_description = 0;
                }
            });
            adapterT.notifyDataSetChanged();

            nrSpinService_description = vehicleHasPlan.getMaintenance_plan_id();
            tvRecommendation.setText(maintenancePlan.getRecommendation());
            tvExpirationNumber.setText(String.valueOf(maintenancePlan.getExpiration_default()));
            tvMeasure.setText(context.getResources().getStringArray(R.array.measure_plan)[maintenancePlan.getMeasure()]);
            etExpirationNumber.setText(String.valueOf(vehicleHasPlan.getExpiration()));

            if (nrSpinService_description > 0) {
                MaintenancePlan mp1 = Database.mMaintenancePlanDao.fetchMaintenancePlanById(nrSpinService_description);
                for (int x = 1; x <= spinService_description.getAdapter().getCount(); x++) {
                    if (spinService_description.getAdapter().getItem(x).toString().equals(mp1.getDescription())) {
                        spinService_description.setSelection(x);
                        nrSpinService_description = mp1.getId();
                        break;
                    }
                }
            }
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton(R.string.OK, (dialog, id) -> {
                        boolean isSave = false;

                        if (!etExpirationNumber.getText().toString().isEmpty() && (!etExpirationNumber.getText().toString().equals(tvExpirationNumber.getText().toString()))) {
                            vehicleHasPlan.setMaintenance_plan_id(nrSpinService_description);
                            vehicleHasPlan.setExpiration(Integer.parseInt(etExpirationNumber.getText().toString()));
                        }

                        try {
                            isSave = Database.mVehicleHasPlanDao.updateVehicleHasPlan(vehicleHasPlan);
                            notifyItemChanged(position);
                        } catch (Exception e) {
                            Toast.makeText(context, R.string.Error_Changing_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        if (!isSave) {
                            Toast.makeText(context, R.string.Error_Saving_Data, Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton(R.string.Cancel, (dialog, id) -> dialog.cancel());
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
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