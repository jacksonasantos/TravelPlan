package com.jacksonasantos.travelplan.ui.general;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.travel.ItineraryActivity;

public class AchievementFragment extends Fragment {

    private String mViewMode;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        int mLayout;
        mViewMode = PreferenceManager.getDefaultSharedPreferences(requireContext()).getString("achievement_view_mode", String.valueOf(0));

        if (mViewMode.equals("list")) {
            mLayout = R.layout.fragment_generic_list;
        } else {
            mLayout = R.layout.activity_itinerary;
        }

        requireActivity().addMenuProvider(new MenuProvider() {

            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
                menuInflater.inflate(R.menu.main, menu);

                MenuItem m1 = menu.findItem(R.id.add_menu);
                MenuItem m2 = menu.findItem(R.id.save_menu);
                MenuItem m3 = menu.findItem(R.id.filter_menu);
                m1.setVisible(true);
                m2.setVisible(false);
                m3.setVisible(false);
            }

            @SuppressLint({"UseCompatLoadingForDrawables", "NonConstantResourceId"})
            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem item) {
                Intent intent;

                if (item.getItemId() == R.id.add_menu) {
                    intent = new Intent(getContext(), AchievementActivity.class);
                    startActivity(intent);
                } else {
                    return false;
                }
                return true;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        return inflater.inflate(mLayout, container, false);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResume() {
        super.onResume();
        Database mDb = new Database(getContext());
        mDb.open();

        if (mViewMode.equals("list")) {
            RecyclerView listAchievement = this.requireView().findViewById(R.id.list);
            AchievementListAdapter adapter = new AchievementListAdapter(Database.mAchievementDao.fetchAllAchievement(), getContext());
            listAchievement.setAdapter(adapter);
            listAchievement.setLayoutManager(new LinearLayoutManager(getContext()));
            listAchievement.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
            adapter.notifyDataSetChanged();
        } else if (mViewMode.equals("map")) {
            Intent intent = new Intent(getContext(), ItineraryActivity.class);
            intent.putExtra("flg_achievement", true);
            myActivityResultLauncher.launch(intent);
        //} else {
            // TODO - Verified Back Stack for return to Home
        //    return;
        }
    }

    final ActivityResultLauncher<Intent> myActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 120) {
                        Intent data = result.getData();
                        if (data != null) {
                            mViewMode = "return";
                        }
                    }
                }
            }
    );
}