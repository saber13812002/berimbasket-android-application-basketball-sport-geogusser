package ir.berimbasket.app.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
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

import co.ronash.pushe.Pushe;
import ir.berimbasket.app.R;
import ir.berimbasket.app.exception.UnknownTelegramURL;
import ir.berimbasket.app.network.HttpFunctions;
import ir.berimbasket.app.util.ApplicationLoader;
import ir.berimbasket.app.util.PrefManager;
import ir.berimbasket.app.util.Redirect;
import ir.berimbasket.app.util.TypefaceManager;

/**
 * A login screen that offers login via email/password.
 */
public class ActivityLogin extends AppCompatActivity {

    private final String LOGIN_URL = "http://berimbasket.ir/bball/getStatusLoginByUsernamePassword.php";
    AppCompatButton btnLogin;
    TextView btnRegisterPage, btnTelegramTut;
    ProgressDialog pDialog;
    // UI references.
    private EditText edtUsername, edtPassword;
    TextInputLayout inputUsername, inputPassword;

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

    @Override
    protected void onResume() {
        super.onResume();
        // Tracking the screen view (Analytics)
        ApplicationLoader.getInstance().trackScreenView(getString(R.string.analytics_screen_login));
    }

    private void initViews() {
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        inputUsername = (TextInputLayout) findViewById(R.id.inputUsername);
        inputPassword = (TextInputLayout) findViewById(R.id.inputPassword);
        btnLogin = (AppCompatButton) findViewById(R.id.btnLogin);
        btnRegisterPage = (TextView) findViewById(R.id.btnRegActivity);
        btnTelegramTut = (TextView) findViewById(R.id.btnTelegramTut);
        pDialog = new ProgressDialog(ActivityLogin.this);

        Drawable imgUser = getResources().getDrawable(R.drawable.ic_login_user);
        imgUser.setBounds(0, 0, 60, 60);
        edtUsername.setCompoundDrawables(imgUser, null, null, null);
        edtUsername.setCompoundDrawablePadding(30);

        Drawable imgPassword = getResources().getDrawable(R.drawable.ic_login_lock);
        imgPassword.setBounds(0, 0, 60, 60);
        edtPassword.setCompoundDrawables(imgPassword, null, null, null);
        edtPassword.setCompoundDrawablePadding(30);
    }

    private void initListeners() {
        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                edtUsername.setError(null);
                edtPassword.setError(null);

                if (edtUsername.getText().toString().equals("")
                        || edtPassword.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.activity_login_toast_fill_all_fields, Toast.LENGTH_LONG).show();
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
        btnTelegramTut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Redirect.sendToTelegram(ActivityLogin.this, "https://t.me/berimbasket/263");
                } catch (UnknownTelegramURL unknownTelegramURL) {
                    // do nothing yet
                }
            }
        });
        edtUsername.requestFocus();
    }

    private void initFonts() {
        Typeface typeface = TypefaceManager.get(getApplicationContext(), getString(R.string.font_yekan));
        inputUsername.setTypeface(typeface);
        inputPassword.setTypeface(typeface);
    }

    private String completeLoginUrl(String loginUrl) {
        PrefManager pref = new PrefManager(getApplicationContext());
        String pusheId = Pushe.getPusheId(getApplicationContext());
        loginUrl = loginUrl + "?" + "mac=" + pref.getDeviceID() + "&username=" + edtUsername.getText().toString()
                + "&password=" + edtPassword.getText().toString() + "&pusheid=" + pusheId;
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
            pDialog.setMessage(getString(R.string.general_progress_dialog_logging_in));
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
                int userId = loginObj.getInt("id");
                PrefManager pref = new PrefManager(getApplicationContext());
                pref.putUserID(userId);
                return loginResult;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                Toast.makeText(ActivityLogin.this, getString(R.string.activity_login_toast_successful), Toast.LENGTH_LONG).show();
                PrefManager pref = new PrefManager(getApplicationContext());
                pref.putUserName(edtUsername.getText().toString());
                pref.putPassword(edtPassword.getText().toString());
                pref.putIsLoggedIn(true);
                // Tracking Event (Analytics)
                ApplicationLoader.getInstance().trackEvent(getString(R.string.analytics_category_login),
                        getString(R.string.analytics_action_log_on), "");
                ActivityLogin.this.finish();
                // FIXME: 16/11/2017  after login fragment profile's childes fragment not showing
            } else {
                edtUsername.setError(getString(R.string.activity_login_edt_username_error));
            }
            pDialog.cancel();
        }

    }
}

