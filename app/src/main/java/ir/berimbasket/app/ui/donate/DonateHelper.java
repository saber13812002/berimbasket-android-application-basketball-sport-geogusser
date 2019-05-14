package ir.berimbasket.app.ui.donate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public interface DonateHelper {
    void initialize(Context context); // call this in onCreate()
    void donate(Activity activity, Donate donate, DonateCallback callback);
    boolean isActivityResultHandled(int requestCode, int resultCode, Intent data); // call this in onActivityResult()
    void disposeService(); // call this in onDestroy()
}