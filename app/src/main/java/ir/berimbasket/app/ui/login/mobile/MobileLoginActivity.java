package ir.berimbasket.app.ui.login.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import ir.berimbasket.app.R;
import ir.berimbasket.app.ui.base.BaseActivity;
import ir.berimbasket.app.ui.common.PermissionsRequest;
import ir.berimbasket.app.ui.login.LoginActivity;

public class MobileLoginActivity extends BaseActivity implements MobileLoginFragment.MobileLoginListener,
        MobileLoginVerifyFragment.MobileVerifyListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.mobile_login_activity_phone_number_text);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        PermissionsRequest.checkReadSmsPermission(getApplicationContext(), MobileLoginActivity.this, 500);
        PermissionsRequest.checkReceiveSmsPermission(getApplicationContext(), MobileLoginActivity.this, 600);

        swapFragment(new MobileLoginFragment(), false);
    }

    @Override
    public void onSignInTelegramClickListener() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void onSignInEmailClickListener() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void onOTPSent(final String mobile) {
        Bundle b = new Bundle();
        b.putString(MobileLoginVerifyFragment.INTENT_KEY_MOBILE, mobile);
        Fragment fragment = new MobileLoginVerifyFragment();
        fragment.setArguments(b);
        swapFragment(fragment, true);
    }

    private void swapFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();  // call fragment manager from parent activity
        ft.replace(R.id.fragmentMobileLoginContainer, fragment);
        if (addToBackStack) {
            ft.addToBackStack(null);
        }
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);  // add animation to replacement process
        ft.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTimerExpire() {
        try {
            getSupportFragmentManager().popBackStack();
        } catch (IllegalStateException e) {
            swapFragment(new MobileLoginFragment(), false);
        }
    }
}
