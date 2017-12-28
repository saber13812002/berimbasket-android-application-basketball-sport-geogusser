package ir.berimbasket.app.update;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.File;

import ir.berimbasket.app.R;
import ir.berimbasket.app.data.network.Connectivity;
import ir.berimbasket.app.data.pref.PrefManager;
import ir.berimbasket.app.ui.common.custom.ToastCustom;
import ir.berimbasket.app.util.Redirect;

public class DownloadApkUpdate {

    private Activity activity;
    private Context context;

    private String DownloadTitle;
    private String fileDir = "";
    private String fileName;

    private long enqueue = 0;//this is download request id , at first set to zero for check download status
    private DownloadManager dm;

    private Request request;

    public DownloadApkUpdate(Activity ac, Context c) {
        this.activity = ac;
        this.fileDir = "/" + c.getString(R.string.download_apk_update_folder);
        this.context = c;
        DownloadTitle = activity.getResources().getString(R.string.app_name);
    }

    public void StartDownload(String url, String filename, int fileSizeByte) {
        if (downloadManagerIsEnable()) {
            this.fileName = filename;
            makeDir();
            initializeDownload(url);

            int FileSizeMB = fileSizeByte / 1024 / 1024;

            boolean storageAvailability;
            long availableSpace = getAvailableSpace();
            availableSpace = (availableSpace - 10);
            storageAvailability = (availableSpace > FileSizeMB);

            File myFile = new File(Environment.getExternalStorageDirectory() +
                    fileDir + "/" + fileName);

            if (myFile.exists() && enqueue == 0) {
                long savedDownloadIds = new PrefManager(context).getDownloadApkID();

                DownloadManager.Query q = new DownloadManager.Query();
                q.setFilterById(savedDownloadIds);
                DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                Cursor c = manager.query(q);

                if (c.getCount() != 0) {
                    c.moveToFirst();
                    int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    if (status != DownloadManager.STATUS_RUNNING)
                        promptInstallApk();
                } else
                    promptInstallApk();
            } else {
                if (Connectivity.isConnected(context)) {
                    if (storageAvailability) {
                        Download();
                    } else {
                        ToastCustom toast = new ToastCustom(context.getString(R.string.download_toast_no_enough_space_for_update), context);
                        toast.showToast(true);
                    }
                }
            }
        } else {
            Redirect.sendToEnableDownloadManager(activity);
        }
    }

    private void initializeDownload(String url) {
        dm = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
        request = new Request(Uri.parse(url));
        request.setAllowedNetworkTypes(Request.NETWORK_WIFI | Request.NETWORK_MOBILE)
                .setTitle(DownloadTitle)
                .setDescription(context.getString(R.string.general_please_wait))
                .setDestinationInExternalPublicDir(fileDir, fileName);
    }

    private void Download() {
        PrefManager prefs = new PrefManager(context);
        long savedDownloadIds = prefs.getDownloadApkID();

        DownloadManager.Query q = new DownloadManager.Query();
        q.setFilterById(savedDownloadIds);
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Cursor c = manager.query(q);

        boolean StartDownload = true;
        if (c != null) {
            if (c.getCount() != 0) {
                c.moveToFirst();
                int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                if (status == DownloadManager.STATUS_RUNNING)
                    StartDownload = false;
            }
        }

        if (StartDownload) {
            enqueue = dm.enqueue(request);
            prefs.putDownloadApkFileName(fileName);
            prefs.putDownloadApkID(enqueue);
        }

    }

    private boolean downloadManagerIsEnable() {
        int state = context.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");
        if (Build.VERSION.SDK_INT >= 18) {
            return (!(state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED));
        } else {
            return (!(state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER));
        }
    }

    private void promptInstallApk() {
        Redirect.sendToInstallApk(context, new File(Environment.getExternalStorageDirectory() + fileDir + "/" + fileName));
    }

    private void makeDir() {
        File direct = new File(Environment.getExternalStorageDirectory() + fileDir);
        if (!direct.exists()) {
            direct.mkdirs();
        }
    }

    private long getAvailableSpace() {
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
        long freeSize;
        if (Build.VERSION.SDK_INT >= 18) {
            freeSize = (statFs.getFreeBlocksLong() * statFs.getBlockSizeLong()) / 1048576;
        } else {
            freeSize = (statFs.getFreeBlocks() * statFs.getBlockSize()) / 1048576;
        }
        Log.d("Free Storage", Long.toString(freeSize));
        return freeSize;
    }

}
