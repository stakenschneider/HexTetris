package com.example.masha.tetris;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;

import static com.example.masha.tetris.Main.dbHelper;

public class Records extends AppCompatActivity {

    TextView textView;
    ArrayList<String> listPoint = new ArrayList<>();
    String[] sortPoint = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        textView = (TextView)findViewById(R.id.text4records);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("mytable", null, null, null, null, null, null);


        if (c.moveToFirst()) {
            int pointColIndex = c.getColumnIndex("point");

            do {
                if(!c.getString(pointColIndex).equals("0"))
                    listPoint.add(c.getString(pointColIndex));

            } while (c.moveToNext());

            sortPoint = listPoint.toArray(new String[listPoint.size()]);

            //TODO: нормальная сортировка + описание начальных условий, аля: 250 на поле 100х100
            Arrays.sort(sortPoint);
            String str = "";

            for (int z = 0; z<listPoint.size(); z++)
                str = str +(z+1)+ ". " + sortPoint[z] +"\n";

            textView.setText(str);

        } else{
            textView.setText("наиграй, а уже потом смотри рекорды");
        }

        c.close();
    }
}