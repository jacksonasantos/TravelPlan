package com.jacksonasantos.travelplan.ui.vehicle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.jacksonasantos.travelplan.DAO.Database;
import com.jacksonasantos.travelplan.DAO.Vehicle;
import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.ui.utility.CustomOnItemSelectedListener;

public class VehicleActivity extends AppCompatActivity {

    private EditText etNameVehicle;
    private EditText etLicencePlateVehicle;
    private EditText etFullCapacity;
    private EditText etAVGConsumption;
    private Spinner spinTypeFuel;
    private EditText etBrand;
    private Vehicle vehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setTitle(R.string.vehicle_Id);

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
            //spinTypeFuel = spinTypeFuel.getSelectedItemId().getType_fuel();
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
        Button btSaveVehicle = findViewById(R.id.btSaveVehicle);

        btSaveVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSave = false;

                Database mdb = new Database(VehicleActivity.this);
                mdb.open();

                if (vehicle != null) {
//                    isSave = Database.mVehicleDao.updateVehicle(vehicle);
                } else {
                    try {
                        final Vehicle vehicle = new Vehicle();
                        vehicle.setName(etNameVehicle.getText().toString());
                        vehicle.setLicense_plate(etLicencePlateVehicle.getText().toString());
                        vehicle.setFull_capacity(Integer.parseInt(etFullCapacity.getText().toString()));
                        vehicle.setAvg_consumption(Double.parseDouble(etAVGConsumption.getText().toString()));
                        //vehicle.setType_fuel(vnome);
                        vehicle.setBrand(etBrand.getText().toString());

                        isSave = Database.mVehicleDao.addVehicle(vehicle);
                    } catch ( Exception e ) {
                        isSave = false;
                        Toast.makeText(getApplicationContext(), "Erro Salvando os Dados", Toast.LENGTH_LONG).show();
                    }
                }

                mdb.close();
                setResult(isSave ? 1 : 0);
                if (isSave ) { finish(); }
            }
        });
    }

    public void addListenerOnSpinnerItemSelection() {
        spinTypeFuel = findViewById(R.id.spinTypeFuel);
        spinTypeFuel.setOnItemSelectedListener(new CustomOnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //String vnome = spinTypeFuel.getSelectedItem().toString();
                //Toast.makeText(getApplicationContext(), "Nome: " + vnome, Toast.LENGTH_LONG).show();
            }
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
}