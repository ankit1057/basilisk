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
import com.androidsrc.snake_game.communication.PlayerInfo;
import com.androidsrc.snake_game.communication.ServerConnThread;
import com.androidsrc.snake_game.snakegame.SnakeActor;
import com.androidsrc.snake_game.snakegame.SnakeCommBuffer;
import com.androidsrc.snake_game.utils.ServerHandler;

import java.util.ArrayList;


public class HostFragment extends Fragment {
    public static ServerConnThread serverConTd;
    public static EditText gameName;
    public EditText numberOfPlayers;
    public static int numberPlayers;
    private static GameActivity act;
    public static String ipaddr;
    public static boolean isAllPlayersConnected = false;
    public static ServerHandler serverHandler;
    public static ArrayList<PlayerInfo> deviceList = new ArrayList();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Start the server Connection thread to listen for client connections.
        serverConTd = new ServerConnThread((Context) act);
        ipaddr = serverConTd.getIpAddress();
        isAllPlayersConnected = false;
        serverHandler = new ServerHandler();
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
                        //serverConTd.start();
                        if(isAllPlayersConnected) {
                            //TODO: initialize the enemies and enemies with others and start clients
                            initializeGame();
                            StartGameFragment gameFragment = new StartGameFragment();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.container, gameFragment)
                                    .commit();
                        }
                        else {
                            Toast.makeText(getActivity(), "Not all clients connected!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "Missing game name or number of players", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
    }

    public void initializeGame() {
        //TODO: create array list for the start index, randomize the start later
        ArrayList<SnakeActor> enemysnakes = MainFragment.sgp.enemysnakes;
        SnakeActor tsnake;
        SnakeCommBuffer tbuff;

        //Initialize server's own snake
        String unmae = MainFragment.userName.getText().toString();
        int uid = MainFragment.constants.SERVER_USER_ID;
        tsnake = new SnakeActor(100, 100, unmae, MainFragment.constants.colorLUT.get(uid));
        tsnake.userID = uid;
        MainFragment.sgp.snake = tsnake;

        //Send server snake to all
        //TODO: set user id also in constructor
        tbuff = new SnakeCommBuffer(unmae, tsnake.tailPos, tsnake.getPoint(), tsnake.getVelocity());
        tbuff.setUserID(uid);
        ServerConnThread.sendToAll(tbuff);

        //Get details from device list and initialize enemy snake and send to all
        int offset = 200;
        for(int i=0; i < deviceList.size(); i++) {
            unmae = deviceList.get(i).username;
            uid = deviceList.get(i).userid;
            tsnake = new SnakeActor((offset + i*100), (offset + i*100), unmae, MainFragment.constants.colorLUT.get(uid));
            tsnake.userID = uid;
            enemysnakes.add(tsnake);

            //update buff and send to all
            tbuff.username = unmae;
            tbuff.snakePos = tsnake.tailPos;
            tbuff.nextPos = tsnake.getPoint();
            tbuff.velocity = tsnake.getVelocity();
            tbuff.userID = uid;
            ServerConnThread.sendToAll(tbuff);
        }

        //send to client about its own start data - this will enable the client to join the game
        for(int i=0; i < enemysnakes.size(); i++) {
            tsnake = enemysnakes.get(i);

            //update buff and send to all
            tbuff.username = tsnake.userName;
            tbuff.snakePos = tsnake.tailPos;
            tbuff.nextPos = tsnake.getPoint();
            tbuff.velocity = tsnake.getVelocity();
            tbuff.userID = tsnake.userID;
            ServerConnThread.sendToClient(tbuff);
        }

    }


}