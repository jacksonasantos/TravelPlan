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
import com.jacksonasantos.travelplan.dao.Database;
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
        holder.txtNumberLiters.setText(fuelSupply.getNumber_liters() +" L");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        holder.txtSupplyValue.setText(currencyFormatter.format(fuelSupply.getSupply_value()));
        // btnEdit
        holder.btnEdit.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), FuelSupplyActivity.class);
                intent.putExtra("fuel_supply_id", fuelSupply.getId());
                intent.putExtra("vehicle_id", fuelSupply.getVehicle_id());
                context.startActivity(intent);
                notifyDataSetChanged();
            }
        });
        // btnDelete
        holder.btnDelete.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle(R.string.FuelSupply_Deleting)
                        .setMessage(R.string.Msg_Confirm)
                        .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    Database.mFuelSupplyDao.deleteFuelSupply(fuelSupply.getId());  // invoca a deleção do registro
                                    mFuelSupply.remove(position);
                                    notifyItemRemoved(position);
                                } catch (Exception e) {
                                    Toast.makeText(context, context.getString(R.string.Error_Deleting_Data) + "\n" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }).setNegativeButton(R.string.No, null)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFuelSupply.size();
    }
}