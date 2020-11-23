package com.jacksonasantos.travelplan.ui.home;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Database;
import com.jacksonasantos.travelplan.dao.VehicleStatistics;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.List;

public class HomeStatisticsListAdapter extends RecyclerView.Adapter<HomeStatisticsListAdapter.MyViewHolder> {

    private final List<VehicleStatistics> mVehicleStatistics;
    Context context;
    String[] typeArray;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView txtReason;
        private final TextView txtAVGConsumption;
        private final TextView txtMeasureConsumption;
        private final TextView txtStatisticDate;

        public MyViewHolder(View v) {
            super(v);
            txtReason = v.findViewById(R.id.txtReason);
            txtAVGConsumption = v.findViewById(R.id.txtAVGConsumption);
            txtMeasureConsumption = v.findViewById(R.id.txtMeasureConsumption);
            txtStatisticDate = v.findViewById(R.id.txtStatisticDate);
        }

        @Override
        public void onClick(View view) {
        }
    }

    public HomeStatisticsListAdapter(List<VehicleStatistics> vehicleStatistics, Context context) {
        this.mVehicleStatistics = vehicleStatistics;
        this.context = context;

        Database mdb = new Database(context);
        mdb.open();
    }

    @NonNull
    @Override
    public HomeStatisticsListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vehicleStatisticsView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_home_item_statistics, parent, false);

        typeArray = parent.getResources().getStringArray(R.array.travel_type_array);

        return new MyViewHolder(vehicleStatisticsView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    //@SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final VehicleStatistics vehicleStatistics = mVehicleStatistics.get(position);

        holder.txtReason.setText(typeArray[vehicleStatistics.getSupply_reason_type()]);
        holder.txtStatisticDate.setText(Utils.dateToString(vehicleStatistics.getStatistic_date()));
        holder.txtAVGConsumption.setText(String.valueOf(vehicleStatistics.getAvg_consumption()));
        holder.txtMeasureConsumption.setText("km/l");
    }

    @Override
    public int getItemCount() {
        return mVehicleStatistics.size();
    }
}