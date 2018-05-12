package richadx.com.compassdemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Locale;

public class SettingActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String KEY_PREF_LANGUAGE = "key_font";
    public String languagePref_ID;
    String beforeEnable;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        addPreferencesFromResource(R.xml.pref_main);

        //checkbox
        final Preference checkboxPref =  getPreferenceManager()
                .findPreference("checkbox_pref");

     checkboxPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
         @Override
         public boolean onPreferenceClick(Preference preference) {
             startActivity( new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
             return false;
         }
     });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
       /* if (key.equals(KEY_PREF_LANGUAGE)) {
            languagePref_ID = sharedPreferences.getString(SettingActivity.KEY_PREF_LANGUAGE, "2");
            switch (languagePref_ID) {
                case "1":
                    Locale localeEN = new Locale("en_US");
                    setLocale(localeEN);
                    break;
                case "2":
                    Locale localeHU = new Locale("vi-VN");
                    setLocale(localeHU);
                    break;
            }
        }*/
    }

   /* public void setLocale(Locale locale) {
        Locale.setDefault(locale);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = locale;
        res.updateConfiguration(conf, dm);
        recreate();
    }*/





}