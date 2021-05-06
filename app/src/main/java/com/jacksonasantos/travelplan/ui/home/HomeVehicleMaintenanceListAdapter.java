package com.jacksonasantos.travelplan.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
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

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class HomeVehicleMaintenanceListAdapter extends RecyclerView.Adapter<HomeVehicleMaintenanceListAdapter.MyViewHolder> {

    private final List<Maintenance> mMaintenance;
    Context context;

    Globals g = Globals.getInstance();

    Locale locale = new Locale(g.getLanguage(), g.getCountry());
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ConstraintLayout llMaintenance;
        private final TextView txtDate;
        private final TextView txtValue;
        private final TextView txtStrOdometer;
        private final TextView txtOdometer;
        private final TextView txtDetail;

        public MyViewHolder(View v) {
            super(v);
            llMaintenance = v.findViewById(R.id.llMaintenance);
            txtDate = v.findViewById(R.id.txtDate);
            txtValue = v.findViewById(R.id.txtValue);
            txtStrOdometer = v.findViewById(R.id.txtStrOdometer);
            txtOdometer = v.findViewById(R.id.txtOdometer);
            txtDetail = v.findViewById(R.id.txtDetail);
        }

        @Override
        public void onClick(View view) {
        }
    }

    public HomeVehicleMaintenanceListAdapter(List<Maintenance> maintenance, Context context) {
        this.mMaintenance = maintenance;
        this.context = context;

        Database mdb = new Database(context);
        mdb.open();
    }

    @NonNull
    @Override
    public HomeVehicleMaintenanceListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View maintenanceView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_home_vehicle_item_maintenance, parent, false);

        return new MyViewHolder(maintenanceView);
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor", "DefaultLocale"})
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Maintenance maintenance = mMaintenance.get(position);
        if (position%2==0) {
            holder.llMaintenance.setBackgroundColor(Color.LTGRAY);
        } else {
            holder.llMaintenance.setBackgroundColor(Color.WHITE);
        }

        holder.txtDate.setText(Utils.dateToString(maintenance.getDate()));
        holder.txtValue.setText(currencyFormatter.format(maintenance.getValue()==0? BigDecimal.ZERO:maintenance.getValue()));
        holder.txtStrOdometer.setText(context.getString(R.string.Odometer)+ ": ");
        holder.txtOdometer.setText(String.format("%8d",maintenance.getOdometer()));
        holder.txtDetail.setText(maintenance.getDetail());
    }

    @Override
    public int getItemCount() {
        return mMaintenance.size();
    }
}