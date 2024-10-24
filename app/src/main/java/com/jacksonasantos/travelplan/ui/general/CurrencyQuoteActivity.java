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

import com.jacksonasantos.travelplan.MainActivity;
import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.CurrencyQuote;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.DateInputMask;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.Objects;

public class CurrencyQuoteActivity extends AppCompatActivity {

    private int nrSpinCurrencyType;
    private EditText etQuoteDate;
    private EditText etCurrencyValue;

    private boolean opInsert = true;
    private boolean opResult = false;
    private CurrencyQuote currencyQuote;

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
            } else {
                if (extras.getInt( "currency_type") > 0) {
                    currencyQuote.setCurrency_type(extras.getInt("currency_type"));
                    opInsert = true;
                    opResult = true;
                }
                if (!Objects.equals(extras.getString("quote_date"), "")) {
                    currencyQuote.setQuote_date(Utils.stringToDate(extras.getString("quote_date")));
                    opInsert = true;
                    opResult = true;
                }
            }
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        addListenerOnButtonSave();

        Spinner spinCurrencyType = findViewById(R.id.spinCurrencyType);
        etQuoteDate = findViewById(R.id.etQuoteDate);
        etCurrencyValue = findViewById(R.id.etCurrencyValue);

        Utils.createSpinnerResources(R.array.currency_array, spinCurrencyType, this);
        nrSpinCurrencyType = 0;
        spinCurrencyType.setSelection(nrSpinCurrencyType);
        spinCurrencyType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long idx) {
                nrSpinCurrencyType = position;  }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                nrSpinCurrencyType =0;
            }
        });
        etQuoteDate.addTextChangedListener(new DateInputMask(etQuoteDate));

        if (currencyQuote != null) {
            nrSpinCurrencyType=currencyQuote.getCurrency_type();
            spinCurrencyType.setSelection(nrSpinCurrencyType);
            etQuoteDate.setText(Utils.dateToString(currencyQuote.getQuote_date()));
            etCurrencyValue.setText(String.valueOf(currencyQuote.getCurrency_value()));
        }
        if (opInsert) {
            etCurrencyValue.setOnFocusChangeListener((view, hasFocus) -> {
                if (hasFocus) {
                    MainActivity.DownloadCurrencyQuote( nrSpinCurrencyType );

                    //TODO - Receber online o valor da cotação que foi adquirida logo acima de maneira assincrona
                    CurrencyQuote c1 = Database.mCurrencyQuoteDao.findDayQuote(nrSpinCurrencyType, Utils.stringToDate(etQuoteDate.getText().toString()));
                    etCurrencyValue.setText(String.valueOf(c1.getCurrency_value()));
                }
            });
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
                    if (opResult) {
                        Intent i = new Intent();
                        i.putExtra("resulted_value", etCurrencyValue.getText().toString());
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