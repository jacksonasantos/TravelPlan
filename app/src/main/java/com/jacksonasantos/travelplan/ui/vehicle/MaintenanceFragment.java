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

public class MaintenanceFragment extends Fragment  {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        requireActivity().addMenuProvider(new MenuProvider() {
            private Menu mMenu;

            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
                menuInflater.inflate(R.menu.main, menu);

                this.mMenu = menu;
                MenuItem m1 = menu.findItem(R.id.addmenu);
                MenuItem m2 = menu.findItem(R.id.savemenu);
                MenuItem m3 = menu.findItem(R.id.filtermenu);
                m1.setVisible(true);
                m2.setVisible(false);
                m3.setVisible(true);
            }

            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch(item.getItemId()) {
                    case R.id.addmenu:
                        intent = new Intent( getContext(), MaintenanceActivity.class );
                        startActivity( intent );
                        break;

                    case R.id.filtermenu:
                        Globals.getInstance().setFilterVehicle(!Globals.getInstance().getFilterVehicle());
                        if ( Globals.getInstance().getFilterVehicle() ) {
                            this.mMenu.getItem(0).setIcon(R.drawable.ic_button_filter);
                        } else {
                            this.mMenu.getItem(0).setIcon(R.drawable.ic_button_filter_no);
                        }

                        RecyclerView listMaintenance = requireView().findViewById(R.id.list);
                        MaintenanceListAdapter adapter = new MaintenanceListAdapter(Database.mMaintenanceDao.fetchAllMaintenance(), getContext());
                        listMaintenance.setAdapter(adapter);
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

        RecyclerView listMaintenance = this.requireView().findViewById(R.id.list);
        MaintenanceListAdapter adapter = new MaintenanceListAdapter(Database.mMaintenanceDao.fetchAllMaintenance(), getContext());
        listMaintenance.setAdapter(adapter);
        listMaintenance.setLayoutManager(new LinearLayoutManager(getContext()));
        listMaintenance.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        adapter.notifyDataSetChanged();
        mDb.close();
    }
}
