package com.jacksonasantos.travelplan.ui.travel;

import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.jacksonasantos.travelplan.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlacesAdapter extends RecyclerView.Adapter <PlacesAdapter.PlaceViewHolder>{

    private final ArrayList<PlaceLikelihood> places ;
    private final Geocoder geocoder;

    //declare interface
    private OnItemClicked onClick;

    //make interface like this
    public interface OnItemClicked {
        void onItemClick(PlaceLikelihood position);
    }

    public PlacesAdapter(ArrayList<PlaceLikelihood> places, Geocoder geocoder){
        this.places = places;
        this.geocoder = geocoder;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item_place, parent, false);
        PlaceViewHolder vh = new PlaceViewHolder(v);
        return vh;
    }

    private String getCityNameByCoordinates(double lat, double lon) throws IOException {
        List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
        if (addresses != null && addresses.size() > 0) {
            return addresses.get(0).getSubAdminArea() + " - "+ addresses.get(0).getAdminArea();
        }
        return null;
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int position) {
        holder.tv_place_name.setText(places.get(position).getPlace().getName());
        holder.tv_place_address.setText(places.get(position).getPlace().getAddress());
        try {
            holder.tv_place_location.setText(getCityNameByCoordinates(Objects.requireNonNull(places.get(position).getPlace().getLatLng()).latitude, Objects.requireNonNull(places.get(position).getPlace().getLatLng()).longitude));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        TextView tv_place_location;
        CardView cardPlace;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            cardPlace = itemView.findViewById(R.id.cardPlace);
            tv_place_name = itemView.findViewById(R.id.tv_place_name);
            tv_place_address = itemView.findViewById(R.id.tv_place_address);
            tv_place_location = itemView.findViewById(R.id.tv_place_location);
        }
    }
}