package com.example.masha.tetris;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import service.MyService;

import static com.example.masha.tetris.Main.scrw;
import static com.example.masha.tetris.Main.scrh;


public class Settings extends AppCompatActivity implements View.OnClickListener {

    Boolean flag = false;
    CheckBox checkBox;
    SharedPreferences sharedPreferences;

    public EditText eTw , eTh;
    public static int height = 0 , width = 0 ;

    public static String MY_PREF = "MY_PREF";
    public static final String sWIDTH = "width" ,
            sHEIGHT = "height" ,
            FLAG = "flag";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        eTw = (EditText) findViewById(R.id.width);
        eTh = (EditText) findViewById(R.id.height);

        findViewById(R.id.bttnSentence).setOnClickListener(this);

        checkBox = (CheckBox) findViewById(R.id.radioButton);
        checkBox.setOnClickListener(this);

        if (flag) {
//            checkBox.isChecked();
            checkBox.setText(getResources().getString(R.string.muson));
        }

    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bttnSentence:
                eTh.setText("" +(int)((2*(scrh-30)-2*scrw/(Math.sqrt(3)*(2*Integer.parseInt(eTw.getText().toString())+1)))/(3*2*scrw/(Math.sqrt(3)*(2*Integer.parseInt(eTw.getText().toString())+1)))));
                break;
        }

        //ЧТО БЫ НЕ ЗАБЫТЬ!!!!!!!!!

        // ТУТ ИЛИ ГДЕ ТО В ЭТОМ КЛАССЕ НЕ ЗАБЫТЬ ПОСТАВИТЬ ШЕРЕДПРЕФЕРЕНСИС (НАВЕРНО, ЛУЧШЕ БЫ СДЕЛАТЬ ЧТО ТО БОЛЕЕ УДОБНОЕ
        // ДЛЯ ТОГО ЧТО БЫ РАБОТА С ГОВЯНЫМ ЧЕКБОКСОМ БЫЛА НОРМАЛЬНА


        if (checkBox.isChecked()) {
            flag = true;
            stopService(new Intent(this, MyService.class));
            checkBox.setText(getResources().getString(R.string.muson));
        }

        if (!checkBox.isChecked())
        {
            flag = false;
            startService(new Intent(this, MyService.class));
            checkBox.setText(getResources().getString(R.string.musoff));
        }

    }


    public void saveText()
    {
        if (!eTh.getText().toString().equals("") && !eTw.getText().toString().equals("")) {
            sharedPreferences = getSharedPreferences(MY_PREF, MODE_PRIVATE);
            Editor ed = sharedPreferences.edit();

            ed.putBoolean(FLAG , flag);
            ed.putInt(sHEIGHT, Integer.parseInt(eTh.getText().toString()));
            ed.putInt(sWIDTH, Integer.parseInt(eTw.getText().toString()));

            ed.commit();
        }
    }


    void loadText() {
        sharedPreferences = getSharedPreferences(MY_PREF, MODE_PRIVATE);

        flag = sharedPreferences.getBoolean(FLAG , true);
        width = sharedPreferences.getInt(sWIDTH , 8 );
        height = sharedPreferences.getInt(sHEIGHT, 16);
    }


    @Override
    protected void onPause(){
        saveText();
        loadText();
        super.onPause();
    }
}