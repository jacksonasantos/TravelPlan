package com.jacksonasantos.travelplan.ui.travel;

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
import com.jacksonasantos.travelplan.dao.Transport;
import com.jacksonasantos.travelplan.dao.Travel;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.DateTimeInputMask;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.ArrayList;
import java.util.List;

public class TransportActivity extends AppCompatActivity implements TransportTypeListAdapter.ItemClickListener {

    private boolean opInsert = true;

    private Transport transport;

    private LinearLayoutManager linearLayoutManager;

    private TextView tvTravel;
    private RecyclerView rvTransportType ;
    private int nrTransportType= -1;
    public TransportTypeListAdapter adapterTransportType;

    private LinearLayout llTransportTypeOwn;
    private LinearLayout llTransportType;

    private Spinner spOwnVehicle;

    private TextView tvIdentifier ;
    private TextView tvDescription ;
    private TextView tvCompany ;
    private TextView tvContact ;
    private TextView tvStartLocation ;
    private TextView tvEndLocation ;
    private TextView tvServiceValue;
    private TextView tvServiceTax;
    private TextView tvAmountPaid;
    private TextView tvNote;

    private EditText etIdentifier ;
    private EditText etDescription ;
    private EditText etCompany ;
    private EditText etContact ;
    private EditText etStartLocation ;
    private EditText etStartDate;
    private EditText etEndLocation ;
    private EditText etEndDate;
    private EditText etServiceValue;
    private EditText etServiceTax;
    private EditText etAmountPaid;
    private EditText etNote;

    private RecyclerView rvTransportPerson;
    private TextView tvTransportPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Database mDb = new Database(getApplicationContext());
        mDb.open();

        setTitle(R.string.Transport);
        setContentView(R.layout.dialog_transport);

        Bundle extras = getIntent().getExtras();
        transport = new Transport();
        transport.setService_value(0.0);
        transport.setService_tax(0.0);
        transport.setAmount_paid(0.0);
        llTransportTypeOwn = findViewById(R.id.llTransportTypeOwn);
        llTransportType = findViewById(R.id.llTransportType);
        if (extras != null) {
            if (extras.getInt( "transport_id") > 0) {
                transport.setId(extras.getInt("transport_id"));
                transport = Database.mTransportDao.fetchTransportById(transport.getId());
                opInsert = false;
                llTransportTypeOwn.setVisibility(View.INVISIBLE);
                llTransportType.setVisibility(View.INVISIBLE);
                if (transport.getTransport_type()==0) {
                    llTransportTypeOwn.setVisibility(View.VISIBLE);
                } else {
                    llTransportType.setVisibility(View.VISIBLE);
                }
            }
            if (extras.getInt( "travel_id") > 0) {
                transport.setTravel_id(extras.getInt("travel_id"));
            }
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        addListenerOnButtonSave();

        tvTravel = findViewById(R.id.tvTravel);
        rvTransportType = findViewById(R.id.rvTransportType);
        spOwnVehicle = findViewById(R.id.spOwnVehicle);

        tvIdentifier = findViewById(R.id.tvIdentifier);
        tvDescription = findViewById(R.id.tvDescription);
        tvCompany = findViewById(R.id.tvCompany);
        tvContact = findViewById(R.id.tvContact);
        tvStartLocation = findViewById(R.id.tvStartLocation);
        tvEndLocation = findViewById(R.id.tvEndLocation);
        tvServiceValue = findViewById(R.id.tvServiceValue);
        tvServiceTax = findViewById(R.id.tvServiceTax);
        tvAmountPaid = findViewById(R.id.tvAmountPaid);
        tvNote = findViewById(R.id.tvNote);

        etIdentifier = findViewById(R.id.etIdentifier);
        etDescription = findViewById(R.id.etDescription);
        etCompany = findViewById(R.id.etCompany);
        etContact = findViewById(R.id.etContact);
        etStartLocation = findViewById(R.id.etStartLocation);
        etStartDate = findViewById(R.id.etStartLocationDate);
        etEndLocation = findViewById(R.id.etEndLocation);
        etEndDate = findViewById(R.id.etEndLocationDate);
        etServiceValue = findViewById(R.id.etServiceValue);
        etServiceTax = findViewById(R.id.etServiceTax);
        etAmountPaid = findViewById(R.id.etAmountPaid);
        etNote = findViewById(R.id.etNote);

        tvTransportPerson = findViewById(R.id.tvTransportPerson);
        rvTransportPerson = findViewById(R.id.rvTransportPerson);

        nrTransportType = -1;

        List<Integer> vTransportType = new ArrayList<>();
        for(int i = 0; i < getApplicationContext().getResources().getStringArray(R.array.transport_type_array).length; i++) {
            vTransportType.add(i);
        }

        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        rvTransportType.setLayoutManager(linearLayoutManager);
        adapterTransportType = new TransportTypeListAdapter(vTransportType, this);
        adapterTransportType.addItemClickListener(this);
        rvTransportType.setAdapter(adapterTransportType);

        etStartDate.addTextChangedListener(new DateTimeInputMask(etStartDate));
        etEndDate.addTextChangedListener(new DateTimeInputMask(etEndDate));

        if (transport != null) {
            Travel t1 = Database.mTravelDao.fetchTravelById(transport.getTravel_id());
            tvTravel.setText(t1.getDescription());
            nrTransportType = transport.getTransport_type();
            Utils.selected_position = nrTransportType;
            etIdentifier.setText(transport.getIdentifier());
            etDescription.setText(transport.getDescription());
            etCompany.setText(transport.getCompany());
            etContact.setText(transport.getContact());
            etStartLocation.setText(transport.getStart_location());
            etStartDate.setText(Utils.dateTimeToString(transport.getStart_date()));
            etEndLocation.setText(transport.getEnd_location());
            etEndDate.setText(Utils.dateTimeToString(transport.getEnd_date()));
            etServiceValue.setText(String.valueOf(transport.getService_value()));
            etServiceTax.setText(String.valueOf(transport.getService_tax()));
            etAmountPaid.setText(String.valueOf(transport.getAmount_paid()));
            etNote.setText(transport.getNote());
        }
    }

