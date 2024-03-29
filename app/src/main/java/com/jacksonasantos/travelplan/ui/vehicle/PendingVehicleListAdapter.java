package com.jacksonasantos.travelplan.ui.vehicle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.NextMaintenanceItem;
import com.jacksonasantos.travelplan.dao.PendingVehicle;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.Globals;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class PendingVehicleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final Globals g = Globals.getInstance();

    final Locale locale = new Locale(g.getLanguage(), g.getCountry());
    final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private final List<PendingVehicle> mPendingVehicle;
    final Context context;
    final int show_header;
    final int is_home;

    public PendingVehicleListAdapter(List<PendingVehicle> pendingVehicle, Context context, int show_header, int is_home) {
        this.mPendingVehicle = pendingVehicle;
        this.context = context;
        this.show_header = show_header;
        this.is_home = is_home;

        Database mdb = new Database(context);
        mdb.open();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View pendingVehicleView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_home_vehicle_item_pending_vehicle, parent, false);

        if (viewType == TYPE_HEADER) {
            return new HeaderViewHolder(pendingVehicleView);
        }
        else return new ItemViewHolder(pendingVehicleView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderViewHolder){
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

            headerViewHolder.llPendingVehicle.setBackgroundColor(Color.LTGRAY);
            headerViewHolder.txtNote.setText(R.string.PendingVehicle_Note);
            headerViewHolder.txtExpectedValue.setText(R.string.PendingVehicle_ExpectedValue);
            headerViewHolder.txtVehicleShortName.setText(R.string.Vehicle_Name);
        }
        else if (holder instanceof ItemViewHolder) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            final PendingVehicle pendingVehicle = mPendingVehicle.get(position-show_header);

            if (position % 2 == 0) {
                itemViewHolder.llPendingVehicle.setBackgroundColor(Color.LTGRAY);
            } else {
                itemViewHolder.llPendingVehicle.setBackgroundColor(Color.WHITE);
            }

            itemViewHolder.imgServiceType.setImageResource(NextMaintenanceItem.getServiceTypeImage(pendingVehicle.getService_type()));
            itemViewHolder.txtNote.setText(pendingVehicle.getNote());
            if (pendingVehicle.getCurrency_type()==0) {
                itemViewHolder.txtExpectedValue.setText(currencyFormatter.format(pendingVehicle.getExpected_value()));
            } else {
                itemViewHolder.txtExpectedValue.setText(context.getResources().getStringArray(R.array.currency_array)[pendingVehicle.getCurrency_type()] +" "+ pendingVehicle.getExpected_value());
            }
            if (is_home==1) {
                itemViewHolder.txtVehicleShortName.setText(Database.mVehicleDao.fetchVehicleById(pendingVehicle.getVehicle_id()).getShort_name());
            }

            if ( pendingVehicle.getStatus_pending() > 0 ) {
                itemViewHolder.txtNote.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                itemViewHolder.txtExpectedValue.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                itemViewHolder.txtVehicleShortName.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }

            itemViewHolder.llPendingVehicle.setOnClickListener(v -> {
                Intent intent = new Intent (v.getContext(), PendingVehicleActivity.class);
                intent.putExtra("pending_vehicle_id", pendingVehicle.getId());
                context.startActivity(intent);
                notifyItemChanged(position);
            });
            // btnDelete
            itemViewHolder.btnDelete.setOnClickListener (v -> new AlertDialog.Builder(v.getContext())
                    .setTitle(R.string.PendingVehicle_Deleting)
                    .setMessage(R.string.Msg_Confirm)
                    .setPositiveButton(R.string.Yes, (dialogInterface, i) -> {
                        try {
                            Database.mPendingVehicleDao.deletePendingVehicle(pendingVehicle.getId());
                            mPendingVehicle.remove(position-show_header);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, mPendingVehicle.size());
                        } catch (Exception e) {
                            Toast.makeText(context, context.getString(R.string.Error_Deleting_Data) + "\n" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton(R.string.No, null)
                    .show());
        }
    }
    public int getItemViewType(int position) {
        if (position == 0 && show_header == 1) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mPendingVehicle.size()+show_header;
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final ConstraintLayout llPendingVehicle;
        private final TextView txtNote;
        private final TextView txtExpectedValue;
        private final TextView txtVehicleShortName;

        public HeaderViewHolder(View v) {
            super(v);
            llPendingVehicle = v.findViewById(R.id.llPendingVehicle);
            txtNote = v.findViewById(R.id.txtNote);
            txtExpectedValue = v.findViewById(R.id.txtExpectedValue);
            txtVehicleShortName = v.findViewById(R.id.txtVehicleShortName);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final ConstraintLayout llPendingVehicle;
        private final ImageView imgServiceType;
        private final TextView txtNote;
        private final TextView txtExpectedValue;
        private final TextView txtVehicleShortName;
        private final ImageButton btnDelete;

        public ItemViewHolder(View v) {
            super(v);
            llPendingVehicle = v.findViewById(R.id.llPendingVehicle);
            imgServiceType = v.findViewById(R.id.imgServiceType);
            txtNote = v.findViewById(R.id.txtNote);
            txtExpectedValue = v.findViewById(R.id.txtExpectedValue);
            txtVehicleShortName = v.findViewById(R.id.txtVehicleShortName);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }
}