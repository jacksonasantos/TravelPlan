package com.jacksonasantos.travelplan.ui.home;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class HomeViewPagerAdapter extends FragmentStatePagerAdapter {

    public HomeViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new HomeVehicleFragment();
            case 1:
                return new HomeTravelFragment();
            default:
        }
        return null;
    }

    @Override
    public int getCount() {return 2; }
}