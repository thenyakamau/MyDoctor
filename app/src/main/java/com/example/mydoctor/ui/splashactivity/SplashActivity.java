package com.example.mydoctor.ui.splashactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.mydoctor.R;
import com.example.mydoctor.ui.loginacitivity.LoginActivity;

public class SplashActivity extends AppCompatActivity {


    private  int SPLASH_RUNTIME = 7000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //This is a very less efficient method of moving from a splash activity
       // new Handler().postDelayed(() -> startActivity(new Intent(SplashActivity.this, LoginActivity.class)), SPLASH_RUNTIME);

        //using threads is reliable and more robust especially when using animation
        Thread splash= new Thread(){
            public void run(){
                try{
                    sleep(SPLASH_RUNTIME);
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    finish();
                }
            }

        };
        splash.start();

    }
}
