package com.jacksonasantos.travelplan.ui.travel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;

import java.util.Random;

public class RandomNumListAdapter extends RecyclerView.Adapter<TravelViewModel> {
    private Random random;

    public RandomNumListAdapter(int seed) {
        this.random = new Random(seed);
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.fragment_item_travel;
    }

    @NonNull
    @Override
    public TravelViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new TravelViewModel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TravelViewModel holder, final int position) {
        holder.getView().setText(String.valueOf(random.nextInt()));
    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
