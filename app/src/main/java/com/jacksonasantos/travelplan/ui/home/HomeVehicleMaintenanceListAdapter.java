package com.jacksonasantos.travelplan.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Database;
import com.jacksonasantos.travelplan.dao.Maintenance;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
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
        private final ImageView imServiceStatus;
        private final TextView txtDate;
        private final TextView txtValue;
        private final TextView txtOdometer;
        private final TextView txtDetail;
        private final ImageButton btnDone;

        public MyViewHolder(View v) {
            super(v);
            llMaintenance = v.findViewById(R.id.llMaintenance);
            imServiceStatus = v.findViewById(R.id.imServiceStatus);
            txtDate = v.findViewById(R.id.txtDate);
            txtValue = v.findViewById(R.id.txtValue);
            txtOdometer = v.findViewById(R.id.txtOdometer);
            txtDetail = v.findViewById(R.id.txtDetail);
            btnDone = v.findViewById(R.id.btnDone);
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Maintenance maintenance = mMaintenance.get(position);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        if (position%2==0) {
            holder.llMaintenance.setBackgroundColor(Color.rgb(209,193,233));
        } else {
            holder.llMaintenance.setBackgroundColor(Color.WHITE);
        }

        holder.imServiceStatus.setImageResource(R.drawable.ic_ball );
        holder.imServiceStatus.setColorFilter(maintenance.getColorStatus(maintenance.getStatus()), PorterDuff.Mode.MULTIPLY);
        holder.txtDate.setText(Utils.dateToString(maintenance.getDate()));
        holder.txtValue.setText(currencyFormatter.format(maintenance.getValue()==0? BigDecimal.ZERO:maintenance.getValue()));
        holder.txtOdometer.setText(String.valueOf(maintenance.getOdometer()));
        holder.txtDetail.setText(maintenance.getDetail());

        // btnDone - change Status for Service for completed and remove of list
        holder.btnDone.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Maintenance m1 = Database.mMaintenanceDao.fetchMaintenanceById(maintenance.getId());
                    m1.setStatus(2);  // executed
                    if (Database.mMaintenanceDao.updateMaintenance(m1)) {
                        mMaintenance.remove(position);
                        notifyDataSetChanged();
                    }
                }  catch (Exception e) {
                    Toast.makeText(context, R.string.Error_Changing_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMaintenance.size();
    }
}