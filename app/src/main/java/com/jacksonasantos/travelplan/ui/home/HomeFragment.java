package com.jacksonasantos.travelplan.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.jacksonasantos.travelplan.R;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        ViewPager2 viewPager = v.findViewById(R.id.view_pager);
        viewPager.setAdapter(new HomePagerAdapter(getActivity()));
        return v;
    }
}
