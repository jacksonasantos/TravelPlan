package com.jacksonasantos.travelplan.ui.travel;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Achievement;
import com.jacksonasantos.travelplan.dao.Driver;
import com.jacksonasantos.travelplan.dao.Travel;
import com.jacksonasantos.travelplan.dao.Vehicle;
import com.jacksonasantos.travelplan.dao.VehicleHasTravel;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.DateInputMask;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.List;

public class TravelActivity extends AppCompatActivity {
    private EditText etDescription;
    private EditText etDeparture_date;
    private EditText etReturn_date;
    private EditText etNote;
    private int nrStatus;
    private TextView tvStatus;

    private ConstraintLayout clVehicleTravel;
    private AutoCompleteTextView spinVehicle;
    private int nrSpinVehicle;
    private AutoCompleteTextView spinDriver;
    private int nrSpinDriver;
    private ImageButton btnAdd;
    private RecyclerView rvVehicleTravel;

    private Spinner spinAchievement;
    private int nrSpinAchievement;
    private ImageButton btnAddAchievement;
    private RecyclerView rvAchievementTravel;

    private RecyclerView rvTravelExpenses;

    private TravelVehicleListAdapter adapterVehicleTravel;
    private TravelAchievementListAdapter adapterAchievementTravel;
    private TravelExpensesListAdapter adapterTravelExpenses;

    private boolean opInsert = true;
    private Travel travel;

    @SuppressLint("WrongViewCast")

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

        clVehicleTravel = findViewById(R.id.clVehicleTravel);
        spinVehicle = findViewById(R.id.spinVehicle);
        spinDriver = findViewById(R.id.spinDriver);
        rvVehicleTravel = findViewById(R.id.rvVehicleTravel);
        btnAdd = findViewById(R.id.btnAdd);

        spinAchievement = findViewById(R.id.spinAchievement);
        rvAchievementTravel = findViewById(R.id.rvAchievementTravel);
        btnAddAchievement = findViewById(R.id.btnAddAchievement);

