package com.jacksonasantos.travelplan.ui.travel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Travel;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.List;

public class TravelListAdapter extends RecyclerView.Adapter<TravelListAdapter.MyViewHolder> {

    private final List<Travel> mTravel;
    final Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView imStatus;
        private final TextView txtDescription;
        private final TextView txtDeparture;
        private final ImageButton btnItinerary;
        private final ImageButton btnEdit;
        private final ImageButton btnDelete;

        public MyViewHolder(View v) {
            super(v);
            imStatus = v.findViewById(R.id.imStatus);
            txtDescription = v.findViewById(R.id.txtDescription);
            txtDeparture = v.findViewById(R.id.txtDeparture);
            btnItinerary = v.findViewById(R.id.btnItinerary);
            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
            btnItinerary.setOnClickListener(this);
            btnEdit.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }
    }

    public TravelListAdapter(List<Travel> travel, Context context) {
        this.mTravel = travel;
        this.context = context;

        Database mdb = new Database(context);
        mdb.open();
    }

    @NonNull
    @Override
    public TravelListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View travelView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_travel, parent, false);

        return new MyViewHolder(travelView);
    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Travel travel = mTravel.get(position);
        holder.imStatus.setImageResource(R.drawable.ic_ball );
        holder.imStatus.setColorFilter(travel.getColorStatus(), PorterDuff.Mode.MULTIPLY);
        holder.txtDescription.setText(travel.getDescription());
        holder.txtDeparture.setText(Utils.dateToString(travel.getDeparture_date()));

        // btnItinerary
        holder.btnItinerary.setOnClickListener (v -> {
            Intent intent = new Intent (v.getContext(), ItineraryActivity.class);
            intent.putExtra("travel_id", travel.getId());
            context.startActivity(intent);
            notifyDataSetChanged();
        });

        // btnEdit
        holder.btnEdit.setOnClickListener (v -> {
            Intent intent = new Intent (v.getContext(), TravelActivity.class);
            intent.putExtra("travel_id", travel.getId());
            context.startActivity(intent);
            notifyDataSetChanged();
        });

        // btnDelete
        holder.btnDelete.setOnClickListener (v -> new AlertDialog.Builder(v.getContext())
                .setTitle(R.string.Travel_Deleting)
                .setMessage(R.string.Msg_Confirm)
                .setPositiveButton(R.string.Yes, (dialogInterface, i) -> {
                    try {
                        Database.mTravelDao.deleteTravel(travel.getId());
                        mTravel.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mTravel.size());
                    } catch (Exception e) {
                        Toast.makeText(context, context.getString(R.string.Error_Deleting_Data) + "\n" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton(R.string.No, null)
                .show());
    }

    @Override
    public int getItemCount() {
        return mTravel.size();
    }
}