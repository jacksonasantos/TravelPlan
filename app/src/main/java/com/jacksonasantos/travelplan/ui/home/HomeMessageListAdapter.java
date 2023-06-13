package com.jacksonasantos.travelplan.ui.home;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.general.Database;

import java.util.List;

public class HomeMessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<String> mMessage;

    public HomeMessageListAdapter(List<String> message, Context context) {
        this.mMessage = message;
        Database mdb = new Database(context);
        mdb.open();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View messageView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home_item_message, parent, false);
        return new ItemViewHolder(messageView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.llMessageItem.setBackgroundColor(Color.WHITE);
        itemViewHolder.txtMessage.setText(mMessage.get(position));
    }

    @Override
    public int getItemCount() {
        return mMessage.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout llMessageItem;
        private final TextView txtMessage;

        public ItemViewHolder(View v) {
            super(v);
            llMessageItem = v.findViewById(R.id.llMessageItem);
            txtMessage = v.findViewById(R.id.txtMessage);
        }
    }
}
