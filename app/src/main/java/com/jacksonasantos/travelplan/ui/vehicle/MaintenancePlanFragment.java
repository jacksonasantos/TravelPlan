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

public class MaintenancePlanFragment extends Fragment  {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_generic_list, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        Database mDb = new Database(getContext());
        mDb.open();

        RecyclerView listMaintenancePlan = this.getView().findViewById(R.id.list);
        MaintenancePlanListAdapter adapter = new MaintenancePlanListAdapter(Database.mMaintenancePlanDao.fetchAllMaintenancePlan(), getContext());
        listMaintenancePlan.setAdapter(adapter);
        listMaintenancePlan.setLayoutManager(new LinearLayoutManager(getContext()));

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
        switch(item.getItemId()) {
            case R.id.addmenu:
                intent = new Intent( getContext(), MaintenancePlanActivity.class );
                startActivity( intent );
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}