package com.jacksonasantos.travelplan.ui.vehicle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.jacksonasantos.travelplan.DAO.DAO;
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

        etOIDVehicle = (EditText) findViewById(R.id.etOIDVehicle);
        etNameVehicle = (EditText) findViewById(R.id.etNameVehicle);
        etLicencePlateVehicle = (EditText) findViewById(R.id.etLicencePlateVehicle);
        etFullCapacity = (EditText) findViewById(R.id.etFullCapacity);
        etAVGConsumption = (EditText) findViewById(R.id.etAVGConsumption);

        Intent intent = getIntent();
        vehicle = (Vehicle) intent.getSerializableExtra("vehicle");
        if (vehicle != null) {
            etOIDVehicle.setText(vehicle.oid_vehicle);
            etNameVehicle.setText(vehicle.name_vehicle);
            etLicencePlateVehicle.setText(vehicle.license_plate);
            etFullCapacity.setText((int) vehicle.full_capacity);
            etAVGConsumption.setText((int) vehicle.avg_consumption);
        }
    }

    public void save_vehicle(View v){
        DAO db = new DAO(this);
        db.abrir();

        if (vehicle != null){
            db.atualizaVehicle(vehicle.id, etOIDVehicle.getText().toString(),
            etNameVehicle.getText().toString(),
            etLicencePlateVehicle.getText().toString(),
            Integer.parseInt(etFullCapacity.getText().toString()),
            Double.parseDouble(etAVGConsumption.getText().toString()));
        } else {
            db.insereVehicle(etOIDVehicle.getText().toString(),
                    etNameVehicle.getText().toString(),
                    etLicencePlateVehicle.getText().toString(),
                    Integer.parseInt(etFullCapacity.getText().toString()),
                    Double.parseDouble(etAVGConsumption.getText().toString()));
        }

        db.fechar();
        setResult(VehicleViewModel.REQUEST_SALVOU);
        finish();
    }

}