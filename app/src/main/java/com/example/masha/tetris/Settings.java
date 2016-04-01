package com.example.masha.tetris;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import service.MyService;

import static com.example.masha.tetris.Main.scrw;
import static com.example.masha.tetris.Main.scrh;


public class Settings extends AppCompatActivity implements View.OnClickListener {

    Button bttnMenu , bttnSentence;
    Intent intent;
    RadioButton radioButton;

    SharedPreferences sharedPreferences;

    public EditText eTw , eTh;
    public static int height = 0 , width = 0 ;

    public static String MY_PREF = "MY_PREF"; //ресурс в виде хмл типо там у нас и хранятся эти значения

    public static final String sWIDTH = "width"; //ключ для ширины и высоты соответственно
    public static final String sHEIGHT = "height";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        eTw = (EditText) findViewById(R.id.width);
        eTh = (EditText) findViewById(R.id.height);

        bttnMenu = (Button) findViewById(R.id.bttnMenu);
        bttnMenu.setOnClickListener(this);

        bttnSentence = (Button) findViewById(R.id.bttnSentence);
        bttnSentence.setOnClickListener(this);

        radioButton = (RadioButton) findViewById(R.id.radioButton);

    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bttnMenu:
                intent = new Intent (this, Main.class);
                startActivity(intent);
                loadText();
                finish();
                break;

            case R.id.bttnSentence:
                eTh.setText("" +(int)((2*(scrh-30)-2*scrw/(Math.sqrt(3)*(2*Integer.parseInt(eTw.getText().toString())+1)))/(3*2*scrw/(Math.sqrt(3)*(2*Integer.parseInt(eTw.getText().toString())+1)))));
                break;
        }

        if (radioButton.isChecked()) { // доделать мнгновенное срабатывание
            stopService(new Intent(this, MyService.class));
            radioButton.setText("music on");
        } else
        {
            startService(new Intent(this, MyService.class));
            radioButton.setText("music off");
        }
    }


    public void saveText()
    {
        sharedPreferences = getSharedPreferences(MY_PREF, MODE_PRIVATE);
        Editor ed = sharedPreferences.edit();

        ed.putInt(sHEIGHT, Integer.parseInt(eTh.getText().toString()));
        ed.putInt(sWIDTH, Integer.parseInt(eTw.getText().toString()));

        ed.commit();
    }


    void loadText() {
        sharedPreferences = getSharedPreferences(MY_PREF, MODE_PRIVATE);

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