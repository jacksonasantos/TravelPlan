package com.jacksonasantos.travelplan;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

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
        }

        Preference.OnPreferenceChangeListener languageChangeListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                switch (newValue.toString()) {
                    case "en":
                        PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putString("LANG", "en").apply();
                        setLocale("en");
                        break;
                    default:
                        PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putString("LANG", "pt").apply();
                        setLocale("pt");
                        break;
                }
                return true;
            }
        };

        public void setLocale(String lang) {
            Resources res = getResources();
            Configuration config = res.getConfiguration();
            if (lang != null && !"".equals(lang) && !config.locale.getLanguage().equals(lang)) {
                Locale myLocale = new Locale(lang);
                config.locale = myLocale;
                DisplayMetrics dm = res.getDisplayMetrics();
                Locale.setDefault(myLocale);
                res.updateConfiguration(config, dm);
                Intent refresh = new Intent(getContext(), MainActivity.class);;
                refresh.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(refresh);
            }
        }
    }
}