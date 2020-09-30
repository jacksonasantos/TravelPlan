package com.jacksonasantos.travelplan.ui.vehicle;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Database;
import com.jacksonasantos.travelplan.dao.FuelSupply;
import com.jacksonasantos.travelplan.dao.Vehicle;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Mask;

public class FuelSupplyActivity extends AppCompatActivity {

    private Long nrVehicleId=0L;
    private TextView txVehicleName;
    private ImageView imVehicleType;
    private TextView txVehicleLicencePlate;
    private EditText etGasStation;
    private EditText etGasStationLocation;
    private EditText etSupplyDate;
    private EditText etNumberLiters;
    private Spinner spinCombustible;
    private int nrSpinCombustible;
    private CheckBox cbFullTank;
    private int vlFullTank = 0;
    private Spinner spinCurrencyType;
    private int nrSpinCurrencyType;
    private EditText etCurrencyValue;
    private EditText etSupplyValue;
    private EditText etFuelValue;
    private EditText etVehicleOdometer;
    private EditText etVehicleTravelledDistance;
    private TextView txStatAvgFuelConsumption;
    private TextView txStatCostPerLitre;
    private RadioGroup rgSupplyReasonType;
    private int rbSupplyReasonType;
    private EditText etSupplyReason;
    private Spinner spinAssociatedTrip;
    private int nrSpinAssociatedTrip;

    private FuelSupply fuelSupply;
    private boolean opInsert = true;
    private Menu menu;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.FuelSupply_Vehicle);
        setContentView(R.layout.activity_fuel_supply);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fuelSupply = new FuelSupply();
            fuelSupply.setVehicle_id(extras.getLong("vehicle_id"));
            nrVehicleId = extras.getLong("vehicle_id");
            if (extras.getLong( "fuel_supply_id") > 0) {
                fuelSupply.setId(extras.getLong("fuel_supply_id"));
                FuelSupply f = Database.mFuelSupplyDao.fetchFuelSupplyById(fuelSupply.getId());
                fuelSupply.setGas_station(f.gas_station);
                fuelSupply.setGas_station_location(f.gas_station_location);
                fuelSupply.setCombustible(f.combustible);
                fuelSupply.setSupply_date(f.supply_date);
                fuelSupply.setFull_tank(f.full_tank);
                fuelSupply.setCurrency_type(f.currency_type);
//            etCurrencyValue
                fuelSupply.setNumber_liters(f.number_liters);
                fuelSupply.setSupply_value(f.supply_value);
                fuelSupply.setFuel_value(f.fuel_value);
                fuelSupply.setVehicle_odometer(f.vehicle_odometer);
                fuelSupply.setVehicle_travelled_distance(f.vehicle_travelled_distance);
                fuelSupply.setStat_avg_fuel_consumption(f.stat_avg_fuel_consumption);
                fuelSupply.setStat_cost_per_litre(f.stat_cost_per_litre);
                fuelSupply.setSupply_reason(f.supply_reason);
                fuelSupply.setSupply_reason_type(f.supply_reason_type);
                fuelSupply.setAssociated_trip(f.associated_trip);
            }
            if (extras.getLong( "fuel_supply_id") > 0) {
                opInsert = false;
            }
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        addListenerOnButtonSave();

        txVehicleName = findViewById(R.id.txVehicleName);
        imVehicleType = findViewById(R.id.imVehicleType);
        txVehicleLicencePlate = findViewById(R.id.txVehicleLicencePlate);
        etGasStation = findViewById(R.id.etGasStation);
        etGasStationLocation = findViewById(R.id.etGasStationLocation);
        spinCombustible = findViewById(R.id.spinCombustible);
        etSupplyDate = findViewById(R.id.etSupplyDate);
        cbFullTank = findViewById(R.id.cbFullTank);
        spinCurrencyType = findViewById(R.id.spinCurrencyType);
        etCurrencyValue = findViewById(R.id.etCurrencyValue);
        etNumberLiters = findViewById(R.id.etNumberLiters);
        etSupplyValue = findViewById(R.id.etSupplyValue);
        etFuelValue = findViewById(R.id.etFuelValue);
        etVehicleOdometer = findViewById(R.id.etVehicleOdometer);
        etVehicleTravelledDistance = findViewById(R.id.etVehicleTravelledDistance);
        txStatAvgFuelConsumption = findViewById(R.id.txStatAvgFuelConsumption);
        txStatCostPerLitre = findViewById(R.id.txStatCostPerLitre);
        rgSupplyReasonType = findViewById(R.id.rgSupplyReasonType);
        etSupplyReason = findViewById(R.id.etSupplyReason);
        spinAssociatedTrip = findViewById(R.id.spinAssociatedTrip);

        Vehicle vehicle;
        if (opInsert) {
            if (nrVehicleId == 0L){
                Globals g = Globals.getInstance();
                nrVehicleId = g.getIdVehicle();
            }
        } else {
            nrVehicleId = fuelSupply.getVehicle_id();
            spinCombustible.setSelection(fuelSupply.getCombustible());
            spinCurrencyType.setSelection(fuelSupply.getCurrency_type());
        }
        vehicle = Database.mVehicleDao.fetchVehicleById(nrVehicleId);
        txVehicleName.setText(vehicle.getName());
        txVehicleLicencePlate.setText(vehicle.getLicense_plate());
        imVehicleType.setImageResource(vehicle.getTypeImage(vehicle.getType()));
        etSupplyDate.addTextChangedListener(Mask.insert("##/##/####", etSupplyDate));
        spinCombustible.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                nrSpinCombustible = (int) adapterView.getItemIdAtPosition(i);
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {
                nrSpinCombustible = 0;
            }

        });
        cbFullTank.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v){
                    if (cbFullTank.isChecked()){
                        vlFullTank = 1;
                    }
                    else {
                        vlFullTank = 0;
                    }
                }
        });
        spinCurrencyType.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                nrSpinCurrencyType = (int) adapterView.getItemIdAtPosition(i);
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {
                nrSpinCurrencyType = 0;
            }
        });