        rvTravelExpenses = findViewById(R.id.rvTravelExpenses);
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "NotifyDataSetChanged"})
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

        final List<Vehicle> vehicles = Database.mVehicleDao.fetchArrayVehicles();
        ArrayAdapter<Vehicle> adapterVehicle = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vehicles);
        adapterVehicle.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinVehicle.setAdapter(adapterVehicle);
        final Vehicle[] v1 = {new Vehicle()};
        spinVehicle.setOnItemClickListener((parent, view, position, id) -> {
            v1[0] = (Vehicle) parent.getItemAtPosition(position);
            nrSpinVehicle = v1[0].getId();
        });
        adapterVehicle.notifyDataSetChanged();

        final List<Driver> drivers = Database.mDriverDao.fetchArrayDriver();
        ArrayAdapter<Driver> adapterDriver = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, drivers);
        adapterDriver.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinDriver.setAdapter(adapterDriver);
        final Driver[] d1 = {new Driver()};
        spinDriver.setOnItemClickListener((parent, view, position, id) -> {
            d1[0] = (Driver) parent.getItemAtPosition(position);
            nrSpinDriver = d1[0].getId();
        });
        adapterDriver.notifyDataSetChanged();

        Cursor cAchievement = Database.mAchievementDao.fetchArrayAchievement();
        String[] adapterColsA = new String[]{"text1"};
        int[] adapterRowViewsA = new int[]{android.R.id.text1};
        SimpleCursorAdapter cursorAdapterA = new SimpleCursorAdapter( this,
                android.R.layout.simple_spinner_item, cAchievement, adapterColsA, adapterRowViewsA, 0);
        cursorAdapterA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinAchievement.setAdapter(cursorAdapterA);
        Utils.setSpinnerToValue(spinAchievement, nrSpinAchievement); // Selected Value of Spinner with value marked maps
        spinAchievement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                nrSpinAchievement = Math.toIntExact(spinAchievement.getSelectedItemId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        if (travel != null) {
            addListenerOnButtonAdd();
            clVehicleTravel.setVisibility(View.VISIBLE);
            adapterVehicleTravel = new TravelVehicleListAdapter(Database.mVehicleHasTravelDao.fetchAllVehicleHasTravelByTravel(travel.getId()), getApplicationContext(),"Travel",0);
            rvVehicleTravel.setAdapter(adapterVehicleTravel);
            rvVehicleTravel.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            adapterVehicleTravel.notifyDataSetChanged();

            adapterAchievementTravel = new TravelAchievementListAdapter(Database.mAchievementDao.fetchAllAchievementByTravel(travel.getId()), getApplicationContext(),"Travel",0);
            rvAchievementTravel.setAdapter(adapterAchievementTravel);
            rvAchievementTravel.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            adapterAchievementTravel.notifyDataSetChanged();

            adapterTravelExpenses = new TravelExpensesListAdapter(travel.getId(), Database.mTravelExpensesDao.fetchAllTravelExpensesByTravel(travel.getId()), getApplicationContext(),1,0);
            rvTravelExpenses.setAdapter(adapterTravelExpenses);
            rvTravelExpenses.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            adapterTravelExpenses.notifyDataSetChanged();
        } else {
            clVehicleTravel.setVisibility(View.INVISIBLE);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addListenerOnButtonAdd() {
        btnAdd.setOnClickListener(v -> {
            boolean isSave = false;

            if (nrSpinVehicle==0) {
                Toast.makeText(getApplicationContext(), R.string.Vehicle_not_Selected, Toast.LENGTH_LONG).show();
            } else {
                final VehicleHasTravel vt1 = new VehicleHasTravel();

                vt1.setTravel_id(travel.getId());
                vt1.setVehicle_id(nrSpinVehicle);
                vt1.setDriver_id(nrSpinDriver);
                try {
                    isSave = Database.mVehicleHasTravelDao.addVehicleHasTravel(vt1);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), R.string.Error_Including_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                }

                setResult(isSave ? 1 : 0);
                if (!isSave) {
                    Toast.makeText(getApplicationContext(), R.string.Error_Saving_Data, Toast.LENGTH_LONG).show();
                } else {
                    adapterVehicleTravel = new TravelVehicleListAdapter(Database.mVehicleHasTravelDao.fetchAllVehicleHasTravelByTravel(travel.getId()), getApplicationContext(),"travel",0);
                    rvVehicleTravel.setAdapter(adapterVehicleTravel);
                    rvVehicleTravel.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapterVehicleTravel.notifyDataSetChanged();
                }
            }
        });
        btnAddAchievement.setOnClickListener(v -> {
            boolean isSave = false;

            if (nrSpinAchievement==0) {
                Toast.makeText(getApplicationContext(), R.string.Achievement_not_Selected, Toast.LENGTH_LONG).show();
            } else {
                Achievement ac1 = Database.mAchievementDao.fetchAchievementById(nrSpinAchievement);

                ac1.setTravel_id(travel.getId());
                ac1.setItinerary_id(ac1.getItinerary_id()==0?null:ac1.getItinerary_id());
                try {
                    isSave = Database.mAchievementDao.updateAchievement(ac1);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), R.string.Error_Including_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                }

                setResult(isSave ? 1 : 0);
                if (!isSave) {
                    Toast.makeText(getApplicationContext(), R.string.Error_Saving_Data, Toast.LENGTH_LONG).show();
                } else {
                    adapterAchievementTravel = new TravelAchievementListAdapter(Database.mAchievementDao.fetchAllAchievementByTravel(travel.getId()), getApplicationContext(),"travel",0);
                    rvAchievementTravel.setAdapter(adapterAchievementTravel);
                    rvAchievementTravel.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapterAchievementTravel.notifyDataSetChanged();
                }
            }
        });
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