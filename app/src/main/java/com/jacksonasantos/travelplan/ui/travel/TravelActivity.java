package com.jacksonasantos.travelplan.ui.travel;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Travel;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.DateInputMask;
import com.jacksonasantos.travelplan.ui.utility.Utils;

public class TravelActivity extends AppCompatActivity {
    private EditText etDescription;
    private EditText etDeparture_date;
    private EditText etReturn_date;
    private EditText etNote;
    private int nrStatus;
    private TextView tvStatus;

    private RecyclerView rvVehicleTravel;
    private RecyclerView rvTourTravel;
    private RecyclerView rvAchievementTravel;
    private RecyclerView rvTravelExpenses;
    private RecyclerView rvItinerary;
    private RecyclerView rvItineraryHasTransport;

    private TravelRouteFragment.HomeTravelItineraryListAdapter adapterItinerary;
    private ItineraryHasTransportListAdapter adapterItineraryHasTransport;
    private TravelVehicleListAdapter adapterVehicleTravel;
    private TravelTourListAdapter adapterTourTravel;
    private TravelAchievementListAdapter adapterAchievementTravel;
    private TravelExpensesListAdapter adapterTravelExpenses;

    private boolean opInsert = true;
    private Travel travel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.Travel);
        setContentView(R.layout.activity_travel);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        addListenerOnButtonSave();

        etDescription = findViewById(R.id.etDescription);
        etDeparture_date = findViewById(R.id.etDeparture_date);
        etReturn_date = findViewById(R.id.etReturn_date);
        etNote = findViewById(R.id.etNote);
        tvStatus = findViewById(R.id.tvStatus);

        rvItinerary = findViewById(R.id.rvItinerary);
        rvItineraryHasTransport = findViewById(R.id.rvItineraryHasTransport);
        rvVehicleTravel = findViewById(R.id.rvVehicleTravel);
        rvTourTravel = findViewById(R.id.rvTourTravel);
        rvAchievementTravel = findViewById(R.id.rvAchievementTravel);
        rvTravelExpenses = findViewById(R.id.rvTravelExpenses);
    }

    @SuppressLint("NotifyDataSetChanged")
    protected void onResume() {
        super.onResume();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            travel = new Travel();
            if (extras.getInt( "travel_id") > 0) {
                travel.setId(extras.getInt("travel_id"));
                travel = Database.mTravelDao.fetchTravelById(travel.getId());
                opInsert = false;
            }
        }

        if (travel != null) {
            etDescription.setText(travel.getDescription());
            etDeparture_date.setText(Utils.dateToString(travel.getDeparture_date()));
            etReturn_date.setText(Utils.dateToString(travel.getReturn_date()));
            etNote.setText(travel.getNote());
            nrStatus=travel.getStatus();
            tvStatus.setText(getResources().getStringArray(R.array.travel_status_array)[nrStatus]);
        }

        etDeparture_date.addTextChangedListener(new DateInputMask(etDeparture_date));
        etReturn_date.addTextChangedListener(new DateInputMask(etReturn_date));

        if (travel != null) {

            adapterItinerary = new TravelRouteFragment.HomeTravelItineraryListAdapter(Database.mItineraryDao.fetchAllItineraryByTravel(travel.getId() ), getApplicationContext(), 0,0, false, travel.getId());
            rvItinerary.setAdapter(adapterItinerary);
            rvItinerary.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            adapterItinerary.notifyDataSetChanged();

            adapterItineraryHasTransport = new ItineraryHasTransportListAdapter(Database.mItineraryHasTransportDao.fetchAllItineraryHasTransportByTravel(travel.getId() ), getApplicationContext(),"Travel", 1, travel.id);
            rvItineraryHasTransport.setAdapter(adapterItineraryHasTransport);
            rvItineraryHasTransport.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            adapterVehicleTravel = new TravelVehicleListAdapter(Database.mVehicleHasTravelDao.fetchAllVehicleHasTravelByTravel(travel.getId()), getApplicationContext(),"Travel",1, travel.getId());
            rvVehicleTravel.setAdapter(adapterVehicleTravel);
            rvVehicleTravel.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            adapterVehicleTravel.notifyDataSetChanged();

            adapterTourTravel = new TravelTourListAdapter(Database.mTourDao.fetchAllTourByTravel(travel.getId()), getApplicationContext(),"Travel",1, travel.getId());
            rvTourTravel.setAdapter(adapterTourTravel);
            rvTourTravel.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            adapterTourTravel.notifyDataSetChanged();

            adapterAchievementTravel = new TravelAchievementListAdapter(Database.mAchievementDao.fetchAllAchievementByTravel(travel.getId()), getApplicationContext(),"Travel",1, travel.getId());
            rvAchievementTravel.setAdapter(adapterAchievementTravel);
            rvAchievementTravel.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            adapterAchievementTravel.notifyDataSetChanged();

            adapterTravelExpenses = new TravelExpensesListAdapter(travel.getId(), Database.mTravelExpensesDao.fetchAllTravelExpensesByTravel(travel.getId()), getApplicationContext(),1,0);
            rvTravelExpenses.setAdapter(adapterTravelExpenses);
            rvTravelExpenses.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            adapterTravelExpenses.notifyDataSetChanged();
        }
    }

    public void addListenerOnButtonSave() {
        Button btSaveTravel = findViewById(R.id.btSaveTravel);

        btSaveTravel.setOnClickListener(v -> {
            boolean isSave = false;

            if (!validateData()) {
                Toast.makeText(getApplicationContext(), R.string.Error_Data_Validation, Toast.LENGTH_LONG).show();
            } else {
                final Travel t1 = new Travel();

                t1.setDescription(etDescription.getText().toString());
                t1.setDeparture_date(Utils.stringToDate(etDeparture_date.getText().toString()));
                t1.setReturn_date(Utils.stringToDate(etReturn_date.getText().toString()));
                t1.setNote(etNote.getText().toString());
                t1.setStatus(nrStatus);

                if (!opInsert) {
                    try {
                        t1.setId(travel.getId());
                        isSave = Database.mTravelDao.updateTravel(t1);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), R.string.Error_Changing_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        isSave = Database.mTravelDao.addTravel(t1);
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
        boolean isValid = false;

        try {
            if (!etDescription.getText().toString().trim().isEmpty()
                && !etDeparture_date.getText().toString().trim().isEmpty()
                && !etReturn_date.getText().toString().trim().isEmpty()
                //&& !etNote.getText().toString().trim().isEmpty()
                //&& !tvStatus.getText().toString().trim().isEmpty()
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