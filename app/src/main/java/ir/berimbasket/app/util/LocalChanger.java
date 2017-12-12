package ir.berimbasket.app.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * Created by mohammad hosein on 12/12/2017.
 */

public class LocalChanger {

    public static final String LOCAL_CODE_PERSIAN = "fa";
    public static final String LOCAL_CODE_ENGLISH = "en";

    public void changeLocal(String localCode, Context context) {
        Locale locale = new Locale(localCode);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale);
        }
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

    }
}
