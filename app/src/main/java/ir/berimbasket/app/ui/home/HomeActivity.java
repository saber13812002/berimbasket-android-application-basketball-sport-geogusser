package ir.berimbasket.app.ui.home;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
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
import ir.berimbasket.app.ui.base.BaseActivity;
import ir.berimbasket.app.ui.common.custom.TypefaceSpanCustom;
import ir.berimbasket.app.ui.settings.SettingsActivity;
import ir.berimbasket.app.util.TypefaceManager;

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private ViewPager homePager;
    private ImageView btnUser, btnSetting;
    private BottomNavigationView navigation;
    private MenuItem prevMenuItem;

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

    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (prevMenuItem != null) {
                prevMenuItem.setChecked(false);  // unCheck previous item
            } else {
                // no previous item yet
                navigation.getMenu().getItem(0).setChecked(false);
            }

            navigation.getMenu().getItem(position).setChecked(true);
            prevMenuItem = navigation.getMenu().getItem(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_home);
        //Register for Push Notifications
        Pushe.initialize(this, true);
        initToolbar();
        initViews();
        initListeners();

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        homePager = findViewById(R.id.vpPager);
        HomePagerAdapter homePagerAdapter = new HomePagerAdapter(getSupportFragmentManager());

        homePager.setAdapter(homePagerAdapter);
        homePager.setCurrentItem(0);
        homePager.setOffscreenPageLimit(3);
        homePager.addOnPageChangeListener(pageChangeListener);

        changeBottomNavFont(navigation);


    }

    private void changeBottomNavFont(BottomNavigationView navigation) {
        Menu m = navigation.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = TypefaceManager.get(getApplicationContext(), getString(R.string.font_yekan));
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new TypefaceSpanCustom("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initViews() {
        btnSetting = findViewById(R.id.btnSetting);
    }

    private void initListeners() {
        btnSetting.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSetting:
                Intent goToSetting = new Intent(this, SettingsActivity.class);
                startActivity(goToSetting);
                break;
            default:
                break;
        }
    }
}
