package com.jacksonasantos.travelplan.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Database;
import com.jacksonasantos.travelplan.dao.NextMaintenanceItem;
import com.jacksonasantos.travelplan.dao.Vehicle;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeVehicleNextMaintenanceListAdapter extends RecyclerView.Adapter<HomeVehicleNextMaintenanceListAdapter.MyViewHolder> {

    private final List<NextMaintenanceItem> mNextMaintenanceItem;
    Context context;
    String[] measureArray;

    Globals g = Globals.getInstance();

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ConstraintLayout llNextMaintenanceItem;
        private final ImageView imgServiceType;
        private final TextView txtDescription;
        private final TextView txtMeasure;
        private final TextView txtNextService;

        public MyViewHolder(View v) {
            super(v);
            llNextMaintenanceItem = v.findViewById(R.id.llNextMaintenanceItem);
            imgServiceType = v.findViewById(R.id.imgServiceType);
            txtDescription = v.findViewById(R.id.txtDescription);
            txtMeasure = v.findViewById(R.id.txtMeasure);
            txtNextService = v.findViewById(R.id.txtNextService);
        }

        @Override
        public void onClick(View view) {
        }
    }

    public HomeVehicleNextMaintenanceListAdapter(List<NextMaintenanceItem> nextMaintenanceItem, Context context) {
        this.mNextMaintenanceItem = nextMaintenanceItem;
        this.context = context;

        Database mdb = new Database(context);
        mdb.open();
    }

    @NonNull
    @Override
    public HomeVehicleNextMaintenanceListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View nextMaintenanceItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_home_vehicle_next_maintenance, parent, false);
        measureArray = parent.getResources().getStringArray(R.array.measure_item);
        return new MyViewHolder(nextMaintenanceItemView);
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Vehicle v1 = Database.mVehicleDao.fetchVehicleById(g.getIdVehicle());
        final NextMaintenanceItem nextMaintenanceItem = mNextMaintenanceItem.get(position);

        if (position%2==0) {
            holder.llNextMaintenanceItem.setBackgroundColor(Color.LTGRAY);
        } else {
            holder.llNextMaintenanceItem.setBackgroundColor(Color.WHITE);
        }

        int vStatus = 0;
        if (nextMaintenanceItem.getMeasure() == 1){         // KMs
            if (v1.getOdometer() > Integer.parseInt(nextMaintenanceItem.getNext_service()) ){
                vStatus = 2;
            } else if (v1.getOdometer() > Integer.parseInt(nextMaintenanceItem.getNext_service()) - g.getKMsPreviousAlert() ||
                       v1.getOdometer() == Integer.parseInt(nextMaintenanceItem.getNext_service()) ) {
                vStatus = 1;
            } else
                vStatus = 0;
        } else {                                            // Date
            Date vData;
            Date vDataNow = new Date();

            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd");

            try {
                vData = dateFormat.parse(nextMaintenanceItem.getNext_service());
                Date vDataLim = vData;

                Calendar c = Calendar.getInstance();
                c.setTime(vDataLim);
                c.add(Calendar.DATE, g.getDaysPreviousAlert()*-1);
                vDataLim = c.getTime();
                nextMaintenanceItem.setNext_service(Utils.dateToString(vData));
                if (vDataNow.compareTo(vData) > 0) {
                    vStatus = 2;
                } else if (vDataNow.compareTo(vData) <= 0 && vDataNow.compareTo(vDataLim) > 0) {
                    vStatus = 1;
                } else {
                    vStatus = 0;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        holder.imgServiceType.setImageResource(nextMaintenanceItem.getServiceTypeImage(nextMaintenanceItem.getService_type()));
        holder.txtDescription.setText(nextMaintenanceItem.getDescription());
        holder.txtMeasure.setText(measureArray[nextMaintenanceItem.getMeasure()-1]);
        switch(vStatus) {
            case 0: holder.txtNextService.setTextColor(Color.BLACK); break;  // Normal
            case 1: holder.txtNextService.setTextColor(Color.MAGENTA); break; // To Win
            case 2: holder.txtNextService.setTextColor(Color.RED); break;    // Expired
        }
        holder.txtNextService.setText(nextMaintenanceItem.getNext_service());
    }

    @Override
    public int getItemCount() {
        return mNextMaintenanceItem.size();
    }
}