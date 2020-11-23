package com.jacksonasantos.travelplan;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.google.android.material.navigation.NavigationView;
import com.jacksonasantos.travelplan.dao.Database;
import com.jacksonasantos.travelplan.dao.InsuranceCompany;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    Globals g = Globals.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Resources res = getResources();
        Configuration config = res.getConfiguration();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String lang = settings.getString("language", "");
        if (lang != null && !"".equals(lang) && !config.locale.getLanguage().equals(lang)) {
            g.setLanguage(lang);
            Locale myLocale = new Locale(lang);
            config.locale = myLocale;
            DisplayMetrics dm = res.getDisplayMetrics();
            Locale.setDefault(myLocale);
            res.updateConfiguration(config, dm);
        }
        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);

        InitialLoadCSV(getApplicationContext());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_vehicle, R.id.nav_fuel_supply, R.id.nav_maintenance, R.id.nav_travel, R.id.nav_settings, R.id.nav_insurance_company)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void InitialLoadCSV(Context ctx) {
        Database mdb = new Database(ctx);
        mdb.open();
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
                        Toast.makeText(ctx, String.valueOf(R.string.Error_Changing_Data) + dataLine, Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (!Database.mInsuranceCompanyDao.addInsuranceCompany(insuranceCompany)) {
                        Toast.makeText(ctx, String.valueOf(R.string.Error_Including_Data) + dataLine, Toast.LENGTH_LONG).show();
                    }
                }
            }
            is.close();
        } catch (IOException ex) {
            Log.i("debug", "Error: " + ex.getMessage());
        }
        mdb.close();
    }
}