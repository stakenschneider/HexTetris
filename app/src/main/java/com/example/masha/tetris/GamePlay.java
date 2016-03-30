package com.example.masha.tetris;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Canvas;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;


import draw.DrawGrid;


public class GamePlay extends AppCompatActivity  {

    private final GestureDetector gd = new GestureDetector(new GestureListener());

    private static final int DISTANCE = 100;
    private static final int VELOCITY = 200;

    DrawGrid d;
    String movement = "START";
    CanvasView view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        view = new CanvasView(this);

        view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(final View v, final MotionEvent e) {
                gd.onTouchEvent(e);
                return true;
            }
        });
        setContentView(view);
    }


    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(e1.getX() - e2.getX() > DISTANCE && Math.abs(velocityX) > VELOCITY) {
                movement = "LEFT";
                view.invalidate();
                return false;
            }

            if (e2.getX() - e1.getX() > DISTANCE && Math.abs(velocityX) > VELOCITY) {
                movement = "RIGHT";
                view.invalidate();
                return false;
            }

            if (e2.getY() - e1.getY() > DISTANCE && Math.abs(velocityY) > VELOCITY)
            {
                movement = "DOWN";
                view.invalidate();
                return false;
            }

            if (e1.getY() - e2.getY() > DISTANCE && Math.abs(velocityY) > VELOCITY)
            {
                movement = "UP";
                view.invalidate();
                return false;
            }
            return false;
        }
    }


    class CanvasView extends View {

        public CanvasView(Context context) {
            super(context);
            d = new DrawGrid();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawRGB(11, 25, 25);
            d.useBuilder(canvas , movement , 0);
        }
    }
}







