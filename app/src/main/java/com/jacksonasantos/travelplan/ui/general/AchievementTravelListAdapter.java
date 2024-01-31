package com.jacksonasantos.travelplan.ui.general;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Itinerary;
import com.jacksonasantos.travelplan.dao.Marker;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.List;

public class AchievementTravelListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    final List<Marker> mMarkers;
    final int show_header;

    public AchievementTravelListAdapter(List<Marker> markers, Context context, int show_header) {
        this.mMarkers = markers;
        this.show_header = show_header;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View markerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item_achievement_travel, parent, false);

        if (viewType == TYPE_HEADER) {
            return new HeaderViewHolder(markerView);
        } else return new ItemViewHolder(markerView);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (holder instanceof HeaderViewHolder){
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

            headerViewHolder.llAchievementTravelItem.setBackgroundColor(Utils.getColorWithAlpha(R.color.colorItemList,0.1f));
            headerViewHolder.txtTravel.setText(R.string.travel);
            headerViewHolder.txtMarker.setText(R.string.marker);
        }
        else if (holder instanceof ItemViewHolder) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            Marker marker = mMarkers.get(position-show_header);
            Itinerary itinerary = Database.mItineraryDao.fetchItineraryById(marker.getItinerary_id());
            itemViewHolder.txtTravel.setText(Database.mTravelDao.fetchTravelById(marker.getTravel_id()).toString());
            itemViewHolder.txtMarker.setText(itinerary.toString());
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
        return mMarkers.size()+show_header;
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout llAchievementTravelItem;
        public final TextView txtTravel;
        public final TextView txtMarker;
        public HeaderViewHolder(View v) {
            super(v);
            llAchievementTravelItem = v.findViewById(R.id.llAchievementTravelItem);
            txtTravel = v.findViewById(R.id.txtTravel);
            txtMarker = v.findViewById(R.id.txtMarker);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout llAchievementTravelItem;
        public final TextView txtTravel;
        public final TextView txtMarker;

        public ItemViewHolder(View v) {
            super(v);
            llAchievementTravelItem = v.findViewById(R.id.llAchievementTravelItem);
            txtTravel = v.findViewById(R.id.txtTravel);
            txtMarker = v.findViewById(R.id.txtMarker);
        }
    }
}