package ir.berimbasket.app.activity.fragment;


import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import ir.berimbasket.app.R;
import ir.berimbasket.app.util.SendTo;

public class FragmentSettings extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener {

    private final static String URL_PREFERENCE_HELP = "http://berimbasket.ir/help";
    private final static String URL_PREFERENCE_TERMS_AND_SERVICES = "http://berimbasket.ir/terms";
    private final static String URL_PREFERENCE_ABOUT_US = "http://berimbasket.ir/about";
    private final static String URL_PREFERENCE_CHANGE_LOG = "http://berimbasket.ir/changelog";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preference_settings);

        // FIXME: 9/20/2017 read keys from strings.xml
        Preference help = findPreference("help");
        Preference aboutUs = findPreference("about_us");
        Preference terms = findPreference("terms_and_services");
        Preference changeLog = findPreference("change_log");
        help.setOnPreferenceClickListener(this);
        aboutUs.setOnPreferenceClickListener(this);
        terms.setOnPreferenceClickListener(this);
        changeLog.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        // FIXME: 9/20/2017 read keys from strings.xml
        switch (key) {
            case "help" :
                SendTo.sendToCustomTab(getActivity(), URL_PREFERENCE_HELP);
                return true;
            case "about_us" :
                SendTo.sendToCustomTab(getActivity(), URL_PREFERENCE_ABOUT_US);
                return true;
            case "terms_and_services" :
                SendTo.sendToCustomTab(getActivity(), URL_PREFERENCE_TERMS_AND_SERVICES);
                return true;
            case "change_log" :
                SendTo.sendToCustomTab(getActivity(), URL_PREFERENCE_CHANGE_LOG);
                return true;
        }
        return false;
    }
}