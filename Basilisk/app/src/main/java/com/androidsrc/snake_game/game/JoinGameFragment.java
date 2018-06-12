package com.androidsrc.snake_game.game;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidsrc.snake_game.R;
import com.androidsrc.snake_game.communication.ClientConnThread;
import com.androidsrc.snake_game.snakegame.SnakeGamePanel;


public class JoinGameFragment extends Fragment {

    public static TextView ServerIP;
    public static TextView userName;
    public static TextView statusTV;
    public static ClientConnThread clientConnTd;
    public static boolean isClientConnected;
    private static GameActivity act;
    public static SnakeGamePanel sgp;
    public static boolean isServer = false;
    //public static Game gameobject;

    public JoinGameFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        act = (GameActivity)context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_join_game_fragment, container, false);
        final Button joinGame = (Button) rootView.findViewById(R.id.joinGame);
        ServerIP = (TextView) rootView.findViewById(R.id.gameName);
        userName = (TextView) rootView.findViewById(R.id.welcometext);
        userName.setText("WELCOME, "+MainFragment.userName.getText()+" !");
        statusTV = (TextView) rootView.findViewById(R.id.ConStatusTV);
        statusTV.setText("Enter the IP and Click Connect");
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        isClientConnected = false;

        joinGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Redo the Game Start logic and hold the game for all players to start. implement the Client connection here
                final String IP = ServerIP.getText().toString();
                if (IP != null) {
                    //TODO: Need to handle the restart - close the handler before restart
                    if (clientConnTd == null) {
                        //TODO: need to handle invalid ip numnber
                        clientConnTd = new ClientConnThread((Context) act, IP);
                        statusTV.setText("Waiting for Connection, connect again to verify!");
                    }

                    //For UI
                    if(isClientConnected) {
                        statusTV.setText("Connected, waiting for server to start game!!");
                        joinGame.setText("Start Game");
                    }

                    //Assuming the server first initializes the client in first messagge on start game
                    if(MainFragment.sgp.snake != null) {
                       StartGameFragment gameFragment = new StartGameFragment();
                       fragmentManager.beginTransaction()
                               .replace(R.id.container, gameFragment)
                               .commit();
                    }
                    else {
                        Toast.makeText(getActivity(), "Waiting for the server to start", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Entered IP is invalid. Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
    }


}