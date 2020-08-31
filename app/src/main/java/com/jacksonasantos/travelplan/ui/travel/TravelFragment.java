package com.jacksonasantos.travelplan.ui.travel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;

public class TravelFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_travel, container, false);
        //final TextView textView = root.findViewById(R.id.text_slideshow);
        RecyclerView recycleList = root.findViewById(R.id.listTravels);
        recycleList.setHasFixedSize(true);
        recycleList.setLayoutManager(new LinearLayoutManager(recycleList.getContext()));
        recycleList.setAdapter(new RandomNumListAdapter(1234));

       // List<Travel> travels = null;
       // TravelAdapter travelAdapter = new TravelAdapter(this.getActivity(), travels);
       // recycleList.setAdapter(travelAdapter);

        return root;
    }
}
