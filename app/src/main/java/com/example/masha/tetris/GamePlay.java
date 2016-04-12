package com.example.masha.tetris;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Canvas;
import android.content.Context;

import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;

import static com.example.masha.tetris.Main.scrh;
import static com.example.masha.tetris.Main.scrw;

import draw.DrawGrid;


public class GamePlay extends AppCompatActivity {


    DrawGrid d;
    CanvasView view , view_2;
    float x  , y;
    Intent intent;
    private boolean over = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//обязательноперед канвасом с клилистенером
        RelativeLayout relLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams relLayoutParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        setContentView(relLayout, relLayoutParam);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        view_2 = new CanvasView(this, "START");
        view = new CanvasView(this, "GAME");
        view.setLayoutParams(params);
        view_2.setLayoutParams(params);
        relLayout.addView(view_2);
        relLayout.addView(view);
        view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(final View v, final MotionEvent event) {
                x = event.getX();
                y = event.getY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (y > (scrh / 1.5) && x < scrw / 2) {
                            view.setMovement("DOWN_LEFT");
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
            }
        });
    }


    class CanvasView extends View {
        String movement;
        public CanvasView(Context context, String movement) {
            super(context);
            this.movement = movement;
            d = new DrawGrid();
        }

        @Override
        protected void onDraw(Canvas canvas) {
             over = d.useBuilder(canvas, movement);
            if (over == true)
                gameOver();

        }

        public void setMovement (String movement)
        {
            this.movement = movement;
        }
    }



    private void gameOver()
    {
        finish();
        intent = new Intent(this , EndGame.class);
        startActivity(intent);
    }
}







