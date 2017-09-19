package ir.berimbasket.app.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;


public class PrefManager extends SecureSharedPreferences {

    private static final String PREF_ID = "pref";
    private static final String KEY_DEVICE_ID = "deviceID";
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
}