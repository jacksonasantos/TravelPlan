package com.jacksonasantos.travelplan.ui.travel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Driver;
import com.jacksonasantos.travelplan.dao.FuelSupply;
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

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (holder instanceof HeaderViewHolder){
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

            final int[] nrSpinVehicle = {0};
            final int[] nrSpinDriver = {0};

            headerViewHolder.llVehicleTravelItem.setBackgroundColor(Utils.getColorWithAlpha(R.color.colorItemList,0.1f));
            headerViewHolder.txtVehicle.setText(R.string.Vehicle);
            headerViewHolder.txtDriver.setText(R.string.Driver);
            headerViewHolder.txtAvgConsumption.setText(R.string.Vehicle_Avg_Consumption);
            headerViewHolder.txtAvgConsumptionTravel.setText(R.string.Travel_Avg_Consumption);
            headerViewHolder.btnDelete.setVisibility(View.INVISIBLE);
            if (form.equals("Home")) {
                headerViewHolder.btnAddVehicelHasTravel.setVisibility(View.INVISIBLE);
            }
            else {
                headerViewHolder.btnAddVehicelHasTravel.setImageResource(R.drawable.ic_button_add);
                headerViewHolder.btnAddVehicelHasTravel.setOnClickListener(v -> {
                    LayoutInflater li = LayoutInflater.from(v.getContext());
                    View promptsView = li.inflate(R.layout.dialog_travel_vehiclehastravel, null);

                    String[] adapterCols = new String[]{"text1"};
                    int[] adapterRowViews = new int[]{android.R.id.text1};
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                    alertDialogBuilder.setView(promptsView);
                    final Spinner spinVehicle = promptsView.findViewById(R.id.spinVehicle);
                    final Spinner spinDriver = promptsView.findViewById(R.id.spinDriver);

                    Cursor cVehicle = Database.mVehicleDao.fetchCursorVehicle();
                    SimpleCursorAdapter cursorAdapterV = new SimpleCursorAdapter(li.getContext(),
                            android.R.layout.simple_spinner_item, cVehicle, adapterCols, adapterRowViews, 0);
                    cursorAdapterV.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinVehicle.setAdapter(cursorAdapterV);
                    Utils.setSpinnerToValue(spinVehicle, nrSpinVehicle[0]);
                    spinVehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            nrSpinVehicle[0] = Math.toIntExact(spinVehicle.getSelectedItemId());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });

                    Cursor cDriver = Database.mDriverDao.fetchCursorDriver();
                    SimpleCursorAdapter cursorAdapterC = new SimpleCursorAdapter(li.getContext(),
                            android.R.layout.simple_spinner_item, cDriver, adapterCols, adapterRowViews, 0);
                    cursorAdapterC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinDriver.setAdapter(cursorAdapterC);
                    Utils.setSpinnerToValue(spinDriver, nrSpinDriver[0]);
                    spinDriver.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            nrSpinDriver[0] = Math.toIntExact(spinDriver.getSelectedItemId());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });

                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton(R.string.OK, (dialog, id) -> {
                                boolean isSave = false;

                                final VehicleHasTravel vt1 = new VehicleHasTravel();

                                vt1.setTravel_id(mTravel_id);
                                vt1.setVehicle_id(nrSpinVehicle[0]);
                                vt1.setDriver_id(nrSpinDriver[0]);
                                try {
                                    isSave = Database.mVehicleHasTravelDao.addVehicleHasTravel(vt1);
                                } catch (Exception e) {
                                    Toast.makeText(context, R.string.Error_Including_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                                }

                                if (!isSave) {
                                    Toast.makeText(context, R.string.Error_Saving_Data, Toast.LENGTH_LONG).show();
                                } else {
                                    mVehicleHasTravel.add(vt1);
                                    notifyItemInserted(mVehicleHasTravel.size());
                                }
                            })
                            .setNegativeButton(R.string.Cancel, (dialog, id) -> dialog.cancel());
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                });

            }
        }
        else if (holder instanceof ItemViewHolder) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            final VehicleHasTravel vehicleHasTravel = mVehicleHasTravel.get(position-show_header);
            final Driver driver = Database.mDriverDao.fetchDriverById(vehicleHasTravel.getDriver_id());
            final Vehicle vehicle = Database.mVehicleDao.fetchVehicleById(vehicleHasTravel.getVehicle_id());
            final FuelSupply vehicleTravel = Database.mFuelSupplyDao.findAVGConsumptionTravel(vehicleHasTravel.getVehicle_id(), vehicleHasTravel.getTravel_id());

            //itemViewHolder.llVehicleTravelItem.setBackgroundColor(Utils.getColorWithAlpha(R.color.colorItemList,0.1f));
            itemViewHolder.txtVehicle.setText(vehicle.getShort_name());
            itemViewHolder.txtDriver.setText(driver.getShort_Name());
            itemViewHolder.txtAvgConsumption.setText(String.format("%s %s", numberFormatter.format(vehicle.getAvg_consumption()), g.getMeasureConsumption()));
            itemViewHolder.txtAvgConsumptionTravel.setText(String.format("%s %s", numberFormatter.format(vehicleTravel.getStat_avg_fuel_consumption()), g.getMeasureConsumption()));

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
                                    mVehicleHasTravel.remove(position-show_header);
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
        public final LinearLayout llVehicleTravelItem;
        public final TextView txtVehicle;
        public final TextView txtDriver;
        public final TextView txtAvgConsumption;
        public final TextView txtAvgConsumptionTravel;
        public final ImageButton btnAddVehicelHasTravel;
        public final ImageButton btnDelete;

        public HeaderViewHolder(View v) {
            super(v);
            llVehicleTravelItem = v.findViewById(R.id.llVehicleTravelItem);
            txtVehicle = v.findViewById(R.id.txtVehicle);
            txtDriver = v.findViewById(R.id.txtDriver);
            txtAvgConsumption = v.findViewById(R.id.txtAvgConsumption);
            txtAvgConsumptionTravel = v.findViewById(R.id.txtAvgConsumptionTravel);
            btnAddVehicelHasTravel = v.findViewById(R.id.btnRefuel);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout llVehicleTravelItem;
        public final TextView txtVehicle;
        public final TextView txtDriver;
        public final TextView txtAvgConsumption;
        public final TextView txtAvgConsumptionTravel;
        public final ImageButton btnDelete;
        public final ImageButton btnRefuel;

        public ItemViewHolder(View v) {
            super(v);
            llVehicleTravelItem = v.findViewById(R.id.llVehicleTravelItem);
            txtVehicle = v.findViewById(R.id.txtVehicle);
            txtDriver = v.findViewById(R.id.txtDriver);
            txtAvgConsumption = v.findViewById(R.id.txtAvgConsumption);
            txtAvgConsumptionTravel = v.findViewById(R.id.txtAvgConsumptionTravel);
            btnDelete = v.findViewById(R.id.btnDelete);
            btnRefuel = v.findViewById(R.id.btnRefuel);
        }
    }
}
