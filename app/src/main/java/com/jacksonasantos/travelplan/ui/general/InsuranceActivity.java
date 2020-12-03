package com.jacksonasantos.travelplan.ui.general;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Broker;
import com.jacksonasantos.travelplan.dao.Database;
import com.jacksonasantos.travelplan.dao.Insurance;
import com.jacksonasantos.travelplan.dao.InsuranceCompany;
import com.jacksonasantos.travelplan.ui.utility.DateInputMask;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public  class InsuranceActivity extends AppCompatActivity {

    private RadioGroup rgInsuranceType;
    private int rbInsuranceType;
    private AutoCompleteTextView spinInsurance_Company;
    private long nrspinInsurance_Company;
    private EditText etDescription;
    private EditText etInsurance_Policy;
    private EditText etIssuance_Date;
    private AutoCompleteTextView spinBroker;
    private long nrspinBroker;
    private EditText etInitial_Effective_Date;
    private EditText etFinal_Effective_Date;
    private EditText etNet_Premium_Value;
    private EditText etTax_Amount;
    private EditText etTotal_Premium_Value;
    private EditText etInsurance_Deductible;
    private EditText etBonus_Class;
    private EditText etNote;
    private TextView tvStatus;
    private int nrStatus;

    private boolean opInsert = true;
    private Insurance insurance;

    Globals g = Globals.getInstance();

    Locale locale = new Locale(g.getLanguage(), g.getCountry());
    NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

    @SuppressLint("WrongViewCast")
    @RequiresApi(api = Build.VERSION_CODES.N)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Database mDb = new Database(getApplicationContext());
        mDb.open();

        setTitle(R.string.Insurance);
        setContentView(R.layout.activity_insurance);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            insurance = new Insurance();
            if (extras.getLong( "insurance_id") > 0) {
                insurance.setId(extras.getLong("insurance_id"));
                insurance = Database.mInsuranceDao.fetchInsuranceById(insurance.getId());
                opInsert = false;
            }
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        addListenerOnButtonSave();

        rgInsuranceType = findViewById(R.id.rgInsuranceType);
        spinInsurance_Company = findViewById(R.id.spinInsurance_Company);
        etDescription = findViewById(R.id.etDescription);
        etInsurance_Policy = findViewById(R.id.etInsurance_Policy);
        etIssuance_Date = findViewById(R.id.etIssuance_Date);
        spinBroker = findViewById(R.id.spinBroker);
        etInitial_Effective_Date = findViewById(R.id.etInitial_Effective_Date);
        etFinal_Effective_Date = findViewById(R.id.etFinal_Effective_Date);
        etNet_Premium_Value = findViewById(R.id.etNet_Premium_Value);
        etTax_Amount = findViewById(R.id.etTax_Amount);
        etTotal_Premium_Value = findViewById(R.id.etTotal_Premium_Value);
        etInsurance_Deductible = findViewById(R.id.etInsurance_Deductible);
        etBonus_Class = findViewById(R.id.etBonus_Class);
        etNote = findViewById(R.id.etNote);
        tvStatus = findViewById(R.id.tvStatus);

        Utils.addRadioButtonResources(R.array.insurance_type_array, rgInsuranceType, this);
        rgInsuranceType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rbInsuranceType = checkedId;
            }
        });

        final List<InsuranceCompany> insuranceCompanies =  Database.mInsuranceCompanyDao.fetchArrayInsuranceCompany();
        ArrayAdapter adapterIC = new ArrayAdapter(this, android.R.layout.simple_spinner_item, insuranceCompanies);
        adapterIC.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinInsurance_Company.setAdapter(adapterIC);

        final InsuranceCompany[] insuranceCompany = {new InsuranceCompany()};
        spinInsurance_Company.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                insuranceCompany[0] = (InsuranceCompany) parent.getItemAtPosition(position);
                nrspinInsurance_Company = insuranceCompany[0].getId();
            }

        });
        adapterIC.notifyDataSetChanged();

        final List<Broker> brokers =  Database.mBrokerDao.fetchArrayBroker();
        ArrayAdapter adapterB = new ArrayAdapter(this, android.R.layout.simple_spinner_item, brokers);
        adapterB.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinBroker.setAdapter(adapterB);

        final Broker[] broker = {new Broker()};
        spinBroker.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                broker[0] = (Broker) parent.getItemAtPosition(position);
                nrspinBroker = broker[0].getId();
            }
        });
        adapterB.notifyDataSetChanged();

        etIssuance_Date.addTextChangedListener(new DateInputMask(etIssuance_Date));
        etInitial_Effective_Date.addTextChangedListener(new DateInputMask(etInitial_Effective_Date));
        etFinal_Effective_Date.addTextChangedListener(new DateInputMask(etFinal_Effective_Date));

        if (insurance != null) {
            rgInsuranceType.check(insurance.getInsurance_type());
            nrspinInsurance_Company=insurance.getInsurance_company_id();
            InsuranceCompany ic1 = Database.mInsuranceCompanyDao.fetchInsuranceCompanyById(nrspinInsurance_Company);
            for (int x = 0; x <= spinInsurance_Company.getAdapter().getCount(); x++) {
                if (spinInsurance_Company.getAdapter().getItem(x).toString().equals(ic1.getCompany_name())) {
                    spinInsurance_Company.setText(spinInsurance_Company.getAdapter().getItem(x).toString(),false);
                    break;
                }
            }
            etDescription.setText(insurance.getDescription());
            etInsurance_Policy.setText(insurance.getInsurance_policy());
            etIssuance_Date.setText(Utils.dateToString(insurance.getIssuance_date()));
            nrspinBroker=insurance.getBroker_id();
            Broker b1 = Database.mBrokerDao.fetchBrokerById(nrspinBroker);
            for (int x = 0; x <= spinBroker.getAdapter().getCount(); x++) {
                if (spinBroker.getAdapter().getItem(x).toString().equals(b1.getName())) {
                    spinBroker.setText(spinBroker.getAdapter().getItem(x).toString(),false);
                    break;
                }
            }
            etInitial_Effective_Date.setText(Utils.dateToString(insurance.getInitial_effective_date()));
            etFinal_Effective_Date.setText(Utils.dateToString(insurance.getFinal_effective_date()));
            etNet_Premium_Value.setText(String.valueOf(insurance.getNet_premium_value()));
            etTax_Amount.setText(String.valueOf(insurance.getTax_amount()));
            etTotal_Premium_Value.setText(String.valueOf(insurance.getTotal_premium_value()));
            etInsurance_Deductible.setText(String.valueOf(insurance.getInsurance_deductible()));
            etBonus_Class.setText(String.valueOf(insurance.getBonus_class()));
            etNote.setText(insurance.getNote());
            tvStatus.setText(getResources().getStringArray(R.array.insurance_status_array)[nrStatus]);
        }
    }

    public void addListenerOnButtonSave() {
        Button btSaveInsurance = findViewById(R.id.btSaveInsurance);

        btSaveInsurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSave = false;

                if (!validateData()) {
                    Toast.makeText(getApplicationContext(), R.string.Error_Data_Validation, Toast.LENGTH_LONG).show();
                } else {
                    final Insurance i1 = new Insurance();

                    i1.setInsurance_type(rbInsuranceType);
                    i1.setInsurance_company_id(nrspinInsurance_Company);
                    i1.setDescription(etDescription.getText().toString());
                    i1.setInsurance_policy(etInsurance_Policy.getText().toString());
                    i1.setIssuance_date(Utils.stringToDate(etIssuance_Date.getText().toString()));
                    i1.setBroker_id(nrspinBroker);
                    i1.setInitial_effective_date(Utils.stringToDate(etInitial_Effective_Date.getText().toString()));
                    i1.setFinal_effective_date(Utils.stringToDate(etFinal_Effective_Date.getText().toString()));
                    i1.setNet_premium_value(Double.parseDouble(etNet_Premium_Value.getText().toString()));
                    i1.setTax_amount(Double.parseDouble(etTax_Amount.getText().toString()));
                    i1.setTotal_premium_value(Double.parseDouble(etTotal_Premium_Value.getText().toString()));
                    i1.setInsurance_deductible(Double.parseDouble(etInsurance_Deductible.getText().toString()));
                    i1.setBonus_class(Integer.parseInt(etBonus_Class.getText().toString()));
                    i1.setNote(etNote.getText().toString());
                    i1.setStatus(nrStatus);

                    if (!opInsert) {
                        try {
                            i1.setId(insurance.getId());
                            isSave = Database.mInsuranceDao.updateInsurance(i1);
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), R.string.Error_Changing_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        try {
                            isSave = Database.mInsuranceDao.addInsurance(i1);
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
            if (rbInsuranceType!=0
                //&& nrspinInsurance_Company!=0
                && !etDescription.getText().toString().trim().isEmpty()
                && !etInsurance_Policy.getText().toString().trim().isEmpty()
                && !etIssuance_Date.getText().toString().trim().isEmpty()
               // && nrspinBroker!=0
                && !etInitial_Effective_Date.getText().toString().trim().isEmpty()
                && !etFinal_Effective_Date.getText().toString().trim().isEmpty()
                && !etNet_Premium_Value.getText().toString().trim().isEmpty()
                && !etTax_Amount.getText().toString().trim().isEmpty()
                && !etTotal_Premium_Value.getText().toString().trim().isEmpty()
                && !etInsurance_Deductible.getText().toString().trim().isEmpty()
                && !etBonus_Class.getText().toString().trim().isEmpty()
                //&& !etNote.getText().toString().trim().isEmpty()
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