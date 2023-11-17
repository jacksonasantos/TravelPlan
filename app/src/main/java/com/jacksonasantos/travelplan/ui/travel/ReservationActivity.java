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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Accommodation;
import com.jacksonasantos.travelplan.dao.Reservation;
import com.jacksonasantos.travelplan.dao.Travel;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.general.AccommodationActivity;
import com.jacksonasantos.travelplan.ui.utility.DateInputMask;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.List;

public class ReservationActivity extends AppCompatActivity {

    private ImageButton btFileDownload;
    private Spinner spinTravel;
    private Integer nrSpinTravel;
    private Spinner spinAccommodation;
    private Integer nrSpinAccommodation;
    private TextView tvAccommodation_Address;
    private TextView tvAccommodation_CityStateCountry;
    private TextView tvAccommodation_LatLng;
    private TextView tvAccommodation_Contact;
    private TextView tvAccommodation_Phone;
    private TextView tvAccommodation_Email;
    private TextView tvAccommodation_Site;
    private ImageButton btAddAccommodation;
    private EditText etVoucher_Number;
    private EditText etCheckin_Date;
    private EditText etCheckout_Date;
    private TextView tvRates;
    private EditText etApt_Type;
    private EditText etDaily_Rate;
    private EditText etOther_Rate;
    private EditText etReservation_Amount;
    private EditText etAmount_Paid;
    private EditText etNote;
    private TextView tvStatus_Reservation;
    private int nrStatus_Reservation;
    private Button btSaveReservation;

    private boolean opInsert = true;
    private Reservation reservation;

    protected final ArrayAdapter<Accommodation> adapterA = null;
    private List<Accommodation> accommodations;

    @SuppressLint({"WrongViewCast", "SetTextI18n"})

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Database mDb = new Database(getApplicationContext());
        mDb.open();

        setTitle(R.string.Reservation);
        setContentView(R.layout.activity_reservation);

