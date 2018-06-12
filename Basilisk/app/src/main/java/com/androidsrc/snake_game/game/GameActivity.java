package com.androidsrc.snake_game.game;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.androidsrc.snake_game.R;

public class GameActivity extends GameBaseActivity {
    public static boolean isServer;
    public static String username;
    public static int nusers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        isServer = extras.getBoolean("isServer");
        username = extras.getString("username");

        Bundle args = new Bundle();
        args.putBoolean("isServer", isServer);
        args.putString("username", username);
        args.putInt("nusers", nusers);

        if (savedInstanceState == null) {
            MainFragment mf = new MainFragment();
            mf.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, mf)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected Fragment createFragment() {
        return new MainFragment();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}