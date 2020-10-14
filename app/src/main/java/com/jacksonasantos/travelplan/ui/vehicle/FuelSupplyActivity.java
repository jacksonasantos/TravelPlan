package com.jacksonasantos.travelplan.ui.vehicle;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import androidx.preference.PreferenceManager;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.CurrencyQuote;
import com.jacksonasantos.travelplan.dao.Database;
import com.jacksonasantos.travelplan.dao.FuelSupply;
import com.jacksonasantos.travelplan.dao.Vehicle;
import com.jacksonasantos.travelplan.ui.utility.DateInputMask;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class FuelSupplyActivity extends AppCompatActivity {

    private Long nrVehicleId=0L;
    private TextView txVehicleName;
    private ImageView imVehicleType;
    private TextView txVehicleLicencePlate;
    private EditText etGasStation;
    private EditText etGasStationLocation;
    private EditText etSupplyDate;
    private EditText etNumberLiters;
    private AutoCompleteTextView spinCombustible;
    private int nrSpinCombustible;
    private SwitchMaterial cbFullTank;
    private int vlFullTank = 0;
    private AutoCompleteTextView spinCurrencyType;
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
    private RadioGroup rgSupplyReasonType;
    private int rbSupplyReasonType;
    private EditText etSupplyReason;
    private AutoCompleteTextView spinAssociatedTrip;
    private int nrSpinAssociatedTrip;
    private int vLastOdometer;
    float vStatAvgFuelConsumption = (float) 0;
    float vStatCostPerLitre = (float) 0;

    private FuelSupply fuelSupply;
    private boolean opInsert = true;
    private Menu menu;

    Locale locale = new Locale("pt", "BR");

    @SuppressLint("WrongViewCast")
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
                fuelSupply = Database.mFuelSupplyDao.fetchFuelSupplyById(fuelSupply.getId());

                CurrencyQuote c1 = Database.mCurrencyQuoteDao.findQuoteDay(fuelSupply.currency_type, fuelSupply.supply_date);
                if ( c1.getId() != null ) {
                    nrIdCurrencyQuote = c1.getId();
                    nrCurrencyValue = c1.getCurrency_value();
                } else {
                    nrIdCurrencyQuote = 0;
                    nrCurrencyValue = 0;
                }
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
        }
        vehicle = Database.mVehicleDao.fetchVehicleById(nrVehicleId);
        txVehicleName.setText(vehicle.getName());
        txVehicleLicencePlate.setText(vehicle.getLicense_plate());
        imVehicleType.setImageResource(vehicle.getTypeImage(vehicle.getType()));
        vLastOdometer = vehicle.getOdometer();

        etSupplyDate.addTextChangedListener(new DateInputMask(etSupplyDate));

        View.OnFocusChangeListener listenerOdometer = new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String txVlrOdometerFuelSupply = etVehicleOdometer.getText().toString();
                    if (!txVlrOdometerFuelSupply.isEmpty()) {
                        int valor = Integer.parseInt(txVlrOdometerFuelSupply);
                        int vLastOdometerNew = 0;
                        if ((valor > vLastOdometer) && (vlFullTank==1)) {
                            vLastOdometerNew = valor - vLastOdometer;
                        }
                        etVehicleTravelledDistance.setText(String.valueOf(vLastOdometerNew));

                         if (vLastOdometerNew > 0) {
                            vStatAvgFuelConsumption = vLastOdometerNew / Float.parseFloat(etNumberLiters.getText().toString());
                            vStatCostPerLitre = vLastOdometerNew / Float.parseFloat(etSupplyValue.getText().toString());
                        }
                        // TODO - Calcular as medias com os abastecimentos sem tanque cheio
                        NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
                        txStatAvgFuelConsumption.setText(numberFormat.format(vStatAvgFuelConsumption));

                        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
                        txStatCostPerLitre.setText(currencyFormatter.format(vStatCostPerLitre));
                    }
                }
            }
        };
        etVehicleOdometer.setOnFocusChangeListener(listenerOdometer);
        etVehicleTravelledDistance.setOnFocusChangeListener(listenerOdometer);

        addSpinnerResources(R.array.fuel_array, spinCombustible);
        nrSpinCombustible = 0;
        spinCombustible.setOnItemClickListener(new Spinner.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                nrSpinCombustible = (int) adapterView.getItemIdAtPosition(i);
            }
        });
        cbFullTank.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v){
                    if (!cbFullTank.isChecked()) vlFullTank = 0; // TODO - Testar melhoria tirando o True e colocando a condição do IF
                    else { vlFullTank = 1; }
                }
        });
        addSpinnerResources(R.array.currency_array, spinCurrencyType);
        nrSpinCurrencyType = 0;
        spinCurrencyType.setOnItemClickListener(new Spinner.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                nrSpinCurrencyType = (int) adapterView.getItemIdAtPosition(i);
                //TODO - Eliminar pesquisa para REAIS ou moeda Dafault
                CurrencyQuote c1 = Database.mCurrencyQuoteDao.findQuoteDay(nrSpinCurrencyType, Utils.stringToDate(etSupplyDate.getText().toString()));
                etCurrencyValue.setText(String.valueOf(c1.getCurrency_value()));
            }
        });

        //TODO - Assossiar Viagem ao Spin
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
            nrSpinCombustible=fuelSupply.getCombustible();
            spinCombustible.setText(getResources().getStringArray(R.array.fuel_array)[nrSpinCombustible],false);
            etSupplyDate.setText(Utils.dateToString(fuelSupply.getSupply_date()));
            if (fuelSupply.getFull_tank() == 1) {
                cbFullTank.setChecked(true); // TODO - Testar melhoria tirando o True e colocando a condição do IF
            }
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
                    Toast.makeText(getApplicationContext(), "Erro na Validação dos Dados... ", Toast.LENGTH_LONG).show();
                } else {
                    final FuelSupply f1 = new FuelSupply();

                    f1.setVehicle_id(nrVehicleId);
                    f1.setGas_station(etGasStation.getText().toString());
                    f1.setGas_station_location(etGasStationLocation.getText().toString());
                    f1.setSupply_date( Utils.stringToDate(etSupplyDate.getText().toString()));
                    f1.setNumber_liters(Double.parseDouble(etNumberLiters.getText().toString()));
                    f1.setCombustible(nrSpinCombustible);
                    f1.setFull_tank(vlFullTank);
                    f1.setCurrency_type(nrSpinCurrencyType);
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
                            isSave = Database.mVehicleDao.updateVehicle(v1);
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Erro Alterando os Dados " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        try {
                            isSave = Database.mFuelSupplyDao.addFuelSupply(f1);
                            isSave = Database.mCurrencyQuoteDao.addCurrencyQuote(c1);
                            isSave = Database.mVehicleDao.updateVehicle(v1);
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Erro Incluindo os Dados " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    mdb.close();
                    setResult(isSave ? 1 : 0);
                    if (isSave) {
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Erro Salvando os Dados ", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    private boolean validateData() {
        boolean isValid = false;

        try {
            String s1 = etGasStation.getText().toString();
            String s2 = etGasStationLocation.getText().toString();
            String s3 = String.valueOf(nrSpinCombustible);
            String s4 = etSupplyDate.getText().toString();
            String s5 = cbFullTank.getText().toString();
            String s6 = String.valueOf(nrSpinCurrencyType);
            String s7 = etCurrencyValue.getText().toString();
            String s8 = etNumberLiters.getText().toString();
            String s9 =  etSupplyValue.getText().toString();
            String s10 = etFuelValue.getText().toString();
            String s11 =  etVehicleOdometer.getText().toString();
            String s12 =  etVehicleTravelledDistance.getText().toString();
            String s13 =  txStatAvgFuelConsumption.getText().toString();
            String s14 =  txStatCostPerLitre.getText().toString();
            String s15 =  String.valueOf(findViewById(rbSupplyReasonType).getId());
            String s16 =  etSupplyReason.getText().toString();
            String s17 = String.valueOf(nrSpinAssociatedTrip);

            if (s1 != null && !s1.trim().isEmpty() &&
                s2 != null && !s2.trim().isEmpty() &&
                s3 != null && !s3.trim().isEmpty() &&
                s4 != null && !s4.trim().isEmpty() &&
                //s5 != null && !s5.trim().isEmpty() &&
                s6 != null && !s6.trim().isEmpty() &&
                s7 != null && !s7.trim().isEmpty() &&
                s8 != null && !s8.trim().isEmpty() &&
                s9 != null && !s9.trim().isEmpty() &&
                s10 != null && !s10.trim().isEmpty() &&
                //s11 != null && !s11.trim().isEmpty() &&
                //s12 != null && !s12.trim().isEmpty() &&
                //s13 != null && !s13.trim().isEmpty() &&
                //s14 != null && !s14.trim().isEmpty() &&
                s15 != null && !s15.trim().isEmpty() //&&
                //s16 != null && !s16.trim().isEmpty() &&
                //s17 != null && !s17.trim().isEmpty()
               )
            {
                isValid = true;
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Erro no Validador dos Dados " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return isValid;
    }

    private void addSpinnerResources(int resource_array, AutoCompleteTextView spin) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.select_dialog_item,
                getResources().getStringArray(resource_array));
        spin.setAdapter(adapter);
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