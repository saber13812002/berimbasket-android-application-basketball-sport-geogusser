package ir.berimbasket.app.activity.fragment;


import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import ir.berimbasket.app.R;
import ir.berimbasket.app.util.SendTo;

public class FragmentSettings extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preference_settings);

        // FIXME: 9/20/2017 read keys from strings.xml
        Preference aboutUs = findPreference("about_us");
        Preference terms = findPreference("terms_and_services");
        Preference changeLog = findPreference("change_log");
        aboutUs.setOnPreferenceClickListener(this);
        terms.setOnPreferenceClickListener(this);
        changeLog.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        // FIXME: 9/20/2017 read keys from strings.xml
        // TODO: 9/20/2017 set correct webservice url
        switch (key) {
            case "about_us" :
                SendTo.sendToCustomTab(getActivity(), "https://www.google.com/search?q=about+us");
                return true;
            case "terms_and_services" :
                SendTo.sendToCustomTab(getActivity(), "https://www.google.com/search?q=terms+and+services");
                return true;
            case "change_log" :
                SendTo.sendToCustomTab(getActivity(), "https://www.google.com/search?q=changelog");
                return true;
        }
        return false;
    }
}