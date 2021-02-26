package com.jacksonasantos.travelplan.ui.vehicle;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.CurrencyQuote;
import com.jacksonasantos.travelplan.dao.Database;
import com.jacksonasantos.travelplan.dao.FuelSupply;
import com.jacksonasantos.travelplan.dao.Travel;
import com.jacksonasantos.travelplan.dao.Vehicle;
import com.jacksonasantos.travelplan.dao.VehicleStatistics;
import com.jacksonasantos.travelplan.ui.utility.DateInputMask;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class FuelSupplyActivity extends AppCompatActivity {

    private Integer nrVehicleId = 0;
    private EditText etGasStation;
    private EditText etGasStationLocation;
    private EditText etSupplyDate;
    private EditText etNumberLiters;
    private int nrSpinCombustible;
    private SwitchMaterial cbFullTank;
    private int vlFullTank = 0;
    private int nrSpinCurrencyType;
    private EditText etCurrencyValue;
    private Integer nrCurrencyQuoteId;
    private EditText etSupplyValue;
    private EditText etFuelValue;
    private EditText etVehicleOdometer;
    private EditText etVehicleTravelledDistance;
    private TextView txStatAvgFuelConsumption;
    private TextView txStatCostPerLitre;
    private int rbSupplyReasonType;
    private EditText etSupplyReason;
    private Integer nrSpinAssociatedTravelId;
    private int vLastOdometer;
    private int vLastTravelledDistance;
    private double vAccumNumberLitre = 0;
    private double vAccumSupplyValue = 0;
    private float vStatAvgFuelConsumption = (float) 0;
    private float vStatCostPerLitre = (float) 0;

    private boolean opInsert = true;
    private FuelSupply fuelSupply;

    Globals g = Globals.getInstance();

    Locale locale = new Locale(g.getLanguage(), g.getCountry());
    NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

    @SuppressLint("WrongViewCast")
    @RequiresApi(api = Build.VERSION_CODES.N)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Database mDb = new Database(getApplicationContext());
        mDb.open();

        setTitle(R.string.FuelSupply_Vehicle);
        setContentView(R.layout.activity_fuel_supply);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            nrVehicleId = extras.getInt("vehicle_id");

            if (extras.getInt( "fuel_supply_id") > 0) {
                fuelSupply = new FuelSupply();
                fuelSupply.setVehicle_id(extras.getInt("vehicle_id"));
                fuelSupply.setId(extras.getInt("fuel_supply_id"));
                fuelSupply = Database.mFuelSupplyDao.fetchFuelSupplyById(fuelSupply.getId());

                CurrencyQuote c1 = Database.mCurrencyQuoteDao.findQuoteDay(fuelSupply.currency_type, fuelSupply.supply_date);
                if ( c1.getId() != null && c1.getId() != 0 ) {
                    nrCurrencyQuoteId = c1.getId();
                } else {
                    nrCurrencyQuoteId = 0;
                }
                opInsert = false;
            }
            if (extras.getInt("travel_id")> 0) {
                nrSpinAssociatedTravelId = extras.getInt("travel_id");
            }
        }
        if (opInsert) {
            if (nrVehicleId == 0){
                nrVehicleId = g.getIdVehicle();
            }
        } else {
            nrVehicleId = fuelSupply.getVehicle_id();
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        addListenerOnButtonSave();

        TextView txVehicleName = findViewById(R.id.txVehicleName);
        ImageView imVehicleType = findViewById(R.id.imVehicleType);
        TextView txVehicleLicencePlate = findViewById(R.id.txVehicleLicencePlate);
        etGasStation = findViewById(R.id.etGasStation);
        etGasStationLocation = findViewById(R.id.etGasStationLocation);
        AutoCompleteTextView spinCombustible = findViewById(R.id.spinCombustible);
        etSupplyDate = findViewById(R.id.etSupplyDate);
        cbFullTank = findViewById(R.id.cbFullTank);
        AutoCompleteTextView spinCurrencyType = findViewById(R.id.spinCurrencyType);
        etCurrencyValue = findViewById(R.id.etCurrencyValue);
        etNumberLiters = findViewById(R.id.etNumberLiters);
        etSupplyValue = findViewById(R.id.etSupplyValue);
        etFuelValue = findViewById(R.id.etFuelValue);
        etVehicleOdometer = findViewById(R.id.etVehicleOdometer);
        etVehicleTravelledDistance = findViewById(R.id.etVehicleTravelledDistance);
        txStatAvgFuelConsumption = findViewById(R.id.txStatAvgFuelConsumption);
        txStatCostPerLitre = findViewById(R.id.txStatCostPerLitre);
        RadioGroup rgSupplyReasonType = findViewById(R.id.rgSupplyReasonType);
        etSupplyReason = findViewById(R.id.etSupplyReason);
        AutoCompleteTextView spinAssociatedTravelId = findViewById(R.id.spinAssociatedTravelId);

        final Vehicle vehicle = Database.mVehicleDao.fetchVehicleById(nrVehicleId);
        txVehicleName.setText(vehicle.getName());
        txVehicleLicencePlate.setText(vehicle.getLicense_plate());
        imVehicleType.setImageResource(vehicle.getVehicleTypeImage(vehicle.getVehicle_type()));
        etSupplyDate.addTextChangedListener(new DateInputMask(etSupplyDate));
        Utils.createSpinnerResources(R.array.fuel_array, spinCombustible, this);
        nrSpinCombustible = 0;
        spinCombustible.setOnItemClickListener(new Spinner.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                nrSpinCombustible = (int) adapterView.getItemIdAtPosition(i);
            }
        });
        cbFullTank.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v){
                vlFullTank = !cbFullTank.isChecked()?0:1;
            }
        });
        Utils.createSpinnerResources(R.array.currency_array, spinCurrencyType, this);
        nrSpinCurrencyType = 0;
        spinCurrencyType.setText(getResources().getStringArray(R.array.currency_array)[nrSpinCurrencyType],false);
        spinCurrencyType.setOnItemClickListener(new Spinner.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                nrSpinCurrencyType = (int) adapterView.getItemIdAtPosition(i);
                if (g.getIdCurrency() != nrSpinCurrencyType ) {
                    CurrencyQuote c1 = Database.mCurrencyQuoteDao.findQuoteDay(nrSpinCurrencyType, Utils.stringToDate(etSupplyDate.getText().toString()));
                    etCurrencyValue.setText(String.valueOf(c1.getCurrency_value()));
                    nrSpinCurrencyType = c1.getId();
                } else {
                    etCurrencyValue.setText(null);
                    nrSpinCurrencyType = Integer.parseInt(null);
                }
            }
        });
        View.OnFocusChangeListener listenerSupplyValue = new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String vSupplyValue = etSupplyValue.getText().toString();
                    String vNumberLiters = etNumberLiters.getText().toString();
                    etFuelValue.setText( numberFormat.format(Double.parseDouble(vSupplyValue) / Double.parseDouble(vNumberLiters)));
                }
            }
        };
        etSupplyValue.setOnFocusChangeListener(listenerSupplyValue);

        vLastOdometer = vehicle.getOdometer();
        View.OnFocusChangeListener listenerOdometer = new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String vTxtOdometerFuelSupply = etVehicleOdometer.getText().toString();
                    if (!vTxtOdometerFuelSupply.isEmpty() && vlFullTank==1) {
                        int vValOdometerFuelSupply = Integer.parseInt(vTxtOdometerFuelSupply);
                        vLastTravelledDistance = 0;
                        if ((vValOdometerFuelSupply >= vLastOdometer) && (vlFullTank==1)) {
                            vLastTravelledDistance = vValOdometerFuelSupply - vLastOdometer;
                            etVehicleTravelledDistance.setText(String.valueOf(vLastTravelledDistance));
                        }
                        if (vLastTravelledDistance > 0) {
                            // TODO - Verifiar ajustes nas estatisicas quando for alterados o Odometro e a Distancia viajada
                            vStatAvgFuelConsumption = vLastTravelledDistance / Float.parseFloat(Double.toString(Double.parseDouble(etNumberLiters.getText().toString()) + vehicle.getAccumulated_number_liters()));
                            vStatCostPerLitre = vLastTravelledDistance / Float.parseFloat(Double.toString(Double.parseDouble(etSupplyValue.getText().toString()) + vehicle.getAccumulated_supply_value()));
                            vAccumNumberLitre = 0;
                            vAccumSupplyValue = 0;
                            if (rbSupplyReasonType!=3 && vehicle.getLast_supply_reason_type()!=rbSupplyReasonType) {
                               rbSupplyReasonType = 3;
                            }
                            txStatAvgFuelConsumption.setText(numberFormat.format(vStatAvgFuelConsumption));
                            txStatCostPerLitre.setText(currencyFormatter.format(vStatCostPerLitre));
                        }
                    } else {
                        vAccumNumberLitre = Float.parseFloat(etNumberLiters.getText().toString() + vehicle.getAccumulated_number_liters());
                        vAccumSupplyValue = Float.parseFloat(etSupplyValue.getText().toString() + vehicle.getAccumulated_supply_value());
                    }
                }
            }
        };
        etVehicleOdometer.setOnFocusChangeListener(listenerOdometer);
        etVehicleTravelledDistance.setOnFocusChangeListener(listenerOdometer);

        Utils.addRadioButtonResources(R.array.supply_reason_type_array, rgSupplyReasonType, this);
        rgSupplyReasonType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rbSupplyReasonType = checkedId;
            }
        });

        final List<Travel> travels =  Database.mTravelDao.fetchArrayTravel();
        ArrayAdapter<Travel> adapterT = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, travels);
        adapterT.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinAssociatedTravelId.setAdapter(adapterT);
        if (nrSpinAssociatedTravelId != null && nrSpinAssociatedTravelId > 0) {
            Travel trip1 = Database.mTravelDao.fetchTravelById(nrSpinAssociatedTravelId);
            for (int x = 0; x <= spinAssociatedTravelId.getAdapter().getCount(); x++) {
                if (spinAssociatedTravelId.getAdapter().getItem(x).toString().equals(trip1.getDescription())) {
                    spinAssociatedTravelId.setText(spinAssociatedTravelId.getAdapter().getItem(x).toString(),false);
                    break;
                }
            }
        }

        final Travel[] t1 = {new Travel()};
        spinAssociatedTravelId.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                t1[0] = (Travel) parent.getItemAtPosition(position);
                nrSpinAssociatedTravelId = t1[0].getId();
            }

        });
        adapterT.notifyDataSetChanged();

        if (fuelSupply != null) {
            etGasStation.setText(fuelSupply.getGas_station());
            etGasStationLocation.setText(fuelSupply.getGas_station_location());
            nrSpinCombustible=fuelSupply.getCombustible();
            spinCombustible.setText(getResources().getStringArray(R.array.fuel_array)[nrSpinCombustible],false);
            etSupplyDate.setText(Utils.dateToString(fuelSupply.getSupply_date()));
            cbFullTank.setChecked(fuelSupply.getFull_tank()==1);
            vlFullTank = fuelSupply.getFull_tank();
            nrSpinCurrencyType=fuelSupply.getCurrency_type();
            spinCurrencyType.setText(getResources().getStringArray(R.array.currency_array)[nrSpinCurrencyType],false);

            if (g.getIdCurrency() != nrSpinCurrencyType ) {
                nrCurrencyQuoteId = fuelSupply.getCurrency_quote_id();
                CurrencyQuote c1 = Database.mCurrencyQuoteDao.fetchCurrencyQuoteById(nrSpinCurrencyType);
                etCurrencyValue.setText(String.valueOf(c1.getCurrency_value()));
            } else {
                nrCurrencyQuoteId = null;
                etCurrencyValue.setText(null);
            }

            etNumberLiters.setText(String.valueOf(fuelSupply.getNumber_liters()));
            etSupplyValue.setText(String.valueOf(fuelSupply.getSupply_value()));
            etFuelValue.setText(numberFormat.format(fuelSupply.getFuel_value()));
            etVehicleOdometer.setText(String.valueOf(fuelSupply.getVehicle_odometer()));
            etVehicleTravelledDistance.setText(String.valueOf(fuelSupply.getVehicle_travelled_distance()));
            txStatAvgFuelConsumption.setText(numberFormat.format(fuelSupply.getStat_avg_fuel_consumption()));
            txStatCostPerLitre.setText(currencyFormatter.format(fuelSupply.getStat_cost_per_litre()));
            rgSupplyReasonType.check(fuelSupply.getSupply_reason_type());
            etSupplyReason.setText(fuelSupply.getSupply_reason());
            nrSpinAssociatedTravelId=fuelSupply.getAssociated_travel_id()==0?null:fuelSupply.getAssociated_travel_id();
            if (nrSpinAssociatedTravelId != null && nrSpinAssociatedTravelId > 0) {
                Travel trip1 = Database.mTravelDao.fetchTravelById(nrSpinAssociatedTravelId);
                for (int x = 0; x <= spinAssociatedTravelId.getAdapter().getCount(); x++) {
                    if (spinAssociatedTravelId.getAdapter().getItem(x).toString().equals(trip1.getDescription())) {
                        spinAssociatedTravelId.setText(spinAssociatedTravelId.getAdapter().getItem(x).toString(),false);
                        break;
                    }
                }
            }
        }
    }

    public void addListenerOnButtonSave() {
        Button btSaveFuelSupply = findViewById(R.id.btSaveFuelSupply);

        btSaveFuelSupply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSave = false;

                if (!validateData()) {
                    Toast.makeText(getApplicationContext(), R.string.Error_Data_Validation, Toast.LENGTH_LONG).show();
                } else {
                    final FuelSupply f1 = new FuelSupply();
                    Vehicle v1 = Database.mVehicleDao.fetchVehicleById(nrVehicleId);

                    if (g.getIdCurrency() != nrSpinCurrencyType ) {
                        final CurrencyQuote c1 = new CurrencyQuote();
                        c1.setId(nrCurrencyQuoteId);
                        c1.setCurrency_type(nrSpinCurrencyType);
                        c1.setCurrency_value(Float.parseFloat(etCurrencyValue.getText().toString()));
                        c1.setQuote_date( Utils.stringToDate(etSupplyDate.getText().toString()));
                        if (nrCurrencyQuoteId > 0) {
                            isSave = Database.mCurrencyQuoteDao.updateCurrencyQuote(c1);
                        } else {
                            isSave = Database.mCurrencyQuoteDao.addCurrencyQuote(c1);
                        }
                    }

                    f1.setVehicle_id(nrVehicleId);
                    f1.setGas_station(etGasStation.getText().toString());
                    f1.setGas_station_location(etGasStationLocation.getText().toString());
                    f1.setSupply_date( Utils.stringToDate(etSupplyDate.getText().toString()));
                    f1.setNumber_liters(Double.parseDouble(etNumberLiters.getText().toString()));
                    f1.setCombustible(nrSpinCombustible);
                    f1.setFull_tank(vlFullTank);
                    f1.setCurrency_type(nrSpinCurrencyType);
                    f1.setCurrency_quote_id(nrCurrencyQuoteId); // TODO - ver id correto da moeda
                    f1.setSupply_value(Double.parseDouble(etSupplyValue.getText().toString()));
                    f1.setFuel_value(Double.parseDouble(etFuelValue.getText().toString().replace(",",".")));
                    f1.setSupply_reason_type(findViewById(rbSupplyReasonType).getId());
                    f1.setSupply_reason(etSupplyReason.getText().toString());
                    f1.setAssociated_travel_id(nrSpinAssociatedTravelId);
                    if (!etVehicleOdometer.getText().toString().isEmpty()) {
                        f1.setVehicle_odometer(Integer.parseInt(etVehicleOdometer.getText().toString()));
                        f1.setVehicle_travelled_distance(Integer.parseInt(etVehicleTravelledDistance.getText().toString()));
                        f1.setStat_avg_fuel_consumption(vStatAvgFuelConsumption);
                        f1.setStat_cost_per_litre(vStatCostPerLitre);
                    }
                    try {
                        if (!opInsert) {
                            f1.setId(fuelSupply.getId());
                            isSave = Database.mFuelSupplyDao.updateFuelSupply(f1);
                        } else {
                            isSave = Database.mFuelSupplyDao.addFuelSupply(f1);
                            if (!etVehicleOdometer.getText().toString().isEmpty() && vlFullTank==1) {
                                v1.setDt_odometer(Utils.stringToDate(etSupplyDate.getText().toString()));
                                v1.setOdometer(Integer.parseInt(etVehicleOdometer.getText().toString()));
                                if (vStatAvgFuelConsumption>0){
                                    List<VehicleStatistics> vStat = Database.mVehicleStatisticsDao.findTotalVehicleStatistics(nrVehicleId);
                                    v1.setAvg_consumption(vStat.get(0).getAvg_consumption());
                                    v1.setAvg_cost_litre(vStat.get(0).getAvg_cost_litre());
                                    v1.setDt_last_fueling(Utils.stringToDate(etSupplyDate.getText().toString()));
                                    v1.setLast_supply_reason_type(findViewById(rbSupplyReasonType).getId());
                                    v1.setAccumulated_number_liters(vAccumNumberLitre);
                                    v1.setAccumulated_supply_value(vAccumSupplyValue);
                                }
                            } else {
                                v1.setDt_last_fueling(Utils.stringToDate(etSupplyDate.getText().toString()));
                                v1.setLast_supply_reason_type(findViewById(rbSupplyReasonType).getId());
                                v1.setAccumulated_number_liters(vAccumNumberLitre);
                                v1.setAccumulated_supply_value(vAccumSupplyValue);
                            }
                            isSave = Database.mVehicleDao.updateVehicle(v1);
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(),  R.string.Error_Saving_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    setResult(isSave ? 1 : 0);
                    if (isSave) {
                        finish();
                    }
                }
            }
        });
    }
    private boolean validateData() {
        boolean isValid = false;

        try {
            if (!etGasStation.getText().toString().trim().isEmpty()
                && !etGasStationLocation.getText().toString().trim().isEmpty()
                && !String.valueOf(nrSpinCombustible).trim().isEmpty()
                && !etSupplyDate.getText().toString().trim().isEmpty()
                //&& !cbFullTank.getText().toString().trim().isEmpty()
                && !String.valueOf(nrSpinCurrencyType).trim().isEmpty()
                //&& !etCurrencyValue.getText().toString().trim().isEmpty()
                && !etNumberLiters.getText().toString().trim().isEmpty()
                && !etSupplyValue.getText().toString().trim().isEmpty()
                && !etFuelValue.getText().toString().trim().isEmpty()
                //&& !etVehicleOdometer.getText().toString().trim().isEmpty()
                //&& !etVehicleTravelledDistance.getText().toString().trim().isEmpty()
                //&& !txStatAvgFuelConsumption.getText().toString().trim().isEmpty()
                //&& !txStatCostPerLitre.getText().toString().trim().isEmpty()
                && !String.valueOf(findViewById(rbSupplyReasonType).getId()).trim().isEmpty()
                //&& !etSupplyReason.getText().toString().trim().isEmpty()
                //&& !String.valueOf(nrSpinAssociatedTrip).trim().isEmpty()
               )
            {
                isValid = true;
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.Data_Validator_Error +" - " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return isValid;
    }
}