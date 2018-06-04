package com.androidsrc.snake_game;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.androidsrc.snake_game.communication.Clienthandler;
import com.androidsrc.snake_game.communication.PlayerInfo;
import com.androidsrc.snake_game.communication.Serverhandler;
import com.androidsrc.snake_game.snakegame.SnakeGamePanel;


public class MainActivity extends Activity {

	Serverhandler serverhandler;
	TextView infoip;
	public TextView msg;
	Button startserver, startclient, sendtestdata, startgame;
	Clienthandler myserver;
	long size1 =0 ;
	long size2 =0 ;
	boolean server = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
					serverhandler = new Serverhandler(MainActivity.this);
                    infoip.setText(serverhandler.getIpAddress()+":"+ serverhandler.getPort());
                    server = true;
					/*PlayerInfo username1 = new PlayerInfo("Basilisk_game_player_1");
					Serverhandler.sendToAll(username1);*/
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
					myserver = new Clienthandler(MainActivity.this, "192.168.1.3");
					/*PlayerInfo username1 = new PlayerInfo("Basilisk_game_player_1");
					Serverhandler.sendToAll(username1);*/

				}
			}
        });
        sendtestdata.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
					size1 = Runtime.getRuntime().totalMemory();
					System.out.println(size1);
					//Runtime.getRuntime().totalMemory();
					PlayerInfo username1 = new PlayerInfo("Basilisk_game_player_1");
					size2 = Runtime.getRuntime().totalMemory();
					System.out.println(size2);
					System.out.println("Size difference ="+ (size1 - size2));
					if(server) {
						Serverhandler.sendToAll(username1);
					}
					else
					{
						Clienthandler.sendToServer(username1);
						final int nbThreads = Thread.getAllStackTraces().keySet().size();
						System.out.println("Number of threads now is : "+nbThreads);
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
				setContentView(new SnakeGamePanel(MainActivity.this, server));
					/*PlayerInfo username1 = new PlayerInfo("Basilisk_game_player_1");
					Serverhandler.sendToAll(username1);*/


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
		Serverhandler.onDestroy();
		Clienthandler.onDestroy();
	}

	
}