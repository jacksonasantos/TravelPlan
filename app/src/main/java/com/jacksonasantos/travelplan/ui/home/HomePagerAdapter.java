package com.jacksonasantos.travelplan.ui.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class HomePagerAdapter extends FragmentStateAdapter {

    public HomePagerAdapter(FragmentActivity fa)
    {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) return new HomeTravelFragment();
        else return new HomeVehicleFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}