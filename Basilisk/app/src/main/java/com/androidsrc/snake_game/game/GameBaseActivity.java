package com.androidsrc.snake_game.game;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import com.androidsrc.snake_game.R;

public abstract class GameBaseActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        replaceFragment(createFragment());
    }

    protected abstract Fragment createFragment();

    public int getFrameLayoutId() {
        return R.id.container;
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();

        Fragment origin = manager.findFragmentById(getFrameLayoutId());
        if (origin == null) {
            trans.add(getFrameLayoutId(), fragment);
        } else {
            trans.replace(getFrameLayoutId(), fragment);
        }
        trans.commit();
    }
}
