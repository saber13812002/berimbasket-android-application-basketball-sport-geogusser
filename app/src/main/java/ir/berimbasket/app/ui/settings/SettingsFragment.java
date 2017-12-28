package ir.berimbasket.app.ui.settings;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import ir.berimbasket.app.R;
import ir.berimbasket.app.data.pref.PrefManager;
import ir.berimbasket.app.ui.base.ApplicationLoader;
import ir.berimbasket.app.ui.splash.SplashActivity;
import ir.berimbasket.app.util.LocaleManager;
import ir.berimbasket.app.util.Redirect;

public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private final static String URL_PREFERENCE_HELP = "http://berimbasket.ir/help";
    private final static String URL_PREFERENCE_TERMS_AND_SERVICES = "http://berimbasket.ir/terms";
    private final static String URL_PREFERENCE_ABOUT_US = "http://berimbasket.ir/about";
    private final static String URL_PREFERENCE_CHANGE_LOG = "http://berimbasket.ir/changelog";
    private Preference help, aboutUs, terms, changeLog;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preference_settings);

        // set default value of language switch to device language
        String deviceLanguage = new PrefManager(getContext()).getSettingsPrefLangList();
        ListPreference langSwitch = (ListPreference) findPreference(getString(R.string.key_pref_lang_list));
        String[] langList = getResources().getStringArray(R.array.pref_lang_list_values);
        int index = 0;
        for (int i = 0; i < langList.length; i++) if (langList[i].equals(deviceLanguage)) index = i;
        langSwitch.setValueIndex(index);

        help = findPreference(getString(R.string.key_pref_help));
        aboutUs = findPreference(getString(R.string.key_pref_about_us));
        terms = findPreference(getString(R.string.key_pref_terms_and_services));
        changeLog = findPreference(getString(R.string.key_pref_change_log));
        help.setOnPreferenceClickListener(this);
        aboutUs.setOnPreferenceClickListener(this);
        terms.setOnPreferenceClickListener(this);
        changeLog.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        if (key.equals(help.getKey())) {
            // Tracking Event (Analytics)
            ApplicationLoader.getInstance().trackEvent(getString(R.string.analytics_category_settings), getString(R.string.analytics_action_help), "");
            Redirect.sendToCustomTab(getActivity(), URL_PREFERENCE_HELP);
            return true;
        } else if (key.equals(aboutUs.getKey())) {
            // Tracking Event (Analytics)
            ApplicationLoader.getInstance().trackEvent(getString(R.string.analytics_category_settings), getString(R.string.analytics_action_about), "");
            Redirect.sendToCustomTab(getActivity(), URL_PREFERENCE_ABOUT_US);
            return true;
        } else if (key.equals(terms.getKey())) {
            // Tracking Event (Analytics)
            ApplicationLoader.getInstance().trackEvent(getString(R.string.analytics_category_settings), getString(R.string.analytics_action_terms), "");
            Redirect.sendToCustomTab(getActivity(), URL_PREFERENCE_TERMS_AND_SERVICES);
            return true;
        } else if (key.equals(changeLog.getKey())) {
            // Tracking Event (Analytics)
            ApplicationLoader.getInstance().trackEvent(getString(R.string.analytics_category_settings), getString(R.string.analytics_action_change_log), "");
            Redirect.sendToCustomTab(getActivity(), URL_PREFERENCE_CHANGE_LOG);
            return true;
        }
        return false;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getResources().getString(R.string.key_pref_lang_list))) {
            String newLang = new PrefManager(getContext()).getSettingsPrefLangList();
            LocaleManager.changeLocale(getContext(), newLang);

            Intent i = new Intent(getActivity(), SplashActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}