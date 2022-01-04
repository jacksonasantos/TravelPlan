package com.jacksonasantos.travelplan.ui.travel;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Accommodation;
import com.jacksonasantos.travelplan.dao.general.Database;

import java.util.List;

public class AccommodationListAdapter extends RecyclerView.Adapter<AccommodationListAdapter.MyViewHolder> {

    private final List<Accommodation> mAccommodation;
    final Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView txtNameAccommodation;
        public final ImageButton btnEdit;
        public final ImageButton btnDelete;

        public MyViewHolder(View v) {
            super(v);
            txtNameAccommodation = v.findViewById(R.id.txtNameAchievement);
            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
            btnEdit.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }
    }

    public AccommodationListAdapter(List<Accommodation> accommodation, Context context) {
        this.mAccommodation = accommodation;
        this.context = context;

        Database mdb = new Database(context);
        mdb.open();
    }

    @NonNull
    @Override
    public AccommodationListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View accommodationView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_accommodation, parent, false);

        return new MyViewHolder(accommodationView);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Accommodation accommodation = mAccommodation.get(position);

        holder.txtNameAccommodation.setText(accommodation.getName());
        // btnEdit
        holder.btnEdit.setOnClickListener (v -> {
            Intent intent = new Intent (v.getContext(), AccommodationActivity.class);
            intent.putExtra("accommodation_id", accommodation.getId());
            context.startActivity(intent);
            notifyItemChanged(position);
        });

        // btnDelete
        holder.btnDelete.setOnClickListener (v -> new AlertDialog.Builder(v.getContext())
                .setTitle(R.string.Accommodation_Deleting)
                .setMessage(R.string.Msg_Confirm)
                .setPositiveButton(R.string.Yes, (dialogInterface, i) -> {
                    try {
                        Database.mAccommodationDao.deleteAccommodation(accommodation.getId());
                        mAccommodation.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mAccommodation.size());
                    } catch (Exception e) {
                        Toast.makeText(context, context.getString(R.string.Error_Deleting_Data) + "\n" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton(R.string.No, null)
                .show());
    }

    @Override
    public int getItemCount() {
        return mAccommodation.size();
    }
}