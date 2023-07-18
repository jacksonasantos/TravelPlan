package com.jacksonasantos.travelplan.ui.travel;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.FuelSupply;
import com.jacksonasantos.travelplan.dao.Insurance;
import com.jacksonasantos.travelplan.dao.Itinerary;
import com.jacksonasantos.travelplan.dao.ItineraryHasTransport;
import com.jacksonasantos.travelplan.dao.Marker;
import com.jacksonasantos.travelplan.dao.Reservation;
import com.jacksonasantos.travelplan.dao.Tour;
import com.jacksonasantos.travelplan.dao.Transport;
import com.jacksonasantos.travelplan.dao.Travel;
import com.jacksonasantos.travelplan.dao.TravelExpenses;
import com.jacksonasantos.travelplan.dao.VehicleHasTravel;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.List;
import java.util.Objects;

public class TravelListAdapter extends RecyclerView.Adapter<TravelListAdapter.MyViewHolder> {

    private final List<Travel> mTravel;
    final Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final LinearLayout llTravel;
        private final ImageView imStatus;
        private final TextView txtDescription;
        private final TextView txtDeparture;
        private final ImageButton btnItinerary;
        private final ImageButton btnDelete;

        public MyViewHolder(View v) {
            super(v);
            llTravel = v.findViewById(R.id.llTravel);
            imStatus = v.findViewById(R.id.imStatus);
            txtDescription = v.findViewById(R.id.txtDescription);
            txtDeparture = v.findViewById(R.id.txtDeparture);
            btnItinerary = v.findViewById(R.id.btnItinerary);
            btnDelete = v.findViewById(R.id.btnDelete);
            btnItinerary.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) { }
    }

    public TravelListAdapter(List<Travel> travel, Context context) {
        this.mTravel = travel;
        this.context = context;
        Database mdb = new Database(context);
        mdb.open();
    }

    @NonNull
    @Override
    public TravelListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View travelView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_travel, parent, false);

        return new MyViewHolder(travelView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Travel travel = mTravel.get(position);
        holder.imStatus.setImageResource(R.drawable.ic_ball );
        holder.imStatus.setColorFilter(travel.getColorStatus(), PorterDuff.Mode.MULTIPLY);
        holder.txtDescription.setText(travel.getDescription());
        holder.txtDeparture.setText(Utils.dateToString(travel.getDeparture_date()));

        // btnItinerary
        holder.btnItinerary.setOnClickListener (v -> {
            Intent intent = new Intent (v.getContext(), MaintenanceItineraryActivity.class);
            intent.putExtra("travel_id", travel.getId());
            context.startActivity(intent);
        });

        // btnEdit
        holder.llTravel.setOnClickListener (v -> {
            Intent intent = new Intent (v.getContext(), TravelActivity.class);
            intent.putExtra("travel_id", travel.getId());
            context.startActivity(intent);
            mTravel.set(position,travel);
            notifyItemChanged(position);
        });

        // btnDelete
        holder.btnDelete.setOnClickListener (v -> new AlertDialog.Builder(v.getContext())
                .setTitle(R.string.Travel_Deleting)
                .setMessage(R.string.Msg_Confirm)
                .setPositiveButton(R.string.Yes, (dialogInterface, i) -> {
                    String tag = null;
                    try {
                        tag = "Insurance";
                        List<Insurance> insurances = Database.mInsuranceDao.fetchAllInsurance();
                        for (int x = 0; x < insurances.size(); x++) {
                            Insurance i1 = insurances.get(x);
                            if (Objects.equals(i1.getTravel_id(), travel.getId())) {
                               i1.setTravel_id(null);
                               Database.mInsuranceDao.updateInsurance(i1);
                               Log.i(tag,"Update "+i1.getId());
                            }
                        }
                        tag = "FuelSupply";
                        List<FuelSupply> fuelSupplies = Database.mFuelSupplyDao.fetchAllFuelSupplyHasTravelByTravel(travel.getId());
                        for (int x = 0; x < fuelSupplies.size(); x++) {
                            FuelSupply f = fuelSupplies.get(x);
                            f.setAssociated_travel_id(null);
                            Database.mFuelSupplyDao.updateFuelSupply(f);
                            Log.i(tag,"Update "+f.getId());
                        }
                        tag = "Reservation";
                        List<Reservation> reservations = Database.mReservationDao.fetchAllReservationByTravel(travel.getId());
                        for (int x = 0; x < reservations.size(); x++) {
                            Database.mReservationDao.deleteReservation(reservations.get(x).getId());
                            Log.i(tag,"Delete "+reservations.get(x).getId());
                        }
                        tag = "TravelExpenses";
                        List<TravelExpenses> travelExpenses = Database.mTravelExpensesDao.fetchAllTravelExpensesByTravel(travel.getId());
                        for (int x = 0; x < travelExpenses.size(); x++) {
                            Database.mTravelExpensesDao.deleteTravelExpenses(travelExpenses.get(x).getId());
                            Log.i(tag,"Delete "+travelExpenses.get(x).getId());
                        }
                        tag = "Marker";
                        List<Marker> markers = Database.mMarkerDao.fetchMarkerByTravelId(travel.getId());
                        for (int x = 0; x < markers.size(); x++) {
                            Database.mMarkerDao.deleteMarker(markers.get(x).getId());
                            Log.i(tag,"Delete "+markers.get(x).getId());
                        }
                        tag = "Tour";
                        List<Tour> tours = Database.mTourDao.fetchAllTourByTravel(travel.getId());
                        for (int x = 0; x < tours.size(); x++) {
                            Database.mTourDao.deleteTour(tours.get(x).getId());
                            Log.i(tag,"Delete "+tours.get(x).getId());
                        }
                        tag = "Itinerary";
                        List<Itinerary> itineraries = Database.mItineraryDao.fetchAllItineraryByTravel(travel.getId());
                        for (int x = 0; x < itineraries.size(); x++) {
                            Database.mItineraryDao.deleteItinerary(itineraries.get(x).getId());
                            Log.i(tag,"Delete "+itineraries.get(x).getId());
                        }
                        tag = "Transport";
                        List<Transport> transports = Database.mTransportDao.fetchAllTransportTravel(travel.getId());
                        for (int x = 0; x < transports.size(); x++) {
                            Database.mTransportDao.deleteTransport(transports.get(x).getId());
                            Log.i(tag,"Delete "+transports.get(x).getId());
                        }
                        tag = "VehicleHasTravel";
                        List<VehicleHasTravel> vt = Database.mVehicleHasTravelDao.fetchAllVehicleHasTravelByTravel(travel.getId());
                        for (int x = 0; x < vt.size(); x++) {
                            Database.mVehicleHasTravelDao.deleteVehicleHasTravel(vt.get(x).getId());
                            Log.i(tag,"Delete "+vt.get(x).getId());
                        }
                        tag = "ItineraryHasTransport";
                        List<ItineraryHasTransport> itineraryHasTransports = Database.mItineraryHasTransportDao.fetchAllItineraryHasTransportByTravel(travel.getId());
                        for (int x = 0; x < itineraryHasTransports.size(); x++) {
                            Database.mItineraryHasTransportDao.deleteItineraryHasTransport(itineraryHasTransports.get(x).getId());
                            Log.i(tag,"Delete "+itineraryHasTransports.get(x).getId());
                        }
                        tag = "Travel";
                        Database.mTravelDao.deleteTravel(travel.getId());
                        Log.i(tag,"Delete "+travel.getId());
                        mTravel.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mTravel.size());
                    } catch (Exception e) {
                        Toast.makeText(context, context.getString(R.string.Error_Deleting_Data) + " " + tag + "\n" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton(R.string.No, null)
                .show());
    }

    @Override
    public int getItemCount() { return mTravel.size(); }
}