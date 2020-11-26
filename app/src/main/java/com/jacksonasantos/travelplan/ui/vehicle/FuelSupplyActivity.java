package com.jacksonasantos.travelplan.ui.vehicle;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.CurrencyQuote;
import com.jacksonasantos.travelplan.dao.Database;
import com.jacksonasantos.travelplan.dao.FuelSupply;
import com.jacksonasantos.travelplan.dao.Vehicle;
import com.jacksonasantos.travelplan.dao.VehicleStatistics;
import com.jacksonasantos.travelplan.ui.utility.DateInputMask;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.text.NumberFormat;
import java.util.Locale;

public class FuelSupplyActivity extends AppCompatActivity {

    private Long nrVehicleId=0L;
    private EditText etGasStation;
    private EditText etGasStationLocation;
    private EditText etSupplyDate;
    private EditText etNumberLiters;
    private int nrSpinCombustible;
    private SwitchMaterial cbFullTank;
    private int vlFullTank = 0;
    private int nrSpinCurrencyType;
    private EditText etCurrencyValue;
    private double nrCurrencyValue = 0;
    private long nrIdCurrencyQuote = 0L;
    private EditText etSupplyValue;
    private EditText etFuelValue;
    private EditText etVehicleOdometer;
    private EditText etVehicleTravelledDistance;
    private TextView txStatAvgFuelConsumption;
    private TextView txStatCostPerLitre;
    private int rbSupplyReasonType;
    private EditText etSupplyReason;
    private int nrSpinAssociatedTrip;
    private int vLastOdometer;
    private int vLastOdometerNew;
    private float vStatAvgFuelConsumption = (float) 0;
    private float vStatCostPerLitre = (float) 0;

    private boolean opInsert = true;
    private FuelSupply fuelSupply;

    Globals g = Globals.getInstance();

    Locale locale = new Locale(g.getLanguage(), g.getCountry());
    NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
   // String currencySymbol = currencyFormatter.getCurrency().getSymbol();

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
            nrVehicleId = extras.getLong("vehicle_id");

