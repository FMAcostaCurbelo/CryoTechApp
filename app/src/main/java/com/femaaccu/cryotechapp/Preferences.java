package com.femaaccu.cryotechapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.Toast;


public class Preferences extends PreferenceActivity {
    public static final String PREFERENCES = "preferences";
    public static final String LOCAL_CURRENCY = "local_currency";
    String local_currency;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        LoadSetting();


    }
    private void LoadSetting(){

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        ListPreference listpref = (ListPreference) findPreference("list");

        String orien = sharedPref.getString("list", "eur");
        local_currency = orien;

        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("list", local_currency);
        editor.apply();

        //Toast.makeText(Preferences.this, "Local Currency changed to \"EUR\""+local_currency, Toast.LENGTH_LONG).show();

        listpref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference pref, Object obj) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(Preferences.this);
                String items = (String) obj;
                if (pref.getKey().equals("list")){
                    local_currency = items;
                    SharedPreferences.Editor editor = sharedPref.edit();

                    editor.putString("list", local_currency);
                    editor.apply();
                }
                return true;
            }
        });
    }




}
