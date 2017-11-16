package ir.berimbasket.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ir.berimbasket.app.activity.fragment.FragmentHome;
import ir.berimbasket.app.activity.fragment.FragmentMissions;
import ir.berimbasket.app.activity.fragment.FragmentPlayerSpecification;

public class AdapterProfilePager extends FragmentPagerAdapter {

    public AdapterProfilePager(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentMissions();
            case 1:
                return new FragmentPlayerSpecification();
        }
        return null;
    }

    private String pagesTitle[] = {"ماموریت ها", "مشخصات"};

    @Override
    public CharSequence getPageTitle(int position) {
        return pagesTitle[position];
    }

}