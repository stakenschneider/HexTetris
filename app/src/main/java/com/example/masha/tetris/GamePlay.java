package com.example.masha.tetris;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Canvas;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.os.Handler;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.LinkedList;

import static com.example.masha.tetris.Main.scrh;
import static com.example.masha.tetris.Main.scrw;
import static com.example.masha.tetris.Settings.strpack;

import AI.Pathfinding;
import api.Hexagon;
import draw.DrawGrid;
import static api.AxialCoordinate.fromCoordinates;


public class GamePlay extends AppCompatActivity {

    DrawGrid d;
    CanvasView view , view_2, view_3;
    float x  , y;
    Intent intent;
    public static Handler h;  //ЭТО НЕ ДОЛЖНО БЫТЬ static ТИМУУУР
    private boolean over = false;
    LinkedList<String> path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        RelativeLayout relLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams relLayoutParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        setContentView(relLayout, relLayoutParam);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        intent = getIntent();
        if (intent.getStringExtra("Player").equals("User"))
        d = new DrawGrid(strpack , "UserParameters");
        else {
            String strJson = intent.getStringExtra("JSON");
            d = new DrawGrid(strJson, "AiParameters");
            ArrayList <Hexagon> start = new ArrayList<Hexagon>();
            start.add(d.hexagonalGrid.getByAxialCoordinate(fromCoordinates(2, 0)).get());
            start.add(d.hexagonalGrid.getByAxialCoordinate(fromCoordinates(3, 0)).get());
            ArrayList <Hexagon> destination = new ArrayList<Hexagon>();
            if (intent.getStringExtra("Problem").equals("1")) {
                destination.add(d.hexagonalGrid.getByAxialCoordinate(fromCoordinates(5, 0)).get());
                destination.add(d.hexagonalGrid.getByAxialCoordinate(fromCoordinates(6, 0)).get());
            }
            if (intent.getStringExtra("Problem").equals("3")) {
                destination.add(d.hexagonalGrid.getByAxialCoordinate(fromCoordinates(-1, 7)).get());
                destination.add(d.hexagonalGrid.getByAxialCoordinate(fromCoordinates(0, 7)).get());
            }
            Pathfinding ai = new Pathfinding(d.hexagonalGrid,
                    d.hexagonalGridCalculator,
                    start,
                    destination,
                    d.hexagonalGrid.getByAxialCoordinate(fromCoordinates(1, 0)).get());
            path = ai.findPath();
            for (String s : path) Log.d("a",s);
        }
        view_2 = new CanvasView(this, "START");
        view = new CanvasView(this, "GAME");
        view_3 = new CanvasView(this, "LOCKED");

        view.setLayoutParams(params);
        view_2.setLayoutParams(params);
        view_3.setLayoutParams(params);

        relLayout.addView(view_2);
        relLayout.addView(view_3);
        relLayout.addView(view);

        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == 1) view_3.invalidate();
                msg.what = 0;
            }
        };


        // Потом листнер стоит оставить только для игры с пользователем
        // if (intent.getStringExtra("Player").equals("User"))
        view.setOnTouchListener( (final View v, final MotionEvent event) -> {
                x = event.getX();
                y = event.getY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (y > (scrh / 1.5) && x < scrw / 2) {
                            view.setMovement(path.pollFirst());
                            view.invalidate();
                            return false;
                        }

                        if (y > (scrh / 1.5) && x > scrw / 2) {
                            view.setMovement("DOWN_RIGHT");
                            view.invalidate();
                            return false;
                        }

                        if (y < (scrh / 1.5) && y > (scrh / 5) && x < scrw / 2) {
                            view.setMovement("LEFT");
                            view.invalidate();
                            return false;
                        }

                        if (y < (scrh / 1.5) && y > (scrh / 5) && x > scrw / 2) {
                            view.setMovement("RIGHT");
                            view.invalidate();
                            return false;
                        }

                        if (y < (scrh / 5) && x > scrw / 2) {
                            view.setMovement("CLCK");
                            view.invalidate();
                            return false;
                        }

                        if (y < (scrh / 5) && x < scrw / 2) {

                            view.setMovement("COUNTER_CLCK");
                            view.invalidate();
                            return false;
                        }
                        break;
                }
                return true;
            });
    }


    class CanvasView extends View {
        String movement;
        public CanvasView(Context context, String movement) {
            super(context);
            this.movement = movement;
        }

        @Override
        protected void onDraw(Canvas canvas) {
             over = d.useBuilder(canvas, movement);
            if (over)
                gameOver();

        }
        public void setMovement (String movement)
        {
            this.movement = movement;
        }
    }


    private void gameOver() {
        finish();
        intent = new Intent(this , EndGame.class);
        startActivity(intent);
    }
}