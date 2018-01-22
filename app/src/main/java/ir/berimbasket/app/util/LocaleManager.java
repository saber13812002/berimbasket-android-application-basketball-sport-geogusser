package ir.berimbasket.app.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

import ir.berimbasket.app.data.pref.PrefManager;

public class LocaleManager {

    public static Context setLocale(Context c) {
        String lang = new PrefManager(c).getSettingsPrefLangList();
        return updateResources(c, lang);
    }

    public static void changeLocale(Context c, String language) {
        new PrefManager(c).putSettingsPrefLangList(language);
        updateResources(c, language);
    }

    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.setLocale(locale);
        context = context.createConfigurationContext(config);
        return context;
    }

    public static Locale getLocale(Context context) {
        Configuration config = context.getResources().getConfiguration();
        return Build.VERSION.SDK_INT >= 24 ? config.getLocales().get(0) : config.locale;
    }
}