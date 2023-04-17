package com.jacksonasantos.travelplan.ui.travel;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Tour;
import com.jacksonasantos.travelplan.dao.Travel;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.DateInputMask;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.ArrayList;
import java.util.List;

public class TourActivity extends AppCompatActivity implements TourTypeListAdapter.ItemClickListener {
    
    private boolean opInsert = true;

    private Tour tour;

    private TextView tvTravel;
    private TextView tvItinerary;
    private TextView tvMarker ;
    private RecyclerView rvTourType ;
    private int nrTourType;
    private EditText etLocalTour ;
    private EditText etDate ;
    private EditText etValueAdult;
    private EditText etValueChild ;
    private Spinner spCurrencyType;
    private int nrSpCurrencyType;
    private EditText etOpeningHours;
    private EditText etVisitationTime;
    private EditText etNote;

    public TourTypeListAdapter adapterTourType;

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

        addListenerOnButtonSave();

        tvTravel = findViewById(R.id.tvTravel);
        tvItinerary = findViewById(R.id.tvItinerary);
        tvMarker = findViewById(R.id.tvMarker);
        rvTourType = findViewById(R.id.rvTourType);
        // TODO - na edição marcar o botão do tipo para permitir a mudança
        // TODO - na mudança desmarcar tipo selecionando anteriormente
        etLocalTour = findViewById(R.id.etLocalTour);
        etDate = findViewById(R.id.etDate);
        etValueAdult = findViewById(R.id.etValueAdult);
        etValueChild = findViewById(R.id.etValueChild);
        spCurrencyType = findViewById(R.id.spCurrencyType);
        etOpeningHours = findViewById(R.id.etOpeningHours);
        etVisitationTime = findViewById(R.id.etVisitationTime);
        etNote = findViewById(R.id.etNote);

        tvItinerary.setVisibility(View.INVISIBLE);
        tvMarker.setVisibility(View.INVISIBLE);
        etDate.addTextChangedListener(new DateInputMask(etDate));

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

        if (tour != null) {
            Travel t1 = Database.mTravelDao.fetchTravelById(tour.getTravel_id());
            tvTravel.setText(t1.getDescription());
            tvItinerary.setVisibility(View.INVISIBLE);//.setText("");
            tvMarker.setVisibility(View.INVISIBLE);//.setText("");
            nrTourType = tour.getTour_type();
            etLocalTour.setText(tour.getLocal_tour());
            etDate.setText(Utils.dateToString(tour.getTour_date()));
            etValueAdult.setText(String.valueOf(tour.getValue_adult())); // TODO - verificar a gravação de valores nulos
            etValueChild.setText(String.valueOf(tour.getValue_child()));
            nrSpCurrencyType = tour.getCurrency_type();
            spCurrencyType.setSelection(nrSpCurrencyType);
            etOpeningHours.setText(tour.getOpening_hours());
            etVisitationTime.setText(tour.getVisitation_time());
            etNote.setText(tour.getNote());
        }

    }

    @Override
    public void onItemClick(int position) {
        if (nrTourType == position) nrTourType = -1;
        else nrTourType = position;
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
                t1.setMarker_id(null);
                t1.setLocal_tour(etLocalTour.getText().toString());
                t1.setTour_type(nrTourType);
                t1.setCurrency_type(nrSpCurrencyType);
                t1.setValue_adult(Double.parseDouble(etValueAdult.getText().toString()));
                t1.setValue_child(Double.parseDouble(etValueChild.getText().toString()));
                t1.setTour_date(Utils.stringToDate(etDate.getText().toString()));
                t1.setOpening_hours(etOpeningHours.getText().toString());
                t1.setDistance(0);
                t1.setVisitation_time(etVisitationTime.getText().toString());
                t1.setNote(etNote.getText().toString());

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
            if (nrTourType == -1 ||
                etLocalTour.getText().toString().trim().isEmpty() ||
                etDate.getText().toString().trim().isEmpty()
                //etValueAdult.getText().toString().trim().isEmpty() ||
                //etValueChild.getText().toString().trim().isEmpty() ||
                //nrspCurrencyType == -1 ||
                //etOpeningHours.getText().toString().trim().isEmpty() ||
                //etVisitationTime.getText().toString().trim().isEmpty() ||
                //etNote.getText().toString().trim().isEmpty()
            ){
                isValid = false;
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.Data_Validator_Error +" - " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return isValid;
    }
}