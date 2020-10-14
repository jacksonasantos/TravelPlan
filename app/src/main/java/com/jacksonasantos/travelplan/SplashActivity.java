package com.jacksonasantos.travelplan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.jacksonasantos.travelplan.dao.Database;

public class SplashActivity extends Activity {
    private static int SPLASH_TIME_OUT = 3000;
    public Database mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mDb = new Database( this);
        mDb.open();
        mDb.close();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
