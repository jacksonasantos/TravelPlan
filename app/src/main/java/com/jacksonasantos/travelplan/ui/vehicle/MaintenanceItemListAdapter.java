package com.jacksonasantos.travelplan.ui.vehicle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
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
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.dao.MaintenanceItem;
import com.jacksonasantos.travelplan.ui.utility.Globals;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class MaintenanceItemListAdapter extends RecyclerView.Adapter<MaintenanceItemListAdapter.MyViewHolder> {

    public final List<MaintenanceItem> mMaintenanceItem;
    Context context;
    String[] measureArray;

    Globals g = Globals.getInstance();
    Locale locale = new Locale(g.getLanguage(), g.getCountry());
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

    public MaintenanceItemListAdapter(List<MaintenanceItem> maintenanceItem, Context context) {
        this.mMaintenanceItem = maintenanceItem;
        this.context = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView imgServiceType;
        private final TextView txtMaintenancePlanItem;
        private final TextView txtExpiration;
        private final TextView txtValue;
        private final TextView txtNote;
        private final ImageButton btnDelete;

        public MyViewHolder(View v) {
            super(v);
            imgServiceType = v.findViewById(R.id.imgServiceType);
            txtMaintenancePlanItem = v.findViewById(R.id.txtMaintenancePlanItem);
            txtExpiration = v.findViewById(R.id.txtExpiration);
            txtNote = v.findViewById(R.id.txtNote);
            txtValue = v.findViewById(R.id.txtValue);
            btnDelete = v.findViewById(R.id.btnDelete);

            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }
    }

    @NonNull
    @Override
    public MaintenanceItemListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View maintenanceItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_maintenance_item_list, parent, false);
        measureArray = parent.getResources().getStringArray(R.array.measure_plan);
        return new MyViewHolder(maintenanceItemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final MaintenanceItem maintenanceItem = mMaintenanceItem.get(position);

        String x = Database.mMaintenancePlanDao.fetchMaintenancePlanById(maintenanceItem.getMaintenance_plan_id()).getDescription();

        holder.imgServiceType.setImageResource(maintenanceItem.getServiceTypeImage(maintenanceItem.getService_type()));
        holder.txtMaintenancePlanItem.setText(x);
        if (!maintenanceItem.getNote().isEmpty()) {
            holder.txtNote.setText("(" + maintenanceItem.getNote() + ")");
        }
        holder.txtValue.setText(currencyFormatter.format(maintenanceItem.getValue()==null? BigDecimal.ZERO: maintenanceItem.getValue()));
        int nrSpinMeasure = maintenanceItem.getMeasure_type();
        if (nrSpinMeasure == 0) {
            holder.txtExpiration.setText(context.getResources().getStringArray(R.array.measure_plan)[nrSpinMeasure]);
        } else {
            holder.txtExpiration.setText(maintenanceItem.getExpiration_value() + " " + context.getResources().getStringArray(R.array.measure_plan)[nrSpinMeasure]);
        }

        // btnDelete
        holder.btnDelete.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle(R.string.MaintenanceItem_Deleting)
                        .setMessage(R.string.Msg_Confirm)
                        .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    Database.mMaintenanceItemDao.deleteMaintenanceItem(maintenanceItem.getMaintenance_id(), maintenanceItem.getId());
                                    mMaintenanceItem.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position,mMaintenanceItem.size());
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
        return mMaintenanceItem.size();
    }
}