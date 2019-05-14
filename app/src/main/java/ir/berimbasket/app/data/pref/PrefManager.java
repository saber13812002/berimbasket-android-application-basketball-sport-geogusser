package ir.berimbasket.app.data.pref;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.preference.PreferenceManager;

import ir.berimbasket.app.R;

public class PrefManager extends SecureSharedPreferences {

    private static final String PREF_ID = "pref";
    private static final String KEY_DEVICE_ID = "deviceID";
    private static final String KEY_UPDATE_VERSION = "updateVersion";
    private static final String KEY_DOWNLOAD_APK_ID = "downloadApkID";
    private static final String KEY_DOWNLOAD_APK_FILE_NAME = "downloadApkFileName";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_INTRO_PASSED = "introPassed";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_START_MESSAGE_DISMISSED = "startMessageDismissed";
    private static final String KEY_DONATE_MESSAGE_DISMISSED = "donateMessageDismissed";
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

    public int getUserId() {
        return getInt(KEY_USER_ID, 1);
    }

    public void putDownloadApkID(long id) {
        edit().putLong(KEY_DOWNLOAD_APK_ID, id).apply();
    }

    public void putUserID(long id) {
        edit().putLong(KEY_USER_ID, id).apply();
    }

    public void putToken(String token) {
        edit().putString(KEY_TOKEN, token).apply();
    }

    public String getToken() {
        return getString(KEY_TOKEN, "null");
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

    public void putIsLoggedIn(boolean isLoggedIn) {
        edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply();
    }

    public boolean getIsLoggedIn() {
        return getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void putUserName(String userName) {
        edit().putString(KEY_USER_NAME, userName).apply();
    }

    public String getUserName() {
        return getString(KEY_USER_NAME, "null");
    }

    public void putPassword(String password) {
        edit().putString(KEY_PASSWORD, password).apply();
    }

    public String getPassword() {
        return getString(KEY_PASSWORD, "");
    }

    public boolean getSettingsPrefUpdateNotification() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getBoolean(context.getString(R.string.key_pref_update_notification), true);
    }

    public void putSettingsPrefUpdateNotification(boolean value) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(context.getString(R.string.key_pref_update_notification), value);
        editor.apply();
    }

    public String getSettingsPrefStateList() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(context.getString(R.string.key_pref_state_list), "0a0");  // 0a0 means based on phone location
    }

    public void putSettingsPrefStateList(String value) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(context.getString(R.string.key_pref_state_list), value);
        editor.apply();
    }

    public String getSettingsPrefLangList() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(context.getString(R.string.key_pref_lang_list), "fa");
    }

    public void putSettingsPrefLangList(String value) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(context.getString(R.string.key_pref_lang_list), value);
        editor.apply();
    }

    public boolean getSettingsPrefMessageNotification() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getBoolean(context.getString(R.string.key_pref_message_notification), true);
    }

    public void putSettingsPrefMessageNotification(boolean value) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(context.getString(R.string.key_pref_message_notification), value);
        editor.apply();
    }

    public boolean getIntroPassed() {
        return getBoolean(KEY_INTRO_PASSED, false);
    }

    public void putIntroPassed(boolean value) {
        edit().putBoolean(KEY_INTRO_PASSED, value).apply();
    }

    public boolean getStartMessagePassed() {
        return getBoolean(KEY_START_MESSAGE_DISMISSED, false);
    }

    public void putStartMessagePassed(boolean value) {
        edit().putBoolean(KEY_START_MESSAGE_DISMISSED, value).apply();
    }

    public boolean getDonateMessageDismissed() {
        return getBoolean(KEY_DONATE_MESSAGE_DISMISSED, false);
    }

    public void putDonateMessageDismissed(boolean value) {
        edit().putBoolean(KEY_DONATE_MESSAGE_DISMISSED, value).apply();
    }
}