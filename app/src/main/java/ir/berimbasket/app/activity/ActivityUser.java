package ir.berimbasket.app.activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import ir.berimbasket.app.R;


public class ActivityUser extends AppCompatActivity {

    TextView txtUsername, txtAccountBalance, btnChangePass, btnLogout;
    // FIXME: 9/22/2017 ship all SharedPreference to centralized PrefManager class (for ease and security reasons)
    private String PREFS_NAME = "BERIM_BASKET_PREF";
    private String USERNAME = "PREF_USERNAME";
    private String PASSWORD = "PREF_PASSWORD";
    private String ATTEMPT_LOGIN = "PREF_ATTEMPT_LOGIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initViews();
        initListeners();
        iniTexts();
    }

    private void initViews() {
        txtUsername = (TextView) findViewById(R.id.txtUsername);
        txtAccountBalance = (TextView) findViewById(R.id.txtAccCharge);
        btnChangePass = (TextView) findViewById(R.id.btnChangePass);
        btnLogout = (TextView) findViewById(R.id.btnLogout);
    }

    private void initListeners() {
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(ActivityUser.this, android.R.style.Theme_Material_Light_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(ActivityUser.this);
                }
                builder.setTitle("ثبت نام")
                        .setMessage("آیا مطمئن هستید که میخواهید از حساب کاربری خود خارج شوید")
                        .setPositiveButton("بله", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                                editor.putBoolean(ATTEMPT_LOGIN, false);
                                editor.putString(USERNAME, null);
                                editor.putString(PASSWORD, null);
                                editor.apply();
                                ActivityUser.this.finish();
                            }
                        })
                        .setNegativeButton("خیر", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .show();
            }
        });
    }

    private void iniTexts() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String username = prefs.getString(USERNAME, null);
        String accBalance = "میزان امتیاز : 122000 امتیاز";
        txtUsername.setText("نام کاربری : " + username);
        txtAccountBalance.setText(accBalance);
    }
}
