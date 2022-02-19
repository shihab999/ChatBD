package com.mss.chatbd.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.mss.chatbd.R;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Thread td = new Thread(){
            public void run(){
                try {
                    sleep(2000);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    startActivity(new Intent(Splash.this,LogIn.class));
                    finish();
                }
            }
        };td.start();
    }
}