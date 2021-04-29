package com.jacksonasantos.travelplan.ui.vehicle;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.MaintenancePlan;
import com.jacksonasantos.travelplan.dao.Vehicle;
import com.jacksonasantos.travelplan.dao.VehicleHasPlan;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.Globals;

import java.util.List;

public class VehiclePlanActivity extends AppCompatActivity {

    private Integer nrVehicle_id =0;
    private Integer nrspinService_description;
    private EditText etRecommendation;
    private EditText etMeasure;
    private int nrMeasure;
    private EditText etExpirationNumber;
    private Button btSaveVehiclePlan;
    private RecyclerView rvVehiclePlan;

    private TextView txVehicleName;
    private ImageView imVehicleType;
    private TextView txVehicleLicencePlate;
    private AutoCompleteTextView spinService_description;

    private VehiclePlanListAdapter adapter;

    private boolean opInsert = true;
    private VehicleHasPlan vehicleHasPlan;

    @SuppressLint("WrongViewCast")
    @RequiresApi(api = Build.VERSION_CODES.N)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.Vehicle_Plan);
        setContentView(R.layout.activity_vehicle_plan);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        txVehicleName = findViewById(R.id.txVehicleName);
        imVehicleType = findViewById(R.id.imVehicleType);
        txVehicleLicencePlate = findViewById(R.id.txVehicleLicencePlate);
        spinService_description = findViewById(R.id.spinService_description);
        etRecommendation = findViewById(R.id.etRecommendation);
        etMeasure = findViewById(R.id.etMeasure);
        nrMeasure = 0;
        etExpirationNumber = findViewById(R.id.etExpirationNumber);
        btSaveVehiclePlan = findViewById(R.id.btSaveVehiclePlan);
        rvVehiclePlan = findViewById(R.id.rvVehiclePlan);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
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

        final List<MaintenancePlan> maintenancePlans = Database.mMaintenancePlanDao.fetchArrayMaintenancePlan();
        ArrayAdapter<MaintenancePlan> adapterT = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, maintenancePlans);
        adapterT.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinService_description.setAdapter(adapterT);

        final MaintenancePlan[] mp1 = {new MaintenancePlan()};
        spinService_description.setOnItemClickListener((parent, view, position, id) -> {
            mp1[0] = (MaintenancePlan) parent.getItemAtPosition(position);
            nrspinService_description = mp1[0].getId();
            etRecommendation.setText(mp1[0].getRecommendation());
            nrMeasure = mp1[0].getMeasure();
            etMeasure.setText(getResources().getStringArray(R.array.measure_plan)[nrMeasure]);
            //etExpirationNumber.setText(String.valueOf(mp1[0].getExpiration_default()));
        });
        adapterT.notifyDataSetChanged();

        if (vehicleHasPlan != null) {
            nrspinService_description = vehicleHasPlan.getMaintenance_plan_id();
            if (nrspinService_description != null && nrspinService_description > 0) {
                MaintenancePlan mp2 = Database.mMaintenancePlanDao.fetchMaintenancePlanById(nrspinService_description);
                for (int x = 0; x <= spinService_description.getAdapter().getCount(); x++) {
                    if (spinService_description.getAdapter().getItem(x).toString().equals(mp2.getDescription())) {
                        spinService_description.setText(spinService_description.getAdapter().getItem(x).toString(),false);
                        etRecommendation.setText(mp2.getRecommendation());
                        nrMeasure = mp2.getMeasure();
                        break;
                    }
                }
            }
            etMeasure.setText(getResources().getStringArray(R.array.measure_plan)[nrMeasure]);
            etExpirationNumber.setText(String.valueOf(vehicleHasPlan.getExpiration()));
        }

        adapter = new VehiclePlanListAdapter(Database.mVehicleHasPlanDao.fetchAllVehicleHasPlanByVehicle(nrVehicle_id), getApplicationContext());
        rvVehiclePlan.setAdapter(adapter);
        rvVehiclePlan.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter.notifyDataSetChanged();


        btSaveVehiclePlan.setOnClickListener(v -> {
            boolean isSave = false;

            if (!validateData()) {
                Toast.makeText(getApplicationContext(), R.string.Error_Data_Validation, Toast.LENGTH_LONG).show();
            } else {
                final VehicleHasPlan vp1 = new VehicleHasPlan();

                vp1.setVehicle_id(nrVehicle_id);
                vp1.setMaintenance_plan_id(nrspinService_description);
                if (etExpirationNumber.getText().toString().isEmpty()){
                    vp1.setExpiration(0);
                } else {
                    vp1.setExpiration(Integer.parseInt(etExpirationNumber.getText().toString()));
                }

                if (!opInsert) {
                    try {
                        vp1.setId(vehicleHasPlan.getId());
                        isSave = Database.mVehicleHasPlanDao.updateVehicleHasPlan(vp1);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), R.string.Error_Changing_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        isSave = Database.mVehicleHasPlanDao.addVehicleHasPlan(vp1);

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), R.string.Error_Including_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                setResult(isSave ? 1 : 0);
                if (!isSave) {
                    Toast.makeText(getApplicationContext(), R.string.Error_Saving_Data, Toast.LENGTH_LONG).show();
                } else {
                    adapter = new VehiclePlanListAdapter(Database.mVehicleHasPlanDao.fetchAllVehicleHasPlanByVehicle(nrVehicle_id), getApplicationContext());
                    rvVehiclePlan.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    spinService_description.setText("");
                    nrspinService_description = 0;
                    etRecommendation.setText("");
                    nrMeasure = 0;
                    etMeasure.setText("");
                    etExpirationNumber.setText("");
                }
            }
        });
    }

    private boolean validateData() {
        boolean isValid = false;
        try {
            if (!String.valueOf(nrspinService_description).trim().isEmpty()
                //&& !etExpirationNumber.getText().toString().trim().isEmpty()
               )
            {
                isValid = true;
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.Data_Validator_Error +" - " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return isValid;
    }
}