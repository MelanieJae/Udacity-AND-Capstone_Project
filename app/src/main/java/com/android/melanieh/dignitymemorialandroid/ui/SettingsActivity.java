package com.android.melanieh.dignitymemorialandroid.ui;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.melanieh.dignitymemorialandroid.R;

public class SettingsActivity extends AppCompatActivity {

    static Preference zipCodePref;
    static Preference providerPref;
    private static final String LOG_TAG = SettingsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

    }

    /*** Created by melanieh on 11/8/16. */

    public static class UserPreferenceFragment extends PreferenceFragment
            implements Preference.OnPreferenceChangeListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Add 'general' preferences, defined in the XML file
            addPreferencesFromResource(R.xml.pref_search);
            // For all preferences, attach an OnPreferenceChangeListener so the UI summary can be
            // updated when the preference changes.
            zipCodePref = findPreference(getString(R.string.pref_zip_code_key));
            providerPref = findPreference(getString(R.string.pref_provider_key));

            bindPreferenceSummaryToValue(zipCodePref);
            bindPreferenceSummaryToValue(providerPref);
        }

        /**
         * Attaches a listener so the summary is always updated with the preference value.
         * and activates the listener once to initialize the summary (so it shows up before the value
         * is changed.)
         */
        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            String defaultPrefValue = PreferenceManager
                    .getDefaultSharedPreferences(preference.getContext())
                    .getString(getPrefKey(preference), "");
            onPreferenceChange(preference, defaultPrefValue);

        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {

            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list (since they have separate labels/values).
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                if (prefIndex >= 0) {
                    preference.setSummary(listPreference.getEntries()[prefIndex]);
                }
            } else {
                // For other preferences, set the summary to the value's simple string representation.
                preference.setSummary(stringValue);

                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getPrefKey(preference), stringValue);
                editor.commit();
            }
            return true;
        }

        private String getPrefKey(Preference preference) {
            String prefDefaultKey;
            if (preference == zipCodePref) {
                prefDefaultKey = getResources().getString(R.string.pref_zip_code_key);
            } else {
                prefDefaultKey = getResources().getString(R.string.pref_provider_key);
            }
            return prefDefaultKey;
        }
    }

}
