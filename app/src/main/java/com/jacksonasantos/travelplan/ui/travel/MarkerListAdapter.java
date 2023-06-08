package com.jacksonasantos.travelplan.ui.travel;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Itinerary;
import com.jacksonasantos.travelplan.dao.Marker;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.List;
import java.util.Objects;

public class MarkerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;

    private final List<Marker> mMarker;
    final Context context;
    final Integer mTravel_id, mItinerary_id;
    final int show_header;
    final int show_footer;
    final boolean isMap;

    public MarkerListAdapter(List<Marker> marker, Context context, int show_header, int show_footer, boolean isMap, Integer travel_id, Integer itinerary_id) {
        this.mMarker = marker;
        this.context = context;
        this.mTravel_id = travel_id;
        this.mItinerary_id = itinerary_id;
        this.show_header = show_header >= 1 ? 1 : 0;
        this.show_footer = show_footer >= 1 ? 1 : 0;
        this.isMap = isMap;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View markerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item_marker, parent, false);
        if (viewType == TYPE_HEADER) {
            return new HeaderViewHolder(markerView);
        } else if (viewType == TYPE_FOOTER) {
            return new FooterViewHolder(markerView);
        } else return new ItemViewHolder(markerView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.llMarker.setBackgroundColor(Utils.getColorWithAlpha(R.color.colorItemList,0.1f));
            headerViewHolder.txtDescription.setText(R.string.marker_description);
            if (isMap) {
                headerViewHolder.txtCityState.setVisibility(View.INVISIBLE);
            } else {
                headerViewHolder.txtCityState.setText(R.string.marker_city);
            }
            headerViewHolder.btnAdd.setImageResource(R.drawable.ic_button_add);
            headerViewHolder.btnAdd.setOnClickListener(v -> {
                Intent intent = new Intent (v.getContext(), MarkerActivity.class);
                intent.putExtra("travel_id", mTravel_id);
                intent.putExtra("itinerary_id", mItinerary_id);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                notifyItemChanged(position);
            });
        } else if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            Marker marker = mMarker.get(position - show_header);

            if (isMap) {
                if (marker.getItinerary_id() % 2 == 0) {
                    itemViewHolder.llMarker.setBackgroundColor(Utils.getColorWithAlpha(Color.LTGRAY, (marker.getSequence() % 2 == 0 ? 0.7f : 0.5f)));
                } else {
                    itemViewHolder.llMarker.setBackgroundColor(Utils.getColorWithAlpha(Color.WHITE, (marker.getSequence() % 2 == 0 ? 0.7f : 0.5f)));
                }
                itemViewHolder.txtCityState.setVisibility(View.GONE);
            } else {
                itemViewHolder.txtCityState.setText(String.format("%s/%s", marker.getCity(), marker.getState()));
            }
            Itinerary itinerary = Database.mItineraryDao.fetchItineraryById(marker.getItinerary_id());
            String txt = " " + itinerary.getSequence() +
                         "." +
                         marker.getSequence() +
                         " " +
                         context.getResources().getStringArray(R.array.marker_type_array)[marker.getMarker_type()];
            if (!Objects.equals(marker.getDescription(), "") && marker.getDescription()!=null) { txt = txt + "-" + marker.getDescription(); }
            itemViewHolder.txtDescription.setText(txt);

            itemViewHolder.llMarker.setOnClickListener( v-> {
                Intent intent = new Intent (v.getContext(), MarkerActivity.class);
                intent.putExtra("marker_id", mMarker.get(position-show_header).getId());
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            });

            itemViewHolder.btnDelete.setOnClickListener(v -> new AlertDialog.Builder(v.getContext())
                    .setTitle(R.string.Marker_Deleting)
                    .setMessage(R.string.Msg_Confirm)
                    .setPositiveButton(R.string.Yes, (dialogInterface, i) -> {
                        try {
                            MarkerActivity.removeMarker(context, marker.getTravel_id(), new LatLng(Double.parseDouble(marker.getLatitude()), Double.parseDouble(marker.getLongitude())));
                            mMarker.remove(position-show_header);
                            notifyItemRemoved(position-show_header);
                            notifyItemRangeChanged(position-show_header, mMarker.size());
                        } catch (Exception e) {
                            Toast.makeText(context, context.getString(R.string.Error_Deleting_Data) + "\n" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    }).setNegativeButton(R.string.No, null)
                    .show());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && show_header == 1) return TYPE_HEADER;
        if (position == mMarker.size() + show_header && show_footer == 1) return TYPE_FOOTER;
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mMarker.size() + show_header + show_footer;
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout llMarker;
        public final TextView txtDescription;
        public final TextView txtCityState;
        public final ImageButton btnAdd;

        public HeaderViewHolder(View v) {
            super(v);
            llMarker = v.findViewById(R.id.llMarker);
            txtDescription = v.findViewById(R.id.txtDescription);
            txtCityState = v.findViewById(R.id.txtCityState);
            btnAdd = v.findViewById(R.id.btnDelete);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout llMarker;
        public final TextView txtDescription;
        public final TextView txtCityState;
        public final ImageButton btnDelete;

        public ItemViewHolder(View v) {
            super(v);
            llMarker = v.findViewById(R.id.llMarker);
            txtDescription = v.findViewById(R.id.txtDescription);
            txtCityState = v.findViewById(R.id.txtCityState);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout llMarker;
        public final TextView txtDescription;
        public final TextView txtCityState;

        public FooterViewHolder(View v) {
            super(v);
            llMarker = v.findViewById(R.id.llMarker);
            txtDescription = v.findViewById(R.id.txtDescription);
            txtCityState = v.findViewById(R.id.txtCityState);
        }
    }
}