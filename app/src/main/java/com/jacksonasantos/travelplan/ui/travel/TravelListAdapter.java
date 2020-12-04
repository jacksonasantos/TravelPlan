package com.jacksonasantos.travelplan.ui.travel;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import com.jacksonasantos.travelplan.dao.Database;
import com.jacksonasantos.travelplan.dao.Travel;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.List;

public class TravelListAdapter extends RecyclerView.Adapter<TravelListAdapter.MyViewHolder> {

    private final List<Travel> mTravel;
    Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imStatus;
        public TextView txtDescription;
        public TextView txtDeparture;
        public ImageButton btnEdit;
        public ImageButton btnDelete;

        public MyViewHolder(View v) {
            super(v);
            imStatus = v.findViewById(R.id.imStatus);
            txtDescription = v.findViewById(R.id.txtDescription);
            txtDeparture = v.findViewById(R.id.txtDeparture);
            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Travel travel = mTravel.get(position);

        holder.imStatus.setImageResource(R.drawable.ic_ball );
        switch (travel.getStatus()) {
            case 0:holder.imStatus.setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);
            case 1:holder.imStatus.setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
            case 2:holder.imStatus.setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
            case 3:holder.imStatus.setColorFilter(Color.MAGENTA, PorterDuff.Mode.MULTIPLY);
            default:holder.imStatus.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        }
        holder.txtDescription.setText(travel.getDescription());
        holder.txtDeparture.setText(Utils.dateToString(travel.getDeparture_date()));
        // btnEdit
        holder.btnEdit.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), TravelActivity.class);
                intent.putExtra("travel_id", travel.getId());
                context.startActivity(intent);
                notifyDataSetChanged();
            }
        });

        // btnDelete
        holder.btnDelete.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle(R.string.Travel_Deleting)
                        .setMessage(R.string.Msg_Confirm)
                        .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    Database.mTravelDao.deleteTravel(travel.getId());
                                    mTravel.remove(position);
                                    notifyItemRemoved(position);
                                } catch (Exception e) {
                                    Toast.makeText(context, R.string.Error_Deleting_Data + e.getMessage() , Toast.LENGTH_LONG).show();
                                }
                            }
                        }).setNegativeButton(R.string.No, null)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTravel.size();
    }
}