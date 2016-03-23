package com.example.masha.tetris;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Canvas;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;

import draw.DrawGrid;


public class GamePlay extends AppCompatActivity implements OnTouchListener {

    float x, y;
    boolean inTouch = false;
    int downPI = 0, upPI = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new CanvasView(this));
    }


    class CanvasView extends View {

        public CanvasView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawRGB(11, 25, 25);
            DrawGrid d = new DrawGrid();
            d.useBuilder(canvas);
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        x = event.getX();
        y = event.getY();

        int actionMask = event.getActionMasked();   //событие
        int pointerIndex = event.getActionIndex();  //индекс касания
        int pointerCount = event.getPointerCount(); //число касаний


        switch (actionMask) {
            case MotionEvent.ACTION_DOWN: //нажатие первое
                inTouch = true;
//                break;

            case MotionEvent.ACTION_POINTER_DOWN: //нажатие больше 1
                downPI = pointerIndex;

                if (pointerCount == 2)
                {
                    //поворот
                }
                break;


            case MotionEvent.ACTION_UP: //отпускание последнего пальца
                //ничего наверн
                inTouch = false;
//                break;

            case MotionEvent.ACTION_POINTER_UP: //наверно отпускание любого пальца кроме последнего
                //возвращение на ACTION_MOVE. ГО ТУ!
                upPI = pointerIndex;
                break;


            case MotionEvent.ACTION_MOVE: //движение
                //влево вправо перемещение фигуры


                break;

            case MotionEvent.ACTION_CANCEL: //хз но по-моему прописывать надо
                //что то если ломается и опять же ИМХО

                break;

        }
        return true;
    }

}






