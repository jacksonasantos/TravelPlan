package com.jacksonasantos.travelplan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        int SPLASH_TIME_OUT = 1000;
        new Handler().postDelayed(() -> {
            Intent i = new Intent(SplashActivity.this,MainActivity.class);
            startActivity(i);
            finish();
        }, SPLASH_TIME_OUT);
    }
}
