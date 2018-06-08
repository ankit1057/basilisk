package com.androidsrc.snake_game.game;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.androidsrc.snake_game.R;

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_base);
        //TODO: update your game theme here
        setTheme(R.style.AppFullScreenTheme);
    }
}
