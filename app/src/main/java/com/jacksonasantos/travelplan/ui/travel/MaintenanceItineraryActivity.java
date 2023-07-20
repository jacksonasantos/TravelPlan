package com.jacksonasantos.travelplan.ui.travel;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Travel;

import java.util.Objects;

public class MaintenanceItineraryActivity extends AppCompatActivity {

    private Travel travel;
    private String txtSearch;
    private int idSearch;
    private boolean flgAchievement = false;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    String result, resultFeature, resultAddress, resultState, resultCity, resultCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.Itinerary);
        setContentView(R.layout.activity_itinerary);

        travel = new Travel();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.getBoolean("flg_achievement")){
                flgAchievement = true;
            }
            if (extras.getInt( "travel_id") > 0) {
                travel.setId(extras.getInt("travel_id"));
            } else {
                travel.setId(null);
            }
            if (!Objects.equals(extras.getString("local_search"), "") && extras.getString("local_search")!=null){
                txtSearch = extras.getString("local_search");
                idSearch = 0;
            }
            if (!Objects.equals(extras.getString("local_search_source"), "") && extras.getString("local_search_source")!=null){
                txtSearch = extras.getString("local_search_source");
                idSearch = 1;
            }
            if (!Objects.equals(extras.getString("local_search_target"), "") && extras.getString("local_search_target")!=null){
                txtSearch = extras.getString("local_search_target");
                idSearch = 2;
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
                                                                               txtSearch,
                                                                               flgAchievement),
                                                     "Tag");
        fragmentTransaction.commit();
    }

    @Override
    public void finish() {
        TravelRouteFragment fragment = (TravelRouteFragment) fragmentManager.findFragmentByTag("Tag");
        Bundle mArg = fragment != null ? fragment.getArguments() : null;
        result = mArg != null ? mArg.getString("point_marker", "") : null;
        resultFeature = mArg != null ? mArg.getString("feature_marker", "") : null;
        resultAddress = mArg != null ? mArg.getString("address_marker", "") : null;
        resultState = mArg != null ? mArg.getString("state_marker", "") : null;
        resultCity = mArg != null ? mArg.getString("city_marker", "") : null;
        resultCountry = mArg != null ? mArg.getString("country_marker", "") : null;

        Intent i = new Intent();
        if (result != null) {
            i.putExtra("resulted_value", result);
            i.putExtra("resulted_feature", resultFeature);
            i.putExtra("resulted_address", resultAddress);
            i.putExtra("resulted_city", resultCity);
            i.putExtra("resulted_state", resultState);
            i.putExtra("resulted_country", resultCountry);
            switch (idSearch) {
                case 0:
                    setResult(124, i);
                    break;
                case 1:
                    setResult(125, i);
                    break;
                case 2:
                    setResult(126, i);
                    break;
                default:
                    break;
            }
        }
        if (flgAchievement) {
            i.putExtra("resulted_return", "");
            setResult(120, i);
        }
        super.finish();
    }
}