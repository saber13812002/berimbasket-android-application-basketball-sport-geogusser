package ir.berimbasket.app.ui.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ir.berimbasket.app.ui.home.main.match.MatchFragment;
import ir.berimbasket.app.ui.home.main.player.PlayerFragment;
import ir.berimbasket.app.ui.home.main.stadium.StadiumFragment;
import ir.berimbasket.app.ui.home.map.MapFragment;
import ir.berimbasket.app.ui.home.profile.ProfileFragment;

public class HomePagerAdapter extends FragmentPagerAdapter {

    final private int pagesNumber = 5;

    public HomePagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return pagesNumber;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MapFragment();
            case 1:
                return new StadiumFragment();
            case 2:
                return new PlayerFragment();
            case 3:
                return new MatchFragment();
            case 4:
                return new ProfileFragment();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

}