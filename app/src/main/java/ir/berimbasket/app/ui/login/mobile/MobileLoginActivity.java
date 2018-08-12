package ir.berimbasket.app.ui.login.mobile;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import ir.berimbasket.app.R;
import ir.berimbasket.app.ui.base.BaseActivity;

public class MobileLoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.mobile_login_activity_phone_number_text);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

}
