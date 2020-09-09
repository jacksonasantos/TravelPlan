package com.jacksonasantos.travelplan.ui.vehicle;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.jacksonasantos.travelplan.DAO.Database;
import com.jacksonasantos.travelplan.DAO.Vehicle;
import com.jacksonasantos.travelplan.R;
import com.jaredrummler.materialspinner.MaterialSpinner;

public class VehicleActivity extends AppCompatActivity {

    private EditText etNameVehicle;
    private EditText etLicencePlateVehicle;
    private EditText etFullCapacity;
    private EditText etAVGConsumption;
    private String txSpinTypeFuel = null;
    private EditText etBrand;
    private Vehicle vehicle = new Vehicle();
    private boolean opInsert = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.vehicle_Id);
        setContentView(R.layout.activity_vehicle);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
//            Toast.makeText(getApplicationContext(), "Exemplo Extra " + extras.getString("name"), Toast.LENGTH_SHORT).show();
            vehicle.setId(extras.getLong("id"));
            vehicle.setName(extras.getString("name"));
            vehicle.setLicense_plate(extras.getString("license_plate"));
            vehicle.setFull_capacity(extras.getInt("full_capacity"));
            vehicle.setAvg_consumption(extras.getDouble("avg_consumption"));
            vehicle.setBrand(extras.getString("brand"));
            vehicle.setType_fuel(extras.getString("type_fuel"));
            opInsert = false;
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        addListenerOnButton();
        addListenerOnSpinnerItemSelection();

        etNameVehicle = findViewById(R.id.etNameVehicle);
        etLicencePlateVehicle = findViewById(R.id.etLicencePlateVehicle);
        etFullCapacity = findViewById(R.id.etFullCapacity);
        etAVGConsumption = findViewById(R.id.etAVGConsumption);
        MaterialSpinner spinTypeFuel = findViewById(R.id.spinTypeFuel);
        etBrand = findViewById(R.id.etBrand);

        spinTypeFuel.setItems(getResources().getStringArray(R.array.type_fuel_array));
        spinTypeFuel.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
             @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
             txSpinTypeFuel = item;
            }
        });

        if (vehicle != null) {
            etNameVehicle.setText(vehicle.getName());
            etLicencePlateVehicle.setText(vehicle.getLicense_plate());
            etFullCapacity.setText(String.valueOf(vehicle.getFull_capacity()));
            etAVGConsumption.setText(String.valueOf(vehicle.getAvg_consumption()));
            spinTypeFuel.setText(vehicle.getType_fuel());
            etBrand.setText(vehicle.getBrand());
        }
    }

    public void addListenerOnButton() {
        etNameVehicle = findViewById(R.id.etNameVehicle);
        etLicencePlateVehicle = findViewById(R.id.etLicencePlateVehicle);
        etFullCapacity = findViewById(R.id.etFullCapacity);
        etAVGConsumption = findViewById(R.id.etAVGConsumption);
        MaterialSpinner spinTypeFuel = (MaterialSpinner) findViewById(R.id.spinTypeFuel);
        etBrand = findViewById(R.id.etBrand);
        Button btSaveVehicle = findViewById(R.id.btSaveVehicle);

        btSaveVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSave = false;

                Database mdb = new Database(VehicleActivity.this);
                mdb.open();

                final Vehicle v1 = new Vehicle();
                v1.setName(etNameVehicle.getText().toString());
                v1.setLicense_plate(etLicencePlateVehicle.getText().toString());
                v1.setFull_capacity(Integer.parseInt(etFullCapacity.getText().toString()));
                v1.setAvg_consumption(Double.parseDouble(etAVGConsumption.getText().toString()));
                v1.setType_fuel(txSpinTypeFuel);
                v1.setBrand(etBrand.getText().toString());

                if (!opInsert) {
                    try {
                        v1.setId(vehicle.getId());
                        isSave = Database.mVehicleDao.updateVehicle(v1);
                    } catch (Exception e ){
                        isSave = false;
                        Toast.makeText(getApplicationContext(), "Erro Alterando os Dados "+e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        isSave = Database.mVehicleDao.addVehicle(v1);
                    } catch ( Exception e ) {
                        isSave = false;
                        Toast.makeText(getApplicationContext(), "Erro Incluindo os Dados "+e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                mdb.close();
                setResult(isSave ? 1 : 0);
                if (isSave ) { finish(); }
            }
        });
    }

    public void addListenerOnSpinnerItemSelection() {
        MaterialSpinner spinTypeFuel = (MaterialSpinner) findViewById(R.id.spinTypeFuel);
        spinTypeFuel.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>(){
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
            }
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
}