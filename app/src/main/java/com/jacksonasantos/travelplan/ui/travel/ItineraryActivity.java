package com.jacksonasantos.travelplan.ui.travel;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Itinerary;
import com.jacksonasantos.travelplan.dao.general.Database;

public class ItineraryActivity extends AppCompatActivity {
    
    private boolean opInsert = true;

    private Itinerary itinerary;

    private EditText etSequence;
    private TextView tvDate;
    private EditText etOrig_location;
    private EditText etDest_location;
    private EditText etDaily;
    private Spinner spTravel_mode;
    private LinearLayout llItineraryHasTransport;
    private RecyclerView rvItineraryHasTransport;
    private LinearLayout llMarker;
    private RecyclerView rvMarker;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Database mDb = new Database(getApplicationContext());
        mDb.open();

        setTitle(R.string.Itinerary);
        setContentView(R.layout.dialog_itinerary);

        Bundle extras = getIntent().getExtras();
        itinerary = new Itinerary();
        if (extras != null) {
            if (extras.getInt( "itinerary_id") > 0) {
                itinerary.setId(extras.getInt("itinerary_id"));
                itinerary = Database.mItineraryDao.fetchItineraryById(itinerary.getId());
                opInsert = false;
            }
            if (extras.getInt( "travel_id") > 0) {
                itinerary.setTravel_id(extras.getInt("travel_id"));
            }
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        addListenerOnButtonSave();
        etSequence = findViewById(R.id.etSequence);
        tvDate = findViewById(R.id.tvDate);
        etOrig_location = findViewById(R.id.etOrig_location);
        etDest_location = findViewById(R.id.etDest_location);
        etDaily = findViewById(R.id.etDaily);
        spTravel_mode = findViewById(R.id.spTravel_mode);
        llItineraryHasTransport = findViewById(R.id.llItineraryHasTransport);
        rvItineraryHasTransport = findViewById(R.id.rvItineraryHasTransport);
        llMarker = findViewById(R.id.llMarker);
        rvMarker = findViewById(R.id.rvMarker);

        if (opInsert){
            Itinerary itineraryLast = Database.mItineraryDao.fetchLastItineraryByTravel(itinerary.getTravel_id());
            if (itineraryLast != null) {
                etSequence.setText(String.valueOf(itineraryLast.getSequence() + 1));
                etOrig_location.setText(itineraryLast.getDest_location());
            } else {
                etSequence.setText(String.valueOf(1));
            }
            tvDate.setText(Database.mItineraryDao.fetchItineraryDateSequence(itinerary.getTravel_id(),0));
            llItineraryHasTransport.setVisibility(View.INVISIBLE);
            llMarker.setVisibility(View.INVISIBLE);
        } else {
            etSequence.setText(String.valueOf(itinerary.getSequence()));
            tvDate.setText(Database.mItineraryDao.fetchItineraryDateSequence(itinerary.getTravel_id(),itinerary.getSequence()));
            etDest_location.setText(itinerary.getDest_location());
            etOrig_location.setText(itinerary.getOrig_location());
            etDaily.setText(String.valueOf(itinerary.getDaily()));
            spTravel_mode.setSelection(itinerary.getTravel_mode());
            llItineraryHasTransport.setVisibility(View.VISIBLE);
            llMarker.setVisibility(View.VISIBLE);

            // Itinerary has Transport
            final int Show_Header_ItineraryHasTransport = 1  ;
            ItineraryHasTransportListAdapter adapterItineraryHasTransport = new ItineraryHasTransportListAdapter(Database.mItineraryHasTransportDao.fetchAllItineraryHasTransportByTravelItinerary(itinerary.getTravel_id(), itinerary.getId() ), getApplicationContext(),"Home", Show_Header_ItineraryHasTransport, itinerary.getTravel_id());
            rvItineraryHasTransport.setAdapter(adapterItineraryHasTransport);
            rvItineraryHasTransport.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            // Marker
            final int Show_Header_Marker = 1  ;
            MarkerListAdapter adapterMarker = new MarkerListAdapter(Database.mMarkerDao.fetchMarkerByTravelItineraryId(itinerary.getTravel_id(), itinerary.getId() ), getApplicationContext(), Show_Header_Marker, itinerary.getTravel_id());
            rvMarker.setAdapter(adapterMarker);
            rvMarker.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        }
    }

    public void addListenerOnButtonSave() {
        Button btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> {
            boolean isSave = false;

            if (!validateData()) {
                Toast.makeText(getApplicationContext(), R.string.Error_Data_Validation, Toast.LENGTH_LONG).show();
            } else {
                final Itinerary i1 = new Itinerary();

                if (!opInsert) i1.setId(itinerary.getId());
                i1.setTravel_id(itinerary.getTravel_id());
                i1.setSequence(Integer.parseInt(etSequence.getText().toString()));
                i1.setOrig_location(etOrig_location.getText().toString());
                i1.setDest_location(etDest_location.getText().toString());
                i1.setDaily(Integer.parseInt(etDaily.getText().toString()));
                i1.setTravel_mode(spTravel_mode.getSelectedItemPosition());
                if (!opInsert) i1.setDistance(itinerary.getDistance());
                if (!opInsert) i1.setTime(itinerary.getTime());

                if (!opInsert) {
                    try {
                        isSave = Database.mItineraryDao.updateItinerary(i1);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), R.string.Error_Changing_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        isSave = Database.mItineraryDao.addItinerary(i1);
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
            if (etSequence.getText().toString().isEmpty() ||
                etOrig_location.getText().toString().isEmpty() ||
                etDest_location.getText().toString().isEmpty() ||
                etDaily.getText().toString().isEmpty() ||
                spTravel_mode.getSelectedItemPosition() < 0) {
                isValid = false;
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.Data_Validator_Error + " - " + e.getMessage(), Toast.LENGTH_LONG).show();
            isValid = false;
        }
        return isValid;
    }
}