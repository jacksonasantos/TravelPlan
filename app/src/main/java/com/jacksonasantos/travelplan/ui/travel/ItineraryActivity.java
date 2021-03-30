package com.jacksonasantos.travelplan.ui.travel;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Travel;

public class ItineraryActivity extends AppCompatActivity {

    private Travel travel;

    @SuppressLint("WrongViewCast")
    @RequiresApi(api = Build.VERSION_CODES.N)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.Itinerary);
        setContentView(R.layout.activity_itinerary);

        travel = new Travel();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.getInt( "travel_id") > 0) {
                travel.setId(extras.getInt("travel_id"));
            }
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                    .replace(R.id.container, new TravelRouteFragment(true, travel.getId()),null);
        fragmentTransaction.commit();
    }
}