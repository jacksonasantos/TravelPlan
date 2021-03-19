package com.jacksonasantos.travelplan.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Itinerary;

import java.util.List;

public class HomeTravelItineraryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public final List<Itinerary> mItinerary;
    Context context;
    int show_header;

    public HomeTravelItineraryListAdapter(List<Itinerary> itinerary, Context context, int show_header) {
        this.mItinerary = itinerary;
        this.context = context;
        this.show_header = show_header; // 0 - NO SHOW HEADER | 1 - SHOW HEADER
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itineraryView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_home_travel_item_itinerary, parent, false);

        if (viewType == TYPE_ITEM) {
            return new ItemViewHolder(itineraryView);
        } else if (viewType == TYPE_HEADER) {
            return new HeaderViewHolder(itineraryView);
        }
        else return null;
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

            headerViewHolder.llItineraryItem.setBackgroundColor(Color.LTGRAY);
            headerViewHolder.txtSequence.setText("");
            headerViewHolder.txtSource.setText(R.string.Itinerary_Source);
            headerViewHolder.txtTarget.setText(R.string.Itinerary_Target);
            headerViewHolder.txtDaily.setText(R.string.Itinerary_Daily);
            headerViewHolder.txtDistance.setText(R.string.Itinerary_Distance);
            headerViewHolder.txtTime.setText(R.string.Itinerary_Time);
        } else if (holder instanceof ItemViewHolder) {

            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            final Itinerary itinerary = mItinerary.get(position-show_header);

            itemViewHolder.txtSequence.setText(Integer.toString(itinerary.getSequence()));
            itemViewHolder.txtSource.setText(itinerary.getOrig_location());
            itemViewHolder.txtTarget.setText(itinerary.getDest_location());
            itemViewHolder.txtDaily.setText(Integer.toString(itinerary.getDaily()));
            itemViewHolder.txtDistance.setText(Integer.toString(itinerary.getDistance()));
            itemViewHolder.txtTime.setText(itinerary.getTime());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && show_header == 1) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mItinerary.size()+show_header;
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout llItineraryItem;
        private final TextView txtSequence;
        private final TextView txtSource;
        private final TextView txtTarget;
        private final TextView txtDaily;
        private final TextView txtDistance;
        private final TextView txtTime;

        public HeaderViewHolder(View v) {
            super(v);
            llItineraryItem = v.findViewById(R.id.llItineraryItem);
            txtSequence = v.findViewById(R.id.txtSequence);
            txtSource = v.findViewById(R.id.txtSource);
            txtTarget = v.findViewById(R.id.txtTarget);
            txtDaily = v.findViewById(R.id.txtDaily);
            txtDistance = v.findViewById(R.id.txtDistance);
            txtTime = v.findViewById(R.id.txtTime);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout llItineraryItem;
        private final TextView txtSequence;
        private final TextView txtSource;
        private final TextView txtTarget;
        private final TextView txtDaily;
        private final TextView txtDistance;
        private final TextView txtTime;

        public ItemViewHolder(View v) {
            super(v);
            llItineraryItem = v.findViewById(R.id.llItineraryItem);
            txtSequence = v.findViewById(R.id.txtSequence);
            txtSource = v.findViewById(R.id.txtSource);
            txtTarget = v.findViewById(R.id.txtTarget);
            txtDaily = v.findViewById(R.id.txtDaily);
            txtDistance = v.findViewById(R.id.txtDistance);
            txtTime = v.findViewById(R.id.txtTime);
        }
    }
}
