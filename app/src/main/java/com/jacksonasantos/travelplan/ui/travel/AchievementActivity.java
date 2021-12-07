package com.jacksonasantos.travelplan.ui.travel;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Achievement;
import com.jacksonasantos.travelplan.dao.general.Database;

public class AchievementActivity extends AppCompatActivity {
    private EditText etAchievement_Short_Name;
    private EditText etAchievement_Name;
    private EditText etAchievement_Image;
    private EditText etAchievement_City;
    private EditText etAchievement_State;
    private EditText etAchievement_Country;
    private EditText etAchievement_Latlng_Achievement;
    private EditText etAchievement_Note;

    private boolean opInsert = true;
    private Achievement achievement;

    @SuppressLint("WrongViewCast")
    @RequiresApi(api = Build.VERSION_CODES.N)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Database mDb = new Database(getApplicationContext());
        mDb.open();

        setTitle(R.string.Achievement);
        setContentView(R.layout.activity_achievement);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            achievement = new Achievement();
            if (extras.getInt( "achievement_id") > 0) {
                achievement.setId(extras.getInt("achievement_id"));
                achievement = Database.mAchievementDao.fetchAchievementById(achievement.getId());
                opInsert = false;
            }
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        addListenerOnButtonSave();

        etAchievement_Short_Name = findViewById(R.id.etAchievement_Short_Name);
        etAchievement_Name = findViewById(R.id.etAchievement_Name);
        etAchievement_Image = findViewById(R.id.etAchievement_Image);
        etAchievement_City = findViewById(R.id.etAchievement_City);
        etAchievement_State = findViewById(R.id.etAchievement_State);
        etAchievement_Country = findViewById(R.id.etAchievement_Country);
        etAchievement_Latlng_Achievement = findViewById(R.id.etAchievement_Latlng_Achievement);
        etAchievement_Note = findViewById(R.id.etAchievement_Note);

        if (achievement != null) {
            etAchievement_Short_Name.setText(achievement.getShort_name());
            etAchievement_Name.setText(achievement.getName());
            etAchievement_Image.setText(achievement.getImage());
            etAchievement_City.setText(achievement.getCity());
            etAchievement_State.setText(achievement.getState());
            etAchievement_Country.setText(achievement.getCountry());
            etAchievement_Latlng_Achievement.setText(achievement.getLatlng_achievement());
            etAchievement_Note.setText(achievement.getNote());
        }
    }

    @SuppressLint("NewApi")
    public void addListenerOnButtonSave() {
        Button btSaveAchievement = findViewById(R.id.btSaveAchievement);

        btSaveAchievement.setOnClickListener(v -> {
            boolean isSave = false;

            if (!validateData()) {
                Toast.makeText(getApplicationContext(), R.string.Error_Data_Validation, Toast.LENGTH_LONG).show();
            } else {
                final Achievement a1 = new Achievement();

                a1.setShort_name(etAchievement_Short_Name.getText().toString());
                a1.setName(etAchievement_Name.getText().toString());
                a1.setImage(etAchievement_Image.getText().toString());
                a1.setCity(etAchievement_City.getText().toString());
                a1.setState(etAchievement_State.getText().toString());
                a1.setCountry(etAchievement_Country.getText().toString());
                a1.setLatlng_achievement(etAchievement_Latlng_Achievement.getText().toString());
                a1.setNote(etAchievement_Note.getText().toString());

                if (!opInsert) {
                    try {
                        a1.setId(achievement.getId());
                        isSave = Database.mAchievementDao.updateAchievement(a1);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), R.string.Error_Changing_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        isSave = Database.mAchievementDao.addAchievement(a1);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), R.string.Error_Including_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                setResult(isSave ? 1 : 0);
                if (isSave) {
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.Error_Saving_Data, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private boolean validateData() {
        boolean isValid = true;

        try {
            if (etAchievement_Short_Name.getText().toString().trim().isEmpty() ||
                etAchievement_Name.getText().toString().trim().isEmpty() ||
                etAchievement_Image.getText().toString().trim().isEmpty() ||
                etAchievement_City.getText().toString().trim().isEmpty() ||
                etAchievement_State.getText().toString().trim().isEmpty() ||
                etAchievement_Country.getText().toString().trim().isEmpty() //||
                //etAchievement_Latlng_Achievement.getText().toString().trim().isEmpty() ||
                //etAchievement_Note.getText().toString().trim().isEmpty()
            ){
                isValid = false;
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.Data_Validator_Error +" - " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return isValid;
    }
}