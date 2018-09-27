package ir.berimbasket.app.ui.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.anetwork.android.sdk.advertising.view.AnetworkBannerView;
import com.wooplr.spotlight.SpotlightView;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.ShapeType;
import co.mobiwise.materialintro.view.MaterialIntroView;
import co.ronash.pushe.Pushe;
import ir.berimbasket.app.R;
import ir.berimbasket.app.data.network.WebApiClient;
import ir.berimbasket.app.data.network.model.Question;
import ir.berimbasket.app.data.pref.PrefManager;
import ir.berimbasket.app.ui.base.BaseActivity;
import ir.berimbasket.app.ui.common.custom.TypefaceSpanCustom;
import ir.berimbasket.app.ui.common.swipe_showcase.SwipeShowcase;
import ir.berimbasket.app.ui.notification.NotificationActivity;
import ir.berimbasket.app.ui.settings.SettingsActivity;
import ir.berimbasket.app.util.EditTextHelper;
import ir.berimbasket.app.util.LocaleManager;
import ir.berimbasket.app.util.TypefaceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private ViewPager homePager;
    private ImageView btnSetting, btnNotification;
    private BottomNavigationView navigation;
    private MenuItem prevMenuItem;
    private AnetworkBannerView anetworkBannerView;

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
        setContentView(R.layout.activity_home);
        //Register for Push Notifications
        Pushe.initialize(this, true);
        initToolbar();
        checkQuestion();
        navigation = findViewById(R.id.navigation);
        homePager = findViewById(R.id.vpPager);
        btnSetting = findViewById(R.id.btnSetting);
        btnNotification = findViewById(R.id.btnNotification);
        anetworkBannerView = findViewById(R.id.banner_1);
        btnSetting.setOnClickListener(this);
        btnNotification.setOnClickListener(this);

        anetworkBannerView.show(this);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        HomePagerAdapter homePagerAdapter = new HomePagerAdapter(getSupportFragmentManager());
        homePager.setAdapter(homePagerAdapter);
        homePager.setCurrentItem(0);
        homePager.setOffscreenPageLimit(3);
        homePager.addOnPageChangeListener(pageChangeListener);

        displayNavigationShowcase();

        changeBottomNavFont(navigation);
    }

    private void displayNavigationShowcase() {
        new MaterialIntroView.Builder(this)
                .enableDotAnimation(false)
                .enableIcon(false)
                .setFocusType(Focus.ALL)
                .setDelayMillis(500)
                .enableFadeAnimation(true)
                .performClick(false)
                .setShape(ShapeType.RECTANGLE)
                .setInfoText(getString(R.string.activity_home_navigation_showcase))
                .setTarget(navigation)
                .setUsageId("home_navigation_showcase") //THIS SHOULD BE UNIQUE ID
                .dismissOnTouch(true)
                .setListener(s -> displayBtnSettingsShowcase())
                .show();
    }

    private void displayBtnSettingsShowcase() {
        new SpotlightView.Builder(HomeActivity.this)
                .introAnimationDuration(400)
                .enableRevealAnimation(false)
                .performClick(false)
                .fadeinTextDuration(400)
                .headingTvColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null))
                .headingTvSize(32)
                .headingTvText(getString(R.string.activity_home_showcase_btn_settings_title))
                .subHeadingTvColor(ResourcesCompat.getColor(getResources(), R.color.showcaseSubHeadingTVColor, null))
                .subHeadingTvSize(16)
                .subHeadingTvText(getString(R.string.activity_home_showcase_btn_settings_description))
                .maskColor(ResourcesCompat.getColor(getResources(), R.color.showcaseMaskColor, null))
                .target(btnSetting)
                .lineAnimDuration(400)
                .lineAndArcColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null))
                .dismissOnTouch(true)
                .dismissOnBackPress(true)
                .enableDismissAfterShown(true)
                .usageId("home_toolbar_settings") //UNIQUE ID
                .setListener(s -> displayBtnNotificationShowcase())
                .show();
    }

    private void displayBtnNotificationShowcase() {
        new SpotlightView.Builder(HomeActivity.this)
                .introAnimationDuration(400)
                .enableRevealAnimation(false)
                .performClick(false)
                .fadeinTextDuration(400)
                .headingTvColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null))
                .headingTvSize(32)
                .headingTvText(getString(R.string.activity_home_showcase_btn_notification_title))
                .subHeadingTvColor(ResourcesCompat.getColor(getResources(), R.color.showcaseSubHeadingTVColor, null))
                .subHeadingTvSize(16)
                .subHeadingTvText(getString(R.string.activity_home_showcase_btn_notification_description))
                .maskColor(ResourcesCompat.getColor(getResources(), R.color.showcaseMaskColor, null))
                .target(btnNotification)
                .lineAnimDuration(400)
                .lineAndArcColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null))
                .dismissOnTouch(true)
                .dismissOnBackPress(true)
                .enableDismissAfterShown(true)
                .usageId("home_toolbar_notification") //UNIQUE ID
                .setListener(s -> displaySwipeHandShowcase())
                .show();
    }

    private void displaySwipeHandShowcase() {
        if (TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == ViewCompat.LAYOUT_DIRECTION_RTL) {
            SwipeShowcase.from(HomeActivity.this)
                    .setContentView(R.layout.activity_home)
                    .on(R.id.vpPager)
                    .displaySwipableRight()
                    .show();
        } else {
            SwipeShowcase.from(HomeActivity.this)
                    .setContentView(R.layout.activity_home)
                    .on(R.id.vpPager)
                    .displaySwipableLeft()
                    .show();
        }
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSetting:
                Intent goToSetting = new Intent(this, SettingsActivity.class);
                startActivity(goToSetting);
                break;

            case R.id.btnNotification:
                Intent goToNotification = new Intent(this, NotificationActivity.class);
                startActivity(goToNotification);
                break;
            default:
                break;
        }
    }

    private void checkQuestion() {
        String pusheid = Pushe.getPusheId(getApplicationContext());
        String userName = new PrefManager(getApplicationContext()).getUserName();
        String lang = LocaleManager.getLocale(getApplicationContext()).getLanguage();
        WebApiClient.getQuestionApi(getApplicationContext()).getQuestion(pusheid, userName, lang).enqueue(new Callback<Question>() {
            @Override
            public void onResponse(Call<Question> call, Response<Question> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    final Question question = response.body();
                    getAnswer(question);
                }
            }

            @Override
            public void onFailure(Call<Question> call, Throwable t) {
                Log.i("responseBodyNow", t.getMessage());
            }
        });
    }

    private void getAnswer(final Question question) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText editText = new EditText(HomeActivity.this);
        alert.setMessage(question.getTitle());
        alert.setTitle(R.string.activity_home_get_answer_dialog_title);
        alert.setView(editText);
        alert.setPositiveButton(R.string.activity_home_get_answer_dialog_btn_yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (!EditTextHelper.isEmpty(editText)) {
                    String answer = editText.getText().toString();
                    sendAnswer(answer, question);
                } else {
                    EditTextHelper.promptEmpty(HomeActivity.this);
                }
            }
        });

        alert.setNegativeButton(R.string.activity_home_get_answer_dialog_btn_no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        alert.setCancelable(false);
        alert.show();
    }

    private void sendAnswer(final String answer, final Question question) {
        Map<String, String> answerBody = new HashMap<>();
        answerBody.put("answer", answer);
        answerBody.put("Schema", question.getSchema());
        answerBody.put("table", question.getTable());

        String pusheid = Pushe.getPusheId(HomeActivity.this);
        String username = new PrefManager(getApplicationContext()).getUserName();
        String lang = LocaleManager.getLocale(getApplicationContext()).getLanguage();
        WebApiClient.sendAnswerApi(getApplicationContext()).sendAnswer(pusheid, username, answerBody, lang).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    Toast.makeText(HomeActivity.this, R.string.activity_home_send_answer_prompt_successful, Toast.LENGTH_LONG).show();
                    Log.i("response code", String.valueOf(response.code()));
                } else {
                    EditTextHelper.promptTryAgain(HomeActivity.this);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                EditTextHelper.promptTryLater(HomeActivity.this);
            }
        });

    }
}
