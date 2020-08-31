package com.jacksonasantos.travelplan.ui.travel;

import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import com.jacksonasantos.travelplan.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TravelViewModel extends RecyclerView.ViewHolder {

    private TextView view;
    public TravelViewModel(@NonNull View itemView) {
        super(itemView);
        view = itemView.findViewById(R.id.randomText);
    }

    public TextView getView(){ return view;  }
}