package ir.berimbasket.app.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;

import co.mobiwise.materialintro.animation.MaterialIntroListener;
import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.shape.ShapeType;
import co.mobiwise.materialintro.view.MaterialIntroView;
import co.ronash.pushe.Pushe;
import ir.berimbasket.app.R;
import ir.berimbasket.app.data.env.UrlConstants;
import ir.berimbasket.app.data.network.WebApiClient;
import ir.berimbasket.app.data.network.model.TokenResponse;
import ir.berimbasket.app.data.network.model.ValidateResponse;
import ir.berimbasket.app.data.pref.PrefManager;
import ir.berimbasket.app.ui.base.BaseActivity;
import ir.berimbasket.app.ui.register.RegisterActivity;
import ir.berimbasket.app.util.AnalyticsHelper;
import ir.berimbasket.app.util.LocaleManager;
import ir.berimbasket.app.util.Redirect;
import ir.berimbasket.app.util.Telegram;
import ir.berimbasket.app.util.TypefaceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {

    AppCompatButton btnLogin, btnSignUpTelegram, btnSignUpEmail;
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

        new MaterialIntroView.Builder(this)
                .enableDotAnimation(true)
                .setFocusGravity(FocusGravity.CENTER)
                .enableIcon(false)
                .setFocusType(Focus.NORMAL)
                .setDelayMillis(500)
                .enableFadeAnimation(true)
                .performClick(false)
                .setShape(ShapeType.RECTANGLE)
                .setTarget(btnRegisterPage)
                .setUsageId("login_don't_have_account") //THIS SHOULD BE UNIQUE ID
                .dismissOnTouch(true)
                .setListener(btnRegisterShowcaseListener)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        /* Auto Login user */
        Uri uri = getIntent().getData();
        if (uri != null) {
            final String token = uri.getQueryParameter("token");
            String pusheId = Pushe.getPusheId(getApplicationContext());
            String lang = LocaleManager.getLocale(getApplicationContext()).getLanguage();
            String username = new PrefManager(getApplicationContext()).getUserName();
            String bearerToken = "Bearer " + token;
            WebApiClient.getTokenApi(getApplicationContext()).validateToken(pusheId, username, lang, bearerToken).enqueue(new Callback<ValidateResponse>() {
                @Override
                public void onResponse(Call<ValidateResponse> call, Response<ValidateResponse> response) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        ValidateResponse body = response.body();
                        if (body != null) {
                            int status = body.getData().getStatus();
                            if (status == 200) {
                                PrefManager pref = new PrefManager(getApplicationContext());
                                Toast.makeText(LoginActivity.this, getString(R.string.activity_login_toast_successful), Toast.LENGTH_LONG).show();
                                pref.putToken(token);
                                pref.putIsLoggedIn(true);
                                // Tracking Event (Analytics)
                                AnalyticsHelper.getInstance().trackEvent(getString(R.string.analytics_category_login),
                                        getString(R.string.analytics_action_log_on), "");
                                LoginActivity.this.finish();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ValidateResponse> call, Throwable t) {

                }
            });
        }
    }

    private MaterialIntroListener btnRegisterShowcaseListener = new MaterialIntroListener() {
        @Override
        public void onUserClicked(String s) {
            new MaterialIntroView.Builder(LoginActivity.this)
                    .enableDotAnimation(true)
                    .setFocusGravity(FocusGravity.CENTER)
                    .enableIcon(false)
                    .setFocusType(Focus.NORMAL)
                    .setDelayMillis(500)
                    .enableFadeAnimation(true)
                    .performClick(false)
                    .setShape(ShapeType.RECTANGLE)
                    .setTarget(btnTelegramTut)
                    .setUsageId("login_telegram_tut") //THIS SHOULD BE UNIQUE ID
                    .dismissOnTouch(true)
                    .show();
        }
    };

    private void initViews() {
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        inputUsername = findViewById(R.id.inputUsername);
        inputPassword = findViewById(R.id.inputPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUpTelegram = findViewById(R.id.btnSignUpTelegram);
        btnSignUpEmail = findViewById(R.id.btnSignUpEmail);
        btnRegisterPage = findViewById(R.id.btnRegActivity);
        btnTelegramTut = findViewById(R.id.btnTelegramTut);
        pDialog = new ProgressDialog(LoginActivity.this);

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
                    loginUser(edtUsername.getText().toString(), edtPassword.getText().toString());
                }
            }
        });
        btnSignUpTelegram.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
            }
        });
        btnSignUpEmail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Redirect.sendToCustomTab(LoginActivity.this, UrlConstants.External.WP_REGISTER);
            }
        });
        btnRegisterPage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                btnRegisterPage.setVisibility(View.GONE);
                Animation in = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
                in.setDuration(1000);
                btnSignUpTelegram.startAnimation(in);
                btnSignUpTelegram.setVisibility(View.VISIBLE);
                btnSignUpEmail.startAnimation(in);
                btnSignUpEmail.setVisibility(View.VISIBLE);
            }
        });
        btnTelegramTut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Redirect.sendToTelegram(LoginActivity.this, UrlConstants.External.REGISTER_TUTORIAL, Telegram.POST);
                } catch (IllegalArgumentException unknownTelegramURL) {
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

    private void loginUser(String username, String password) {
        pDialog.setMessage(getString(R.string.general_progress_dialog_logging_in));
        pDialog.setCancelable(false);
        pDialog.show();
        String pusheId = Pushe.getPusheId(getApplicationContext());
        String lang = LocaleManager.getLocale(getApplicationContext()).getLanguage();
        WebApiClient.getTokenApi(getApplicationContext()).getToken(pusheId, lang, username, password).enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                pDialog.cancel();
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    TokenResponse tokenResponse = response.body();
                    if (tokenResponse != null) {
                        PrefManager pref = new PrefManager(getApplicationContext());
                        Toast.makeText(LoginActivity.this, getString(R.string.activity_login_toast_successful), Toast.LENGTH_LONG).show();
                        pref.putToken(tokenResponse.getToken());
                        pref.putUserName(edtUsername.getText().toString());
                        pref.putPassword(edtPassword.getText().toString());
                        pref.putIsLoggedIn(true);
                        // Tracking Event (Analytics)
                        AnalyticsHelper.getInstance().trackEvent(getString(R.string.analytics_category_login),
                                getString(R.string.analytics_action_log_on), "");
                        LoginActivity.this.finish();
                    }
                } else if (response.code() == HttpURLConnection.HTTP_FORBIDDEN) {
                    edtUsername.setError(getString(R.string.activity_login_edt_username_error));
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {

            }
        });
    }
}