    @Override
    public void onItemClick(int position) {

        if (nrTransportType != position) {
            nrTransportType = position;
        }
        else nrTransportType = -1;

        Utils.selected_position = nrTransportType;

        switch(nrTransportType) {
            case -1: {
                llTransportTypeOwn.setVisibility(View.GONE);
                llTransportType.setVisibility(View.GONE);
                break;
            }
            case 0: {
                llTransportTypeOwn.setVisibility(View.VISIBLE);
                llTransportType.setVisibility(View.GONE);
                tvTransportPerson.setText(getResources().getString(R.string.Person));
                break;
            }
            case 1: {
                llTransportTypeOwn.setVisibility(View.GONE);
                llTransportType.setVisibility(View.VISIBLE);
                tvIdentifier.setText(getResources().getString(R.string.Transport_Voo_Reserve));
                tvCompany.setText(getResources().getString(R.string.Transport_Voo_Company));
                tvDescription.setText(getResources().getString(R.string.Transport_Voo_Voo_Code));
                tvContact.setText(getResources().getString(R.string.Transport_Voo_Contact));
                tvStartLocation.setText(getResources().getString(R.string.Transport_Voo_Departure));
                tvEndLocation.setText(getResources().getString(R.string.Transport_Voo_Arrival));
                tvServiceValue.setText(getResources().getString(R.string.Transport_Voo_Value_Ticket));
                tvServiceTax.setText(getResources().getString(R.string.Transport_Voo_Value_Tax));
                tvAmountPaid.setText(getResources().getString(R.string.Transport_Voo_Amount_Paid));
                tvNote.setText(getResources().getString(R.string.Transport_Voo_Note));
                tvTransportPerson.setText(getResources().getString(R.string.Traveler));
                break;
            }
            case 2: {
                llTransportTypeOwn.setVisibility(View.GONE);
                llTransportType.setVisibility(View.VISIBLE);
                tvIdentifier.setText(getResources().getString(R.string.Transport_Rental_Voucher));
                tvCompany.setText(getResources().getString(R.string.Transport_Rental_Company));
                tvDescription.setText(getResources().getString(R.string.Transport_Rental_Description));
                tvContact.setText(getResources().getString(R.string.Transport_Rental_Contact));
                tvStartLocation.setText(getResources().getString(R.string.Transport_Rental_PickUp_Location));
                tvEndLocation.setText(getResources().getString(R.string.Transport_Rental_Return_Location));
                tvServiceValue.setText(getResources().getString(R.string.Transport_Rental_Reserve_Value));
                tvServiceTax.setText(getResources().getString(R.string.Transport_Rental_Value_Tax));
                tvAmountPaid.setText(getResources().getString(R.string.Transport_Rental_Amount_Paid));
                tvNote.setText(getResources().getString(R.string.Transport_Rental_Note));
                tvTransportPerson.setText(getResources().getString(R.string.Renter));
                break;
            }
            case 3: {
                llTransportTypeOwn.setVisibility(View.GONE);
                llTransportType.setVisibility(View.VISIBLE);
                tvIdentifier.setText(getResources().getString(R.string.Transport_Hiring_Identifier));
                tvCompany.setText(getResources().getString(R.string.Transport_Hiring_Company));
                tvDescription.setText(getResources().getString(R.string.Transport_Hiring_Description));
                tvContact.setText(getResources().getString(R.string.Transport_Hiring_Contact));
                tvStartLocation.setText(getResources().getString(R.string.Transport_Hiring_Start_Location));
                tvEndLocation.setText(getResources().getString(R.string.Transport_Hiring_End_Location));
                tvServiceValue.setText(getResources().getString(R.string.Transport_Hiring_Service_Value));
                tvServiceTax.setText(getResources().getString(R.string.Transport_Hiring_Service_Tax));
                tvAmountPaid.setText(getResources().getString(R.string.Transport_Hiring_Amount_Paid));
                tvNote.setText(getResources().getString(R.string.Transport_Hiring_Note));
                tvTransportPerson.setText(getResources().getString(R.string.Traveler));
                break;
            }

        }
    }

