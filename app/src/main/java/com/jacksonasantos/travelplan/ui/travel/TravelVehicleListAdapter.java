package com.jacksonasantos.travelplan.ui.travel;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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

    public TravelVehicleListAdapter(List<VehicleHasTravel> vehicleHasTravels, Context context, String form, int show_header, Integer travel_id) {
        this.mVehicleHasTravel = vehicleHasTravels;
        this.mTravel_id = travel_id;
        this.context = context;
        this.form = form;
        this.show_header = show_header;

        numberFormatter.setMaximumFractionDigits(2);
        numberFormatter.setMinimumFractionDigits(2);
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
            headerViewHolder.imgTransportType.setVisibility(View.INVISIBLE);
            headerViewHolder.txtVehicle.setText(R.string.Vehicle);
            headerViewHolder.txtPerson.setText(R.string.Itinerary_has_Transport_Driver);
            headerViewHolder.txtIdentifier.setText(R.string.Transport_Identifier);
            headerViewHolder.txtAvgConsumption.setText(R.string.Vehicle_Avg_Consumption);
            headerViewHolder.txtAvgConsumptionTravel.setText(R.string.Travel_Avg_Consumption);
            headerViewHolder.btnRefuel.setVisibility(View.INVISIBLE);
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
            final FuelSupply fuelSupply = Database.mFuelSupplyDao.findAVGConsumptionTravel(vehicleHasTravel.getVehicle_id(), mTravel_id);

            itemViewHolder.imgTransportType.setImageResource(Transport.getTransportTypeImage(transport.getTransport_type()));
            if (vehicleHasTravel.getVehicle_id() == null || vehicleHasTravel.getVehicle_id() == 0 ) {
                itemViewHolder.txtVehicle.setText(transport.getDescription());
                itemViewHolder.txtIdentifier.setText(transport.getIdentifier());
                itemViewHolder.txtAvgConsumption.setText(String.format("%s %s", numberFormatter.format(vehicleHasTravel.getAvg_consumption()), g.getMeasureConsumption()));
                }
            else {
                itemViewHolder.txtVehicle.setText(vehicle.getShort_name());
                itemViewHolder.txtIdentifier.setText(vehicle.getLicense_plate());
                itemViewHolder.txtAvgConsumption.setText(String.format("%s %s", numberFormatter.format(vehicle.getAvg_consumption()), g.getMeasureConsumption()));
                itemViewHolder.txtAvgConsumptionTravel.setText(String.format("%s %s", numberFormatter.format(fuelSupply.getStat_avg_fuel_consumption()), g.getMeasureConsumption()));
                // TODO - Armazenar abastecimento para transporte e mostra media
            }
            itemViewHolder.txtPerson.setText(driver.getShort_Name());

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

            if (!itemViewHolder.txtPerson.getText().toString().equals("")) {
                itemViewHolder.btnRefuel.setOnClickListener(v -> {
                    Intent intent = new Intent(v.getContext(), FuelSupplyActivity.class);
                    if (vehicle != null) {
                        intent.putExtra("vehicle_id", vehicleHasTravel.getVehicle_id());
                    }
                    if (transport != null) {
                        intent.putExtra("transport_id", vehicleHasTravel.getTransport_id());
                    }
                    intent.putExtra("travel_id", vehicleHasTravel.getTravel_id());
                    v.getContext().startActivity(intent);
                });
            } else {
                itemViewHolder.btnRefuel.setVisibility(View.INVISIBLE);
            }
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
        public final ImageView imgTransportType;
        public final TextView txtVehicle;
        public final TextView txtPerson;
        public final TextView txtIdentifier;
        public final TextView txtAvgConsumption;
        public final TextView txtAvgConsumptionTravel;
        public final ImageButton btnRefuel;
        public final ImageButton btnAddVehicleHasTravel;

        public HeaderViewHolder(View v) {
            super(v);
            llVehicleTravelItem = v.findViewById(R.id.llVehicleTravelItem);
            imgTransportType = v.findViewById(R.id.imgTransportType);
            txtVehicle = v.findViewById(R.id.txtVehicle);
            txtPerson = v.findViewById(R.id.txtPerson);
            txtIdentifier = v.findViewById(R.id.txtIdentifier);
            txtAvgConsumption = v.findViewById(R.id.txtAvgConsumption);
            txtAvgConsumptionTravel = v.findViewById(R.id.txtAvgConsumptionTravel);
            btnRefuel = v.findViewById(R.id.btnRefuel);
            btnAddVehicleHasTravel = v.findViewById(R.id.btnDelete);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout llVehicleTravelItem;
        public final ImageView imgTransportType;
        public final TextView txtVehicle;
        public final TextView txtPerson;
        public final TextView txtIdentifier;
        public final TextView txtAvgConsumption;
        public final TextView txtAvgConsumptionTravel;
        public final ImageButton btnDelete;
        public final ImageButton btnRefuel;

        public ItemViewHolder(View v) {
            super(v);
            llVehicleTravelItem = v.findViewById(R.id.llVehicleTravelItem);
            imgTransportType = v.findViewById(R.id.imgTransportType);
            txtVehicle = v.findViewById(R.id.txtVehicle);
            txtPerson = v.findViewById(R.id.txtPerson);
            txtIdentifier = v.findViewById(R.id.txtIdentifier);
            txtAvgConsumption = v.findViewById(R.id.txtAvgConsumption);
            txtAvgConsumptionTravel = v.findViewById(R.id.txtAvgConsumptionTravel);
            btnDelete = v.findViewById(R.id.btnDelete);
            btnRefuel = v.findViewById(R.id.btnRefuel);
        }
    }
}