package com.androidsrc.snake_game;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	Serverhandler serverhandler;
	TextView infoip, msg;
	Button startserver, startclient, sendtestdata;
	Clienthandler myserver;
	long size1 =0 ;
	long size2 =0 ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		infoip = (TextView) findViewById(R.id.infoip);
		msg = (TextView) findViewById(R.id.msg);
		startserver = (Button) findViewById(R.id.startserver);
        startclient = (Button) findViewById(R.id.startclient);
        sendtestdata = (Button) findViewById(R.id.sendtestdata);

		startserver.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try {
					serverhandler = new Serverhandler(MainActivity.this);
                    infoip.setText(serverhandler.getIpAddress()+":"+ serverhandler.getPort());
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

                    myserver = new Clienthandler(MainActivity.this,"192.168.1.3") ;
					PlayerInfo username1 = new PlayerInfo("Basilisk_game_player_1");
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
					Serverhandler.sendToAll(username1);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

            }
        });
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Serverhandler.onDestroy();
		Clienthandler.onDestroy();
	}

	
}