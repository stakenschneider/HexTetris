package com.example.masha.tetris;

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
    public String width = "" , height = "";
    private static final String TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        eTw = (EditText) findViewById(R.id.width);
        eTh = (EditText) findViewById(R.id.height);

        bttnMenu = (Button) findViewById(R.id.bttnMenu);
        bttnMenu.setOnClickListener(this);

        //оптимальные (ИМХО) размеры сетки
        eTw.setText("8");
        eTh.setText("15");
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bttnMenu:
                intent = new Intent (this, Main.class);
                startActivity(intent);
                break;

        }
    }

    public void saveText()   //значения высоты и ширины сетки  сохраняются
    {
        sharedPreferences = getPreferences(MODE_PRIVATE);
        Editor ed = sharedPreferences.edit();

        height = eTh.getText().toString();
        ed.putString(height, eTh.getText().toString());
        ed.commit();

        width = eTw.getText().toString();
        ed.putString(width, eTw.getText().toString());

        ed.commit();
    }


//    void loadText() {  //если хотим постоянно свои значения
//        sharedPreferences = getPreferences( MODE_PRIVATE);
//        String h = sharedPreferences.getString(height, "");
//        eTh.setText(h);
//
//        String w = sharedPreferences.getString(width, "");
//        eTw.setText(w);
//    }


    @Override
    protected void onPause(){
        saveText();  //значения сохраняются при выходе из активити с настройками или через кнопку меню или назад
        super.onPause();
        Log.d(TAG , "Settings Pause");
    }



}