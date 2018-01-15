package ir.berimbasket.app.ui.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import ir.berimbasket.app.util.AnalyticsHelper;
import ir.berimbasket.app.util.LocaleManager;

/**
 * Created by Mahdi on 9/22/2017.
 * first place callback when application start's up
 * singleton class
 */

public class BaseApplication extends Application {

    private static BaseApplication instance;

    public static synchronized BaseApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        AnalyticsHelper.initialize(this);
        registerActivityLifecycleCallbacks(new BaseActivityLifeCycle());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleManager.setLocale(this);
    }

}