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
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

public class HomeVehicleMaintenanceListAdapter extends RecyclerView.Adapter<HomeVehicleMaintenanceListAdapter.MyViewHolder> {

    private final List<Maintenance> mMaintenance;
    Context context;
    String[] typeArray;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ConstraintLayout llMaintenanceItem;
        private final ImageView imServiceType;
        private final ImageView imServiceExpired;
        private final TextView txtMaintenanceExpirationDate;
        private final TextView txtMaintenanceOdometer;
        private final TextView txtServiceType;
        private final ImageButton btnDone;

        public MyViewHolder(View v) {
            super(v);
            llMaintenanceItem = v.findViewById(R.id.llMaintenanceItem);
            imServiceType = v.findViewById(R.id.imServiceType);
            imServiceExpired = v.findViewById(R.id.imServiceExpired);
            txtMaintenanceExpirationDate = v.findViewById(R.id.txtMaintenanceExpirationDate);
            txtMaintenanceOdometer = v.findViewById(R.id.txtMaintenanceOdometer);
            txtServiceType = v.findViewById(R.id.txtServiceType);
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
        typeArray = parent.getResources().getStringArray(R.array.vehicle_services);

        return new MyViewHolder(maintenanceView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    //@SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Maintenance maintenance = mMaintenance.get(position);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        if (position%2==0) {
            holder.llMaintenanceItem.setBackgroundColor(Color.rgb(209,193,233));
        } else {
            holder.llMaintenanceItem.setBackgroundColor(Color.WHITE);
        }
        holder.imServiceType.setImageResource(maintenance.getServiceTypeImage(maintenance.getService_type()));
        holder.imServiceExpired.setImageResource(R.drawable.ic_ball );
        try {
            if (maintenance.getStatus() == 1) {
                holder.imServiceExpired.setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);
            } else {
                if (!(maintenance.getExpiration_date() == null)) { // TODO - VER ISSO sdf
                    if (System.currentTimeMillis() < Objects.requireNonNull(sdf.parse(Objects.requireNonNull(Utils.dateToString(maintenance.getExpiration_date())))).getTime()) {
                        holder.imServiceExpired.setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                    } else {
                        holder.imServiceExpired.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.txtMaintenanceExpirationDate.setText(Utils.dateToString(maintenance.getExpiration_date()));
        holder.txtMaintenanceOdometer.setText(String.valueOf(maintenance.getExpiration_km()));
        holder.txtServiceType.setText(typeArray[maintenance.getService_type()]);

        // btnDone - change Status for Service for completed and remove of list
        holder.btnDone.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Maintenance m1 = Database.mMaintenanceDao.fetchMaintenanceById(maintenance.getId());
                    m1.setStatus(maintenance.getStatus() == 0 ? 1 : 0);
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