            if (extras.getLong( "fuel_supply_id") > 0) {
                fuelSupply = new FuelSupply();
                fuelSupply.setVehicle_id(extras.getLong("vehicle_id"));
                fuelSupply.setId(extras.getLong("fuel_supply_id"));
                fuelSupply = Database.mFuelSupplyDao.fetchFuelSupplyById(fuelSupply.getId());

                CurrencyQuote c1 = Database.mCurrencyQuoteDao.findQuoteDay(fuelSupply.currency_type, fuelSupply.supply_date);
                if ( c1.getId() != 0 ) {
                    nrIdCurrencyQuote = c1.getId();
                    nrCurrencyValue = c1.getCurrency_value();
                } else {
                    nrIdCurrencyQuote = 0; // TODO - colocar a moeda default
                    nrCurrencyValue = 0;
                }
                opInsert = false;
            }
        }
        if (opInsert) {
            if (nrVehicleId == 0L){
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
        AutoCompleteTextView spinAssociatedTrip = findViewById(R.id.spinAssociatedTrip);

        Vehicle vehicle = Database.mVehicleDao.fetchVehicleById(nrVehicleId);
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
        spinCurrencyType.setOnItemClickListener(new Spinner.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                nrSpinCurrencyType = (int) adapterView.getItemIdAtPosition(i);
                // TODO - Eliminar pesquisa para REAIS ou moeda Default

                // currencyFormatter.getCurrency().getSymbol()
                CurrencyQuote c1 = Database.mCurrencyQuoteDao.findQuoteDay(nrSpinCurrencyType, Utils.stringToDate(etSupplyDate.getText().toString()));
                etCurrencyValue.setText(String.valueOf(c1.getCurrency_value()));
            }
        });
        View.OnFocusChangeListener listenerSupplyValue = new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String vSupplyValue = etSupplyValue.getText().toString();
                    String vNumberLiters = etNumberLiters.getText().toString();
                    etFuelValue.setText( String.valueOf(Double.parseDouble(vSupplyValue) / Double.parseDouble(vNumberLiters)));
                }
            }
        };
        etSupplyValue.setOnFocusChangeListener(listenerSupplyValue);

        vLastOdometer = vehicle.getOdometer();
        View.OnFocusChangeListener listenerOdometer = new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String txVlrOdometerFuelSupply = etVehicleOdometer.getText().toString();
                    if (!txVlrOdometerFuelSupply.isEmpty()) {
                        int valor = Integer.parseInt(txVlrOdometerFuelSupply);
                        vLastOdometerNew = 0;
                        if ((valor > vLastOdometer) && (vlFullTank==1)) {
                            vLastOdometerNew = valor - vLastOdometer;
                            etVehicleTravelledDistance.setText(String.valueOf(vLastOdometerNew));
                        }
                        if (vLastOdometerNew > 0) {
                            vStatAvgFuelConsumption = vLastOdometerNew / Float.parseFloat(etNumberLiters.getText().toString());
                            vStatCostPerLitre = vLastOdometerNew / Float.parseFloat(etSupplyValue.getText().toString());
                            // TODO - Calcular as medias com os abastecimentos sem tanque cheio
                            // TODO - Verifiar ajustes nas estatisicas quando for alterados o Odometro e a Distancia viajada
                            //NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
                            txStatAvgFuelConsumption.setText(numberFormat.format(vStatAvgFuelConsumption));
                            //NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
                            txStatCostPerLitre.setText(currencyFormatter.format(vStatCostPerLitre));
                        }
                    }
                }
            }
        };
        etVehicleOdometer.setOnFocusChangeListener(listenerOdometer);
        etVehicleTravelledDistance.setOnFocusChangeListener(listenerOdometer);

        int i = 0;
        String[] S = getResources().getStringArray(R.array.supply_reason_type_array);
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
        //TODO - Assossiar Viagem ao Spin
        /*if (fuelSupply.getAssociated_trip() != null) {
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
            etCurrencyValue.setText(String.valueOf(nrCurrencyValue));
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

                    f1.setVehicle_id(nrVehicleId);
                    f1.setGas_station(etGasStation.getText().toString());
                    f1.setGas_station_location(etGasStationLocation.getText().toString());
                    f1.setSupply_date( Utils.stringToDate(etSupplyDate.getText().toString()));
                    f1.setNumber_liters(Double.parseDouble(etNumberLiters.getText().toString()));
                    f1.setCombustible(nrSpinCombustible);
                    f1.setFull_tank(vlFullTank);
                    f1.setCurrency_type(nrSpinCurrencyType); // TODO - Ajustar para guardar o ID do currency_quote
                    f1.setSupply_value(Double.parseDouble(etSupplyValue.getText().toString()));
                    f1.setFuel_value(Double.parseDouble(etFuelValue.getText().toString()));
                    f1.setVehicle_odometer(Integer.parseInt(etVehicleOdometer.getText().toString()));
                    f1.setVehicle_travelled_distance(Integer.parseInt(etVehicleTravelledDistance.getText().toString()));
                    f1.setStat_avg_fuel_consumption(vStatAvgFuelConsumption);
                    f1.setStat_cost_per_litre(vStatCostPerLitre);
                    f1.setSupply_reason_type(findViewById(rbSupplyReasonType).getId());
                    f1.setSupply_reason(etSupplyReason.getText().toString());
                    //f1.setAssociated_trip((long) spinAssociatedTrip.getItemIdAtPosition(nrSpinAssociatedTrip));

                    final CurrencyQuote c1 = new CurrencyQuote();
                    c1.setCurrency_type(nrSpinCurrencyType);
                    c1.setCurrency_value(Float.parseFloat(etCurrencyValue.getText().toString()));
                    c1.setQuote_date( Utils.stringToDate(etSupplyDate.getText().toString()));

                    final VehicleStatistics ve1 = new VehicleStatistics();
                    ve1.setVehicle_id(nrVehicleId);
                    ve1.setSupply_reason_type(findViewById(rbSupplyReasonType).getId());
                    ve1.setStatistic_date(Utils.stringToDate(etSupplyDate.getText().toString()));
                    ve1.setAvg_consumption(vStatAvgFuelConsumption);

                    Vehicle v1;
                    v1 = Database.mVehicleDao.fetchVehicleById(nrVehicleId);
                    v1.setDt_odometer( Utils.stringToDate(etSupplyDate.getText().toString()));
                    v1.setOdometer(Integer.parseInt(etVehicleOdometer.getText().toString()));

                    Database mdb = new Database(FuelSupplyActivity.this);
                    mdb.open();

                    if (!opInsert) {
                        try {
                            f1.setId(fuelSupply.getId());
                            isSave = Database.mFuelSupplyDao.updateFuelSupply(f1);
                            if (nrIdCurrencyQuote > 0) {
                                c1.setId(nrIdCurrencyQuote);
                                isSave = Database.mCurrencyQuoteDao.updateCurrencyQuote(c1);
                            } else {
                                isSave = Database.mCurrencyQuoteDao.addCurrencyQuote(c1);
                            }
                            //if (vLastOdometerNew > 0) {
                            //    isSave = Database.mVehicleDao.updateVehicle(v1);
                            //    isSave = Database.mVehicleStatisticsDao.addVehicleStatistics(ve1);
                            //}
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), R.string.Error_Changing_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        try {
                            isSave = Database.mFuelSupplyDao.addFuelSupply(f1);
                            isSave = Database.mCurrencyQuoteDao.addCurrencyQuote(c1);
                            isSave = Database.mVehicleDao.updateVehicle(v1);

                            // TODO - verificar abastecimentos antigos para não atualizar estatísticas
                            if (Database.mVehicleStatisticsDao.changeVehicleStatistics(ve1)) {
                                isSave = true;
                            } else {
                                isSave = Database.mVehicleStatisticsDao.addVehicleStatistics(ve1);
                            }
                        } catch (Exception e) {
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
        boolean isValid = false;

        try {
            if (!etGasStation.getText().toString().trim().isEmpty()
                && !etGasStationLocation.getText().toString().trim().isEmpty()
                && !String.valueOf(nrSpinCombustible).trim().isEmpty()
                && !etSupplyDate.getText().toString().trim().isEmpty()
                //&& !cbFullTank.getText().toString().trim().isEmpty()
                && !String.valueOf(nrSpinCurrencyType).trim().isEmpty()
                && !etCurrencyValue.getText().toString().trim().isEmpty()
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
}