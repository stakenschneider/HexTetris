package com.example.masha.tetris;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import service.MyService;


public class Main extends AppCompatActivity implements View.OnClickListener {

    Button bttnPlay , bttnSettings , bttnTutorial , bttnTwitter, bttnFB, bttnGoogle, bttnExit;
    Intent intent;
    Toast toast;
    private static final String MY_SETTINGS = "my_settings";

    public static double scrh = 0 , scrw = 0 ;

    private static final String TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startService(new Intent(this, MyService.class));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bttnPlay = (Button) findViewById(R.id.bttnPlay);
        bttnPlay.setOnClickListener(this);

        bttnTutorial = (Button) findViewById(R.id.bttnTutorial);
        bttnTutorial.setOnClickListener(this);

        bttnSettings = (Button) findViewById(R.id.bttnSettings);
        bttnSettings.setOnClickListener(this);

        bttnFB = (Button) findViewById(R.id.bttnFacebook);
        bttnFB.setOnClickListener(this);

        bttnGoogle = (Button) findViewById(R.id.bttnGoogle);
        bttnGoogle.setOnClickListener(this);

        bttnTwitter = (Button) findViewById(R.id.bttnTwitter);
        bttnTwitter.setOnClickListener(this);

        bttnExit = (Button) findViewById(R.id.bttnExit);
        bttnExit.setOnClickListener(this);


        scrh = screenSizeH();
        scrw = screenSizeW();



        SharedPreferences sp = getSharedPreferences(MY_SETTINGS, Context.MODE_PRIVATE);
        // проверяем, первый ли раз открывается программа
        boolean hasVisited = sp.getBoolean("hasVisited", false);

        if (!hasVisited) {
            // при первом открытии инфа
            SharedPreferences.Editor e = sp.edit();
            e.putBoolean("hasVisited", true);
            e.commit(); // не забудьте подтвердить изменения
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bttnPlay:
                intent = new Intent (this, GamePlay.class);
                startActivity(intent);
                Log.d(TAG, "нажали gameplay");
                break;

            case R.id.bttnTutorial:
                intent = new Intent (this, GamePlay.class);
                startActivity(intent);
                Log.d(TAG, "нажали tutorial");
                break;

            case R.id.bttnSettings:
                intent = new Intent (this, Settings.class);
                startActivity(intent);
                Log.d(TAG, "нажали settings");
                break;

            case R.id.bttnFacebook:
                 toast = Toast.makeText(getApplicationContext(), "FaceBook", Toast.LENGTH_SHORT);
                toast.show();
                Log.d(TAG, "нажали imgFB");
                break;

            case R.id.bttnGoogle:
                toast = Toast.makeText(getApplicationContext(), "Google+", Toast.LENGTH_SHORT);
                toast.show();
                Log.d(TAG, "нажали imgGplus");
                break;

            case R.id.bttnTwitter:
                toast = Toast.makeText(getApplicationContext(), "Twitter", Toast.LENGTH_SHORT);
                toast.show();
                Log.d(TAG, "нажали imgTweet");
                break;

            case R.id.bttnExit:
                finish();
                Log.d(TAG, "нажали Exit");
                break;

        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d(TAG , "Start");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d(TAG , "Resume");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d(TAG , "Pause");
    }

    @Override
    protected void onStop(){
        super.onStop();

        Log.d(TAG , "Stop");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        stopService(new Intent(this, MyService.class));
        Log.d(TAG, "Destroy");
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