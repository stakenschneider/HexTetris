package com.example.masha.tetris;

//TODO: настройка скорости игры

import android.app.AlertDialog;
import android.content.DialogInterface;
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

    CheckBox checkBox;
    SharedPreferences sharedPreferences;
    AlertDialog.Builder ad;
    public static String strpack = "{\"height\":15,\"width\":8,\"sourceSeeds\":[0],\"units\":[{\"members\":[{\"x\":0,\"y\":0}],\"pivot\":{\"x\":0,\"y\":0}},{\"members\":[{\"x\":0,\"y\":0},{\"x\":2,\"y\":0}],\"pivot\":{\"x\":1,\"y\":0}},{\"members\":[{\"x\":0,\"y\":0},{\"x\":0,\"y\":2}],\"pivot\":{\"x\":0,\"y\":1}},{\"members\":[{\"x\":2,\"y\":0},{\"x\":0,\"y\":1},{\"x\":2,\"y\":2}],\"pivot\":{\"x\":1,\"y\":1}},{\"members\":[{\"x\":0,\"y\":0},{\"x\":1,\"y\":1},{\"x\":0,\"y\":2}],\"pivot\":{\"x\":0,\"y\":1}},{\"members\":[{\"x\":0,\"y\":0},{\"x\":1,\"y\":0}],\"pivot\":{\"x\":0,\"y\":0}},{\"members\":[{\"x\":0,\"y\":0},{\"x\":1,\"y\":0}],\"pivot\":{\"x\":1,\"y\":0}},{\"members\":[{\"x\":0,\"y\":0},{\"x\":0,\"y\":1}],\"pivot\":{\"x\":0,\"y\":0}},{\"members\":[{\"x\":0,\"y\":0},{\"x\":0,\"y\":1}],\"pivot\":{\"x\":0,\"y\":1}},{\"members\":[{\"x\":0,\"y\":0},{\"x\":1,\"y\":0},{\"x\":2,\"y\":0}],\"pivot\":{\"x\":0,\"y\":0}},{\"members\":[{\"x\":0,\"y\":0},{\"x\":1,\"y\":0},{\"x\":2,\"y\":0}],\"pivot\":{\"x\":1,\"y\":0}},{\"members\":[{\"x\":0,\"y\":0},{\"x\":1,\"y\":0},{\"x\":2,\"y\":0}],\"pivot\":{\"x\":2,\"y\":0}},{\"members\":[{\"x\":0,\"y\":0},{\"x\":0,\"y\":1},{\"x\":0,\"y\":2}],\"pivot\":{\"x\":0,\"y\":0}},{\"members\":[{\"x\":0,\"y\":0},{\"x\":0,\"y\":1},{\"x\":0,\"y\":2}],\"pivot\":{\"x\":0,\"y\":1}},{\"members\":[{\"x\":0,\"y\":0},{\"x\":0,\"y\":1},{\"x\":0,\"y\":2}],\"pivot\":{\"x\":0,\"y\":2}},{\"members\":[{\"x\":1,\"y\":0},{\"x\":0,\"y\":1},{\"x\":1,\"y\":2}],\"pivot\":{\"x\":1,\"y\":0}},{\"members\":[{\"x\":1,\"y\":0},{\"x\":0,\"y\":1},{\"x\":1,\"y\":2}],\"pivot\":{\"x\":1,\"y\":1}},{\"members\":[{\"x\":1,\"y\":0},{\"x\":0,\"y\":1},{\"x\":1,\"y\":2}],\"pivot\":{\"x\":1,\"y\":2}}],\"id\":0,\"filled\":[],\"sourceLength\":100}";

    public EditText eTw, eTh;
    public static int height = 0, width = 0;

    public static String MY_PREF = "MY_PREF";
    public static final String sWIDTH = "width", sHEIGHT = "height";

    final CharSequence myList[] = {"pack 1", "pack 2", "pack 3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        findViewById(R.id.bttnDialog).setOnClickListener(this);
        findViewById(R.id.bttnSentence).setOnClickListener(this);
        findViewById(R.id.bttnMenu).setOnClickListener(this);

        eTw = (EditText) findViewById(R.id.width);
        eTh = (EditText) findViewById(R.id.height);

        checkBox = (CheckBox) findViewById(R.id.radioButton);
        checkBox.setOnClickListener(this);

        ad = new AlertDialog.Builder(this);
        ad.setTitle(getResources().getString(R.string.pack) + "?");

        ad.setSingleChoiceItems(myList, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                switch (myList[arg1].toString()) {
                    case "pack 1":
                        strpack = getResources().getString(R.string.problem_0);
                        break;
                    case "pack 2":
                        strpack = getResources().getString(R.string.problem_1);
                        break;
                    case "pack 3":
                        strpack = getResources().getString(R.string.problem_11);
                        break;
                }

            }
        });

        ad.setNegativeButton("OK", (DialogInterface dialog, int which) -> {
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bttnSentence:
                //TODO: сократить формулки
                if (!eTw.getText().toString().equals(""))
                    eTh.setText("" + (int) ((2 * (scrh - 30) - 2 * scrw /
                            (Math.sqrt(3) * (2 * Integer.parseInt(eTw.getText().toString()) + 1))) /
                            (3 * 2 * scrw / (Math.sqrt(3) * (2 * Integer.parseInt(eTw.getText().toString()) + 1)))));
                break;

            case R.id.bttnDialog:
                ad.show();
                break;

            case R.id.bttnMenu:
                finish();
                break;
        }

        //TODO: уже и не знаю где искать помощь - моментальное срабатывание
        if (checkBox.isChecked()) {
            stopService(new Intent(this, MyService.class));
            checkBox.setText(getResources().getString(R.string.muson));
        }

        if (!checkBox.isChecked()) {
            startService(new Intent(this, MyService.class));
            checkBox.setText(getResources().getString(R.string.musoff));
        }
    }


    public void saveText() {
        if (!eTh.getText().toString().equals("") && !eTw.getText().toString().equals("")) {

            sharedPreferences = getSharedPreferences(MY_PREF, MODE_PRIVATE);
            Editor ed = sharedPreferences.edit();

            ed.putInt(sHEIGHT, Integer.parseInt(eTh.getText().toString()));
            ed.putInt(sWIDTH, Integer.parseInt(eTw.getText().toString()));

            ed.commit();
        }
    }


    public void loadText() {
        sharedPreferences = getSharedPreferences(MY_PREF, MODE_PRIVATE);

        width = sharedPreferences.getInt(sWIDTH, 8);
        height = sharedPreferences.getInt(sHEIGHT, 16);
    }


    @Override
    protected void onPause() {
        saveText();
        loadText();
        super.onPause();
    }
}