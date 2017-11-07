package ir.berimbasket.app.activity;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import ir.berimbasket.app.R;
import ir.berimbasket.app.util.ApplicationLoader;
import ir.berimbasket.app.util.PrefManager;
import ir.berimbasket.app.util.SendTo;


public class ActivityUser extends AppCompatActivity implements View.OnClickListener {

    TextView txtUsername, txtAccountBalance, btnChangePass, btnLogout, btnCompleteProfile;

    private static final String UPDATE_USER_INFO_BOT = "https://t.me/berimbasketprofilebot";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initViews();
        initListeners();
        iniTexts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tracking the screen view (Analytics)
        ApplicationLoader.getInstance().trackScreenView("User Screen");
    }

    private void initViews() {
        txtUsername = (TextView) findViewById(R.id.txtUsername);
        txtAccountBalance = (TextView) findViewById(R.id.txtAccCharge);
        btnChangePass = (TextView) findViewById(R.id.btnChangePass);
        btnLogout = (TextView) findViewById(R.id.btnLogout);
        btnCompleteProfile = (TextView) findViewById(R.id.btnCompleteProfile);
    }

    private void initListeners() {
        btnChangePass.setOnClickListener(ActivityUser.this);
        btnLogout.setOnClickListener(ActivityUser.this);
        btnCompleteProfile.setOnClickListener(ActivityUser.this);
    }

    private void iniTexts() {
        PrefManager pref = new PrefManager(getApplicationContext());
        String username = pref.getUserName();
        String accBalance = "میزان امتیاز : 122000 امتیاز";
        txtUsername.setText("نام کاربری : " + username);
        txtAccountBalance.setText(accBalance);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.btnLogout :
                onLogoutClick();
                break;
            case R.id.btnCompleteProfile :
                onCompleteProfileClick();
                break;
        }
    }

    private void onCompleteProfileClick() {
        SendTo.sendToTelegramChat(getApplicationContext(), UPDATE_USER_INFO_BOT);
    }

    private void onLogoutClick() {
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
                        PrefManager pref = new PrefManager(getApplicationContext());
                        pref.putIsLoggedIn(false);
                        pref.putUserName(null);
                        pref.putPassword(null);
                        // Tracking Event (Analytics)
                        ApplicationLoader.getInstance().trackEvent("Login", "Log out", "");
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
}
