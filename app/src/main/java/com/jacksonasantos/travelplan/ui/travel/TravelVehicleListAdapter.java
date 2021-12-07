package com.jacksonasantos.travelplan.ui.travel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Vehicle;
import com.jacksonasantos.travelplan.dao.VehicleHasTravel;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.vehicle.FuelSupplyActivity;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TravelVehicleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private final List<VehicleHasTravel> mVehicleHasTravel;
    Context context;
    int show_header;
    public String form;

    Globals g = Globals.getInstance();
    Locale locale = new Locale(g.getLanguage(), g.getCountry());
    NumberFormat numberFormatter = NumberFormat.getNumberInstance(locale);
    NumberFormat integerFormatter = NumberFormat.getNumberInstance(locale);

    public TravelVehicleListAdapter(List<VehicleHasTravel> vehicleHasTravels, Context context, String form, int show_header) {
        this.mVehicleHasTravel = vehicleHasTravels;
        this.context = context;
        this.form = form;
        this.show_header = show_header; // 0 - NO SHOW HEADER | 1 - SHOW HEADER

        integerFormatter.setMaximumFractionDigits(0);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vehicleView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_travel_vehicle, parent, false);

        if (viewType == TYPE_HEADER) {
            return new HeaderViewHolder(vehicleView);
        } else return new ItemViewHolder(vehicleView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (holder instanceof HeaderViewHolder){
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

            headerViewHolder.llVehicleTravelItem.setBackgroundColor(Color.LTGRAY);
            headerViewHolder.txtVehicle.setText(R.string.Vehicle_Name);
            headerViewHolder.txtAvgConsumption.setText(R.string.Vehicle_Avg_Consumption);
            headerViewHolder.btnDelete.setVisibility(View.INVISIBLE);
            headerViewHolder.btnRefuel.setVisibility(View.INVISIBLE);
        }
        else if (holder instanceof ItemViewHolder) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            final VehicleHasTravel vehicleHasTravel = mVehicleHasTravel.get(position-show_header);
            final Vehicle vehicle = Database.mVehicleDao.fetchVehicleById(vehicleHasTravel.getVehicle_id());

            itemViewHolder.txtVehicle.setText(vehicle.getShort_name());
            itemViewHolder.txtAvgConsumption.setText(numberFormatter.format(vehicle.getAvg_consumption()) + " " + g.getMeasureConsumption());

            if (form.equals("Home")) {
                itemViewHolder.btnDelete.setVisibility(View.INVISIBLE);
            }

            itemViewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemViewHolder.btnDelete.setOnClickListener(this);
                    new AlertDialog.Builder(v.getContext())
                            .setTitle(R.string.Vehicle_Travel_Deleting)
                            .setMessage(R.string.Msg_Confirm)
                            .setPositiveButton(R.string.Yes, (dialogInterface, i) -> {
                                try {
                                    Database.mVehicleHasTravelDao.deleteVehicleHasTravel(vehicleHasTravel.getVehicle_id(), vehicleHasTravel.getTravel_id());
                                    mVehicleHasTravel.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, mVehicleHasTravel.size());
                                } catch (Exception e) {
                                    Toast.makeText(context, context.getString(R.string.Error_Deleting_Data) + "\n" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                }
                            }).setNegativeButton(R.string.No, null)
                            .show();
                }
            });

            itemViewHolder.btnRefuel.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), FuelSupplyActivity.class);
                intent.putExtra("vehicle_id", vehicleHasTravel.getVehicle_id());
                intent.putExtra("travel_id", vehicleHasTravel.getTravel_id());
                v.getContext().startActivity(intent);
            });
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
        return mVehicleHasTravel.size()+show_header;
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout llVehicleTravelItem;
        public TextView txtVehicle;
        public TextView txtAvgConsumption;
        public ImageButton btnDelete;
        public ImageButton btnRefuel;

        public HeaderViewHolder(View v) {
            super(v);
            llVehicleTravelItem = v.findViewById(R.id.llVehicleTravelItem);
            txtVehicle = v.findViewById(R.id.txtVehicle);
            txtAvgConsumption = v.findViewById(R.id.txtAvgConsumption);
            btnDelete = v.findViewById(R.id.btnDelete);
            btnRefuel = v.findViewById(R.id.btnRefuel);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout llVehicleTravelItem;
        public TextView txtVehicle;
        public TextView txtAvgConsumption;
        public ImageButton btnDelete;
        public ImageButton btnRefuel;

        public ItemViewHolder(View v) {
            super(v);
            llVehicleTravelItem = v.findViewById(R.id.llVehicleTravelItem);
            txtVehicle = v.findViewById(R.id.txtVehicle);
            txtAvgConsumption = v.findViewById(R.id.txtAvgConsumption);
            btnDelete = v.findViewById(R.id.btnDelete);
            btnRefuel = v.findViewById(R.id.btnRefuel);
        }
    }
}
