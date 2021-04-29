package com.jacksonasantos.travelplan.ui.vehicle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.dao.FuelSupply;
import com.jacksonasantos.travelplan.dao.Vehicle;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class FuelSupplyListAdapter extends RecyclerView.Adapter<FuelSupplyListAdapter.MyViewHolder> {

    private final List<FuelSupply> mFuelSupply;
    Context context;

    Globals g = Globals.getInstance();

    Locale locale = new Locale(g.getLanguage(), g.getCountry());

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtSupplyDate;
        public TextView txtVehicleName;
        public TextView txtNumberLiters;
        public TextView txtSupplyValue;
        public ImageButton btnEdit;
        public ImageButton btnDelete;

        public MyViewHolder(View v) {
            super(v);
            txtSupplyDate = v.findViewById(R.id.txtSupplyDate);
            txtVehicleName = v.findViewById(R.id.txtVehicleName);
            txtNumberLiters = v.findViewById(R.id.txtNumberLiters);
            txtSupplyValue = v.findViewById(R.id.txtSupplyValue);
            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
            btnEdit.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }
    }

    public FuelSupplyListAdapter(List<FuelSupply> supplies, Context context) {
        this.mFuelSupply = supplies;
        this.context = context;

        Database mdb = new Database(context);
        mdb.open();
    }

    @NonNull
    @Override
    public FuelSupplyListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View supplyView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_fuel_supply, parent, false);
        return new MyViewHolder(supplyView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final FuelSupply fuelSupply = mFuelSupply.get(position);
        Vehicle v = Database.mVehicleDao.fetchVehicleById(fuelSupply.getVehicle_id());

        holder.txtSupplyDate.setText(Utils.dateToString(fuelSupply.getSupply_date()));
        holder.txtVehicleName.setText(v.getName());
        holder.txtNumberLiters.setText(fuelSupply.getNumber_liters() +" "+g.getMeasureCapacity());
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        holder.txtSupplyValue.setText(currencyFormatter.format(fuelSupply.getSupply_value()));
        // btnEdit
        holder.btnEdit.setOnClickListener (v13 -> {
            Intent intent = new Intent (v13.getContext(), FuelSupplyActivity.class);
            intent.putExtra("fuel_supply_id", fuelSupply.getId());
            intent.putExtra("vehicle_id", fuelSupply.getVehicle_id());
            context.startActivity(intent);
            notifyDataSetChanged();
        });
        // btnDelete
        holder.btnDelete.setOnClickListener (v12 -> new AlertDialog.Builder(v12.getContext())
                .setTitle(R.string.FuelSupply_Deleting)
                .setMessage(R.string.Msg_Confirm)
                .setPositiveButton(R.string.Yes, (dialogInterface, i) -> {
                    try {
                        Database.mFuelSupplyDao.deleteFuelSupply(fuelSupply.getId());
                        mFuelSupply.remove(position);
                        FuelSupply f1 = Database.mFuelSupplyDao.findLastFuelSupply(fuelSupply.getVehicle_id());
                        Vehicle v1;
                        v1 = Database.mVehicleDao.fetchVehicleById(fuelSupply.getVehicle_id());
                        v1.setDt_odometer(f1.getSupply_date());
                        v1.setOdometer(f1.getVehicle_odometer());
                        if (f1.getVehicle_odometer() == 0 && f1.getFull_tank() == 1) {
                            v1.setDt_last_fueling(f1.getSupply_date());
                            v1.setLast_supply_reason_type(f1.getSupply_reason_type());
                            v1.setAccumulated_supply_value(f1.getSupply_value());
                            v1.setAccumulated_number_liters(f1.getNumber_liters());
                        }
                        Database.mVehicleDao.updateVehicle(v1);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mFuelSupply.size());
                    } catch (Exception e) {
                        Toast.makeText(context, context.getString(R.string.Error_Deleting_Data) + "\n" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton(R.string.No, null)
                .show());
    }

    @Override
    public int getItemCount() {
        return mFuelSupply.size();
    }
}