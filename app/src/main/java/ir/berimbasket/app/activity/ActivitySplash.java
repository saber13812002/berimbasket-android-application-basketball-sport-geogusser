package ir.berimbasket.app.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Locale;

import co.ronash.pushe.Pushe;
import ir.berimbasket.app.R;
import ir.berimbasket.app.downloadmanager.DownloadApkUpdate;
import ir.berimbasket.app.network.Connectivity;
import ir.berimbasket.app.network.HttpFunctions;
import ir.berimbasket.app.util.LocalChanger;
import ir.berimbasket.app.util.PrefManager;
import ir.berimbasket.app.view.CustomAlertDialog;

public class ActivitySplash extends AppCompatActivity {

    private static final String URL_API_UPDATE = "https://berimbasket.ir/app/update.php";
    private static final int KEY_WRITE_EXTERNAL_STORAGE = 1;
    // api update fields
    private int apkVersion;
    private String apkUrl;
    private String fileName;
    private String fileSizeByte;
    private String changeLog = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PrefManager pref = new PrefManager(getApplicationContext());
        if (!pref.getIntroPassed()) {
            Intent intent = new Intent(this, ActivityIntro.class);
            startActivity(intent);
        } else {
            LocalChanger localChanger = new LocalChanger();
            localChanger.changeLocal(pref.getSettingsPrefLangList(), getApplicationContext());

            setContentView(R.layout.activity_splash);

            boolean needForUpdate = pref.getSettingsPrefUpdateNotification();
            if (needForUpdate) {
                if (Connectivity.isConnected(this)) {
                    new UpdateTask().execute();
                } else {
                    Toast.makeText(this, getString(R.string.general_toast_no_internet), Toast.LENGTH_LONG).show();
                }
            } else {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        goToActivityHome();
                    }
                }, 2000);
            }
        }

    }

    private class UpdateTask extends AsyncTask<Void, Integer, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            try {
                // prepare url params
                String url_params = "";
                try {
                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    int version = pInfo.versionCode;
                    url_params = "package=" + getPackageName() + "&version=" + String.valueOf(version);
                } catch (PackageManager.NameNotFoundException e) {
                    // do nothing
                }
                PrefManager pref = new PrefManager(getApplicationContext());
                String pusheId = Pushe.getPusheId(getApplicationContext());
                String userName = pref.getUserName();
                url_params += "&username=" + userName + "&pusheid=" + pusheId;
                // call web service
                HttpFunctions httpFunctions = new HttpFunctions(HttpFunctions.RequestType.GET);
                String response = httpFunctions.makeServiceCall(URL_API_UPDATE + "?" + url_params);
                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(response);
                JsonObject root = element.getAsJsonObject();

                // parse json
                if (root != null) {
                    int code = root.get("code").getAsInt();
                    if (code == 200) {
                        return 200;  // ok
                    } else if (code == 300) {
                        apkVersion = root.get("version").getAsInt();
                        apkUrl = root.get("apk_url").getAsString();
                        fileName = root.get("file_name").getAsString();
                        fileSizeByte = root.get("file_size_byte").getAsString();
                        JsonArray chLog = root.get("change_log").getAsJsonArray();
                        if (chLog != null && chLog.size() > 0) {
                            for (int i = 0; i < chLog.size(); i++) {
                                changeLog += "* " + chLog.get(i).getAsString() + "\n";
                            }
                        }
                        return 300;  // update
                    } else {
                        return 400;  // error
                    }
                }
            } catch (Exception e) {
                // do nothing yet
            }
            return 400;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (result == 200 || result == 400) {
                goToActivityHome();
            } else if (result == 300) {
                final PrefManager prefs = new PrefManager(ActivitySplash.this);
                int savedVersion = prefs.getUpdateVersion();
                if (apkVersion > savedVersion) {

                    CustomAlertDialog customAlertDialog = new CustomAlertDialog(ActivitySplash.this);

                    AlertDialog dialog = new AlertDialog.Builder(ActivitySplash.this)
                            .setCustomTitle(customAlertDialog.getTitleText(getString(R.string.general_dialog_title_update)))
                            .setMessage(getString(R.string.general_dialog_message_update))
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.general_dialog_option_yes), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    if (Build.VERSION.SDK_INT >= 23) {
                                        // Here, thisActivity is the current activity
                                        if (ContextCompat.checkSelfPermission(ActivitySplash.this,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                                != PackageManager.PERMISSION_GRANTED) {

                                            ActivityCompat.requestPermissions(ActivitySplash.this,
                                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                    KEY_WRITE_EXTERNAL_STORAGE);

                                            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                                            // app-defined int constant. The callback method gets the
                                            // result of the request.
                                        } else {
                                            downloadApk();
                                        }
                                    } else {
                                        downloadApk();
                                    }
                                }
                            })
                            .setNegativeButton(getString(R.string.general_dialog_option_no), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    goToActivityHome();
                                }
                            })
                            .setNeutralButton(getString(R.string.general_dialog_option_dont_show_again), new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int id) {
                                    prefs.putUpdateVersion(apkVersion);
                                    dialog.dismiss();
                                    goToActivityHome();
                                }
                            })
                            .show();

                    customAlertDialog.setDialogStyle(dialog);
                } else {
                    goToActivityHome();
                }
            }
        }
    }

    private void goToActivityHome() {
        Intent intent = new Intent(ActivitySplash.this, ActivityHome.class);
        startActivity(intent);
        this.finish();
    }

    private void downloadApk() {
        // start update
        DownloadApkUpdate downloadApk = new DownloadApkUpdate(ActivitySplash.this, ActivitySplash.this);
        downloadApk.StartDownload(apkUrl, fileName, Integer.parseInt(fileSizeByte));
        // show change log
        if (changeLog != null && !changeLog.equals("")) {
            CustomAlertDialog customAlertDialog = new CustomAlertDialog(ActivitySplash.this);

            AlertDialog dialog = new AlertDialog.Builder(ActivitySplash.this)
                    .setCustomTitle(customAlertDialog.getTitleText(getString(R.string.general_dialog_title_change_log)))
                    .setMessage(changeLog)
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.general_dialog_option_close), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            goToActivityHome();
                        }
                    })
                    .show();

            customAlertDialog.setDialogStyle(dialog);
        } else {
            goToActivityHome();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case KEY_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    downloadApk();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    if (Locale.getDefault().getISO3Language().equals("fas")
                            || Locale.getDefault().getISO3Language().equals("per")
                            || Locale.getDefault().getISO3Language().equals("fa")
                            || Locale.getDefault().getISO3Language().equals("farsi")) {

                        CustomAlertDialog customAlertDialog = new CustomAlertDialog(ActivitySplash.this);

                        AlertDialog dialog = new AlertDialog.Builder(ActivitySplash.this)
                                .setCustomTitle(customAlertDialog.getTitleText(getString(R.string.general_dialog_title_guide)))
                                .setMessage(getString(R.string.permission_explain_write_external_storage_fa_phones))
                                .setCancelable(false)
                                .setPositiveButton(getString(R.string.general_dialog_option_settings), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        intent.setData(Uri.parse("package:" + getPackageName()));
                                        startActivity(intent);

                                    }
                                })
                                .show();

                        customAlertDialog.setDialogStyle(dialog);
                    } else {
                        CustomAlertDialog customAlertDialog = new CustomAlertDialog(ActivitySplash.this);

                        AlertDialog dialog = new AlertDialog.Builder(ActivitySplash.this)
                                .setCustomTitle(customAlertDialog.getTitleText(getString(R.string.general_dialog_title_guide)))
                                .setCancelable(false)
                                .setMessage(getString(R.string.permission_explain_write_external_storage_en_phones))
                                .setPositiveButton(getString(R.string.general_dialog_option_settings), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        intent.setData(Uri.parse("package:" + getPackageName()));
                                        startActivity(intent);

                                    }
                                })
                                .show();

                        customAlertDialog.setDialogStyle(dialog);
                    }

                }

            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
