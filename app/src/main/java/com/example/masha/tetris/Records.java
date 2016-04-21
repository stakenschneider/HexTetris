package com.example.masha.tetris;


import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.get.DefaultGetResolver;
import com.pushtorefresh.storio.sqlite.queries.Query;
import java.util.Arrays;



import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.example.masha.tetris.Main.dbHelper;


public class Records extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        TextView textView = (TextView) findViewById(R.id.text4records);
        class StringGetResolver extends DefaultGetResolver<String> {

            @Override
            public String mapFromCursor(Cursor cursor)  {

                String values = cursor.getString(cursor.getColumnIndexOrThrow("point"));
                return values;
            }

        }
            StorIOSQLite storIOSQLite = DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(dbHelper)
                .build();
        storIOSQLite
                .get()
                .listOfObjects(String.class)
                .withQuery(Query.builder()
                        .table("mytable").columns("point")
                        .build())
                .withGetResolver(new StringGetResolver())
                .prepare()
                .asRxObservable()
                /* rx.exceptions.OnErrorNotImplementedException: more items arrived than were requested
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                Как то с потоками не задалось
                */
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
                }).subscribe(s -> {
            textView.setText(s);});
    }
}
