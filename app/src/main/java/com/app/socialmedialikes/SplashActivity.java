package com.app.socialmedialikes;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SaveSharedPreference.setFirstLaunch(this, "firstLaunch");

        if (SaveSharedPreference.getUserName(SplashActivity.this).length() == 0) {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        } else {
//            Intent i = new Intent(this, MainActivity.class);
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }


    }
}
