package com.jacksonasantos.travelplan.ui.general;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.MaintenancePlan;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.Utils;

public class MaintenancePlanActivity extends AppCompatActivity {

    private Spinner spinService_type;
    private int nrSpinService_type;
    private EditText etDescription;
    private EditText etRecommendation;
    private Spinner spinMeasure;
    private int nrSpinMeasure;
    private EditText etExpiration_default;
    private SwitchMaterial cbRecurringService;
    private int vlRecurringService = 0;

    private boolean opInsert = true;
    private MaintenancePlan maintenancePlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.Maintenance_Plan);
        setContentView(R.layout.activity_maintenance_plan);

        spinService_type = findViewById(R.id.spinService_type);
        etDescription = findViewById(R.id.etDescription);
        spinMeasure = findViewById(R.id.spinMeasure);
        etExpiration_default = findViewById(R.id.etExpiration_default);
        etRecommendation = findViewById(R.id.etRecommendation);
        cbRecurringService = findViewById(R.id.cbRecurringService);
    }

    protected void onResume() {

        super.onResume();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            maintenancePlan = new MaintenancePlan();
            if (extras.getInt("maintenance_plan_id") > 0) {
                maintenancePlan.setId(extras.getInt("maintenance_plan_id"));
                maintenancePlan = Database.mMaintenancePlanDao.fetchMaintenancePlanById(maintenancePlan.getId());
                opInsert = false;
            }
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        addListenerOnButtonSave();

        Utils.createSpinnerResources(R.array.vehicle_services, spinService_type, this);
        nrSpinService_type = 0;
        spinService_type.setSelection(nrSpinService_type);
        spinService_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long idx) {
                nrSpinService_type = position;  }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                nrSpinService_type =0;
            }
        });

        Utils.createSpinnerResources(R.array.measure_plan, spinMeasure, this);
        nrSpinMeasure = 0;
        spinMeasure.setSelection(nrSpinMeasure);
        spinMeasure.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long idx) {
                nrSpinMeasure = position;  }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                nrSpinMeasure =0;
            }
        });
        cbRecurringService.setOnClickListener(v -> vlRecurringService = !cbRecurringService.isChecked()?0:1);

        if (maintenancePlan != null) {
            nrSpinService_type = maintenancePlan.getService_type();
            spinService_type.setSelection(nrSpinService_type);
            etDescription.setText(maintenancePlan.getDescription());
            nrSpinMeasure = maintenancePlan.getMeasure();
            spinMeasure.setSelection(nrSpinMeasure);
            etExpiration_default.setText(String.valueOf(maintenancePlan.getExpiration_default()));
            etRecommendation.setText(maintenancePlan.getRecommendation());
            cbRecurringService.setChecked(maintenancePlan.getRecurring_service()==1);
            vlRecurringService = maintenancePlan.getRecurring_service();
        }
    }

    public void addListenerOnButtonSave() {
        Button btSaveMaintenancePlan = findViewById(R.id.btSaveMaintenancePlan);

        btSaveMaintenancePlan.setOnClickListener(v -> {
            boolean isSave = false;

            if (!validateData()) {
                Toast.makeText(getApplicationContext(), R.string.Error_Data_Validation, Toast.LENGTH_LONG).show();
            } else {
                final MaintenancePlan mp1 = new MaintenancePlan();

                mp1.setService_type(nrSpinService_type);
                mp1.setDescription(etDescription.getText().toString());
                mp1.setMeasure(nrSpinMeasure);
                mp1.setExpiration_default((Integer.parseInt(etExpiration_default.getText().toString().isEmpty() ? "0": etExpiration_default.getText().toString())));
                mp1.setRecommendation(etRecommendation.getText().toString());
                mp1.setRecurring_service(vlRecurringService);

                if (!opInsert) {
                    try {
                        mp1.setId(maintenancePlan.getId());
                        isSave = Database.mMaintenancePlanDao.updateMaintenancePlan(mp1);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), R.string.Error_Changing_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        isSave = Database.mMaintenancePlanDao.addMaintenancePlan(mp1);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), R.string.Error_Including_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                setResult(isSave ? 1 : 0);
                if (isSave) {
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.Error_Saving_Data, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private boolean validateData() {
        boolean isValid = false;
        try {
            if (!String.valueOf(nrSpinService_type).trim().isEmpty()
                && !etDescription.getText().toString().trim().isEmpty()
                && !String.valueOf(nrSpinMeasure).trim().isEmpty()
                //&& !etExpiration_default.getText().toString().trim().isEmpty()
                && !etRecommendation.getText().toString().trim().isEmpty()
                //&& vlRecurringService != null
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