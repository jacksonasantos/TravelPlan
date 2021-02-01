package com.jacksonasantos.travelplan.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Accommodation;
import com.jacksonasantos.travelplan.dao.Database;
import com.jacksonasantos.travelplan.dao.Reservation;
import com.jacksonasantos.travelplan.ui.travel.ReservationActivity;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.List;

public class HomeTravelReservationListAdapter extends RecyclerView.Adapter<HomeTravelReservationListAdapter.MyViewHolder> {

    private final List<Reservation> mReservation;
    Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final LinearLayout llReservationItem;
        private final TextView txtAccommodation;
        private final TextView txtVoucher;
        private final TextView txtCheckin;
        private final TextView txtDaily;
        private final ImageButton btnEdit;
        private final ImageButton btnDelete;

        public MyViewHolder(View v) {
            super(v);
            llReservationItem = v.findViewById(R.id.llReservationItem);
            txtAccommodation = v.findViewById(R.id.txtAccommodation);
            txtVoucher = v.findViewById(R.id.txtVoucher);
            txtCheckin = v.findViewById(R.id.txtCheckin);
            txtDaily = v.findViewById(R.id.txtDaily);
            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
        }

        @Override
        public void onClick(View view) {
        }
    }

    public HomeTravelReservationListAdapter(List<Reservation> reservation, Context context) {
        this.mReservation = reservation;
        this.context = context;

        Database mdb = new Database(context);
        mdb.open();
    }

    @NonNull
    @Override
    public HomeTravelReservationListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View reservationView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_home_travel_item_reservation, parent, false);

        return new MyViewHolder(reservationView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    //@SuppressLint("ResourceAsColor")
    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Reservation reservation = mReservation.get(position);
        if (position%2==0) {
            holder.llReservationItem.setBackgroundColor(Color.WHITE);
        } else {
            holder.llReservationItem.setBackgroundColor(Color.rgb(209,193,233));
        }

        Accommodation a1 = Database.mAccommodationDao.fetchAccommodationById(reservation.getAccommodation_id());
        holder.txtAccommodation.setText(a1.getName());
        holder.txtVoucher.setText(reservation.getVoucher_number());
        holder.txtCheckin.setText(Utils.dateToString(reservation.getCheckin_date()));
        holder.txtDaily.setText(Integer.toString(Utils.diffBetweenDate(reservation.getCheckout_date(), reservation.getCheckin_date())));

        // btnEdit
        holder.btnEdit.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), ReservationActivity.class);
                intent.putExtra("reservation_id", reservation.getId());
                context.startActivity(intent);
                notifyDataSetChanged();
            }
        });

        // btnDelete
        holder.btnDelete.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle(R.string.Reservation_Deleting)
                        .setMessage(R.string.Msg_Confirm)
                        .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    Database.mReservationDao.deleteReservation(reservation.getId());
                                    mReservation.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position,mReservation.size());
                                } catch (Exception e) {
                                    Toast.makeText(context, context.getString(R.string.Error_Deleting_Data) + "\n" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }).setNegativeButton(R.string.No, null)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mReservation.size();
    }
}