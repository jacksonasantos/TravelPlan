package com.jacksonasantos.travelplan.ui.general;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Account;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.Utils;

public class AccountActivity extends AppCompatActivity {
    private Spinner spinAccount_Account_Type;
    private int nrSpinAccount_Account_Type;
    private EditText etAccount_Description;

    private boolean opInsert = true;
    private boolean opResult = false;
    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Database mDb = new Database(getApplicationContext());
        mDb.open();

        setTitle(R.string.account);
        setContentView(R.layout.activity_account);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            account = new Account();
            if (extras.getInt( "account_id") > 0) {
                account.setId(extras.getInt("account_id"));
                account = Database.mAccountDao.fetchAccountById(account.getId());
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

        spinAccount_Account_Type = findViewById(R.id.spinAccount_Account_Type);
        etAccount_Description = findViewById(R.id.etAccount_Description);

        Utils.createSpinnerResources(R.array.account_type_array, spinAccount_Account_Type, this);
        nrSpinAccount_Account_Type = 0;
        spinAccount_Account_Type.setSelection(nrSpinAccount_Account_Type);
        spinAccount_Account_Type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long idx) {
                nrSpinAccount_Account_Type = position;  }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                nrSpinAccount_Account_Type =0;
            }
        });

        if (account != null) {
            etAccount_Description.setText(account.getDescription());
            nrSpinAccount_Account_Type = account.getAccount_type();
            spinAccount_Account_Type.setSelection(nrSpinAccount_Account_Type);
        }
    }

    public void addListenerOnButtonSave() {
        Button btSaveAccount = findViewById(R.id.btSaveAccount);

        btSaveAccount.setOnClickListener(v -> {
            boolean isSave = false;

            if (!validateData()) {
                Toast.makeText(getApplicationContext(), R.string.Error_Data_Validation, Toast.LENGTH_LONG).show();
            } else {
                final Account a1 = new Account();

                a1.setDescription(etAccount_Description.getText().toString());
                a1.setAccount_type(nrSpinAccount_Account_Type);

                if (!opInsert) {
                    try {
                        a1.setId(account.getId());
                        isSave = Database.mAccountDao.updateAccount(a1);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), R.string.Error_Changing_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        isSave = Database.mAccountDao.addAccount(a1);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), R.string.Error_Including_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                setResult(isSave ? 1 : 0);
                if (isSave) {
                    if (opResult) {
                        Intent i = new Intent();
                        i.putExtra("resulted_value", a1.getDescription());
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
            if (etAccount_Description.getText().toString().trim().isEmpty() ||
                String.valueOf(nrSpinAccount_Account_Type).trim().isEmpty()
            ){
                isValid = false;
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.Data_Validator_Error +" - " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return isValid;
    }
}