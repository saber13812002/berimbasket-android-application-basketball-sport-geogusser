package ir.berimbasket.app.activity.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ir.berimbasket.app.R;
import ir.berimbasket.app.activity.ActivityLogin;
import ir.berimbasket.app.adapter.AdapterProfilePager;
import ir.berimbasket.app.exception.UnknownTelegramURL;
import ir.berimbasket.app.util.ApplicationLoader;
import ir.berimbasket.app.util.PrefManager;
import ir.berimbasket.app.util.Redirect;
import ir.berimbasket.app.view.FontHelper;
import ir.berimbasket.app.view.WrapContentViewPager;

/**
 * Created by mohammad hosein on 5/1/2017.
 */

public class FragmentProfile extends Fragment {

    TextView txtProfileName;
    AppCompatButton btnScoreProfile, btnProfileTeam;
    private static final String PROFILE_SCORE_INFO_BOT = "http://t.me/berimbasketScorebot";
    private static final String PROFILE_TEAM_INFO_BOT = "http://t.me/berimbasketScorebot";
    private static final String UPDATE_USER_INFO_BOT = "https://t.me/berimbasketprofilebot";
    private static final String UPLOAD_PHOTO_BOT = "https://t.me/berimbasketUploadbot";
    private static boolean isLoggedIn;
    private AdapterProfilePager adapterProfilePager;
    private TabLayout tabProfile;
    private WrapContentViewPager pagerProfile;
    private FloatingActionButton fabProfileMenu, fabChangeAvatar;

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

            txtProfileName = rootView.findViewById(R.id.txtProfileName);
            txtProfileName.setText(pref.getUserName());

        } else {
            //user not logged in
            rootView = inflater.inflate(R.layout.fragment_profile_not_registered, container, false);
            TextView txtNotRegisteredMsg = rootView.findViewById(R.id.txtFragmentProfile_userNotRegMsg);
            Button btnGoToLogin = rootView.findViewById(R.id.btnFragmentProfile_goToLogin);

            btnGoToLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ActivityLogin.class);
                    context.startActivity(intent);
                }
            });
        }

        return rootView;
    }

    private void initViews(View view) {
        pagerProfile = view.findViewById(R.id.pagerProfile);
        tabProfile = view.findViewById(R.id.tabProfile);
        fabProfileMenu = view.findViewById(R.id.fabProfileMenu);
        fabChangeAvatar = view.findViewById(R.id.fabChangeAvatar);

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
                    Redirect.sendToTelegram(getActivity(), UPLOAD_PHOTO_BOT);
                } catch (UnknownTelegramURL unknownTelegramURL) {
                    // do nothing yet
                }
            }
        });
    }

    private void initProductPager() {
        adapterProfilePager = new AdapterProfilePager(getContext(), getActivity().getSupportFragmentManager());
        pagerProfile.setAdapter(adapterProfilePager);
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
                            Redirect.sendToTelegram(getActivity(), UPDATE_USER_INFO_BOT);
                        } catch (UnknownTelegramURL unknownTelegramURL) {
                            // do nothing yet
                        }
                        break;
                    case R.id.menu_profile_logout:
                        logout();
                        break;

                    case R.id.menu_profile_score:
                        try {
                            Redirect.sendToTelegram(getActivity(), PROFILE_SCORE_INFO_BOT);
                        } catch (UnknownTelegramURL unknownTelegramURL) {
                            // do nothing yet
                        }
                        break;
                    case R.id.menu_profile_team:
                        try {
                            Redirect.sendToTelegram(getActivity(), PROFILE_TEAM_INFO_BOT);
                        } catch (UnknownTelegramURL unknownTelegramURL) {
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
        // Tracking the screen view (Analytics)
        ApplicationLoader.getInstance().trackScreenView(getString(R.string.analytics_screen_fragment_profile));
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
                        ApplicationLoader.getInstance().trackEvent(getString(R.string.analytics_category_login), getString(R.string.analytics_action_log_out), "");
                        getFragmentManager().beginTransaction().detach(FragmentProfile.this).attach(FragmentProfile.this).commitAllowingStateLoss();
                    }
                })
                .setNegativeButton(getString(R.string.general_dialog_option_no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

}
