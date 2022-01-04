package com.jacksonasantos.travelplan.ui.travel;

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
import com.jacksonasantos.travelplan.dao.Marker;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.List;

public class MarkerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;

    private final List<Marker> mMarker;
    final Context context;
    final int show_header;
    final int show_footer;

    public MarkerListAdapter(List<Marker> marker, Context context, int show_header, int show_footer) {
        this.mMarker = marker;
        this.context = context;
        this.show_header = show_header >= 1 ? 1 : 0;
        this.show_footer = show_footer >= 1 ? 1 : 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View markerView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_marker, parent, false);
        if (viewType == TYPE_HEADER) {
            return new HeaderViewHolder(markerView);
        } else if (viewType == TYPE_FOOTER) {
            return new FooterViewHolder(markerView);
        } else return new ItemViewHolder(markerView);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.llMarker.setBackgroundColor(Color.LTGRAY);
            headerViewHolder.txtDescription.setText(R.string.marker_description);
        } else if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            Marker marker = mMarker.get(position - show_header);

            if (marker.getItinerary_id()%2 == 0) {
                itemViewHolder.llMarker.setBackgroundColor(Utils.getColorWithAlpha(Color.LTGRAY,0.7f));
            } else {
                itemViewHolder.llMarker.setBackgroundColor(Utils.getColorWithAlpha(Color.WHITE,0.7f));
            }
            String txt = " " + marker.getItinerary_id() +
                         "." +
                         marker.getSequence() +
                         " " +
                         context.getResources().getStringArray(R.array.marker_type_array)[marker.getMarker_type()];
                         if (!marker.getDescription().equals("")) { txt = txt + "-" + marker.getDescription(); }
            itemViewHolder.txtDescription.setText(txt);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && show_header == 1) {
            return TYPE_HEADER;
        }
        if (position == mMarker.size() + show_header && show_footer == 1) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mMarker.size() + show_header + show_footer;
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout llMarker;
        public final TextView txtDescription;

        public HeaderViewHolder(View v) {
            super(v);
            llMarker = v.findViewById(R.id.llMarker);
            txtDescription = v.findViewById(R.id.txtDescription);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout llMarker;
        public final TextView txtDescription;

        public ItemViewHolder(View v) {
            super(v);
            llMarker = v.findViewById(R.id.llMarker);
            txtDescription = v.findViewById(R.id.txtDescription);
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout llMarker;
        public final TextView txtDescription;

        public FooterViewHolder(View v) {
            super(v);
            llMarker = v.findViewById(R.id.llMarker);
            txtDescription = v.findViewById(R.id.txtDescription);
        }
    }
}