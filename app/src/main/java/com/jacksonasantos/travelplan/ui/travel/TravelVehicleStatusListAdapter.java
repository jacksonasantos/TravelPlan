package com.jacksonasantos.travelplan.ui.travel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Vehicle;
import com.jacksonasantos.travelplan.dao.VehicleHasTravel;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TravelVehicleStatusListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private final List<VehicleHasTravel> mVehicleHasTravel;
    final Context context;
    final int show_header;
    public final String form;

    final Globals g = Globals.getInstance();
    final Locale locale = new Locale(g.getLanguage(), g.getCountry());
    final NumberFormat numberFormatter = NumberFormat.getNumberInstance(locale);

    public TravelVehicleStatusListAdapter(List<VehicleHasTravel> vehicleHasTravels, Context context, String form, int show_header) {
        this.mVehicleHasTravel = vehicleHasTravels;
        this.context = context;
        this.form = form;
        this.show_header = show_header; // 0 - NO SHOW HEADER | 1 - SHOW HEADER
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vehicleView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_home_travel_item_vehicle, parent, false);

        if (viewType == TYPE_HEADER) {
            return new HeaderViewHolder(vehicleView);
        } else return new ItemViewHolder(vehicleView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (holder instanceof HeaderViewHolder){
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

            headerViewHolder.llItemVehicle.setBackgroundColor(Utils.getColorWithAlpha(R.color.colorItemList,0.1f));
            headerViewHolder.txtVehicle.setText(R.string.Vehicle);
            headerViewHolder.txtStartOdometer.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            headerViewHolder.txtStartOdometer.setText(R.string.StartOdometer);
            headerViewHolder.txtFinalOdometer.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            headerViewHolder.txtFinalOdometer.setText(R.string.FinalOdometer);
            headerViewHolder.btnEdit.setVisibility(View.INVISIBLE);
        }
        else if (holder instanceof ItemViewHolder) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            final VehicleHasTravel vehicleHasTravel = mVehicleHasTravel.get(position-show_header);
            final Vehicle vehicle = Database.mVehicleDao.fetchVehicleById(vehicleHasTravel.getVehicle_id());

            itemViewHolder.txtVehicle.setText(vehicle.getShort_name());
            itemViewHolder.txtStartOdometer.setText(numberFormatter.format(vehicleHasTravel.getStart_odometer()));
            itemViewHolder.txtFinalOdometer.setText(numberFormatter.format(vehicleHasTravel.getFinal_odometer()));

            itemViewHolder.btnEdit.setOnClickListener(v -> {
                LayoutInflater li = LayoutInflater.from(v.getContext());
                View promptsView = li.inflate(R.layout.dialog_travel_vehicle, null);

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());

                alertDialogBuilder.setView(promptsView);
                final TextView txtVehicle = promptsView.findViewById(R.id.txtVehicle);
                final EditText etStartOdometer = promptsView.findViewById(R.id.etStartOdometer);
                final EditText etFinalOdometer = promptsView.findViewById(R.id.etFinalOdometer);

                txtVehicle.setText(vehicle.getShort_name());
                etStartOdometer.setText(String.valueOf(vehicleHasTravel.getStart_odometer()));
                etFinalOdometer.setText(String.valueOf(vehicleHasTravel.getFinal_odometer()));

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton(R.string.OK, (dialog, id) -> {
                            boolean isSave = false;

                            if (!etStartOdometer.getText().toString().isEmpty()) {
                                vehicleHasTravel.setStart_odometer(Integer.parseInt(etStartOdometer.getText().toString()));
                            }
                            if (!etFinalOdometer.getText().toString().isEmpty()) {
                                vehicleHasTravel.setFinal_odometer(Integer.parseInt(etFinalOdometer.getText().toString()));
                            }

                            try {
                                isSave = Database.mVehicleHasTravelDao.updateVehicleHasTravel(vehicleHasTravel);
                            } catch (Exception e) {
                                Toast.makeText(context, R.string.Error_Changing_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                            if (!isSave) {
                                Toast.makeText(context, R.string.Error_Saving_Data, Toast.LENGTH_LONG).show();
                            } else {
                                mVehicleHasTravel.set(position-show_header, vehicleHasTravel);
                                notifyItemRangeChanged(position-show_header, mVehicleHasTravel.size());
                            }
                        })
                        .setNegativeButton(R.string.Cancel, (dialog, id) -> dialog.cancel());
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
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
        public final LinearLayout llItemVehicle;
        public final TextView txtVehicle;
        public final TextView txtStartOdometer;
        public final TextView txtFinalOdometer;
        public final ImageButton btnEdit;

        public HeaderViewHolder(View v) {
            super(v);
            llItemVehicle = v.findViewById(R.id.llItemVehicle);
            txtVehicle = v.findViewById(R.id.txtVehicle);
            txtStartOdometer = v.findViewById(R.id.txtStartOdometer);
            txtFinalOdometer = v.findViewById(R.id.txtFinalOdometer);
            btnEdit = v.findViewById(R.id.btnEdit);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout llItemVehicle;
        public final TextView txtVehicle;
        public final TextView txtStartOdometer;
        public final TextView txtFinalOdometer;
        public final ImageButton btnEdit;

        public ItemViewHolder(View v) {
            super(v);
            llItemVehicle = v.findViewById(R.id.llItemVehicle);
            txtVehicle = v.findViewById(R.id.txtVehicle);
            txtStartOdometer = v.findViewById(R.id.txtStartOdometer);
            txtFinalOdometer = v.findViewById(R.id.txtFinalOdometer);
            btnEdit = v.findViewById(R.id.btnEdit);
        }
    }
}
