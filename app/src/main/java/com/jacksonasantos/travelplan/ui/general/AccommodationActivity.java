package com.jacksonasantos.travelplan.ui.general;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Accommodation;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.travel.MaintenanceItineraryActivity;
import com.jacksonasantos.travelplan.ui.utility.Utils;

public class AccommodationActivity extends AppCompatActivity {
    private EditText etAccommodation_Name;
    private EditText etAccommodation_Address;
    private EditText etAccommodation_City;
    private EditText etAccommodation_State;
    private EditText etAccommodation_Country;
    private EditText etAccommodation_Latlng_Accommodation;
    private ImageButton btAccommodation_Latlng_Accommodation;
    private EditText etAccommodation_Contact_Name;
    private EditText etAccommodation_Phone;
    private EditText etAccommodation_Email;
    private EditText etAccommodation_Site;
    private Spinner spinAccommodation_Accommodation_Type;
    private int nrSpinAccommodation_Accommodation_Type;

    private boolean opInsert = true;
    private boolean opResult = false;
    private Accommodation accommodation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Database mDb = new Database(getApplicationContext());
        mDb.open();

        setTitle(R.string.accommodation);
        setContentView(R.layout.activity_accommodation);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            accommodation = new Accommodation();
            if (extras.getInt( "accommodation_id") > 0) {
                accommodation.setId(extras.getInt("accommodation_id"));
                accommodation = Database.mAccommodationDao.fetchAccommodationById(accommodation.getId());
                opInsert = false;
            }
            if (extras.getBoolean("op_result")) {
                opResult = true;
            }
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        addListenerOnButtonSave();

        etAccommodation_Name = findViewById(R.id.etAccommodation_Name);
        etAccommodation_Address = findViewById(R.id.etAccommodation_Address);
        etAccommodation_City = findViewById(R.id.etAccommodation_City);
        etAccommodation_State = findViewById(R.id.etAccommodation_State);
        etAccommodation_Country = findViewById(R.id.etAccommodation_Country);
        etAccommodation_Latlng_Accommodation = findViewById(R.id.etAccommodation_Latlng_Accommodation);
        btAccommodation_Latlng_Accommodation = findViewById(R.id.btAccommodation_Latlng_Accommodation);
        etAccommodation_Contact_Name = findViewById(R.id.etAccommodation_Contact_Name);
        etAccommodation_Phone = findViewById(R.id.etAccommodation_Phone);
        etAccommodation_Email = findViewById(R.id.etAccommodation_Email);
        etAccommodation_Site = findViewById(R.id.etAccommodation_Site);
        spinAccommodation_Accommodation_Type = findViewById(R.id.spinAccommodation_Accommodation_Type);

        btAccommodation_Latlng_Accommodation.setOnClickListener(view -> {
            Intent intent = new Intent (getBaseContext(), MaintenanceItineraryActivity.class);
            if (!etAccommodation_Latlng_Accommodation.getText().toString().equals("")) {
                intent.putExtra("local_search", etAccommodation_Latlng_Accommodation.getText().toString());
            } else {
                intent.putExtra("local_search", etAccommodation_Name.getText().toString() + "," +
                                                            etAccommodation_Address.getText().toString()+ "," +
                                                            etAccommodation_City.getText().toString()+ "," +
                                                            etAccommodation_State.getText().toString()+ "," +
                                                            etAccommodation_Country.getText().toString());
            }
            myActivityResultLauncher.launch(intent);
        });

