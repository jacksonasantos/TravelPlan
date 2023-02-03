package com.jacksonasantos.travelplan.ui.vehicle;

import android.content.Context;
import android.content.Intent;
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

public class PendingVehicleListAdapter extends RecyclerView.Adapter<PendingVehicleListAdapter.MyViewHolder> {

    private final List<PendingVehicle> mPendingVehicle;
    final Context context;

    final Globals g = Globals.getInstance();

    final Locale locale = new Locale(g.getLanguage(), g.getCountry());
    final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ConstraintLayout llPendingVehicle;
        public final ImageView imgServiceType;
        public final TextView txtNote;
        public final TextView txtExpectedValue;
        public final TextView txtVehicleShortName;
        public final ImageButton btnDelete;

        public MyViewHolder(View v) {
            super(v);
            llPendingVehicle = v.findViewById(R.id.llPendingVehicle);
            imgServiceType = v.findViewById(R.id.imgServiceType);
            txtNote = v.findViewById(R.id.txtNote);
            txtExpectedValue = v.findViewById(R.id.txtExpectedValue);
            txtVehicleShortName = v.findViewById(R.id.txtVehicleShortName);
            btnDelete = v.findViewById(R.id.btnDelete);
            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }
    }

    public PendingVehicleListAdapter(List<PendingVehicle> pendingVehicle, Context context) {
        this.mPendingVehicle = pendingVehicle;
        this.context = context;

        Database mdb = new Database(context);
        mdb.open();
    }

    @NonNull
    @Override
    public PendingVehicleListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View pendingVehicleView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_home_vehicle_item_pending_vehicle, parent, false);

        return new MyViewHolder(pendingVehicleView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final PendingVehicle pendingVehicle = mPendingVehicle.get(position);

        holder.imgServiceType.setImageResource(NextMaintenanceItem.getServiceTypeImage(pendingVehicle.getService_type()));
        holder.txtNote.setText(pendingVehicle.getNote());
        holder.txtExpectedValue.setText(currencyFormatter.format(pendingVehicle.getExpected_value()));
        holder.txtVehicleShortName.setText(Database.mVehicleDao.fetchVehicleById(pendingVehicle.getVehicle_id()).getShort_name());

        if ( pendingVehicle.getStatus_pending() > 0 ) {
            holder.txtNote.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.txtExpectedValue.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.txtVehicleShortName.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }

        holder.llPendingVehicle.setOnClickListener(v -> {
            Intent intent = new Intent (v.getContext(), PendingVehicleActivity.class);
            intent.putExtra("pending_vehicle_id", pendingVehicle.getId());
            context.startActivity(intent);
            notifyItemChanged(position);
        });

        // btnDelete
        holder.btnDelete.setOnClickListener (v1 -> new AlertDialog.Builder(v1.getContext())
                .setTitle(R.string.PendingVehicle_Deleting)
                .setMessage(R.string.Msg_Confirm)
                .setPositiveButton(R.string.Yes, (dialogInterface, i) -> {
                    try {
                        Database.mPendingVehicleDao.deletePendingVehicle(pendingVehicle.getId());
                        mPendingVehicle.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mPendingVehicle.size());
                    } catch (Exception e) {
                        Toast.makeText(context, context.getString(R.string.Error_Deleting_Data) + "\n" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton(R.string.No, null)
                .show());
    }

    @Override
    public int getItemCount() {
        return mPendingVehicle.size();
    }
}