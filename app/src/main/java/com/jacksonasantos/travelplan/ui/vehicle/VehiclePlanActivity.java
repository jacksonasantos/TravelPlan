package com.jacksonasantos.travelplan.ui.vehicle;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Vehicle;
import com.jacksonasantos.travelplan.dao.VehicleHasPlan;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.Globals;

public class VehiclePlanActivity extends AppCompatActivity {

    private Integer nrVehicle_id = 0;
    private TextView txVehicleName;
    private ImageView imVehicleType;
    private TextView txVehicleLicencePlate;
    private RecyclerView rvVehiclePlan;

    private boolean opInsert = true;

    private VehiclePlanListAdapter adapter;
    private VehicleHasPlan vehicleHasPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.Vehicle_Plan);
        setContentView(R.layout.activity_vehicle_plan);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        addListenerOnButtonDone();
        txVehicleName = findViewById(R.id.txVehicleName);
        imVehicleType = findViewById(R.id.imVehicleType);
        txVehicleLicencePlate = findViewById(R.id.txVehicleLicencePlate);
        rvVehiclePlan = findViewById(R.id.rvVehiclePlan);
    }

    @SuppressLint("NotifyDataSetChanged")
    protected void onResume() {
        super.onResume();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            vehicleHasPlan = new VehicleHasPlan();
            if (extras.getInt("id") > 0) {
                vehicleHasPlan.setId(extras.getInt("id"));
                vehicleHasPlan = Database.mVehicleHasPlanDao.fetchVehicleHasPlanById(vehicleHasPlan.getId());
                nrVehicle_id = vehicleHasPlan.getVehicle_id();
                opInsert = false;
            }
            if (extras.getInt("vehicle_id") > 0) {
                vehicleHasPlan.setVehicle_id(extras.getInt("vehicle_id"));
                nrVehicle_id = vehicleHasPlan.getVehicle_id();
            }
        }

        Vehicle vehicle;
        if (opInsert) {
            if (nrVehicle_id == 0) {
                Globals g = Globals.getInstance();
                nrVehicle_id = g.getIdVehicle();
            }
        } else {
            nrVehicle_id = vehicleHasPlan.getVehicle_id();
        }
        vehicle = Database.mVehicleDao.fetchVehicleById(nrVehicle_id);
        txVehicleName.setText(vehicle.getName());
        txVehicleLicencePlate.setText(vehicle.getLicense_plate());
        imVehicleType.setImageResource(vehicle.getVehicleTypeImage(vehicle.getVehicle_type()));

/*        if (vehicleHasPlan != null) {
            nrSpinService_description = vehicleHasPlan.getMaintenance_plan_id();
            if (nrSpinService_description != null && nrSpinService_description > 0) {
                MaintenancePlan mp3 = Database.mMaintenancePlanDao.fetchMaintenancePlanById(nrSpinService_description);
                for (int x = 1; x <= spinService_description.getAdapter().getCount(); x++) {
                    if (spinService_description.getAdapter().getItem(x).toString().equals(mp3.getDescription())) {
                        spinService_description.setSelection(x);
                        etRecommendation.setText(mp3.getRecommendation());
                        nrMeasure = mp3.getMeasure();
                        break;
                    }
                }
            }
            tvMeasure.setText(getResources().getStringArray(R.array.measure_plan)[nrMeasure]);
            etExpirationNumber.setText(String.valueOf(vehicleHasPlan.getExpiration()));
        }*/

        adapter = new VehiclePlanListAdapter(Database.mVehicleHasPlanDao.fetchAllVehicleHasPlanByVehicle(nrVehicle_id), getApplicationContext());
        rvVehiclePlan.setAdapter(adapter);
        rvVehiclePlan.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter.notifyDataSetChanged();
    }

    public void addListenerOnButtonDone() {
        Button btDoneVehiclePlan = findViewById(R.id.btDoneVehiclePlan);
        btDoneVehiclePlan.setOnClickListener(v -> finish());
    }
}