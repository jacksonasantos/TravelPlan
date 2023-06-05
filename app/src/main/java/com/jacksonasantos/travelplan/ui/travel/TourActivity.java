package com.jacksonasantos.travelplan.ui.travel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Achievement;
import com.jacksonasantos.travelplan.dao.Itinerary;
import com.jacksonasantos.travelplan.dao.Marker;
import com.jacksonasantos.travelplan.dao.Tour;
import com.jacksonasantos.travelplan.dao.Travel;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.DateInputMask;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TourActivity extends AppCompatActivity implements TourTypeListAdapter.ItemClickListener {
    
    private boolean opInsert = true;

    private Tour tour;

    private TextView tvTravel;
    private TextView tvItinerary;
    private TextView tvMarker;
    private RecyclerView rvTourType;
    private int nrTourType = -1;

    private Spinner spAchievement;
    private Integer nrSpAchievement;
    private EditText etLocalTour ;
    private EditText etDate ;
    private EditText etValueAdult;
    private EditText etValueChild ;
    private EditText etNumberAdult;
    private EditText etNumberChild ;
    private Spinner spCurrencyType;
    private int nrSpCurrencyType;
    private EditText etOpeningHours;
    private EditText etVisitationTime;
    private EditText etNote;
    private EditText etAddressTour;
    private EditText etCityTour;
    private EditText etStateTour;
    private EditText etCountryTour;
    private EditText etLatLngTour;
    private ImageButton btLocationTour;

    public TourTypeListAdapter adapterTourType;

    Travel travel;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Database mDb = new Database(getApplicationContext());
        mDb.open();

        setTitle(R.string.Tour);
        setContentView(R.layout.dialog_tour);

        Bundle extras = getIntent().getExtras();
        tour = new Tour();
        tour.setValue_adult(0.0);
        tour.setValue_child(0.0);
        if (extras != null) {
            if (extras.getInt( "tour_id") > 0) {
                tour.setId(extras.getInt("tour_id"));
                tour = Database.mTourDao.fetchTourById(tour.getId());
                opInsert = false;
            }
            if (extras.getInt( "travel_id") > 0) {
                tour.setTravel_id(extras.getInt("travel_id"));
            }
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        travel = Database.mTravelDao.fetchTravelById(tour.getTravel_id());
        addListenerOnButtonSave();
        tvTravel = findViewById(R.id.tvTravel);
        tvItinerary = findViewById(R.id.tvItinerary);
        tvMarker = findViewById(R.id.tvMarker);
        rvTourType = findViewById(R.id.rvTourType);
        spAchievement = findViewById(R.id.spAchievement);
        etLocalTour = findViewById(R.id.etLocalTour);
        etDate = findViewById(R.id.etDate);
        etValueAdult = findViewById(R.id.etValueAdult);
        etValueChild = findViewById(R.id.etValueChild);
        etNumberAdult = findViewById(R.id.etNumberAdult);
        etNumberChild = findViewById(R.id.etNumberChild);
        spCurrencyType = findViewById(R.id.spCurrencyType);
        etOpeningHours = findViewById(R.id.etOpeningHours);
        etVisitationTime = findViewById(R.id.etVisitationTime);
        etNote = findViewById(R.id.etNote);
        etAddressTour = findViewById(R.id.etAddressTour);
        etCityTour = findViewById(R.id.etCityTour);
        etStateTour = findViewById(R.id.etStateTour);
        etCountryTour = findViewById(R.id.etCountryTour);
        etLatLngTour = findViewById(R.id.etLatLngTour);
        btLocationTour = findViewById(R.id.btLocationTour);

        btLocationTour.setOnClickListener(view -> {
            Intent intent = new Intent (getBaseContext(), MaintenanceItineraryActivity.class);
            if (!etLatLngTour.getText().toString().equals("")) {
                intent.putExtra("local_search", etLatLngTour.getText().toString());
            } else {
                intent.putExtra("local_search", etLocalTour.getText().toString()+ "," + etAddressTour.getText().toString()+"," + etCityTour.getText().toString()+"," + etStateTour.getText().toString()+"," + etCountryTour.getText().toString());
            }
            myActivityResultLauncher.launch(intent);
        });
        etDate.addTextChangedListener(new DateInputMask(etDate));
        etDate.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus) {
                Date d = Utils.stringToDate(etDate.getText().toString());
                if (d != null) {
                    try {
                        if ( d.before(travel.getDeparture_date()) ||
                            d.after(travel.getReturn_date())) {
                            etDate.setError(getResources().getString(R.string.Error_Date_Invalid));
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        final List<Achievement> achievements =  Database.mAchievementDao.fetchAllAchievement();
        achievements.add(0, new Achievement());
        ArrayAdapter<Achievement> adapterA = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, achievements);
        adapterA.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spAchievement.setAdapter(adapterA);

        nrSpAchievement = tour.getAchievement_id();
        spAchievement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long idx) {
                nrSpAchievement = ((Achievement) parent.getItemAtPosition(position)).getId();
                spAchievement.setSelection(position);
                if (opInsert) {
                    etCityTour.setText(((Achievement) parent.getItemAtPosition(position)).getCity());
                    etStateTour.setText(((Achievement) parent.getItemAtPosition(position)).getState());
                    etCountryTour.setText(((Achievement) parent.getItemAtPosition(position)).getCountry());
                    etLatLngTour.setText(((Achievement) parent.getItemAtPosition(position)).getLatlng_achievement());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                nrSpAchievement =0;
            }
        });

        Utils.createSpinnerResources(R.array.currency_array, spCurrencyType, this);
        nrSpCurrencyType = tour.getCurrency_type();
        spCurrencyType.setSelection(nrSpCurrencyType);
        spCurrencyType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long idx) {
                nrSpCurrencyType = position;
                spCurrencyType.setSelection(nrSpCurrencyType);

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                nrSpCurrencyType =0;
            }
        });

        nrTourType = -1;

        List<Integer> vTourType = new ArrayList<>();
        for(int i = 0; i < getApplicationContext().getResources().getStringArray(R.array.tour_type_array).length; i++) {
            vTourType.add(i);
        }

        adapterTourType = new TourTypeListAdapter(vTourType, this);
        adapterTourType.addItemClickListener(this);
        rvTourType.setAdapter(adapterTourType);
        rvTourType.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        etValueAdult.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {etValueAdult.setText("");}
            else if(etValueAdult.getText().toString().equals("")) etValueAdult.setText("0");
        });
        etValueChild.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {etValueChild.setText("");}
            else if(etValueChild.getText().toString().equals("")) etValueChild.setText("0");
        });
        etNumberAdult.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {etNumberAdult.setText("");}
            else if(etNumberAdult.getText().toString().equals("")) etNumberAdult.setText("0");
        });
        etNumberChild.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {etNumberChild.setText("");}
            else if(etNumberChild.getText().toString().equals("")) etNumberChild.setText("0");
        });
        if (tour != null) {
            travel = Database.mTravelDao.fetchTravelById(tour.getTravel_id());
            Marker marker = Database.mMarkerDao.fetchMarkerByTour(tour.getId());
            Itinerary itinerary = Database.mItineraryDao.fetchItineraryById(marker.getItinerary_id());
            tvTravel.setText(travel.getDescription());
            tvItinerary.setText(itinerary.toString());
            tvMarker.setText(marker.toString());
            nrTourType = tour.getTour_type();
            Utils.selected_position = nrTourType;
            nrSpAchievement = tour.getAchievement_id();
            if (nrSpAchievement != null && nrSpAchievement > 0) {
                Achievement a1 = Database.mAchievementDao.fetchAchievementById(nrSpAchievement);
                for (int x = 1; x <= spAchievement.getAdapter().getCount(); x++) {
                    if (spAchievement.getAdapter().getItem(x).toString().equals(a1.getName())) {
                        spAchievement.setSelection(x);
                        break;
                    }
                }
            }
            etLocalTour.setText(tour.getLocal_tour());
            etDate.setText(Utils.dateToString(tour.getTour_date()));
            etValueAdult.setText(String.valueOf(tour.getValue_adult()));
            etValueChild.setText(String.valueOf(tour.getValue_child()));
            etNumberAdult.setText(String.valueOf(tour.getNumber_adult()));
            etNumberChild.setText(String.valueOf(tour.getNumber_child()));
            nrSpCurrencyType = tour.getCurrency_type();
            spCurrencyType.setSelection(nrSpCurrencyType);
            etOpeningHours.setText(tour.getOpening_hours());
            etVisitationTime.setText(tour.getVisitation_time());
            etNote.setText(tour.getNote());
            etAddressTour.setText(tour.getAddress_tour());
            etCityTour.setText(tour.getCity_tour());
            etStateTour.setText(tour.getState_tour());
            etCountryTour.setText(tour.getCountry_tour());
            etLatLngTour.setText(tour.getLatlng_tour());
        }
    }

    final ActivityResultLauncher<Intent> myActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 124) {
                        Intent data = result.getData();
                        if (data != null) {
                            etLatLngTour.setText(data.getStringExtra("resulted_value"));
                            etLocalTour.setText(data.getStringExtra("resulted_feature"));
                            etAddressTour.setText(data.getStringExtra("resulted_address"));
                            etCityTour.setText(data.getStringExtra("resulted_city"));
                            etStateTour.setText(data.getStringExtra("resulted_state"));
                            etCountryTour.setText(data.getStringExtra("resulted_country"));
                        }
                    }
                }
            });

    @Override
    public void onItemClick(int position) {
        if (nrTourType == position) nrTourType = -1;
        else nrTourType = position;
        Utils.selected_position = nrTourType;
    }

    public void addListenerOnButtonSave() {
        Button btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> {
            boolean isSave = false;

            if (!validateData()) {
                Toast.makeText(getApplicationContext(), R.string.Error_Data_Validation, Toast.LENGTH_LONG).show();
            } else {
                final Tour t1 = new Tour();
                t1.setTravel_id(tour.getTravel_id());
                t1.setLocal_tour(etLocalTour.getText().toString());
                t1.setTour_type(nrTourType);
                t1.setAchievement_id(nrSpAchievement);
                t1.setCurrency_type(nrSpCurrencyType);
                t1.setValue_adult(Double.parseDouble(etValueAdult.getText().toString()));
                t1.setValue_child(Double.parseDouble(etValueChild.getText().toString()));
                t1.setNumber_adult(Integer.parseInt(etNumberAdult.getText().toString()));
                t1.setNumber_child(Integer.parseInt(etNumberChild.getText().toString()));
                t1.setTour_date(Utils.stringToDate(etDate.getText().toString()));
                t1.setOpening_hours(etOpeningHours.getText().toString());
                t1.setDistance(0);
                t1.setVisitation_time(etVisitationTime.getText().toString());
                t1.setNote(etNote.getText().toString());
                t1.setAddress_tour(etAddressTour.getText().toString());
                t1.setCity_tour(etCityTour.getText().toString());
                t1.setState_tour(etStateTour.getText().toString());
                t1.setCountry_tour(etCountryTour.getText().toString());
                t1.setLatlng_tour(etLatLngTour.getText().toString());

                if (!opInsert) {
                    try {
                        t1.setId(tour.getId());
                        isSave = Database.mTourDao.updateTour(t1);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), R.string.Error_Changing_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        isSave = Database.mTourDao.addTour(t1);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), R.string.Error_Including_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                setResult(isSave ? 1 : 0);
                if (isSave) {
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.Error_Saving_Data, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private boolean validateData() {
        boolean isValid = true;

        try {
            Date d1 = Utils.stringToDate(etDate.getText().toString());
            if (d1 !=null &&
                (nrTourType == -1 ||
                 //nrSpAchievement == -1 ||
                 etLocalTour.getText().toString().trim().isEmpty() ||
                 etDate.getText().toString().trim().isEmpty() ||
                 d1.before(travel.getDeparture_date()) ||
                 d1.after(travel.getReturn_date()))
                //etValueAdult.getText().toString().trim().isEmpty() ||
                //etValueChild.getText().toString().trim().isEmpty() ||
                //etNumberAdult.getText().toString().trim().isEmpty() ||
                //etNumberChild.getText().toString().trim().isEmpty() ||
                //nrSpCurrencyType == -1 ||
                //etOpeningHours.getText().toString().trim().isEmpty() ||
                //etVisitationTime.getText().toString().trim().isEmpty() ||
                //etNote.getText().toString().trim().isEmpty()||
                //etAddressTour.getText().toString().trim().isEmpty()||
                //etCityTour.getText().toString().trim().isEmpty()||
                //etStateTour.getText().toString().trim().isEmpty()||
                //etCountryTour.getText().toString().trim().isEmpty()||
                //etLatLngTour.getText().toString().trim().isEmpty()
            ){
                isValid = false;
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.Data_Validator_Error +" - " + e.getMessage(), Toast.LENGTH_LONG).show();
            isValid = false;
        }
        return isValid;
    }
}