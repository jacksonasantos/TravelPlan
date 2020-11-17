package com.jacksonasantos.travelplan.ui.vehicle;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.jacksonasantos.travelplan.dao.Database;
import com.jacksonasantos.travelplan.dao.Vehicle;
import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.ui.utility.DateInputMask;
import com.jacksonasantos.travelplan.ui.utility.Utils;

public class VehicleActivity extends AppCompatActivity {

    private RadioGroup rgType;
    private int rbType;
    private EditText etNameVehicle;
    private EditText etShortNameVehicle;
    private EditText etBrand;                            // TODO - Implement API of BRAND´s
    private EditText etModel;
    private AutoCompleteTextView spinTypeFuel;
    private int nrspinTypeFuel;

    private EditText etYearModel;
    private EditText etYearManufacture;
    private EditText etLicencePlateVehicle;
    private EditText etColor;
    private EditText etVin;
    private EditText etLicenceNumber;
    private EditText etStateVehicle;
    private EditText etCityVehicle;
    private EditText etAcquisition;
    private EditText etSale;

    private EditText etDoors;
    private EditText etCapacity;
    private EditText etPower;
    private EditText etEstimatedValue;
    private EditText etFullCapacity;
    private EditText etAVGConsumption;
    private EditText etDtOdometer;
    private EditText etOdometer;

