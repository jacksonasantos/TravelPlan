package com.jacksonasantos.travelplan.ui.vehicle;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.jacksonasantos.travelplan.dao.Database;
import com.jacksonasantos.travelplan.dao.Vehicle;
import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.ui.utility.Mask;
import com.jaredrummler.materialspinner.MaterialSpinner;

public class VehicleActivity extends AppCompatActivity {

    private RadioGroup rgType;
    private int rbType;
    private EditText etNameVehicle;
    private EditText etShortNameVehicle;
    private EditText etLicencePlateVehicle;
    private EditText etBrand;
    private MaterialSpinner spinTypeFuel;
    private String txSpinTypeFuel = null;
    private EditText etFullCapacity;
    private EditText etAVGConsumption;
    private EditText etAcquisition;
    private EditText etSale;
    private EditText etDtOdometer;
    private EditText etOdometer;
    private Vehicle vehicle = new Vehicle();
    private boolean opInsert = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.Vehicle_Id);
        setContentView(R.layout.activity_vehicle);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //Toast.makeText(getApplicationContext(), "Exemplo Extra " + extras.getString("type"), Toast.LENGTH_SHORT).show();
            vehicle.setId(extras.getLong("id"));
            vehicle.setType(extras.getInt("type"));
            vehicle.setName(extras.getString("name"));
            vehicle.setShort_name(extras.getString("short_name"));
            vehicle.setLicense_plate(extras.getString("license_plate"));
            vehicle.setBrand(extras.getString("brand"));
            vehicle.setType_fuel(extras.getString("type_fuel"));
            vehicle.setFull_capacity(extras.getInt("full_capacity"));
            vehicle.setAvg_consumption(extras.getDouble("avg_consumption"));
            vehicle.setDt_acquisition(extras.getString("dt_acquisition"));
            vehicle.setDt_sale(extras.getString("dt_sale"));
            vehicle.setDt_odometer(extras.getString("dt_odometer"));
            vehicle.setOdometer(extras.getInt("odometer"));
            opInsert = false;
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        addListenerOnButton();

        rgType = findViewById(R.id.rgType);
        etNameVehicle = findViewById(R.id.etNameVehicle);
        etShortNameVehicle = findViewById(R.id.etShortNameVehicle);
        etLicencePlateVehicle = findViewById(R.id.etLicencePlateVehicle);
        etBrand = findViewById(R.id.etBrand);
        spinTypeFuel = findViewById(R.id.spinTypeFuel);
        etFullCapacity = findViewById(R.id.etFullCapacity);
        etAVGConsumption = findViewById(R.id.etAVGConsumption);
        etAcquisition = findViewById(R.id.etAcquisition);
        etSale = findViewById(R.id.etSale);
        etDtOdometer = findViewById(R.id.etDtOdometer);
        etOdometer = findViewById(R.id.etOdometer);

        etAcquisition.addTextChangedListener(Mask.insert("##/##/####",etAcquisition));
        etSale.addTextChangedListener(Mask.insert("##/##/####",etSale));
        etDtOdometer.addTextChangedListener(Mask.insert("##/##/####",etDtOdometer));

        rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rbType = checkedId;
                //Toast.makeText(getApplicationContext(), checkedId+" - "+" - ",Toast.LENGTH_SHORT).show();
            }
        });

        spinTypeFuel.setItems(getResources().getStringArray(R.array.type_fuel_array));
        spinTypeFuel.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
             @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
             txSpinTypeFuel = item;
            }
        });

        if (vehicle != null) {
            rgType.check(vehicle.getType());
            etNameVehicle.setText(vehicle.getName());
            etShortNameVehicle.setText(vehicle.getShort_name());
            etLicencePlateVehicle.setText(vehicle.getLicense_plate());
            etBrand.setText(vehicle.getBrand());
            spinTypeFuel.setText(vehicle.getType_fuel());
            txSpinTypeFuel=vehicle.getType_fuel();
            etFullCapacity.setText(String.valueOf(vehicle.getFull_capacity()));
            etAVGConsumption.setText(String.valueOf(vehicle.getAvg_consumption()));
            etAcquisition.setText(String.valueOf(vehicle.getDt_acquisition()));
            etSale.setText(String.valueOf(vehicle.getDt_sale()));
            etDtOdometer.setText(String.valueOf(vehicle.getDt_odometer()));
            etOdometer.setText(String.valueOf(vehicle.getOdometer()));
        }
    }

    public void addListenerOnButton() {
       Button btSaveVehicle = findViewById(R.id.btSaveVehicle);

        btSaveVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSave = false;

                Database mdb = new Database(VehicleActivity.this);
                mdb.open();

                final Vehicle v1 = new Vehicle();
                v1.setType(rgType.getCheckedRadioButtonId());
                v1.setName(etNameVehicle.getText().toString());
                v1.setShort_name(etShortNameVehicle.getText().toString());
                v1.setLicense_plate(etLicencePlateVehicle.getText().toString());
                v1.setBrand(etBrand.getText().toString());
                v1.setType_fuel(txSpinTypeFuel);
                if (etFullCapacity.getText().toString()!=null && !etFullCapacity.getText().toString().isEmpty()) {
                    v1.setFull_capacity(Integer.parseInt(etFullCapacity.getText().toString()));
                } else { v1.setFull_capacity(0); }
                if (etAVGConsumption.getText().toString()!=null && !etAVGConsumption.getText().toString().isEmpty()) {
                    v1.setAvg_consumption(Double.parseDouble(etAVGConsumption.getText().toString()));
                } else { v1.setAvg_consumption((double) 0); }
                v1.setDt_acquisition(etAcquisition.getText().toString().replace("/","").trim());
                v1.setDt_sale(etSale.getText().toString().replace("/","").trim());
                v1.setDt_odometer(etDtOdometer.getText().toString().replace("/","").trim());
                if (etOdometer.getText().toString()!=null && !etOdometer.getText().toString().isEmpty()) {
                    v1.setOdometer(Integer.parseInt(etOdometer.getText().toString()));
                } else { v1.setOdometer(0); }

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
}