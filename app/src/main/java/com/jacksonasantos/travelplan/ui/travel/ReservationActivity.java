package com.jacksonasantos.travelplan.ui.travel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Accommodation;
import com.jacksonasantos.travelplan.dao.Reservation;
import com.jacksonasantos.travelplan.dao.Travel;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.DateInputMask;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.List;

public class ReservationActivity extends AppCompatActivity {
    
    private AutoCompleteTextView spinTravel;
    private int nrSpinTravel;
    private AutoCompleteTextView spinAccommodation;
    private int nrSpinAccommodation;
    private ImageButton btAddAccommodation;
    private EditText etVoucher_Number;
    private EditText etCheckin_Date;
    private EditText etCheckout_Date;
    private EditText etApto_Type;
    private EditText etDaily_Rate;
    private EditText etOther_Rate;
    private EditText etReservation_Amount;
    private EditText etAmount_Paid;
    private EditText etNote;
    private TextView tvStatus_Reservation;
    private int nrStatus_Reservation;

    private boolean opInsert = true;
    private Reservation reservation;

    @SuppressLint("WrongViewCast")
    @RequiresApi(api = Build.VERSION_CODES.N)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Database mDb = new Database(getApplicationContext());
        mDb.open();

        setTitle(R.string.Reservation);
        setContentView(R.layout.activity_reservation);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            reservation = new Reservation();
            if (extras.getInt( "reservation_id") > 0) {
                reservation.setId(extras.getInt("reservation_id"));
                reservation = Database.mReservationDao.fetchReservationById(reservation.getId());
                opInsert = false;
            }
            if (extras.getInt( "travel_id") > 0) {
                reservation.setTravel_id(extras.getInt("travel_id"));
            }
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        addListenerOnButtonSave();
        addListenerOnButtonAddAccommodation();

        spinTravel = findViewById(R.id.spinTravel);
        spinAccommodation = findViewById(R.id.spinAccommodation);
        etVoucher_Number = findViewById(R.id.etVoucher_Number);
        etCheckin_Date = findViewById(R.id.etCheckin_Date);
        etCheckout_Date = findViewById(R.id.etCheckout_Date);
        etApto_Type = findViewById(R.id.etApto_Type);
        etDaily_Rate = findViewById(R.id.etDaily_Rate);
        etOther_Rate = findViewById(R.id.etOther_Rate);
        etReservation_Amount = findViewById(R.id.etReservation_Amount);
        etAmount_Paid = findViewById(R.id.etAmount_Paid);
        etNote = findViewById(R.id.etNote);
        tvStatus_Reservation = findViewById(R.id.tvStatus_Reservation);
        nrStatus_Reservation = 0;

