package com.jacksonasantos.travelplan.ui.general;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Broker;
import com.jacksonasantos.travelplan.dao.general.Database;

public class BrokerActivity extends AppCompatActivity {
    private EditText etName;
    private EditText etPhone;
    private EditText etEmail;
    private EditText etContact_name;

    private boolean opInsert = true;
    private Broker broker;

    @SuppressLint("WrongViewCast")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Database mDb = new Database(getApplicationContext());
        mDb.open();

        setTitle(R.string.Broker);
        setContentView(R.layout.activity_broker);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            broker = new Broker();
            if (extras.getInt( "broker_id") > 0) {
                broker.setId(extras.getInt("broker_id"));
                broker = Database.mBrokerDao.fetchBrokerById(broker.getId());
                opInsert = false;
            }
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        addListenerOnButtonSave();

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etContact_name = findViewById(R.id.etContact_name);

        if (broker != null) {
            etName.setText(broker.getName());
            etPhone.setText(broker.getPhone());
            etEmail.setText(broker.getEmail());
            etContact_name.setText(broker.getContact_name());
        }
    }

    public void addListenerOnButtonSave() {
        Button btSaveBroker = findViewById(R.id.btSaveBroker);

        btSaveBroker.setOnClickListener(v -> {
            boolean isSave = false;

            if (!validateData()) {
                Toast.makeText(getApplicationContext(), R.string.Error_Data_Validation, Toast.LENGTH_LONG).show();
            } else {
                final Broker b1 = new Broker();

                b1.setName(etName.getText().toString());
                b1.setPhone(etPhone.getText().toString());
                b1.setEmail(etEmail.getText().toString());
                b1.setContact_name(etContact_name.getText().toString());

                if (!opInsert) {
                    try {
                        b1.setId(broker.getId());
                        isSave = Database.mBrokerDao.updateBroker(b1);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), R.string.Error_Changing_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        isSave = Database.mBrokerDao.addBroker(b1);
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
            if (etName.getText().toString().trim().isEmpty() ||
                etPhone.getText().toString().trim().isEmpty() ||
                etEmail.getText().toString().trim().isEmpty() ||
                etContact_name.getText().toString().trim().isEmpty()
            ){
                isValid = false;
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.Data_Validator_Error +" - " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return isValid;
    }
}