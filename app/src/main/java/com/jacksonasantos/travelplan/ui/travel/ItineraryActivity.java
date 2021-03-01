package com.jacksonasantos.travelplan.ui.travel;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Database;
import com.jacksonasantos.travelplan.dao.Itinerary;
import com.jacksonasantos.travelplan.dao.Travel;
import com.jacksonasantos.travelplan.ui.home.HomeTravelItineraryListAdapter;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.List;

public class ItineraryActivity extends AppCompatActivity {
    private Spinner spinItineraryTravel;
    private Integer nrSpinItineraryTravel;
    private ImageView imTravelStatus;
    private TextView tvNote;
    private TextView tvDeparture;
    private TextView tvReturn;
    private TextView tvDays;

    private ConstraintLayout layerItinerary;
    private RecyclerView listItinerary;
    private LinearLayout totalTravelItinerary;

    private FrameLayout container;

    private Itinerary itinerary;

    private boolean clearMap;

    @SuppressLint("WrongViewCast")
    @RequiresApi(api = Build.VERSION_CODES.N)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.Itinerary);
        setContentView(R.layout.activity_itinerary);

        itinerary = new Itinerary();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.getInt( "itinerary_id") > 0) {
                itinerary.setId(extras.getInt("itinerary_id"));
                itinerary = Database.mItineraryDao.fetchItineraryById(itinerary.getId());
            }
            if (extras.getInt( "travel_id") > 0) {
                itinerary.setTravel_id(extras.getInt("travel_id"));
            }
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        spinItineraryTravel = findViewById(R.id.spinItineraryTravel);
        imTravelStatus = findViewById(R.id.imTravelStatus);
        tvNote = findViewById(R.id.tvNote);
        tvDeparture = findViewById(R.id.tvDeparture);
        tvReturn = findViewById(R.id.tvReturn);
        tvDays = findViewById(R.id.tvDays);

        layerItinerary = findViewById(R.id.layerItinerary);
        listItinerary = findViewById(R.id.listItinerary);
        totalTravelItinerary = findViewById(R.id.totalTravelItinerary);
    }

    @Override
    protected void onResume() {
        super.onResume();

        final List<Travel> travels =  Database.mTravelDao.fetchArrayTravel();
        ArrayAdapter<Travel> adapterT = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, travels);
        adapterT.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinItineraryTravel.setAdapter(adapterT);

        final Travel[] travel = {new Travel()};
        spinItineraryTravel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                travel[0] = (Travel) parent.getItemAtPosition(position);
                nrSpinItineraryTravel = travel[0].getId();
                imTravelStatus.setImageResource(R.drawable.ic_ball );
                imTravelStatus.setColorFilter(travel[0].getColorStatus(), PorterDuff.Mode.MULTIPLY);
                tvNote.setText(travel[0].getNote());
                tvDeparture.setText(Utils.dateToString(travel[0].getDeparture_date()));
                tvReturn.setText(Utils.dateToString(travel[0].getReturn_date()));
                long d1 = travel[0].getDeparture_date().getTime();
                long d2 = travel[0].getReturn_date().getTime();
                int dias = (int) ((d2 - d1) / (24*60*60*1000));     // calculate in days
                tvDays.setText((dias + 1) + " "+getString(R.string.days));

                itinerary.setTravel_id(nrSpinItineraryTravel);
                if (itinerary != null) {
                    nrSpinItineraryTravel=itinerary.getTravel_id();
                    Travel t1 = Database.mTravelDao.fetchTravelById(nrSpinItineraryTravel);
                    if (t1.getDescription()!=null) {
                        for (int x = 0; x <= spinItineraryTravel.getAdapter().getCount(); x++) {
                            if (spinItineraryTravel.getAdapter().getItem(x).toString().equals(t1.getDescription())) {
                                spinItineraryTravel.setSelection(x);
                                break;
                            }
                        }
                    }

                    HomeTravelItineraryListAdapter adapterItinerary = new HomeTravelItineraryListAdapter(Database.mItineraryDao.fetchAllItineraryByTravel(itinerary.getTravel_id() ), getApplicationContext());
                    if ( adapterItinerary.getItemCount() > 0){
                        layerItinerary.setVisibility(View.VISIBLE);
                        listItinerary.setAdapter(adapterItinerary);
                        listItinerary.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                        long vDistance = 0;
                        long nrTime = 0;
                        String vTime;
                        for (int x=0; x < adapterItinerary.getItemCount(); x++) {
                            vDistance = vDistance + adapterItinerary.mItinerary.get(x).getDistance();

                            String[] time = adapterItinerary.mItinerary.get(x).getTime().split(":");
                            long hr = Long.parseLong(time[0]);
                            long min = Long.parseLong(time[1]);
                            nrTime += (min * 60) + (hr * 3600);
                        }
                        long totalHr = nrTime / 3600;
                        nrTime -= (totalHr * 3600);
                        long totalMin = nrTime / 60;
                        vTime = String.format("%3d:%02d", totalHr, totalMin);

                        View vT = LayoutInflater.from(getApplicationContext()).inflate(R.layout.fragment_home_travel_item_itinerary, (ViewGroup) getWindow().getDecorView(), false);
                        totalTravelItinerary.removeAllViews();
                        TextView totSource = vT.findViewById(R.id.txtSource);
                        TextView totDistance = vT.findViewById(R.id.txtDistance);
                        TextView totTime = vT.findViewById(R.id.txtTime);
                        totSource.setText("      TOTAL");
                        totDistance.setText(Long.toString(vDistance));
                        totTime.setText(vTime);
                        totalTravelItinerary.addView(vT);
                        totalTravelItinerary.setBackgroundColor(Color.rgb(209,193,233));
                        clearMap = false;
                    } else {
                        layerItinerary.setVisibility(View.GONE);
                        clearMap = true;
                    }
                    // Mostrar mapa
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                                .replace(R.id.container, new TravelRouteFragment(clearMap),null);
                    fragmentTransaction.commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {  }
        });
        adapterT.notifyDataSetChanged();
    }
}