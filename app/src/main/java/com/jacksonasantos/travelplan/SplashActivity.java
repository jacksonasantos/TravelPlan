package com.jacksonasantos.travelplan;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.jacksonasantos.travelplan.dao.Database;
import com.jacksonasantos.travelplan.dao.InsuranceCompany;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class SplashActivity extends AppCompatActivity {
    public Database mDb;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mDb = new Database( this);
        mDb.open();

        InitialLoadCSV(getApplicationContext());

        mDb.close();

        int SPLASH_TIME_OUT = 3000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void InitialLoadCSV(Context ctx) {
        // Carga de Seguradoras
        try {
            AssetManager assetManager = ctx.getAssets();
            InputStreamReader is = new InputStreamReader(assetManager.open("seguradoras.csv"), StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(is);
            String lineStr;

            while ((lineStr = reader.readLine()) != null) {
                String[] dataLine = lineStr.split(";");
                InsuranceCompany insuranceCompany = Database.mInsuranceCompanyDao.fetchInsuranceCompanyByCNPJ(dataLine[1]);
                insuranceCompany.setCompany_name(dataLine[0]);
                insuranceCompany.setCnpj(dataLine[1]);
                insuranceCompany.setFip_code(dataLine[2]);
                insuranceCompany.setAddress(dataLine[3]);
                insuranceCompany.setCity(dataLine[4]);
                insuranceCompany.setState(dataLine[5]);
                insuranceCompany.setZip_code(dataLine[6]);
                insuranceCompany.setTelephone(dataLine[7]);
                if (dataLine.length>8 && !dataLine[8].isEmpty()) {
                    insuranceCompany.setAuthorization_date(Utils.stringToDate(dataLine[8]));
                }
                if (insuranceCompany.getId() != null) {
                    if (!Database.mInsuranceCompanyDao.updateInsuranceCompany(insuranceCompany)) {
                        Toast.makeText(ctx, String.valueOf(R.string.Error_Changing_Data) + Arrays.toString(dataLine), Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (!Database.mInsuranceCompanyDao.addInsuranceCompany(insuranceCompany)) {
                        Toast.makeText(ctx, String.valueOf(R.string.Error_Including_Data) + Arrays.toString(dataLine), Toast.LENGTH_LONG).show();
                    }
                }
            }
            is.close();
        } catch (IOException ex) {
            Log.i("debug", "Error: " + ex.getMessage());
        }
    }
}
