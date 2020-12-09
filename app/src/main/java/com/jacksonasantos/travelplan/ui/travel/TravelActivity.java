package com.jacksonasantos.travelplan.ui.travel;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Database;
import com.jacksonasantos.travelplan.dao.Travel;
import com.jacksonasantos.travelplan.ui.utility.DateInputMask;
import com.jacksonasantos.travelplan.ui.utility.Utils;

public class TravelActivity extends AppCompatActivity {
    private EditText etDescription;
    private EditText etDeparture_date;
    private EditText etReturn_date;
    private EditText etNote;
    private int nrStatus;

    private boolean opInsert = true;
    private Travel travel;

    @SuppressLint("WrongViewCast")
    @RequiresApi(api = Build.VERSION_CODES.N)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Database mDb = new Database(getApplicationContext());
        mDb.open();

        setTitle(R.string.Travel);
        setContentView(R.layout.activity_travel);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            travel = new Travel();
            if (extras.getInt( "travel_id") > 0) {
                travel.setId(extras.getInt("travel_id"));
                travel = Database.mTravelDao.fetchTravelById(travel.getId());
                opInsert = false;
            }
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        addListenerOnButtonSave();

        etDescription = findViewById(R.id.etDescription);
        etDeparture_date = findViewById(R.id.etDeparture_date);
        etReturn_date = findViewById(R.id.etReturn_date);
        etNote = findViewById(R.id.etNote);
        TextView tvStatus = findViewById(R.id.tvStatus);

        etDeparture_date.addTextChangedListener(new DateInputMask(etDeparture_date));
        etReturn_date.addTextChangedListener(new DateInputMask(etReturn_date));

        if (travel != null) {
            etDescription.setText(travel.getDescription());
            etDeparture_date.setText(Utils.dateToString(travel.getDeparture_date()));
            etReturn_date.setText(Utils.dateToString(travel.getReturn_date()));
            etNote.setText(travel.getNote());
            nrStatus=travel.getStatus();
            tvStatus.setText(getResources().getStringArray(R.array.travel_status_array)[nrStatus]);
        }
    }

    public void addListenerOnButtonSave() {
        Button btSaveTravel = findViewById(R.id.btSaveTravel);

        btSaveTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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