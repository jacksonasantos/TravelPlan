package com.jacksonasantos.travelplan.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Database;
import com.jacksonasantos.travelplan.dao.Travel;
import com.jacksonasantos.travelplan.ui.general.InsuranceActivity;
import com.jacksonasantos.travelplan.ui.travel.ItineraryActivity;
import com.jacksonasantos.travelplan.ui.travel.ReservationActivity;
import com.jacksonasantos.travelplan.ui.travel.VehicleTravelListAdapter;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Utils;
import com.jacksonasantos.travelplan.ui.vehicle.FuelSupplyActivity;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class HomeTravelFragment extends Fragment implements View.OnClickListener {

    private ConstraintLayout layerHomeTravel;
    private ConstraintLayout layerTravel;
    private ImageView imTravelStatus;
    private Spinner spTravel;
    private TextView tvNote;
    private TextView tvDeparture;
    private TextView tvReturn;
    private TextView tvDays;

    private LinearLayout layerBtnMenu;
    private ImageButton btnItinerary;
    private ImageButton btnAccommodation;
    private ImageButton btnFood;
    private ImageButton btnFuel;
    private ImageButton btnExtra;
    private ImageButton btnTour;
    private ImageButton btnTolls;
    private ImageButton btnInsurance;

    private ConstraintLayout layerVehicle;
    private RecyclerView listVehicle;

    private ConstraintLayout layerItinerary;
    private RecyclerView listItinerary;
    private LinearLayout totalTravelItinerary;

    private ConstraintLayout layerReservation;
    private RecyclerView listReservation;

    private ConstraintLayout layerExpense;
    private ConstraintLayout labelTravelExpenses;
    private RecyclerView listTravelExpenses;
    private ConstraintLayout totalTravelExpenses;

    private ConstraintLayout layerInsurance;
    private RecyclerView listInsuranceExpiration;

    Globals g = Globals.getInstance();
    Locale locale = new Locale(g.getLanguage(), g.getCountry());
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_home_travel, container, false);
        layerHomeTravel = v.findViewById(R.id.layerHomeTravel);
        layerTravel = v.findViewById(R.id.layerTravel);
        spTravel = v.findViewById(R.id.spTravel);
        imTravelStatus = v.findViewById(R.id.imTravelStatus);
        tvNote = v.findViewById(R.id.tvNote);
        tvDeparture = v.findViewById(R.id.tvDeparture);
        tvReturn = v.findViewById(R.id.tvReturn);
        tvDays = v.findViewById(R.id.tvDays);

        layerBtnMenu = v.findViewById(R.id.layerBtnMenu);
        btnItinerary = v.findViewById(R.id.btnItinerary);
        btnAccommodation = v.findViewById(R.id.btnAccommodation);
        btnFood = v.findViewById(R.id.btnFood);
        btnFuel = v.findViewById(R.id.btnFuel);
        btnExtra = v.findViewById(R.id.btnExtra);
        btnTour = v.findViewById(R.id.btnTour);
        btnTolls = v.findViewById(R.id.btnTolls);
        btnInsurance = v.findViewById(R.id.btnInsurance);

        layerVehicle = v.findViewById(R.id.layerVehicle);
        listVehicle = v.findViewById(R.id.listVehicle);

        layerItinerary = v.findViewById(R.id.layerItinerary);
        listItinerary = v.findViewById(R.id.listItinerary);
        totalTravelItinerary = v.findViewById(R.id.totalTravelItinerary);

        layerReservation = v.findViewById(R.id.layerReservation);
        listReservation = v.findViewById(R.id.listReservation);

        layerExpense = v.findViewById(R.id.layerExpense);
        labelTravelExpenses = v.findViewById(R.id.labelTravelExpenses);
        listTravelExpenses = v.findViewById(R.id.listTravelExpenses);
        totalTravelExpenses = v.findViewById(R.id.totalTravelExpenses);

        layerInsurance = v.findViewById(R.id.layerInsurance);
        listInsuranceExpiration = v.findViewById(R.id.listInsuranceExpiration);

        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume() {
        super.onResume();
        final Database mDb = new Database(getActivity());
        mDb.open();

        final List<Travel> travels =  Database.mTravelDao.fetchArrayTravel();
        ArrayAdapter<Travel> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, travels);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spTravel.setAdapter(adapter);

        final Travel[] travel = {new Travel()};
        spTravel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Recupera dados da viagem selecionada no Spinner - layerTravel
                layerTravel.setVisibility(View.VISIBLE);
                travel[0] = (Travel) parent.getItemAtPosition(position);
                imTravelStatus.setImageResource(R.drawable.ic_ball );
                imTravelStatus.setColorFilter(travel[0].getColorStatus(), PorterDuff.Mode.MULTIPLY);
                tvNote.setText(travel[0].getNote());
                tvDeparture.setText(Utils.dateToString(travel[0].getDeparture_date()));
                tvReturn.setText(Utils.dateToString(travel[0].getReturn_date()));
                long d1 = travel[0].getDeparture_date().getTime();
                long d2 = travel[0].getReturn_date().getTime();
                int dias = (int) ((d2 - d1) / (24*60*60*1000));     // calculate in days
                tvDays.setText((dias + 1) + " "+getString(R.string.days));

                btnItinerary.setEnabled(true);
                btnAccommodation.setEnabled(true);
                btnFood.setEnabled(false);
                btnFuel.setEnabled(true);
                btnExtra.setEnabled(false);
                btnTour.setEnabled(false);
                btnTolls.setEnabled(false);
                btnInsurance.setEnabled(true);

                btnItinerary.setOnClickListener (new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       /* FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                                .replace(R.id.container, new TravelRouteFragment(),"Travel");
                        fragmentTransaction.addToBackStack("Travel").commit();*/
                        Intent intent = new Intent(v.getContext(), ItineraryActivity.class);
                        intent.putExtra("travel_id", travel[0].id);
                        startActivity(intent);
                    }
                });
                btnAccommodation.setOnClickListener (new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), ReservationActivity.class);
                        intent.putExtra("travel_id", travel[0].id);
                        startActivity(intent);
                    }
                });
                btnFuel.setOnClickListener (new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), FuelSupplyActivity.class);
                        intent.putExtra("travel_id", travel[0].id);
                        startActivity(intent);
                    }
                });
                btnInsurance.setOnClickListener (new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), InsuranceActivity.class);
                        intent.putExtra("travel_id", travel[0].id);
                        startActivity(intent);
                    }
                });

                // Vehicles has Travel
                VehicleTravelListAdapter adapterVehicle = new VehicleTravelListAdapter(Database.mVehicleHasTravelDao.fetchAllVehicleHasTravelByTravel(travel[0].getId() ), getContext(),"Home");
                if ( adapterVehicle.getItemCount() > 0){
                    layerVehicle.setVisibility(View.VISIBLE);
                    listVehicle.setAdapter(adapterVehicle);
                    listVehicle.setLayoutManager(new LinearLayoutManager(getContext()));
                } else {
                    layerVehicle.setVisibility(View.GONE);
                }

                // Itinerary
                HomeTravelItineraryListAdapter adapterItinerary = new HomeTravelItineraryListAdapter(Database.mItineraryDao.fetchAllItineraryByTravel(travel[0].getId() ), getContext());
                if ( adapterItinerary.getItemCount() > 0){
                    layerItinerary.setVisibility(View.VISIBLE);
                    listItinerary.setAdapter(adapterItinerary);
                    listItinerary.setLayoutManager(new LinearLayoutManager(getContext()));

                    long vDaily = 0;
                    long vDistance = 0;
                    long nrTime = 0;
                    String vTime;
                    for (int x=0; x < adapterItinerary.getItemCount(); x++) {
                        vDistance = vDistance + adapterItinerary.mItinerary.get(x).getDistance();
                        vDaily = vDaily + adapterItinerary.mItinerary.get(x).getDaily();
                        String[] time = adapterItinerary.mItinerary.get(x).getTime().split(":");
                        long hr = Long.parseLong(time[0]);
                        long min = Long.parseLong(time[1]);
                        nrTime += (min * 60) + (hr * 3600);

                    }
                    long totalHr = nrTime / 3600;
                    nrTime -= (totalHr * 3600);
                    long totalMin = nrTime / 60;
                    vTime = String.format("%3d:%02d", totalHr, totalMin);

                    View vT = LayoutInflater.from(getContext()).inflate(R.layout.fragment_home_travel_item_itinerary, parent, false);
                    totalTravelItinerary.removeAllViews();
                    TextView totSource = vT.findViewById(R.id.txtSource);
                    TextView totDaily = vT.findViewById(R.id.txtDaily);
                    TextView totDistance = vT.findViewById(R.id.txtDistance);
                    TextView totTime = vT.findViewById(R.id.txtTime);
                    totSource.setText("      TOTAL");
                    totDaily.setText(Long.toString(vDaily));
                    totDistance.setText(Long.toString(vDistance));
                    totTime.setText(vTime);
                    totalTravelItinerary.addView(vT);
                    totalTravelItinerary.setBackgroundColor(Color.rgb(209,193,233));
                } else {
                    layerItinerary.setVisibility(View.GONE);
                }

                // Reservation
                HomeTravelReservationListAdapter adapterReservation = new HomeTravelReservationListAdapter(Database.mReservationDao.fetchAllReservationByTravel(travel[0].getId() ), getContext());
                if ( adapterReservation.getItemCount() > 0){
                    layerReservation.setVisibility(View.VISIBLE);
                    listReservation.setAdapter(adapterReservation);
                    listReservation.setLayoutManager(new LinearLayoutManager(getContext()));
                } else {
                    layerReservation.setVisibility(View.GONE);
                }

                // Expenses - LayerExpense
                HomeTravelSummaryExpenseListAdapter adapterTravelExpense = new HomeTravelSummaryExpenseListAdapter( Database.mSummaryTravelExpenseDao.findTravelExpense(travel[0].getId() ), getContext(), travel[0].getId());
                if ( adapterTravelExpense.getItemCount() > 0){
                    layerExpense.setVisibility(View.VISIBLE);

                    View vL = LayoutInflater.from(getContext()).inflate(R.layout.fragment_home_travel_item_expense, parent, false);
                    labelTravelExpenses.removeAllViews();
                    TextView lblExpense = vL.findViewById(R.id.txtExpense);
                    TextView lblExpectedValue = vL.findViewById(R.id.txtExpectedValue);
                    TextView lblRealizedValue = vL.findViewById(R.id.txtRealizedValue);
                    lblExpense.setText("");
                    lblExpectedValue.setText(getString(R.string.Expected));
                    lblRealizedValue.setText(getString(R.string.Realized));
                    labelTravelExpenses.addView(vL);
                    labelTravelExpenses.setBackgroundColor(Color.rgb(209,193,233));

                    listTravelExpenses.setAdapter(adapterTravelExpense);
                    listTravelExpenses.setLayoutManager(new LinearLayoutManager(getContext()));

                    double vExpected = 0;
                    double vRealized = 0;
                    for (int x=0; x < adapterTravelExpense.getItemCount(); x++) {
                        vExpected = vExpected + adapterTravelExpense.mSummaryTravelExpense.get(x).getExpected_value();
                        vRealized = vRealized + adapterTravelExpense.mSummaryTravelExpense.get(x).getRealized_value();
                    }
                    View vT = LayoutInflater.from(getContext()).inflate(R.layout.fragment_home_travel_item_expense, parent, false);
                    totalTravelExpenses.removeAllViews();
                    TextView totExpense = vT.findViewById(R.id.txtExpense);
                    TextView totExpectedValue = vT.findViewById(R.id.txtExpectedValue);
                    TextView totRealizedValue = vT.findViewById(R.id.txtRealizedValue);
                    totExpense.setText("      TOTAL");
                    totExpectedValue.setText(currencyFormatter.format(vExpected));
                    totRealizedValue.setText(currencyFormatter.format(vRealized));
                    totalTravelExpenses.addView(vT);
                    totalTravelExpenses.setBackgroundColor(Color.rgb(209,193,233));
                } else {
                    layerExpense.setVisibility(View.GONE);
                }

                // Insurance - layerInsurance
                HomeInsuranceListAdapter adapterInsurance = new HomeInsuranceListAdapter(Database.mInsuranceDao.findReminderInsurance("T", travel[0].getId() ), getContext());
                if ( adapterInsurance.getItemCount() > 0){
                    layerInsurance.setVisibility(View.VISIBLE);
                    listInsuranceExpiration.setAdapter(adapterInsurance);
                    listInsuranceExpiration.setLayoutManager(new LinearLayoutManager(getContext()));
                } else {
                    layerInsurance.setVisibility(View.GONE);
                }

                adapterVehicle.notifyDataSetChanged();
                adapterReservation.notifyDataSetChanged();
                adapterTravelExpense.notifyDataSetChanged();
                adapterInsurance.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                travel[0] = null;
            }
        });
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {}
}