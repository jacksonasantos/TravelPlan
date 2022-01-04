package com.jacksonasantos.travelplan.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.general.DialogHeader;
import com.jacksonasantos.travelplan.dao.general.DialogItem;
import com.jacksonasantos.travelplan.dao.general.ListItemDialog;

import java.util.ArrayList;

public class HomeInsuranceContactDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<ListItemDialog> items;

    public HomeInsuranceContactDataAdapter(ArrayList<ListItemDialog> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == ListItemDialog.TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_insurance_contact_header, parent, false);
            return new VHHeader(v);
        } else if(viewType == ListItemDialog.TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_insurance_contact_item, parent, false);
            return new VHItem(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof VHHeader) {
            DialogHeader header = (DialogHeader) items.get(position);
            VHHeader VHheader = (VHHeader)holder;
            VHheader.tvTypeContact.setText(header.getTxt_header());
        } else if(holder instanceof VHItem) {
            DialogItem dialogItem = (DialogItem) items.get(position);
            VHItem VHitem = (VHItem)holder;
            VHitem.tvDescriptionContact.setText(dialogItem.getTxt_description());
            VHitem.tvDetailContact.setText(dialogItem.getTxt_detail());
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getItemType();
    }

    static class VHHeader extends RecyclerView.ViewHolder{
        final TextView tvTypeContact;
        public VHHeader(View itemView) {
            super(itemView);
            this.tvTypeContact = itemView.findViewById(R.id.tvTypeContact);
        }
    }

    static class VHItem extends RecyclerView.ViewHolder{
        final TextView tvDescriptionContact;
        final TextView tvDetailContact;
        public VHItem(View itemView) {
            super(itemView);
            this.tvDescriptionContact = itemView.findViewById(R.id.tvDescriptionContact);
            this.tvDetailContact = itemView.findViewById(R.id.tvDetailContact);
        }
    }
}