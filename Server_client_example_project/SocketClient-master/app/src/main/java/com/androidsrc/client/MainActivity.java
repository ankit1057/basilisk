package com.androidsrc.client;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.androidsrc.client.Clienthandler;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

public class MainActivity extends Activity {

	TextView response;
	EditText editTextAddress, editTextPort;
	Button buttonConnect, buttonClear, sendtestdata;
	Clienthandler myserver;
	Clienthandler.ClientsenderThread myserver2;
	String ipaddress = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		editTextAddress = (EditText) findViewById(R.id.addressEditText);
		editTextPort = (EditText) findViewById(R.id.portEditText);
		buttonConnect = (Button) findViewById(R.id.connectButton);
		buttonClear = (Button) findViewById(R.id.clearButton);
		sendtestdata = (Button) findViewById(R.id.senddata);
		response = (TextView) findViewById(R.id.responseTextView);

		buttonConnect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ipaddress = editTextAddress.getText().toString();
				myserver = new Clienthandler(MainActivity.this,ipaddress) ;
			}
		});

		sendtestdata.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					Clienthandler.sendtestdata();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}

			}
		});
		buttonClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				response.setText("");
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Clienthandler.onDestroy();
	}

}
