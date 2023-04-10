package com.jacksonasantos.travelplan.ui.vehicle;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.MaintenancePlan;
import com.jacksonasantos.travelplan.dao.VehicleHasPlan;
import com.jacksonasantos.travelplan.dao.general.Database;

import java.util.List;

public  class VehicleHasPlanActivity extends AppCompatActivity {

    private Spinner spinService_description;
    private Integer nrSpinService_description;
    private TextView tvRecommendation;
    private TextView tvMeasure;
    private TextView tvExpirationNumber;
    private EditText etExpirationNumber;

    private boolean opInsert = true;
    private VehicleHasPlan vehicleHasPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Database mDb = new Database(getApplicationContext());
        mDb.open();

        setTitle(R.string.Vehicle_Plan);
        setContentView(R.layout.dialog_vehicle_plan);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            vehicleHasPlan = new VehicleHasPlan();
            if (extras.getInt( "vehicle_has_plan_id") > 0) {
                vehicleHasPlan.setId(extras.getInt("vehicle_has_plan_id"));
                vehicleHasPlan = Database.mVehicleHasPlanDao.fetchVehicleHasPlanById(vehicleHasPlan.getId());
                opInsert = false;
            }
            if (extras.getInt( "vehicle_has_plan_maintenance_plan_id") >0 ) {
                vehicleHasPlan.setMaintenance_plan_id(extras.getInt("vehicle_has_plan_maintenance_plan_id"));
                nrSpinService_description = vehicleHasPlan.getMaintenance_plan_id();
            }
            if (extras.getInt( "vehicle_has_plan_vehicle_id") > 0) {
                vehicleHasPlan.setVehicle_id(extras.getInt("vehicle_has_plan_vehicle_id"));
            }
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        addListenerOnButtonSave();

        spinService_description = findViewById(R.id.spinService_description);
        tvRecommendation = findViewById(R.id.tvRecommendation);
        tvMeasure = findViewById(R.id.tvMeasure);
        tvExpirationNumber = findViewById(R.id.tvExpirationNumber);
        etExpirationNumber = findViewById(R.id.etExpirationNumber);

        final List<MaintenancePlan> maintenancePlans =  Database.mMaintenancePlanDao.fetchArrayMaintenancePlan();
        maintenancePlans.add(0, new MaintenancePlan());
        ArrayAdapter<MaintenancePlan> adapterM = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, maintenancePlans);
        adapterM.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinService_description.setAdapter(adapterM);
        if (nrSpinService_description != null && nrSpinService_description > 0) {
            MaintenancePlan m1 = Database.mMaintenancePlanDao.fetchMaintenancePlanById(nrSpinService_description);
            for (int x = 1; x <= spinService_description.getAdapter().getCount(); x++) {
                if (spinService_description.getAdapter().getItem(x).toString().equals(m1.getDescription())) {
                    spinService_description.setSelection(x);
                    nrSpinService_description = m1.getId();
                    break;
                }
            }
        }
        final MaintenancePlan[] m1 = {new MaintenancePlan()};
        spinService_description.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long idx) {
                m1[0] = (MaintenancePlan) parent.getItemAtPosition(position);
                nrSpinService_description = m1[0].getId();
                tvRecommendation.setText(m1[0].getRecommendation());
                tvExpirationNumber.setText(String.valueOf(m1[0].getExpiration_default()));
                tvMeasure.setText(getResources().getStringArray(R.array.measure_plan)[m1[0].getMeasure()]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                nrSpinService_description = null;
            }
        });
        adapterM.notifyDataSetChanged();

        final MaintenancePlan maintenancePlan = Database.mMaintenancePlanDao.fetchMaintenancePlanById(vehicleHasPlan.getMaintenance_plan_id());

        if (vehicleHasPlan != null) {
            nrSpinService_description=vehicleHasPlan.getMaintenance_plan_id();
            tvRecommendation.setText(maintenancePlan.getRecommendation());
            tvExpirationNumber.setText(String.valueOf(maintenancePlan.getExpiration_default()));
            tvMeasure.setText(getResources().getStringArray(R.array.measure_plan)[maintenancePlan.getMeasure()]);
            etExpirationNumber.setText(String.valueOf(vehicleHasPlan.getExpiration()));

            if (nrSpinService_description != null && nrSpinService_description > 0) {
                MaintenancePlan m2 = Database.mMaintenancePlanDao.fetchMaintenancePlanById(nrSpinService_description);
                for (int x = 1; x <= spinService_description.getAdapter().getCount(); x++) {
                    if (spinService_description.getAdapter().getItem(x).toString().equals(m2.getDescription())) {
                        spinService_description.setSelection(x);
                        nrSpinService_description = m2.getId();
                        break;
                    }
                }
            }
        }
    }

    public void addListenerOnButtonSave() {
        Button btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> {
            boolean isSave = false;

            if (!validateData()) {
                Toast.makeText(getApplicationContext(), R.string.Error_Data_Validation, Toast.LENGTH_LONG).show();
            } else {
                final VehicleHasPlan vp1 = new VehicleHasPlan();

                vp1.setVehicle_id(vehicleHasPlan.getVehicle_id());
                vp1.setMaintenance_plan_id(nrSpinService_description);
                vp1.setExpiration(Integer.parseInt(etExpirationNumber.getText().toString()));

                try {
                    if (!opInsert) {
                        try {
                            vp1.setId(vehicleHasPlan.getId());
                            isSave = Database.mVehicleHasPlanDao.updateVehicleHasPlan(vp1);
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.Error_Changing_Data) + "\n"+ e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        try {
                            vp1.setId(null);
                            isSave = Database.mVehicleHasPlanDao.addVehicleHasPlan(vp1);
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.Error_Including_Data)+ "\n" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    setResult(isSave ? 1 : 0);
                    if (isSave) { finish(); }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.Error_Saving_Data) + "\n" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private boolean validateData() {
        boolean isValid = false;

        try {
            if (nrSpinService_description!=0
                && !etExpirationNumber.getText().toString().trim().isEmpty()
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