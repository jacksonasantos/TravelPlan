package com.jacksonasantos.travelplan.ui.travel;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Transport;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.List;

public class TravelTransportListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private final List<Transport> mTransport;
    final Context context;
    final int show_header;
    final Integer mTravel_id;
    public final String form;

    public TravelTransportListAdapter(List<Transport> transports, Context context, String form, int show_header, Integer travel_id) {
        this.mTransport = transports;
        this.mTravel_id = travel_id;
        this.context = context;
        this.form = form;
        this.show_header = show_header; // 0 - NO SHOW HEADER | 1 - SHOW HEADER
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View transportView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_travel_transport, parent, false);

        if (viewType == TYPE_HEADER) {
            return new HeaderViewHolder(transportView);
        } else return new ItemViewHolder(transportView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (holder instanceof HeaderViewHolder){
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.llTransportTravelItem.setBackgroundColor(Utils.getColorWithAlpha(R.color.colorItemList,0.1f));
            headerViewHolder.imgTransportType.setImageBitmap(null);
            headerViewHolder.txtTransportDescription.setText(R.string.Transport_Description);
            headerViewHolder.txtTransportIdentifier.setText(R.string.Transport_Identifier);
            headerViewHolder.btnAddTransport.setImageResource(R.drawable.ic_button_add);
            headerViewHolder.btnAddTransport.setOnClickListener(v -> {
                Intent intent = new Intent (v.getContext(), TransportActivity.class);
                intent.putExtra("travel_id", mTravel_id);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                notifyItemChanged(position);
            });
        }
        else if (holder instanceof ItemViewHolder) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

            itemViewHolder.imgTransportType.setImageResource(Transport.getTransportTypeImage(mTransport.get(position-show_header).getTransport_type()));
            if (mTransport.get(position-show_header).getTransport_type() == 0 ) {
                itemViewHolder.txtTransportDescription.setText(R.string.Onw);
            } else {
                itemViewHolder.txtTransportDescription.setText(mTransport.get(position - show_header).getDescription());
                itemViewHolder.txtTransportIdentifier.setText(mTransport.get(position - show_header).getIdentifier());
                itemViewHolder.btnDelete.setVisibility(View.VISIBLE);
            }

            itemViewHolder.llTransportTravelItem.setOnClickListener( v-> {
                Intent intent = new Intent (v.getContext(), TransportActivity.class);
                intent.putExtra("transport_id", mTransport.get(position-show_header).getId());
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                notifyItemChanged(position);
            });
            itemViewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemViewHolder.btnDelete.setOnClickListener(this);
                    new AlertDialog.Builder(v.getContext())
                            .setTitle(R.string.Transport_Deleting)
                            .setMessage(R.string.Msg_Confirm)
                            .setPositiveButton(R.string.Yes, (dialogInterface, i) -> {
                                try {
                                    Database.mTransportDao.deleteTransport(mTransport.get(position-show_header).getId());
                                    mTransport.remove(position-show_header);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, mTransport.size());
                                } catch (Exception e) {
                                    Toast.makeText(context, context.getString(R.string.Error_Deleting_Data) + "\n" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                }
                            }).setNegativeButton(R.string.No, null)
                            .show();
                }
            });
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
        return mTransport.size()+show_header;
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout llTransportTravelItem;
        public final ImageView imgTransportType;
        public final TextView txtTransportDescription;
        public final TextView txtTransportIdentifier;
        public final ImageButton btnAddTransport;

        public HeaderViewHolder(View v) {
            super(v);
            llTransportTravelItem = v.findViewById(R.id.llTransportTravelItem);
            imgTransportType = v.findViewById(R.id.imgTransportType);
            txtTransportDescription = v.findViewById(R.id.txtTransportDescription);
            txtTransportIdentifier = v.findViewById(R.id.txtTransportIdentifier);
            btnAddTransport = v.findViewById(R.id.btnDelete);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout llTransportTravelItem;
        public final ImageView imgTransportType;
        public final TextView txtTransportDescription;
        public final TextView txtTransportIdentifier;
        public final ImageButton btnDelete;

        public ItemViewHolder(View v) {
            super(v);
            llTransportTravelItem = v.findViewById(R.id.llTransportTravelItem);
            imgTransportType = v.findViewById(R.id.imgTransportType);
            txtTransportDescription = v.findViewById(R.id.txtTransportDescription);
            txtTransportIdentifier = v.findViewById(R.id.txtTransportIdentifier);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }
}