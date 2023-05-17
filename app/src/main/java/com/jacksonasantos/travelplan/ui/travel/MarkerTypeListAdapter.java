package com.jacksonasantos.travelplan.ui.travel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Marker;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.List;

public class MarkerTypeListAdapter extends RecyclerView.Adapter<MarkerTypeListAdapter.MyViewHolder> {

    public final List<Integer> mMarkerType;
    final Context context;
    private ItemClickListener mItemClickListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final LinearLayout ll_marker_type;
        public final ImageView img_marker_type;
        public final TextView tv_marker_type;

        public MyViewHolder(View v) {
            super(v);
            ll_marker_type = v.findViewById(R.id.ll_type);
            img_marker_type = v.findViewById(R.id.img_type);
            img_marker_type.setOnClickListener(this);
            tv_marker_type = v.findViewById(R.id.tv_type);
        }
        void bindItem(int pos) {
            if(Utils.selected_position==pos){
                ll_marker_type.setBackgroundColor(Color.LTGRAY);
            } else {
                ll_marker_type.setBackgroundColor(Color.WHITE);
            }
        }
        @Override
        public void onClick(View view) { }
    }

       public MarkerTypeListAdapter(List<Integer> markerType, Context context) {
        this.mMarkerType = markerType;
        this.context = context;
    }

    @NonNull
    @Override
    public MarkerTypeListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_type, parent, false);

        return new MyViewHolder(v);
    }

    public void addItemClickListener(ItemClickListener listener) {
        mItemClickListener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.bindItem(position);
        holder.tv_marker_type.setText(context.getResources().getStringArray(R.array.marker_type_array)[mMarkerType.get(position)]);
        holder.img_marker_type.setImageResource(Marker.getMarker_typeImage(mMarkerType.get(position)));
        holder.img_marker_type.setOnClickListener(view -> {
            if (mItemClickListener != null) {
                if (Utils.selected_position == position) {
                    Utils.selected_position = RecyclerView.NO_POSITION;
                }
                mItemClickListener.onItemClick(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMarkerType.size();
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }
}