package com.jacksonasantos.travelplan.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Database;
import com.jacksonasantos.travelplan.dao.FuelSupply;
import com.jacksonasantos.travelplan.dao.Vehicle;
import com.jacksonasantos.travelplan.dao.VehicleStatistics;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Utils;
import com.jacksonasantos.travelplan.ui.vehicle.FuelSupplyActivity;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private Spinner spVehicle;
    private TextView tvLicencePlate;
    private ImageView imVehicleType;

    private TextView tvFuelSupplyDate;
    private TextView tvFuelSupplyLastOdometer;
    private TextView tvFuelSupplyNumberLiters;
    private TextView tvFuelSupplyValue;

    private RecyclerView vehicleStatisticsList;

    private RecyclerView maintenanceList;

    Globals g = Globals.getInstance();

    Locale locale = new Locale(g.getLanguage(), g.getCountry());

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_home, container, false);
        spVehicle =v.findViewById(R.id.spVehicle);
        tvLicencePlate = v.findViewById(R.id.tvLicencePlate);
        imVehicleType = v.findViewById(R.id.imVehicleType);
        ImageButton btnRefuel = v.findViewById(R.id.btnRefuel);
        tvFuelSupplyDate = v.findViewById(R.id.tvFuelSupplyDate);
        tvFuelSupplyLastOdometer = v.findViewById(R.id.tvFuelSupplyLastOdometer);
        tvFuelSupplyNumberLiters = v.findViewById(R.id.tvFuelSupplyNumberLiters);
        tvFuelSupplyValue = v.findViewById(R.id.tvFuelSupplyValue);
        vehicleStatisticsList = v.findViewById(R.id.listVehicleStatistics);
        maintenanceList = v.findViewById(R.id.listInVehicleService);

        btnRefuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), FuelSupplyActivity.class);
                intent.putExtra("vehicle_id", g.getIdVehicle());
                v.getContext().startActivity(intent);
            }
        });
        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume() {
        super.onResume();
        final Database mDb = new Database(getActivity());
        mDb.open();

        final List<Vehicle> vehicles =  Database.mVehicleDao.fetchArrayVehicles();
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, vehicles);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spVehicle.setAdapter(adapter);
        Vehicle v1 = Database.mVehicleDao.fetchVehicleById(g.getIdVehicle());
        for (int x = 0; x < spVehicle.getAdapter().getCount(); x++) {
            if (spVehicle.getItemAtPosition(x).toString().equals(v1.getName())) {
                spVehicle.setSelection(x);
                break;
            }
        }

        final Vehicle[] vehicle = {new Vehicle()};
        spVehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Displays maintenance service reminder data for the selected vehicle
                Database mDb = new Database(getContext());
                mDb.open();

                // Recupera dados do veículo selecionado no Spinner
                vehicle[0] = (Vehicle) parent.getItemAtPosition(position);
                tvLicencePlate.setText(vehicle[0].getLicense_plate());
                imVehicleType.setImageResource(vehicle[0].getVehicleTypeImage(vehicle[0].getVehicle_type()));

                g.setIdVehicle(vehicle[0].getId());

                // Last Fuel Supply of Vehicle in Global selection
                FuelSupply fuelSupply = Database.mFuelSupplyDao.findLastFuelSupply( g.getIdVehicle() );
                tvFuelSupplyDate.setText(Utils.dateToString(fuelSupply.getSupply_date()));
                tvFuelSupplyLastOdometer.setText(getString(R.string.Odometer)+ ": "+fuelSupply.getVehicle_odometer());
                tvFuelSupplyNumberLiters.setText(fuelSupply.getNumber_liters()==null? "0 L" :fuelSupply.getNumber_liters() +" L");
                NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
                tvFuelSupplyValue.setText(currencyFormatter.format(fuelSupply.getSupply_value()==null?BigDecimal.ZERO:fuelSupply.getSupply_value()));

                // Statistics of Vehicle in Global selection
                HomeStatisticsListAdapter adapterVehicle = new HomeStatisticsListAdapter(Database.mVehicleStatisticsDao.findVehicleStatisticsbyId(g.getIdVehicle()), getContext());
                vehicleStatisticsList.setAdapter(adapterVehicle);
                vehicleStatisticsList.setLayoutManager(new LinearLayoutManager(getContext()));

                // In-Vehicle Services
                HomeMaintenanceListAdapter adapterMaintenance = new HomeMaintenanceListAdapter(Database.mMaintenanceDao.findReminderMaintenance( g.getIdVehicle() ), getContext());
                maintenanceList.setAdapter(adapterMaintenance);
                maintenanceList.setLayoutManager(new LinearLayoutManager(getContext()));

                mDb.close();
                adapterVehicle.notifyDataSetChanged();
                adapterMaintenance.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                vehicle[0] = null;
            }
        });
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {

    }
}