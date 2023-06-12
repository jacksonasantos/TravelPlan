package com.jacksonasantos.travelplan.ui.general;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.dao.Achievement;
import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.ui.travel.MaintenanceItineraryActivity;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class AchievementActivity extends AppCompatActivity {
    private EditText etAchievement_Name;
    private EditText etAchievement_Short_Name;
    public ImageView imgAchievement_Image;      // TODO - Adjust image search and conversion (directory or internet)
    Bitmap raw;
    private byte[] imgArray = null;
    private EditText etAchievement_City;
    private EditText etAchievement_State;
    private ImageButton btLocationSource;
    private EditText etAchievement_Latlng_Source;
    private EditText etAchievement_City_End;
    private EditText etAchievement_State_End;
    private ImageButton btLocationTarget;
    private EditText etAchievement_Latlng_Target;
    private EditText etAchievement_Country;
    private ImageButton btLocation;
    private EditText etAchievement_Latlng_Achievement;
    private EditText etAchievement_Length_Achievement;
    private EditText etAchievement_Note;
    private ImageButton imgStatusAchievement;
    private Integer nrStatusAchievement = 0;
    private RecyclerView rvAchievementTravel;  // TODO - Mostrar as viagem que possuem Marker relacionado a Conquista

    private boolean opInsert = true;
    private Achievement achievement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Database mDb = new Database(getApplicationContext());
        mDb.open();

        setTitle(R.string.Achievement);
        setContentView(R.layout.activity_achievement);

        if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            achievement = new Achievement();
            if (extras.getInt( "achievement_id") > 0) {
                achievement.setId(extras.getInt("achievement_id"));
                achievement = Database.mAchievementDao.fetchAchievementById(achievement.getId());
                opInsert = false;
            }
            if (extras.getInt( "Latlng_Achievement") > 0) {
                achievement.setLatlng_achievement(extras.getString("Latlng_Achievement"));
                opInsert = true;
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
        btLocationSource = findViewById(R.id.btLocationSource);
        etAchievement_Latlng_Source = findViewById(R.id.etAchievement_Latlng_Source);
        etAchievement_City_End = findViewById(R.id.etAchievement_City_End);
        etAchievement_State_End = findViewById(R.id.etAchievement_State_End);
        btLocationTarget = findViewById(R.id.btLocationTarget);
        etAchievement_Latlng_Target = findViewById(R.id.etAchievement_Latlng_Target);
        etAchievement_Country = findViewById(R.id.etAchievement_Country);
        btLocation = findViewById(R.id.btLocation);
        etAchievement_Latlng_Achievement = findViewById(R.id.etAchievement_Latlng_Achievement);
        etAchievement_Length_Achievement = findViewById(R.id.etAchievement_Length_Achievement);
        etAchievement_Note = findViewById(R.id.etAchievement_Note);
        imgStatusAchievement = findViewById(R.id.imgStatusAchievement);
        rvAchievementTravel = findViewById(R.id.rvAchievementTravel);

        btLocation.setOnClickListener(view -> {
           Intent intent = new Intent (getBaseContext(), MaintenanceItineraryActivity.class);
            if (!etAchievement_Latlng_Achievement.getText().toString().equals("")) {
                intent.putExtra("local_search", etAchievement_Latlng_Achievement.getText().toString());
            } else {
                intent.putExtra("local_search", etAchievement_Name.getText().toString() + "," + etAchievement_Short_Name.getText().toString());
            }
            myActivityResultLauncher.launch(intent);
        });

        btLocationSource.setOnClickListener(view -> {
            Intent intent = new Intent (getBaseContext(), MaintenanceItineraryActivity.class);
            if (!etAchievement_Latlng_Source.getText().toString().equals("")) {
                intent.putExtra("local_search_source", etAchievement_Latlng_Source.getText().toString());
            } else {
                intent.putExtra("local_search_source", etAchievement_City.getText().toString() + "," + etAchievement_State.getText().toString());
            }
            myActivityResultLauncher.launch(intent);
        });

        btLocationTarget.setOnClickListener(view -> {
            Intent intent = new Intent (getBaseContext(), MaintenanceItineraryActivity.class);
            if (!etAchievement_Latlng_Target.getText().toString().equals("")) {
                intent.putExtra("local_search_target", etAchievement_Latlng_Target.getText().toString());
            } else {
                intent.putExtra("local_search_target", etAchievement_City_End.getText().toString() + "," + etAchievement_State_End.getText().toString());
            }
            myActivityResultLauncher.launch(intent);
        });

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
            etAchievement_Latlng_Source.setText(achievement.getLatlng_source());
            etAchievement_City_End.setText(achievement.getCity_end());
            etAchievement_State_End.setText(achievement.getState_end());
            etAchievement_Latlng_Target.setText(achievement.getLatlng_target());
            etAchievement_Country.setText(achievement.getCountry());
            etAchievement_Latlng_Achievement.setText(achievement.getLatlng_achievement());
            etAchievement_Length_Achievement.setText(String.valueOf(achievement.getLength_achievement()));
            etAchievement_Note.setText(achievement.getNote());
            nrStatusAchievement = achievement.getStatus_achievement();
            if (nrStatusAchievement == 1){
                imgStatusAchievement.setBackgroundColor(Color.GREEN);
            } else {
                imgStatusAchievement.setBackgroundColor(Color.LTGRAY);
            }
        }
    }

    final ActivityResultLauncher<Intent> myActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 124) {
                        Intent data = result.getData();
                        if (data != null) {
                            etAchievement_Latlng_Achievement.setText(data.getStringExtra("resulted_value"));
                            etAchievement_City.setText(data.getStringExtra("resulted_city"));
                            etAchievement_State.setText(data.getStringExtra("resulted_state"));
                            etAchievement_Country.setText(data.getStringExtra("resulted_country"));
                        }
                    }
                    if (result.getResultCode() == 125) {
                        Intent data = result.getData();
                        if (data != null) {
                            etAchievement_Latlng_Source.setText(data.getStringExtra("resulted_value"));
                            etAchievement_City.setText(data.getStringExtra("resulted_city"));
                            etAchievement_State.setText(data.getStringExtra("resulted_state"));
                            etAchievement_Country.setText(data.getStringExtra("resulted_country"));
                        }
                    }
                    if (result.getResultCode() == 126) {
                        Intent data = result.getData();
                        if (data != null) {
                            etAchievement_Latlng_Target.setText(data.getStringExtra("resulted_value"));
                            etAchievement_City_End.setText(data.getStringExtra("resulted_city"));
                            etAchievement_State_End.setText(data.getStringExtra("resulted_state"));
                            etAchievement_Country.setText(data.getStringExtra("resulted_country"));
                        }
                    }
                }
            });

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
                }

                a1.setCity(etAchievement_City.getText().toString());
                a1.setState(etAchievement_State.getText().toString());
                a1.setLatlng_source(etAchievement_Latlng_Source.getText().toString());
                a1.setCity_end(etAchievement_City_End.getText().toString());
                a1.setState_end(etAchievement_State_End.getText().toString());
                a1.setLatlng_target(etAchievement_Latlng_Target.getText().toString());
                a1.setCountry(etAchievement_Country.getText().toString());
                a1.setLatlng_achievement(etAchievement_Latlng_Achievement.getText().toString());
                a1.setLength_achievement(Double.parseDouble(etAchievement_Length_Achievement.getText().toString().isEmpty()?"0":etAchievement_Length_Achievement.getText().toString()));
                a1.setNote(etAchievement_Note.getText().toString());
                a1.setStatus_achievement(nrStatusAchievement);
                if (!opInsert) {
                     try {
                        a1.setId(achievement.getId());
                        isSave = Database.mAchievementDao.updateAchievement(a1);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), R.string.Error_Changing_Data + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        isSave =(Database.mAchievementDao.addAchievement(a1)>0);
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

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), isGranted -> {
                String tag = new ActivityResultContracts.RequestPermission().toString();
                if (isGranted) {
                    Log.e(tag, "onActivityResult: PERMISSION GRANTED");
                } else {
                    Log.e(tag, "onActivityResult: PERMISSION DENIED");
                }
            }
    );

    private boolean validateData() {
        boolean isValid = true;

        try {
            if (etAchievement_Short_Name.getText().toString().trim().isEmpty() ||
                etAchievement_Name.getText().toString().trim().isEmpty() ||
                //imgAchievement_Image.getImageMatrix().toString().trim().isEmpty() ||
                //etAchievement_City.getText().toString().trim().isEmpty() ||
                //etAchievement_State.getText().toString().trim().isEmpty() ||
                //etAchievement_Latlng_Source.getText().toString().trim().isEmpty() ||
                //etAchievement_City_End.getText().toString().trim().isEmpty() ||
                //etAchievement_State_End.getText().toString().trim().isEmpty() ||
                //etAchievement_Latlng_Target.getText().toString().trim().isEmpty() ||
                etAchievement_Country.getText().toString().trim().isEmpty() ||
                etAchievement_Latlng_Achievement.getText().toString().trim().isEmpty() //||
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