        final List<Travel> travels =  Database.mTravelDao.fetchArrayTravel();
        ArrayAdapter<Travel> adapterT = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, travels);
        adapterT.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinTravel.setAdapter(adapterT);
        final Travel[] travel = {new Travel()};
        spinTravel.setOnItemClickListener((parent, view, position, id) -> {
            travel[0] = (Travel) parent.getItemAtPosition(position);
            nrSpinTravel = travel[0].getId();
        });
        adapterT.notifyDataSetChanged();

        final List<Accommodation> accommodations =  Database.mAccommodationDao.fetchArrayAccommodation();
        ArrayAdapter<Accommodation> adapterA = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, accommodations);
        adapterA.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinAccommodation.setAdapter(adapterA);
        final Accommodation[] accommodation = {new Accommodation()};
        spinAccommodation.setOnItemClickListener((parent, view, position, id) -> {
            accommodation[0] = (Accommodation) parent.getItemAtPosition(position);
            nrSpinAccommodation = accommodation[0].getId();
        });
        adapterA.notifyDataSetChanged();

        etCheckin_Date.addTextChangedListener(new DateInputMask(etCheckin_Date));
        etCheckout_Date.addTextChangedListener(new DateInputMask(etCheckout_Date));

        if (reservation != null) {
            nrSpinTravel=reservation.getTravel_id();
            Travel t1 = Database.mTravelDao.fetchTravelById(nrSpinTravel);
            if (t1.getDescription()!=null) {
                for (int x = 0; x <= spinTravel.getAdapter().getCount(); x++) {
                    if (spinTravel.getAdapter().getItem(x).toString().equals(t1.getDescription())) {
                        spinTravel.setText(spinTravel.getAdapter().getItem(x).toString(),false);
                        break;
                    }
                }
            }
            if (reservation.getAccommodation_id()!=null) {
                nrSpinAccommodation = reservation.getAccommodation_id();
                Accommodation a1 = Database.mAccommodationDao.fetchAccommodationById(nrSpinAccommodation);
                if (a1.getName() != null) {
                    for (int x = 0; x < spinAccommodation.getAdapter().getCount(); x++) {
                        if (spinAccommodation.getAdapter().getItem(x).toString().equals(a1.getName())) {
                            spinAccommodation.setText(spinAccommodation.getAdapter().getItem(x).toString(), false);
                            break;
                        }
                    }
                }
            }
            etVoucher_Number.setText(reservation.getVoucher_number());
            etCheckin_Date.setText(Utils.dateToString(reservation.getCheckin_date()));
            etCheckout_Date.setText(Utils.dateToString(reservation.getCheckout_date()));
            etApto_Type.setText(reservation.getApto_type());
            etDaily_Rate.setText(String.valueOf(reservation.getDaily_rate()));
            etOther_Rate.setText(String.valueOf(reservation.getOther_rate()));
            etReservation_Amount.setText(String.valueOf(reservation.getReservation_amount()));
            etAmount_Paid.setText(String.valueOf(reservation.getAmount_paid()));
            etNote.setText(reservation.getNote());
            tvStatus_Reservation.setText(getResources().getStringArray(R.array.reservation_status_array)[nrStatus_Reservation]);
        }
    }

    public void addListenerOnButtonAddAccommodation() {
        btAddAccommodation = findViewById(R.id.btAddAccommodation);

        btAddAccommodation.setOnClickListener(v -> {
            Intent intent = new Intent (v.getContext(), AccommodationActivity.class);
            getApplicationContext().startActivity(intent);
        });
    }

    public void addListenerOnButtonSave() {
        Button btSaveReservation = findViewById(R.id.btSaveReservation);

        btSaveReservation.setOnClickListener(v -> {
            boolean isSave = false;

            if (!validateData()) {
                Toast.makeText(getApplicationContext(), R.string.Error_Data_Validation, Toast.LENGTH_LONG).show();
            } else {
                final Reservation r1 = new Reservation();

                r1.setTravel_id(nrSpinTravel);
                r1.setAccommodation_id(nrSpinAccommodation);
                r1.setVoucher_number(etVoucher_Number.getText().toString());
                r1.setCheckin_date(Utils.stringToDate(etCheckin_Date.getText().toString()));
                r1.setCheckout_date(Utils.stringToDate(etCheckout_Date.getText().toString()));
                r1.setApto_type(etApto_Type.getText().toString());
                r1.setDaily_rate(Double.parseDouble(etDaily_Rate.getText().toString()));
                r1.setOther_rate(Double.parseDouble(etOther_Rate.getText().toString()));
                r1.setReservation_amount(Double.parseDouble(etReservation_Amount.getText().toString()));
                r1.setAmount_paid(Double.parseDouble(etAmount_Paid.getText().toString()));
                r1.setNote(etNote.getText().toString());
                r1.setStatus_reservation(nrStatus_Reservation);

                if (!opInsert) {
                    try {
                        r1.setId(reservation.getId());
                        isSave = Database.mReservationDao.updateReservation(r1);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), R.string.Error_Changing_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        isSave = Database.mReservationDao.addReservation(r1);
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
            if (String.valueOf(nrSpinTravel).trim().isEmpty() ||
                String.valueOf(nrSpinAccommodation).trim().isEmpty() ||
                etVoucher_Number.getText().toString().trim().isEmpty() ||
                etCheckin_Date.getText().toString().trim().isEmpty() ||
                etCheckout_Date.getText().toString().trim().isEmpty() ||
                etApto_Type.getText().toString().trim().isEmpty() ||
                etDaily_Rate.getText().toString().trim().isEmpty() ||
                etOther_Rate.getText().toString().trim().isEmpty() ||
                etReservation_Amount.getText().toString().trim().isEmpty() //||
                //etAmount_Paid.getText().toString().trim().isEmpty() ||
                //etNote.getText().toString().trim().isEmpty() ||
                //etStatus_Reservation.getText().toString().trim().isEmpty()
            ){
                isValid = false;
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.Data_Validator_Error +" - " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return isValid;
    }
}