package com.androidsrc.snake_game;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.androidsrc.snake_game.communication.ClientConnThread;
import com.androidsrc.snake_game.communication.PlayerInfo;
import com.androidsrc.snake_game.communication.ServerConnThread;
import com.androidsrc.snake_game.game.GameActivity;


public class MainActivity extends Activity {

	ServerConnThread serverConnThread;
	TextView infoip;
	public static String username;
	public static int nusers;
	public TextView msg;
	Button startserver, startclient, sendtestdata, startgame;
	ClientConnThread myserver;
	long size1 =0 ;
	long size2 =0 ;
	boolean server = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		username = "Test";
		nusers = 2;
		setContentView(R.layout.activity_main);
		infoip = (TextView) findViewById(R.id.infoip);
		msg = (TextView) findViewById(R.id.msg);
		startserver = (Button) findViewById(R.id.startserver);
        startclient = (Button) findViewById(R.id.startclient);
        sendtestdata = (Button) findViewById(R.id.sendtestdata);
		startgame = (Button) findViewById(R.id.startgame);
		startserver.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					serverConnThread = new ServerConnThread(MainActivity.this);
                    infoip.setText(serverConnThread.getIpAddress()+":"+ serverConnThread.getPort());
                    server = true;
					/*PlayerInfo username1 = new PlayerInfo("Basilisk_game_player_1");
					ServerConnThread.sendToAll(username1);*/
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}

			}
		});

        startclient.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
				if (!server) {
					myserver = new ClientConnThread(MainActivity.this,  "192.168.1.3");
					/*PlayerInfo username1 = new PlayerInfo("Basilisk_game_player_1");
					ServerConnThread.sendToAll(username1);*/

				}
			}
        });
        sendtestdata.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
					size1 = Runtime.getRuntime().totalMemory();
					//system.out.println(size1);
					//Runtime.getRuntime().totalMemory();
					PlayerInfo username1 = new PlayerInfo("Basilisk_game_player_1");
					size2 = Runtime.getRuntime().totalMemory();
					//system.out.println(size2);
					//system.out.println("Size difference ="+ (size1 - size2));
					if(server) {
						//ServerConnThread.sendToAll(username1);
						//TODO: old code, ignore it!
					}
					else
					{
						ClientConnThread.sendToServer(username1);
						final int nbThreads = Thread.getAllStackTraces().keySet().size();
						//system.out.println("Number of threads now is : "+nbThreads);
					}
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

            }
        });
		startgame.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//TODO Startgame intent to next activity
				switchFullscreen();
				//setContentView(new SnakeGamePanel(MainActivity.this, server));
                Intent i = new Intent(MainActivity.this, GameActivity.class);
                i.putExtra("isServer", server);
                i.putExtra("username", username);
				i.putExtra("nusers", nusers);
                startActivity(i);
					/*PlayerInfo username1 = new PlayerInfo("Basilisk_game_player_1");
					ServerConnThread.sendToAll(username1);*/


			}
		});
	}

	public void switchFullscreen() {
		// requesting to turn the title OFF
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		// making it full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ServerConnThread.onDestroy();
		ClientConnThread.onDestroy();
	}

	
}