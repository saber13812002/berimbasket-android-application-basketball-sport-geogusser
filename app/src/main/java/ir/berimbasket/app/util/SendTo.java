package ir.berimbasket.app.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;

import ir.berimbasket.app.R;
import ir.berimbasket.app.util.customtabs.CustomTabActivityHelper;
import ir.berimbasket.app.view.CustomAlertDialog;
import ir.berimbasket.app.view.CustomToast;

/**
 * Created by Mahdi on 9/20/2017.
 * redirect user to external activity
 */

public class SendTo {

    public static void sendToCustomTab(Activity activity, String url){
        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
        CustomTabActivityHelper.openCustomTab(activity, customTabsIntent, Uri.parse(url),
                new CustomTabActivityHelper.CustomTabFallback() {
                    @Override
                    public void openUri(Activity activity, Uri uri) {
                        try{
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            activity.startActivity(intent);
                        }catch (ActivityNotFoundException e){
                            sendToMarketToInstallApp("com.android.chrome",
                                    activity.getString(R.string.browser_not_found_install_now),activity);
                        }
                    }
                });
    }

    public static void sendToMarketToInstallApp(final String packageName,final String message,final Activity activity){
        CustomAlertDialog customAlertDialog = new CustomAlertDialog(activity);

        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setCustomTitle(customAlertDialog.getTitleText(activity.getString(R.string.install_app)))
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(activity.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try{

                            Uri marketUri = Uri.parse("market://details?id=" + packageName);
                            activity.startActivity(new Intent(Intent.ACTION_VIEW, marketUri));
                        } catch (ActivityNotFoundException e) {
                            try{
                                activity.startActivity(new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)));
                            }catch (ActivityNotFoundException e2){
                                new CustomToast(activity.getString(R.string.no_google_play_found),activity).showToast(true);
                            }
                        }
                    }
                })
                .setNegativeButton(activity.getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .show();
        customAlertDialog.setDialogStyle(dialog);
    }
}
