package com.jacksonasantos.travelplan.ui.general;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Person;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.DateInputMask;
import com.jacksonasantos.travelplan.ui.utility.Utils;

public class PersonActivity extends AppCompatActivity {
    private EditText etName;
    private EditText etShortName;
    private RadioGroup rgGender;
    private int rbGender;
    private EditText etDateBirth;
    private EditText etDrivingRecord;
    private EditText etLicenseExpirationDate;
    private EditText etFirstLicenseDate;
    private EditText etLicenseIssueDate;
    private EditText etLicenseCategory;

    private boolean opInsert = true;
    private Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Database mDb = new Database(getApplicationContext());
        mDb.open();

        setTitle(R.string.Person);
        setContentView(R.layout.activity_person);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            person = new Person();
            if (extras.getInt( "person_id") > 0) {
                person.setId(extras.getInt("person_id"));
                person = Database.mPersonDao.fetchPersonById(person.getId());
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
        etDateBirth = findViewById(R.id.etDateBirth);
        etDrivingRecord = findViewById(R.id.etDrivingRecord);
        etLicenseExpirationDate = findViewById(R.id.etLicenseExpirationDate);
        etFirstLicenseDate = findViewById(R.id.etFirstLicenseDate);
        etLicenseIssueDate = findViewById(R.id.etLicenseIssueDate);
        etLicenseCategory = findViewById(R.id.etLicenseCategory);

        Utils.addRadioButtonResources(R.array.gender, rgGender, this);
        rgGender.setOnCheckedChangeListener((group, checkedId) -> rbGender = checkedId);

        etDateBirth.addTextChangedListener(new DateInputMask(etDateBirth));
        etLicenseExpirationDate.addTextChangedListener(new DateInputMask(etLicenseExpirationDate));
        etFirstLicenseDate.addTextChangedListener(new DateInputMask(etFirstLicenseDate));
        etLicenseIssueDate.addTextChangedListener(new DateInputMask(etLicenseIssueDate));

        if (person != null) {
            etName.setText(person.getName());
            etShortName.setText(person.getShort_Name());
            rgGender.check(person.getGender());
            etDateBirth.setText(Utils.dateToString(person.getDate_birth()));
            etDrivingRecord.setText(person.getDriving_record());
            etLicenseExpirationDate.setText(Utils.dateToString(person.getLicense_expiration_date()));
            etFirstLicenseDate.setText(Utils.dateToString(person.getFirst_license_date()));
            etLicenseIssueDate.setText(Utils.dateToString(person.getLicense_issue_date()));
            etLicenseCategory.setText(person.getLicense_category());
        }
    }

    public void addListenerOnButtonSave() {
        Button btSavePerson = findViewById(R.id.btSavePerson);

        btSavePerson.setOnClickListener(v -> {
            boolean isSave = false;

            if (!validateData()) {
                Toast.makeText(getApplicationContext(), R.string.Error_Data_Validation, Toast.LENGTH_LONG).show();
            } else {
                final Person d1 = new Person();

                d1.setName(etName.getText().toString());
                d1.setShort_Name(etShortName.getText().toString());
                d1.setGender(rbGender);
                d1.setDate_birth(Utils.stringToDate(etDateBirth.getText().toString()));
                d1.setDriving_record(etDrivingRecord.getText().toString());
                d1.setLicense_expiration_date(Utils.stringToDate(etLicenseExpirationDate.getText().toString()));
                d1.setFirst_license_date(Utils.stringToDate(etFirstLicenseDate.getText().toString()));
                d1.setLicense_issue_date(Utils.stringToDate(etLicenseIssueDate.getText().toString()));
                d1.setLicense_category(etLicenseCategory.getText().toString());

                if (!opInsert) {
                    try {
                        d1.setId(person.getId());
                        isSave = Database.mPersonDao.updatePerson(d1);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), R.string.Error_Changing_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        isSave = Database.mPersonDao.addPerson(d1);
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
                !etDateBirth.getText().toString().trim().isEmpty() //&&
//                !etDrivingRecord.getText().toString().trim().isEmpty() &&
//                !etLicenseIssueDate.getText().toString().trim().isEmpty() &&
//                !etFirstLicenseDate.getText().toString().trim().isEmpty() &&
//                !etLicenseIssueDate.getText().toString().trim().isEmpty() &&
//                !etLicenseCategory.getText().toString().trim().isEmpty()
            ){
                isValid = true;
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.Data_Validator_Error +" - " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return isValid;
    }
}