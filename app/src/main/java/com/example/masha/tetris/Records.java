package com.example.masha.tetris;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.queries.RawQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.example.masha.tetris.Main.dbHelper;

public class Records extends AppCompatActivity {
    ArrayList<String> listPoint = new ArrayList<>();
    String[] sortPoint = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        StorIOSQLite storIOSQLite = DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(dbHelper)
                .addTypeMapping(String.class, ) //???
                .build();
        storIOSQLite
                .get()
                .listOfObjects(String.class)
                .withQuery(Query.builder()
                        .table("mytable")
                        .build())
                .prepare()
                .asRxObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(points -> {
                    String[] s = points.toArray(new String[points.size()]);
                    Arrays.sort(s);
                    return s;
                })
                        //.flatMap(s-> Observable.from(s))
                .map((String[] str) -> {
                    String finals = new String();
                    for (int z = 0; z < str.length; z++)
                        finals = finals + (z + 1) + ". " + str[z] + "\n";
                    return finals;
                }).subscribe( s -> {TextView textView = (TextView) findViewById(R.id.text4records); textView.setText(s);});
    }
}
