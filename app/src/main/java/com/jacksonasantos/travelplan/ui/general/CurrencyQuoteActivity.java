package com.jacksonasantos.travelplan.ui.general;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.dao.CurrencyQuote;
import com.jacksonasantos.travelplan.ui.utility.DateInputMask;
import com.jacksonasantos.travelplan.ui.utility.Utils;

public class CurrencyQuoteActivity extends AppCompatActivity {

    private int nrSpinCurrencyType;
    private EditText etQuoteDate;
    private EditText etCurrencyValue;

    private boolean opInsert = true;
    private CurrencyQuote currencyQuote;

    @SuppressLint("WrongViewCast")
    @RequiresApi(api = Build.VERSION_CODES.N)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Database mDb = new Database(getApplicationContext());
        mDb.open();

        setTitle(R.string.Currency_Quote);
        setContentView(R.layout.activity_currency_quote);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currencyQuote = new CurrencyQuote();
            if (extras.getInt( "currencyQuote_id") > 0) {
                currencyQuote.setId(extras.getInt("currencyQuote_id"));
                currencyQuote = Database.mCurrencyQuoteDao.fetchCurrencyQuoteById(currencyQuote.getId());
                opInsert = false;
            }
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        addListenerOnButtonSave();

        AutoCompleteTextView spinCurrencyType = findViewById(R.id.spinCurrencyType);
        etQuoteDate = findViewById(R.id.etQuoteDate);
        etCurrencyValue = findViewById(R.id.etCurrencyValue);

        Utils.createSpinnerResources(R.array.currency_array, spinCurrencyType, this);
        nrSpinCurrencyType = 0;
        spinCurrencyType.setOnItemClickListener((adapterView, view, i, l) -> nrSpinCurrencyType = (int) adapterView.getItemIdAtPosition(i));
        etQuoteDate.addTextChangedListener(new DateInputMask(etQuoteDate));

        if (currencyQuote != null) {
            nrSpinCurrencyType=currencyQuote.getCurrency_type();
            spinCurrencyType.setText(getResources().getStringArray(R.array.currency_array)[nrSpinCurrencyType],false);
            etQuoteDate.setText(Utils.dateToString(currencyQuote.getQuote_date()));
            etCurrencyValue.setText(String.valueOf(currencyQuote.getCurrency_value()));
        }
    }

    public void addListenerOnButtonSave() {
        Button btSaveCurrencyQuote = findViewById(R.id.btSaveCurrencyQuote);

        btSaveCurrencyQuote.setOnClickListener(v -> {
            boolean isSave = false;

            if (!validateData()) {
                Toast.makeText(getApplicationContext(), R.string.Error_Data_Validation, Toast.LENGTH_LONG).show();
            } else {
                final CurrencyQuote c1 = new CurrencyQuote();

                c1.setCurrency_type(nrSpinCurrencyType);
                c1.setQuote_date(Utils.stringToDate(etQuoteDate.getText().toString()));
                c1.setCurrency_value(Double.parseDouble(etCurrencyValue.getText().toString()));

                if (!opInsert) {
                    try {
                        c1.setId(currencyQuote.getId());
                        isSave = Database.mCurrencyQuoteDao.updateCurrencyQuote(c1);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), R.string.Error_Changing_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        isSave = Database.mCurrencyQuoteDao.addCurrencyQuote(c1);
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
            if (!String.valueOf(nrSpinCurrencyType).trim().isEmpty()
                && !etQuoteDate.getText().toString().trim().isEmpty()
                && !etCurrencyValue.getText().toString().trim().isEmpty()
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