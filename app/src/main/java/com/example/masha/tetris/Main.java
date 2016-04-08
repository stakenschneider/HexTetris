package com.example.masha.tetris;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

import service.MyService;


public class Main extends AppCompatActivity implements View.OnClickListener {

    private Intent intent;

    public static double scrh = 0 , scrw = 0 ;
    private static final String MY_SETTINGS = "my_settings";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startService(new Intent(this, MyService.class));
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        findViewById(R.id.bttnPlay).setOnClickListener(this);
        findViewById(R.id.bttnTutorial).setOnClickListener(this);
        findViewById(R.id.bttnSettings).setOnClickListener(this);
        findViewById(R.id.bttnTwitter).setOnClickListener(this);
        findViewById(R.id.bttnExit).setOnClickListener(this);

        scrh = screenSizeH();
        scrw = screenSizeW();

        SharedPreferences sp = getSharedPreferences(MY_SETTINGS, Context.MODE_PRIVATE);
        boolean hasVisited = sp.getBoolean("hasVisited", false);
        if (!hasVisited) {
            intent = new Intent (this, Start.class);
            startActivity(intent);

            SharedPreferences.Editor e = sp.edit();
            e.putBoolean("hasVisited", true);
            e.commit();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bttnPlay:
                intent = new Intent (this, GamePlay.class);
                startActivity(intent);
                break;

            case R.id.bttnTutorial:
                Toast.makeText(this, "Может когда-нибудь потом", Toast.LENGTH_SHORT).show();
                break;

            case R.id.bttnSettings:
                intent = new Intent (this, Settings.class);
                startActivity(intent);
                break;

            case R.id.bttnExit:
                finish();
                break;

            case R.id.bttnTwitter:
                intent = new Intent(this , Social.class);
                startActivity(intent);
                break;

        }
    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
        stopService(new Intent(this, MyService.class));
    }


    public double screenSizeW()
    {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metricsB = new DisplayMetrics();
        display.getMetrics(metricsB);

        return  metricsB.widthPixels;
    }


    public double screenSizeH()
    {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metricsB = new DisplayMetrics();
        display.getMetrics(metricsB);

        return metricsB.heightPixels;
    }

}