package ir.berimbasket.app.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import co.ronash.pushe.Pushe;
import ir.berimbasket.app.R;
import ir.berimbasket.app.network.HttpFunctions;
import ir.berimbasket.app.util.ApplicationLoader;
import ir.berimbasket.app.util.PrefManager;
import ir.berimbasket.app.util.TypefaceManager;


public class ActivityRegister extends AppCompatActivity {

    private final String VERIFY_URL = "http://berimbasket.ir/bball/getSignupStatusByVerificationCodeAndMAC.php";
    private final String USER_URL = "http://berimbasket.ir/bball/getExistOrNotThisNewRequestedUsername.php";
    private final String REGISTER_URL = "http://berimbasket.ir/bball/setPasswordForThisUsername.php?mac=1a2b3c4d5e6f&username=allstar33&password=@ll$tar33";
    private final String USER_ERROR = "userError";
    private final String VERIFY_ERROR = "verifyError";
    private final String REGISTER_ERROR = "registerError";
    EditText edtUsername, edtVerifyCode, edtPassword, edtPasswordRepeat;
    TextInputLayout inputUsername, inputVerifyCode, inputPassword, inputPasswordRepeat;
    ProgressDialog pDialog;
    private AppCompatButton btnRegister;
    private TextView btnLoginActivity;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        initFonts();
        initListeners();
        showRobotDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tracking the screen view (Analytics)
        ApplicationLoader.getInstance().trackScreenView(getString(R.string.analytics_screen_register));
    }

    private void initViews() {
        btnRegister = (AppCompatButton) findViewById(R.id.btnRegister);
        btnLoginActivity = (TextView) findViewById(R.id.btnLoginActivity);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtVerifyCode = (EditText) findViewById(R.id.edtVerifyCode);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtPasswordRepeat = (EditText) findViewById(R.id.edtPasswordRepeat);
        inputVerifyCode = (TextInputLayout) findViewById(R.id.inputVerifyCode);
        inputUsername = (TextInputLayout) findViewById(R.id.inputUsername);
        inputPassword = (TextInputLayout) findViewById(R.id.inputPassword);
        inputPasswordRepeat = (TextInputLayout) findViewById(R.id.inputPasswordRepeat);
        pDialog = new ProgressDialog(ActivityRegister.this);
        edtVerifyCode.requestFocus();


        Drawable imgUser = getResources().getDrawable(R.drawable.ic_login_user);
        imgUser.setBounds(0, 0, 60, 60);
        edtUsername.setCompoundDrawables(imgUser, null, null, null);

        Drawable imgPassword = getResources().getDrawable(R.drawable.ic_login_lock);
        imgPassword.setBounds(0, 0, 60, 60);
        edtPassword.setCompoundDrawables(imgPassword, null, null, null);


        Drawable imgPasswordRepeat = getResources().getDrawable(R.drawable.ic_login_open_lock);
        imgPasswordRepeat.setBounds(0, 0, 60, 60);
        edtPasswordRepeat.setCompoundDrawables(imgPasswordRepeat, null, null, null);

        Drawable imgVerify = getResources().getDrawable(R.drawable.ic_login_zero);
        imgVerify.setBounds(0, 0, 60, 60);
        edtVerifyCode.setCompoundDrawables(imgVerify, null, null, null);
    }

    private void initFonts() {
        Typeface typeface = TypefaceManager.get(getApplicationContext(), getString(R.string.font_yekan));
        inputVerifyCode.setTypeface(typeface);
        inputUsername.setTypeface(typeface);
        inputPassword.setTypeface(typeface);
        inputPasswordRepeat.setTypeface(typeface);
    }

    private void initListeners() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edtUsername.setError(null);
                edtVerifyCode.setError(null);
                edtPassword.setError(null);
                edtPasswordRepeat.setError(null);

                if (edtVerifyCode.getText().toString().equals("")
                        || edtUsername.getText().toString().equals("")
                        || edtPassword.getText().toString().equals("")
                        || edtPasswordRepeat.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), getString(R.string.activity_register_toast_fill_all_fields), Toast.LENGTH_LONG).show();
                } else if (!edtPassword.getText().toString().equals(edtPasswordRepeat.getText().toString())) {
                    Toast.makeText(getApplicationContext(), getString(R.string.activity_register_toast_wrong_password), Toast.LENGTH_LONG).show();
                } else {
                    new ValidateUsername().execute();
                }

            }
        });


        btnLoginActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToLoginActivity = new Intent(ActivityRegister.this, ActivityLogin.class);
                startActivity(goToLoginActivity);
                ActivityRegister.this.finish();
            }
        });
    }


    private void showRobotDialog() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(ActivityRegister.this, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(ActivityRegister.this);
        }
        builder.setTitle(getString(R.string.general_dialog_title_register))
                .setMessage(getString(R.string.general_dialog_message_bot_register))
                .setPositiveButton(getString(R.string.general_dialog_option_bot_link), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent telegram = new Intent(Intent.ACTION_VIEW, Uri.parse("https://telegram.me/berimbasketbot"));
                        // Tracking Event (Analytics)
                        ApplicationLoader.getInstance().trackEvent(getString(R.string.analytics_category_registration)
                                , getString(R.string.analytics_action_telegram_bot), "");
                        startActivity(telegram);
                    }
                })
                .setNegativeButton(getString(R.string.general_dialog_option_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        ActivityRegister.this.finish();
                    }
                })
                .show();
    }

    private String completeUserUrl(String userUrl) {
        PrefManager pref = new PrefManager(getApplicationContext());
        String pusheId = Pushe.getPusheId(getApplicationContext());
        String deviceId = pref.getDeviceID();
        userUrl = userUrl + "?" + "mac=" + deviceId + "&username=" + edtUsername.getText().toString() + "&pusheid=" + pusheId;
        return userUrl;
    }

    private String completeVerifyUrl(String verifyUrl) {
        PrefManager pref = new PrefManager(getApplicationContext());
        String pusheId = Pushe.getPusheId(getApplicationContext());
        String userName = pref.getUserName();
        String deviceId = pref.getDeviceID();
        String code = edtVerifyCode.getText().toString();
        String urlParams = String.format("mac=%s&code=%s&pusheid=%s&username=%s", deviceId, code, pusheId, userName);
        verifyUrl = verifyUrl + "?" + urlParams;
        return verifyUrl;
    }

    private String completeRegisterUrl(String registerUrl) {
        PrefManager pref = new PrefManager(getApplicationContext());
        String pusheId = Pushe.getPusheId(getApplicationContext());
        registerUrl = registerUrl + pref.getDeviceID() + "&username=" + edtUsername.getText().toString()
                + "&password=" + edtPassword.getText().toString() + "&pusheid=" + pusheId;
        return registerUrl;
    }

    private class ValidateUsername extends AsyncTask<Void, Void, Bundle> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage(getString(R.string.general_progress_dialog_checking_info));
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Bundle doInBackground(Void... voids) {
            String userUrl = completeUserUrl(USER_URL);
            HttpFunctions httpFunctions = new HttpFunctions(HttpFunctions.RequestType.GET);
            String userJson = httpFunctions.makeServiceCall(userUrl);
            Bundle errorBundle = new Bundle();
            try {
                JSONArray arrayUser = new JSONArray(userJson);
                JSONObject userObj = arrayUser.getJSONObject(0);
                boolean userExist = (boolean) userObj.get("exist");
                if (userExist) {
                    errorBundle.putBoolean(USER_ERROR, true);
                } else {
                    String verifyUrl = completeVerifyUrl(VERIFY_URL);
                    String verifyJson = httpFunctions.makeServiceCall(verifyUrl);
                    JSONArray arrayVerify = new JSONArray(verifyJson);
                    JSONObject verifyObj = arrayVerify.getJSONObject(0);
                    boolean verifyState = (boolean) verifyObj.get("SignupStatus");
                    if (!verifyState) {
                        errorBundle.putBoolean(VERIFY_ERROR, true);
                    } else {
                        String registerUrl = completeRegisterUrl(REGISTER_URL);
                        String registerJson = httpFunctions.makeServiceCall(registerUrl);
                        JSONArray arrayRegister = new JSONArray(registerJson);
                        JSONObject registerObj = arrayRegister.getJSONObject(0);
                        boolean registerState = (boolean) registerObj.get("passwordset");
                        if (registerState) {
                            errorBundle.putBoolean(REGISTER_ERROR, false);
                        }
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return errorBundle;
        }

        @Override
        protected void onPostExecute(Bundle result) {
            super.onPostExecute(result);
            edtUsername.setError(getString(R.string.activity_register_edt_username_error));
            boolean userError = result.getBoolean(USER_ERROR, false);
            boolean verifyError = result.getBoolean(VERIFY_ERROR, false);
            boolean registerError = result.getBoolean(REGISTER_ERROR, true);

            if (userError) {
                edtUsername.setError(getString(R.string.activity_register_edt_username_error));
            }
            if (verifyError) {
                edtVerifyCode.setError(getString(R.string.activity_register_edt_verify_error));
            }
            if (!registerError) {
                Toast.makeText(getApplicationContext(), getString(R.string.activity_register_toast_register_successful), Toast.LENGTH_LONG).show();
                // Tracking Event (Analytics)
                ApplicationLoader.getInstance().trackEvent(getString(R.string.analytics_category_registration), getString(R.string.analytics_action_new_register), "");
                ActivityRegister.this.finish();
            }
            pDialog.cancel();
        }
    }
}
