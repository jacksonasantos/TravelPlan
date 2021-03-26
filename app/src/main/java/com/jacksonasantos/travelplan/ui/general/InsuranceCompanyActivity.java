package com.jacksonasantos.travelplan.ui.general;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.dao.InsuranceCompany;
import com.jacksonasantos.travelplan.ui.utility.DateInputMask;
import com.jacksonasantos.travelplan.ui.utility.Utils;

public class InsuranceCompanyActivity extends AppCompatActivity {
    private EditText etCompanyName;
    private EditText etCnpj;
    private EditText etFip_code;
    private EditText etAddress;
    private EditText etCity;
    private EditText etState;
    private EditText etZipCode;
    private EditText etTelephone;
    private EditText etAuthorization_Date;

    private boolean opInsert = true;
    private InsuranceCompany insuranceCompany;

    @SuppressLint("WrongViewCast")
    @RequiresApi(api = Build.VERSION_CODES.N)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Database mDb = new Database(getApplicationContext());
        mDb.open();

        setTitle(R.string.Insurance_Company);
        setContentView(R.layout.activity_insurance_company);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            insuranceCompany = new InsuranceCompany();
            if (extras.getInt( "insuranceCompany_id") > 0) {
                insuranceCompany.setId(extras.getInt("insuranceCompany_id"));
                insuranceCompany = Database.mInsuranceCompanyDao.fetchInsuranceCompanyById(insuranceCompany.getId());
                opInsert = false;
            }
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        addListenerOnButtonSave();

        etCompanyName = findViewById(R.id.etCompanyName);
        etCnpj = findViewById(R.id.etCnpj);
        etFip_code = findViewById(R.id.etFip_code);
        etAddress = findViewById(R.id.etAddress);
        etCity = findViewById(R.id.etCity);
        etState = findViewById(R.id.etState);
        etZipCode = findViewById(R.id.etZipCode);
        etTelephone = findViewById(R.id.etTelephone);
        etAuthorization_Date = findViewById(R.id.etAuthorization_Date);

        etAuthorization_Date.addTextChangedListener(new DateInputMask(etAuthorization_Date));

        if (insuranceCompany != null) {
            etCompanyName.setText(insuranceCompany.getCompany_name());
            etCnpj.setText(insuranceCompany.getCnpj());
            etFip_code.setText(insuranceCompany.getFip_code());
            etAddress.setText(insuranceCompany.getAddress());
            etCity.setText(insuranceCompany.getCity());
            etState.setText(insuranceCompany.getState());
            etZipCode.setText(insuranceCompany.getZip_code());
            etTelephone.setText(insuranceCompany.getTelephone());
            etAuthorization_Date.setText(Utils.dateToString(insuranceCompany.getAuthorization_date()));
        }
    }

    public void addListenerOnButtonSave() {
        Button btSaveInsuranceCompany = findViewById(R.id.btSaveInsuranceCompany);

        btSaveInsuranceCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSave = false;

                if (!validateData()) {
                    Toast.makeText(getApplicationContext(), R.string.Error_Data_Validation, Toast.LENGTH_LONG).show();
                } else {
                    final InsuranceCompany i1 = new InsuranceCompany();

                    i1.setCompany_name(etCompanyName.getText().toString());
                    i1.setCnpj(etCnpj.getText().toString());
                    i1.setFip_code(etFip_code.getText().toString());
                    i1.setAddress(etAddress.getText().toString());
                    i1.setCity(etCity.getText().toString());
                    i1.setState(etState.getText().toString());
                    i1.setZip_code(etZipCode.getText().toString());
                    i1.setTelephone(etTelephone.getText().toString());
                    i1.setAuthorization_date(Utils.stringToDate(etAuthorization_Date.getText().toString()));

                    if (!opInsert) {
                        try {
                            i1.setId(insuranceCompany.getId());
                            isSave = Database.mInsuranceCompanyDao.updateInsuranceCompany(i1);
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), R.string.Error_Changing_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        try {
                            isSave = Database.mInsuranceCompanyDao.addInsuranceCompany(i1);
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
            if (!etCompanyName.getText().toString().trim().isEmpty()
                && !etCnpj.getText().toString().trim().isEmpty()
                && !etFip_code.getText().toString().trim().isEmpty()
                && !etAddress.getText().toString().trim().isEmpty()
                && !etCity.getText().toString().trim().isEmpty()
                && !etState.getText().toString().trim().isEmpty()
                && !etZipCode.getText().toString().trim().isEmpty()
                && !etTelephone.getText().toString().trim().isEmpty()
                && !etAuthorization_Date.getText().toString().trim().isEmpty()
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