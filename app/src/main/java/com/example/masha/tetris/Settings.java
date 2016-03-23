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
import static com.example.masha.tetris.Main.scrw;
import static com.example.masha.tetris.Main.scrh;


public class Settings extends AppCompatActivity implements View.OnClickListener {

    Button bttnMenu , bttnSentence;
    Intent intent;
    public EditText eTw , eTh;  //вот эти два поля куда вводим значения
    SharedPreferences sharedPreferences;

    public static int height = 0 , width = 0 ; //соответствено значения которые надо получить

    private static final String TAG = "myLogs"; //хуйня

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

        loadText();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bttnMenu:
                intent = new Intent (this, Main.class);
                startActivity(intent);
                break;

            case R.id.bttnSentence:
                eTh.setText("" +(int)((2*scrh-2*scrw/(Math.sqrt(3)*(2*Integer.parseInt(eTw.getText().toString())+1)))/(3*2*scrw/(Math.sqrt(3)*(2*Integer.parseInt(eTw.getText().toString())+1)))));
                break;
        }
    }


    public void saveText() //должен типо сохранять значения 
    {

        sharedPreferences = getSharedPreferences(MY_PREF, MODE_PRIVATE); //создаем преференсис (MODE_PRIVATE для того что бы эти значения были только для одного приложения (хуйня не обращай внимания))
        Editor ed = sharedPreferences.edit(); //для изменения и редактирования и бла бла бда

        ed.putInt(sHEIGHT, Integer.parseInt(eTh.getText().toString())); //записываем в ключ ВЫСОТА значение уже переведенное в инт с поля куда вводили 

        ed.putInt(sWIDTH, Integer.parseInt(eTw.getText().toString()));  //таже нерабочая херня

        ed.commit();  //сохраняем изменения - типо как в бд комит или apply разницы нет 
    }


    void loadText() { //функция которая ДОЛЖНА ВОЗВРАЩАТЬ ДОЛБАННЫЕ ЗНАЧЕНИЯ
        sharedPreferences = getSharedPreferences(MY_PREF, MODE_PRIVATE);

        //если такой ресурс существует то (сразу говорю тут ошибок нет, потому что и без ифа нихуя не работает)
        width = sharedPreferences.getInt(sWIDTH , 8 ); //получаем значение по ключу (если такого нет то 8)
        height = sharedPreferences.getInt(sHEIGHT, 15); //аналогично
    }


    @Override
    protected void onPause(){
        saveText(); //если нажимаем кнопу меню или выходим из этой активити стрелочкой "иди нахуй" то вызываем сохранение 
        super.onPause();
        Log.d(TAG, "Settings Pause");
    }

}