        Bundle extras = getIntent().getExtras();
        reservation = new Reservation();
        if (extras != null) {
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

        btFileDownload = findViewById(R.id.btFileDownload);
        spinTravel = findViewById(R.id.spinTravel);
        spinAccommodation = findViewById(R.id.spinAccommodation);
        btAddAccommodation = findViewById(R.id.btAddAccommodation);
        tvAccommodation_Address = findViewById(R.id.tvAccommodation_Address);
        tvAccommodation_CityStateCountry = findViewById(R.id.tvAccommodation_CityStateCountry);
        tvAccommodation_LatLng = findViewById(R.id.tvAccommodation_LatLng);
        tvAccommodation_Contact = findViewById(R.id.tvAccommodation_Contact);
        tvAccommodation_Phone = findViewById(R.id.tvAccommodation_Phone);
        tvAccommodation_Email = findViewById(R.id.tvAccommodation_Email);
        tvAccommodation_Site = findViewById(R.id.tvAccommodation_Site);
        etVoucher_Number = findViewById(R.id.etVoucher_Number);
        etCheckin_Date = findViewById(R.id.etCheckin_Date);
        etCheckout_Date = findViewById(R.id.etCheckout_Date);
        tvRates = findViewById(R.id.tvRates);
        etApt_Type = findViewById(R.id.etApt_Type);
        etDaily_Rate = findViewById(R.id.etDaily_Rate);
        etOther_Rate = findViewById(R.id.etOther_Rate);
        etReservation_Amount = findViewById(R.id.etReservation_Amount);
        etAmount_Paid = findViewById(R.id.etAmount_Paid);
        etNote = findViewById(R.id.etNote);
        tvStatus_Reservation = findViewById(R.id.tvStatus_Reservation);
        nrStatus_Reservation = 0;
        btSaveReservation = findViewById(R.id.btSaveReservation);

        addListenerOnButtonSave();
        addListenerOnButtonAddAccommodation();
        addListenerOnButtonFileDownload();

        final List<Travel> travels =  Database.mTravelDao.fetchArrayTravel();
        travels.add(0, new Travel());

        ArrayAdapter<Travel> adapterT = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, travels);
        adapterT.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinTravel.setAdapter(adapterT);
        if (nrSpinTravel != null && nrSpinTravel > 0) {
            Travel trip1 = Database.mTravelDao.fetchTravelById(nrSpinTravel);
            for (int x = 1; x <= spinTravel.getAdapter().getCount(); x++) {
                if (spinTravel.getAdapter().getItem(x).toString().equals(trip1.getDescription())) {
                    spinTravel.setSelection(x);
                    nrSpinTravel = trip1.getId();
                    break;
                }
            }
        }
        View.OnFocusChangeListener listenerCheckDate = (v, hasFocus) -> {
            if (!hasFocus) {
                reservation.setCheckin_date(Utils.stringToDate(etCheckin_Date.getText().toString()));
                reservation.setCheckout_date(Utils.stringToDate(etCheckout_Date.getText().toString()));
                tvRates.setText(String.valueOf(reservation.getRates()));
            }
        };
        View.OnFocusChangeListener listenerReservationAmount = (v, hasFocus) -> {
            if (!hasFocus) {
                etReservation_Amount.setText(String.valueOf(Integer.parseInt(tvRates.getText().toString())*Double.parseDouble(etDaily_Rate.getText().toString())+Double.parseDouble(etOther_Rate.getText().toString())));
            }
        };
        etCheckin_Date.setOnFocusChangeListener(listenerCheckDate);
        etCheckout_Date.setOnFocusChangeListener(listenerCheckDate);
        etDaily_Rate.setOnFocusChangeListener(listenerReservationAmount);
        etOther_Rate.setOnFocusChangeListener(listenerReservationAmount);
        final Travel[] t1 = {new Travel()};
        spinTravel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long idx) {
                t1[0] = (Travel) parent.getItemAtPosition(position);
                nrSpinTravel = t1[0].getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                nrSpinTravel = 0;
            }
        });
        adapterT.notifyDataSetChanged();

        final List<Accommodation> accommodations =  Database.mAccommodationDao.fetchArrayAccommodation();
        accommodations.add(0, new Accommodation());
        ArrayAdapter<Accommodation> adapterA = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, accommodations);
        adapterA.setDropDownViewResource( R.layout._multi_spinner_dropdown_item);
        spinAccommodation.setAdapter(adapterA);

        final Accommodation[] a1 = {new Accommodation()};
        spinAccommodation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long idx) {
                a1[0] = (Accommodation) parent.getItemAtPosition(position);
                nrSpinAccommodation = a1[0].getId();
                tvAccommodation_Address.setText(a1[0].getAddress());
                if (a1[0].getCity()!=null) {
                    tvAccommodation_CityStateCountry.setText(String.format("%s - %s - %s", a1[0].getCity(), a1[0].getState(), a1[0].getCountry()));
                } else {
                    tvAccommodation_CityStateCountry.setText("");
                }
                tvAccommodation_LatLng.setText(a1[0].getLatlng_accommodation());
                tvAccommodation_Contact.setText(a1[0].getContact_name());
                tvAccommodation_Phone.setText(a1[0].getPhone());
                tvAccommodation_Email.setText(a1[0].getEmail());
                tvAccommodation_Site.setText(a1[0].getSite());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                nrSpinAccommodation = 0;
            }
        });
        adapterA.notifyDataSetChanged();

        etCheckin_Date.addTextChangedListener(new DateInputMask(etCheckin_Date));
        etCheckout_Date.addTextChangedListener(new DateInputMask(etCheckout_Date));

        if (reservation != null) {
            nrSpinTravel=reservation.getTravel_id();
            if (nrSpinTravel > 0) {
                Travel trip1 = Database.mTravelDao.fetchTravelById(nrSpinTravel);
                for (int x = 1; x <= spinTravel.getAdapter().getCount(); x++) {
                    if (spinTravel.getAdapter().getItem(x).toString().equals(trip1.getDescription())) {
                        spinTravel.setSelection(x);
                        break;
                    }
                }
            }
            if (reservation.getAccommodation_id()!=null && reservation.getAccommodation_id()>0) {
                nrSpinAccommodation = reservation.getAccommodation_id();
                Accommodation accommodation1 = Database.mAccommodationDao.fetchAccommodationById(nrSpinAccommodation);
                for (int x = 1; x <= spinAccommodation.getAdapter().getCount(); x++) {
                    if (spinAccommodation.getAdapter().getItem(x).toString().equals(accommodation1.toString())) {
                        spinAccommodation.setSelection(x);
                        tvAccommodation_Address.setText(accommodation1.getAddress());
                        tvAccommodation_CityStateCountry.setText(String.format("%s - %s - %s", accommodation1.getCity(),accommodation1.getState(),accommodation1.getCountry()));
                        tvAccommodation_LatLng.setText(accommodation1.getLatlng_accommodation());
                        tvAccommodation_Contact.setText(accommodation1.getContact_name());
                        tvAccommodation_Phone.setText(accommodation1.getPhone());
                        tvAccommodation_Email.setText(accommodation1.getEmail());
                        tvAccommodation_Site.setText(accommodation1.getSite());
                        break;
                    }
                }
            }
            etVoucher_Number.setText(reservation.getVoucher_number());
            etCheckin_Date.setText(Utils.dateToString(reservation.getCheckin_date()));
            etCheckout_Date.setText(Utils.dateToString(reservation.getCheckout_date()));
            tvRates.setText(String.valueOf(reservation.getRates()));
            /*if (!etCheckin_Date.getText().toString().equals("") &&
                    !etCheckout_Date.getText().toString().equals("")) {
                long d1 = Objects.requireNonNull(Utils.stringToDate(etCheckin_Date.getText().toString())).getTime();
                long d2 = Objects.requireNonNull(Utils.stringToDate(etCheckout_Date.getText().toString())).getTime();
                int dias = (int) ((d2 - d1) / (24 * 60 * 60 * 1000));
                tvRates.setText(String.valueOf(dias));
            }*/
            etApt_Type.setText(reservation.getApt_type());
            etDaily_Rate.setText(String.valueOf(reservation.getDaily_rate()));
            etOther_Rate.setText(String.valueOf(reservation.getOther_rate()));
            etReservation_Amount.setText(String.valueOf(reservation.getReservation_amount()));
            etAmount_Paid.setText(String.valueOf(reservation.getAmount_paid()));
            etNote.setText(reservation.getNote());
            tvStatus_Reservation.setText(getResources().getStringArray(R.array.reservation_status_array)[nrStatus_Reservation]);
        }
    }

    public void addListenerOnButtonAddAccommodation() {
        btAddAccommodation.setOnClickListener(v -> {
            Intent intent = new Intent (v.getContext(), AccommodationActivity.class);
            intent.putExtra("op_result", true);
            myActivityResultLauncher.launch(intent);
        });
    }

    public void addListenerOnButtonFileDownload() {
        btFileDownload.setOnClickListener(v -> {
            try {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                alertDialog.setIcon(R.drawable.ic_file_download);
                alertDialog.setTitle(getString(R.string.choose) + " " + getString(R.string.of) + " " + getString(R.string.site));
                String[] aSites = getApplicationContext().getResources().getStringArray(R.array.sites_reservation_array);
                alertDialog.setSingleChoiceItems(aSites, -1, (dialog, which) -> {
                    // TODO- Implementar leitura de PDF da reserva para o cadastramento no sistema
                    if (which == 0) {
                        Toast.makeText(getApplicationContext(), aSites[which], Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), aSites[which], Toast.LENGTH_LONG).show();
                    }
                    dialog.dismiss();
                });
                alertDialog.setNeutralButton("Ok", (dialog, which) -> {
                });
                AlertDialog customAlertDialog = alertDialog.create();
                customAlertDialog.show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    final ActivityResultLauncher<Intent> myActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 123) {
                        Intent data = result.getData();
                        if (data != null) {
                            accommodations =  Database.mAccommodationDao.fetchArrayAccommodation();
                            adapterA.clear();
                            adapterA.addAll(accommodations);
                            adapterA.notifyDataSetChanged() ;
                            spinAccommodation.requestFocus();
                        }
                    }
                }
            }
    );

    public void addListenerOnButtonSave() {

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
                r1.setApt_type(etApt_Type.getText().toString());
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
                etApt_Type.getText().toString().trim().isEmpty() ||
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