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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Travel;
import com.jacksonasantos.travelplan.dao.VehicleHasTravel;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.general.InsuranceActivity;
import com.jacksonasantos.travelplan.ui.travel.ItineraryHasTransportActivity;
import com.jacksonasantos.travelplan.ui.travel.ItineraryHasTransportListAdapter;
import com.jacksonasantos.travelplan.ui.travel.MaintenanceItineraryActivity;
import com.jacksonasantos.travelplan.ui.travel.ReservationActivity;
import com.jacksonasantos.travelplan.ui.travel.ReservationListAdapter;
import com.jacksonasantos.travelplan.ui.travel.TourActivity;
import com.jacksonasantos.travelplan.ui.travel.TravelAchievementListAdapter;
import com.jacksonasantos.travelplan.ui.travel.TravelExpensesListAdapter;
import com.jacksonasantos.travelplan.ui.travel.TravelFuelSupplyListAdapter;
import com.jacksonasantos.travelplan.ui.travel.TravelRouteFragment;
import com.jacksonasantos.travelplan.ui.travel.TourListAdapter;
import com.jacksonasantos.travelplan.ui.travel.TravelVehicleListAdapter;
import com.jacksonasantos.travelplan.ui.travel.TravelVehicleStatusListAdapter;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Utils;
import com.jacksonasantos.travelplan.ui.vehicle.FuelSupplyActivity;

import java.util.List;

public class HomeTravelFragment extends Fragment implements View.OnClickListener {

    private ScrollView layerHomeTravel;
    private ConstraintLayout layerTravel;
    private ImageView imTravelStatus;
    private int rbTravelStatus;
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
    private ImageButton btnTransport;

    private ConstraintLayout layerExpense;
    private RecyclerView listTravelExpenses;

    private ConstraintLayout layerVehicle;
    private RecyclerView listVehicle;

    private ConstraintLayout layerItineraryHasTransport;
    private RecyclerView listItineraryHasTransport;

    private ConstraintLayout layerTour;
    private RecyclerView listTour;

    private ConstraintLayout layerFuelSupply;
    private RecyclerView listFuelSupply;

    private ConstraintLayout layerAchievement;
    private RecyclerView listAchievement;

    private ConstraintLayout layerItinerary;
    private RecyclerView listItinerary;

    private ConstraintLayout layerReservation;
    private RecyclerView listReservation;

    private ConstraintLayout layerInsurance;
    private RecyclerView listInsuranceExpiration;

    final Globals g = Globals.getInstance();

