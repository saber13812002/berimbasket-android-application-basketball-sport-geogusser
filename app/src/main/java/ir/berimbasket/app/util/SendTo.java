package ir.berimbasket.app.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.util.Locale;

import ir.berimbasket.app.R;
import ir.berimbasket.app.util.customtabs.CustomTabActivityHelper;
import ir.berimbasket.app.view.CustomAlertDialog;
import ir.berimbasket.app.view.CustomToast;

/**
 * Created by Mahdi on 9/20/2017.
 * redirect user to external activity
 */

public class SendTo {

    public static void sendToInstallApk(Context context, File apk) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
            Uri apkUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", apk);
            intent.setData(apkUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(intent);
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri apkUri = Uri.fromFile(apk);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    public static void sendToCustomTab(Activity activity, String url) {
        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
        CustomTabActivityHelper.openCustomTab(activity, customTabsIntent, Uri.parse(url),
                new CustomTabActivityHelper.CustomTabFallback() {
                    @Override
                    public void openUri(Activity activity, Uri uri) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            activity.startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            sendToMarketToInstallApp("com.android.chrome",
                                    activity.getString(R.string.browser_not_found_install_now), activity);
                        }
                    }
                });
    }

    public static void sendToMarketToInstallApp(final String packageName, final String message, final Activity activity) {
        CustomAlertDialog customAlertDialog = new CustomAlertDialog(activity);

        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setCustomTitle(customAlertDialog.getTitleText(activity.getString(R.string.install_app)))
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(activity.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {

                            Uri marketUri = Uri.parse("market://details?id=" + packageName);
                            activity.startActivity(new Intent(Intent.ACTION_VIEW, marketUri));
                        } catch (ActivityNotFoundException e) {
                            try {
                                activity.startActivity(new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)));
                            } catch (ActivityNotFoundException e2) {
                                new CustomToast(activity.getString(R.string.no_google_play_found), activity).showToast(true);
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

    public static void sendToEnableDownloadManager(final Activity activity) {

        CustomAlertDialog customAlertDialog = new CustomAlertDialog(activity);
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setCustomTitle(customAlertDialog.getTitleText(activity.getString(R.string.guide)))
                .setMessage(activity.getString(R.string.enable_download_manager_guide) + " و " + "\n" +
                        (SendTo.isRtlLanguage() ? activity.getString(R.string.enable_download_manager_persian)
                                : activity.getString(R.string.enable_download_manager_english))
                        + "\n" + activity.getString(R.string.restart_your_device))
                .setCancelable(false)
                .setPositiveButton(activity.getString(R.string.settings), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        String packageName = "com.android.providers.downloads";
                        try {
                            //Open the specific App Info page:
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:" + packageName));
                            activity.startActivity(intent);

                        } catch (ActivityNotFoundException e) {
                            new CustomToast(activity.getString(R.string.download_manager_not_found), activity).showToast(true);
                        }
                    }
                })
                .show();
        customAlertDialog.setDialogStyle(dialog);
    }

    private static boolean isRtlLanguage() {
        return (Locale.getDefault().getISO3Language().equals("fas")
                || Locale.getDefault().getISO3Language().equals("per")
                || Locale.getDefault().getISO3Language().equals("fa")
                || Locale.getDefault().getISO3Language().equals("ar")
                || Locale.getDefault().getISO3Language().equals("ara"));
    }
}
