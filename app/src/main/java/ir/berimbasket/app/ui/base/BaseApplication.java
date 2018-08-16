package ir.berimbasket.app.ui.base;

import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDexApplication;

import com.anetwork.android.sdk.advertising.AnetworkAdvertising;

import ir.berimbasket.app.data.env.ConfigConstants;
import ir.berimbasket.app.util.AnalyticsHelper;
import ir.berimbasket.app.util.LocaleManager;

/**
 * Created by Mahdi on 9/22/2017.
 * first place callback when application start's up
 * singleton class
 */

public class BaseApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        AnetworkAdvertising.initialize(this, ConfigConstants.A_NETWORK_TOKEN);
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
