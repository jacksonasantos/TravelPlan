package com.jacksonasantos.travelplan.ui.vehicle;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import com.jacksonasantos.travelplan.dao.Account;
import com.jacksonasantos.travelplan.dao.CurrencyQuote;
import com.jacksonasantos.travelplan.dao.FuelSupply;
import com.jacksonasantos.travelplan.dao.Transport;
import com.jacksonasantos.travelplan.dao.Travel;
import com.jacksonasantos.travelplan.dao.Vehicle;
import com.jacksonasantos.travelplan.dao.VehicleHasTravel;
import com.jacksonasantos.travelplan.dao.VehicleStatistics;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.general.CurrencyQuoteActivity;
import com.jacksonasantos.travelplan.ui.travel.PlacesAdapter;
import com.jacksonasantos.travelplan.ui.utility.Abbreviations;
import com.jacksonasantos.travelplan.ui.utility.DateInputMask;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class FuelSupplyActivity extends AppCompatActivity implements PlacesAdapter.OnItemClicked {

    private CardView cvVehicle;
    private ImageView imVehicleType;
    private TextView txVehicleName;
    private TextView txVehicleLicencePlate;
    private Integer nrVehicleId = null;

    private CardView cvTransport;
    private ImageView imTransportType;
    private TextView txTransportDescription;
    private TextView txTransportIdentifier;
    private Integer nrTransportId = null;

    private ImageButton btLocation;
    public RecyclerView listPlaces;
    private EditText etGasStation;
    private EditText etGasStationLocation;

    private EditText etSupplyDate;
    private SwitchMaterial cbFullTank;
    private int vlFullTank = 0;
    private Spinner spCombustible;
    private int nrSpCombustible;
    private Spinner spAccount;
    private Integer nrSpAccountId;
    private EditText etNumberLiters;
    private Spinner spCurrencyType;
    private int nrSpCurrencyType;
    private Integer nrCurrencyQuoteId;
    private TextView txCurrencyValue;
    private EditText etSupplyValue;
    private EditText etFuelValue;
    private EditText etVehicleOdometer;
    private TextView tvVehicleOdometer;
    private RadioGroup rgSupplyReasonType;
    private int rbSupplyReasonType;
    private EditText etSupplyReason;
    private Spinner spAssociatedTravelId;
    private Integer nrSpAssociatedTravelId;

    private CardView cvStatistics;
    private TextView txVehicleTravelledDistance;
    private TextView txUnitDistance;
    private TextView txStatAvgFuelConsumption;
    private TextView txUnitConsumption;
    private TextView txStatCostPerLitre;

    private CardView cvAccumulated;
    private TextView txAccumulatedNumberLiters;
    private TextView txAccumulatedSupplyValue;

    private int vLastOdometer;
    private int vLastTravelledDistance;
    private double vAccumulatedNumberLiter = 0;
    private double vAccumulatedSupplyValue = 0;
    private double vAccumulatedLiterNumber = 0;
    private double vAccumulatedValueSupply = 0;
    private float vStatAvgFuelConsumption = (float) 0;
    private float vStatCostPerLitre = (float) 0;

    private boolean opInsert = true;
    private FuelSupply fuelSupply;

    private PlacesAdapter adapterPlaces;
    final Globals g = Globals.getInstance();

    public Geocoder mGeocoder;

    final Locale locale = new Locale(g.getLanguage(), g.getCountry());
    final NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
    final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

    // Use fields to define the data types to return.
    final List<Place.Field> placeFields = Arrays.asList(
            Place.Field.NAME,
            Place.Field.LAT_LNG,
            Place.Field.ADDRESS);

    private static final String GOOGLE_API_KEY =  MainActivity.getAppResources().getString(R.string.google_maps_key);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGeocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(0);

        Places.initialize(getApplicationContext(), GOOGLE_API_KEY);
        PlacesClient placesClient = Places.createClient(this);

        Database mDb = new Database(getApplicationContext());
        mDb.open();

        setTitle(R.string.FuelSupply_Vehicle);
        setContentView(R.layout.activity_fuel_supply);

        Bundle extras = getIntent().getExtras();
        fuelSupply = new FuelSupply();
        if (extras != null) {
            if (extras.getInt( "vehicle_id") > 0) {
                nrVehicleId = extras.getInt("vehicle_id");
                fuelSupply.setVehicle_id(nrVehicleId);
            }

            if (extras.getInt( "transport_id") > 0) {
                nrTransportId = extras.getInt("transport_id");
                fuelSupply.setTransport_id(nrTransportId);
            }

            if (extras.getInt( "fuel_supply_id") > 0) {
                fuelSupply.setId(extras.getInt("fuel_supply_id"));
                fuelSupply = Database.mFuelSupplyDao.fetchFuelSupplyById(fuelSupply.getId());
                nrSpAssociatedTravelId = fuelSupply.getAssociated_travel_id();
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
        if (!opInsert) {
            nrVehicleId = fuelSupply.getVehicle_id();
            nrTransportId = fuelSupply.getTransport_id();
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        addListenerOnButtonSave();

        cvTransport = findViewById(R.id.cvTransport);
        imTransportType = findViewById(R.id.imTransportType);
        txTransportDescription = findViewById(R.id.txTransportDescription);
        txTransportIdentifier = findViewById(R.id.txTransportIdentifier);

        cvVehicle = findViewById(R.id.cvVehicle);
        txVehicleName = findViewById(R.id.txVehicleName);
        imVehicleType = findViewById(R.id.imVehicleType);
        txVehicleLicencePlate = findViewById(R.id.txVehicleLicencePlate);

        btLocation  = findViewById(R.id.btLocation);
        listPlaces = findViewById(R.id.listPlaces);
        etGasStation = findViewById(R.id.etGasStation);
        etGasStationLocation = findViewById(R.id.etGasStationLocation);
        spCombustible = findViewById(R.id.spCombustible);
        spAccount = findViewById(R.id.spAccount);
        etSupplyDate = findViewById(R.id.etSupplyDate);
        cbFullTank = findViewById(R.id.cbFullTank);
        spCurrencyType = findViewById(R.id.spCurrencyType);
        txCurrencyValue = findViewById(R.id.txCurrencyValue);
        etNumberLiters = findViewById(R.id.etNumberLiters);
        etSupplyValue = findViewById(R.id.etSupplyValue);
        etFuelValue = findViewById(R.id.etFuelValue);
        etVehicleOdometer = findViewById(R.id.etVehicleOdometer);
        tvVehicleOdometer = findViewById(R.id.tvVehicleOdometer);
        txVehicleTravelledDistance = findViewById(R.id.txVehicleTravelledDistance);
        rgSupplyReasonType = findViewById(R.id.rgSupplyReasonType);
        etSupplyReason = findViewById(R.id.etSupplyReason);
        spAssociatedTravelId = findViewById(R.id.spAssociatedTravelId);

        cvStatistics = findViewById(R.id.cvStatistics);
        txStatAvgFuelConsumption = findViewById(R.id.txStatAvgFuelConsumption);
        txStatCostPerLitre = findViewById(R.id.txStatCostPerLitre);
        txUnitDistance = findViewById(R.id.txUnitDistance);
        txUnitConsumption = findViewById(R.id.txUnitConsumption);

        cvAccumulated = findViewById(R.id.cvAccumulated);
        txAccumulatedNumberLiters = findViewById(R.id.txAccumulatedNumberLiters);
        txAccumulatedSupplyValue = findViewById(R.id.txAccumulatedSupplyValue);

        Vehicle vehicle = new Vehicle();
        Transport transport;
        VehicleHasTravel vehicleHasTravel = new VehicleHasTravel();
        if (nrVehicleId != null) {
            vehicle = Database.mVehicleDao.fetchVehicleById(nrVehicleId);
            txVehicleName.setText(vehicle.getName());
            txVehicleLicencePlate.setText(vehicle.getLicense_plate());
            imVehicleType.setImageResource(vehicle.getVehicleTypeImage(vehicle.getVehicle_type()));
            cvVehicle.setVisibility(View.VISIBLE);
            cvTransport.setVisibility(View.GONE);
            cvStatistics.setVisibility(View.VISIBLE);
            cvAccumulated.setVisibility(View.VISIBLE);
        } else {
            if (nrTransportId != null) {
                transport = Database.mTransportDao.fetchTransportById(nrTransportId);
                vehicleHasTravel = Database.mVehicleHasTravelDao.findVehicleHasTravelByTravelTransport(nrSpAssociatedTravelId,nrTransportId);
                if (vehicleHasTravel.getLast_odometer()==0){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.Transport_Odometer_value_is_invalid), Toast.LENGTH_LONG).show();
                    Log.e("FuelSupply",  getResources().getString(R.string.Transport_Odometer_value_is_invalid));
                    finish();
                }
                txTransportDescription.setText(transport.getDescription());
                txTransportIdentifier.setText(transport.getIdentifier());
                imTransportType.setImageResource(Transport.getTransportTypeImage(transport.getTransport_type()));
                cvTransport.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.A_vehicle_needs_to_be_selected), Toast.LENGTH_LONG).show();
                finish();
            }
            cvVehicle.setVisibility(View.GONE);
            cvStatistics.setVisibility(View.GONE);
            cvAccumulated.setVisibility(View.GONE);
        }

        btLocation.setOnClickListener(view -> {
            if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION);
            }

            FindCurrentPlaceRequest request = FindCurrentPlaceRequest.builder(placeFields).build();
            Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
            ArrayList<PlaceLikelihood> arrayPlaces = new ArrayList<>();

            placeResponse.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FindCurrentPlaceResponse response = task.getResult();

                    arrayPlaces.addAll(response.getPlaceLikelihoods());
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
                nrCurrencyQuoteId = null;
                CurrencyQuote c1 = Database.mCurrencyQuoteDao.findQuoteDay(nrSpCurrencyType, Utils.stringToDate(etSupplyDate.getText().toString()));
                if (c1.getId()==null && nrSpCurrencyType!=0) {
                    Intent intent = new Intent (getBaseContext(), CurrencyQuoteActivity.class);
                    intent.putExtra("currency_type", nrSpCurrencyType);
                    intent.putExtra("quote_date", etSupplyDate.getText().toString());
                    myActivityResultLauncher.launch(intent);
                }
                txCurrencyValue.setText(numberFormat.format(c1.getCurrency_value()));
                nrCurrencyQuoteId = c1.getId();
                spCurrencyType.setSelection(nrSpCurrencyType);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                nrCurrencyQuoteId = null;
                nrSpCurrencyType =0;
            }

            final ActivityResultLauncher<Intent> myActivityResultLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == 123) {
                                Intent data = result.getData();
                                if (data != null) {
                                    txCurrencyValue.setText(data.getStringExtra("resulted_value"));
                                    etNumberLiters.requestFocus();
                                }
                            }
                        }
                    }
            );
        });

        View.OnFocusChangeListener listenerNumberLiters = (v, hasFocus) -> {
            if (!hasFocus) {
                double vFuelValue = Utils.convertStrCurrencyToDouble(etFuelValue.getText().toString());
                double vNumberLiters = Utils.convertStrCurrencyToDouble(etNumberLiters.getText().toString());
                double vCurrencyValue = Utils.convertStrCurrencyToDouble(txCurrencyValue.getText().toString());
                double vSupplyValue = 0;
                if (vCurrencyValue == 0) vCurrencyValue = 1;
                if (vFuelValue>0 && vNumberLiters>0) {
                    vSupplyValue = (vFuelValue * vCurrencyValue * vNumberLiters);
                }
                etSupplyValue.setText( numberFormat.format(Double.isNaN(vSupplyValue)?0:vSupplyValue).replace(",","."));
            }
        };
        etNumberLiters.setOnFocusChangeListener(listenerNumberLiters);

        if (nrVehicleId!=null) {
            vLastOdometer = vehicle.getOdometer();
            View.OnFocusChangeListener listenerOdometer = (v, hasFocus) -> {
                if (!hasFocus) {
                    String vTxtOdometerFuelSupply = etVehicleOdometer.getText().toString();
                    if (!vTxtOdometerFuelSupply.isEmpty() && vlFullTank == 1) {
                        int vValOdometerFuelSupply = Integer.parseInt(vTxtOdometerFuelSupply);
                        vLastTravelledDistance = 0;
                        if ((vValOdometerFuelSupply >= vLastOdometer) && (vlFullTank == 1)) {
                            vLastTravelledDistance = vValOdometerFuelSupply - vLastOdometer;
                            txVehicleTravelledDistance.setText(String.valueOf(vLastTravelledDistance));
                        }
                    }
                }
            };
            tvVehicleOdometer.setVisibility(View.VISIBLE);
            etVehicleOdometer.setVisibility(View.VISIBLE);
            etVehicleOdometer.setOnFocusChangeListener(listenerOdometer);
        } else {
            if (nrTransportId!=null) {
                vLastOdometer = vehicleHasTravel.getLast_odometer();
                View.OnFocusChangeListener listenerOdometer = (v, hasFocus) -> {
                    if (!hasFocus) {
                        String vTxtOdometerFuelSupply = etVehicleOdometer.getText().toString();
                        if (!vTxtOdometerFuelSupply.isEmpty() && vlFullTank == 1) {
                            int vValOdometerFuelSupply = Integer.parseInt(vTxtOdometerFuelSupply);
                            vLastTravelledDistance = 0;
                            if ((vValOdometerFuelSupply >= vLastOdometer) && (vlFullTank == 1)) {
                                vLastTravelledDistance = vValOdometerFuelSupply - vLastOdometer;
                                txVehicleTravelledDistance.setText(String.valueOf(vLastTravelledDistance));
                            }
                        }
                    }
                };
                tvVehicleOdometer.setVisibility(View.VISIBLE);
                etVehicleOdometer.setVisibility(View.VISIBLE);
                etVehicleOdometer.setOnFocusChangeListener(listenerOdometer);
            } else {
                tvVehicleOdometer.setVisibility(View.GONE);
                etVehicleOdometer.setVisibility(View.GONE);
            }
        }

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

        final List<Account> accounts =  Database.mAccountDao.fetchAllAccount();
        accounts.add(0, new Account());
        ArrayAdapter<Account> adapterA = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, accounts);
        adapterA.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spAccount.setAdapter(adapterA);
        if (nrSpAccountId != null && nrSpAccountId > 0) {
            Account account1 = Database.mAccountDao.fetchAccountById(nrSpAccountId);
            for (int x = 1; x <= spAccount.getAdapter().getCount(); x++) {
                if (spAccount.getAdapter().getItem(x).toString().equals(account1.getDescription())) {
                    spAccount.setSelection(x);
                    nrSpAccountId = account1.getId();
                    break;
                }
            }
        }
        final Account[] a1 = {new Account()};
        spAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long idx) {
                a1[0] = (Account) parent.getItemAtPosition(position);
                nrSpAccountId = a1[0].getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                nrSpAccountId = null;
            }
        });
        adapterA.notifyDataSetChanged();

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
                CurrencyQuote c1 = Database.mCurrencyQuoteDao.findQuoteDay(nrSpCurrencyType,fuelSupply.getSupply_date());
                nrCurrencyQuoteId = c1.getId();
            } else { nrCurrencyQuoteId = null;}
            etNumberLiters.setText(String.valueOf(fuelSupply.getNumber_liters()==0?"":fuelSupply.getNumber_liters()));
            etSupplyValue.setText(String.valueOf(fuelSupply.getSupply_value()==0?"":fuelSupply.getSupply_value()));
            etFuelValue.setText(String.valueOf(fuelSupply.getFuel_value()==0?"":fuelSupply.getFuel_value()));
            if ( nrVehicleId != null ) {
                etVehicleOdometer.setText(String.valueOf(fuelSupply.getVehicle_odometer() == 0 ? "" : fuelSupply.getVehicle_odometer()));
                txVehicleTravelledDistance.setText(String.valueOf(fuelSupply.getVehicle_travelled_distance()));
            } else {
                if (nrTransportId != null ){
                    etVehicleOdometer.setText(String.valueOf(fuelSupply.getVehicle_odometer() == 0 ? "" : fuelSupply.getVehicle_odometer()));
                    txVehicleTravelledDistance.setText(String.valueOf(fuelSupply.getVehicle_travelled_distance()));
                }
            }
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
            nrSpAccountId=fuelSupply.getAccount_id();
            if (nrSpAccountId != null && nrSpAccountId > 0) {
                Account account1 = Database.mAccountDao.fetchAccountById(nrSpAccountId);
                for (int x = 1; x <= spAccount.getAdapter().getCount(); x++) {
                    if (spAccount.getAdapter().getItem(x).toString().equals(account1.getDescription())) {
                        spAccount.setSelection(x);
                        break;
                    }
                }
            }
            txAccumulatedNumberLiters.setText(String.valueOf(fuelSupply.getAccumulated_number_liters()==0?"":fuelSupply.getAccumulated_number_liters()));
            txAccumulatedSupplyValue.setText(currencyFormatter.format(fuelSupply.getAccumulated_supply_value()));
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
                Vehicle v1 = new Vehicle();
                VehicleHasTravel vt1 = new VehicleHasTravel();

                if (nrVehicleId != null || nrTransportId != null ) {
                    if (nrVehicleId != null) {
                        v1 = Database.mVehicleDao.fetchVehicleById(nrVehicleId);
                        vLastOdometer = v1.getOdometer();
                    } else {
                        vt1 = Database.mVehicleHasTravelDao.findVehicleHasTravelByTravelTransport(nrSpAssociatedTravelId, nrTransportId);
                        vLastOdometer = vt1.getLast_odometer();
                    }
                    String vTxtOdometerFuelSupply = etVehicleOdometer.getText().toString();
                    if (vTxtOdometerFuelSupply.equals("0") || vTxtOdometerFuelSupply.equals("")) {
                        vTxtOdometerFuelSupply = null;
                    }
                    if (!(vTxtOdometerFuelSupply == null) && vlFullTank == 1) {
                        int vValOdometerFuelSupply = Integer.parseInt(vTxtOdometerFuelSupply);
                        vLastTravelledDistance = 0;
                        if ((vValOdometerFuelSupply >= vLastOdometer) && (vlFullTank == 1)) {
                            vLastTravelledDistance = vValOdometerFuelSupply - vLastOdometer;
                            txVehicleTravelledDistance.setText(String.valueOf(vLastTravelledDistance));
                        }
                        if (vLastTravelledDistance > 0) {
                            // TODO - recalcular a distancia e o novo consumo quando alterado o odometro pegando o abastecimento imediatamente anterior
                            if (nrVehicleId!=null) {
                                vStatAvgFuelConsumption = vLastTravelledDistance / Float.parseFloat(Double.toString(Double.parseDouble(etNumberLiters.getText().toString()) + v1.getAccumulated_number_liters()));
                                vStatCostPerLitre = vLastTravelledDistance / Float.parseFloat(Double.toString(Double.parseDouble(etSupplyValue.getText().toString()) + v1.getAccumulated_supply_value()));
                                vAccumulatedNumberLiter = v1.getAccumulated_number_liters();
                                vAccumulatedSupplyValue = v1.getAccumulated_supply_value();
                                vAccumulatedLiterNumber = 0;
                                vAccumulatedValueSupply = 0;
                                if (rbSupplyReasonType != 3 &&
                                    v1.getLast_supply_reason_type() != rbSupplyReasonType &&
                                    v1.getAccumulated_number_liters() > 0) {
                                    rbSupplyReasonType = 3;
                                }
                            } else {
                                vStatAvgFuelConsumption = vLastTravelledDistance / Float.parseFloat(Double.toString(Double.parseDouble(etNumberLiters.getText().toString()) ));
                                vStatCostPerLitre = vLastTravelledDistance / Float.parseFloat(Double.toString(Double.parseDouble(etSupplyValue.getText().toString()) ));
                                vAccumulatedNumberLiter = 0;
                                vAccumulatedSupplyValue = 0;
                                vAccumulatedLiterNumber = 0;
                                vAccumulatedValueSupply = 0;
                                rbSupplyReasonType = 2;
                            }
                            txStatAvgFuelConsumption.setText(numberFormat.format(vStatAvgFuelConsumption));
                            txStatCostPerLitre.setText(currencyFormatter.format(vStatCostPerLitre));
                            txAccumulatedNumberLiters.setText(numberFormat.format(vAccumulatedNumberLiter));
                            txAccumulatedSupplyValue.setText(currencyFormatter.format(vAccumulatedSupplyValue));
                        }
                    } else {
                        vAccumulatedNumberLiter = 0;
                        vAccumulatedSupplyValue = 0;
                        vAccumulatedLiterNumber = Double.parseDouble(etNumberLiters.getText().toString()) + v1.getAccumulated_number_liters();
                        vAccumulatedValueSupply = Double.parseDouble(etSupplyValue.getText().toString().replace(",", ".")) + v1.getAccumulated_supply_value();
                    }
                }

                f1.setVehicle_id(nrVehicleId);
                f1.setTransport_id(nrTransportId);
                f1.setGas_station(etGasStation.getText().toString());
                f1.setGas_station_location(etGasStationLocation.getText().toString());
                f1.setSupply_date( Utils.stringToDate(etSupplyDate.getText().toString()));
                f1.setNumber_liters(Double.parseDouble(etNumberLiters.getText().toString()));
                f1.setAccumulated_Number_liters(vAccumulatedNumberLiter);
                f1.setAccumulated_supply_value(vAccumulatedSupplyValue);
                f1.setCombustible(nrSpCombustible);
                f1.setFull_tank(vlFullTank);
                f1.setCurrency_type(nrSpCurrencyType);
                f1.setCurrency_quote_id(nrCurrencyQuoteId);
                f1.setSupply_value(Double.parseDouble(etSupplyValue.getText().toString().replace(",",".")));
                f1.setFuel_value(Double.parseDouble(etFuelValue.getText().toString().replace(",",".")));
                f1.setSupply_reason_type(findViewById(rbSupplyReasonType).getId());
                f1.setSupply_reason(etSupplyReason.getText().toString());
                f1.setAssociated_travel_id(nrSpAssociatedTravelId);
                f1.setAccount_id(nrSpAccountId);
                if (nrVehicleId != null || nrTransportId!=null ) {
                    if (!etVehicleOdometer.getText().toString().isEmpty()) {
                        f1.setVehicle_odometer(Integer.parseInt(etVehicleOdometer.getText().toString()));
                        f1.setVehicle_travelled_distance(Integer.parseInt(txVehicleTravelledDistance.getText().toString()));
                        f1.setStat_avg_fuel_consumption(vStatAvgFuelConsumption);
                        f1.setStat_cost_per_litre(vStatCostPerLitre);
                    } else {
                    /*
                        final FuelSupply f2 = new FuelSupply();
                        // TODO - popular f2 with next supply to receive the accumulated
                        f2.setAccumulated_Number_liters(f1.getNumber_liters());
                        f2.setVehicle_travelled_distance(f2.getVehicle_travelled_distance()+f1.getVehicle_travelled_distance());
                        vStatAvgFuelConsumption = (f2.getVehicle_travelled_distance()+f1.getVehicle_travelled_distance()) / Float.parseFloat(Double.toString(f2.getNumber_liters() + f1.getNumber_liters()));
                        vStatCostPerLitre = (f2.getVehicle_travelled_distance()+f1.getVehicle_travelled_distance()) / Float.parseFloat(Double.toString(f2.getSupply_value() + f1.getSupply_value()));
                        f2.setStat_avg_fuel_consumption(vStatAvgFuelConsumption);
                        f2.setStat_cost_per_litre(vStatCostPerLitre);
                         */
                    }
                }
                try {
                    if (!opInsert) {
                        f1.setId(fuelSupply.getId());
                        /*
                        if (Integer.parseInt(etVehicleOdometer.getText().toString()) != f1.getVehicle_odometer()) {
                            if (etVehicleOdometer.getText().toString().isEmpty()){
                                isSave = Database.mFuelSupplyDao.updateFuelSupply(f2);
                            }
                        }
                        */
                        isSave = Database.mFuelSupplyDao.updateFuelSupply(f1);
                    } else {
                        isSave = Database.mFuelSupplyDao.addFuelSupply(f1);
                        if (nrVehicleId!=null) {
                            if (!etVehicleOdometer.getText().toString().isEmpty() && vlFullTank == 1) {
                                v1.setDt_odometer(Utils.stringToDate(etSupplyDate.getText().toString()));
                                v1.setOdometer(Integer.parseInt(etVehicleOdometer.getText().toString()));
                                if (vStatAvgFuelConsumption > 0) {
                                    List<VehicleStatistics> vStat = Database.mVehicleStatisticsDao.findTotalFuelingVehicleStatistics(nrVehicleId);
                                    v1.setAvg_consumption(vStat.get(0).getAvg_consumption());
                                    v1.setAvg_cost_litre(vStat.get(0).getAvg_cost_litre());
                                    v1.setDt_last_fueling(Utils.stringToDate(etSupplyDate.getText().toString()));
                                    v1.setLast_supply_reason_type(findViewById(rbSupplyReasonType).getId());
                                    v1.setAccumulated_number_liters(vAccumulatedLiterNumber);
                                    v1.setAccumulated_supply_value(vAccumulatedValueSupply);
                                }
                            } else {
                                v1.setDt_last_fueling(Utils.stringToDate(etSupplyDate.getText().toString()));
                                v1.setLast_supply_reason_type(findViewById(rbSupplyReasonType).getId());
                                v1.setAccumulated_number_liters(vAccumulatedLiterNumber);
                                v1.setAccumulated_supply_value(vAccumulatedValueSupply);
                            }
                            isSave = Database.mVehicleDao.updateVehicle(v1);
                        } else {
                            if (nrTransportId!= null) {
                                List<VehicleStatistics> vStat = Database.mVehicleStatisticsDao.findTotalFuelingTransportStatistics(nrTransportId);
                                vt1.setLast_odometer(Integer.parseInt(etVehicleOdometer.getText().toString()));
                                vt1.setAvg_consumption(vStat.get(0).getAvg_consumption());
                                vt1.setAvg_cost_litre(vStat.get(0).getAvg_cost_litre());
                                isSave = Database.mVehicleHasTravelDao.updateVehicleHasTravel(vt1);
                            }
                        }
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

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), isGranted -> {
                String tag = new ActivityResultContracts.RequestPermission().toString();
                if (isGranted) {
                    Log.e(tag, "onActivityResult: PERMISSION GRANTED");
                } else {
                    Log.e(tag, "onActivityResult: PERMISSION DENIED");
                }
            }
    );

    private boolean validateData() {
        boolean isValid = false;
        Date now = new Date();
        try {
            if ( !now.after(Utils.stringToDate(etSupplyDate.getText().toString()))) {
                Toast.makeText(getApplicationContext(), R.string.FuelSupply_Supply_Date_must_be_less, Toast.LENGTH_LONG).show();
            }
            if (!etGasStation.getText().toString().trim().isEmpty()
                && !etGasStationLocation.getText().toString().trim().isEmpty()
                && !String.valueOf(nrSpCombustible).trim().isEmpty()
                && !etSupplyDate.getText().toString().trim().isEmpty()
                && now.after(Utils.stringToDate(etSupplyDate.getText().toString()))
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
                //&& !String.valueOf(nrSpAccountId).trim().isEmpty()
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
        try {
            etGasStation.setText(position.getPlace().getName());
            etGasStationLocation.setText(getCityNameByCoordinates(Objects.requireNonNull(position.getPlace().getLatLng()).latitude, Objects.requireNonNull(position.getPlace().getLatLng()).longitude));
        } catch (IOException e) {
            e.printStackTrace();
        }
        listPlaces.setVisibility(View.GONE);
    }
}