        Utils.createSpinnerResources(R.array.accommodation_type_array, spinAccommodation_Accommodation_Type, this);
        nrSpinAccommodation_Accommodation_Type = 0;
        spinAccommodation_Accommodation_Type.setSelection(nrSpinAccommodation_Accommodation_Type);
        spinAccommodation_Accommodation_Type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long idx) {
                nrSpinAccommodation_Accommodation_Type = position;  }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                nrSpinAccommodation_Accommodation_Type =0;
            }
        });

        if (accommodation != null) {
            etAccommodation_Name.setText(accommodation.getName());
            etAccommodation_Address.setText(accommodation.getAddress());
            etAccommodation_City.setText(accommodation.getCity());
            etAccommodation_State.setText(accommodation.getState());
            etAccommodation_Country.setText(accommodation.getCountry());
            etAccommodation_Latlng_Accommodation.setText(accommodation.getLatlng_accommodation());
            etAccommodation_Contact_Name.setText(accommodation.getContact_name());
            etAccommodation_Phone.setText(accommodation.getPhone());
            etAccommodation_Email.setText(accommodation.getEmail());
            etAccommodation_Site.setText(accommodation.getSite());
            nrSpinAccommodation_Accommodation_Type = accommodation.getAccommodation_type();
            //spinAccommodation_Accommodation_Type.setText(getResources().getStringArray(R.array.accommodation_type_array)[nrSpinAccommodation_Accommodation_Type], false);
            spinAccommodation_Accommodation_Type.setSelection(nrSpinAccommodation_Accommodation_Type);
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
                            etAccommodation_Latlng_Accommodation.setText(data.getStringExtra("resulted_value"));
                            etAccommodation_Address.setText(data.getStringExtra("resulted_address"));
                            etAccommodation_City.setText(data.getStringExtra("resulted_city"));
                            etAccommodation_State.setText(data.getStringExtra("resulted_state"));
                            etAccommodation_Country.setText(data.getStringExtra("resulted_country"));
                        }
                    }
                }
            });

    public void addListenerOnButtonSave() {
        Button btSaveAccommodation = findViewById(R.id.btSaveAccommodation);

        btSaveAccommodation.setOnClickListener(v -> {
            boolean isSave = false;

            if (!validateData()) {
                Toast.makeText(getApplicationContext(), R.string.Error_Data_Validation, Toast.LENGTH_LONG).show();
            } else {
                final Accommodation a1 = new Accommodation();

                a1.setName(etAccommodation_Name.getText().toString());
                a1.setAddress(etAccommodation_Address.getText().toString());
                a1.setCity(etAccommodation_City.getText().toString());
                a1.setState(etAccommodation_State.getText().toString());
                a1.setCountry(etAccommodation_Country.getText().toString());
                a1.setLatlng_accommodation(etAccommodation_Latlng_Accommodation.getText().toString());
                a1.setContact_name(etAccommodation_Contact_Name.getText().toString());
                a1.setPhone(etAccommodation_Phone.getText().toString());
                a1.setEmail(etAccommodation_Email.getText().toString());
                a1.setSite(etAccommodation_Site.getText().toString());
                a1.setAccommodation_type(nrSpinAccommodation_Accommodation_Type);

                if (!opInsert) {
                    try {
                        a1.setId(accommodation.getId());
                        isSave = Database.mAccommodationDao.updateAccommodation(a1);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), R.string.Error_Changing_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        isSave = Database.mAccommodationDao.addAccommodation(a1);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), R.string.Error_Including_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                setResult(isSave ? 1 : 0);
                if (isSave) {
                    if (opResult) {
                        Intent i = new Intent();
                        i.putExtra("resulted_value", a1.getName());
                        setResult(123, i);
                    }
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
            if (etAccommodation_Name.getText().toString().trim().isEmpty() ||
                etAccommodation_Address.getText().toString().trim().isEmpty() ||
                etAccommodation_City.getText().toString().trim().isEmpty() ||
                etAccommodation_State.getText().toString().trim().isEmpty() ||
                etAccommodation_Country.getText().toString().trim().isEmpty() ||
                //etAccommodation_Latlng_Accommodation.getText().toString().trim().isEmpty() ||
                //etAccommodation_Contact_Name.getText().toString().trim().isEmpty() ||
                etAccommodation_Phone.getText().toString().trim().isEmpty() ||
                //etAccommodation_Email.getText().toString().trim().isEmpty() ||
                //etAccommodation_Site.getText().toString().trim().isEmpty() ||
                String.valueOf(nrSpinAccommodation_Accommodation_Type).trim().isEmpty()
            ){
                isValid = false;
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.Data_Validator_Error +" - " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return isValid;
    }
}