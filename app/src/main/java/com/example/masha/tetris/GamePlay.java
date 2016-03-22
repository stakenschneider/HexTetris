package com.example.masha.tetris;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Canvas;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import draw.DrawGrid;


public class GamePlay extends AppCompatActivity {

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

}






