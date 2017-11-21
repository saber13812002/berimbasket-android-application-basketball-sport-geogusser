package ir.berimbasket.app.downloadmanager;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;

import java.io.File;

import ir.berimbasket.app.R;
import ir.berimbasket.app.util.PrefManager;
import ir.berimbasket.app.util.Redirect;
import ir.berimbasket.app.view.CustomToast;

/**
 * Created by Mahdi on 29/05/2016.
 * when download finishes this broadcast will be called
 */
public class DownloadBroadCast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        PrefManager prefs = new PrefManager(context);
        long savedDownloadIds = prefs.getDownloadApkID();
        String fileName = prefs.getDownloadApkFileName();

        Bundle extras = intent.getExtras();
        DownloadManager.Query q = new DownloadManager.Query();
        Long downloaded_id = extras.getLong(DownloadManager.EXTRA_DOWNLOAD_ID);

        if (savedDownloadIds == downloaded_id) { // so it is my file that has been completed

            q.setFilterById(downloaded_id);
            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Cursor c = manager.query(q);
            if (c.moveToFirst()) {
                int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                if (status == DownloadManager.STATUS_SUCCESSFUL) {

                    CustomToast toast = new CustomToast(context.getString(R.string.download_receiver_toast_download_successful), context);
                    toast.showToast(true);

                    prefs.putDownloadApkID(0);

                    Redirect.sendToInstallApk(context, new File(Environment.getExternalStorageDirectory() +
                            "/" + context.getString(R.string.download_apk_update_folder) + "/" + fileName));
                }
            }

            c.close();

        }
    }

}
