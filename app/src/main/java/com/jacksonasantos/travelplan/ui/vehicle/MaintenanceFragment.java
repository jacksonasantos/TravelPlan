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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Database;
import com.jacksonasantos.travelplan.ui.utility.Globals;

import java.text.ParseException;

import static com.jacksonasantos.travelplan.ui.vehicle.MaintenanceListAdapter.*;

public class MaintenanceFragment extends Fragment  {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_maintenance, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        Database mDb = new Database(getContext());
        mDb.open();

        RecyclerView listMaintenance = (RecyclerView) this.getView().findViewById(R.id.listMaintenance);
        MaintenanceListAdapter adapter = new MaintenanceListAdapter(Database.mMaintenanceDao.fetchAllMaintenance(), getContext());
        listMaintenance.setAdapter(adapter);
        listMaintenance.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter.notifyDataSetChanged();
        mDb.close();
    }

    private Menu mMenu;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        this.mMenu = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem m = menu.findItem(R.id.addmenu);
        m.setVisible(true);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;
        switch(item.getItemId()) {
            case R.id.addmenu:
                intent = new Intent( getContext(), MaintenanceActivity.class );
                startActivity( intent );

            case R.id.filtermenu:
                Globals.getInstance().setFilterVehicle(!Globals.getInstance().getFilterVehicle());
                if ( Globals.getInstance().getFilterVehicle() ) {
                    this.mMenu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_menu_filter));
                } else {
                    this.mMenu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_menu_filter_no));
                }

                RecyclerView listMaintenance = (RecyclerView) this.getView().findViewById(R.id.listMaintenance);
                MaintenanceListAdapter adapter = new MaintenanceListAdapter(Database.mMaintenanceDao.fetchAllMaintenance(), getContext());
                listMaintenance.setAdapter(adapter);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}