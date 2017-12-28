package ir.berimbasket.app.ui.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ir.berimbasket.app.ui.home.main.MainFragment;
import ir.berimbasket.app.ui.home.map.MapFragment;
import ir.berimbasket.app.ui.home.profile.ProfileFragment;

public class HomePagerAdapter extends FragmentPagerAdapter {

    final private int pagesNumber = 3;

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
                return new MainFragment();
            case 1:
                return new MapFragment();
            case 2:
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