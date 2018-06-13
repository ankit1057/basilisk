package com.androidsrc.snake_game.game;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidsrc.snake_game.R;
import com.androidsrc.snake_game.snakegame.SnakeGamePanel;
import com.androidsrc.snake_game.utils.ClientHandler;
import com.androidsrc.snake_game.utils.Constants;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class MainFragment extends Fragment {
    private static GameActivity act;
    public static SnakeGamePanel sgp;
    public static ClientHandler clientHandler;
    public static boolean isServer;
    public static EditText userName;
    public static Constants constants;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        act = (GameActivity)context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        constants = new Constants();
        isServer = false;
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
        View rootView = inflater.inflate(R.layout.activity_snake, container, false);
        //currContext = getActivity().getApplicationContext();
        //sgp = new SnakeGamePanel(act, isServer);
        /*if (clientConnTd == null) {
            clientConnTd = new ClientHandler();
        }*/
        Button hostGame = (Button) rootView.findViewById(R.id.hostgame);
        Button joinGame = (Button) rootView.findViewById(R.id.joingame);
        Button infoButton = (Button) rootView.findViewById(R.id.infobutton);
        //Info button fragment
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new InformationFragment()).addToBackStack(InformationFragment.class.getName())
                        .commit();
            }
        });
        WifiManager wifi = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        Method[] wmMethods = wifi.getClass().getDeclaredMethods();
        userName = (EditText) rootView.findViewById(R.id.username_edit_text);
        for (Method method : wmMethods) {
            if (method.getName().equals("isWifiApEnabled")) {
                try {
                    boolean isWifiAPEnabled = (Boolean) method.invoke(wifi);
                    final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    if (isWifiAPEnabled) {
                        isServer = true;
                        joinGame.setVisibility(View.GONE);
                        hostGame.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (userName.getText() != null && userName.getText().toString().trim().length() > 0) {
                                    sgp = new SnakeGamePanel(userName.getText().toString(), act, isServer);
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.container, new HostFragment()).addToBackStack(HostFragment.class.getName())
                                            .commit();
                                } else {
                                    Toast.makeText(getActivity(), "Please enter a UserName", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        if (clientHandler == null) {
                            //TODO: Need to get Ip address to establish communication. So dont follow Game of cards logic here. Instead use this code in the Join game Fragment
                            clientHandler = new ClientHandler();
                        }
                        isServer = false;
                        hostGame.setVisibility(View.GONE);
                        joinGame.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                /*ClientConnectionThread clientConnect = new ClientConnectionThread(userName.getText().toString());
                                clientConnect.start();*/
                                if (userName.getText() != null && userName.getText().toString().trim().length() > 0) {
//                                    if (ClientConnectionThread.serverStarted) {
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.container, new JoinGameFragment()).addToBackStack(JoinGameFragment.class.getName())
                                            .commit();
//                                    } else {
//                                        Toast.makeText(getActivity(), "Game yet to be hosted", Toast.LENGTH_SHORT).show();
//                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Please enter a UserName", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return rootView;
    }

    public boolean onTouch(View v, MotionEvent event) {
        System.out.println("MainFragmentTouchActivity");
        return true;
    }
    //TODO: Username data should be sent to snakegame panel constructor when starting the game. Function to access it is here.
    public String getusername(){
        return userName.toString();
    }
}
