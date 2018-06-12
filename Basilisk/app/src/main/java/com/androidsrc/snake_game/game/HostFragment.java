package com.androidsrc.snake_game.game;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidsrc.snake_game.R;
import com.androidsrc.snake_game.communication.ServerConnThread;


public class HostFragment extends Fragment {
    public static ServerConnThread serverHandler;
    public static EditText gameName;
    public EditText numberOfPlayers;
    public static int numberPlayers;
    private static GameActivity act;
    public static String ipaddr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Start the server Connection thread to listen for client connections.
        serverHandler = new ServerConnThread();
        ipaddr = serverHandler.getIpAddress();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        act = (GameActivity)context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_host_game, container, false);
        Button startGame = (Button) rootView.findViewById(R.id.startGame);
        TextView serverip = (TextView) rootView.findViewById(R.id.server_ip_addr);
        serverip.setText(""+ ipaddr);
        gameName = (EditText) rootView.findViewById(R.id.gameName);
        numberOfPlayers = (EditText) rootView.findViewById(R.id.numberOfPlayers);
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), ipaddr, Toast.LENGTH_LONG);
                if (gameName.getText() != null && numberOfPlayers.getText() != null
                        && numberOfPlayers.getText().toString().trim().length() > 0 && gameName.getText().toString().trim().length() > 0) {
                    numberPlayers = Integer.valueOf(numberOfPlayers.getText().toString());
                    if (numberPlayers > 5 || numberPlayers < 1) {
                        Toast.makeText(getActivity(), "Maximum 5 players allowed ", Toast.LENGTH_SHORT).show();
                    } else {
                        //serverHandler.start();
                        //TODO: Find what to do here. How to Transition into game fragment
                        StartGameFragment gameFragment = new StartGameFragment();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, gameFragment)
                                .commit();
                    }
                } else {
                    Toast.makeText(getActivity(), "Missing game name or number of players", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
    }


}