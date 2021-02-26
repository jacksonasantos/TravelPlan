package com.jacksonasantos.travelplan.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Itinerary;

import java.util.List;

public class HomeTravelItineraryListAdapter extends RecyclerView.Adapter<HomeTravelItineraryListAdapter.MyViewHolder> {

    public final List<Itinerary> mItinerary;
    Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView txtSequence;
        private final TextView txtSource;
        private final TextView txtTarget;
        private final TextView txtDaily;
        private final TextView txtDistance;
        private final TextView txtTime;

        public MyViewHolder(View v) {
            super(v);
            txtSequence = v.findViewById(R.id.txtSequence);
            txtSource = v.findViewById(R.id.txtSource);
            txtTarget = v.findViewById(R.id.txtTarget);
            txtDaily = v.findViewById(R.id.txtDaily);
            txtDistance = v.findViewById(R.id.txtDistance);
            txtTime = v.findViewById(R.id.txtTime);
        }

        @Override
        public void onClick(View view) {
        }
    }

    public HomeTravelItineraryListAdapter(List<Itinerary> itinerary, Context context) {
        this.mItinerary = itinerary;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeTravelItineraryListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itineraryView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_home_travel_item_itinerary, parent, false);

        return new MyViewHolder(itineraryView);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Itinerary itinerary = mItinerary.get(position);

        holder.txtSequence.setText(Integer.toString(itinerary.getSequence()));
        holder.txtSource.setText(itinerary.getOrig_location());
        holder.txtTarget.setText(itinerary.getDest_location());
        holder.txtDaily.setText(Integer.toString(itinerary.getDaily()));
        holder.txtDistance.setText(Integer.toString(itinerary.getDistance()));
        holder.txtTime.setText(itinerary.getTime());
    }

    @Override
    public int getItemCount() {
        return mItinerary.size();
    }
}