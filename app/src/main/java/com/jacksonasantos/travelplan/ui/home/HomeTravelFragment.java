package com.jacksonasantos.travelplan.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Travel;
import com.jacksonasantos.travelplan.dao.TravelExpenses;
import com.jacksonasantos.travelplan.dao.TravelItemExpenses;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.general.InsuranceActivity;
import com.jacksonasantos.travelplan.ui.travel.ItineraryActivity;
import com.jacksonasantos.travelplan.ui.travel.ReservationActivity;
import com.jacksonasantos.travelplan.ui.travel.TravelAchievementListAdapter;
import com.jacksonasantos.travelplan.ui.travel.TravelFuelSupplyListAdapter;
import com.jacksonasantos.travelplan.ui.travel.TravelRouteFragment;
import com.jacksonasantos.travelplan.ui.travel.TravelVehicleListAdapter;
import com.jacksonasantos.travelplan.ui.utility.DateInputMask;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Utils;
import com.jacksonasantos.travelplan.ui.vehicle.FuelSupplyActivity;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class HomeTravelFragment extends Fragment implements View.OnClickListener {

    private ScrollView layerHomeTravel;
    private ConstraintLayout layerTravel;
    private ImageView imTravelStatus;
    private Spinner spTravel;
    private TextView tvNote;
    private TextView tvDeparture;
    private TextView tvReturn;
    private TextView tvDays;

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

    private ConstraintLayout layerFuelSupply;
    private RecyclerView listFuelSupply;

    private ConstraintLayout layerAchievement;
    private RecyclerView listAchievement;

    private ConstraintLayout layerItinerary;
    private RecyclerView listItinerary;

    private ConstraintLayout layerReservation;
    private RecyclerView listReservation;

    private ConstraintLayout layerExpense;
    private RecyclerView listTravelExpenses;

    private ConstraintLayout layerInsurance;
    private RecyclerView listInsuranceExpiration;

    final Globals g = Globals.getInstance();
    final Locale locale = new Locale(g.getLanguage(), g.getCountry());
    final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

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

        layerFuelSupply = v.findViewById(R.id.layerFuelSupply);
        listFuelSupply = v.findViewById(R.id.listFuelSupply);

        layerAchievement = v.findViewById(R.id.layerAchievement);
        listAchievement = v.findViewById(R.id.listAchievement);

        layerItinerary = v.findViewById(R.id.layerItinerary);
        listItinerary = v.findViewById(R.id.listItinerary);

        layerReservation = v.findViewById(R.id.layerReservation);
        listReservation = v.findViewById(R.id.listReservation);

        layerExpense = v.findViewById(R.id.layerExpense);
        listTravelExpenses = v.findViewById(R.id.listTravelExpenses);

        layerInsurance = v.findViewById(R.id.layerInsurance);
        listInsuranceExpiration = v.findViewById(R.id.listInsuranceExpiration);

        layerHomeTravel.setFocusable(false);
        layerHomeTravel.setFocusableInTouchMode(true);
        layerHomeTravel.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);

        onResume();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        final Database mDb = new Database(getActivity());
        mDb.open();

        final List<Travel> travels =  Database.mTravelDao.fetchArrayTravel();
        ArrayAdapter<Travel> adapterTravel = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, travels);
        adapterTravel.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spTravel.setAdapter(adapterTravel);

        if (travels.size()>0) {
            for (int x = 0; x <= travels.size(); x++) {
                if (travels.get(x).getId().equals(g.getIdTravel())) {
                    spTravel.setSelection(x);
                    break;
                }
            }
        }

        final Travel[] travel = {new Travel()};
        spTravel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                travel[0] = (Travel) parent.getItemAtPosition(position);
                g.setIdTravel(travel[0].getId());

                layerTravel.setVisibility(View.VISIBLE);
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
                btnFood.setEnabled(true);
                btnFuel.setEnabled(true);
                btnExtra.setEnabled(true);
                btnTour.setEnabled(true);
                btnTolls.setEnabled(true);
                btnInsurance.setEnabled(true);

                imTravelStatus.setOnClickListener(v -> {
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext(),R.style.MyDialogStyle);
                    int checkedItem = travel[0].getStatus();
                    alertDialogBuilder.setTitle(getString(R.string.change)+ " "+getString(R.string.Travel_Status))
                          .setSingleChoiceItems(R.array.travel_status_array, checkedItem, (dialog, id13) -> travel[0].setStatus(id13))
                          .setPositiveButton(R.string.OK, (dialog, id12) -> {
                              boolean isSave = false;
                              try {
                                  isSave = Database.mTravelDao.updateTravel(travel[0]);
                                  imTravelStatus.setColorFilter(travel[0].getColorStatus(), PorterDuff.Mode.MULTIPLY);
                              } catch (Exception e) {
                                  Toast.makeText(getContext(), R.string.Error_Including_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                              }
                              if (!isSave) {
                                  Toast.makeText(getContext(), R.string.Error_Saving_Data, Toast.LENGTH_LONG).show();
                              }
                          })
                          .setNegativeButton(R.string.Cancel, (dialog, id1) -> dialog.cancel());
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                });
                //TODO - Update Travel Expense Adapter values
                btnFood.setOnClickListener (v -> TravelItemExpenses(v,1));
                btnTolls.setOnClickListener (v -> TravelItemExpenses(v,2));
                btnTour.setOnClickListener (v -> TravelItemExpenses(v,3));
                btnExtra.setOnClickListener (v -> TravelItemExpenses(v,5));
                btnItinerary.setOnClickListener (v -> {
                    Intent intent = new Intent(v.getContext(), ItineraryActivity.class);
                    intent.putExtra("travel_id", travel[0].id);
                    startActivity(intent);
                });
                btnAccommodation.setOnClickListener (v -> {
                    Intent intent = new Intent(v.getContext(), ReservationActivity.class);
                    intent.putExtra("travel_id", travel[0].id);
                    startActivity(intent);
                });
                btnFuel.setOnClickListener (v -> {
                    Intent intent = new Intent(v.getContext(), FuelSupplyActivity.class);
                    intent.putExtra("travel_id", travel[0].id);
                    startActivity(intent);
                });
                btnInsurance.setOnClickListener (v -> {
                    Intent intent = new Intent(v.getContext(), InsuranceActivity.class);
                    intent.putExtra("travel_id", travel[0].id);
                    startActivity(intent);
                });

                // Vehicles has Travel
                final int Show_Header_VehicleTravel = 1  ;
                TravelVehicleListAdapter adapterVehicleTravel = new TravelVehicleListAdapter(Database.mVehicleHasTravelDao.fetchAllVehicleHasTravelByTravel(travel[0].getId() ), getContext(),"Home", Show_Header_VehicleTravel);
                if ( adapterVehicleTravel.getItemCount() > Show_Header_VehicleTravel){
                    layerVehicle.setVisibility(View.VISIBLE);
                    listVehicle.setAdapter(adapterVehicleTravel);
                    listVehicle.setLayoutManager(new LinearLayoutManager(getContext()));
                } else {
                    layerVehicle.setVisibility(View.GONE);
                }

                // Fuel Supply has Travel
                final int Show_Header_FuelSupplyTravel = 1  ;
                TravelFuelSupplyListAdapter adapterFuelSupplyTravel = new TravelFuelSupplyListAdapter(Database.mFuelSupplyDao.fetchAllFuelSupplyHasTravelByTravel(travel[0].getId() ), getContext(),"Home", Show_Header_FuelSupplyTravel);
                if ( adapterFuelSupplyTravel.getItemCount() > Show_Header_FuelSupplyTravel){
                    layerFuelSupply.setVisibility(View.VISIBLE);
                    listFuelSupply.setAdapter(adapterFuelSupplyTravel);
                    listFuelSupply.setLayoutManager(new LinearLayoutManager(getContext()));
                } else {
                    layerFuelSupply.setVisibility(View.GONE);
                }

                // Achievement has Travel
                final int Show_Header_AchievementTravel = 0 ;
                TravelAchievementListAdapter adapterAchievementTravel = new TravelAchievementListAdapter(Database.mAchievementDao.fetchAllAchievementByTravel(travel[0].getId() ), getContext(),"Home", Show_Header_AchievementTravel);
                if ( adapterAchievementTravel.getItemCount() > Show_Header_AchievementTravel){
                    layerAchievement.setVisibility(View.VISIBLE);
                    listAchievement.setAdapter(adapterAchievementTravel);
                    listAchievement.setLayoutManager(new LinearLayoutManager(getContext()));
                } else {
                    layerAchievement.setVisibility(View.GONE);
                }

                // Itinerary
                final int Show_Header_Itinerary = 0;    // 0 - NO SHOW HEADER | 1 - SHOW HEADER
                final int Show_Footer_Itinerary = 1;
                TravelRouteFragment.HomeTravelItineraryListAdapter adapterItinerary = new TravelRouteFragment.HomeTravelItineraryListAdapter(Database.mItineraryDao.fetchAllItineraryByTravel(travel[0].getId() ), getContext(), Show_Header_Itinerary,Show_Footer_Itinerary, true);
                if ( adapterItinerary.getItemCount() > Show_Header_Itinerary+Show_Footer_Itinerary){
                    layerItinerary.setVisibility(View.VISIBLE);
                    listItinerary.setAdapter(adapterItinerary);
                    listItinerary.setLayoutManager(new LinearLayoutManager(getContext()));
                } else {
                    layerItinerary.setVisibility(View.GONE);
                }

                // Reservation
                final int Show_Header_Reservation = 0; // 0 - NO SHOW HEADER | 1 - SHOW HEADER
                HomeTravelReservationListAdapter adapterReservation = new HomeTravelReservationListAdapter( Database.mReservationDao.fetchAllReservationByTravel(travel[0].getId()), getContext(), Show_Header_Reservation);
                if ( adapterReservation.getItemCount() > Show_Header_Reservation){
                    layerReservation.setVisibility(View.VISIBLE);
                    listReservation.setAdapter(adapterReservation);
                    listReservation.setLayoutManager(new LinearLayoutManager(getContext()));
                } else {
                    layerReservation.setVisibility(View.GONE);
                }

                // Expenses - LayerExpense
                final int Show_Header_SummaryExpense = 1; // 0 - NO SHOW HEADER | 1 - SHOW HEADER
                final int Show_Footer_SummaryExpense = 1; // 0 - NO SHOW FOOTER | 1 - SHOW FOOTER
                HomeTravelSummaryExpenseListAdapter adapterTravelExpense = new HomeTravelSummaryExpenseListAdapter( Database.mSummaryTravelExpenseDao.findTravelExpense(travel[0].getId() ), getContext(), Show_Header_SummaryExpense, Show_Footer_SummaryExpense);
                if ( adapterTravelExpense.getItemCount() > Show_Header_SummaryExpense+Show_Footer_SummaryExpense){
                    layerExpense.setVisibility(View.VISIBLE);
                    listTravelExpenses.setAdapter(adapterTravelExpense);
                    listTravelExpenses.setLayoutManager(new LinearLayoutManager(getContext()));
                } else {
                    layerExpense.setVisibility(View.GONE);
                }

                // Insurance - layerInsurance
                final int Show_Header_Insurance = 0; // 0 - NO SHOW HEADER | 1 - SHOW HEADER
                HomeInsuranceListAdapter adapterInsurance = new HomeInsuranceListAdapter(Database.mInsuranceDao.findReminderInsurance("T", travel[0].getId() ), getContext(), Show_Header_Insurance);
                if ( adapterInsurance.getItemCount() > Show_Header_Insurance){
                    layerInsurance.setVisibility(View.VISIBLE);
                    listInsuranceExpiration.setAdapter(adapterInsurance);
                    listInsuranceExpiration.setLayoutManager(new LinearLayoutManager(getContext()));
                } else {
                    layerInsurance.setVisibility(View.GONE);
                }

                adapterVehicleTravel.notifyDataSetChanged();
                adapterAchievementTravel.notifyDataSetChanged();
                adapterReservation.notifyDataSetChanged();
                adapterTravelExpense.notifyDataSetChanged();
                adapterInsurance.notifyDataSetChanged();
            }

            public void TravelItemExpenses(View v, int expense_type ) {

                LayoutInflater li = LayoutInflater.from(v.getContext());
                View promptsView = li.inflate(R.layout.dialog_travel_item_expenses, null);

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                alertDialogBuilder.setView(promptsView);

                final HomeTravelItemExpensesListAdapter[] adapterTravelItemExpenses = new HomeTravelItemExpensesListAdapter[1];
                final Spinner spinExpenseType = promptsView.findViewById(R.id.spinExpenseType);
                final EditText etExpectedValue = promptsView.findViewById(R.id.etExpectedValue);
                final EditText etNote = promptsView.findViewById(R.id.etNote);

                final EditText etExpenseDate = promptsView.findViewById(R.id.etExpenseDate);
                final EditText etExpenseItemRealizedValue = promptsView.findViewById(R.id.etExpenseItemRealizedValue);
                final EditText etExpenseItemNote = promptsView.findViewById(R.id.etExpenseItemNote);

                final RecyclerView rvTravelExpenseItem = promptsView.findViewById(R.id.rvTravelExpenseItem);
                List<TravelExpenses> travelExpensesList = Database.mTravelExpensesDao.fetchAllTravelExpensesByTravelType(travel[0].getId(), expense_type);

                if( travelExpensesList.size() == 0 ){
                    TravelExpenses te = new TravelExpenses();
                    te.setTravel_id(travel[0].getId());
                    te.setExpense_type(expense_type);
                    Database.mTravelExpensesDao.addTravelExpenses(te);
                    travelExpensesList = Database.mTravelExpensesDao.fetchAllTravelExpensesByTravelType(travel[0].getId(), expense_type);
                }
                spinExpenseType.setSelection(expense_type);
                double vlrExpectedValue = 0.0;
                for (int x = 0; x < travelExpensesList.size(); x++) {
                    vlrExpectedValue = vlrExpectedValue + travelExpensesList.get(x).getExpected_value();
                }
                etExpectedValue.setText(currencyFormatter.format(vlrExpectedValue));
                etNote.setText(travelExpensesList.get(0).getNote());

                etExpenseDate.addTextChangedListener(new DateInputMask(etExpenseDate));

                adapterTravelItemExpenses[0] = new HomeTravelItemExpensesListAdapter(Database.mTravelItemExpensesDao.fetchTravelItemExpensesByExpenseType( travelExpensesList.get(0).getTravel_id(), travelExpensesList.get(0).getExpense_type()), requireContext());
                rvTravelExpenseItem.setAdapter(adapterTravelItemExpenses[0]);
                rvTravelExpenseItem.setLayoutManager(new LinearLayoutManager(requireContext()));

                final List<TravelExpenses> finalTravelExpensesList = travelExpensesList;
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton(R.string.OK, (dialog, id) -> {
                            boolean isSave = false;

                            TravelItemExpenses TIE = new TravelItemExpenses();

                            TIE.setExpense_type(finalTravelExpensesList.get(0).getExpense_type());
                            TIE.setTravel_id(travel[0].getId());
                            TIE.setExpense_date(Utils.stringToDate(etExpenseDate.getText().toString()));
                            if (!etExpenseItemRealizedValue.getText().toString().isEmpty()) {
                                TIE.setRealized_value(Double.parseDouble(etExpenseItemRealizedValue.getText().toString()));
                            }
                            TIE.setNote(etExpenseItemNote.getText().toString());

                            if (TIE.getExpense_date() != null ||
                                TIE.getRealized_value() != null) {

                                try {
                                    isSave = Database.mTravelItemExpensesDao.addTravelItemExpenses(TIE);
                                } catch (Exception e) {
                                    Toast.makeText(requireContext(), R.string.Error_Including_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                                }

                                if (!isSave) {
                                    Toast.makeText(requireContext(), R.string.Error_Saving_Data, Toast.LENGTH_LONG).show();
                                } else {
                                    adapterTravelItemExpenses[0] = new HomeTravelItemExpensesListAdapter(Database.mTravelItemExpensesDao.fetchTravelItemExpensesByExpenseType(finalTravelExpensesList.get(0).getTravel_id(),finalTravelExpensesList.get(0).getExpense_type()), requireContext());
                                    rvTravelExpenseItem.setAdapter(adapterTravelItemExpenses[0]);
                                    rvTravelExpenseItem.setLayoutManager(new LinearLayoutManager(requireContext()));
                                }
                            }
                        })
                        .setNegativeButton(R.string.Cancel, (dialog, id) -> dialog.cancel());
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                travel[0] = null;
            }
        });
        adapterTravel.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {}
}