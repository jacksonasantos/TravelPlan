package com.jacksonasantos.travelplan.ui.vehicle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.jacksonasantos.travelplan.DAO.Database;
import com.jacksonasantos.travelplan.DAO.Vehicle;
import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.ui.utility.CustomOnItemSelectedListener;

public class VehicleActivity extends Activity {

    private EditText etNameVehicle;
    private EditText etLicencePlateVehicle;
    private EditText etFullCapacity;
    private EditText etAVGConsumption;
    private Spinner spinTypeFuel;
    private EditText etBrand;
    private Button btSaveVehicle;
    private Vehicle vehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle);
        setTitle("Identificação do Veículo");

        addListenerOnButton();
        addListenerOnSpinnerItemSelection();

        etNameVehicle = findViewById(R.id.etNameVehicle);
        etLicencePlateVehicle = findViewById(R.id.etLicencePlateVehicle);
        etFullCapacity = findViewById(R.id.etFullCapacity);
        etAVGConsumption = findViewById(R.id.etAVGConsumption);
        spinTypeFuel = findViewById(R.id.spinTypeFuel);
        etBrand = findViewById(R.id.etBrand);

        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this,
                R.array.type_fuel_array, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTypeFuel.setAdapter(adapterSpinner);

        Intent intent = getIntent();
        vehicle = (Vehicle) intent.getSerializableExtra("vehicle");
        if (vehicle != null) {
            etNameVehicle.setText(vehicle.name);
            etLicencePlateVehicle.setText(vehicle.license_plate);
            etFullCapacity.setText(vehicle.full_capacity);
            etAVGConsumption.setText((int) vehicle.avg_consumption);
            //spintypefuel.setText(vehicle.type_fuel);
            etBrand.setText(vehicle.brand);
        }
    }

    public void addListenerOnButton() {
        etNameVehicle = findViewById(R.id.etNameVehicle);
        etLicencePlateVehicle = findViewById(R.id.etLicencePlateVehicle);
        etFullCapacity = findViewById(R.id.etFullCapacity);
        etAVGConsumption = findViewById(R.id.etAVGConsumption);
        spinTypeFuel = findViewById(R.id.spinTypeFuel);
        etBrand = findViewById(R.id.etBrand);
        btSaveVehicle = findViewById(R.id.btSaveVehicle);

        btSaveVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSave = false;

                Database mdb = new Database(VehicleActivity.this);
                mdb.open();

/*                Toast.makeText(VehicleActivity.this,
                        "ADD : " +
                                "\nNome : "+ vehicle.getName() +
                                "\nPlaca : "+ vehicle.getLicense_plate() +
                                "\ntanque : "+ vehicle.getFull_capacity() +
                                "\navg : "+vehicle.getAvg_consumption() +
                                "\nbrand : "+vehicle.getBrand(),
                        Toast.LENGTH_LONG).show();
*/
                Spinner spinner = (Spinner) findViewById(R.id.spinTypeFuel);
                if (vehicle != null){
//                    isSave = Database.mVehicleDao.updateVehicle(vehicle);
                } else {
                    Vehicle vehicle = new Vehicle();
                    vehicle.setName(etNameVehicle.getText().toString());
                    vehicle.setLicense_plate(etLicencePlateVehicle.getText().toString());
                    vehicle.setFull_capacity(Integer.parseInt(etFullCapacity.getText().toString()));
                    vehicle.setAvg_consumption(Double.parseDouble(etAVGConsumption.getText().toString()));
                    //vehicle.type_fuel = spinner.setOnItemSelectedListener();
                    vehicle.setBrand(etBrand.getText().toString());

                    isSave = Database.mVehicleDao.addVehicle(vehicle);
                }

                mdb.close();
                setResult(isSave ? 1 : 0);
                finish();
            }
        });
    }

    public void addListenerOnSpinnerItemSelection() {
        spinTypeFuel = (Spinner) findViewById(R.id.spinTypeFuel);
        spinTypeFuel.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }
}