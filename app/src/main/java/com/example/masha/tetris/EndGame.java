package com.example.masha.tetris;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import static draw.DrawGrid.point;
import static com.example.masha.tetris.Main.dbHelper;

//TODO: @string/bttnRetry проверить
public class EndGame extends AppCompatActivity implements View.OnClickListener {

    TextView textView , textEnd;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        textEnd = (TextView) findViewById(R.id.textView6);
        textView = (TextView) findViewById(R.id.textScore);
        textView.setText(getResources().getString(R.string.score) + point);

        intent = getIntent();
        if (intent.getStringExtra("END").equals("win"))
        textEnd.setText(getResources().getString(R.string.win)); else
        textEnd.setText(getResources().getString(R.string.lose));

        findViewById(R.id.bttnMyMenu).setOnClickListener(this);
        findViewById(R.id.bttnRetry).setOnClickListener(this);

        ContentValues cv = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        cv.put("point" , point);
        db.insert("mytable", null, cv);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bttnRetry:
                finish();
                intent = new Intent (this, GamePlay.class);
                startActivity(intent);
                break;

            case R.id.bttnMyMenu:
                finish();
                break;
        }
    }

}
