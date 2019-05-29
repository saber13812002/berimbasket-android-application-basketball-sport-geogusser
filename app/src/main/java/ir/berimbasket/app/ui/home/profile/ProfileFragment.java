package ir.berimbasket.app.ui.home.profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.HttpURLConnection;

import co.ronash.pushe.Pushe;
import ir.berimbasket.app.BuildConfig;
import ir.berimbasket.app.R;
import ir.berimbasket.app.data.env.UrlConstants;
import ir.berimbasket.app.data.network.WebApiClient;
import ir.berimbasket.app.data.network.model.Profile;
import ir.berimbasket.app.data.pref.PrefManager;
import ir.berimbasket.app.ui.login.mobile.MobileLoginActivity;
import ir.berimbasket.app.util.AnalyticsHelper;
import ir.berimbasket.app.util.FontHelper;
import ir.berimbasket.app.util.LocaleManager;
import ir.berimbasket.app.util.Redirect;
import ir.berimbasket.app.util.ScreenDensityHelper;
import ir.berimbasket.app.util.Telegram;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohammad hosein on 5/1/2017.
 */

public class ProfileFragment extends Fragment {

    private static boolean isLoggedIn;
    private TabLayout tabProfile;
    private ViewPager pagerProfile;
    private ImageView imgProfileImage, imgCoach;
    private TextView txtProfileName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Context context = inflater.getContext();
        PrefManager pref = new PrefManager(getContext());
        isLoggedIn = pref.getIsLoggedIn();
        View rootView;
        if (isLoggedIn) {
            // user logged in
            rootView = inflater.inflate(R.layout.fragment_profile, container, false);
            initViews(rootView);
            initProductPager();
            initTabProduct();

            imgProfileImage = rootView.findViewById(R.id.imgPlayerProfile);
            imgCoach = rootView.findViewById(R.id.imgCoach);
            txtProfileName = rootView.findViewById(R.id.txtProfileName);

            initPlayer();
        } else {
            //user not logged in
            rootView = inflater.inflate(R.layout.fragment_profile_not_registered, container, false);
            TextView txtNotRegisteredMsg = rootView.findViewById(R.id.txtFragmentProfile_userNotRegMsg);
            Button btnGoToLogin = rootView.findViewById(R.id.btnFragmentProfile_goToLogin);

            btnGoToLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MobileLoginActivity.class);
                    context.startActivity(intent);
                }
            });
        }

        return rootView;
    }

    @SuppressLint("RestrictedApi")
    private void initViews(View view) {
        pagerProfile = view.findViewById(R.id.pagerProfile);
        tabProfile = view.findViewById(R.id.tabProfile);
        final FloatingActionButton fabProfileMenu = view.findViewById(R.id.fabProfileMenu);
        FloatingActionButton fabChangeAvatar = view.findViewById(R.id.fabChangeAvatar);

        if (BuildConfig.FLAVOR.equals("bazaar")) {
            fabProfileMenu.setVisibility(View.GONE);
            fabChangeAvatar.setVisibility(View.GONE);
        }

        fabProfileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu(fabProfileMenu);
            }
        });

        fabChangeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!BuildConfig.FLAVOR.equals("bazaar")) {
                        Redirect.sendToTelegram(getActivity(), UrlConstants.Bot.PROFILE, Telegram.DEFAULT_BOT);
                    }
                } catch (IllegalArgumentException unknownTelegramURL) {
                    // do nothing yet
                }
            }
        });
    }

    private void initProductPager() {
        ProfilePagerAdapter profilePagerAdapter = new ProfilePagerAdapter(getContext(), getActivity().getSupportFragmentManager());
        pagerProfile.setAdapter(profilePagerAdapter);
        pagerProfile.setOffscreenPageLimit(3);
    }

    private void initTabProduct() {
        tabProfile.setupWithViewPager(pagerProfile);
        FontHelper fontHelper = new FontHelper(getContext(), getString(R.string.font_yekan));
        fontHelper.tablayoutApplyFont(tabProfile);
    }

    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_profile_change_pass:

                        break;
                    case R.id.menu_profile_info:
                        try {
                            if (!BuildConfig.FLAVOR.equals("bazaar")) {
                                Redirect.sendToTelegram(getActivity(), UrlConstants.Bot.PROFILE, Telegram.DEFAULT_BOT);
                            }
                        } catch (IllegalArgumentException unknownTelegramURL) {
                            // do nothing yet
                        }
                        break;
                    case R.id.menu_profile_logout:
                        logout();
                        break;

                    case R.id.menu_profile_score:
                        try {
                            if (!BuildConfig.FLAVOR.equals("bazaar")) {
                                Redirect.sendToTelegram(getActivity(), UrlConstants.Bot.SCORE, Telegram.DEFAULT_BOT);
                            }
                        } catch (IllegalArgumentException unknownTelegramURL) {
                            // do nothing yet
                        }
                        break;
                    case R.id.menu_profile_team:
                        try {
                            if (!BuildConfig.FLAVOR.equals("bazaar")) {
                                Redirect.sendToTelegram(getActivity(), UrlConstants.Bot.SCORE, Telegram.DEFAULT_BOT);
                            }
                        } catch (IllegalArgumentException unknownTelegramURL) {
                            // do nothing yet
                        }
                        break;
                }
                return false;
            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_fragment_profile, popup.getMenu());
        popup.show();
        FontHelper fontHelper = new FontHelper(getActivity(), getString(R.string.font_yekan));
        fontHelper.popupApplyFont(popup);

    }

    @Override
    public void onResume() {
        super.onResume();
        // Track screen view (Analytics)
        AnalyticsHelper.getInstance().trackScreenView(getContext(), this.getClass().getSimpleName());
        // invalidate fragment when login status changes (eg. return from login activity)
        PrefManager pref = new PrefManager(getContext());
        boolean loginStatus = pref.getIsLoggedIn();
        if (loginStatus != isLoggedIn) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }

    private void logout() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }
        builder.setTitle(getString(R.string.general_dialog_title_register))
                .setMessage(getString(R.string.general_dialog_message_log_out))
                .setPositiveButton(getString(R.string.general_dialog_option_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        PrefManager pref = new PrefManager(getActivity().getApplicationContext());
                        pref.putIsLoggedIn(false);
                        pref.putUserName(null);
                        pref.putPassword(null);
                        // Tracking Event (Analytics)
                        AnalyticsHelper.getInstance().trackEvent(getString(R.string.analytics_category_login), getString(R.string.analytics_action_log_out), "");
                        getFragmentManager().beginTransaction().detach(ProfileFragment.this).attach(ProfileFragment.this).commitAllowingStateLoss();
                    }
                })
                .setNegativeButton(getString(R.string.general_dialog_option_no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    private void initPlayer() {
        PrefManager pref = new PrefManager(getContext());
        String userName = pref.getUserName();
        String pusheId = Pushe.getPusheId(getContext());
        String lang = LocaleManager.getLocale(getContext()).getLanguage();
        String token = "Bearer " + pref.getToken();
        WebApiClient.getProfileApi(getContext()).getMe(pusheId, userName, lang, token).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    Profile me = response.body();
                    if (me != null) {
                        ScreenDensityHelper.loadAvatarBasedOnDensity(imgProfileImage, me.getAvatarUrl(), getContext());
                        txtProfileName.setText(me.getUsername());
                        if (me.getPriority() > 6) {
                            imgCoach.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {

            }
        });
    }

}
