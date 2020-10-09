package com.jacksonasantos.travelplan.ui.vehicle;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.dao.Database;
import com.jacksonasantos.travelplan.R;

import java.text.ParseException;

//TODO - Implantar Filtro para Vehicle Default

public class FuelSupplyFragment extends Fragment  {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_fuel_supply, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        Database mDb = new Database(getContext());
        mDb.open();

        RecyclerView listFuelSupply = this.getView().findViewById(R.id.listFuelSupply);

        FuelSupplyListAdapter adapter = null;
        try {
            adapter = new FuelSupplyListAdapter(Database.mFuelSupplyDao.fetchAllFuelSupplies(), getContext());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        listFuelSupply.setAdapter(adapter);
        listFuelSupply.setLayoutManager(new LinearLayoutManager(getContext()));

        mDb.close();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem m = menu.findItem(R.id.addmenu);
        m.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch(item.getItemId()) {
            case R.id.addmenu:
                intent = new Intent( getContext(), FuelSupplyActivity.class );
                startActivity( intent );
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}
