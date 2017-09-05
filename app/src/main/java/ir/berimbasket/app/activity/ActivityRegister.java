package ir.berimbasket.app.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
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

import ir.berimbasket.app.R;
import ir.berimbasket.app.json.HttpHandler;


public class ActivityRegister extends AppCompatActivity {

    private final String VERIFY_URL = "http://imenservice.com/bball/getSignupStatusByVerificationCodeAndMAC.php?mac=";
    private final String USER_URL = "http://imenservice.com/bball/getExistOrNotThisNewRequestedUsername.php?mac=";
    private final String REGISTER_URL = "http://imenservice.com/bball/setPasswordForThisUsername.php?mac=1a2b3c4d5e6f&username=allstar33&password=@ll$tar33";
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


        Drawable imgUser = getResources().getDrawable( R.drawable.ic_user_accent_24dp);
        imgUser.setBounds( 0, 0, 60, 60 );
        edtUsername.setCompoundDrawables( imgUser, null, null, null );

        Drawable imgPassword = getResources().getDrawable( R.drawable.ic_lock_accent_24dp);
        imgPassword.setBounds( 0, 0, 60, 60 );
        edtPassword.setCompoundDrawables( imgPassword, null, null, null );


        Drawable imgPasswordRepeat = getResources().getDrawable( R.drawable.ic_lock_open_accent_24dp);
        imgPasswordRepeat.setBounds( 0, 0, 60, 60 );
        edtPasswordRepeat.setCompoundDrawables( imgPasswordRepeat, null, null, null );

        Drawable imgVerify = getResources().getDrawable( R.drawable.ic_zero_accent_24dp);
        imgVerify.setBounds( 0, 0, 60, 60 );
        edtVerifyCode.setCompoundDrawables( imgVerify, null, null, null );
    }

    private void initFonts(){
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/yekan.ttf");
        btnLoginActivity.setTypeface(typeface);
        btnRegister.setTypeface(typeface);
        edtVerifyCode.setTypeface(typeface);
        edtUsername.setTypeface(typeface);
        edtPassword.setTypeface(typeface);
        edtPasswordRepeat.setTypeface(typeface);
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
                    Toast.makeText(getApplicationContext(), "لطفا تمامی فیلد ها را پرکنید", Toast.LENGTH_LONG).show();
                }else if (!edtPassword.getText().toString().equals(edtPasswordRepeat.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "رمزهای وارد شده مطابقت ندارد", Toast.LENGTH_LONG).show();
                }else {
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
        builder.setTitle("ثبت نام")
                .setMessage("برای ثبت نام روی دکمه لینک ربات کلیک کنید و سپس با دریافت کد چهار رقمی با برگشت به اپلیکیشن آن را در اپلیکیشن وارد کنید تا ثبت نام شما انجام شود")
                .setPositiveButton("لینک ربات", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent telegram = new Intent(Intent.ACTION_VIEW, Uri.parse("https://telegram.me/berimbasketbot"));
                        startActivity(telegram);
                    }
                })
                .setNegativeButton("لغو", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        ActivityRegister.this.finish();
                    }
                })
                .show();
    }

    private String getMacAddress() {
        WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        return info.getMacAddress();
    }

    private String completeUserUrl(String userUrl) {
        userUrl = userUrl + getMacAddress() + "&username=" + edtUsername.getText().toString();
        return userUrl;
    }

    private String completeVerifyUrl(String verifyUrl) {
        verifyUrl = verifyUrl + getMacAddress() + "&code=" + edtVerifyCode.getText().toString();
        return verifyUrl;
    }

    private String completeRegisterUrl(String registerUrl) {
        registerUrl = registerUrl + getMacAddress() + "&username=" + edtUsername.getText().toString() + "&password=" + edtPassword.getText().toString();
        return registerUrl;
    }

    private class ValidateUsername extends AsyncTask<Void, Void, Bundle> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("در حال بررسی اطلاعات");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Bundle doInBackground(Void... voids) {
            String userUrl = completeUserUrl(USER_URL);
            HttpHandler httpHandler = new HttpHandler(HttpHandler.RequestType.GET);
            String userJson = httpHandler.makeServiceCall(userUrl);
            Bundle errorBundle = new Bundle();
            try {
                JSONArray arrayUser = new JSONArray(userJson);
                JSONObject userObj = arrayUser.getJSONObject(0);
                boolean userExist = (boolean) userObj.get("exist");
                if (userExist) {
                    errorBundle.putBoolean(USER_ERROR, true);
                } else {
                    String verifyUrl = completeVerifyUrl(VERIFY_URL);
                    String verifyJson = httpHandler.makeServiceCall(verifyUrl);
                    JSONArray arrayVerify = new JSONArray(verifyJson);
                    JSONObject verifyObj = arrayVerify.getJSONObject(0);
                    boolean verifyState = (boolean) verifyObj.get("SignupStatus");
                    if (!verifyState) {
                        errorBundle.putBoolean(VERIFY_ERROR, true);
                    } else {
                        String registerUrl = completeRegisterUrl(REGISTER_URL);
                        String registerJson = httpHandler.makeServiceCall(registerUrl);
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
            edtUsername.setError("این نام کاربری قبلا استفاده شده");
            boolean userError = result.getBoolean(USER_ERROR, false);
            boolean verifyError = result.getBoolean(VERIFY_ERROR, false);
            boolean registerError = result.getBoolean(REGISTER_ERROR, true);

            if (userError) {
                edtUsername.setError("این نام کاربری قبلا استفاده شده");
            }
            if (verifyError) {
                edtVerifyCode.setError("کد اشتباه است یا منقضی شده");
            }
            if (!registerError) {
                Toast.makeText(getApplicationContext(), "ثبت نام شما با موفقیت انجام شد، حالا میتوانید وارد شوید", Toast.LENGTH_LONG).show();
                ActivityRegister.this.finish();
            }
            pDialog.cancel();
        }
    }
}
