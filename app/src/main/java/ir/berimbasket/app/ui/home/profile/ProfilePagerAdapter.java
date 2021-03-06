package ir.berimbasket.app.ui.home.profile;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ir.berimbasket.app.R;
import ir.berimbasket.app.ui.home.profile.mission.MissionFragment;
import ir.berimbasket.app.ui.home.profile.specification.SpecificationFragment;

public class ProfilePagerAdapter extends FragmentStatePagerAdapter {

    private String pagesTitle[] = new String[2];

    public ProfilePagerAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        pagesTitle[0] = context.getString(R.string.adapter_profile_pager_missions);
        pagesTitle[1] = context.getString(R.string.adapter_profile_pager_specification);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MissionFragment();
            case 1:
                return new SpecificationFragment();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pagesTitle[position];
    }

}