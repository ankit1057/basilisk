package com.androidsrc.snake_game;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.androidsrc.snake_game.snakegame.SnakeGamePanel;

public class SnakeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snake);
        setContentView(new SnakeGamePanel(SnakeActivity.this, false));
    }
}
