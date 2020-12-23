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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Database;
import com.jacksonasantos.travelplan.dao.Maintenance;
import com.jacksonasantos.travelplan.dao.MaintenancePlan;

import java.util.List;

public class MaintenancePlanListAdapter extends RecyclerView.Adapter<MaintenancePlanListAdapter.MyViewHolder> {

    private final List<MaintenancePlan> mMaintenancePlan;
    Context context;
    String[] measureArray;


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imServiceType;
        public TextView txtDescription;
        public TextView txtMeasure;
        public TextView txtExpiration;
        public ImageButton btnEdit;
        public ImageButton btnDelete;

        public MyViewHolder(View v) {
            super(v);
            imServiceType = v.findViewById(R.id.imServiceType);
            txtDescription = v.findViewById(R.id.txtDescription);
            txtMeasure = v.findViewById(R.id.txtMeasure);
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final MaintenancePlan maintenancePlan = mMaintenancePlan.get(position);
        final Maintenance maintenance = new Maintenance();

        holder.imServiceType.setImageResource(maintenance.getServiceTypeImage(maintenancePlan.getService_type()));
        holder.txtDescription.setText(maintenancePlan.getDescription());
        if (maintenancePlan.getExpiration()>0) {
            holder.txtExpiration.setText(String.valueOf(maintenancePlan.getExpiration()));
        } else {
            holder.txtExpiration.setText("");
        }
        holder.txtMeasure.setText(measureArray[maintenancePlan.getMeasure()]);

        // btnEdit
        holder.btnEdit.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), MaintenancePlanActivity.class);
                intent.putExtra("maintenance_plan_id", maintenancePlan.getId());
                context.startActivity(intent);
                notifyDataSetChanged();
            }
        });

        // btnDelete
        holder.btnDelete.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle(R.string.MaintenancePlan_Deleting)
                        .setMessage(R.string.Msg_Confirm)
                        .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    Database.mMaintenancePlanDao.deleteMaintenancePlan(maintenancePlan.getId());
                                    mMaintenancePlan.remove(position);
                                    notifyItemRemoved(position);
                                } catch (Exception e) {
                                    Toast.makeText(context, context.getString(R.string.Error_Deleting_Data) + "\n" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }).setNegativeButton(R.string.No, null)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMaintenancePlan.size();
    }
}