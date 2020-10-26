package com.jacksonasantos.travelplan.ui.vehicle;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Database;
import com.jacksonasantos.travelplan.dao.Maintenance;
import com.jacksonasantos.travelplan.dao.Vehicle;
import com.jacksonasantos.travelplan.ui.utility.DateInputMask;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.Locale;

public class MaintenanceActivity extends AppCompatActivity {

    private Long nrVehicle_id=0L;
    private int nrSpinType;
    private EditText etDetail;
    private EditText etDate;
    private EditText etExpiration_date;
    private EditText etExpiration_km;
    private EditText etOdometer;
    private EditText etValue;
    private EditText etLocation;
    private EditText etNote;

    private boolean opInsert = true;
    private Maintenance maintenance;

    Locale locale = new Locale("pt", "BR");

    @SuppressLint("WrongViewCast")
    @RequiresApi(api = Build.VERSION_CODES.N)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Database mDb = new Database(getApplicationContext());
        mDb.open();

        setTitle(R.string.Maintenance_Vehicle);
        setContentView(R.layout.activity_maintenance);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            maintenance = new Maintenance();
            maintenance.setVehicle_id(extras.getLong("vehicle_id"));
            nrVehicle_id = extras.getLong("vehicle_id");
            if (extras.getLong( "maintenance_id") > 0) {
                maintenance.setId(extras.getLong("maintenance_id"));
                maintenance = Database.mMaintenanceDao.fetchMaintenanceById(maintenance.getId());
                opInsert = false;
            }
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        addListenerOnButtonSave();

        TextView txVehicleName = findViewById(R.id.txVehicleName);
        ImageView imVehicleType = findViewById(R.id.imVehicleType);
        TextView txVehicleLicencePlate = findViewById(R.id.txVehicleLicencePlate);
        AutoCompleteTextView spinType = findViewById(R.id.spinType);
        etDetail = findViewById(R.id.etDetail);
        etDate = findViewById(R.id.etDate);
        etExpiration_date = findViewById(R.id.etExpiration_date);
        etExpiration_km = findViewById(R.id.etExpiration_km);
        etOdometer = findViewById(R.id.etOdometer);
        etValue = findViewById(R.id.etValue);
        etLocation = findViewById(R.id.etLocation);
        etNote = findViewById(R.id.etNote);

        Vehicle vehicle;
        if (opInsert) {
            if (nrVehicle_id == 0L){
                Globals g = Globals.getInstance();
                nrVehicle_id = g.getIdVehicle();
            }
        } else {
            nrVehicle_id = maintenance.getVehicle_id();
        }
        vehicle = Database.mVehicleDao.fetchVehicleById(nrVehicle_id);
        txVehicleName.setText(vehicle.getName());
        txVehicleLicencePlate.setText(vehicle.getLicense_plate());
        imVehicleType.setImageResource(vehicle.getTypeImage(vehicle.getType()));

        etDate.addTextChangedListener(new DateInputMask(etDate));
        etExpiration_date.addTextChangedListener(new DateInputMask(etExpiration_date));
        createSpinnerResources(R.array.vehicle_services, spinType);
        nrSpinType = 0;
        spinType.setOnItemClickListener(new Spinner.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                nrSpinType = (int) adapterView.getItemIdAtPosition(i);
            }
        });

        if (maintenance != null) {
            nrSpinType=maintenance.getType();
            spinType.setText(getResources().getStringArray(R.array.vehicle_services)[nrSpinType],false);
            etDetail.setText(maintenance.getDetail());
            etDate.setText(Utils.dateToString(maintenance.getDate()));
            etExpiration_date.setText(Utils.dateToString(maintenance.getExpiration_date()));
            etExpiration_km.setText(String.valueOf(maintenance.getExpiration_km()));
            etOdometer.setText(String.valueOf(maintenance.getOdometer()));
            etValue.setText(String.valueOf(maintenance.getValue()));
            etLocation.setText(maintenance.getLocation());
            etNote.setText(maintenance.getNote());
        }
    }

    public void addListenerOnButtonSave() {
        Button btSaveMaintenance = findViewById(R.id.btSaveMaintenance);

        btSaveMaintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSave = false;

                if (!validateData()) {
                    Toast.makeText(getApplicationContext(), R.string.Error_Data_Validation, Toast.LENGTH_LONG).show();
                } else {
                    final Maintenance m1 = new Maintenance();

                    m1.setVehicle_id(nrVehicle_id);
                    m1.setType(nrSpinType);
                    m1.setDetail(etDetail.getText().toString());
                    m1.setDate(Utils.stringToDate(etDate.getText().toString()));
                    m1.setExpiration_date(Utils.stringToDate(etExpiration_date.getText().toString()));
                    m1.setExpiration_km((Integer.parseInt(etExpiration_km.getText().toString().isEmpty() ? "0": etExpiration_km.getText().toString())));
                    m1.setOdometer(Integer.parseInt(etOdometer.getText().toString()));
                    m1.setValue(Double.valueOf(etValue.getText().toString()));
                    m1.setLocation(etLocation.getText().toString());
                    m1.setNote(etNote.getText().toString());

                    Database mdb = new Database(MaintenanceActivity.this);
                    mdb.open();

                    if (!opInsert) {
                        try {
                            m1.setId(maintenance.getId());
                            isSave = Database.mMaintenanceDao.updateMaintenance(m1);
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), R.string.Error_Changing_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        try {
                            isSave = Database.mMaintenanceDao.addMaintenance(m1);
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), R.string.Error_Including_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    mdb.close();
                    setResult(isSave ? 1 : 0);
                    if (isSave) {
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.Error_Saving_Data, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    private boolean validateData() {
        boolean isValid = false;

        try {
            if (!String.valueOf(nrSpinType).trim().isEmpty()
//                && !etDetail.getText().toString().trim().isEmpty()
                && !etDate.getText().toString().trim().isEmpty()
//                && !etExpiration_date.getText().toString().trim().isEmpty()
//                && !etExpiration_km.getText().toString().trim().isEmpty()
                && !etOdometer.getText().toString().trim().isEmpty()
                && !etValue.getText().toString().trim().isEmpty()
//                && !etLocation.getText().toString().trim().isEmpty()
//                && !etNote.getText().toString().trim().isEmpty()
               )
            {
                isValid = true;
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.Data_Validator_Error +" - " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return isValid;
    }

    private void createSpinnerResources(int resource_array, AutoCompleteTextView spin) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.select_dialog_item,
                // TODO - colocar  o icone do resource
                getResources().getStringArray(resource_array));
        spin.setAdapter(adapter);
    }
}