package com.jacksonasantos.travelplan.ui.travel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Achievement;
import com.jacksonasantos.travelplan.dao.Itinerary;
import com.jacksonasantos.travelplan.dao.Marker;
import com.jacksonasantos.travelplan.dao.Tour;
import com.jacksonasantos.travelplan.dao.Travel;
import com.jacksonasantos.travelplan.dao.TravelExpenses;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.general.AchievementActivity;
import com.jacksonasantos.travelplan.ui.utility.Abbreviations;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MarkerActivity extends AppCompatActivity implements MarkerTypeListAdapter.ItemClickListener{
    
    private boolean opInsert = true;

    private Marker marker;
    Travel travel;

    private TextView tvLat, tvLng, tvName, tvAddress, tvCity, tvState, tvAbbrCountry, tvCountry;
    private Spinner spinItinerary, spinMarkerAchievement, spinTour;
    private Integer nrSpinItinerary, nrSpinMarkerAchievement, nrSpinTour;
    private EditText etSeq, etDescription, etExpectedValue, etPredictedStopTime ;
    private ImageButton btLocation, btAddAchievement, btAddTour;
    private RecyclerView rvListMarkers, rvMarkerType;
    private LinearLayout llItinerary, llAchievement, llTour;
    private int nrMarkerType = -1;

    public MarkerTypeListAdapter adapterMarkerType;

    protected ArrayAdapter<Tour> adapterT = null;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Database mDb = new Database(getApplicationContext());
        mDb.open();

        setTitle(R.string.marker);
        setContentView(R.layout.dialog_marker);

        if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        tvLat = findViewById(R.id.tvLat);
        tvLng = findViewById(R.id.tvLng);
        tvName = findViewById(R.id.tvName);
        tvAddress = findViewById(R.id.tvAddress);
        tvCity = findViewById(R.id.tvCity);
        tvState = findViewById(R.id.tvState);
        tvAbbrCountry = findViewById(R.id.tvAbbrCountry);
        tvCountry = findViewById(R.id.tvCountry);
        btLocation = findViewById(R.id.btLocation);
        btAddAchievement = findViewById(R.id.btAddAchievement);
        btAddTour = findViewById(R.id.btAddTour);
        spinItinerary = findViewById(R.id.spinItinerary);
        spinMarkerAchievement = findViewById(R.id.spinMarkerAchievement);
        spinTour =findViewById(R.id.spinTour);
        etSeq = findViewById(R.id.etSeq);
        etDescription = findViewById(R.id.etDescription);
        etExpectedValue = findViewById(R.id.etExpectedValue);
        etPredictedStopTime = findViewById(R.id.etPredictedStopTime);
        rvListMarkers = findViewById(R.id.rvListMarkers);
        rvMarkerType = findViewById(R.id.rvMarkerType);
        llItinerary = findViewById(R.id.llItinerary);
        llAchievement = findViewById(R.id.llAchievement);
        llTour = findViewById(R.id.llTour);

        Bundle extras = getIntent().getExtras();
        marker = new Marker();
        if (extras != null) {
            if (extras.getInt( "marker_id") > 0) {
                marker.setId(extras.getInt("marker_id"));
                marker = Database.mMarkerDao.fetchMarkerById(marker.getId());
                opInsert = false;
            } else {
                if (extras.getDouble( "lat") != 0) {
                    LatLng point = new LatLng(extras.getDouble("lat"), extras.getDouble("lng"));
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    try {
                        final List<Address> addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
                        assert addresses != null;
                        if (addresses.size()>0) {
                            tvLat.setText(String.valueOf(point.latitude));
                            tvLng.setText(String.valueOf(point.longitude));
                            tvName.setText(Objects.requireNonNull(addresses).get(0).getFeatureName());
                            tvAddress.setText(addresses.get(0).getAddressLine(0));
                            tvCity.setText(addresses.get(0).getSubAdminArea());
                            tvState.setText(Abbreviations.getAbbreviationFromState(addresses.get(0).getAdminArea()));
                            tvAbbrCountry.setText(addresses.get(0).getCountryCode());
                            tvCountry.setText(addresses.get(0).getCountryName());
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            if (extras.getInt( "travel_id") > 0) {
                marker.setTravel_id(extras.getInt("travel_id"));
            }
            if (extras.getInt( "itinerary_id") > 0) {
                marker.setItinerary_id(extras.getInt("itinerary_id"));
            }
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        travel = Database.mTravelDao.fetchTravelById(marker.getTravel_id());
        addListenerOnButtonSave();

        btLocation.setOnClickListener(view -> {
            Intent intent = new Intent (getBaseContext(), MaintenanceItineraryActivity.class);
            if (!tvLat.getText().toString().equals("")) {
                intent.putExtra("local_search", tvLat.getText().toString()+","+tvLng.getText().toString());
            } else {
                if (!etDescription.getText().toString().equals("")) {
                    intent.putExtra("local_search", etDescription.getText().toString());
                } else {
                    intent.putExtra("local_search", "Selection local");
                }
            }
            myActivityResultLauncher.launch(intent);
        });

        btAddAchievement.setOnClickListener(view -> {
            Intent intent = new Intent (getBaseContext(), AchievementActivity.class);
            myActivityResultLauncher.launch(intent);
        });

        btAddTour.setOnClickListener(view -> {
            Intent intent = new Intent (getBaseContext(), TourActivity.class);
            intent.putExtra("travel_id", marker.getTravel_id());
            myActivityResultLauncher.launch(intent);
        });

        final List<Itinerary> itineraries =  Database.mItineraryDao.fetchAllItineraryByTravel(travel.getId());
        itineraries.add(0, new Itinerary());
        ArrayAdapter<Itinerary> adapterA = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, itineraries);
        adapterA.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinItinerary.setAdapter(adapterA);
        nrSpinItinerary = marker.getItinerary_id();
        spinItinerary.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long idx) {
                nrSpinItinerary = ((Itinerary) parent.getItemAtPosition(position)).getId();
                spinItinerary.setSelection(position);
                MarkerListAdapter adapterMarker = new MarkerListAdapter(Database.mMarkerDao.fetchMarkerByTravelItineraryId(travel.getId(), nrSpinItinerary), getApplicationContext(), 1, 0, false, travel.getId(), nrSpinItinerary);
                rvListMarkers.setAdapter(adapterMarker);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {nrSpinItinerary =-1; }
        });

        final List<Achievement> achievements =  Database.mAchievementDao.fetchAllAchievement();
        achievements.add(0, new Achievement());
        ArrayAdapter<Achievement> adapterA1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, achievements);
        adapterA1.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinMarkerAchievement.setAdapter(adapterA1);
        nrSpinMarkerAchievement = marker.getItinerary_id();
        spinMarkerAchievement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long idx) {
                nrSpinMarkerAchievement = ((Achievement) parent.getItemAtPosition(position)).getId();
                spinMarkerAchievement.setSelection(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {nrSpinMarkerAchievement = -1;}
        });

        final List<Tour> tours =  Database.mTourDao.fetchAllTourByTravel(marker.getTravel_id());
        tours.add(0, new Tour());
        adapterT = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tours);
        adapterT.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinTour.setAdapter(adapterT);
        nrSpinTour = marker.getTour_id();
        spinTour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long idx) {
                nrSpinTour = ((Tour) parent.getItemAtPosition(position)).getId();
                spinTour.setSelection(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {nrSpinTour = -1;}
        });

        nrMarkerType = -1;
        List<Integer> vMarkerType = new ArrayList<>();
        for(int i = 0; i < getApplicationContext().getResources().getStringArray(R.array.marker_type_array).length; i++) {
            vMarkerType.add(i);
        }
        adapterMarkerType = new MarkerTypeListAdapter(vMarkerType, this);
        adapterMarkerType.addItemClickListener(this);
        rvMarkerType.setAdapter(adapterMarkerType);
        rvMarkerType.setLayoutManager(new GridLayoutManager(this, 5));

        MarkerListAdapter adapterMarker = new MarkerListAdapter(Database.mMarkerDao.fetchMarkerByTravelItineraryId(travel.getId(), nrSpinItinerary), getApplicationContext(), 1, 0, false, travel.getId(), nrSpinItinerary);
        rvListMarkers.setAdapter(adapterMarker);
        rvListMarkers.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        travel = Database.mTravelDao.fetchTravelById(marker.getTravel_id());
        if (opInsert) {
            Marker markerLast = Database.mMarkerDao.fetchLastMarkerByTravelItinerary(marker.getTravel_id(), marker.getItinerary_id());
            if (markerLast.getSequence() != null) {
                etSeq.setText(String.valueOf(markerLast.getSequence() + 1));
            } else {
                etSeq.setText(String.valueOf(1));
            }
            nrSpinItinerary = marker.getItinerary_id();
            nrSpinMarkerAchievement = -1;
            nrMarkerType = -1;
            Utils.selected_position = nrMarkerType;
        } else {
            tvLat.setText(marker.getLatitude());
            tvLng.setText(marker.getLongitude());
            tvName.setText(marker.getName());
            tvAddress.setText(marker.getAddress());
            tvCity.setText(marker.getCity());
            tvState.setText(marker.getState());
            tvAbbrCountry.setText(marker.getAbbr_country());
            tvCountry.setText(marker.getCountry());
            etSeq.setText(String.valueOf(marker.getSequence()));
            etDescription.setText(marker.getDescription());
            nrSpinItinerary = marker.getItinerary_id();
            nrSpinMarkerAchievement = marker.getAchievement_id();
            nrSpinTour = marker.getTour_id();
            nrMarkerType = marker.getMarker_type();
            Utils.selected_position = nrMarkerType;
            showLayers(nrMarkerType);
            TravelExpenses te1 = Database.mTravelExpensesDao.fetchTravelExpensesByTravelMarker(travel.getId(), marker.getId());
            etExpectedValue.setText(te1.getExpected_value() == null ? "0.00" : te1.getExpected_value().toString());
            //marker.setDuration_Predicted_stop_time(marker.getPredicted_stop_time() == 0 ? 0 : marker.getMarker_typePredictedTime(marker.getMarker_typePredictedTime(nrMarkerType)));
            etPredictedStopTime.setText(marker.getDuration_Predicted_stop_time());
        }
        etExpectedValue.setOnFocusChangeListener((v, hasFocus) -> etExpectedValue.setText(""));
        if (nrSpinItinerary != null && nrSpinItinerary > 0) {
            Itinerary i1 = Database.mItineraryDao.fetchItineraryById(nrSpinItinerary);
            for (int x = 1; x <= spinItinerary.getAdapter().getCount(); x++) {
                if (spinItinerary.getAdapter().getItem(x).toString().equals(i1.toString())) {
                    spinItinerary.setSelection(x);
                    break;
                }
            }
        }
        if (nrSpinMarkerAchievement != null && nrSpinMarkerAchievement > 0) {
            Achievement a1 = Database.mAchievementDao.fetchAchievementById(nrSpinMarkerAchievement);
            for (int x = 1; x <= spinMarkerAchievement.getAdapter().getCount(); x++) {
                if (spinMarkerAchievement.getAdapter().getItem(x).toString().equals(a1.getName())) {
                    spinMarkerAchievement.setSelection(x);
                    break;
                }
            }
        }
        if (nrSpinTour != null && nrSpinTour > 0) {
            Tour t1 = Database.mTourDao.fetchTourById(nrSpinTour);
            for (int x = 1; x <= spinTour.getAdapter().getCount(); x++) {
                if (spinTour.getAdapter().getItem(x).toString().equals(t1.toString())) {
                    spinTour.setSelection(x);
                    break;
                }
            }
        }
    }

    private void showLayers(int nrMarkerType) {
        llItinerary.setVisibility(View.VISIBLE);
        llTour.setVisibility(View.GONE);
        llAchievement.setVisibility(View.GONE);

        switch (nrMarkerType) {
            case 6: llTour.setVisibility(View.VISIBLE); nrSpinTour=null; break;
            case 9: llAchievement.setVisibility(View.VISIBLE); nrSpinMarkerAchievement=null; break;
        }
    }

    @Override
    public void onItemClick(int position) {
        if (nrMarkerType == position) nrMarkerType = -1;
        else nrMarkerType = position;
        Utils.selected_position = nrMarkerType;
        showLayers(nrMarkerType);
        etExpectedValue.setText(String.valueOf(marker.getMarker_typeExpectedValue(nrMarkerType)==null? (Double) 0.00 :marker.getMarker_typeExpectedValue(nrMarkerType)));
        etPredictedStopTime.setText(marker.getDuration_Predicted_stop_time(marker.getMarker_typePredictedTime(nrMarkerType)));
    }

    final ActivityResultLauncher<Intent> myActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 124) {
                        Intent data = result.getData();
                        if (data != null) {
                            String[] point = Objects.requireNonNull(data.getStringExtra("resulted_value")).split(",");
                            tvLat.setText(point[0]);
                            tvLng.setText(point[1]);
                            tvName.setText(data.getStringExtra("resulted_feature"));
                            tvAddress.setText(data.getStringExtra("resulted_address"));
                            tvCity.setText(data.getStringExtra("resulted_city"));
                            tvState.setText(data.getStringExtra("resulted_state"));
                            tvCountry.setText(data.getStringExtra("resulted_country"));
                        }
                    } else {
                        if (result.getResultCode() == 128) {
                            final List<Tour> tours =  Database.mTourDao.fetchAllTourByTravel(marker.getTravel_id());
                            tours.add(0, new Tour());
                            adapterT.clear();
                            adapterT.addAll(tours);
                            adapterT.notifyDataSetChanged() ;
                            spinTour.requestFocus();
                        }
                    }
                }
            });

    public void addListenerOnButtonSave() {
        Button btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> {
            boolean isSave = false;

            if (!validateData()) {
                Toast.makeText(getApplicationContext(), R.string.Error_Data_Validation, Toast.LENGTH_LONG).show();
            } else {
                Marker m1 = new Marker();
                m1.setId(null);
                m1.setTravel_id(marker.getTravel_id());
                m1.setLatitude(tvLat.getText().toString());
                m1.setLongitude(tvLng.getText().toString());
                m1.setName(tvName.getText().toString());
                m1.setAddress(tvAddress.getText().toString());
                m1.setCity(tvCity.getText().toString());
                m1.setState(tvState.getText().toString());
                m1.setAbbr_country(tvAbbrCountry.getText().toString());
                m1.setCountry(tvCountry.getText().toString());
                m1.setItinerary_id(nrSpinItinerary);
                m1.setAchievement_id(nrSpinMarkerAchievement);
                m1.setTour_id(nrSpinTour);
                m1.setMarker_type(nrMarkerType);
                m1.setSequence(Integer.valueOf(etSeq.getText().toString()));
                m1.setDescription(etDescription.getText().toString());
                m1.setDuration_Predicted_stop_time(etPredictedStopTime.getText().toString());

                if (!opInsert) {
                    try {
                        m1.setId(marker.getId());
                        /*if (nrSpinMarkerAchievement != null && nrSpinMarkerAchievement > 0 ) {
                            // Changes the achievement raising awareness to Travel and Itinerary
                            Achievement a = Database.mAchievementDao.fetchAchievementById(marker.getAchievement_id());
                            a.setTravel_id(null);
                            a.setItinerary_id(null);
                            isSave = Database.mAchievementDao.updateAchievement(a);

                            a = Database.mAchievementDao.fetchAchievementById(nrSpinMarkerAchievement);
                            a.setLatlng_achievement(tvLat.getText().toString() + "," + tvLng.getText().toString());
                            a.setTravel_id(a.getTravel_id()==null?travel.getId():a.getTravel_id());
                            a.setItinerary_id(a.getItinerary_id()==null ?nrSpinMarkerAchievement:a.getItinerary_id());
                            isSave = Database.mAchievementDao.updateAchievement(a);
                        }*/
                        // Changes TravelExpenses
                        TravelExpenses te = Database.mTravelExpensesDao.fetchTravelExpensesByTravelMarker(m1.getTravel_id(), m1.getId());
                        etExpectedValue.setText(etExpectedValue.getText().toString().isEmpty() ? "0.00" : etExpectedValue.getText().toString());
                        if (te.getId() != null) {
                            if (Double.parseDouble(etExpectedValue.getText().toString()) > 0) {
                                te.setExpense_type(m1.getMarker_typeExpenseType(nrMarkerType));
                                te.setExpected_value(Double.parseDouble(etExpectedValue.getText().toString()));
                                te.setNote(getResources().getString(R.string.marker_itinerary) + ": " + nrSpinItinerary);
                                // Change existing TravelExpenses data
                                isSave = Database.mTravelExpensesDao.updateTravelExpenses(te);
                            } else {
                                isSave= Database.mTravelExpensesDao.deleteTravelExpenses(te.getId());
                            }
                        } else {
                            if (Double.parseDouble(etExpectedValue.getText().toString()) > 0) {
                                // Adds new TravelExpenses when screen value is informed
                                te = new TravelExpenses();
                                te.setTravel_id(m1.getTravel_id());
                                te.setExpense_type(m1.getMarker_typeExpenseType(nrMarkerType));
                                te.setExpected_value(Double.parseDouble(etExpectedValue.getText().toString()));
                                te.setNote(getResources().getString(R.string.marker_itinerary) + ": " + nrSpinItinerary);
                                te.setMarker_id(m1.getId());
                                isSave = Database.mTravelExpensesDao.addTravelExpenses(te);
                            }
                        }
                        // Changes the marker
                        isSave = Database.mMarkerDao.updateMarker(m1);
                    } catch (Exception e) {
                        Log.i(getResources().getString(R.string.Error_Changing_Data), Objects.requireNonNull(e.getMessage()));
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Error_Changing_Data) + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        // Adjusts the marker in the sequence position
                        isSave = adjustMarker(travel.getId(), nrSpinItinerary, m1.getSequence(), true);
                        /*if ( nrSpinMarkerAchievement != null && nrSpinMarkerAchievement > 0 ) {
                            // Changes the achievement raising awareness to Travel and Itinerary
                            Achievement a = Database.mAchievementDao.fetchAchievementById(nrSpinMarkerAchievement);
                            a.setLatlng_achievement(tvLat.getText().toString() + "," + tvLng.getText().toString());
                            a.setTravel_id(a.getTravel_id()==null?travel.getId():a.getTravel_id());
                            a.setItinerary_id(a.getItinerary_id()==null?nrSpinItinerary:a.getItinerary_id());
                            isSave = Database.mAchievementDao.updateAchievement(a);
                        } else {
                            if ( m1.getMarker_type() == 9 ) {
                                // Adds new achievement when it doesn't exist
                                Achievement a = new Achievement();
                                a.setTravel_id(travel.getId());
                                a.setItinerary_id(nrSpinItinerary);
                                a.setShort_name(tvName.getText().toString());
                                a.setName(tvName.getText().toString());
                                a.setCity(tvCity.getText().toString());
                                a.setState(tvState.getText().toString());
                                a.setCountry(tvAbbrCountry.getText().toString());
                                a.setLatlng_achievement(tvLat.getText().toString()+","+tvLng.getText().toString());
                                a.setStatus_achievement(0);
                                Integer nrIdAchievement = Database.mAchievementDao.addAchievement(a);
                                isSave = (nrIdAchievement > 0);
                            }
                        }*/
                        // Adds new Marker
                        Integer nrIdMarker = Database.mMarkerDao.addMarker(m1);
                        if (!etExpectedValue.getText().toString().equals("") && Double.parseDouble(etExpectedValue.getText().toString()) > 0) {
                            // Adds TravelExpenses if ExpectedValue not null
                            TravelExpenses te = new TravelExpenses();
                            te.setTravel_id(m1.getTravel_id());
                            te.setExpense_type(m1.getMarker_typeExpenseType(nrMarkerType));
                            te.setExpected_value(Double.parseDouble(etExpectedValue.getText().toString()));
                            te.setNote(getResources().getString(R.string.marker_itinerary) + ": " + nrSpinItinerary);
                            te.setMarker_id(nrIdMarker);
                            isSave = Database.mTravelExpensesDao.addTravelExpenses(te);
                        }
                    } catch (Exception e) {
                        Log.i(getResources().getString(R.string.Error_Including_Data), Objects.requireNonNull(e.getMessage()));
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Error_Including_Data) +"| "+ e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                setResult(isSave ? 1 : 0);
                if (isSave) {
                    finish();
                } else {
                    Log.i(getResources().getString(R.string.Error_Saving_Data),"MarkerActivity()");
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.Error_Saving_Data), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    static boolean adjustMarker(Integer travel_id, Integer itinerary_id, int sequence, boolean increment){
        boolean result = true;
        List<Marker> cursor = Database.mMarkerDao.fetchMarkerByTravelItineraryId(travel_id, itinerary_id);
        for (int x = 0; x < cursor.size(); x++) {
            Marker m1 = cursor.get(x);
            if (m1.getSequence() >= sequence ){
                if (increment) {
                    m1.setSequence(m1.getSequence() + 1);
                } else {
                    m1.setSequence(m1.getSequence() - 1);
                }
                result = Database.mMarkerDao.updateMarker(m1);
            }
        }
        return result;
    }

    public static boolean removeMarker(Context context, Integer nrTravel_Id, LatLng point) {
        boolean result;
        try {
            Marker m = Database.mMarkerDao.fetchMarkerByPoint( nrTravel_Id, point);
            /*if (m.getAchievement_id()!=null && m.getAchievement_id()>0){
                Achievement a = Database.mAchievementDao.fetchAchievementById(m.getAchievement_id());
                a.setItinerary_id(null);
                a.setTravel_id(null);
                Database.mAchievementDao.updateAchievement(a);
            }*/
            TravelExpenses te = Database.mTravelExpensesDao.fetchTravelExpensesByTravelMarker(nrTravel_Id, m.getId() );
            result = Database.mTravelExpensesDao.deleteTravelExpenses(te.getId());
            if ( Database.mMarkerDao.deleteMarker(nrTravel_Id, String.valueOf(point.latitude), String.valueOf(point.longitude)) ) {
                result = adjustMarker(nrTravel_Id, m.getItinerary_id(), m.getSequence(), false);
            }
        } catch (Exception e) {
            Toast.makeText(context, context.getResources().getString(R.string.Error_Deleting_Data) + e.getMessage(), Toast.LENGTH_LONG).show();
            result = false;
        }
        return result;
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
        boolean isValid = true;

        try {
            if (marker.getTravel_id()==null ||
                //tvLat.getText().toString().trim().isEmpty() ||
                //tvLng.getText().toString().trim().isEmpty() ||
                //tvZoom.getText().toString().trim().isEmpty() ||
                //tvName.getText().toString().trim().isEmpty() ||
                //tvAddress.getText().toString().trim().isEmpty() ||
                //tvCity.getText().toString().trim().isEmpty() ||
                //tvState.getText().toString().trim().isEmpty() ||
                //tvAbbrCountry.getText().toString().trim().isEmpty() ||
                //tvCountry.getText().toString().trim().isEmpty() ||
                //tvCategory.getText().toString().trim().isEmpty() ||
                nrSpinItinerary == -1 ||
                nrMarkerType == -1 ||
                //nrSpinMarkerAchievement == -1 ||
                etSeq.getText().toString().trim().isEmpty()
                //etDescription.getText().toString().trim().isEmpty()
                //etExpectedValue.setText(String.valueOf(0));
                //etPredictedTime.getText().toString().trim().isEmpty()
            ){
                isValid = false;
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Data_Validator_Error) +" - " + e.getMessage(), Toast.LENGTH_LONG).show();
            isValid = false;
        }
        return isValid;
    }
}