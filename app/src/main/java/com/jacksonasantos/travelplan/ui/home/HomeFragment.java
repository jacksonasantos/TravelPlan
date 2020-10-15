package com.jacksonasantos.travelplan.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.assist.AssistStructure;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.CurrencyQuote;
import com.jacksonasantos.travelplan.dao.Database;
import com.jacksonasantos.travelplan.dao.FuelSupply;
import com.jacksonasantos.travelplan.dao.Vehicle;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Utils;
import com.jacksonasantos.travelplan.ui.vehicle.FuelSupplyActivity;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private TextView tvLicencePlate;
    private ImageView imVehicleType;
    private ImageButton btnRefuel;
    private TextView tvFuelSupplyDate;
    private TextView tvFuelSupplyLastOdometer;
    private TextView tvFuelSupplyNumberLiters;
    private TextView tvFuelSupplyValue;
    private ListView listInVehicleService;

    Context context;
    Locale locale = new Locale("pt", "BR"); // TODO - disponibilizar local dinamico

    Globals g = Globals.getInstance();

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_home, container, false);
        btnRefuel = v.findViewById(R.id.btnRefuel);
        btnRefuel.setOnClickListener((View.OnClickListener) this);
        return v;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent (v.getContext(), FuelSupplyActivity.class);
        intent.putExtra("vehicle_id", g.getIdVehicle());
        v.getContext().startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        final Database mDb = new Database(getActivity());
        mDb.open();

        Spinner spVehicle = this.getView().findViewById(R.id.spVehicle);
        tvLicencePlate = this.getView().findViewById(R.id.tvLicencePlate);
        imVehicleType = this.getView().findViewById(R.id.imVehicleType);
        tvFuelSupplyDate = this.getView().findViewById(R.id.tvFuelSupplyDate);
        tvFuelSupplyLastOdometer = this.getView().findViewById(R.id.tvFuelSupplyLastOdometer);
        tvFuelSupplyNumberLiters = this.getView().findViewById(R.id.tvFuelSupplyNumberLiters);
        tvFuelSupplyValue = this.getView().findViewById(R.id.tvFuelSupplyValue);

        final List<Vehicle> vehicles =  Database.mVehicleDao.fetchArrayVehicles();
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, vehicles);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spVehicle.setAdapter(adapter);
        spVehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Vehicle vehicle = (Vehicle) parent.getSelectedItem();
                tvLicencePlate.setText(vehicle.getLicense_plate());
                imVehicleType.setImageResource(vehicle.getTypeImage(vehicle.getType()));

                g.setIdVehicle(vehicle.getId());

                mDb.open();
                FuelSupply fuelSupply = Database.mFuelSupplyDao.findLastFuelSupply( g.getIdVehicle() );
                mDb.close();
                tvFuelSupplyDate.setText(Utils.dateToString(fuelSupply.getSupply_date()));
                tvFuelSupplyLastOdometer.setText("Odometer: "+fuelSupply.getVehicle_odometer());
                tvFuelSupplyNumberLiters.setText(fuelSupply.getNumber_liters()==null? "0 L" :fuelSupply.getNumber_liters() +" L");
                NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
                tvFuelSupplyValue.setText(currencyFormatter.format(fuelSupply.getSupply_value()==null?BigDecimal.ZERO:fuelSupply.getSupply_value()));

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mDb.close();
        adapter.notifyDataSetChanged();
    }
}