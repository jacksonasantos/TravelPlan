package com.jacksonasantos.travelplan.ui.vehicle;

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
import com.jacksonasantos.travelplan.ui.utility.Globals;

public class FuelSupplyFragment extends Fragment  {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        requireActivity().addMenuProvider(new MenuProvider() {
            private Menu mMenu;

            final Globals g = Globals.getInstance();

            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
                menuInflater.inflate(R.menu.main, menu);

                this.mMenu = menu;
                MenuItem m1 = menu.findItem(R.id.add_menu);
                MenuItem m2 = menu.findItem(R.id.save_menu);
                MenuItem m3 = menu.findItem(R.id.filter_menu);
                m1.setVisible(true);
                m2.setVisible(false);
                m3.setVisible(true);
            }

            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch(item.getItemId()) {
                    case R.id.add_menu:
                        intent = new Intent( getContext(), FuelSupplyActivity.class );
                        intent.putExtra("vehicle_id", g.getIdVehicle());
                        startActivity( intent );
                        break;

                    case R.id.filter_menu:
                        g.setFilterVehicle(!g.getFilterVehicle());
                        if ( g.getFilterVehicle() ) {
                            this.mMenu.getItem(0).setIcon(R.drawable.ic_button_filter);
                        } else {
                            this.mMenu.getItem(0).setIcon(R.drawable.ic_button_filter_no);
                        }
                        RecyclerView listFuelSupply = requireView().findViewById(R.id.list);
                        FuelSupplyListAdapter adapter = new FuelSupplyListAdapter(Database.mFuelSupplyDao.fetchAllFuelSupplies(true), getContext());
                        listFuelSupply.setAdapter(adapter);
                        break;

                    default:
                        return false;
                }
                return true;
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
        RecyclerView listFuelSupply = this.requireView().findViewById(R.id.list);
        FuelSupplyListAdapter adapter = new FuelSupplyListAdapter(Database.mFuelSupplyDao.fetchAllFuelSupplies(true), getContext());
        listFuelSupply.setAdapter(adapter);
        listFuelSupply.setLayoutManager(new LinearLayoutManager(getContext()));
        listFuelSupply.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        adapter.notifyDataSetChanged();
        mDb.close();
    }
}
