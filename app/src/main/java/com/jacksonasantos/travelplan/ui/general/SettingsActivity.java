package com.jacksonasantos.travelplan.ui.general;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.os.LocaleList;
import android.util.DisplayMetrics;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.jacksonasantos.travelplan.MainActivity;
import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.Globals;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_settings, new SettingsFragment()).commit();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            Database mDb = new Database(getContext());
            mDb.open();

            ListPreference langPref = findPreference("language");
            if (langPref != null) {
                langPref.setOnPreferenceChangeListener((preference, o) -> {
                    preference.setSummary(o.toString());
                    return true;
                });
            }
            ListPreference langPreference = findPreference("language");
            if (langPreference != null) {
                langPreference.setOnPreferenceChangeListener(languageChangeListener);
            }

            ListPreference vehicleList = findPreference("vehicle_default");
            Cursor v = Database.mVehicleDao.selectVehicles();
            List<String> vehicle_entries = new ArrayList<>();
            List<String> vehicle_entryValues = new ArrayList<>();
            if (v != null && v.moveToFirst()) {
                do {vehicle_entries.add(v.getString(1));
                    vehicle_entryValues.add(Integer.toString(v.getInt(0)));
                } while (v.moveToNext());
                v.close();
            }
            final CharSequence[] entryCharSeq = vehicle_entries.toArray(new CharSequence[0]);
            final CharSequence[] entryValChar = vehicle_entryValues.toArray(new CharSequence[0]);
            if ( vehicleList != null) {
                vehicleList.setEntries(entryCharSeq);
                vehicleList.setEntryValues(entryValChar);
            }

            ListPreference travelList = findPreference("travel_default");
            Cursor t = Database.mTravelDao.selectTravels();
            List<String> travel_entries = new ArrayList<>();
            List<String> travel_entryValues = new ArrayList<>();
            if(t != null && t.moveToFirst()){
                do {travel_entries.add(t.getString(1));
                    travel_entryValues.add(Integer.toString(t.getInt(0)));
                } while (t.moveToNext());
                t.close();
            }
            final CharSequence[] t_entryCharSeq = travel_entries.toArray(new CharSequence[0]);
            final CharSequence[] t_entryValChar = travel_entryValues.toArray(new CharSequence[0]);
            if (travelList != null) {
                travelList.setEntries(t_entryCharSeq);
                travelList.setEntryValues(t_entryValChar);
            }
        }

        final Preference.OnPreferenceChangeListener languageChangeListener = (preference, newValue) -> {
            if ("en".equals(newValue.toString())) {
                PreferenceManager.getDefaultSharedPreferences(requireContext()).edit().putString("LANG", "en").apply();
                setLocale("en");
            } else {
                PreferenceManager.getDefaultSharedPreferences(requireContext()).edit().putString("LANG", "pt").apply();
                setLocale("pt");
            }
            return true;
        };

        public void setLocale(String lang) {

            Globals g = Globals.getInstance();
            Resources res = getResources();
            Configuration config = res.getConfiguration();
            if (lang != null && !"".equals(lang) && !config.getLocales().get(0).getLanguage().equals(lang)) {
                g.setLanguage(lang);
                Locale myLocale = new Locale(lang);
                LocaleList localeList = new LocaleList(myLocale);
                LocaleList.setDefault(localeList);
                config.setLocales(localeList);
                DisplayMetrics dm = res.getDisplayMetrics();
                Locale.setDefault(myLocale);
                res.updateConfiguration(config, dm);
                Intent refresh = new Intent(getContext(), MainActivity.class);
                refresh.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(refresh);
            }
        }
    }
}