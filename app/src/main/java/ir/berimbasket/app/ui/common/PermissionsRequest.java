package ir.berimbasket.app.ui.common;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import ir.berimbasket.app.R;
import ir.berimbasket.app.ui.common.custom.AlertDialogCustom;

/**
 * Created by Mahdi on 7/28/2017.
 * all permission things goes here
 */

public class PermissionsRequest {

    private static final String PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String PERMISSION_READ_SMS = Manifest.permission.READ_SMS;
    private static final String PERMISSION_RECEIVE_SMS = Manifest.permission.RECEIVE_SMS;

    /**
     * @return True if permission granted , False if not and tries to get permission
     */
    public static boolean checkAccessFineLocationPermission(Context context, final Fragment fragment, int key) {
        return checkPermission(context, fragment, PERMISSION_ACCESS_FINE_LOCATION,
                context.getString(R.string.general_dialog_message_location_permission), key);
    }

    public static void checkReadSmsPermission(Context context, final Activity activity, int key) {
        checkPermission(context, activity, PERMISSION_READ_SMS, context.getString(R.string.general_dialog_message_sms_permission), key);
    }

    public static void checkReceiveSmsPermission(Context context, final Activity activity, int key) {
        checkPermission(context, activity, PERMISSION_RECEIVE_SMS, context.getString(R.string.general_dialog_message_sms_permission), key);
    }

    /**
     * @return True if permission granted , False if not and tries to get permission
     */
    public static boolean checkAccessFineLocationPermission(Context context, final Activity activity, int key) {
        return checkPermission(context, activity, PERMISSION_ACCESS_FINE_LOCATION,
                context.getString(R.string.general_dialog_message_location_permission), key);
    }


    /**
     * @param permission  used to grant from permission manager
     * @param explanation tell user why you need this privilege
     * @param key         permission developer defined key in order to use at callback method
     * @return True if permission granted , False if not and tries to get permission
     */
    private static boolean checkPermission(Context context, Activity activity, String permission, String explanation, int key) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                showAlertDialog(context, activity, explanation);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity,
                        new String[]{permission},
                        key);
            }
            return false;
        } else {
            return true;
        }
    }

    private static boolean checkPermission(Context context, Fragment fragment, String permission, String explanation, int key) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (fragment.shouldShowRequestPermissionRationale(permission)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                showAlertDialog(context, fragment.getActivity(), explanation);
            } else {
                // No explanation needed, we can request the permission.
                fragment.requestPermissions(new String[]{permission}, key);
            }
            return false;
        } else {
            return true;
        }
    }

    /**
     * this dialog guide user to app settings in order to grant permission manually
     *
     * @param explanation tell user why you need this privilege
     */
    private static void showAlertDialog(Context context, final Activity activity, String explanation) {
        AlertDialogCustom customAlertDialog = new AlertDialogCustom(context);
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setCustomTitle(customAlertDialog.getTitleText(context.getString(R.string.general_dialog_title_guide)))
                .setMessage(explanation)
                .setCancelable(true)
                .setPositiveButton(R.string.general_dialog_option_settings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + activity.getPackageName()));
                        activity.startActivity(intent);
                    }
                }).show();
        customAlertDialog.setDialogStyle(dialog);
    }
}