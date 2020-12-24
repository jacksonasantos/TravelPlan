package com.jacksonasantos.travelplan.ui.general;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.jacksonasantos.travelplan.MainActivity;
import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Database;
import com.jacksonasantos.travelplan.ui.utility.Globals;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

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

            ListPreference langPref = findPreference("language");
            assert langPref != null;
            langPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    preference.setSummary(o.toString());
                    return true;
                }
            });
            ListPreference langPreference = findPreference("language");
            assert langPreference != null;
            langPreference.setOnPreferenceChangeListener(languageChangeListener);

            ListPreference vehicleList = findPreference("vehicle_default");
            Cursor v = Database.mVehicleDao.selectVehicles();
            List<String> entries = new ArrayList<String>();
            List<String> entryValues = new ArrayList<String>();
            if(v != null && v.moveToFirst()){
                do {entries.add(v.getString(1));
                    entryValues.add(Integer.toString(v.getInt(0)));
                } while (v.moveToNext());
            }
            assert v != null;
            v.close();
            final CharSequence[] entryCharSeq = entries.toArray(new CharSequence[entries.size()]);
            final CharSequence[] entryValsChar = entryValues.toArray(new CharSequence[entryValues.size()]);
            vehicleList.setEntries(entryCharSeq);
            vehicleList.setEntryValues(entryValsChar);
        }

        Preference.OnPreferenceChangeListener languageChangeListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if ("en".equals(newValue.toString())) {
                    PreferenceManager.getDefaultSharedPreferences(requireContext()).edit().putString("LANG", "en").apply();
                    setLocale("en");
                } else {
                    PreferenceManager.getDefaultSharedPreferences(requireContext()).edit().putString("LANG", "pt").apply();
                    setLocale("pt");
                }
                return true;
            }
        };

        public void setLocale(String lang) {

            Globals g = Globals.getInstance();
            Resources res = getResources();
            Configuration config = res.getConfiguration();
            if (lang != null && !"".equals(lang) && !config.locale.getLanguage().equals(lang)) {
                g.setLanguage(lang);
                Locale myLocale = new Locale(lang);
                config.locale = myLocale;
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