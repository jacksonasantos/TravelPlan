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
import com.jacksonasantos.travelplan.ui.travel.MaintenanceItineraryActivity;

public class AchievementFragment extends Fragment {

    private String mViewMode;

    private RecyclerView listAchievement;
    private AchievementListAdapter adapter;

    Integer v_filter_type = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        int mLayout;
        mViewMode = PreferenceManager.getDefaultSharedPreferences(requireContext()).getString("achievement_view_mode", String.valueOf(0));

        if (mViewMode.equals("list")) {
            mLayout = R.layout.fragment_generic_list;

            requireActivity().addMenuProvider(new MenuProvider() {

                @Override
                public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                    menu.clear();
                    menuInflater.inflate(R.menu.main, menu);

                    MenuItem m1 = menu.findItem(R.id.add_menu);
                    MenuItem m2 = menu.findItem(R.id.save_menu);
                    MenuItem m3 = menu.findItem(R.id.filter_menu);
                    MenuItem m4 = menu.findItem(R.id.filter_type_menu);
                    m1.setVisible(true);
                    m2.setVisible(true).setIcon(R.drawable.ic_resume);
                    m3.setVisible(false);
                    m4.setVisible(true);
                }

                @SuppressLint({"UseCompatLoadingForDrawables", "NonConstantResourceId"})
                @Override
                public boolean onMenuItemSelected(@NonNull MenuItem item) {
                    Intent intent;

                    switch(item.getItemId()) {
                        case R.id.add_menu:
                            intent = new Intent( getContext(), AchievementActivity.class );
                            startActivity( intent );
                            break;

                        case R.id.save_menu:
                            intent = new Intent( getContext(), AchievementResumeDialog.class );
                            startActivity( intent );
                            break;

                        case R.id.filter_allType_menu:
                            v_filter_type = null;
                            adapter = new AchievementListAdapter(Database.mAchievementDao.fetchAllAchievementByTypeAchievement(null), getContext());
                            listAchievement.setAdapter(adapter);
                            break;

                        case R.id.filter_noType_menu:
                            v_filter_type = 0;
                            adapter = new AchievementListAdapter(Database.mAchievementDao.fetchAllAchievementByTypeAchievement(0), getContext());
                            listAchievement.setAdapter(adapter);
                            break;

                        case R.id.filter_mountainRange_menu:
                            v_filter_type = 1;
                            adapter = new AchievementListAdapter(Database.mAchievementDao.fetchAllAchievementByTypeAchievement(1), getContext());
                            listAchievement.setAdapter(adapter);
                            break;

                        case R.id.filter_road_menu:
                            v_filter_type = 2;
                            adapter = new AchievementListAdapter(Database.mAchievementDao.fetchAllAchievementByTypeAchievement(2), getContext());
                            listAchievement.setAdapter(adapter);
                            break;

                        case R.id.filter_cave_menu:
                            v_filter_type = 3;
                            adapter = new AchievementListAdapter(Database.mAchievementDao.fetchAllAchievementByTypeAchievement(3), getContext());
                            listAchievement.setAdapter(adapter);
                            break;

                        case R.id.filter_touristSpot_menu:
                            v_filter_type = 4;
                            adapter = new AchievementListAdapter(Database.mAchievementDao.fetchAllAchievementByTypeAchievement(4), getContext());
                            listAchievement.setAdapter(adapter);
                            break;

                        default:
                            return false;
                    }
                    return true;
                }


            }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        } else {
            mLayout = R.layout.activity_itinerary;
        }

        return inflater.inflate(mLayout, container, false);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResume() {
        super.onResume();

        Database mDb = new Database(getContext());
        mDb.open();

        switch (mViewMode) {
            case "list":
                listAchievement = this.requireView().findViewById(R.id.list);
                adapter = new AchievementListAdapter(Database.mAchievementDao.fetchAllAchievementByTypeAchievement(v_filter_type), getContext());
                listAchievement.setAdapter(adapter);
                listAchievement.setLayoutManager(new LinearLayoutManager(getContext()));
                listAchievement.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
                adapter.notifyDataSetChanged();
                break;
            case "map":
                Intent intent = new Intent(getContext(), MaintenanceItineraryActivity.class);
                intent.putExtra("flg_achievement", true);
                myActivityResultLauncher.launch(intent);
                break;
            case "return":
                getParentFragmentManager().popBackStack();
                break;
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