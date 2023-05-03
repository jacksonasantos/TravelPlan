package com.jacksonasantos.travelplan.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Reservation;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.travel.ReservationActivity;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.List;
import java.util.Objects;

public class HomeTravelReservationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private final List<Reservation> mReservation;
    final Context context;
    final int show_header;

    public HomeTravelReservationListAdapter(List<Reservation> reservation, Context context, int show_header) {
        this.mReservation = reservation;
        this.context = context;
        this.show_header = show_header; // 0 - NO SHOW HEADER | 1 - SHOW HEADER

        Database mdb = new Database(context);
        mdb.open();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View reservationView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_home_travel_item_reservation, parent, false);

        if (viewType == TYPE_HEADER) {
            return new HeaderViewHolder(reservationView);
        }
        else return new ItemViewHolder(reservationView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderViewHolder){
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

            headerViewHolder.llReservationItem.setBackgroundColor(Color.LTGRAY);
            headerViewHolder.imgStatus.setVisibility(View.VISIBLE);
            headerViewHolder.txtAccommodation.setText(R.string.Accommodation_Name);
            headerViewHolder.txtVoucher.setText(R.string.Reservation_Voucher_Number);
            headerViewHolder.txtCheckin.setText(R.string.Reservation_Checkin_Date);
            headerViewHolder.txtDaily.setText(R.string.Reservation_Daily);
            headerViewHolder.btnDelete.setVisibility(View.INVISIBLE);
        }
        else if (holder instanceof ItemViewHolder) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            final Reservation reservation = mReservation.get(position-show_header);

            if (position%2==0) {
                itemViewHolder.llReservationItem.setBackgroundColor(Color.WHITE);
            } else {
                itemViewHolder.llReservationItem.setBackgroundColor(Utils.getColorWithAlpha(R.color.colorItemList,0.1f));
            }

            itemViewHolder.txtAccommodation.setText(Database.mAccommodationDao.fetchAccommodationById(reservation.getAccommodation_id()).getName());
            itemViewHolder.txtVoucher.setText(reservation.getVoucher_number());
            itemViewHolder.txtCheckin.setText(Objects.requireNonNull(Utils.dateToString(reservation.getCheckin_date())).substring(0,5));
            itemViewHolder.txtDaily.setText(Integer.toString(Utils.diffBetweenDate(reservation.getCheckout_date(), reservation.getCheckin_date())));

            itemViewHolder.imgStatus.setImageResource(reservation.getImage_Status_reservation(reservation.getStatus_reservation()));
            itemViewHolder.imgStatus.setOnClickListener( v -> {
                LayoutInflater li = LayoutInflater.from(v.getContext());
                View promptsView = li.inflate(R.layout.dialog_travel_item_reservation, null);

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                alertDialogBuilder.setView(promptsView);

                final Spinner spinStatusReservation = promptsView.findViewById(R.id.spinStatusReservation);
                final EditText etAmountPaid = promptsView.findViewById(R.id.etAmountPaid);

                spinStatusReservation.setSelection(reservation.getStatus_reservation());
                etAmountPaid.setText(String.valueOf(reservation.getAmount_paid()));

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton(R.string.OK, (dialog, id) -> {
                            boolean isSave = false;

                            if (!etAmountPaid.getText().toString().isEmpty()) {
                                reservation.setAmount_paid(Double.parseDouble(etAmountPaid.getText().toString()));
                            }
                            reservation.setStatus_reservation(spinStatusReservation.getSelectedItemPosition());

                            try {
                                isSave = Database.mReservationDao.updateReservation(reservation);
                                notifyItemChanged(position);
                            } catch (Exception e) {
                                Toast.makeText(context, R.string.Error_Changing_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            if (!isSave) {
                                Toast.makeText(context, R.string.Error_Saving_Data, Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton(R.string.Cancel, (dialog, id) -> dialog.cancel());
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            });

            itemViewHolder.llReservationItem.setOnClickListener (v -> {
                Intent intent = new Intent (v.getContext(), ReservationActivity.class);
                intent.putExtra("reservation_id", reservation.getId());
                context.startActivity(intent);
                notifyItemChanged(position);
            });

            itemViewHolder.btnDelete.setOnClickListener (v -> new AlertDialog.Builder(v.getContext())
                    .setTitle(R.string.Reservation_Deleting)
                    .setMessage(R.string.Msg_Confirm)
                    .setPositiveButton(R.string.Yes, (dialogInterface, i) -> {
                        try {
                            Database.mReservationDao.deleteReservation(reservation.getId());
                            mReservation.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, mReservation.size());
                        } catch (Exception e) {
                            Toast.makeText(context, context.getString(R.string.Error_Deleting_Data) + "\n" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton(R.string.No, null)
                    .show());
        }
    }
    @Override
    public int getItemViewType(int position) {
        if (position == 0 && show_header == 1) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mReservation.size()+show_header;
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout llReservationItem;
        private final ImageView imgStatus;
        private final TextView txtAccommodation;
        private final TextView txtVoucher;
        private final TextView txtCheckin;
        private final TextView txtDaily;
        private final ImageButton btnDelete;

        public HeaderViewHolder(View v) {
            super(v);
            llReservationItem = v.findViewById(R.id.llReservationItem);
            imgStatus = v.findViewById(R.id.imgStatus);
            txtAccommodation = v.findViewById(R.id.txtAccommodation);
            txtVoucher = v.findViewById(R.id.txtVoucher);
            txtCheckin = v.findViewById(R.id.txtCheckin);
            txtDaily = v.findViewById(R.id.txtDaily);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout llReservationItem;
        private final ImageView imgStatus;
        private final TextView txtAccommodation;
        private final TextView txtVoucher;
        private final TextView txtCheckin;
        private final TextView txtDaily;
        private final ImageButton btnDelete;

        public ItemViewHolder(View v) {
            super(v);
            llReservationItem = v.findViewById(R.id.llReservationItem);
            imgStatus = v.findViewById(R.id.imgStatus);
            txtAccommodation = v.findViewById(R.id.txtAccommodation);
            txtVoucher = v.findViewById(R.id.txtVoucher);
            txtCheckin = v.findViewById(R.id.txtCheckin);
            txtDaily = v.findViewById(R.id.txtDaily);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }
}
