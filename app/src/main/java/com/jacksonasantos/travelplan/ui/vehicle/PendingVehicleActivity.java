package com.jacksonasantos.travelplan.ui.vehicle;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.PendingVehicle;
import com.jacksonasantos.travelplan.dao.Vehicle;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Utils;

public class PendingVehicleActivity extends AppCompatActivity {

    private TextView txVehicleName;
    private TextView txVehicleLicencePlate;
    private Integer nrVehicleId = 0;
    private int nrSpServiceType = 0;
    private Spinner spServiceType;
    private EditText etNote;
    private EditText etExpectedValue;
    private TextView tvStatusPending;

    private boolean opInsert = true;
    private PendingVehicle pendingVehicle;

    final Globals g = Globals.getInstance();
    String[] statusArray;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Database mDb = new Database(getApplicationContext());
        mDb.open();

        setTitle(R.string.PendingVehicle);
        setContentView(R.layout.activity_pending_vehicle);

        Bundle extras = getIntent().getExtras();
        pendingVehicle = new PendingVehicle();
        statusArray = getResources().getStringArray(R.array.status_pending_vehicle_array);
        pendingVehicle.setExpected_value((double) 0);

        if (extras != null) {
            if (extras.getInt( "vehicle_id") > 0) {
                nrVehicleId = extras.getInt("vehicle_id");
                pendingVehicle.setVehicle_id(nrVehicleId);
            }
            if (extras.getInt( "pending_vehicle_id") > 0) {
                pendingVehicle.setId(extras.getInt("pending_vehicle_id"));
                pendingVehicle = Database.mPendingVehicleDao.fetchPendingVehicleById(pendingVehicle.getId());
                opInsert = false;
            }
        }
        if (opInsert) {
            if (nrVehicleId == 0) {
                nrVehicleId = g.getIdVehicle();
            }
        } else {
            nrVehicleId = pendingVehicle.getVehicle_id();
        }

        if (nrVehicleId==null){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.A_vehicle_needs_to_be_selected), Toast.LENGTH_LONG).show();
            finish();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        addListenerOnButtonSave();
        txVehicleName = findViewById(R.id.txVehicleName);
        txVehicleLicencePlate = findViewById(R.id.txVehicleLicencePlate);
        spServiceType = findViewById(R.id.spServiceType);
        etNote = findViewById(R.id.etNote);
        etExpectedValue = findViewById(R.id.etExpectedValue);
        tvStatusPending = findViewById(R.id.tvStatusPending);

        final Vehicle vehicle = Database.mVehicleDao.fetchVehicleById(nrVehicleId);
        txVehicleName.setText(vehicle.getName());
        txVehicleLicencePlate.setText(vehicle.getLicense_plate());
        etExpectedValue.setText(pendingVehicle.getExpected_value().toString());
        tvStatusPending.setText(statusArray[pendingVehicle.getStatus_pending()]);

        Utils.createSpinnerResources(R.array.vehicle_services, spServiceType, this);
        nrSpServiceType = pendingVehicle.getService_type();
        spServiceType.setSelection(nrSpServiceType);
        spServiceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long idx) {
                nrSpServiceType = position;  }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                nrSpServiceType = 0;
            }
        });
        
        if (pendingVehicle != null) {
            nrSpServiceType=pendingVehicle.getService_type();
            spServiceType.setSelection(nrSpServiceType);
            etNote.setText(pendingVehicle.getNote());
            etExpectedValue.setText(String.valueOf(pendingVehicle.getExpected_value()));
            tvStatusPending.setText(statusArray[pendingVehicle.getStatus_pending()]);
        }
    }

    public void addListenerOnButtonSave() {
        Button btSavePendingVehicle = findViewById(R.id.btSavePendingVehicle);

        btSavePendingVehicle.setOnClickListener(v -> {
            boolean isSave = false;

            if (!validateData()) {
                Toast.makeText(getApplicationContext(), R.string.Error_Data_Validation, Toast.LENGTH_LONG).show();
            } else {
                final PendingVehicle pv1 = new PendingVehicle();

                pv1.setVehicle_id(nrVehicleId);
                pv1.setService_type(nrSpServiceType);
                pv1.setNote(etNote.getText().toString());
                if (!etExpectedValue.getText().toString().isEmpty()) {
                    pv1.setExpected_value(Double.parseDouble(etExpectedValue.getText().toString()));
                } else { pv1.setExpected_value((double) 0); }
                try {
                    if (!opInsert) {
                        pv1.setId(pendingVehicle.getId());
                        isSave = Database.mPendingVehicleDao.updatePendingVehicle(pv1);
                    } else {
                        pv1.setStatus_pending(0);
                        isSave = Database.mPendingVehicleDao.addPendingVehicle(pv1);
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),  R.string.Error_Saving_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                setResult(isSave ? 1 : 0);
                if (isSave) {
                    finish();
                }
            }
        });
    }

    private boolean validateData() {
        boolean isValid = false;

        try {
            if (!String.valueOf(nrSpServiceType).trim().isEmpty()
                && !etNote.getText().toString().trim().isEmpty()
                //&& !etExpectedValue.getText().toString().trim().isEmpty()
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