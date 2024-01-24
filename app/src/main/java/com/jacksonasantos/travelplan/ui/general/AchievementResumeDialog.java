package com.jacksonasantos.travelplan.ui.general;


import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.general.Database;

public class AchievementResumeDialog extends AppCompatActivity {
    private RecyclerView rvList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Database mDb = new Database(getApplicationContext());
        mDb.open();

        setTitle(R.string.Achievement);
        setContentView(R.layout.dialog_achievement_resume);

        addListenerOnButtonDone();
        rvList = findViewById(R.id.rvList);

        AchievementResumeListAdapter adapterAchievementResume = new AchievementResumeListAdapter(Database.mAchievementResumeDao.fetchAchievementResume(), getApplicationContext(), 1);
        rvList.setAdapter(adapterAchievementResume);
        rvList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    public void addListenerOnButtonDone() {
        Button btnDone = findViewById(R.id.btnDone);
        btnDone.setOnClickListener(v -> finish());
    }
}