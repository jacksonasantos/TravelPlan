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

    private int rbType;
    private EditText etNameVehicle;
    private EditText etShortNameVehicle;
    private EditText etLicencePlateVehicle;
    private EditText etBrand;            // TODO - Implement API of BRAND´s
    private AutoCompleteTextView spinTypeFuel;
    private int nrspinTypeFuel;
    private EditText etFullCapacity;
    private EditText etAVGConsumption;
    private EditText etAcquisition;
    private EditText etSale;
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

        addListenerOnButton();

        RadioGroup rgType = findViewById(R.id.rgType);
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
            etLicencePlateVehicle.setText(vehicle.getLicense_plate());
            etBrand.setText(vehicle.getBrand());
            nrspinTypeFuel=vehicle.getType_fuel();
            spinTypeFuel.setText(getResources().getStringArray(R.array.type_fuel_array)[nrspinTypeFuel],false);
            etFullCapacity.setText(String.valueOf(vehicle.getFull_capacity()));
            etAVGConsumption.setText(String.valueOf(vehicle.getAvg_consumption()));
            etAcquisition.setText(Utils.dateToString(vehicle.getDt_acquisition()));
            etSale.setText(Utils.dateToString(vehicle.getDt_sale()));
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

    public void addListenerOnButton() {
       Button btSaveVehicle = findViewById(R.id.btSaveVehicle);

        btSaveVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSave = false;
                if (!validateData()) {
                    Toast.makeText(getApplicationContext(), "Erro na Validação dos Dados... ", Toast.LENGTH_LONG).show();
                } else {
                    Database mdb = new Database(VehicleActivity.this);
                    mdb.open();
                    final Vehicle v1 = new Vehicle();
                    v1.setType(findViewById(rbType).getId());
                    v1.setName(etNameVehicle.getText().toString());
                    v1.setShort_name(etShortNameVehicle.getText().toString());
                    v1.setLicense_plate(etLicencePlateVehicle.getText().toString());
                    v1.setBrand(etBrand.getText().toString());
                    v1.setType_fuel(nrspinTypeFuel);
                    if (!etFullCapacity.getText().toString().isEmpty()) {
                        v1.setFull_capacity(Integer.parseInt(etFullCapacity.getText().toString()));
                    } else { v1.setFull_capacity(0); }
                    if (!etAVGConsumption.getText().toString().isEmpty()) {
                        v1.setAvg_consumption(Float.parseFloat(etAVGConsumption.getText().toString()));
                    } else { v1.setAvg_consumption((float) 0); }
                    v1.setDt_acquisition(Utils.stringToDate(etAcquisition.getText().toString()));
                    v1.setDt_sale(Utils.stringToDate((etSale.getText().toString())));
                    v1.setDt_odometer(Utils.stringToDate((etDtOdometer.getText().toString())));
                    if (!etOdometer.getText().toString().isEmpty()) {
                        v1.setOdometer(Integer.parseInt(etOdometer.getText().toString()));
                    } else { v1.setOdometer(0); }

                    if (!opInsert) {
                        try {
                            v1.setId(vehicle.getId());
                            isSave = Database.mVehicleDao.updateVehicle(v1);
                        } catch (Exception e ){
                            Toast.makeText(getApplicationContext(), "Erro Alterando os Dados "+e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        try {
                            isSave = Database.mVehicleDao.addVehicle(v1);
                        } catch ( Exception e ) {
                            Toast.makeText(getApplicationContext(), "Erro Incluindo os Dados "+e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    mdb.close();
                    setResult(isSave ? 1 : 0);
                    if (isSave ) { finish(); }
                }
            }
        });
    }
    private boolean validateData() {
        boolean isValid = false;

        try {
            String s1 = String.valueOf(findViewById(rbType).getId());
            String s2 = etNameVehicle.getText().toString();
            String s3 = etShortNameVehicle.getText().toString();
            String s4 = etLicencePlateVehicle.getText().toString();
            String s5 = etBrand.getText().toString();
            String s6 = String.valueOf(nrspinTypeFuel);
            String s7 = etFullCapacity.getText().toString();
            String s8 = etAVGConsumption.getText().toString();
            String s9 = etAcquisition.getText().toString();
            String s10 = etSale.getText().toString();
            String s11 = etDtOdometer.getText().toString();
            String s12 = etOdometer.getText().toString();

            if ( s1 != null && !s1.trim().isEmpty() &&
                 s2 != null && !s2.trim().isEmpty() &&
                 s3 != null && !s3.trim().isEmpty() &&
                 s4 != null && !s4.trim().isEmpty() &&
                 s5 != null && !s5.trim().isEmpty() &&
                 s6 != null  && !s6.trim().isEmpty() &&
                 s7 != null && !s7.trim().isEmpty() &&
                 // s8 != null  && !s8.trim().isEmpty() &&
                 // s9 != null  && !s9.trim().isEmpty() &&
                 // s10 != null  && !s10.trim().isEmpty() &&
                 s11 != null && !s11.trim().isEmpty() &&
                 s12 != null && !s12.trim().isEmpty())
            {
                 isValid = true;
            }
        }catch ( Exception e ) {
            Toast.makeText(getApplicationContext(), "Erro no Validador dos Dados "+e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return isValid;
    }
}