    private Vehicle vehicle;
    private boolean opInsert = true;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.Vehicle_Id);
        setContentView(R.layout.activity_vehicle);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            vehicle = new Vehicle();
            vehicle.setId(extras.getLong("id"));
            vehicle = Database.mVehicleDao.fetchVehicleById(vehicle.getId());
            opInsert = false;
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        addListenerOnButtonSave();

        rgType = findViewById(R.id.rgType);
        etNameVehicle = findViewById(R.id.etNameVehicle);
        etShortNameVehicle = findViewById(R.id.etShortNameVehicle);
        etBrand = findViewById(R.id.etBrand);
        etModel = findViewById(R.id.etModel);
        spinTypeFuel = findViewById(R.id.spinTypeFuel);
        etYearModel = findViewById(R.id.etYearModel);
        etYearManufacture = findViewById(R.id.etYearManufacture);
        etLicencePlateVehicle = findViewById(R.id.etLicencePlateVehicle);
        etColor = findViewById(R.id.etColor);
        etVin = findViewById(R.id.etVin);
        etLicenceNumber = findViewById(R.id.etLicenceNumber);
        etStateVehicle = findViewById(R.id.etStateVehicle);
        etCityVehicle = findViewById(R.id.etCityVehicle);
        etAcquisition = findViewById(R.id.etAcquisition);
        etSale = findViewById(R.id.etSale);
        etDoors = findViewById(R.id.etDoors);
        etCapacity = findViewById(R.id.etCapacity);
        etPower = findViewById(R.id.etPower);
        etEstimatedValue = findViewById(R.id.etEstimatedValue);
        etFullCapacity = findViewById(R.id.etFullCapacity);
        etAVGConsumption = findViewById(R.id.etAVGConsumption);
        etDtOdometer = findViewById(R.id.etDtOdometer);
        etOdometer = findViewById(R.id.etOdometer);

        addRadioButtonResources(R.array.type_vehicle_array, rgType);
        rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rbType = checkedId;
            }
        });
        addSpinnerResources(R.array.type_fuel_array, spinTypeFuel);
        nrspinTypeFuel = 0;
        spinTypeFuel.setOnItemClickListener(new Spinner.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                nrspinTypeFuel = (int) adapterView.getItemIdAtPosition(i);
            }
        });
        etAcquisition.addTextChangedListener(new DateInputMask(etAcquisition));
        etSale.addTextChangedListener(new DateInputMask(etSale));
        etDtOdometer.addTextChangedListener(new DateInputMask(etDtOdometer));

        if (vehicle != null) {
            rgType.check(vehicle.getType());
            etNameVehicle.setText(vehicle.getName());
            etShortNameVehicle.setText(vehicle.getShort_name());
            etBrand.setText(vehicle.getBrand());
            etModel.setText(vehicle.getModel());
            nrspinTypeFuel=vehicle.getType_fuel();
            spinTypeFuel.setText(getResources().getStringArray(R.array.type_fuel_array)[nrspinTypeFuel],false);
            etYearModel.setText(vehicle.getYear_model());
            etYearManufacture.setText(vehicle.getYear_manufacture());
            etLicencePlateVehicle.setText(vehicle.getLicense_plate());
            etColor.setText(vehicle.getColor());
            etVin.setText(vehicle.getVin());
            etLicenceNumber.setText(vehicle.getLicence_number());
            etStateVehicle.setText(vehicle.getState());
            etCityVehicle.setText(vehicle.getCity());
            etAcquisition.setText(Utils.dateToString(vehicle.getDt_acquisition()));
            etSale.setText(Utils.dateToString(vehicle.getDt_sale()));
            etDoors.setText(String.valueOf(vehicle.getDoors()));
            etCapacity.setText(String.valueOf(vehicle.getCapacity()));
            etPower.setText(String.valueOf(vehicle.getPower()));
            etEstimatedValue.setText(String.valueOf(vehicle.getEstimated_value()));
            etFullCapacity.setText(String.valueOf(vehicle.getFull_capacity()));
            etAVGConsumption.setText(String.valueOf(vehicle.getAvg_consumption()));
            etDtOdometer.setText(Utils.dateToString(vehicle.getDt_odometer()));
            etOdometer.setText(String.valueOf(vehicle.getOdometer()));
        }
    }

    private void addSpinnerResources(int resource_array, AutoCompleteTextView spin) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.select_dialog_item,
                getResources().getStringArray(resource_array));
        spin.setAdapter(adapter);
    }

    private void addRadioButtonResources(int resource_array, RadioGroup rg) {
        int i = 0;
        String[] S = getResources().getStringArray(resource_array);
        for (String item : S) {
            RadioButton newRadio = createRadioButton(item, ++i);
            rg.addView(newRadio);
        }
    }

    @NonNull
    private RadioButton createRadioButton(String txt, int i) {
        RadioButton nRadio = new RadioButton(this );
        LinearLayout.LayoutParams params = new RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.WRAP_CONTENT,
                RadioGroup.LayoutParams.WRAP_CONTENT);
        nRadio.setLayoutParams(params);
        nRadio.setText(txt); // define o texto
        nRadio.setId(i);     // define o codigo - sequencia do for
        return nRadio;
    }

    public void addListenerOnButtonSave() {
       Button btSaveVehicle = findViewById(R.id.btSaveVehicle);

        btSaveVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSave = false;
                if (!validateData()) {
                    Toast.makeText(getApplicationContext(), R.string.Error_Data_Validation, Toast.LENGTH_LONG).show();
                } else {
                    Database mdb = new Database(VehicleActivity.this);
                    mdb.open();
                    final Vehicle v1 = new Vehicle();
                    v1.setType(rbType);
                    v1.setName(etNameVehicle.getText().toString());
                    v1.setShort_name(etShortNameVehicle.getText().toString());
                    v1.setBrand(etBrand.getText().toString());
                    v1.setModel(etModel.getText().toString());
                    v1.setType_fuel(nrspinTypeFuel);
                    v1.setYear_model(etYearModel.getText().toString());
                    v1.setYear_manufacture(etYearManufacture.getText().toString());
                    v1.setLicense_plate(etLicencePlateVehicle.getText().toString());
                    v1.setColor(etColor.getText().toString());
                    v1.setVin(etVin.getText().toString());
                    v1.setLicence_number(etLicenceNumber.getText().toString());
                    v1.setState(etStateVehicle.getText().toString());
                    v1.setCity(etCityVehicle.getText().toString());
                    v1.setDt_acquisition(Utils.stringToDate(etAcquisition.getText().toString()));
                    v1.setDt_sale(Utils.stringToDate(etSale.getText().toString()));
                    if (!etDoors.getText().toString().isEmpty()) {
                        v1.setDoors(Integer.parseInt(etDoors.getText().toString()));
                    } else { v1.setDoors(0); }
                    if (!etCapacity.getText().toString().isEmpty()) {
                        v1.setCapacity(Integer.parseInt(etCapacity.getText().toString()));
                    } else { v1.setCapacity(0); }
                    if (!etPower.getText().toString().isEmpty()) {
                        v1.setPower(Integer.parseInt(etPower.getText().toString()));
                    } else { v1.setPower(0); }
                    if (!etEstimatedValue.getText().toString().isEmpty()) {
                        v1.setEstimated_value(Double.parseDouble(etEstimatedValue.getText().toString()));
                    } else { v1.setEstimated_value(0); }
                    if (!etFullCapacity.getText().toString().isEmpty()) {
                        v1.setFull_capacity(Integer.parseInt(etFullCapacity.getText().toString()));
                    } else { v1.setFull_capacity(0); }
                    if (!etAVGConsumption.getText().toString().isEmpty()) {
                        v1.setAvg_consumption(Float.parseFloat(etAVGConsumption.getText().toString()));
                    } else { v1.setAvg_consumption(0); }
                    v1.setDt_odometer(Utils.stringToDate(etDtOdometer.getText().toString()));
                    if (!etOdometer.getText().toString().isEmpty()) {
                        v1.setOdometer(Integer.parseInt(etOdometer.getText().toString()));
                    } else { v1.setOdometer(0); }

                    if (!opInsert) {
                        try {
                            v1.setId(vehicle.getId());
                            isSave = Database.mVehicleDao.updateVehicle(v1);
                        } catch (Exception e ){
                            Toast.makeText(getApplicationContext(), R.string.Error_Changing_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        try {
                            isSave = Database.mVehicleDao.addVehicle(v1);
                        } catch ( Exception e ) {
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
        boolean isValid = true;
        try {
            if ( rbType==0 ||
                etNameVehicle.getText().toString().isEmpty() ||
                etShortNameVehicle.getText().toString().isEmpty() ||
                etBrand.getText().toString().isEmpty() ||
                etModel.getText().toString().isEmpty() ||
                String.valueOf(nrspinTypeFuel).isEmpty() ||
                etYearModel.getText().toString().isEmpty() ||
                etYearManufacture.getText().toString().isEmpty() ||
                etLicencePlateVehicle.getText().toString().isEmpty() ||
                etColor.getText().toString().isEmpty() ||
                etVin.getText().toString().isEmpty() ||
                etLicenceNumber.getText().toString().isEmpty() ||
                etStateVehicle.getText().toString().isEmpty() ||
                etCityVehicle.getText().toString().isEmpty() ||
                etAcquisition.getText().toString().isEmpty() ||
                //etSale.getText().toString().isEmpty() ||
                etDoors.getText().toString().isEmpty() ||
                etCapacity.getText().toString().isEmpty() ||
                etPower.getText().toString().isEmpty() ||
                //etEstimatedValue.getText().toString().isEmpty() ||
                etFullCapacity.getText().toString().isEmpty() //||
                //etAVGConsumption.getText().toString().isEmpty() ||
                //etDtOdometer.getText().toString().isEmpty() ||
                //etOdometer.getText().toString().isEmpty()
            ){
                isValid = false;
            }
        }catch ( Exception e ) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Data_Validator_Error )+" - " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return isValid;
    }
}