package ir.berimbasket.app.activity.fragment;


import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import ir.berimbasket.app.R;

public class FragmentSettings extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preference_settings);
    }
}