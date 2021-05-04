package com.jacksonasantos.travelplan.ui.travel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.jacksonasantos.travelplan.R;

import java.util.ArrayList;

public class PlacesAdapter extends RecyclerView.Adapter <PlacesAdapter.PlaceViewHolder>{

    private final ArrayList<PlaceLikelihood> places ;

    private OnItemClicked onClick;

    public interface OnItemClicked {
        void onItemClick(PlaceLikelihood position);
    }

    public PlacesAdapter(ArrayList<PlaceLikelihood> places){
        this.places = places;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item_place, parent, false);
        return new PlaceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int position) {
        holder.tv_place_name.setText(places.get(position).getPlace().getName());
        holder.tv_place_address.setText(places.get(position).getPlace().getAddress());
        holder.itemView.setOnClickListener(view -> onClick.onItemClick(places.get(position)));
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setOnClick(OnItemClicked onClick) { this.onClick=onClick; }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public static class PlaceViewHolder extends RecyclerView.ViewHolder{
        TextView tv_place_name;
        TextView tv_place_address;
        CardView cardPlace;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            cardPlace = itemView.findViewById(R.id.cardPlace);
            tv_place_name = itemView.findViewById(R.id.tv_place_name);
            tv_place_address = itemView.findViewById(R.id.tv_place_address);
        }
    }
}