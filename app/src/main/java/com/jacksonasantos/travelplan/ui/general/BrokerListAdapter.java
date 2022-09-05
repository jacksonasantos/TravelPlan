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
import com.jacksonasantos.travelplan.dao.Broker;
import com.jacksonasantos.travelplan.dao.general.Database;

import java.util.List;

public class BrokerListAdapter extends RecyclerView.Adapter<BrokerListAdapter.MyViewHolder> {

        private final List<Broker> mBroker;
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

    public BrokerListAdapter(List<Broker> broker, Context context) {
        this.mBroker = broker;
        this.context = context;

        Database mdb = new Database(context);
        mdb.open();
    }

    @NonNull
    @Override
    public BrokerListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View brokerView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_broker, parent, false);

        return new MyViewHolder(brokerView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Broker broker = mBroker.get(position);

        holder.txtName.setText(broker.getName());
        // btnEdit
        holder.btnEdit.setOnClickListener (v -> {
            Intent intent = new Intent (v.getContext(), BrokerActivity.class);
            intent.putExtra("broker_id", broker.getId());
            context.startActivity(intent);
            notifyItemChanged(position);
        });

        // btnDelete
        holder.btnDelete.setOnClickListener (v -> new AlertDialog.Builder(v.getContext())
                .setTitle(R.string.Broker_Deleting)
                .setMessage(R.string.Msg_Confirm)
                .setPositiveButton(R.string.Yes, (dialogInterface, i) -> {
                    try {
                        Database.mBrokerDao.deleteBroker(broker.getId());
                        mBroker.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mBroker.size());
                    } catch (Exception e) {
                        Toast.makeText(context, context.getString(R.string.Error_Deleting_Data) + "\n" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton(R.string.No, null)
                .show());
    }

    @Override
    public int getItemCount() {
        return mBroker.size();
    }
}