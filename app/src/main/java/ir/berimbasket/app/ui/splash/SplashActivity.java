package ir.berimbasket.app.ui.splash;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.util.Locale;

import co.ronash.pushe.Pushe;
import ir.berimbasket.app.R;
import ir.berimbasket.app.data.network.Connectivity;
import ir.berimbasket.app.data.network.WebApiClient;
import ir.berimbasket.app.data.network.model.Update;
import ir.berimbasket.app.data.pref.PrefManager;
import ir.berimbasket.app.ui.base.BaseActivity;
import ir.berimbasket.app.ui.common.custom.AlertDialogCustom;
import ir.berimbasket.app.ui.home.HomeActivity;
import ir.berimbasket.app.ui.intro.IntroActivity;
import ir.berimbasket.app.update.DownloadApkUpdate;
import ir.berimbasket.app.util.LocaleManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends BaseActivity {

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
        if (false) { //!pref.getIntroPassed()
            Intent intent = new Intent(this, IntroActivity.class);
            startActivity(intent);
        } else {
            setContentView(R.layout.activity_splash);

            boolean needForUpdate = pref.getSettingsPrefUpdateNotification();
            if (needForUpdate) {
                if (Connectivity.isConnected(this)) {
                    updateTask();
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

    private void updateTask() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            PrefManager pref = new PrefManager(getApplicationContext());
            String packageName = getPackageName();
            String version = String.valueOf(pInfo.versionCode);
            String userName = pref.getUserName();
            String pusheId = Pushe.getPusheId(getApplicationContext());
            String lang = LocaleManager.getLocale(getApplicationContext()).getLanguage();
            WebApiClient.getUpdateApi(getApplicationContext()).checkForUpdate(packageName, version, userName, pusheId, lang).enqueue(new Callback<Update>() {
                @Override
                public void onResponse(Call<Update> call, Response<Update> response) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        Update update = response.body();
                        if (update != null) {
                            if (update.getCode() == 200 || update.getCode() == 400) {
                                goToActivityHome();
                            } else if (update.getCode() == 300) {
                                apkVersion = update.getVersion();
                                apkUrl = update.getApkUrl();
                                fileName = update.getFileName();
                                fileSizeByte = update.getFileSizeByte();
                                if (update.getChangeLog() != null && update.getChangeLog().size() > 0) {
                                    for (int i = 0; i < update.getChangeLog().size(); i++) {
                                        changeLog += "* " + update.getChangeLog().get(i) + "\n";
                                    }
                                }
                                final PrefManager prefs = new PrefManager(SplashActivity.this);
                                int savedVersion = prefs.getUpdateVersion();
                                if (apkVersion > savedVersion) {

                                    AlertDialogCustom customAlertDialog = new AlertDialogCustom(SplashActivity.this);

                                    AlertDialog dialog = new AlertDialog.Builder(SplashActivity.this)
                                            .setCustomTitle(customAlertDialog.getTitleText(getString(R.string.general_dialog_title_update)))
                                            .setMessage(getString(R.string.general_dialog_message_update))
                                            .setCancelable(false)
                                            .setPositiveButton(getString(R.string.general_dialog_option_yes), new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    if (Build.VERSION.SDK_INT >= 23) {
                                                        // Here, thisActivity is the current activity
                                                        if (ContextCompat.checkSelfPermission(SplashActivity.this,
                                                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                                                != PackageManager.PERMISSION_GRANTED) {

                                                            ActivityCompat.requestPermissions(SplashActivity.this,
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
                }

                @Override
                public void onFailure(Call<Update> call, Throwable t) {

                }
            });
        } catch (PackageManager.NameNotFoundException e) {
            // do nothing
        }
    }

    private void goToActivityHome() {
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void downloadApk() {
        // start update
        DownloadApkUpdate downloadApk = new DownloadApkUpdate(SplashActivity.this, SplashActivity.this);
        downloadApk.StartDownload(apkUrl, fileName, Integer.parseInt(fileSizeByte));
        // show change log
        if (changeLog != null && !changeLog.equals("")) {
            AlertDialogCustom customAlertDialog = new AlertDialogCustom(SplashActivity.this);

            AlertDialog dialog = new AlertDialog.Builder(SplashActivity.this)
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

                        AlertDialogCustom customAlertDialog = new AlertDialogCustom(SplashActivity.this);

                        AlertDialog dialog = new AlertDialog.Builder(SplashActivity.this)
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
                        AlertDialogCustom customAlertDialog = new AlertDialogCustom(SplashActivity.this);

                        AlertDialog dialog = new AlertDialog.Builder(SplashActivity.this)
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
