package com.jacksonasantos.travelplan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.LocaleList;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.google.android.material.navigation.NavigationView;
import com.jacksonasantos.travelplan.dao.CurrencyQuote;
import com.jacksonasantos.travelplan.dao.CurrencyQuoteResponse;
import com.jacksonasantos.travelplan.dao.InsuranceCompany;
import com.jacksonasantos.travelplan.dao.MaintenancePlan;
import com.jacksonasantos.travelplan.dao.MaintenancePlanHasVehicleType;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static Resources res;
    public static Context context;

    private AppBarConfiguration mAppBarConfiguration;

    // TODO - Amazement in Cloud
    public Database mDb;
    final Globals g = Globals.getInstance();

    // TODO - Authenticate ao APP
    // TODO - botão de filtro do veiculo verificar a variavel geral e mostrar o icone certo

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.menu.activity_main);

        context = getApplicationContext();

        res = getResources();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

        settingGlobals( res, settings );

        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.menu_home);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_vehicle, R.id.nav_fuel_supply, R.id.nav_maintenance_plan, R.id.nav_maintenance,
                R.id.nav_travel, R.id.nav_accommodation, R.id.nav_achievement,
                R.id.nav_settings,
                R.id.nav_insurance, R.id.nav_insurance_company, R.id.nav_broker, R.id.nav_currency_quote)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        InitialLoadCSV(getApplicationContext());

        mDb = new Database( this );
        mDb.open();
        List list = Database.mCurrencyQuoteDao.findCurrencyNoQuoteDay();
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                DownloadCurrencyQuote( Integer.parseInt(list.get(i).toString()) );
            }
        }
    }

    public static void DownloadCurrencyQuote(int currency_id) {
        String v_currency_abbrev = context.getResources().getStringArray(R.array.currency_abbrev_array)[currency_id];
        String url = String.format("https://economia.awesomeapi.com.br/json/%s", v_currency_abbrev);
        new DownloadJsonAsyncTask().execute(url, String.valueOf(currency_id));
    }

    static class DownloadJsonAsyncTask extends AsyncTask<String, Void, CurrencyQuoteResponse> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected CurrencyQuoteResponse doInBackground(String... params) {
            String urlString = params[0];
            int currencyId = Integer.parseInt(params[1]);

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(urlString);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();

                if (httpEntity != null) {
                    InputStream httpStream = httpEntity.getContent();
                    String jsonString = toString(httpStream);
                    httpStream.close();

                    CurrencyQuoteResponse objectQuote = new CurrencyQuoteResponse();
                    try {
                        JSONArray trendLists = new JSONArray(jsonString);
                        JSONObject trendList = trendLists.getJSONObject(0);

                        objectQuote.setCurrency_type(currencyId);
                        objectQuote.setBid((float) trendList.getDouble("bid"));
                        objectQuote.setCreate_date(Utils.stringToDateTime2(trendList.getString("create_date"), true));

                        CurrencyQuote currencyQuote = Database.mCurrencyQuoteDao.findDayQuote(objectQuote.getCurrency_type(), objectQuote.getCreate_date());
                        currencyQuote.setCurrency_type(objectQuote.getCurrency_type());
                        currencyQuote.setQuote_date(objectQuote.getCreate_date());
                        currencyQuote.setCurrency_value(objectQuote.getBid());
                        if (currencyQuote.getId() == null) {
                            Database.mCurrencyQuoteDao.addCurrencyQuote(currencyQuote);
                        } else {
                            Database.mCurrencyQuoteDao.updateCurrencyQuote(currencyQuote);
                        }

                    } catch (JSONException e) {
                        Log.i("debug", R.string.Currency_Quote_JSON_parsing_error +"/n"+ e.getMessage());
                    }
                    return objectQuote;
                }
            } catch (Exception e) {
                Log.i("debug", R.string.Currency_Quote_Failed_access_Web_Service +"/n"+ e.getMessage());
            }
            return null;
        }
        @Override
        protected void onPostExecute(CurrencyQuoteResponse result) {
            super.onPostExecute(result);
            if (result==null) {
                Toast.makeText(context, R.string.Currency_Quote_Unable_access_AwesomeAPI_information, Toast.LENGTH_SHORT).show();
            }
        }

        private String toString(InputStream is) throws IOException {
            byte[] bytes = new byte[1024];
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            int l;
            while ((l = is.read(bytes)) > 0) {
                b.write(bytes, 0, l);
            }
            return b.toString();
        }
    }

    public static Resources getAppResources() { return res; }

    public void InitialLoadCSV(Context ctx) {

        mDb = new Database( ctx );
        mDb.open();

        try {
            // Load of InsuranceCompany
            // TODO - Load files in directory in install app
            try {
                AssetManager assetManager = ctx.getAssets();
                InputStreamReader isr = new InputStreamReader(assetManager.open("seguradoras.csv"), StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(isr);
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
                            Toast.makeText(ctx, R.string.Error_Changing_Data + Arrays.toString(dataLine), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        if (!Database.mInsuranceCompanyDao.addInsuranceCompany(insuranceCompany)) {
                            Toast.makeText(ctx, R.string.Error_Including_Data + Arrays.toString(dataLine), Toast.LENGTH_LONG).show();
                        }
                    }
                }
                isr.close();
            } catch (IOException ex) {
                Log.i("debug", "Error load 'Seguradoras': " + ex.getMessage());
            }

            // Maintenance Plan Load
            try {
                AssetManager assetManager = ctx.getAssets();
                InputStreamReader is = new InputStreamReader(assetManager.open("service_type.csv"), StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(is);
                String lineStr;

                while ((lineStr = reader.readLine()) != null) {
                    String[] dataLine = lineStr.split(";");
                    MaintenancePlan maintenancePlan = Database.mMaintenancePlanDao.fetchMaintenancePlanByService(Integer.parseInt(dataLine[0]),dataLine[1]);
                    maintenancePlan.setService_type(Integer.parseInt(dataLine[0]));
                    maintenancePlan.setDescription(dataLine[1]);
                    int x;
                    switch(dataLine[2]) {
                        case "km":   x = 1; break;
                        case "dias": x = 2; break;
                        default:     x = 0;
                    }
                    maintenancePlan.setMeasure(x);
                    if (dataLine[3] != null && !dataLine[3].isEmpty()){
                        maintenancePlan.setExpiration_default(Integer.parseInt(dataLine[3].replace(".","")));
                    }
                    maintenancePlan.setRecommendation(dataLine[4]);
                    if (maintenancePlan.getId() != null) {
                        if (!Database.mMaintenancePlanDao.updateMaintenancePlan(maintenancePlan)) {
                            Toast.makeText(ctx, R.string.Error_Changing_Data + Arrays.toString(dataLine), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        if (Database.mMaintenancePlanDao.addMaintenancePlan(maintenancePlan)==null) {
                            Toast.makeText(ctx, R.string.Error_Including_Data + Arrays.toString(dataLine), Toast.LENGTH_LONG).show();
                        }
                    }
                }
                is.close();
            } catch (IOException ex) {
                Log.i("debug", "Error load 'Plano de Manutenção': " + ex.getMessage());
            }

            // Load data of plans maintenance for all vehicles types
            String[] array = getResources().getStringArray(R.array.vehicle_type_array);

            for (int i=1 ; i <= array.length ; i++){

                List<MaintenancePlan> maintenance_plan_list = Database.mMaintenancePlanDao.fetchAllMaintenancePlan();
                for ( MaintenancePlan maintenance_plan : maintenance_plan_list) {

                    MaintenancePlanHasVehicleType maintenancePlanHasVehicleType = Database.mMaintenancePlanHasVehicleTypeDAO.fetchMaintenancePlanHasVehicleTypeByMaintenancePlanIdVehicleId(maintenance_plan.getId(), i);
                    maintenancePlanHasVehicleType.setVehicle_type(i);
                    maintenancePlanHasVehicleType.setMaintenance_plan_id(maintenance_plan.getId());
                    maintenancePlanHasVehicleType.setRecurring_service(1);
                    if (maintenancePlanHasVehicleType.getId() != null) {
                        if (!Database.mMaintenancePlanHasVehicleTypeDAO.updateMaintenancePlanHasVehicleType(maintenancePlanHasVehicleType)) {
                            Toast.makeText(ctx, R.string.Error_Changing_Data , Toast.LENGTH_LONG).show();
                        }
                    } else {
                        if (!Database.mMaintenancePlanHasVehicleTypeDAO.addMaintenancePlanHasVehicleType(maintenancePlanHasVehicleType)) {
                            Toast.makeText(ctx, R.string.Error_Including_Data , Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }

        } catch (Exception x) {
            Log.i("debug", "Error : " + x.getMessage());
            finish();
        }
        mDb.close();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    public void settingGlobals(Resources res, SharedPreferences settings ) {
        Configuration config = res.getConfiguration();

        String vehicle_pref = settings.getString("vehicle_default", String.valueOf(0));
        if (!vehicle_pref.equals("0")) {
            g.setIdVehicle(Integer.valueOf(vehicle_pref));
        }
        else {
            // TODO - Adjust for when there is no defined preference
            g.setIdVehicle(1);
        }

        String travel_pref = settings.getString("travel_default", String.valueOf(0));
        if (!travel_pref.equals("0")) {
            g.setIdTravel(Integer.valueOf(travel_pref));
        }
        else {
            // TODO - Adjust for when there is no defined preference
            g.setIdTravel(1);
        }

        String lang = settings.getString("language", "");
        if (!lang.isEmpty() && !config.getLocales().get(0).getLanguage().equals(lang)) {
            g.setLanguage(lang);
            Locale myLocale = new Locale(lang);
            LocaleList localeList = new LocaleList(myLocale);
            LocaleList.setDefault(localeList);
            config.setLocales(localeList);
            DisplayMetrics dm = res.getDisplayMetrics();
            Locale.setDefault(myLocale);
            res.updateConfiguration(config, dm);
            g.setCountry(myLocale.getCountry());
        }
        // TODO - Create form to configure Global variables
        g.setIdCurrency(0);                // R$ de R.array.currency_array
        g.setMeasureCost("km");
        g.setMeasureIndexInMeter(1000);
        g.setMeasureCapacity("l");
        g.setMeasureConsumption(g.getMeasureCost()+"/"+g.getMeasureCapacity());
        g.setMeasureSpeed(g.getMeasureCost()+"/"+"h");
        g.setDateFormat("yyyy-MM-dd HH:mm:ss");

        g.setKMsPreviousAlert(1000);
        g.setDaysPreviousAlert(60);

        g.setExpectedValueRestaurant(100.00);
        g.setExpectedValueAccommodation(200.00);
        g.setExpectedValueToll(7.00);
        g.setExpectedValueTour(60.00);
        g.setExpectedValueLandmark(30.00);
    }
}