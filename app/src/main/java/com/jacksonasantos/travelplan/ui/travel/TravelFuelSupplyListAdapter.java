package com.jacksonasantos.travelplan.ui.travel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.FuelSupply;
import com.jacksonasantos.travelplan.dao.Vehicle;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TravelFuelSupplyListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private final List<FuelSupply> mFuelSupplyHasTravel;
    final Context context;
    final int show_header;
    public final String form;

    final Globals g = Globals.getInstance();
    final Locale locale = new Locale(g.getLanguage(), g.getCountry());
    final NumberFormat numberFormatter = NumberFormat.getNumberInstance(locale);
    final NumberFormat integerFormatter = NumberFormat.getNumberInstance(locale);

    public TravelFuelSupplyListAdapter(List<FuelSupply> fuelSupplyHasTravels, Context context, String form, int show_header) {
        this.mFuelSupplyHasTravel = fuelSupplyHasTravels;
        this.context = context;
        this.form = form;
        this.show_header = show_header; // 0 - NO SHOW HEADER | 1 - SHOW HEADER

        integerFormatter.setMaximumFractionDigits(0);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View fuelSupplyView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_travel_fuel_supply, parent, false);

        if (viewType == TYPE_HEADER) {
            return new HeaderViewHolder(fuelSupplyView);
        } else return new ItemViewHolder(fuelSupplyView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (holder instanceof HeaderViewHolder){
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

            headerViewHolder.llVehicleTravelItem.setBackgroundColor(Color.LTGRAY);
            headerViewHolder.txtVehicle.setText(R.string.Vehicle);
            headerViewHolder.txtSupplyDate.setText(R.string.Home_Travel_Date);
            headerViewHolder.txtLocation.setText(R.string.Home_Travel_Location);
            headerViewHolder.txtNumberLiters.setText(R.string.Home_Travel_Liters);
            headerViewHolder.txtTravelledDistance.setText(R.string.Home_Travel_Distance);
            headerViewHolder.txtAvgConsumption.setText(g.getMeasureConsumption());
        }
        else if (holder instanceof ItemViewHolder) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            final FuelSupply fuelSupplyHasTravel = mFuelSupplyHasTravel.get(position-show_header);
            final Vehicle vehicle = Database.mVehicleDao.fetchVehicleById(fuelSupplyHasTravel.getVehicle_id());

            itemViewHolder.llVehicleTravelItem.setBackgroundColor(Utils.getColorWithAlpha(R.color.colorItemList,0.1f));
            itemViewHolder.txtVehicle.setText(vehicle.getShort_name());
            itemViewHolder.txtSupplyDate.setText(Utils.dateToString(fuelSupplyHasTravel.getSupply_date()));
            itemViewHolder.txtLocation.setText(fuelSupplyHasTravel.getGas_station_location());
            itemViewHolder.txtNumberLiters.setText(String.valueOf(fuelSupplyHasTravel.getNumber_liters()));
            itemViewHolder.txtTravelledDistance.setText(numberFormatter.format(fuelSupplyHasTravel.getVehicle_travelled_distance()));
            itemViewHolder.txtAvgConsumption.setText(numberFormatter.format(fuelSupplyHasTravel.getStat_avg_fuel_consumption()));
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
        return mFuelSupplyHasTravel.size()+show_header;
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout llVehicleTravelItem;
        public final TextView txtVehicle;
        public final TextView txtSupplyDate;
        public final TextView txtLocation;
        public final TextView txtNumberLiters;
        public final TextView txtTravelledDistance;
        public final TextView txtAvgConsumption;

        public HeaderViewHolder(View v) {
            super(v);
            llVehicleTravelItem = v.findViewById(R.id.llVehicleTravelItem);
            txtVehicle = v.findViewById(R.id.txtVehicle);
            txtSupplyDate = v.findViewById(R.id.txtSupplyDate);
            txtLocation = v.findViewById(R.id.txtLocation);
            txtNumberLiters = v.findViewById(R.id.txtNumberLiters);
            txtTravelledDistance = v.findViewById(R.id.txtTravelledDistance);
            txtAvgConsumption = v.findViewById(R.id.txtAvgConsumption);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout llVehicleTravelItem;
        public final TextView txtVehicle;
        public final TextView txtSupplyDate;
        public final TextView txtLocation;
        public final TextView txtNumberLiters;
        public final TextView txtTravelledDistance;
        public final TextView txtAvgConsumption;

        public ItemViewHolder(View v) {
            super(v);
            llVehicleTravelItem = v.findViewById(R.id.llVehicleTravelItem);
            txtVehicle = v.findViewById(R.id.txtVehicle);
            txtSupplyDate = v.findViewById(R.id.txtSupplyDate);
            txtLocation = v.findViewById(R.id.txtLocation);
            txtNumberLiters = v.findViewById(R.id.txtNumberLiters);
            txtTravelledDistance = v.findViewById(R.id.txtTravelledDistance);
            txtAvgConsumption = v.findViewById(R.id.txtAvgConsumption);
        }
    }
}
