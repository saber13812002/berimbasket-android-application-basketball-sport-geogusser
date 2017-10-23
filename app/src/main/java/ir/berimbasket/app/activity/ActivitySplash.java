package ir.berimbasket.app.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Locale;

import ir.berimbasket.app.R;
import ir.berimbasket.app.downloadmanager.DownloadApkUpdate;
import ir.berimbasket.app.network.Connectivity;
import ir.berimbasket.app.network.HttpFunctions;
import ir.berimbasket.app.util.PrefManager;
import ir.berimbasket.app.util.TypefaceManager;
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
        setContentView(R.layout.activity_splash);

        Typeface typeface = TypefaceManager.get(getApplicationContext(), getString(R.string.font_yekan));
        TextView txtSplashLoading = (TextView) findViewById(R.id.txtSplash_loading);
        txtSplashLoading.setTypeface(typeface);

        PrefManager pref = new PrefManager(getApplicationContext());
        boolean needForUpdate = pref.getSettingsPrefUpdateNotification();
        if (needForUpdate) {
            if (Connectivity.isConnected(this)) {
                new UpdateTask().execute();
            } else {
                Toast.makeText(this, "اینترنت در دسترس نیست", Toast.LENGTH_LONG).show();
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
                            .setCustomTitle(customAlertDialog.getTitleText("به روز رسانی"))
                            .setMessage("نسخه ی جدید موجود است ، اکنون نصب میکنید ؟")
                            .setCancelable(false)
                            .setPositiveButton("بله", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    if (Build.VERSION.SDK_INT >= 23) {
                                        // Here, thisActivity is the current activity
                                        if (ContextCompat.checkSelfPermission(ActivitySplash.this,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                                != PackageManager.PERMISSION_GRANTED){

                                            ActivityCompat.requestPermissions(ActivitySplash.this,
                                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                    KEY_WRITE_EXTERNAL_STORAGE);

                                            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                                            // app-defined int constant. The callback method gets the
                                            // result of the request.
                                        }
                                        else {
                                            downloadApk();
                                        }
                                    } else {
                                        downloadApk();
                                    }
                                }
                            })
                            .setNegativeButton("خیر", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    goToActivityHome();
                                }
                            })
                            .setNeutralButton("دیگر نشان نده", new DialogInterface.OnClickListener() {

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

    private void downloadApk(){
        // start update
        DownloadApkUpdate downloadApk = new DownloadApkUpdate(ActivitySplash.this, ActivitySplash.this);
        downloadApk.StartDownload(apkUrl, fileName, Integer.parseInt(fileSizeByte));
        // show change log
        if (changeLog != null && !changeLog.equals("")) {
            CustomAlertDialog customAlertDialog = new CustomAlertDialog(ActivitySplash.this);

            AlertDialog dialog = new AlertDialog.Builder(ActivitySplash.this)
                    .setCustomTitle(customAlertDialog.getTitleText("تغییرات نسخه ی جدید"))
                    .setMessage(changeLog)
                    .setCancelable(false)
                    .setPositiveButton("بستن", new DialogInterface.OnClickListener() {
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
                                .setCustomTitle(customAlertDialog.getTitleText("راهنمایی"))
                                .setMessage(getString(R.string.write_external_storage_guide_persian_phones))
                                .setCancelable(false)
                                .setPositiveButton("تنظیمات", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        intent.setData(Uri.parse("package:" + getPackageName()));
                                        startActivity(intent);

                                    }
                                })
                                .show();

                        customAlertDialog.setDialogStyle(dialog);
                    }
                    else{
                        CustomAlertDialog customAlertDialog = new CustomAlertDialog(ActivitySplash.this);

                        AlertDialog dialog = new AlertDialog.Builder(ActivitySplash.this)
                                .setCustomTitle(customAlertDialog.getTitleText("راهنمایی"))
                                .setCancelable(false)
                                .setMessage(getString(R.string.write_external_storage_guide_english_phones))
                                .setPositiveButton("تنظیمات", new DialogInterface.OnClickListener() {
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
