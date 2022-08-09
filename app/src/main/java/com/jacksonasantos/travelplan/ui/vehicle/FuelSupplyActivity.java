package com.jacksonasantos.travelplan.ui.vehicle;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.jacksonasantos.travelplan.MainActivity;
import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.CurrencyQuote;
import com.jacksonasantos.travelplan.dao.FuelSupply;
import com.jacksonasantos.travelplan.dao.Travel;
import com.jacksonasantos.travelplan.dao.Vehicle;
import com.jacksonasantos.travelplan.dao.VehicleStatistics;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.travel.PlacesAdapter;
import com.jacksonasantos.travelplan.ui.utility.Abbreviations;
import com.jacksonasantos.travelplan.ui.utility.DateInputMask;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class FuelSupplyActivity extends AppCompatActivity implements PlacesAdapter.OnItemClicked {

    private TextView txVehicleName;
    private ImageView imVehicleType;
    private TextView txVehicleLicencePlate;
    private Integer nrVehicleId = 0;
    private ImageButton btLocation;
    public RecyclerView listPlaces;
    private EditText etGasStation;
    private EditText etGasStationLocation;
    private Spinner spCombustible;
    private int nrSpCombustible;
    private EditText etSupplyDate;
    private EditText etNumberLiters;
    private SwitchMaterial cbFullTank;
    private int vlFullTank = 0;
    private Spinner spCurrencyType;
    private int nrSpCurrencyType;
    private Integer nrCurrencyQuoteId;
    private EditText etSupplyValue;
    private EditText etFuelValue;
    private EditText etVehicleOdometer;
    private TextView txVehicleTravelledDistance;
    private TextView txStatAvgFuelConsumption;
    private TextView txStatCostPerLitre;
    private TextView txUnitDistance;
    private TextView txUnitConsumption;
    private RadioGroup rgSupplyReasonType;
    private int rbSupplyReasonType;
    private EditText etSupplyReason;
    private Spinner spAssociatedTravelId;
    private Integer nrSpAssociatedTravelId;
    private int vLastOdometer;
    private int vLastTravelledDistance;
    private double vAccumulatedNumberLitre = 0;
    private double vAccumulatedSupplyValue = 0;
    private double vAccumNumberLitre = 0;
    private double vAccumSupplyValue = 0;
    private float vStatAvgFuelConsumption = (float) 0;
    private float vStatCostPerLitre = (float) 0;

    private boolean opInsert = true;
    private FuelSupply fuelSupply;

    private PlacesAdapter adapterPlaces;
    final Globals g = Globals.getInstance();

    public Geocoder mGeocoder;

    private static final int REQUEST_PERMISSION = 1;        // TODO - testar quando não dá permissão de localização

    final Locale locale = new Locale(g.getLanguage(), g.getCountry());
    final NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
    final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

    // Use fields to define the data types to return.
    final List<Place.Field> placeFields = Arrays.asList(
            Place.Field.NAME,
            Place.Field.LAT_LNG,
            Place.Field.ADDRESS);

    private static final String GOOGLE_API_KEY =  MainActivity.getAppResources().getString(R.string.google_maps_key);

    @SuppressLint("WrongViewCast")
    @RequiresApi(api = Build.VERSION_CODES.N)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGeocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(0);

        Places.initialize(getApplicationContext(), GOOGLE_API_KEY);
        PlacesClient placesClient = Places.createClient(this);

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_WIFI_STATE};
            requestPermissions(permissions, REQUEST_PERMISSION);
        }

        Database mDb = new Database(getApplicationContext());
        mDb.open();

        setTitle(R.string.FuelSupply_Vehicle);
        setContentView(R.layout.dialog_fuel_supply);

        Bundle extras = getIntent().getExtras();
        fuelSupply = new FuelSupply();
        if (extras != null) {
            nrVehicleId = extras.getInt("vehicle_id");
            fuelSupply.setVehicle_id(nrVehicleId);

            if (extras.getInt( "fuel_supply_id") > 0) {
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
                nrSpAssociatedTravelId = extras.getInt("travel_id");
            }
        }
        if (opInsert) {
            if (nrVehicleId == 0) {
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
        txVehicleName = findViewById(R.id.txVehicleName);
        imVehicleType = findViewById(R.id.imVehicleType);
        txVehicleLicencePlate = findViewById(R.id.txVehicleLicencePlate);
        btLocation  = findViewById(R.id.btLocation);
        listPlaces = findViewById(R.id.listPlaces);
        etGasStation = findViewById(R.id.etGasStation);
        etGasStationLocation = findViewById(R.id.etGasStationLocation);
        spCombustible = findViewById(R.id.spCombustible);
        etSupplyDate = findViewById(R.id.etSupplyDate);
        cbFullTank = findViewById(R.id.cbFullTank);
        spCurrencyType = findViewById(R.id.spCurrencyType);
        etNumberLiters = findViewById(R.id.etNumberLiters);
        etSupplyValue = findViewById(R.id.etSupplyValue);
        etFuelValue = findViewById(R.id.etFuelValue);
        etVehicleOdometer = findViewById(R.id.etVehicleOdometer);
        txVehicleTravelledDistance = findViewById(R.id.txVehicleTravelledDistance);
        txStatAvgFuelConsumption = findViewById(R.id.txStatAvgFuelConsumption);
        txStatCostPerLitre = findViewById(R.id.txStatCostPerLitre);
        txUnitDistance = findViewById(R.id.txUnitDistance);
        txUnitConsumption = findViewById(R.id.txUnitConsumption);
        rgSupplyReasonType = findViewById(R.id.rgSupplyReasonType);
        etSupplyReason = findViewById(R.id.etSupplyReason);
        spAssociatedTravelId = findViewById(R.id.spAssociatedTravelId);

        final Vehicle vehicle = Database.mVehicleDao.fetchVehicleById(nrVehicleId);
        txVehicleName.setText(vehicle.getName());
        txVehicleLicencePlate.setText(vehicle.getLicense_plate());
        imVehicleType.setImageResource(vehicle.getVehicleTypeImage(vehicle.getVehicle_type()));

        btLocation.setOnClickListener(view -> {
            FindCurrentPlaceRequest request = FindCurrentPlaceRequest.builder(placeFields).build();
            Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
            ArrayList<PlaceLikelihood> arrayPlaces = new ArrayList<>();

            placeResponse.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FindCurrentPlaceResponse response = task.getResult();

                    for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                        arrayPlaces.add(placeLikelihood);
                        Log.i("FuelSupply", String.format("Place '%s' in '%s' - '%s' has likelihood: %f",
                                placeLikelihood.getPlace().getName(), placeLikelihood.getPlace().getLatLng(),
                                placeLikelihood.getPlace().getAddress(), placeLikelihood.getLikelihood()));
                    }
                    adapterPlaces = new PlacesAdapter(arrayPlaces);
                    listPlaces.setVisibility(View.VISIBLE);
                    listPlaces.setAdapter(adapterPlaces);
                    listPlaces.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                    adapterPlaces.setOnClick(FuelSupplyActivity.this);
                } else {
                    Exception exception = task.getException();
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        Log.e("FuelSupply", "Place not found: " + apiException.getStatusCode());
                    }
                }
            });

        });
        etSupplyDate.addTextChangedListener(new DateInputMask(etSupplyDate));
        cbFullTank.setOnClickListener(v -> vlFullTank = !cbFullTank.isChecked()?0:1);

        Utils.createSpinnerResources(R.array.fuel_array, spCombustible, this);
        nrSpCombustible = fuelSupply.getCombustible();
        spCombustible.setSelection(nrSpCombustible);
        spCombustible.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long idx) {
                nrSpCombustible = position;  }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                nrSpCombustible =0;
            }
        });

        Utils.createSpinnerResources(R.array.currency_array, spCurrencyType, this);
        nrSpCurrencyType = fuelSupply.getCurrency_type();
        spCurrencyType.setSelection(nrSpCurrencyType);
        spCurrencyType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long idx) {
                nrSpCurrencyType = position;
                // TODO - ver cotação correta da moeda para a data do abastecimento
                nrCurrencyQuoteId = null;
//               CurrencyQuote c1 = Database.mCurrencyQuoteDao.findQuoteDay(nrSpCurrencyType, Utils.stringToDate(etSupplyDate.getText().toString()));
//               etCurrencyValue.setText(String.valueOf(c1.getCurrency_value()));
               //nrSpCurrencyType = c1.getId();
                //spCurrencyType.setSelection(nrSpCurrencyType);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                nrCurrencyQuoteId = null;
                nrSpCurrencyType =0;
            }
        });

        /*View.OnFocusChangeListener listenerNumberLiters = (v, hasFocus) -> {
            if (!hasFocus) {
                // TODO - Implementar o cálculo com o valor da moeda correta
                String vFuelValue = etFuelValue.getText().toString();
                String vNumberLiters = etNumberLiters.getText().toString();
                double vSupplyValue = 0;
                if (!vFuelValue.equals("") && !vNumberLiters.equals("")) {
                    try {
                        vSupplyValue = Utils.convertStrCurrencyToDouble(vFuelValue) * Utils.convertStrCurrencyToDouble(vNumberLiters);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                etSupplyValue.setText( numberFormat.format(Double.isNaN(vSupplyValue)?0:vSupplyValue));
            }
        };
        etNumberLiters.setOnFocusChangeListener(listenerNumberLiters);

        View.OnFocusChangeListener listenerSupplyValue = (v, hasFocus) -> {
            if (!hasFocus) {
                String vSupplyValue = etSupplyValue.getText().toString();
                String vNumberLiters = etNumberLiters.getText().toString();
                double vFuelValue = 0;
                if (!vSupplyValue.equals("") && !vNumberLiters.equals("") ) {
                    try {
                        vFuelValue = Utils.convertStrCurrencyToDouble(vSupplyValue ) / Utils.convertStrCurrencyToDouble(vNumberLiters);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                etFuelValue.setText( numberFormat.format(Double.isNaN(vFuelValue)?0:vFuelValue) );
            }
        };
        etSupplyValue.setOnFocusChangeListener(listenerSupplyValue);
        */
        vLastOdometer = vehicle.getOdometer();
        View.OnFocusChangeListener listenerOdometer = (v, hasFocus) -> {
            if (!hasFocus) {
                String vTxtOdometerFuelSupply = etVehicleOdometer.getText().toString();
                if (!vTxtOdometerFuelSupply.isEmpty() && vlFullTank==1) {
                    int vValOdometerFuelSupply = Integer.parseInt(vTxtOdometerFuelSupply);
                    vLastTravelledDistance = 0;
                    if ((vValOdometerFuelSupply >= vLastOdometer) && (vlFullTank==1)) {
                        vLastTravelledDistance = vValOdometerFuelSupply - vLastOdometer;
                        txVehicleTravelledDistance.setText(String.valueOf(vLastTravelledDistance));
                    }
                }
            }
        };
        etVehicleOdometer.setOnFocusChangeListener(listenerOdometer);

        Utils.addRadioButtonResources(R.array.supply_reason_type_array, rgSupplyReasonType, this);
        rgSupplyReasonType.setOnCheckedChangeListener((group, checkedId) -> rbSupplyReasonType = checkedId);

        final List<Travel> travels =  Database.mTravelDao.fetchArrayTravel();
        travels.add(0, new Travel());

        ArrayAdapter<Travel> adapterT = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, travels);
        adapterT.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spAssociatedTravelId.setAdapter(adapterT);
        if (nrSpAssociatedTravelId != null && nrSpAssociatedTravelId > 0) {
            Travel trip1 = Database.mTravelDao.fetchTravelById(nrSpAssociatedTravelId);
            for (int x = 1; x <= spAssociatedTravelId.getAdapter().getCount(); x++) {
                if (spAssociatedTravelId.getAdapter().getItem(x).toString().equals(trip1.getDescription())) {
                    spAssociatedTravelId.setSelection(x);
                    nrSpAssociatedTravelId = trip1.getId();
                    break;
                }
            }
        }

        final Travel[] t1 = {new Travel()};
        spAssociatedTravelId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long idx) {
                t1[0] = (Travel) parent.getItemAtPosition(position);
                nrSpAssociatedTravelId = t1[0].getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                nrSpAssociatedTravelId = null;
            }
        });
        adapterT.notifyDataSetChanged();

        if (fuelSupply != null) {
            etGasStation.setText(fuelSupply.getGas_station());
            etGasStationLocation.setText(fuelSupply.getGas_station_location());
            nrSpCombustible=fuelSupply.getCombustible();
            spCombustible.setSelection(nrSpCombustible);
            etSupplyDate.setText(Utils.dateToString(fuelSupply.getSupply_date()));
            cbFullTank.setChecked(fuelSupply.getFull_tank()==1);
            vlFullTank = fuelSupply.getFull_tank();
            nrSpCurrencyType=fuelSupply.getCurrency_type();
            spCurrencyType.setSelection(nrSpCurrencyType);

            if (g.getIdCurrency() != nrSpCurrencyType ) {
                nrCurrencyQuoteId = fuelSupply.getCurrency_quote_id();
                CurrencyQuote c1 = Database.mCurrencyQuoteDao.fetchCurrencyQuoteById(nrSpCurrencyType);
                nrCurrencyQuoteId = c1.getId();
            } else {
                nrCurrencyQuoteId = null;
            }

            etNumberLiters.setText(String.valueOf(fuelSupply.getNumber_liters()==0?"":fuelSupply.getNumber_liters()));
            etSupplyValue.setText(String.valueOf(fuelSupply.getSupply_value()==0?"":fuelSupply.getSupply_value()));
            etFuelValue.setText(String.valueOf(fuelSupply.getFuel_value()==0?"":fuelSupply.getFuel_value()));
            etVehicleOdometer.setText(String.valueOf(fuelSupply.getVehicle_odometer()==0?"":fuelSupply.getVehicle_odometer()));
            txVehicleTravelledDistance.setText(String.valueOf(fuelSupply.getVehicle_travelled_distance()));
            txStatAvgFuelConsumption.setText(numberFormat.format(fuelSupply.getStat_avg_fuel_consumption()));
            txStatCostPerLitre.setText(currencyFormatter.format(fuelSupply.getStat_cost_per_litre()));
            txUnitDistance.setText(g.getMeasureCost());
            txUnitConsumption.setText(g.getMeasureConsumption());
            rgSupplyReasonType.check(fuelSupply.getSupply_reason_type());
            etSupplyReason.setText(fuelSupply.getSupply_reason());
            nrSpAssociatedTravelId=fuelSupply.getAssociated_travel_id();
            if (nrSpAssociatedTravelId != null && nrSpAssociatedTravelId > 0) {
                Travel trip1 = Database.mTravelDao.fetchTravelById(nrSpAssociatedTravelId);
                for (int x = 1; x <= spAssociatedTravelId.getAdapter().getCount(); x++) {
                    if (spAssociatedTravelId.getAdapter().getItem(x).toString().equals(trip1.getDescription())) {
                        spAssociatedTravelId.setSelection(x);
                        break;
                    }
                }
            }
        }
    }

    public void addListenerOnButtonSave() {
        Button btSaveFuelSupply = findViewById(R.id.btSaveFuelSupply);

        btSaveFuelSupply.setOnClickListener(v -> {
            boolean isSave = false;

            if (!validateData()) {
                Toast.makeText(getApplicationContext(), R.string.Error_Data_Validation, Toast.LENGTH_LONG).show();
            } else {
                final FuelSupply f1 = new FuelSupply();
                Vehicle v1 = Database.mVehicleDao.fetchVehicleById(nrVehicleId);

                vLastOdometer = v1.getOdometer();
                String vTxtOdometerFuelSupply = etVehicleOdometer.getText().toString();
                if (vTxtOdometerFuelSupply.equals("0")||vTxtOdometerFuelSupply.equals("")){
                    vTxtOdometerFuelSupply = null;
                }
                if (!(vTxtOdometerFuelSupply ==null) && vlFullTank==1) {
                    int vValOdometerFuelSupply = Integer.parseInt(vTxtOdometerFuelSupply);
                    vLastTravelledDistance = 0;
                    if ((vValOdometerFuelSupply >= vLastOdometer) && (vlFullTank==1)) {
                        vLastTravelledDistance = vValOdometerFuelSupply - vLastOdometer;
                        txVehicleTravelledDistance.setText(String.valueOf(vLastTravelledDistance));
                    }
                    if (vLastTravelledDistance > 0) {
                        // TODO - VERIFIAR AJUSTES NAS ESTATISICAS QUANDO FOR ALTERADOS O ODOMETRO E A DISTANCIA VIAJADA
                        vStatAvgFuelConsumption = vLastTravelledDistance / Float.parseFloat(Double.toString(Double.parseDouble(etNumberLiters.getText().toString()) + v1.getAccumulated_number_liters()));
                        vStatCostPerLitre = vLastTravelledDistance / Float.parseFloat(Double.toString(Double.parseDouble(etSupplyValue.getText().toString()) + v1.getAccumulated_supply_value()));
                        vAccumulatedNumberLitre = v1.getAccumulated_number_liters();
                        vAccumulatedSupplyValue = v1.getAccumulated_supply_value();
                        vAccumNumberLitre = 0;
                        vAccumSupplyValue = 0;
                        if (rbSupplyReasonType!=3 &&
                            v1.getLast_supply_reason_type()!=rbSupplyReasonType &&
                            v1.getAccumulated_number_liters()>0) {
                            rbSupplyReasonType = 3;
                        }
                        txStatAvgFuelConsumption.setText(numberFormat.format(vStatAvgFuelConsumption));
                        txStatCostPerLitre.setText(currencyFormatter.format(vStatCostPerLitre));
                    }
                } else {
                    vAccumulatedNumberLitre = 0;
                    vAccumulatedSupplyValue = 0;
                    vAccumNumberLitre = Double.parseDouble(etNumberLiters.getText().toString()) + v1.getAccumulated_number_liters();
                    vAccumSupplyValue = Double.parseDouble(etSupplyValue.getText().toString()) + v1.getAccumulated_supply_value();
                }

                if (g.getIdCurrency() != nrSpCurrencyType ) {
                    final CurrencyQuote c1 = new CurrencyQuote();
                    c1.setId(nrCurrencyQuoteId);
                    c1.setCurrency_type(nrSpCurrencyType);
                    // TODO - Definir um dialog para entrada do valor da cotação do dia
//                    c1.setCurrency_value(Float.parseFloat(etCurrencyValue.getText().toString()));
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
                f1.setAccumulated_Number_liters(vAccumulatedNumberLitre);
                f1.setAccumulated_supply_value(vAccumulatedSupplyValue);
                f1.setCombustible(nrSpCombustible);
                f1.setFull_tank(vlFullTank);
                f1.setCurrency_type(nrSpCurrencyType);
                f1.setCurrency_quote_id(nrCurrencyQuoteId); // TODO - ver id correto da cotação da moeda
                f1.setSupply_value(Double.parseDouble(etSupplyValue.getText().toString()));
                f1.setFuel_value(Double.parseDouble(etFuelValue.getText().toString().replace(",",".")));
                f1.setSupply_reason_type(findViewById(rbSupplyReasonType).getId());
                f1.setSupply_reason(etSupplyReason.getText().toString());
                f1.setAssociated_travel_id(nrSpAssociatedTravelId);
                if (!etVehicleOdometer.getText().toString().isEmpty()){
                    f1.setVehicle_odometer(Integer.parseInt(etVehicleOdometer.getText().toString()));
                    f1.setVehicle_travelled_distance(Integer.parseInt(txVehicleTravelledDistance.getText().toString()));
                    f1.setStat_avg_fuel_consumption(vStatAvgFuelConsumption);
                    f1.setStat_cost_per_litre(vStatCostPerLitre);
                }
                try {
                    if (!opInsert) {
                        f1.setId(fuelSupply.getId());
                        /*
                        if (Integer.parseInt(etVehicleOdometer.getText().toString()) != f1.getVehicle_odometer()) {
                            if (etVehicleOdometer.getText().toString().isEmpty()){
                                final FuelSupply f2 = new FuelSupply();
                                // TODO - popular f2 com proximo abastecimento a receber o acumulado
                                f2.setAccumulated_Number_liters(f1.getNumber_liters());
                                f2.setVehicle_travelled_distance(f2.getVehicle_travelled_distance()+f1.getVehicle_travelled_distance());
                                vStatAvgFuelConsumption = (f2.getVehicle_travelled_distance()+f1.getVehicle_travelled_distance()) / Float.parseFloat(Double.toString(f2.getNumber_liters() + f1.getNumber_liters()));
                                vStatCostPerLitre = (f2.getVehicle_travelled_distance()+f1.getVehicle_travelled_distance()) / Float.parseFloat(Double.toString(f2.getSupply_value() + f1.getSupply_value()));
                                f2.setStat_avg_fuel_consumption(vStatAvgFuelConsumption);
                                f2.setStat_cost_per_litre(vStatCostPerLitre);
                                isSave = Database.mFuelSupplyDao.updateFuelSupply(f2);
                            }
                        }
                        */
                        isSave = Database.mFuelSupplyDao.updateFuelSupply(f1);
                    } else {
                        isSave = Database.mFuelSupplyDao.addFuelSupply(f1);
                        if (!etVehicleOdometer.getText().toString().isEmpty() && vlFullTank==1) {
                            v1.setDt_odometer(Utils.stringToDate(etSupplyDate.getText().toString()));
                            v1.setOdometer(Integer.parseInt(etVehicleOdometer.getText().toString()));
                            if (vStatAvgFuelConsumption>0){
                                List<VehicleStatistics> vStat = Database.mVehicleStatisticsDao.findTotalFuelingVehicleStatistics(nrVehicleId);
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
        });
    }
    private boolean validateData() {
        boolean isValid = false;

        try {
            if (!etGasStation.getText().toString().trim().isEmpty()
                && !etGasStationLocation.getText().toString().trim().isEmpty()
                && !String.valueOf(nrSpCombustible).trim().isEmpty()
                && !etSupplyDate.getText().toString().trim().isEmpty()
                //&& !cbFullTank.getText().toString().trim().isEmpty()
                && !String.valueOf(nrSpCurrencyType).trim().isEmpty()
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

    private String getCityNameByCoordinates(double lat, double lon) throws IOException {
        List<Address> addresses = mGeocoder.getFromLocation(lat, lon, 1);
        if (addresses != null && addresses.size() > 0) {
            return addresses.get(0).getSubAdminArea() + "/"+ Abbreviations.getAbbreviationFromState(addresses.get(0).getAdminArea())+ " - "+ addresses.get(0).getCountryCode();
        }
        return null;
    }

    @Override
    public void onItemClick(PlaceLikelihood position) {
        etGasStation.setText(position.getPlace().getName());
        try {
            etGasStationLocation.setText(getCityNameByCoordinates(Objects.requireNonNull(position.getPlace().getLatLng()).latitude, Objects.requireNonNull(position.getPlace().getLatLng()).longitude));
        } catch (IOException e) {
            e.printStackTrace();
        }
        listPlaces.setVisibility(View.GONE);
    }
}