    public void addListenerOnButtonSave() {
        Button btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> {
            boolean isSave = false;

            if (!validateData()) {
                Toast.makeText(getApplicationContext(), R.string.Error_Data_Validation, Toast.LENGTH_LONG).show();
            } else {
                final Transport t1 = new Transport();
                t1.setTravel_id(transport.getTravel_id());
                t1.setTransport_type(nrTransportType);
                t1.setIdentifier(etIdentifier.getText().toString());
                t1.setDescription(etDescription.getText().toString());
                t1.setCompany(etCompany.getText().toString());
                t1.setContact(etContact.getText().toString());
                t1.setStart_location(etStartLocation.getText().toString());
                t1.setStart_date(Utils.stringToDateTime(etStartDate.getText().toString()));
                t1.setEnd_location(etEndLocation.getText().toString());
                t1.setEnd_date(Utils.stringToDateTime(etEndDate.getText().toString()));
                t1.setService_value(Double.parseDouble(etServiceValue.getText().toString()));
                t1.setService_tax(Double.parseDouble(etServiceTax.getText().toString()));
                t1.setAmount_paid(Double.parseDouble(etAmountPaid.getText().toString()));
                t1.setNote(etNote.getText().toString());

                if (!opInsert) {
                    try {
                        t1.setId(transport.getId());
                        isSave = Database.mTransportDao.updateTransport(t1);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), R.string.Error_Changing_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        isSave = Database.mTransportDao.addTransport(t1);
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
            if (nrTransportType == -1 ||
                etIdentifier.getText().toString().trim().isEmpty() ||
                etDescription.getText().toString().trim().isEmpty() ||
                etCompany.getText().toString().trim().isEmpty() ||
                //etContact.getText().toString().trim().isEmpty() ||
                etStartLocation.getText().toString().trim().isEmpty() ||
                etStartDate.getText().toString().trim().isEmpty() ||
                //etEndLocation.getText().toString().trim().isEmpty() ||
                //etEndDate.getText().toString().trim().isEmpty() ||
                etServiceValue.getText().toString().trim().isEmpty() ||
                //etServiceTax.getText().toString().trim().isEmpty() ||
                //etAmountPaid.getText().toString().trim().isEmpty() ||
                etNote.getText().toString().trim().isEmpty()
            ){
                isValid = false;
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.Data_Validator_Error +" - " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return isValid;
    }
}