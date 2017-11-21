package ir.berimbasket.app.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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

public class Redirect {

    public static void sendToTelegramChat(Context context, String chatUrl) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(chatUrl));
        final String packageName = "org.telegram.messenger";
        if (Redirect.isAppAvailable(context, packageName)) {
            try {
                i.setPackage(packageName);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            } catch (ActivityNotFoundException e) {
                // do nothing
            }
        } else {
            sendToMarketToInstallApp(packageName, context.getString(R.string.general_dialog_message_telegram_not_found), context);
        }
    }

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
                                    activity.getString(R.string.general_dialog_message_browser_not_found), activity.getApplicationContext());
                        }
                    }
                });
    }

    public static void sendToMarketToInstallApp(final String packageName, final String message, final Context context) {
        CustomAlertDialog customAlertDialog = new CustomAlertDialog(context);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setCustomTitle(customAlertDialog.getTitleText(context.getString(R.string.general_dialog_title_install_app)))
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.general_dialog_option_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {

                            Uri marketUri = Uri.parse("market://details?id=" + packageName);
                            context.startActivity(new Intent(Intent.ACTION_VIEW, marketUri));
                        } catch (ActivityNotFoundException e) {
                            try {
                                context.startActivity(new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)));
                            } catch (ActivityNotFoundException e2) {
                                new CustomToast(context.getString(R.string.general_dialog_message_google_play_not_found), context).showToast(true);
                            }
                        }
                    }
                })
                .setNegativeButton(context.getString(R.string.general_dialog_option_no), new DialogInterface.OnClickListener() {
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
                .setCustomTitle(customAlertDialog.getTitleText(activity.getString(R.string.general_dialog_title_guide)))
                .setMessage(activity.getString(R.string.general_dialog_message_download_manager_not_enabled) + " و " + "\n" +
                        (Redirect.isRtlLanguage() ? activity.getString(R.string.general_dialog_message_enable_download_manager_fa)
                                : activity.getString(R.string.general_dialog_message_enable_download_manager_en))
                        + "\n" + activity.getString(R.string.general_dialog_message_download_manager_restart))
                .setCancelable(false)
                .setPositiveButton(activity.getString(R.string.general_dialog_option_settings), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        String packageName = "com.android.providers.downloads";
                        try {
                            //Open the specific App Info page:
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:" + packageName));
                            activity.startActivity(intent);

                        } catch (ActivityNotFoundException e) {
                            new CustomToast(activity.getString(R.string.general_dialog_message_download_manager_not_found), activity).showToast(true);
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

    private static boolean isAppAvailable(Context context, String appName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(appName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}