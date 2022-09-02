package com.jacksonasantos.travelplan.ui.vehicle;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.NextMaintenanceItem;
import com.jacksonasantos.travelplan.dao.general.Database;

import java.util.List;

public class ServiceTypeListAdapter extends RecyclerView.Adapter<ServiceTypeListAdapter.MyViewHolder> {

    public final List<Integer> mServiceType;
    final Context context;
    private ItemClickListener mItemClickListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final LinearLayout ll_service_type;
        public final ImageView img_service_type;

        public MyViewHolder(View v) {
            super(v);
            ll_service_type = v.findViewById(R.id.ll_service_type);
            img_service_type = v.findViewById(R.id.img_service_type);
            img_service_type.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) { }
    }

       public ServiceTypeListAdapter(List<Integer> serviceType, Context context) {
        this.mServiceType = serviceType;
        this.context = context;

        Database mdb = new Database(context);
        mdb.open();
    }

    @NonNull
    @Override
    public ServiceTypeListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_service_type, parent, false);

        return new MyViewHolder(v);
    }

    public void addItemClickListener(ItemClickListener listener) {
        mItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.img_service_type.setImageResource(NextMaintenanceItem.getServiceTypeImage(mServiceType.get(position)));
        holder.img_service_type.setOnClickListener(view -> {
            if (mItemClickListener != null) {
                int color = Color.TRANSPARENT;
                Drawable background = holder.ll_service_type.getBackground();
                if (background instanceof ColorDrawable)
                    color = ((ColorDrawable) background).getColor();
                if (color==Color.LTGRAY){
                    holder.ll_service_type.setBackgroundColor(Color.WHITE);
                } else {
                    holder.ll_service_type.setBackgroundColor(Color.LTGRAY);
                }
                mItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mServiceType.size();
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }
}