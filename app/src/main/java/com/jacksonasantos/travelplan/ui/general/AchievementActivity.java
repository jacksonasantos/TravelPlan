package com.jacksonasantos.travelplan.ui.general;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Achievement;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class AchievementActivity extends AppCompatActivity {
    private EditText etAchievement_Name;
    private EditText etAchievement_Short_Name;
    public ImageView imgAchievement_Image;
    Bitmap raw;
    private byte[] imgArray = null;
    private EditText etAchievement_City;
    private EditText etAchievement_State;
    private EditText etAchievement_City_End;
    private EditText etAchievement_State_End;
    private EditText etAchievement_Country;
    private EditText etAchievement_Latlng_Achievement;
    private EditText etAchievement_Length_Achievement;
    private EditText etAchievement_Note;
    private TextView txTravelAchievement;
    private ImageButton imgStatusAchievement;
    private Integer nrStatusAchievement = 0;

    private boolean opInsert = true;
    private Achievement achievement;

    @SuppressLint("WrongViewCast")

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
        addListenerOnButtonStatusAchievement();
        addListenerOnImageAchievement();
        addListenerOnDeleteImageAchievement();
        addListenerOnEditImageAchievement();

        etAchievement_Name = findViewById(R.id.etAchievement_Name);
        etAchievement_Short_Name = findViewById(R.id.etAchievement_Short_Name);
        imgAchievement_Image = findViewById(R.id.imgAchievement_Image);
        etAchievement_City = findViewById(R.id.etAchievement_City);
        etAchievement_State = findViewById(R.id.etAchievement_State);
        etAchievement_City_End = findViewById(R.id.etAchievement_City_End);
        etAchievement_State_End = findViewById(R.id.etAchievement_State_End);
        etAchievement_Country = findViewById(R.id.etAchievement_Country);
        etAchievement_Latlng_Achievement = findViewById(R.id.etAchievement_Latlng_Achievement);
        etAchievement_Length_Achievement = findViewById(R.id.etAchievement_Length_Achievement);
        etAchievement_Note = findViewById(R.id.etAchievement_Note);
        txTravelAchievement = findViewById(R.id.txTravelAchievement);
        imgStatusAchievement = findViewById(R.id.imgStatusAchievement);

        if (achievement != null) {
            etAchievement_Name.setText(achievement.getName());
            etAchievement_Short_Name.setText(achievement.getShort_name());
            imgArray = achievement.getImage();
            if(imgArray!=null){
                raw = BitmapFactory.decodeByteArray(imgArray,0, imgArray.length);
                imgAchievement_Image.setImageBitmap(raw);
            }
            etAchievement_City.setText(achievement.getCity());
            etAchievement_State.setText(achievement.getState());
            etAchievement_City_End.setText(achievement.getCity_end());
            etAchievement_State_End.setText(achievement.getState_end());
            etAchievement_Country.setText(achievement.getCountry());
            etAchievement_Latlng_Achievement.setText(achievement.getLatlng_achievement());
            etAchievement_Length_Achievement.setText(String.valueOf(achievement.getLength_achievement()));
            etAchievement_Note.setText(achievement.getNote());
            txTravelAchievement.setText(Database.mTravelDao.fetchTravelById(achievement.getTravel_id()).getDescription());
            nrStatusAchievement = achievement.getStatus_achievement();
            if (nrStatusAchievement == 1){
                imgStatusAchievement.setBackgroundColor(Color.GREEN);
            } else {
                imgStatusAchievement.setBackgroundColor(Color.LTGRAY);
            }
        }
    }

    private void button() {
        AtomicInteger imgPos = new AtomicInteger();
        ArrayList<File> list = Utils.imageReader(Objects.requireNonNull(getExternalFilesDir("/achievements")));
        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_my_files, null);
        GridView gV = v.findViewById(R.id.gridView1);
        gV.setAdapter(new MyGalleryImageAdapter(this, list));
        gV.setOnItemClickListener((parent, view1, position, id) -> imgPos.set(position) );
        final AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        builder2.setView(v)
                .setPositiveButton(R.string.OK, (dialog, which) -> {
                    Bitmap myBitmap = BitmapFactory.decodeFile(list.get(imgPos.get()).getAbsolutePath());
                    imgAchievement_Image.setImageBitmap(myBitmap);
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.Cancel, (dialog, which) -> dialog.cancel());
        builder2.setCancelable(false);
        builder2.create().show();
    }

    private void addListenerOnImageAchievement() {
        imgAchievement_Image = findViewById(R.id.imgAchievement_Image);
        imgAchievement_Image.setOnClickListener( view -> button());
    }

    private void addListenerOnEditImageAchievement() {
        ImageView imgEdit_Image = findViewById(R.id.imgEdit_Image);
        imgEdit_Image.setOnClickListener(view -> button());
    }

    private void addListenerOnDeleteImageAchievement() {
        ImageView imgDelete_Image = findViewById(R.id.imgDelete_Image);
        imgAchievement_Image = findViewById(R.id.imgAchievement_Image);
        imgDelete_Image.setOnClickListener(view -> imgAchievement_Image.setImageResource(R.drawable.ic_menu_achievement));
    }

    private void addListenerOnButtonStatusAchievement() {
        imgStatusAchievement = findViewById(R.id.imgStatusAchievement);
        imgStatusAchievement.setOnClickListener( view -> {
            if (nrStatusAchievement == 0) {
                nrStatusAchievement = 1;
                imgStatusAchievement.setBackgroundColor(Color.GREEN);
            } else {
                nrStatusAchievement = 0;
                imgStatusAchievement.setBackgroundColor(Color.LTGRAY);
            }
        });
    }

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

                imgAchievement_Image.buildDrawingCache();
                Bitmap bitmap = imgAchievement_Image.getDrawingCache();
                if (bitmap!=null) {
                    ByteArrayOutputStream saida = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, saida);
                    byte[] img = saida.toByteArray();
                    a1.setImage(img);
                } else

                a1.setCity(etAchievement_City.getText().toString());
                a1.setState(etAchievement_State.getText().toString());
                a1.setCity_end(etAchievement_City_End.getText().toString());
                a1.setState_end(etAchievement_State_End.getText().toString());
                a1.setCountry(etAchievement_Country.getText().toString());
                a1.setLatlng_achievement(etAchievement_Latlng_Achievement.getText().toString());
                a1.setLength_achievement(Double.parseDouble(etAchievement_Length_Achievement.getText().toString()));
                a1.setNote(etAchievement_Note.getText().toString());
                a1.setStatus_achievement(nrStatusAchievement);
                if (txTravelAchievement.getText().toString().equals("")) {
                    a1.setTravel_id(null);
                } else {
                    a1.setTravel_id(achievement.getTravel_id());
                }
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
                //imgAchievement_Image.getImageMatrix().toString().trim().isEmpty() ||
                    //etAchievement_City.getText().toString().trim().isEmpty() ||
                    //etAchievement_State.getText().toString().trim().isEmpty() ||
                    //etAchievement_City_End.getText().toString().trim().isEmpty() ||
                    //etAchievement_State_End.getText().toString().trim().isEmpty() ||
                etAchievement_Country.getText().toString().trim().isEmpty() //||
                //etAchievement_Latlng_Achievement.getText().toString().trim().isEmpty() ||
                //etAchievement_Length.getText().toString().trim().isEmpty() ||
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