package com.jacksonasantos.travelplan.ui.travel;

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
import com.jacksonasantos.travelplan.dao.Tour;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.List;

public class TourTypeListAdapter extends RecyclerView.Adapter<TourTypeListAdapter.MyViewHolder> {

    public final List<Integer> mTourType;
    final Context context;
    private ItemClickListener mItemClickListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final LinearLayout ll_tour_type;
        public final ImageView img_tour_type;
        public final TextView tv_tour_type;

        public MyViewHolder(View v) {
            super(v);
            ll_tour_type = v.findViewById(R.id.ll_tour_type);
            img_tour_type = v.findViewById(R.id.img_tour_type);
            img_tour_type.setOnClickListener(this);
            tv_tour_type = v.findViewById(R.id.tv_tour_type);
        }
        void bindItem(int pos) {
            if(Utils.selected_position==pos){
                ll_tour_type.setBackgroundColor(Color.LTGRAY);
            } else {
                ll_tour_type.setBackgroundColor(Color.WHITE);
            }
        }
        @Override
        public void onClick(View view) { }
    }

       public TourTypeListAdapter(List<Integer> tourType, Context context) {
        this.mTourType = tourType;
        this.context = context;

        Database mdb = new Database(context);
        mdb.open();
    }

    @NonNull
    @Override
    public TourTypeListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_tour_type, parent, false);

        return new MyViewHolder(v);
    }

    public void addItemClickListener(ItemClickListener listener) {
        mItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.bindItem(position);
        holder.tv_tour_type.setText(context.getResources().getStringArray(R.array.tour_type_array)[mTourType.get(position)]);
        holder.img_tour_type.setImageResource(Tour.getTourTypeImage(mTourType.get(position)));
        holder.img_tour_type.setOnClickListener(view -> {
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
        return mTourType.size();
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }
}