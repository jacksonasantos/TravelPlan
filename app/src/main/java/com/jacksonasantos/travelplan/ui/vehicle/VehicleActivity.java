package com.jacksonasantos.travelplan.ui.vehicle;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.jacksonasantos.travelplan.DAO.Database;
import com.jacksonasantos.travelplan.DAO.Vehicle;
import com.jacksonasantos.travelplan.R;

public class VehicleActivity extends Activity {

    private EditText etOIDVehicle;
    private EditText etNameVehicle;
    private EditText etLicencePlateVehicle;
    private EditText etFullCapacity;
    private EditText etAVGConsumption;
    private Vehicle vehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle);

        etOIDVehicle = findViewById(R.id.etOIDVehicle);
        etNameVehicle = findViewById(R.id.etNameVehicle);
        etLicencePlateVehicle = findViewById(R.id.etLicencePlateVehicle);
        etFullCapacity = findViewById(R.id.etFullCapacity);
        etAVGConsumption = findViewById(R.id.etAVGConsumption);

        Intent intent = getIntent();
        vehicle = (Vehicle) intent.getSerializableExtra("vehicle");
        if (vehicle != null) {
            etOIDVehicle.setText(vehicle.oid);
            etNameVehicle.setText(vehicle.name);
            etLicencePlateVehicle.setText(vehicle.license_plate);
            etFullCapacity.setText((int) vehicle.full_capacity);
            etAVGConsumption.setText((int) vehicle.avg_consumption);
        }
    }
/*
    public void save_vehicle(View v){
        Database db = new Database(this);
        db.open();

        if (vehicle != null){
            Vehicle.atualizaVehicle(db, vehicle.id, etOIDVehicle.getText().toString(),
            etNameVehicle.getText().toString(),
            etLicencePlateVehicle.getText().toString(),
            Integer.parseInt(etFullCapacity.getText().toString()),
            Double.parseDouble(etAVGConsumption.getText().toString()));
        } else {
            Vehicle.insereVehicle(db, etOIDVehicle.getText().toString(),
                    etNameVehicle.getText().toString(),
                    etLicencePlateVehicle.getText().toString(),
                    Integer.parseInt(etFullCapacity.getText().toString()),
                    Double.parseDouble(etAVGConsumption.getText().toString()));
        }

        db.close();
        setResult(VehicleViewModel.REQUEST_SALVOU);
        finish();
    }
*/
}