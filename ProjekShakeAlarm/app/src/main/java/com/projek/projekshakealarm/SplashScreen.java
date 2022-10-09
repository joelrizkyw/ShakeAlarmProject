package com.projek.projekshakealarm;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    private int waktu_loading=3000;

    //4000=4 detik

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                Intent MainActivity=new Intent(SplashScreen.this, MainActivity.class);
                startActivity(MainActivity);
                finish();

            }
        },waktu_loading);
    }
}