    @Override
    public View onCreateView(final LayoutInflater inflater,
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
        btnTransport = v.findViewById(R.id.btnTransport);

        layerExpense = v.findViewById(R.id.layerExpense);
        listTravelExpenses = v.findViewById(R.id.listTravelExpenses);

        layerItineraryHasTransport = v.findViewById(R.id.layerItineraryHasTransport);
        listItineraryHasTransport = v.findViewById(R.id.listItineraryHasTransport);

        layerVehicle = v.findViewById(R.id.layerVehicle);
        listVehicle = v.findViewById(R.id.listVehicle);

        layerTour = v.findViewById(R.id.layerTour);
        listTour = v.findViewById(R.id.listTour);

        layerFuelSupply = v.findViewById(R.id.layerFuelSupply);
        listFuelSupply = v.findViewById(R.id.listFuelSupply);

        layerAchievement = v.findViewById(R.id.layerAchievement);
        listAchievement = v.findViewById(R.id.listAchievement);

        layerItinerary = v.findViewById(R.id.layerItinerary);
        listItinerary = v.findViewById(R.id.listItinerary);

        layerReservation = v.findViewById(R.id.layerReservation);
        listReservation = v.findViewById(R.id.listReservation);

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
            for (int x = 0; x < travels.size(); x++) {
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
                btnTransport.setEnabled(true);

                imTravelStatus.setOnClickListener(v -> {
                    LayoutInflater li = LayoutInflater.from(v.getContext());
                    View promptsView = li.inflate(R.layout.fragment_home_travel_status, null);

                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                    alertDialogBuilder.setView(promptsView);

                    final RadioGroup rgTravelStatus = promptsView.findViewById(R.id.rgTravelStatus);
                    final RecyclerView rvVehicles = promptsView.findViewById(R.id.rvVehicles);

                    Utils.addRadioButtonResources(R.array.travel_status_array, rgTravelStatus, requireContext());
                    rgTravelStatus.setOnCheckedChangeListener((group, checkedId) -> rbTravelStatus = checkedId);
                    rgTravelStatus.check(travel[0].getStatus()+1);

                    final int Show_Header_VehicleTravelStatus = 1  ;
                    TravelVehicleStatusListAdapter adapterTravelVehicleStatus = new TravelVehicleStatusListAdapter(Database.mVehicleHasTravelDao.fetchAllVehicleHasTravelByTravelForFuel(travel[0].getId() ), getContext(), "Home", Show_Header_VehicleTravelStatus);
                    if ( adapterTravelVehicleStatus.getItemCount() > Show_Header_VehicleTravelStatus){
                        rvVehicles.setAdapter(adapterTravelVehicleStatus);
                        rvVehicles.setLayoutManager(new LinearLayoutManager(getContext()));
                    }

                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton(R.string.OK, (dialog, id1) -> {
                                boolean isSave = true;
                                try {
                                    boolean temOdometerStart = true;
                                    boolean temOdometerFinal = true;
                                    boolean goRun = true;
                                    List<VehicleHasTravel> list = Database.mVehicleHasTravelDao.fetchAllVehicleHasTravelByTravelForFuel(travel[0].getId());
                                    for (int i = 0; i < list.size(); i++) {
                                        VehicleHasTravel vt1 = Database.mVehicleHasTravelDao.findVehicleHasTravelByID(list.get(i).getId());
                                        if (vt1.getStart_odometer() == 0) temOdometerStart = false;
                                        if (vt1.getFinal_odometer() == 0) temOdometerFinal = false;
                                    }
                                    if ((rbTravelStatus-1)==2 && !temOdometerStart){
                                        Toast.makeText(requireContext(), R.string.Home_Travel_Change_Status_Running, Toast.LENGTH_LONG).show();
                                        goRun = false;
                                    }
                                    if ((rbTravelStatus-1)==3 && !temOdometerFinal){
                                        Toast.makeText(requireContext(), R.string.Home_Travel_Change_Status_Executed, Toast.LENGTH_LONG).show();
                                        goRun = false;
                                    }
                                    if (goRun) {
                                        travel[0].setStatus(rbTravelStatus-1 );
                                        isSave = Database.mTravelDao.updateTravel(travel[0]);
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(requireContext(), R.string.Error_Changing_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                                }

                                if (!isSave) {
                                    Toast.makeText(requireContext(), R.string.Error_Saving_Data, Toast.LENGTH_LONG).show();
                                }
                            })
                            .setNegativeButton(R.string.Cancel, (dialog, id1) -> dialog.cancel());
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                });

                btnFood.setOnClickListener (v -> TravelItemExpenses(v,1));
                btnTolls.setOnClickListener (v -> TravelItemExpenses(v,2));
                btnTour.setOnClickListener (v -> TravelItemExpenses(v,3));
                btnExtra.setOnClickListener (v -> TravelItemExpenses(v,5));
                btnItinerary.setOnClickListener (v -> {
                    Intent intent = new Intent(v.getContext(), MaintenanceItineraryActivity.class);
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
                    List<VehicleHasTravel> vehicleHasTravel = Database.mVehicleHasTravelDao.fetchAllVehicleHasTravelByTravelForFuel(travel[0].getId());
                    if (vehicleHasTravel.size() > 0) {
                        if (vehicleHasTravel.size() == 1) {
                            if (vehicleHasTravel.get(0).getVehicle_id()!=null) {
                                intent.putExtra("vehicle_id", vehicleHasTravel.get(0).getVehicle_id());
                            }
                            if (vehicleHasTravel.get(0).getTransport_id()!=null) {
                                intent.putExtra("transport_id", vehicleHasTravel.get(0).getTransport_id());
                            }
                            startActivity(intent);
                        } else {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());
                            alertDialog.setIcon(R.drawable.ic_menu_vehicle);
                            alertDialog.setTitle(getString(R.string.choose)+" "+getString(R.string.of)+" "+getString(R.string.vehicle));

                            String[] listItems = new String[vehicleHasTravel.size()];
                            for (int i = 0; i<vehicleHasTravel.size(); i++){
                                if (vehicleHasTravel.get(i).getVehicle_id()==null){
                                    listItems[i] = Database.mTransportDao.fetchTransportById(vehicleHasTravel.get(i).getTransport_id()).getDescription();
                                } else {
                                    listItems[i] = Database.mVehicleDao.fetchVehicleById(vehicleHasTravel.get(i).getVehicle_id()).getName();
                                }
                            }

                            final int[] checkedItem = {-1};
                            alertDialog.setSingleChoiceItems(listItems, checkedItem[0], (dialog, which) -> {
                                if (vehicleHasTravel.get(which).getVehicle_id()!=null) {
                                    intent.putExtra("vehicle_id", vehicleHasTravel.get(which).getVehicle_id());
                                }
                                if (vehicleHasTravel.get(which).getTransport_id()!=null) {
                                    intent.putExtra("transport_id", vehicleHasTravel.get(which).getTransport_id());
                                }
                                dialog.dismiss();
                                startActivity(intent);
                            });

                            alertDialog.setNegativeButton("Cancel", (dialog, which) -> { });
                            AlertDialog customAlertDialog = alertDialog.create();
                            customAlertDialog.show();
                        }
                    } else {
                        startActivity(intent);
                    }
                });
                btnInsurance.setOnClickListener (v -> {
                    Intent intent = new Intent(v.getContext(), InsuranceActivity.class);
                    intent.putExtra("travel_id", travel[0].id);
                    startActivity(intent);
                });
                btnTransport.setOnClickListener (v -> {
                    // TODO - Criar tela mais pratica para vincular os Veiculos aos Itinerarios
                    Intent intent = new Intent(v.getContext(), ItineraryHasTransportActivity.class);
                    intent.putExtra("travel_id", travel[0].id);
                    startActivity(intent);
                });

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

                // Itinerary
                final int Show_Header_Itinerary = 1;
                final int Show_Footer_Itinerary = 1;
                TravelRouteFragment.HomeTravelItineraryListAdapter adapterItinerary = new TravelRouteFragment.HomeTravelItineraryListAdapter(Database.mItineraryDao.fetchAllItineraryByTravel(travel[0].getId() ), getContext(), Show_Header_Itinerary,Show_Footer_Itinerary, true, travel[0].getId());
                layerItinerary.setVisibility(View.VISIBLE);
                listItinerary.setAdapter(adapterItinerary);
                listItinerary.setLayoutManager(new LinearLayoutManager(getContext()));

                // Vehicles has Travel
                final int Show_Header_VehicleTravel = 1  ;
                TravelVehicleListAdapter adapterVehicleTravel = new TravelVehicleListAdapter(Database.mVehicleHasTravelDao.fetchAllVehicleHasTravelByTravel(travel[0].getId() ), getContext(),"TravelActivity", Show_Header_VehicleTravel,travel[0].id);
                if ( adapterVehicleTravel.getItemCount() > Show_Header_VehicleTravel){
                    layerVehicle.setVisibility(View.VISIBLE);
                    listVehicle.setAdapter(adapterVehicleTravel);
                    listVehicle.setLayoutManager(new LinearLayoutManager(getContext()));
                } else {
                    layerVehicle.setVisibility(View.GONE);
                }

                // Itinerary has Transport
                final int Show_Header_ItineraryHasTransport = 1  ;
                ItineraryHasTransportListAdapter adapterItineraryHasTransport = new ItineraryHasTransportListAdapter(Database.mItineraryHasTransportDao.fetchAllItineraryHasTransportByTravel(travel[0].getId() ), getContext(),"Home", Show_Header_ItineraryHasTransport, travel[0].id);
                if (adapterItineraryHasTransport.getItemCount() > Show_Header_ItineraryHasTransport) {
                    layerItineraryHasTransport.setVisibility(View.VISIBLE);
                    listItineraryHasTransport.setAdapter(adapterItineraryHasTransport);
                    listItineraryHasTransport.setLayoutManager(new LinearLayoutManager(getContext()));
                }else {
                    layerItineraryHasTransport.setVisibility(View.GONE);
                }

                // Tours
                final int Show_Header_Tour = 1  ;
                TourListAdapter adapterTourTravel = new TourListAdapter(Database.mTourDao.fetchAllTourByTravel(travel[0].getId() ), getContext(), Show_Header_Tour,travel[0].id);
                if ( adapterTourTravel.getItemCount() > Show_Header_Tour){
                    layerTour.setVisibility(View.VISIBLE);
                    listTour.setAdapter(adapterTourTravel);
                    listTour.setLayoutManager(new LinearLayoutManager(getContext()));
                } else {
                    layerTour.setVisibility(View.GONE);
                    btnTour.setOnClickListener (v -> {
                        Intent intent = new Intent(v.getContext(), TourActivity.class);
                        intent.putExtra("travel_id", travel[0].id);
                        startActivity(intent);
                    });
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
                final int Show_Header_AchievementTravel = 1 ;
                TravelAchievementListAdapter adapterAchievementTravel = new TravelAchievementListAdapter(Database.mAchievementDao.fetchAllAchievementByTravel(travel[0].getId()), getContext(),"Home", Show_Header_AchievementTravel, travel[0].getId());
                if ( adapterAchievementTravel.getItemCount() > Show_Header_AchievementTravel){
                    layerAchievement.setVisibility(View.VISIBLE);
                    listAchievement.setAdapter(adapterAchievementTravel);
                    listAchievement.setLayoutManager(new LinearLayoutManager(getContext()));
                } else {
                    layerAchievement.setVisibility(View.GONE);
                }

                // Reservation
                final int Show_Header_Reservation = 1;
                ReservationListAdapter adapterReservation = new ReservationListAdapter( Database.mReservationDao.fetchAllReservationByTravel(travel[0].getId()), getContext(), Show_Header_Reservation);
                if ( adapterReservation.getItemCount() > Show_Header_Reservation){
                    layerReservation.setVisibility(View.VISIBLE);
                    listReservation.setAdapter(adapterReservation);
                    listReservation.setLayoutManager(new LinearLayoutManager(getContext()));
                } else {
                    layerReservation.setVisibility(View.GONE);
                }

                // Insurance - layerInsurance
                final int Show_Header_Insurance = 0;
                HomeInsuranceListAdapter adapterInsurance = new HomeInsuranceListAdapter(Database.mInsuranceDao.findReminderInsurance("T", travel[0].getId() ), getContext(), Show_Header_Insurance);
                if ( adapterInsurance.getItemCount() > Show_Header_Insurance){
                    layerInsurance.setVisibility(View.VISIBLE);
                    listInsuranceExpiration.setAdapter(adapterInsurance);
                    listInsuranceExpiration.setLayoutManager(new LinearLayoutManager(getContext()));
                } else {
                    layerInsurance.setVisibility(View.GONE);
                }

                adapterTravelExpense.notifyDataSetChanged();
                adapterItinerary.notifyDataSetChanged();
                adapterVehicleTravel.notifyDataSetChanged();
                adapterItineraryHasTransport.notifyDataSetChanged();
                adapterTourTravel.notifyDataSetChanged();
                adapterFuelSupplyTravel.notifyDataSetChanged();
                adapterAchievementTravel.notifyDataSetChanged();
                adapterReservation.notifyDataSetChanged();
                adapterInsurance.notifyDataSetChanged();
            }

            public void TravelItemExpenses(View v, int expense_type ) {

                // TODO - show items registered in other tables to choose from and link to the executed expense, changing or marking the status of the expenses incurred
                // TODO - show the markers for each type to choose from in the expense record
                LayoutInflater li = LayoutInflater.from(v.getContext());
                View promptsView = li.inflate(R.layout.dialog_travel_item_expenses, null);

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                alertDialogBuilder.setView(promptsView);

                final HomeTravelItemExpensesListAdapter[] adapterTravelItemExpenses = new HomeTravelItemExpensesListAdapter[1];
                final TextView txtExpenseType = promptsView.findViewById(R.id.txtExpenseType);
                final RecyclerView rvTravelExpenseExpected = promptsView.findViewById(R.id.rvTravelExpenseExpected);
                final RecyclerView rvTravelExpenseItem = promptsView.findViewById(R.id.rvTravelExpenseItem);

                txtExpenseType.setText(getResources().getStringArray(R.array.expenses_type_array)[expense_type]);

                TravelExpensesListAdapter adapterTravelExpenses = new TravelExpensesListAdapter(travel[0].getId(), Database.mTravelExpensesDao.fetchAllTravelExpensesByTravelType(travel[0].getId(), expense_type), requireContext(), 1, 1, true);
                rvTravelExpenseExpected.setAdapter(adapterTravelExpenses);
                rvTravelExpenseExpected.setLayoutManager(new LinearLayoutManager(requireContext()));

                adapterTravelItemExpenses[0] = new HomeTravelItemExpensesListAdapter(Database.mTravelItemExpensesDao.fetchTravelItemExpensesByExpenseType( travel[0].getId(), expense_type), requireContext(),1,1, travel[0].getId(), expense_type);
                rvTravelExpenseItem.setAdapter(adapterTravelItemExpenses[0]);
                rvTravelExpenseItem.setLayoutManager(new LinearLayoutManager(requireContext()));

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton(R.string.OK, (dialog, id) -> { });
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