package ir.berimbasket.app.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ir.berimbasket.app.activity.fragment.FragmentAccount;
import ir.berimbasket.app.activity.fragment.FragmentMap;
import ir.berimbasket.app.activity.fragment.FragmentMatchBoard;

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
                FragmentMap fragmentMap = new FragmentMap();
                return fragmentMap;
            case 1:
                FragmentMatchBoard FragmentMatchBoard = new FragmentMatchBoard();
                return FragmentMatchBoard;
            case 2:
                FragmentAccount fragmentAccount = new FragmentAccount();
                return fragmentAccount;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

}