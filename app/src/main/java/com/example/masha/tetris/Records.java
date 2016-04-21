package com.example.masha.tetris;


import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.delete.DefaultDeleteResolver;
import com.pushtorefresh.storio.sqlite.operations.get.DefaultGetResolver;
import com.pushtorefresh.storio.sqlite.operations.put.DefaultPutResolver;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.queries.InsertQuery;
import com.pushtorefresh.storio.sqlite.queries.UpdateQuery;

import java.util.ArrayList;
import java.util.Arrays;



import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.example.masha.tetris.Main.dbHelper;



public class Records extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        class StringGetResolver extends DefaultGetResolver<String> {

            @Override
            public String mapFromCursor(Cursor cursor)  {

                String values = cursor.getString(cursor.getColumnIndexOrThrow("points"));
                return values;
            }

        }

        class StringPutResolver extends DefaultPutResolver<String> {

            @Override
            public InsertQuery mapToInsertQuery (String s)  {
                return InsertQuery.builder()
                        .table("points")
                        .build();
            }

            @Override
            public UpdateQuery mapToUpdateQuery (String s) {
                return UpdateQuery.builder()
                        .table("points")
                        .where("${points} = ?")
                        .build();
            }

            @Override
            public ContentValues mapToContentValues (String s)  {
              ContentValues cv = new ContentValues();
                cv.put("points",s);
                return cv;
            }
        }
        class StringDeleteResolver extends DefaultDeleteResolver<String> {

            @Override
            public DeleteQuery mapToDeleteQuery(String s)
            {
                return DeleteQuery.builder()
                    .table("mytable")
                    .build();
            }

        }





            StorIOSQLite storIOSQLite = DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(dbHelper)
                .addTypeMapping(String.class, SQLiteTypeMapping.<String>builder()
                        .putResolver(new StringPutResolver())
                        .getResolver(new StringGetResolver())
                        .deleteResolver(new StringDeleteResolver())
                        .build())
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
