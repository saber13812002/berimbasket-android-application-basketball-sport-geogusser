package ir.berimbasket.app.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;


public class PrefManager extends SecureSharedPreferences {

    private static final String PREF_ID = "pref";
    private static final String KEY_DEVICE_ID = "deviceID";
    private static final String KEY_UPDATE_VERSION = "updateVersion";
    private static final String KEY_DOWNLOAD_APK_ID = "downloadApkID";
    private static final String KEY_DOWNLOAD_APK_FILE_NAME = "downloadApkFileName";
    private Context context;

    public PrefManager(Context context) {
        super(context, context.getSharedPreferences(PREF_ID, Context.MODE_PRIVATE));
        this.context = context;
    }

    private void putDeviceID(String deviceId) {
        edit().putString(KEY_DEVICE_ID, deviceId).apply();
    }

    @SuppressLint("HardwareIds")
    public String getDeviceID() {
        String deviceID = getString(KEY_DEVICE_ID, null);
        if (deviceID == null) {
            deviceID = Settings.Secure.getString(
                    context.getContentResolver(), Settings.Secure.ANDROID_ID);
            putDeviceID(deviceID);
            return deviceID;
        } else {
            return deviceID;
        }
    }

    public void putUpdateVersion(int version) {
        edit().putInt(KEY_UPDATE_VERSION, version).apply();
    }

    public int getUpdateVersion() {
        return getInt(KEY_UPDATE_VERSION, 1);
    }

    public void putDownloadApkID(long id) {
        edit().putLong(KEY_DOWNLOAD_APK_ID, id).apply();
    }

    public long getDownloadApkID() {
        return getLong(KEY_DOWNLOAD_APK_ID, 0);
    }

    public void putDownloadApkFileName(String fileName) {
        edit().putString(KEY_DOWNLOAD_APK_FILE_NAME, fileName).apply();
    }

    public String getDownloadApkFileName() {
        return getString(KEY_DOWNLOAD_APK_FILE_NAME, "");
    }
}