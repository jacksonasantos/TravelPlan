package com.jacksonasantos.travelplan.ui.general;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.general.Database;

public class AchievementResumeDialog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Database mDb = new Database(getApplicationContext());
        mDb.open();

        setTitle(R.string.Achievement);
        setContentView(R.layout.dialog_achievement_resume);

        addListenerOnButtonDone();
        RecyclerView rvListSummary = findViewById(R.id.rvListSummary);
        RecyclerView rvList = findViewById(R.id.rvList);

        AchievementResumeSummaryListAdapter adapterAchievementResumeSummary = new AchievementResumeSummaryListAdapter(Database.mAchievementResumeDao.fetchAchievementResumeSummary(), getApplicationContext());
        rvListSummary.setAdapter(adapterAchievementResumeSummary);
        GridLayoutManager mGridLayoutManagerII = new GridLayoutManager(this, 5);
        rvListSummary.setLayoutManager(mGridLayoutManagerII);

        AchievementResumeListAdapter adapterAchievementResume = new AchievementResumeListAdapter(Database.mAchievementResumeDao.fetchAchievementResume(), getApplicationContext());
        rvList.setAdapter(adapterAchievementResume);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, 3);
        rvList.setLayoutManager(mGridLayoutManager);
    }

    public void addListenerOnButtonDone() {
        Button btnDone = findViewById(R.id.btnDone);
        btnDone.setOnClickListener(v -> finish());
    }
}