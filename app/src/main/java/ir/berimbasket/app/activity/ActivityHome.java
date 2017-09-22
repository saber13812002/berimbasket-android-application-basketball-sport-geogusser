package ir.berimbasket.app.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;

import co.ronash.pushe.Pushe;
import io.fabric.sdk.android.Fabric;
import ir.berimbasket.app.R;
import ir.berimbasket.app.adapter.AdapterHomePager;
import ir.berimbasket.app.entity.EntityLocation;
import ir.berimbasket.app.network.SendLocationTask;
import ir.berimbasket.app.util.GPSTracker;
import ir.berimbasket.app.view.CustomTypefaceSpan;

public class ActivityHome extends AppCompatActivity implements View.OnClickListener {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private static final int MY_PERMISSIONS_REQUEST_INTERNET = 1000;
    private ViewPager homePager;
    private ImageView btnUser, btnSetting;
    // FIXME: 9/22/2017 ship all SharedPreference to centralized PrefManager class (for ease and security reasons)
    private String PREFS_NAME = "BERIM_BASKET_PREF";
    private String ATTEMPT_LOGIN = "PREF_ATTEMPT_LOGIN";
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    homePager.setCurrentItem(0);
                    return true;
                case R.id.navigation_map:
                    homePager.setCurrentItem(1);
                    return true;
                case R.id.navigation_profile:
                    homePager.setCurrentItem(2);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_home);
        //Register for Push Notifications
        Pushe.initialize(this,true);
        initToolbar();
        initViews();
        initListeners();
        checkPermissions();
        sendUserLocationToServer();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        homePager = (ViewPager) findViewById(R.id.vpPager);
        AdapterHomePager adapterHomePager = new AdapterHomePager(getSupportFragmentManager());

        homePager.setAdapter(adapterHomePager);
        homePager.setCurrentItem(0);
        homePager.setOffscreenPageLimit(3);

        Menu m = navigation.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }

    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/yekan.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.INTERNET)) {
//
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//
//            } else {

            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_INTERNET);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
//            }
        }
    }

    private void sendUserLocationToServer() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            GPSTracker gps = new GPSTracker(this);
            // Check if GPS enabled
            if (gps.canGetLocation()) {
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                new SendLocationTask().execute(new EntityLocation(latitude, longitude));
            } else {
                // Can't get location.
                // GPS or network is not enabled.
                // Ask user to enable GPS/network in settings.
                gps.showSettingsAlert();
            }
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initViews() {
        btnSetting = (ImageView) findViewById(R.id.btnSetting);
        btnUser = (ImageView) findViewById(R.id.btnUser);
    }

    private void initListeners() {
        btnSetting.setOnClickListener(this);
        btnUser.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSetting:
                Intent goToSetting = new Intent(this, ActivitySetting.class);
                startActivity(goToSetting);
                break;

            case R.id.btnUser:
                SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                boolean attemptLogin = preferences.getBoolean(ATTEMPT_LOGIN, false);
                Intent intent;
                if (attemptLogin) {
                    intent = new Intent(this, ActivityUser.class);
                } else {
                    intent = new Intent(this, ActivityLogin.class);
                }
                startActivity(intent);
                break;

            default:

                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_INTERNET: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
