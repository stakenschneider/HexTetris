package com.example.masha.tetris;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import draw.DrawGrid;


public class FigureCreating extends AppCompatActivity implements View.OnTouchListener {

    Button bttnAdd, bttnPl;
    float x = 0 , y = 0;
//    Intent intent;
public static int hBttn = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new CanvasView(this));
        hBttn = bttnAdd.getHeight();

        bttnAdd = (Button) findViewById(R.id.bttnAdd);
        bttnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //метод добавления фигуры
                hBttn = bttnAdd.getHeight();

            }
        });

        bttnPl = (Button) findViewById(R.id.bttnPl);
        bttnPl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                intent = new Intent( GamePlay.class);
//                startActivity(intent);
            }
        });
    }

    class CanvasView extends View {

        public CanvasView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawRGB(11, 25, 25);
            DrawGrid d = new DrawGrid();
            d.useBuilder(canvas , 1);
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        x = event.getX();
        y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // если происходит нажатие возвращает х и у нажатия


                break;

            case MotionEvent.ACTION_CANCEL:
                //не трогай
                break;


        }

        return true;
    }

}
