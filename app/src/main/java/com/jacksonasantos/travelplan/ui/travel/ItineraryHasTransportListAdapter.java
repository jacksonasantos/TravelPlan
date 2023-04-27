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
import com.jacksonasantos.travelplan.dao.Itinerary;
import com.jacksonasantos.travelplan.dao.ItineraryHasTransport;
import com.jacksonasantos.travelplan.dao.Person;
import com.jacksonasantos.travelplan.dao.Transport;
import com.jacksonasantos.travelplan.dao.Vehicle;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.List;

public class ItineraryHasTransportListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private final List<ItineraryHasTransport> mItineraryHasTransport;
    final Context context;
    final int show_header;
    final Integer mTravel_id;
    public final String form;

    public ItineraryHasTransportListAdapter(List<ItineraryHasTransport> itineraryHasTransport, Context context, String form, int show_header, Integer travel_id) {
        this.mItineraryHasTransport = itineraryHasTransport;
        this.mTravel_id = travel_id;
        this.context = context;
        this.form = form;
        this.show_header = show_header;
    }
    
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itineraryHasTransportView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_itinerary_has_transport, parent, false);

        if (viewType == TYPE_HEADER) {
            return new ItineraryHasTransportListAdapter.HeaderViewHolder(itineraryHasTransportView);
        } else return new ItineraryHasTransportListAdapter.ItemViewHolder(itineraryHasTransportView);
    }

    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (holder instanceof HeaderViewHolder){
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

            headerViewHolder.llItineraryHasTransport.setBackgroundColor(Utils.getColorWithAlpha(R.color.colorItemList,0.1f));
            headerViewHolder.txtTransport.setText(R.string.Itinerary_has_Transport_Transport);
            headerViewHolder.txtPerson.setText(R.string.Itinerary_has_Transport_Person);
            headerViewHolder.txtItinerary.setText(R.string.Itinerary_has_Transport_Itinerary);
            headerViewHolder.btnAdd.setImageResource(R.drawable.ic_button_add);
            headerViewHolder.btnAdd.setOnClickListener(v -> {
                Intent intent = new Intent (v.getContext(), ItineraryHasTransportActivity.class);
                intent.putExtra("travel_id", mTravel_id);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                notifyItemChanged(position);
            });
        }
        else if (holder instanceof ItemViewHolder) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

            ItineraryHasTransport itineraryHasTransport = mItineraryHasTransport.get(position-show_header);

            Person p1 = Database.mPersonDao.fetchPersonById(itineraryHasTransport.getPerson_id());
            Itinerary i1 = Database.mItineraryDao.fetchItineraryById(itineraryHasTransport.getItinerary_id());
            String vTransport;
            if (itineraryHasTransport.getTransport_type()==0) {
                Vehicle v1 = Database.mVehicleDao.fetchVehicleById(itineraryHasTransport.getVehicle_id());
                vTransport = v1.getShort_name();
            } else {
                Transport t1 = Database.mTransportDao.fetchTransportById(itineraryHasTransport.getTransport_id());
                vTransport = t1.getDescription();
            }
            itemViewHolder.imgType.setImageResource(Transport.getTransportTypeImage(itineraryHasTransport.getTransport_type()));
            itemViewHolder.txtTransport.setText(vTransport);
            itemViewHolder.txtPerson.setText(p1.getShort_Name());
            itemViewHolder.txtItinerary.setText(i1.toString());

            itemViewHolder.llItineraryHasTransport.setOnClickListener (v -> {
                Intent intent = new Intent (v.getContext(), ItineraryHasTransportActivity.class);
                intent.putExtra("itinerary_has_transport_id", itineraryHasTransport.getId());
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                notifyDataSetChanged();
            });

            itemViewHolder.btnDelete.setOnClickListener (v -> new AlertDialog.Builder(v.getContext())
                    .setTitle(R.string.Itinerary_has_Transport_Deleting)
                    .setMessage(R.string.Msg_Confirm)
                    .setPositiveButton(R.string.Yes, (dialogInterface, i) -> {
                        try {
                            Database.mItineraryHasTransportDao.deleteItineraryHasTransport(mItineraryHasTransport.get(position - show_header).getId());
                            mItineraryHasTransport.remove(position - show_header);
                            notifyItemRemoved(position - show_header);
                            notifyItemRangeChanged(position - show_header, mItineraryHasTransport.size());
                        } catch (Exception e) {
                            Toast.makeText(context, context.getString(R.string.Error_Deleting_Data) + "\n" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    }).setNegativeButton(R.string.No, null)
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
        return mItineraryHasTransport.size()+show_header;
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout llItineraryHasTransport;
        public final ImageView imgType;
        public final TextView txtTransport;
        public final TextView txtPerson;
        public final TextView txtItinerary;
        public final ImageButton btnAdd;

        public HeaderViewHolder(View v) {
            super(v);
            llItineraryHasTransport = v.findViewById(R.id.llItineraryHasTransport);
            imgType = v.findViewById(R.id.imgType);
            txtTransport = v.findViewById(R.id.txtTransport);
            txtPerson = v.findViewById(R.id.txtPerson);
            txtItinerary = v.findViewById(R.id.txtItinerary);
            btnAdd = v.findViewById(R.id.btnDelete);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout llItineraryHasTransport;
        public final ImageView imgType;
        public final TextView txtTransport;
        public final TextView txtPerson;
        public final TextView txtItinerary;
        public final ImageButton btnDelete;

        public ItemViewHolder(View v) {
            super(v);
            llItineraryHasTransport = v.findViewById(R.id.llItineraryHasTransport);
            imgType = v.findViewById(R.id.imgType);
            txtTransport = v.findViewById(R.id.txtTransport);
            txtPerson = v.findViewById(R.id.txtPerson);
            txtItinerary = v.findViewById(R.id.txtItinerary);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }
}