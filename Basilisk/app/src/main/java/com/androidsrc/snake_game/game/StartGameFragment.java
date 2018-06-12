package com.androidsrc.snake_game.game;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidsrc.snake_game.R;
import com.androidsrc.snake_game.snakegame.SnakeGamePanel;

public class StartGameFragment extends Fragment {
    private static GameActivity act;
    public static SnakeGamePanel sgp;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        act = (GameActivity)context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_game_fragment, container, false);
        sgp = MainFragment.sgp;
        return sgp;
    }
}
