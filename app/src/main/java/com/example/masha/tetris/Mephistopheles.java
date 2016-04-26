package com.example.masha.tetris;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Canvas;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.os.Handler;
import android.widget.RelativeLayout;

import JSON.InitGame;
import draw.DrawGrid;


public class Mephistopheles extends AppCompatActivity {

    DrawGrid d = new DrawGrid();
    CanvasView view , view_2, view_3;
    Intent intent;
    public static Handler h;
    private boolean over = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        RelativeLayout relLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams relLayoutParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        setContentView(relLayout, relLayoutParam);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

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

        intent  = getIntent();
        String strJson = intent.getStringExtra("JSON");
        InitGame g = new InitGame(strJson);
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
            if (over == true)
                gameOver();
        }
    }

    private void gameOver() {
        finish();
        intent = new Intent(this , EndGame.class);
        startActivity(intent);
    }
}