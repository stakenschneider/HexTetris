package com.example.masha.tetris;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ChoiceOfProblems extends AppCompatActivity implements View.OnClickListener {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_of_problems);

        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);
        findViewById(R.id.button6).setOnClickListener(this);
        findViewById(R.id.button7).setOnClickListener(this);
        findViewById(R.id.button8).setOnClickListener(this);
        findViewById(R.id.button9).setOnClickListener(this);
        findViewById(R.id.button10).setOnClickListener(this);
        findViewById(R.id.button11).setOnClickListener(this);
        findViewById(R.id.button12).setOnClickListener(this);
        findViewById(R.id.button13).setOnClickListener(this);
        findViewById(R.id.button14).setOnClickListener(this);
        findViewById(R.id.button15).setOnClickListener(this);
        findViewById(R.id.button16).setOnClickListener(this);
        findViewById(R.id.button17).setOnClickListener(this);
        findViewById(R.id.button18).setOnClickListener(this);
        findViewById(R.id.button19).setOnClickListener(this);
        findViewById(R.id.button20).setOnClickListener(this);
        findViewById(R.id.button21).setOnClickListener(this);
        findViewById(R.id.button22).setOnClickListener(this);
        findViewById(R.id.button23).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                intent = new Intent (this, GamePlay.class);
                intent.putExtra("JSON" , getResources().getString(R.string.problem_0));
                intent.putExtra("Player" , "AI");
                intent.putExtra("Problem" , "1");
                startActivity(intent);
                finish();
                break;

            case R.id.button2:
                intent = new Intent (this, GamePlay.class);
                intent.putExtra("JSON" , getResources().getString(R.string.problem_1));
                intent.putExtra("Player" , "AI");
                startActivity(intent);
                finish();
                break;

            case R.id.button3:
                intent = new Intent (this, GamePlay.class);
                intent.putExtra("JSON" , getResources().getString(R.string.problem_2));
                intent.putExtra("Player" , "AI");
                intent.putExtra("Problem" , "3");
                startActivity(intent);
                finish();
                break;

            case R.id.button4:
                intent = new Intent (this, GamePlay.class);
                intent.putExtra("JSON" , getResources().getString(R.string.problem_3));
                intent.putExtra("Player" , "AI");
                startActivity(intent);
                finish();
                break;

            case R.id.button5:
                intent = new Intent (this, GamePlay.class);
                intent.putExtra("JSON" , getResources().getString(R.string.problem_4));
                intent.putExtra("Player" , "AI");
                startActivity(intent);
                finish();
                break;

            case R.id.button6:
                intent = new Intent (this, GamePlay.class);
                intent.putExtra("JSON" , getResources().getString(R.string.problem_5));
                intent.putExtra("Player" , "AI");
                startActivity(intent);
                finish();
                break;

            case R.id.button7:
                intent = new Intent (this, GamePlay.class);
                intent.putExtra("JSON" , getResources().getString(R.string.problem_6));
                intent.putExtra("Player" , "AI");
                startActivity(intent);
                finish();
                break;
            case R.id.button8:
                intent = new Intent (this, GamePlay.class);
                intent.putExtra("JSON" , getResources().getString(R.string.problem_7));
                intent.putExtra("Player" , "AI");
                startActivity(intent);
                finish();
                break;
            case R.id.button9:
                intent = new Intent (this, GamePlay.class);
                intent.putExtra("JSON" , getResources().getString(R.string.problem_8));
                intent.putExtra("Player" , "AI");
                startActivity(intent);
                finish();
                break;
            case R.id.button10:
                intent = new Intent (this, GamePlay.class);
                intent.putExtra("JSON" , getResources().getString(R.string.problem_9));
                intent.putExtra("Player" , "AI");
                startActivity(intent);
                finish();
                break;
            case R.id.button11:
                intent = new Intent (this, GamePlay.class);
                intent.putExtra("JSON" , getResources().getString(R.string.problem_10));
                intent.putExtra("Player" , "AI");
                startActivity(intent);
                finish();
                break;
            case R.id.button12:
                intent = new Intent (this, GamePlay.class);
                intent.putExtra("JSON" , getResources().getString(R.string.problem_11));
                intent.putExtra("Player" , "AI");
                startActivity(intent);
                finish();
                break;
            case R.id.button13:
                intent = new Intent (this, GamePlay.class);
                intent.putExtra("JSON" , getResources().getString(R.string.problem_12));
                intent.putExtra("Player" , "AI");
                startActivity(intent);
                finish();
                break;
            case R.id.button14:
                intent = new Intent (this, GamePlay.class);
                intent.putExtra("JSON" , getResources().getString(R.string.problem_13));
                intent.putExtra("Player" , "AI");
                startActivity(intent);
                finish();
                break;
            case R.id.button15:
                intent = new Intent (this, GamePlay.class);
                intent.putExtra("JSON" , getResources().getString(R.string.problem_14));
                intent.putExtra("Player" , "AI");
                startActivity(intent);
                finish();
                break;
            case R.id.button16:
                intent = new Intent (this, GamePlay.class);
                intent.putExtra("JSON" , getResources().getString(R.string.problem_15));
                intent.putExtra("Player" , "AI");
                startActivity(intent);
                finish();
                break;
            case R.id.button17:
                intent = new Intent (this, GamePlay.class);
                intent.putExtra("JSON" , getResources().getString(R.string.problem_16));
                intent.putExtra("Player" , "AI");
                startActivity(intent);
                finish();
                break;
            case R.id.button18:
                intent = new Intent (this, GamePlay.class);
                intent.putExtra("JSON" , getResources().getString(R.string.problem_17));
                intent.putExtra("Player" , "AI");
                startActivity(intent);
                finish();
                break;
            case R.id.button19:
                intent = new Intent (this, GamePlay.class);
                intent.putExtra("JSON" , getResources().getString(R.string.problem_18));
                intent.putExtra("Player" , "AI");
                startActivity(intent);
                finish();
                break;
            case R.id.button20:
                intent = new Intent (this, GamePlay.class);
                intent.putExtra("JSON" , getResources().getString(R.string.problem_19));
                intent.putExtra("Player" , "AI");
                startActivity(intent);
                finish();
                break;
            case R.id.button21:
                intent = new Intent (this, GamePlay.class);
                intent.putExtra("JSON" , getResources().getString(R.string.problem_20));
                intent.putExtra("Player" , "AI");
                startActivity(intent);
                finish();
                break;
            case R.id.button22:
                intent = new Intent (this, GamePlay.class);
                intent.putExtra("JSON" , getResources().getString(R.string.problem_21));
                intent.putExtra("Player" , "AI");
                startActivity(intent);
                finish();
                break;
            case R.id.button23:
                intent = new Intent (this, GamePlay.class);
                intent.putExtra("JSON" , getResources().getString(R.string.problem_22));
                intent.putExtra("Player" , "AI");
                startActivity(intent);
                finish();
                break;

        }
    }
}
