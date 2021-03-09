package com.jacksonasantos.travelplan.ui.vehicle;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Database;
import com.jacksonasantos.travelplan.dao.Maintenance;
import com.jacksonasantos.travelplan.dao.MaintenanceItem;
import com.jacksonasantos.travelplan.dao.Vehicle;
import com.jacksonasantos.travelplan.ui.utility.DateInputMask;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Utils;

public class MaintenanceActivity extends AppCompatActivity {

    private Integer nrVehicle_id =0;
    private EditText etDetail;
    private EditText etDate;
    private EditText etOdometer;
    private EditText etValue;
    private EditText etLocation;
    private EditText etNote;

    private ConstraintLayout labelMaintenanceItem;
    private RecyclerView listMaintenanceItem;

    private boolean opInsert = true;
    private Maintenance maintenance;

    private MaintenanceItemListAdapter adapterMaintenanceItem;

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
            maintenance.setVehicle_id(extras.getInt("vehicle_id"));
            nrVehicle_id = extras.getInt("vehicle_id");
            if (extras.getInt( "maintenance_id") > 0) {
                maintenance.setId(extras.getInt("maintenance_id"));
                maintenance = Database.mMaintenanceDao.fetchMaintenanceById(maintenance.getId());
                opInsert = false;
            }
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        addListenerOnButtonSave();
        addListenerOnButtonAdd();

        TextView txVehicleName = findViewById(R.id.txVehicleName);
        ImageView imVehicleType = findViewById(R.id.imVehicleType);
        TextView txVehicleLicencePlate = findViewById(R.id.txVehicleLicencePlate);
        etDetail = findViewById(R.id.etDetail);
        etDate = findViewById(R.id.etDate);
        etOdometer = findViewById(R.id.etOdometer);
        etValue = findViewById(R.id.etValue);
        etLocation = findViewById(R.id.etLocation);
        etNote = findViewById(R.id.etNote);

        labelMaintenanceItem = findViewById(R.id.labelMaintenanceItem);
        listMaintenanceItem = findViewById(R.id.listMaintenanceItem);

        Vehicle vehicle;
        if (opInsert) {
            if (nrVehicle_id == 0){
                Globals g = Globals.getInstance();
                nrVehicle_id = g.getIdVehicle();
            }
        } else {
            nrVehicle_id = maintenance.getVehicle_id();
        }
        vehicle = Database.mVehicleDao.fetchVehicleById(nrVehicle_id);
        txVehicleName.setText(vehicle.getName());
        txVehicleLicencePlate.setText(vehicle.getLicense_plate());
        imVehicleType.setImageResource(vehicle.getVehicleTypeImage(vehicle.getVehicle_type()));

        etDate.addTextChangedListener(new DateInputMask(etDate));

        @SuppressLint("InflateParams") View vL = getLayoutInflater().inflate(R.layout.activity_list_maintenance_item, null);
        labelMaintenanceItem.removeAllViews();
        ImageView lblServiceType = vL.findViewById(R.id.imgServiceType);
        TextView lblExpiration = vL.findViewById(R.id.txtExpiration);
        TextView lblValue = vL.findViewById(R.id.txtValue);
        TextView lblNote = vL.findViewById(R.id.txtNote);
        ImageButton btnDelete = vL.findViewById(R.id.btnDelete);

        lblServiceType.setImageResource(0);
        lblNote.setText(getString(R.string.MaintenanceItem_Note));
        lblValue.setText(getString(R.string.MaintenanceItem_Value));
        lblExpiration.setText(getString(R.string.MaintenanceItem_Expiration));
        btnDelete.setVisibility(View.INVISIBLE);
        labelMaintenanceItem.addView(vL);
        labelMaintenanceItem.setBackgroundColor(Color.rgb(209,193,233));

        if (!opInsert) {
            adapterMaintenanceItem = new MaintenanceItemListAdapter(Database.mMaintenanceItemDao.fetchMaintenanceItemByMaintenance(maintenance.getId()), getApplicationContext());
            listMaintenanceItem.setAdapter(adapterMaintenanceItem);
            listMaintenanceItem.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        }

        if (maintenance != null) {
            etDetail.setText((maintenance.getDetail()));
            etDate.setText(Utils.dateToString(maintenance.getDate()));
            etOdometer.setText(String.valueOf(maintenance.getOdometer()));
            etValue.setText(String.valueOf(maintenance.getValue()));
            etLocation.setText(maintenance.getLocation());
            etNote.setText(maintenance.getNote());
        }
    }

    public void addListenerOnButtonAdd() {
        ImageButton btAddMaintenanceItem = findViewById(R.id.btAddMaintenanceItem);

        btAddMaintenanceItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(v.getContext());
                View promptsView = li.inflate(R.layout.activity_maintenance_item, null);

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());

                alertDialogBuilder.setView(promptsView);
                final Spinner spinServiceType = (Spinner) promptsView.findViewById(R.id.spinServiceType);
                final Spinner spinMeasureType = (Spinner) promptsView.findViewById(R.id.spinMeasureType);
                final EditText etExpiration_value = (EditText) promptsView.findViewById(R.id.etExpiration_value);
                final EditText etValue = (EditText) promptsView.findViewById(R.id.etValue);
                final EditText etNote = (EditText) promptsView.findViewById(R.id.etNote);

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                boolean isSave = false;

                                MaintenanceItem mI = new MaintenanceItem();

                                mI.setMaintenance_id(maintenance.getId());
                                mI.setService_type(spinServiceType.getSelectedItemPosition());
                                mI.setMeasure_type(spinMeasureType.getSelectedItemPosition());
                                if (!etExpiration_value.getText().toString().isEmpty()) { mI.setExpiration_value(Integer.parseInt(etExpiration_value.getText().toString()));}
                                if (!etValue.getText().toString().isEmpty())            { mI.setValue(Double.parseDouble(etValue.getText().toString()));}
                                mI.setNote(etNote.getText().toString());

                                try {
                                     isSave = Database.mMaintenanceItemDao.addMaintenanceItem(mI);
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), R.string.Error_Including_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                                setResult(isSave ? 1 : 0);
                                if (!isSave) {
                                    Toast.makeText(getApplicationContext(), R.string.Error_Saving_Data, Toast.LENGTH_LONG).show();
                                } else {
                                    adapterMaintenanceItem = new MaintenanceItemListAdapter(Database.mMaintenanceItemDao.fetchMaintenanceItemByMaintenance(maintenance.getId()), getApplicationContext());
                                    listMaintenanceItem.setAdapter(adapterMaintenanceItem);
                                    adapterMaintenanceItem.notifyDataSetChanged();
                                }
                            }
                        })
                        .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
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
                    m1.setDetail(etDetail.getText().toString());
                    m1.setDate(Utils.stringToDate(etDate.getText().toString()));
                    m1.setOdometer(Integer.parseInt(etOdometer.getText().toString()));
                    m1.setValue(Double.valueOf(etValue.getText().toString()));
                    m1.setLocation(etLocation.getText().toString());
                    m1.setNote(etNote.getText().toString());

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
            if (!etDate.getText().toString().trim().isEmpty()
//                && !etDetail.getText().toString().trim().isEmpty()
                    && !etOdometer.getText().toString().trim().isEmpty()
                    && !etValue.getText().toString().trim().isEmpty()
                    && !etLocation.getText().toString().trim().isEmpty()
//                && !etNote.getText().toString().trim().isEmpty()
//                && !tvStats.getText().toString().trim().isEmpty()
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