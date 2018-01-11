package ir.berimbasket.app.ui.base;


import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;

import ir.berimbasket.app.util.AnalyticsHelper;

/**
 * Created by Mahdi on 12/28/2017.
 * Listen to callback of every activity
 */

public class BaseActivityLifeCycle implements ActivityLifecycleCallbacks {

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        // Track screen view (Analytics)
        String screenName = activity.getClass().getSimpleName();
        AnalyticsHelper.getInstance().trackScreenView(activity.getApplicationContext(), screenName);
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
