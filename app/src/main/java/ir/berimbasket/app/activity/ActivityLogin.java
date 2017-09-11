package ir.berimbasket.app.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ir.berimbasket.app.R;
import ir.berimbasket.app.json.HttpFunctions;

/**
 * A login screen that offers login via email/password.
 */
public class ActivityLogin extends AppCompatActivity {

    private final String LOGIN_URL = "http://imenservice.com/bball/getStatusLoginByUsernamePassword.php?mac=";
    AppCompatButton btnLogin;
    TextView btnRegisterPage;
    ProgressDialog pDialog;
    // UI references.
    private EditText edtUsername, edtPassword;
    TextInputLayout inputUsername, inputPassword;
    private String PREFS_NAME = "BERIM_BASKET_PREF";
    private String USERNAME = "PREF_USERNAME";
    private String PASSWORD = "PREF_PASSWORD";
    private String ATTEMPT_LOGIN = "PREF_ATTEMPT_LOGIN";


    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        initFonts();
        initListeners();

    }

    private void initViews() {
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        inputUsername = (TextInputLayout) findViewById(R.id.inputUsername);
        inputPassword = (TextInputLayout) findViewById(R.id.inputPassword);
        btnLogin = (AppCompatButton) findViewById(R.id.btnLogin);
        btnRegisterPage = (TextView) findViewById(R.id.btnRegActivity);
        pDialog = new ProgressDialog(ActivityLogin.this);

        Drawable imgUser = getResources().getDrawable( R.drawable.ic_login_user);
        imgUser.setBounds( 0, 0, 60, 60 );
        edtUsername.setCompoundDrawables( imgUser, null, null, null );

        Drawable imgPassword = getResources().getDrawable( R.drawable.ic_login_lock);
        imgPassword.setBounds( 0, 0, 60, 60 );
        edtPassword.setCompoundDrawables( imgPassword, null, null, null );
    }

    private void initListeners() {
        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                edtUsername.setError(null);
                edtPassword.setError(null);

                if (edtUsername.getText().toString().equals("")
                        || edtPassword.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "لطفا تمامی فیلد ها را پرکنید", Toast.LENGTH_LONG).show();
                } else {
                    new UserLoginTask().execute();
                }
            }
        });
        btnRegisterPage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityLogin.this, ActivityRegister.class);
                startActivity(intent);
                ActivityLogin.this.finish();
            }
        });
        edtUsername.requestFocus();
    }

    private void initFonts() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/yekan.ttf");
        edtUsername.setTypeface(typeface);
        edtPassword.setTypeface(typeface);
        btnLogin.setTypeface(typeface);
        btnRegisterPage.setTypeface(typeface);
        inputUsername.setTypeface(typeface);
        inputPassword.setTypeface(typeface);
    }

    private String getMacAddress() {
        WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        return info.getMacAddress();
    }

    private String completeLoginUrl(String loginUrl) {
        loginUrl = loginUrl + getMacAddress() + "&username=" + edtUsername.getText().toString() + "&password=" + edtPassword.getText().toString();
        return loginUrl;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    private class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("در حال ورود");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            HttpFunctions httpFunctions = new HttpFunctions(HttpFunctions.RequestType.GET);
            String jsonLogin = httpFunctions.makeServiceCall(completeLoginUrl(LOGIN_URL));
            try {
                JSONArray arrayLogin = new JSONArray(jsonLogin);
                JSONObject loginObj = arrayLogin.getJSONObject(0);
                boolean loginResult = loginObj.getBoolean("login");
                return loginResult;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                Toast.makeText(ActivityLogin.this, "ورود شما با موفقیت انجام شد", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ActivityLogin.this, ActivityUser.class);
                SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString(USERNAME, edtUsername.getText().toString());
                editor.putString(PASSWORD, edtPassword.getText().toString());
                editor.putBoolean(ATTEMPT_LOGIN, true);
                editor.apply();
                startActivity(intent);
                ActivityLogin.this.finish();
            } else {
                edtUsername.setError("نام کاربری یا رمز عبور اشتباه است");
            }
            pDialog.cancel();
        }

    }
}

