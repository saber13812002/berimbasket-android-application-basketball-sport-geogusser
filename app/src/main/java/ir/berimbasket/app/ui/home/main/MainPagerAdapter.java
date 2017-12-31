package ir.berimbasket.app.ui.home.main;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ir.berimbasket.app.R;
import ir.berimbasket.app.ui.home.main.match.MatchFragment;
import ir.berimbasket.app.ui.home.main.player.PlayerFragment;
import ir.berimbasket.app.ui.home.main.stadium.StadiumFragment;

public class MainPagerAdapter extends FragmentStatePagerAdapter {

    private String pagesTitle[] = new String[3];

    MainPagerAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        pagesTitle[0] = context.getString(R.string.fragment_main_tab_stadium);
        pagesTitle[1] = context.getString(R.string.fragment_main_tab_player);
        pagesTitle[2] = context.getString(R.string.fragment_main_tab_match);
    }

    @Override
    public int getCount() {
        return pagesTitle.length;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new StadiumFragment();
            case 1:
                return new PlayerFragment();
            case 2:
                return new MatchFragment();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pagesTitle[position];
    }
}
