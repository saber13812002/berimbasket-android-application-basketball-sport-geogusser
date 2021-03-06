package ir.berimbasket.app.util;

import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;

import ir.berimbasket.app.R;

/**
 * A collection of Google Analytics trackers. Fetch the tracker you need using
 * {@code AnalyticsHelper.getInstance().get(...)}
 * <p/>
 * This code was generated by Android Studio but can be safely modified by
 * hand at this point.
 * <p/>
 * before using this!
 */
public final class AnalyticsHelper {
 
    private static AnalyticsHelper instance;
    private Tracker tracker;
 
    public static synchronized AnalyticsHelper getInstance() {
        if (instance == null) {
            instance = new AnalyticsHelper();
        }
        return instance;
    }
 
    /**
     * Don't instantiate directly - use {@link #getInstance()} instead.
     */
    private AnalyticsHelper(){
    }
 
    public static void initialize(Context context) {
        getInstance().tracker = GoogleAnalytics.getInstance(context).newTracker(R.xml.app_tracker);
    }

    /***
     * Google Analytics
     * Tracking screen view
     *
     * @param screenName screen name to be displayed on GA dashboard
     */
    public void trackScreenView(Context context, String screenName) {
        if (tracker != null) {
            // Set screen name.
            tracker.setScreenName(screenName);

            // Send a screen view.
            tracker.send(new HitBuilders.ScreenViewBuilder().build());

            GoogleAnalytics.getInstance(context).dispatchLocalHits();
        }
    }

    /***
     * Google Analytics
     * Tracking exception
     *
     * @param e exception to be tracked
     */
    public void trackException(Context context, Exception e) {
        if (e != null && tracker != null) {
            tracker.send(new HitBuilders.ExceptionBuilder()
                    .setDescription(
                            new StandardExceptionParser(context, null)
                                    .getDescription(Thread.currentThread().getName(), e))
                    .setFatal(false)
                    .build()
            );
        }
    }

    /***
     * Google Analytics
     * Tracking event
     *
     * @param category event category
     * @param action   action of the event
     * @param label    label
     */
    public void trackEvent(String category, String action, String label) {
        if (tracker != null) {
            // Build and send an Event.
            tracker.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());
        }
    }
}