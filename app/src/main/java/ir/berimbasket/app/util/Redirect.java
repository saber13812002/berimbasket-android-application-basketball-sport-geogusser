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

import co.ronash.pushe.Pushe;
import ir.berimbasket.app.R;
import ir.berimbasket.app.ui.common.custom.AlertDialogCustom;
import ir.berimbasket.app.ui.common.custom.ToastCustom;
import ir.berimbasket.app.util.customtabs.CustomTabActivityHelper;

/**
 * Created by Mahdi on 9/20/2017.
 * redirect user to external activity
 */

public class Redirect {

    public static void sendToTelegram(Context context, String telegramUrl, Telegram type) throws IllegalArgumentException {
        switch (type) {
            case PUSHEID_BOT:
                String pusheId = Pushe.getPusheId(context);
                telegramUrl += "?start=" + pusheId;
                break;
        }

        String regex = "http(s?)://(www\\.)?(t|telegram)\\.me/.+";
        if (telegramUrl.matches(regex)) {
//            final String packageName = "org.telegram.messenger";
//            if (Redirect.isAppAvailable(context, packageName)) {
            try {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(telegramUrl));
//                    i.setPackage(packageName);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            } catch (ActivityNotFoundException e) {
                // do nothing
            }
//            } else {
//                sendToMarketToInstallApp(packageName, context.getString(R.string.general_dialog_message_telegram_not_found), context);
//            }
        } else {
            throw new IllegalArgumentException("Wrong telegram url");
        }
    }

    public static void sendToInstagram(Context context, String instagramUrl) throws IllegalArgumentException {
        String regex = "http(s?)://(www\\.)?(instagram)\\.com/.+";
        if (instagramUrl.matches(regex)) {
//            String packageName = "com.instagram.android";
//            if (Redirect.isAppAvailable(context, packageName)) {
            try {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(instagramUrl));
//                    i.setPackage(packageName);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            } catch (ActivityNotFoundException e) {
                // do nothing
            }
//            } else {
//                sendToMarketToInstallApp(packageName, context.getString(R.string.general_dialog_message_instagram_not_found), context);
//            }
        } else {
            throw new IllegalArgumentException("Wrong instagram url");
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

    public static void sendToCustomTab(final Activity activity, final String url, final String message, final Context context) {
        AlertDialogCustom alertDialogCustom = new AlertDialogCustom(context);
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setCustomTitle(alertDialogCustom.getTitleText(context.getString(R.string.general_dialog_title_notice)))
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.general_dialog_option_yes), (dialog1, which) -> sendToCustomTab(activity, url))
                .setNegativeButton(context.getString(R.string.general_dialog_option_no), (dialog1, which) -> dialog1.dismiss()).show();
        alertDialogCustom.setDialogStyle(dialog);
    }

    public static void sendToMarketToInstallApp(final String packageName, final String message, final Context context) {
        AlertDialogCustom customAlertDialog = new AlertDialogCustom(context);

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
                                new ToastCustom(context.getString(R.string.general_dialog_message_google_play_not_found), context).showToast(true);
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

        AlertDialogCustom customAlertDialog = new AlertDialogCustom(activity);
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setCustomTitle(customAlertDialog.getTitleText(activity.getString(R.string.general_dialog_title_guide)))
                .setMessage(activity.getString(R.string.general_dialog_message_download_manager_not_enabled) + " "
                        + activity.getString(R.string.general_and) + " " + "\n" +
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
                            new ToastCustom(activity.getString(R.string.general_dialog_message_download_manager_not_found), activity).showToast(true);
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

    public static void sendToMyketForComment(final Context context) {
        String url = "http://myket.ir/app/ir.berimbasket.app";
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }

    public static void sendToBazaarForComment(Context context) {
        String bazaarPackage = "com.farsitel.bazaar";
        try {
            Intent intent = new Intent(Intent.ACTION_EDIT);
            intent.setData(Uri.parse("bazaar://details?id=ir.berimbasket.app"));
            intent.setPackage(bazaarPackage);
            context.startActivity(intent);
        } catch (Exception e) {
            sendToMarketToInstallApp(bazaarPackage, context.getString(R.string.general_dialog_message_bazaar_not_found), context);
        }
    }
}
