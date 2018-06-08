package com.androidsrc.snake_game.game;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.androidsrc.snake_game.R;
import com.androidsrc.snake_game.snakegame.SnakeGamePanel;
import com.androidsrc.snake_game.utils.ClientHandler;


public class MainFragment extends Fragment {
    private static GameActivity act;
    public static SnakeGamePanel sgp;
    public static ClientHandler clientHandler;
    public static boolean isServer;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        act = (GameActivity)context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //update the isServer param from GameActivity, which in turn from MainActivity
        if (getArguments() != null) {
            Bundle args = this.getArguments();
            isServer = args.getBoolean("isServer", false);
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_snake, container, false);
        //currContext = getActivity().getApplicationContext();
        sgp = new SnakeGamePanel(act, isServer);

        if (clientHandler == null) {
            clientHandler = new ClientHandler();
        }
        return sgp;

    }

    public boolean onTouch(View v, MotionEvent event) {
        System.out.println("MainFragmentTouchActivity");
        return true;
    }
}
