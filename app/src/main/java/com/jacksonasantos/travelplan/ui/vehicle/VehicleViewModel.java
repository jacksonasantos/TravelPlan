package com.jacksonasantos.travelplan.ui.vehicle;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;

import com.jacksonasantos.travelplan.DAO.DAO;
import com.jacksonasantos.travelplan.DAO.Vehicle;
import com.jacksonasantos.travelplan.R;

import java.util.ArrayList;
import java.util.List;

public class VehicleViewModel extends ListActivity {
    private DAO db;
    private final List<Vehicle> vehicles = new ArrayList<Vehicle>();
    private VehicleAdapter vehicleAdapter;
    public static final int REQUEST_EDICAO = 0;
    public static final int REQUEST_SALVOU = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_vehicle);

        db = new DAO(this);
        lerVehicles();
    }

    public void lerVehicles(){
        db.abrir();
        vehicles.clear();
        Cursor cursor = db.retornaTodosVehicle();
        if (cursor.moveToFirst()){
            do {
                Vehicle a = new Vehicle();
                a.id = cursor.getInt (cursor.getColumnIndex(DAO.KEY_ID));
                a.oid_vehicle = cursor.getString (cursor.getColumnIndex(DAO.KEY_OID_VEHICLE));
                a.name_vehicle = cursor.getString (cursor.getColumnIndex(DAO.KEY_NAME_VEHICLE));
                a.license_plate = cursor.getString (cursor.getColumnIndex(DAO.KEY_LICENCE_PLATE));
                a.full_capacity = cursor.getInt (cursor.getColumnIndex(DAO.KEY_FULL_CAPACITY));
                a.avg_consumption = cursor.getDouble (cursor.getColumnIndex(DAO.KEY_AVG_CONSUMPTION));
                vehicles.add(a);
            } while (cursor.moveToNext());
        }

        if (vehicles.size() > 0){
            if (vehicleAdapter == null){
                vehicleAdapter = new VehicleAdapter(this, vehicles){
                    @Override
                    public void edita(Vehicle vehicle) {
                        Intent intent = new Intent(getApplicationContext(), VehicleActivity.class);
                        intent.putExtra("vehicleDAO", vehicle);
                        startActivityForResult(intent, REQUEST_EDICAO);
                    }

                    @Override
                    public void deleta(Vehicle vehicle) {
                        db.abrir();
                        db.apagaVehicle(vehicle.id);
                        db.fechar();
                        lerVehicles();
                    }
                };
                setListAdapter(vehicleAdapter);
            } else {
                vehicleAdapter.novosDados(vehicles);
            }
        }
        db.fechar();
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (item.getItemId() == R.id.menu_add){

            Intent intent = new Intent(this, VehicleActivity.class);
            startActivityForResult(intent, REQUEST_EDICAO);
            return true;
        } else {
            return super.onMenuItemSelected(featureId, item);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EDICAO) {
            if (resultCode == REQUEST_SALVOU) {
                lerVehicles();
            }
        }
    }
}