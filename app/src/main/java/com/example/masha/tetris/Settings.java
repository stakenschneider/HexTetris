package com.example.masha.tetris;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Settings extends AppCompatActivity implements View.OnClickListener {

    Button bttnMenu;
    Intent intent;
    EditText eTw , eTh;
    SharedPreferences sharedPreferences;

    int height = 0 , width = 0;

    private static final String TAG = "myLogs";

    public static String MY_PREF = "MY_PREF";

    public static final String sWIDTH = "width";
    public static final String sHEIGHT = "height";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        eTw = (EditText) findViewById(R.id.width);
        eTh = (EditText) findViewById(R.id.height);

        bttnMenu = (Button) findViewById(R.id.bttnMenu);
        bttnMenu.setOnClickListener(this);

        loadText();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bttnMenu:
                intent = new Intent (this, Main.class);
                startActivity(intent);

                break;

        }
    }


    public void saveText()
    {

        sharedPreferences = getSharedPreferences(MY_PREF, MODE_PRIVATE);
        Editor ed = sharedPreferences.edit();

        ed.putInt(sHEIGHT, Integer.parseInt(eTh.getText().toString()));

        ed.putInt(sWIDTH, Integer.parseInt(eTw.getText().toString()));

        ed.commit();  //comm
    }


    void loadText() {
//        sharedPreferences = getSharedPreferences(MY_PREF, MODE_PRIVATE);

//        if (sharedPreferences.contains(APP_PREFERENCES)) {
            width = sharedPreferences.getInt(sWIDTH , 1 );
//            eTw.setText("");
//        }
        height = sharedPreferences.getInt(sHEIGHT , 1 );
    }


    @Override
    protected void onPause(){
        saveText();
        super.onPause();
        Log.d(TAG, "Settings Pause");
    }

}