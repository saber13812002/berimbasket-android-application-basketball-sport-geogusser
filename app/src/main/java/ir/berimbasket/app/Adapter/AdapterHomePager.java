package ir.berimbasket.app.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ir.berimbasket.app.activity.fragment.FragmentHome;
import ir.berimbasket.app.activity.fragment.FragmentMap;
import ir.berimbasket.app.activity.fragment.FragmentProfile;

public class AdapterHomePager extends FragmentPagerAdapter {

    final private int pagesNumber = 3;

    public AdapterHomePager(FragmentManager fragmentManager) {
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
                return new FragmentHome();
            case 1:
                return new FragmentMap();
            case 2:
                return new FragmentProfile();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

}