/*        if (fuelSupply.getAssociated_trip() != null) {
            spinAssociatedTrip.setSelection(fuelSupply.getAssociated_trip());
            spinAssociatedTrip.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    nrSpinAssociatedTrip = (int) adapterView.getItemIdAtPosition(i);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    nrSpinAssociatedTrip = 0;
                }
            });
        }*/
        int i = 0;
        String[] S = getResources().getStringArray(R.array.travel_type_array);
        for (String item : S) {
            RadioButton newRadio = createRadioButton(item, ++i);
            rgSupplyReasonType.addView(newRadio);
        }
        rgSupplyReasonType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rbSupplyReasonType = checkedId;
            }
        });

        if (fuelSupply != null) {
            etGasStation.setText(fuelSupply.getGas_station());
            etGasStationLocation.setText(fuelSupply.getGas_station_location());
            spinCombustible.setSelection(fuelSupply.getCombustible());
            etSupplyDate.setText(fuelSupply.getSupply_date());
            if (fuelSupply.getFull_tank() == 1) {
                cbFullTank.setChecked(true);
                vlFullTank = 1;
            }
            spinCurrencyType.setSelection(fuelSupply.getCurrency_type());
//            etCurrencyValue
            etNumberLiters.setText(String.valueOf(fuelSupply.getNumber_liters()));
            etSupplyValue.setText(String.valueOf(fuelSupply.getSupply_value()));
            etFuelValue.setText(String.valueOf(fuelSupply.getFuel_value()));
            etVehicleOdometer.setText(String.valueOf(fuelSupply.getVehicle_odometer()));
            etVehicleTravelledDistance.setText(String.valueOf(fuelSupply.getVehicle_travelled_distance()));
            txStatAvgFuelConsumption.setText(String.valueOf(fuelSupply.getStat_avg_fuel_consumption()));
            txStatCostPerLitre.setText(String.valueOf(fuelSupply.getStat_cost_per_litre()));
            rgSupplyReasonType.check(fuelSupply.getSupply_reason_type());
            etSupplyReason.setText(fuelSupply.getSupply_reason());
            //spinAssociatedTrip.setSelection(Math.toIntExact(fuelSupply.getAssociated_trip()));
        }
    }

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
        Button btSaveFuelSupply = findViewById(R.id.btSaveFuelSupply);

        btSaveFuelSupply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSave = false;

                Database mdb = new Database(FuelSupplyActivity.this);
                mdb.open();

                final FuelSupply v1 = new FuelSupply();
                v1.setVehicle_id(nrVehicleId);
                v1.setGas_station(etGasStation.getText().toString());
                v1.setGas_station_location(etGasStationLocation.getText().toString());
                v1.setSupply_date(etSupplyDate.getText().toString().replace("/","").trim());
                if (!etNumberLiters.getText().toString().isEmpty()) {
                    v1.setNumber_liters(Float.parseFloat(etNumberLiters.getText().toString()));
                } else { v1.setNumber_liters(0); }
                v1.setCombustible((int) spinCombustible.getItemIdAtPosition(nrSpinCombustible));
                v1.setFull_tank(vlFullTank);
                v1.setCurrency_type((int) spinCurrencyType.getItemIdAtPosition(nrSpinCurrencyType));
                if (!etSupplyValue.getText().toString().isEmpty()) {
                    v1.setSupply_value(Float.parseFloat(etSupplyValue.getText().toString()));
                } else { v1.setSupply_value(0); }
                if (!etFuelValue.getText().toString().isEmpty()) {
                    v1.setFuel_value(Float.parseFloat(etFuelValue.getText().toString()));
                } else { v1.setFuel_value(0); }
                if (!etVehicleOdometer.getText().toString().isEmpty()) {
                    v1.setVehicle_odometer(Integer.parseInt(etVehicleOdometer.getText().toString()));
                } else { v1.setVehicle_odometer(0); }
                if (!etVehicleTravelledDistance.getText().toString().isEmpty()) {
                    v1.setVehicle_travelled_distance(Integer.parseInt(etVehicleTravelledDistance.getText().toString()));
                } else { v1.setVehicle_travelled_distance(0); }
                if (!txStatAvgFuelConsumption.getText().toString().isEmpty()) {
                    v1.setStat_avg_fuel_consumption(Float.parseFloat(txStatAvgFuelConsumption.getText().toString()));
                } else { v1.setStat_avg_fuel_consumption(0); }
                if (!txStatCostPerLitre.getText().toString().isEmpty()) {
                    v1.setStat_cost_per_litre(Float.parseFloat(txStatCostPerLitre.getText().toString()));
                } else { v1.setStat_cost_per_litre(0); }
                v1.setSupply_reason_type(findViewById(rbSupplyReasonType).getId());
                v1.setSupply_reason(etSupplyReason.getText().toString());
                v1.setAssociated_trip((long) spinAssociatedTrip.getItemIdAtPosition(nrSpinAssociatedTrip));

                if (!opInsert) {
                    try {
                        v1.setId(fuelSupply.getId());
                        isSave = Database.mFuelSupplyDao.updateFuelSupply(v1);
                    } catch (Exception e ){
                        Toast.makeText(getApplicationContext(), "Erro Alterando os Dados "+e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        isSave = Database.mFuelSupplyDao.addFuelSupply(v1);
                    } catch ( Exception e ) {
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