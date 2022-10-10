package com.jacksonasantos.travelplan.ui.general;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Driver;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.DateInputMask;
import com.jacksonasantos.travelplan.ui.utility.Utils;

public class DriverActivity extends AppCompatActivity {
    private EditText etName;
    private EditText etShortName;
    private RadioGroup rgGender;
    private int rbGender;
    private EditText etDrivingRecord;
    private EditText etLicenceExpirationDate;
    private EditText etFirstLicenceDate;
    private EditText etLicenceIssueDate;
    private EditText etCategory;

    private boolean opInsert = true;
    private Driver driver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Database mDb = new Database(getApplicationContext());
        mDb.open();

        setTitle(R.string.Driver);
        setContentView(R.layout.activity_driver);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            driver = new Driver();
            if (extras.getInt( "driver_id") > 0) {
                driver.setId(extras.getInt("driver_id"));
                driver = Database.mDriverDao.fetchDriverById(driver.getId());
                opInsert = false;
            }
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        addListenerOnButtonSave();

        etName = findViewById(R.id.etName);
        etShortName = findViewById(R.id.etShortName);
        rgGender = findViewById(R.id.rgGender);
        etDrivingRecord = findViewById(R.id.etDrivingRecord);
        etLicenceExpirationDate = findViewById(R.id.etLicenceExpirationDate);
        etFirstLicenceDate = findViewById(R.id.etFirstLicenceDate);
        etLicenceIssueDate = findViewById(R.id.etLicenceIssueDate);
        etCategory = findViewById(R.id.etCategory);

        Utils.addRadioButtonResources(R.array.gender, rgGender, this);
        rgGender.setOnCheckedChangeListener((group, checkedId) -> rbGender = checkedId);

        etLicenceExpirationDate.addTextChangedListener(new DateInputMask(etLicenceExpirationDate));
        etFirstLicenceDate.addTextChangedListener(new DateInputMask(etFirstLicenceDate));
        etLicenceIssueDate.addTextChangedListener(new DateInputMask(etLicenceIssueDate));

        if (driver != null) {
            etName.setText(driver.getName());
            etShortName.setText(driver.getShort_Name());
            rgGender.check(driver.getGender());
            etDrivingRecord.setText(driver.getDriving_record());
            etLicenceExpirationDate.setText(Utils.dateToString(driver.getLicense_expiration_date()));
            etFirstLicenceDate.setText(Utils.dateToString(driver.getFirst_license_date()));
            etLicenceIssueDate.setText(Utils.dateToString(driver.getLicence_issue_date()));
            etCategory.setText(driver.getCategory());
        }
    }

    public void addListenerOnButtonSave() {
        Button btSaveDriver = findViewById(R.id.btSaveDriver);

        btSaveDriver.setOnClickListener(v -> {
            boolean isSave = false;

            if (!validateData()) {
                Toast.makeText(getApplicationContext(), R.string.Error_Data_Validation, Toast.LENGTH_LONG).show();
            } else {
                final Driver d1 = new Driver();

                d1.setName(etName.getText().toString());
                d1.setShort_Name(etShortName.getText().toString());
                d1.setGender(rbGender);
                d1.setDriving_record(etDrivingRecord.getText().toString());
                d1.setLicense_expiration_date(Utils.stringToDate(etLicenceExpirationDate.getText().toString()));
                d1.setFirst_license_date(Utils.stringToDate(etFirstLicenceDate.getText().toString()));
                d1.setLicence_issue_date(Utils.stringToDate(etLicenceIssueDate.getText().toString()));
                d1.setCategory(etCategory.getText().toString());

                if (!opInsert) {
                    try {
                        d1.setId(driver.getId());
                        isSave = Database.mDriverDao.updateDriver(d1);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), R.string.Error_Changing_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        isSave = Database.mDriverDao.addDriver(d1);
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
            if (!etName.getText().toString().trim().isEmpty() &&
                !etShortName.getText().toString().trim().isEmpty() &&
                rbGender != 0 &&
                !etDrivingRecord.getText().toString().trim().isEmpty() &&
                !etLicenceIssueDate.getText().toString().trim().isEmpty() &&
                !etFirstLicenceDate.getText().toString().trim().isEmpty() &&
                !etLicenceIssueDate.getText().toString().trim().isEmpty() &&
                !etCategory.getText().toString().trim().isEmpty()
            ){
                isValid = true;
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.Data_Validator_Error +" - " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return isValid;
    }
}