package com.jacksonasantos.travelplan.ui.vehicle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.MaintenanceItem;
import com.jacksonasantos.travelplan.dao.PendingVehicle;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.Globals;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class MaintenanceItemListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public final List<MaintenanceItem> mMaintenanceItem;
    final Context context;
    final int show_header;
    String[] measureArray;

    final Globals g = Globals.getInstance();
    final Locale locale = new Locale(g.getLanguage(), g.getCountry());
    final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

    public MaintenanceItemListAdapter(List<MaintenanceItem> maintenanceItem, Context context, int show_header) {
        this.mMaintenanceItem = maintenanceItem;
        this.context = context;
        this.show_header = show_header;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View maintenanceItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_maintenance_item_list, parent, false);
        measureArray = parent.getResources().getStringArray(R.array.measure_plan);
        if (viewType == TYPE_HEADER) {
            return new HeaderViewHolder(maintenanceItemView);
        } else return new ItemViewHolder(maintenanceItemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderViewHolder){
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

            headerViewHolder.llMaintenanceItem.setBackgroundColor(Color.LTGRAY);
            headerViewHolder.imgServiceType.setVisibility(View.INVISIBLE);
            headerViewHolder.txtMaintenancePlanItem.setText(R.string.Maintenance_Plan_Description);
            headerViewHolder.txtNote.setText(R.string.MaintenanceItem_Note);
            headerViewHolder.txtValue.setText(R.string.MaintenanceItem_Value);
            headerViewHolder.txtExpiration.setText(R.string.MaintenanceItem_Expiration);
            headerViewHolder.btnDelete.setVisibility(View.INVISIBLE);
        }
        else if (holder instanceof ItemViewHolder) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            final MaintenanceItem maintenanceItem = mMaintenanceItem.get(position-show_header);

            String x = Database.mMaintenancePlanDao.fetchMaintenancePlanById(maintenanceItem.getMaintenance_plan_id()).getDescription();

            itemViewHolder.imgServiceType.setImageResource(maintenanceItem.getServiceTypeImage(maintenanceItem.getService_type()));
            itemViewHolder.txtMaintenancePlanItem.setText(x);
            if (!maintenanceItem.getNote().isEmpty()) {
                itemViewHolder.txtNote.setText("(" + maintenanceItem.getNote() + ")");
            }
            itemViewHolder.txtValue.setText(currencyFormatter.format(maintenanceItem.getValue() == null ? BigDecimal.ZERO : maintenanceItem.getValue()));
            int nrSpinMeasure = maintenanceItem.getMeasure_type();
            if (nrSpinMeasure == 0) {
                itemViewHolder.txtExpiration.setText(context.getResources().getStringArray(R.array.measure_plan)[nrSpinMeasure]);
            } else {
                itemViewHolder.txtExpiration.setText(maintenanceItem.getExpiration_value() + " " + context.getResources().getStringArray(R.array.measure_plan)[nrSpinMeasure]);
            }

            // btnDelete
            itemViewHolder.btnDelete.setOnClickListener(v -> new AlertDialog.Builder(v.getContext())
                    .setTitle(R.string.MaintenanceItem_Deleting)
                    .setMessage(R.string.Msg_Confirm)
                    .setPositiveButton(R.string.Yes, (dialogInterface, i) -> {
                        try {
                            if (maintenanceItem.getPending_vehicle_id()>0) {
                                PendingVehicle pendingVehicle = Database.mPendingVehicleDao.fetchPendingVehicleById(maintenanceItem.getPending_vehicle_id());
                                pendingVehicle.setStatus_pending(0);
                                Database.mPendingVehicleDao.updatePendingVehicle(pendingVehicle);
                            }
                            Database.mMaintenanceItemDao.deleteMaintenanceItem(maintenanceItem.getMaintenance_id(), maintenanceItem.getId());
                            mMaintenanceItem.remove(position-show_header);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, mMaintenanceItem.size());
                        } catch (Exception e) {
                            Toast.makeText(context, context.getString(R.string.Error_Deleting_Data) + "\n" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    }).setNegativeButton(R.string.No, null)
                    .show()
            );
        }
    }
    @Override
    public int getItemViewType(int position) {
        if (position == 0 && show_header == 1) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mMaintenanceItem.size()+show_header;
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final ConstraintLayout llMaintenanceItem;
        private final ImageView imgServiceType;
        private final TextView txtMaintenancePlanItem;
        private final TextView txtExpiration;
        private final TextView txtValue;
        private final TextView txtNote;
        private final ImageButton btnDelete;

        public HeaderViewHolder(View v) {
            super(v);
            llMaintenanceItem = v.findViewById(R.id.llMaintenanceItem);
            imgServiceType = v.findViewById(R.id.imgServiceType);
            txtMaintenancePlanItem = v.findViewById(R.id.txtMaintenancePlanItem);
            txtExpiration = v.findViewById(R.id.txtExpiration);
            txtNote = v.findViewById(R.id.txtNote);
            txtValue = v.findViewById(R.id.txtValue);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        //private final ConstraintLayout llMaintenanceItem;
        private final ImageView imgServiceType;
        private final TextView txtMaintenancePlanItem;
        private final TextView txtExpiration;
        private final TextView txtValue;
        private final TextView txtNote;
        private final ImageButton btnDelete;

        public ItemViewHolder(View v) {
            super(v);
            //llMaintenanceItem = v.findViewById(R.id.llMaintenanceItem);
            imgServiceType = v.findViewById(R.id.imgServiceType);
            txtMaintenancePlanItem = v.findViewById(R.id.txtMaintenancePlanItem);
            txtExpiration = v.findViewById(R.id.txtExpiration);
            txtNote = v.findViewById(R.id.txtNote);
            txtValue = v.findViewById(R.id.txtValue);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }
}