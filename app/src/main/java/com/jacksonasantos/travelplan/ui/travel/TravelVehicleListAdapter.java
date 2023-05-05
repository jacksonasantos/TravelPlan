package com.jacksonasantos.travelplan.ui.travel;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.FuelSupply;
import com.jacksonasantos.travelplan.dao.Person;
import com.jacksonasantos.travelplan.dao.Transport;
import com.jacksonasantos.travelplan.dao.Vehicle;
import com.jacksonasantos.travelplan.dao.VehicleHasTravel;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Utils;
import com.jacksonasantos.travelplan.ui.vehicle.FuelSupplyActivity;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TravelVehicleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private final List<VehicleHasTravel> mVehicleHasTravel;
    final Context context;
    final int show_header;
    final Integer mTravel_id;
    public final String form;

    final Globals g = Globals.getInstance();
    final Locale locale = new Locale(g.getLanguage(), g.getCountry());
    final NumberFormat numberFormatter = NumberFormat.getNumberInstance(locale);
    final NumberFormat integerFormatter = NumberFormat.getNumberInstance(locale);

    public TravelVehicleListAdapter(List<VehicleHasTravel> vehicleHasTravels, Context context, String form, int show_header, Integer travel_id) {
        this.mVehicleHasTravel = vehicleHasTravels;
        this.mTravel_id = travel_id;
        this.context = context;
        this.form = form;
        this.show_header = show_header;

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

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (holder instanceof HeaderViewHolder){
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

            headerViewHolder.llVehicleTravelItem.setBackgroundColor(Utils.getColorWithAlpha(R.color.colorItemList,0.1f));
            headerViewHolder.txtVehicle.setText(R.string.Vehicle);
            headerViewHolder.txtPerson.setText(R.string.Itinerary_has_Transport_Driver);
            headerViewHolder.txtAvgConsumption.setText(R.string.Vehicle_Avg_Consumption);
            headerViewHolder.txtAvgConsumptionTravel.setText(R.string.Travel_Avg_Consumption);
            headerViewHolder.btnDelete.setVisibility(View.INVISIBLE);
            headerViewHolder.btnAddVehicleHasTravel.setImageResource(R.drawable.ic_button_add);
            headerViewHolder.btnAddVehicleHasTravel.setOnClickListener(v -> {
                Intent intent = new Intent (v.getContext(), ItineraryHasTransportActivity.class);
                intent.putExtra("travel_id", mTravel_id);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                notifyItemChanged(position);
            });
        }
        else if (holder instanceof ItemViewHolder) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            final VehicleHasTravel vehicleHasTravel = mVehicleHasTravel.get(position-show_header);
            final Person driver = Database.mPersonDao.fetchPersonById(vehicleHasTravel.getPerson_id());
            final Vehicle vehicle = Database.mVehicleDao.fetchVehicleById(vehicleHasTravel.getVehicle_id());
            final Transport transport = Database.mTransportDao.fetchTransportById(vehicleHasTravel.getTransport_id());
            final FuelSupply vehicleTravel = Database.mFuelSupplyDao.findAVGConsumptionTravel(vehicleHasTravel.getVehicle_id(), vehicleHasTravel.getTravel_id());

            if (vehicleHasTravel.getVehicle_id() == null || vehicleHasTravel.getVehicle_id() == 0 ) {
                itemViewHolder.txtVehicle.setText(transport.getDescription());
                itemViewHolder.txtAvgConsumption.setText(String.format("%s %s", numberFormatter.format(vehicleHasTravel.getAvg_consumption()), g.getMeasureConsumption()));
                }
            else {
                itemViewHolder.txtVehicle.setText(vehicle.getShort_name());
                itemViewHolder.txtAvgConsumption.setText(String.format("%s %s", numberFormatter.format(vehicle.getAvg_consumption()), g.getMeasureConsumption()));
                itemViewHolder.txtAvgConsumptionTravel.setText(String.format("%s %s", numberFormatter.format(vehicleTravel.getStat_avg_fuel_consumption()), g.getMeasureConsumption()));
            }
            itemViewHolder.txtPerson.setText(driver.getShort_Name());

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
                                    Database.mVehicleHasTravelDao.deleteVehicleHasTravel(vehicleHasTravel.getId());
                                    mVehicleHasTravel.remove(position-show_header);
                                    notifyItemRemoved(position-show_header);
                                    notifyItemRangeChanged(position-show_header, mVehicleHasTravel.size());
                                } catch (Exception e) {
                                    Toast.makeText(context, context.getString(R.string.Error_Deleting_Data) + "\n" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                }
                            }).setNegativeButton(R.string.No, null)
                            .show();
                }
            });

            itemViewHolder.btnRefuel.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), FuelSupplyActivity.class);
                if (vehicle != null) {
                    intent.putExtra("vehicle_id", vehicleHasTravel.getVehicle_id());
                }
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
        public final LinearLayout llVehicleTravelItem;
        public final TextView txtVehicle;
        public final TextView txtPerson;
        public final TextView txtAvgConsumption;
        public final TextView txtAvgConsumptionTravel;
        public final ImageButton btnAddVehicleHasTravel;
        public final ImageButton btnDelete;

        public HeaderViewHolder(View v) {
            super(v);
            llVehicleTravelItem = v.findViewById(R.id.llVehicleTravelItem);
            txtVehicle = v.findViewById(R.id.txtVehicle);
            txtPerson = v.findViewById(R.id.txtPerson);
            txtAvgConsumption = v.findViewById(R.id.txtAvgConsumption);
            txtAvgConsumptionTravel = v.findViewById(R.id.txtAvgConsumptionTravel);
            btnAddVehicleHasTravel = v.findViewById(R.id.btnRefuel);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout llVehicleTravelItem;
        public final TextView txtVehicle;
        public final TextView txtPerson;
        public final TextView txtAvgConsumption;
        public final TextView txtAvgConsumptionTravel;
        public final ImageButton btnDelete;
        public final ImageButton btnRefuel;

        public ItemViewHolder(View v) {
            super(v);
            llVehicleTravelItem = v.findViewById(R.id.llVehicleTravelItem);
            txtVehicle = v.findViewById(R.id.txtVehicle);
            txtPerson = v.findViewById(R.id.txtPerson);
            txtAvgConsumption = v.findViewById(R.id.txtAvgConsumption);
            txtAvgConsumptionTravel = v.findViewById(R.id.txtAvgConsumptionTravel);
            btnDelete = v.findViewById(R.id.btnDelete);
            btnRefuel = v.findViewById(R.id.btnRefuel);
        }
    }
}