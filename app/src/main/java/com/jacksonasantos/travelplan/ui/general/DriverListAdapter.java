package com.jacksonasantos.travelplan.ui.general;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Driver;
import com.jacksonasantos.travelplan.dao.general.Database;

import java.util.List;

public class DriverListAdapter extends RecyclerView.Adapter<DriverListAdapter.MyViewHolder> {

    private final List<Driver> mDriver;
    final Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView txtName;
        public final ImageButton btnEdit;
        public final ImageButton btnDelete;

        public MyViewHolder(View v) {
            super(v);
            txtName = v.findViewById(R.id.txtName);
            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
            btnEdit.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }
    }

    public DriverListAdapter(List<Driver> driver, Context context) {
        this.mDriver = driver;
        this.context = context;

        Database mdb = new Database(context);
        mdb.open();
    }

    @NonNull
    @Override
    public DriverListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View driverView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_driver, parent, false);

        return new MyViewHolder(driverView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Driver driver = mDriver.get(position);

        holder.txtName.setText(driver.getName());
        // btnEdit
        holder.btnEdit.setOnClickListener (v -> {
            Intent intent = new Intent (v.getContext(), DriverActivity.class);
            intent.putExtra("driver_id", driver.getId());
            context.startActivity(intent);
            notifyItemChanged(position);
        });

        // btnDelete
        holder.btnDelete.setOnClickListener (v -> new AlertDialog.Builder(v.getContext())
                .setTitle(R.string.Driver_Deleting)
                .setMessage(R.string.Msg_Confirm)
                .setPositiveButton(R.string.Yes, (dialogInterface, i) -> {
                    try {
                        Database.mDriverDao.deleteDriver(driver.getId());
                        mDriver.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mDriver.size());
                    } catch (Exception e) {
                        Toast.makeText(context, context.getString(R.string.Error_Deleting_Data) + "\n" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton(R.string.No, null)
                .show());
    }

    @Override
    public int getItemCount() {
        return mDriver.size();
    }
}