package ir.berimbasket.app.ui.register;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.util.List;

import co.ronash.pushe.Pushe;
import ir.berimbasket.app.R;
import ir.berimbasket.app.data.network.WebApiClient;
import ir.berimbasket.app.data.network.model.CheckUsername;
import ir.berimbasket.app.data.network.model.Register;
import ir.berimbasket.app.data.network.model.VerifyBot;
import ir.berimbasket.app.data.pref.PrefManager;
import ir.berimbasket.app.ui.base.BaseActivity;
import ir.berimbasket.app.ui.login.LoginActivity;
import ir.berimbasket.app.util.AnalyticsHelper;
import ir.berimbasket.app.util.Redirect;
import ir.berimbasket.app.util.Telegram;
import ir.berimbasket.app.util.TypefaceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterActivity extends BaseActivity {

    private static final String REGISTER_BOT = "https://telegram.me/berimbasketbot";
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

    private void initViews() {
        btnRegister = findViewById(R.id.btnRegister);
        btnLoginActivity = findViewById(R.id.btnLoginActivity);
        edtUsername = findViewById(R.id.edtUsername);
        edtVerifyCode = findViewById(R.id.edtVerifyCode);
        edtPassword = findViewById(R.id.edtPassword);
        edtPasswordRepeat = findViewById(R.id.edtPasswordRepeat);
        inputVerifyCode = findViewById(R.id.inputVerifyCode);
        inputUsername = findViewById(R.id.inputUsername);
        inputPassword = findViewById(R.id.inputPassword);
        inputPasswordRepeat = findViewById(R.id.inputPasswordRepeat);
        pDialog = new ProgressDialog(RegisterActivity.this);
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
        edtVerifyCode.setCompoundDrawablePadding(30);
        edtPassword.setCompoundDrawablePadding(30);
        edtPasswordRepeat.setCompoundDrawablePadding(30);
        edtUsername.setCompoundDrawablePadding(30);
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
                    registerUser(edtVerifyCode.getText().toString(), edtUsername.getText().toString(), edtPassword.getText().toString());
                }

            }
        });


        btnLoginActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToLoginActivity = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(goToLoginActivity);
                RegisterActivity.this.finish();
            }
        });
    }


    private void showRobotDialog() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(RegisterActivity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(RegisterActivity.this);
        }
        builder.setTitle(getString(R.string.general_dialog_title_register))
                .setMessage(getString(R.string.general_dialog_message_bot_register))
                .setPositiveButton(getString(R.string.general_dialog_option_bot_link), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Redirect.sendToTelegram(getApplicationContext(), REGISTER_BOT, Telegram.PUSHEID_BOT);
                        // Tracking Event (Analytics)
                        AnalyticsHelper.getInstance().trackEvent(getString(R.string.analytics_category_registration)
                                , getString(R.string.analytics_action_telegram_bot), "");
                    }
                })
                .setNegativeButton(getString(R.string.general_dialog_option_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        RegisterActivity.this.finish();
                    }
                })
                .show();
    }

    private void registerUser(final String code, final String username, final String password) {
        pDialog.setMessage(getString(R.string.general_progress_dialog_checking_info));
        pDialog.setCancelable(false);
        pDialog.show();
        PrefManager pref = new PrefManager(getApplicationContext());
        final String pusheId = Pushe.getPusheId(getApplicationContext());
        final String deviceId = pref.getDeviceID();
        WebApiClient.getRegisterApi().checkUsername(deviceId, username, pusheId).enqueue(new Callback<List<CheckUsername>>() {
            @Override
            public void onResponse(Call<List<CheckUsername>> call, Response<List<CheckUsername>> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    List<CheckUsername> checkUsernameList = response.body();
                    if (checkUsernameList != null) {
                        if (checkUsernameList.get(0).getExist()) {
                            edtUsername.setError(getString(R.string.activity_register_edt_username_error));
                            pDialog.cancel();
                        } else {
                            WebApiClient.getRegisterApi().verifyBotCode(deviceId, code, pusheId, username).enqueue(new Callback<List<VerifyBot>>() {
                                @Override
                                public void onResponse(Call<List<VerifyBot>> call, Response<List<VerifyBot>> response) {
                                    if (response.code() == HttpURLConnection.HTTP_OK) {
                                        List<VerifyBot> verifyBotList = response.body();
                                        if (verifyBotList != null) {
                                            if (!verifyBotList.get(0).getStatus()) {
                                                edtVerifyCode.setError(getString(R.string.activity_register_edt_verify_error));
                                                pDialog.cancel();
                                            } else {
                                                WebApiClient.getRegisterApi().register(deviceId, username, password, pusheId).enqueue(new Callback<List<Register>>() {
                                                    @Override
                                                    public void onResponse(Call<List<Register>> call, Response<List<Register>> response) {
                                                        pDialog.cancel();
                                                        if (response.code() == HttpURLConnection.HTTP_OK) {
                                                            List<Register> registerList = response.body();
                                                            if (registerList != null) {
                                                                if (registerList.get(0).getPasswordSet()) {
                                                                    Toast.makeText(getApplicationContext(), getString(R.string.activity_register_toast_register_successful), Toast.LENGTH_LONG).show();
                                                                    // Tracking Event (Analytics)
                                                                    AnalyticsHelper.getInstance().trackEvent(getString(R.string.analytics_category_registration), getString(R.string.analytics_action_new_register), "");
                                                                    RegisterActivity.this.finish();
                                                                }
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<List<Register>> call, Throwable t) {
                                                        pDialog.cancel();
                                                    }
                                                });
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<VerifyBot>> call, Throwable t) {
                                    pDialog.cancel();
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CheckUsername>> call, Throwable t) {
                pDialog.cancel();
            }
        });
    }
}
