package ir.berimbasket.app.ui.intro;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cleveroad.slidingtutorial.Direction;
import com.cleveroad.slidingtutorial.IndicatorOptions;
import com.cleveroad.slidingtutorial.OnTutorialPageChangeListener;
import com.cleveroad.slidingtutorial.PageOptions;
import com.cleveroad.slidingtutorial.TransformItem;
import com.cleveroad.slidingtutorial.TutorialOptions;
import com.cleveroad.slidingtutorial.TutorialPageOptionsProvider;
import com.cleveroad.slidingtutorial.TutorialPageProvider;
import com.cleveroad.slidingtutorial.TutorialSupportFragment;

import ir.berimbasket.app.R;
import ir.berimbasket.app.data.pref.PrefManager;
import ir.berimbasket.app.ui.home.HomeActivity;

public class IntroAdapter extends TutorialSupportFragment
        implements OnTutorialPageChangeListener {

    private static final int TOTAL_PAGES = 4;
    private int currentPosition;
    private TextView txtSkip;

    private final View.OnClickListener mOnSkipClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (currentPosition != (TOTAL_PAGES - 1)) {
                ViewPager pager = getViewPager();
                pager.setCurrentItem(currentPosition + 1, true);
            } else {
                PrefManager pref = new PrefManager(getActivity());
                pref.putIntroPassed(true);
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        }
    };

    private final TutorialPageOptionsProvider mTutorialPageOptionsProvider = new TutorialPageOptionsProvider() {
        @NonNull
        @Override
        public PageOptions provide(int position) {
            @LayoutRes int pageLayoutResId;
            TransformItem[] tutorialItems;
            switch (position) {
                case 0: {
                    pageLayoutResId = R.layout.fragment_intro_player;
                    tutorialItems = new TransformItem[]{
                            TransformItem.create(R.id.imView_introPlayerBG, Direction.LEFT_TO_RIGHT, 0.50f),
                            TransformItem.create(R.id.imView_introPlayerCenter, Direction.RIGHT_TO_LEFT, 0.06f),
                            TransformItem.create(R.id.imView_introPlayer1, Direction.LEFT_TO_RIGHT, 0.08f),
                            TransformItem.create(R.id.imView_introPlayer2, Direction.RIGHT_TO_LEFT, 0.1f),
                            TransformItem.create(R.id.imView_introPlayer3, Direction.RIGHT_TO_LEFT, 0.03f),
                    };
                    break;
                }
                case 1: {
                    pageLayoutResId = R.layout.fragment_intro_stadium;
                    tutorialItems = new TransformItem[]{
                            TransformItem.create(R.id.imView_introStadiumBG, Direction.RIGHT_TO_LEFT, 0.50f),
                            TransformItem.create(R.id.imView_introStadiumCenter, Direction.LEFT_TO_RIGHT, 0.06f),
                            TransformItem.create(R.id.imView_introStadium1, Direction.RIGHT_TO_LEFT, 0.08f),
                            TransformItem.create(R.id.imView_introStadium2, Direction.LEFT_TO_RIGHT, 0.1f),
                            TransformItem.create(R.id.imView_introStadium3, Direction.LEFT_TO_RIGHT, 0.03f),
                    };
                    break;
                }
                case 2: {
                    pageLayoutResId = R.layout.fragment_intro_match;
                    tutorialItems = new TransformItem[]{
                            TransformItem.create(R.id.imView_introMatchBG, Direction.RIGHT_TO_LEFT, 0.7f),
                            TransformItem.create(R.id.imView_introMatchCenter, Direction.LEFT_TO_RIGHT, 0.06f),
                            TransformItem.create(R.id.imView_introMatch1, Direction.RIGHT_TO_LEFT, 0.08f),
                            TransformItem.create(R.id.imView_introMatch2, Direction.LEFT_TO_RIGHT, 0.1f),
                            TransformItem.create(R.id.imView_introMatch3, Direction.LEFT_TO_RIGHT, 0.03f),
                    };
                    break;
                }
                case 3: {
                    pageLayoutResId = R.layout.fragment_intro_preference;
                    tutorialItems = new TransformItem[] {};
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Unknown position: " + position);
                }
            }

            return PageOptions.create(pageLayoutResId, position, tutorialItems);
        }
    };

    private final TutorialPageProvider<Fragment> mTutorialPageProvider = new TutorialPageProvider<Fragment>() {
        @NonNull
        @Override
        public Fragment providePage(int position) {
            switch (position) {
                case 0:
                    return new PlayerIntroFragment();
                case 1:
                    return new StadiumIntroFragment();
                case 2:
                    return new MatchIntroFragment();
                case 3:
                    return new PreferenceIntroFragment();
                default:
                    throw new IllegalArgumentException("Unknown position: " + position);
            }
        }
    };

    private int[] pagesColors;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (pagesColors == null) {
            pagesColors = new int[]{
                    ContextCompat.getColor(getActivity(), android.R.color.holo_blue_dark),
                    ContextCompat.getColor(getActivity(), android.R.color.holo_green_dark),
                    ContextCompat.getColor(getActivity(), android.R.color.holo_orange_dark),
                    ContextCompat.getColor(getActivity(), android.R.color.holo_purple)
            };
        }
        addOnTutorialPageChangeListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
        if (root != null) {
            txtSkip = root.findViewById(R.id.tvSkipCustom);
        }
        return root;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.custom_fragment_intro_adapter;
    }

    @Override
    protected int getIndicatorResId() {
        return R.id.indicatorCustom;
    }

    @Override
    protected int getSeparatorResId() {
        return R.id.separatorCustom;
    }

    @Override
    protected int getButtonSkipResId() {
        return R.id.tvSkipCustom;
    }

    @Override
    protected int getViewPagerResId() {
        return R.id.viewPagerCustom;
    }

    @Override
    protected TutorialOptions provideTutorialOptions() {
        return newTutorialOptionsBuilder(getActivity())
                .setUseAutoRemoveTutorialFragment(false)
                .setPagesColors(pagesColors)
                .setPagesCount(TOTAL_PAGES)
                .setTutorialPageProvider(mTutorialPageOptionsProvider)
                .setIndicatorOptions(IndicatorOptions.newBuilder(getActivity())
                        .setElementSizeRes(R.dimen.intro_indicator_size)
                        .setElementSpacingRes(R.dimen.intro_indicator_spacing)
                        .setElementColorRes(android.R.color.darker_gray)
                        .setSelectedElementColor(Color.LTGRAY)
                        .setRenderer(IndicatorDrawableRenderer.create(getActivity()))
                        .build())
                .setTutorialPageProvider(mTutorialPageProvider)
                .setOnSkipClickListener(mOnSkipClickListener)
                .build();
    }

    @Override
    public void onPageChanged(int position) {
        currentPosition = position;
        if (position == (TOTAL_PAGES - 1)) {
            txtSkip.setText(R.string.activity_intro_start_text);
        } else {
            txtSkip.setText(R.string.activity_intro_next_text);
        }
    }
}
