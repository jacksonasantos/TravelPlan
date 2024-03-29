package com.jacksonasantos.travelplan.ui.travel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.general.Database;

public class TravelFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.main, menu);
                MenuItem m1 = menu.findItem(R.id.add_menu);
                MenuItem m2 = menu.findItem(R.id.save_menu);
                MenuItem m3 = menu.findItem(R.id.filter_menu);
                m1.setVisible(true);
                m2.setVisible(false);
                m3.setVisible(false);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem item) {
                Intent intent;
                if (item.getItemId() == R.id.add_menu) {
                    intent = new Intent(getContext(), TravelActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        return inflater.inflate(R.layout.fragment_generic_list, container, false);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResume() {
        super.onResume();
        Database mDb = new Database(getContext());
        mDb.open();

        RecyclerView listTravel = this.requireView().findViewById(R.id.list);
        TravelListAdapter adapter = new TravelListAdapter(Database.mTravelDao.fetchAllTravel(), getContext());
        listTravel.setAdapter(adapter);
        listTravel.setLayoutManager(new LinearLayoutManager(getContext()));
        listTravel.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        adapter.notifyDataSetChanged();
        mDb.close();
    }
}