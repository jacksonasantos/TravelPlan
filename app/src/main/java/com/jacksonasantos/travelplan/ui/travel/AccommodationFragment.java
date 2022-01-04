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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.general.Database;

public class AccommodationFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_generic_list, container, false);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResume() {
        super.onResume();
        Database mDb = new Database(getContext());
        mDb.open();

        RecyclerView listAccommodation = this.requireView().findViewById(R.id.list);
        AccommodationListAdapter adapter = new AccommodationListAdapter(Database.mAccommodationDao.fetchAllAccommodation(), getContext());
        listAccommodation.setAdapter(adapter);
        listAccommodation.setLayoutManager(new LinearLayoutManager(getContext()));
        listAccommodation.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        adapter.notifyDataSetChanged();
        mDb.close();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem m1 = menu.findItem(R.id.addmenu);
        MenuItem m2 = menu.findItem(R.id.savemenu);
        MenuItem m3 = menu.findItem(R.id.filtermenu);
        m1.setVisible(true);
        m2.setVisible(false);
        m3.setVisible(false);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        if (item.getItemId() == R.id.addmenu) {
            intent = new Intent(getContext(), AccommodationActivity.class);
            startActivity(intent);
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }
}