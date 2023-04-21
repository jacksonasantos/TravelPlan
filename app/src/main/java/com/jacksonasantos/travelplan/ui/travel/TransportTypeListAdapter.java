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
import com.jacksonasantos.travelplan.dao.Transport;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.List;

public class TransportTypeListAdapter extends RecyclerView.Adapter<TransportTypeListAdapter.MyViewHolder> {

    public final List<Integer> mTransportType;
    final Context context;
    private ItemClickListener mItemClickListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final LinearLayout ll_transport_type;
        public final ImageView img_transport_type;
        public final TextView tv_transport_type;

        public MyViewHolder(View v) {
            super(v);
            ll_transport_type = v.findViewById(R.id.ll_transport_type);
            img_transport_type = v.findViewById(R.id.img_transport_type);
            img_transport_type.setOnClickListener(this);
            tv_transport_type = v.findViewById(R.id.tv_transport_type);
        }
        void bindItem(int pos) {
            if(Utils.selected_position==pos){
                ll_transport_type.setBackgroundColor(Color.LTGRAY);
            } else {
                ll_transport_type.setBackgroundColor(Color.WHITE);
            }
        }
        @Override
        public void onClick(View view) {
        }
    }

    public TransportTypeListAdapter(List<Integer> transportType, Context context) {
        this.mTransportType = transportType;
        this.context = context;

        Database mdb = new Database(context);
        mdb.open();
    }

    @NonNull
    @Override
    public TransportTypeListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_transport_type, parent, false);

        return new MyViewHolder(v);
    }

    public void addItemClickListener(ItemClickListener listener) {
        mItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.bindItem(position);
        holder.tv_transport_type.setText(context.getResources().getStringArray(R.array.transport_type_array)[mTransportType.get(position)]);
        holder.img_transport_type.setImageResource(Transport.getTransportTypeImage(mTransportType.get(position)));
        holder.img_transport_type.setOnClickListener(view -> {
            if (Utils.selected_position == position) {
                Utils.selected_position = RecyclerView.NO_POSITION;
            }
            mItemClickListener.onItemClick(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return mTransportType.size();
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }
}