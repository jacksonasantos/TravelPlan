package com.jacksonasantos.travelplan.ui.travel;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Travel;

import java.util.Objects;

public class ItineraryActivity extends AppCompatActivity {

    private Travel travel;
    private String txtSearch;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    String result;

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
            if (!Objects.equals(extras.getString("local_search"), "")){
                txtSearch = extras.getString("local_search");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction()
                                             .replace( R.id.container,
                                                       new TravelRouteFragment(true,
                                                                               travel.getId(),
                                                                               txtSearch),
                                                     "Tag");
        fragmentTransaction.commit();
    }

    @Override
    public void finish() {
        TravelRouteFragment fragment = (TravelRouteFragment) fragmentManager.findFragmentByTag("Tag");
        Bundle mArg = fragment != null ? fragment.getArguments() : null;
        result = mArg != null ? mArg.getString("point_marker", "") : null;

        Intent i = new Intent();
        i.putExtra("resulted_value", result);
        setResult(124, i);

        super.finish();
    }
}