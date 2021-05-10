package com.jacksonasantos.travelplan.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Maintenance;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Utils;
import com.jacksonasantos.travelplan.ui.vehicle.MaintenanceActivity;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class HomeVehicleMaintenanceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private final List<Maintenance> mMaintenance;
    Context context;
    int show_header;

    Globals g = Globals.getInstance();

    Locale locale = new Locale(g.getLanguage(), g.getCountry());
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

    public HomeVehicleMaintenanceListAdapter(List<Maintenance> maintenance, Context context, int show_header) {
        this.mMaintenance = maintenance;
        this.context = context;
        this.show_header = show_header;

        Database mdb = new Database(context);
        mdb.open();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View maintenanceView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_home_vehicle_item_maintenance, parent, false);

        if (viewType == TYPE_ITEM) {
            return new ItemViewHolder(maintenanceView);
        } else if (viewType == TYPE_HEADER) {
            return new HeaderViewHolder(maintenanceView);
        }
        else return null;
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor", "DefaultLocale"})
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderViewHolder){
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

            headerViewHolder.llMaintenance.setBackgroundColor(Color.LTGRAY);
            headerViewHolder.txtDate.setText(R.string.Maintenance_Date);
            headerViewHolder.txtValue.setText(R.string.Maintenance_Value);
            headerViewHolder.txtStrOdometer.setText(R.string.Maintenance_Odometer);
            headerViewHolder.txtDetail.setText(R.string.Maintenance_Detail);
        }
        else if (holder instanceof ItemViewHolder) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            final Maintenance maintenance = mMaintenance.get(position-show_header);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            if (position % 2 == 0) {
                itemViewHolder.llMaintenance.setBackgroundColor(Color.LTGRAY);
            } else {
                itemViewHolder.llMaintenance.setBackgroundColor(Color.WHITE);
            }
            itemViewHolder.txtDate.setText(Utils.dateToString(maintenance.getDate()));
            itemViewHolder.txtValue.setText(currencyFormatter.format(maintenance.getValue()==0? BigDecimal.ZERO:maintenance.getValue()));
            itemViewHolder.txtStrOdometer.setText(context.getString(R.string.Odometer)+ ": ");
            itemViewHolder.txtOdometer.setText(String.format("%8d",maintenance.getOdometer()));
            itemViewHolder.txtDetail.setText(maintenance.getDetail());
            itemViewHolder.llMaintenance.setOnClickListener(v -> {
                Intent intent = new Intent (context, MaintenanceActivity.class);
                intent.putExtra("maintenance_id", maintenance.getId());
                context.startActivity(intent);
            });
        }
    }
    public int getItemViewType(int position) {
        if (position == 0 && show_header == 1) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mMaintenance.size()+show_header;
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final ConstraintLayout llMaintenance;
        private final TextView txtDate;
        private final TextView txtValue;
        private final TextView txtStrOdometer;
        private final TextView txtOdometer;
        private final TextView txtDetail;

        public HeaderViewHolder(View v) {
            super(v);
            llMaintenance = v.findViewById(R.id.llMaintenance);
            txtDate = v.findViewById(R.id.txtDate);
            txtValue = v.findViewById(R.id.txtValue);
            txtStrOdometer = v.findViewById(R.id.txtStrOdometer);
            txtOdometer = v.findViewById(R.id.txtOdometer);
            txtDetail = v.findViewById(R.id.txtDetail);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final ConstraintLayout llMaintenance;
        private final TextView txtDate;
        private final TextView txtValue;
        private final TextView txtStrOdometer;
        private final TextView txtOdometer;
        private final TextView txtDetail;

        public ItemViewHolder(View v) {
            super(v);
            llMaintenance = v.findViewById(R.id.llMaintenance);
            txtDate = v.findViewById(R.id.txtDate);
            txtValue = v.findViewById(R.id.txtValue);
            txtStrOdometer = v.findViewById(R.id.txtStrOdometer);
            txtOdometer = v.findViewById(R.id.txtOdometer);
            txtDetail = v.findViewById(R.id.txtDetail);
        }
    }
}