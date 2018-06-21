package com.androidsrc.snake_game.communication;


import android.content.Context;

import com.androidsrc.snake_game.MainActivity;
import com.androidsrc.snake_game.R;
import com.androidsrc.snake_game.game.HostFragment;
import com.androidsrc.snake_game.game.MainFragment;
import com.androidsrc.snake_game.snakegame.SnakeCommBuffer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;


public class ServerConnThread {
	static MainActivity activity;
	ServerSocket myserverSocket;
	static final int socketServerPORT = 8080;
	boolean ServerOn = false;
	public boolean allplayersjoined = false;
	//public String username = "player1";
	public static int userid;
	public static HashMap<Socket , Integer> socketHashMapID = new HashMap();
	public static HashMap<Socket , ObjectInputStream> socketHashMapIStream = new HashMap();
	public static HashMap<Socket , ObjectOutputStream> socketHashMapOStream = new HashMap();
	public static HashMap<Socket , String> socketHashMapUName = new HashMap();
	static Object testmessage;
	public static Context context;



	public ServerConnThread(Context mycontext) {
		Thread socketServerThread = new Thread(new SocketServerThread());
		socketServerThread.start();
		context = mycontext;
		userid = MainFragment.constants.SERVER_USER_ID;		//user id for the server is 1.
	}

	public int getPort() {
		return socketServerPORT;
	}

	//TODO: Ondestroy kill all the sockets connected to the server
	public static void onDestroy() {
		Iterator<Socket> socketIterator = ServerConnThread.socketHashMapID.keySet().iterator();
		Socket socket;
		while (socketIterator.hasNext()) {
			socket = socketIterator.next();
			if (ServerConnThread.socketHashMapID.get(socket) != null) {
				try{
					socket.close();
				}
				catch(IOException ioe){
					ioe.printStackTrace();
				}
			}
		}
	}

	private class SocketServerThread extends Thread {

		ObjectOutputStream objectOutputStream;
		ObjectInputStream objectInputStream;
		@Override
		public void run() {
			try {
				myserverSocket = new ServerSocket(socketServerPORT);
				ServerOn = true;
				while(ServerOn)
				{
					try
					{
						// Accept incoming connections.
						final Socket clientSocket = myserverSocket.accept();
						// accept() will block until a client connects to the snake_game.
						// If execution reaches this point, then it means that a client
						// socket has been accepted.
						// For each client, we will start a service thread to
						// service the client requests. This is to demonstrate a
						// Multi-Threaded snake_game. Starting a thread also lets our
						// MultiThreadedSocketServer accept multiple connections simultaneously.
						// Start a Service thread
						if(!allplayersjoined) //check to see if all the clients have connected
						{
							objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
							userid = userid + 1;	//get next user ID for each clients
							Serverlistenerthread serverthread = new Serverlistenerthread(clientSocket, objectInputStream);
							serverthread.start();
							testmessage = context.getString(R.string.clientConnAck);

							objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
							Serversenderthread serverthread2 = new Serversenderthread(clientSocket, objectOutputStream, testmessage);
							serverthread2.start();

							//username will be replaced in listner thread
							socketHashMapID.put(clientSocket,userid);
							socketHashMapIStream.put(clientSocket,objectInputStream);
							socketHashMapOStream.put(clientSocket,objectOutputStream);
							socketHashMapUName.put(clientSocket,null);

							if (socketHashMapID.size() == HostFragment.numberPlayers) {
								allplayersjoined = true;
								HostFragment.isAllPlayersConnected = true;
							}
						}
					}
					catch(IOException ioe)
					{
						//system.out.println("Exception encountered on accept. Ignoring. Stack Trace :");
						ioe.printStackTrace();
					}
				}
				try
				{
					myserverSocket.close();
					ServerOn = false;
					//system.out.println("ServerConnThread Stopped");
				}
				catch(Exception ioe)
				{
					//system.out.println("Problem stopping snake_game socket");
					System.exit(-1);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void sendToAll(Object gameObject) {
		ObjectOutputStream objectOutputStream;
		Iterator<Socket> socketIterator = ServerConnThread.socketHashMapID.keySet().iterator();
		Socket socket;
		while (socketIterator.hasNext()) {
			socket = socketIterator.next();
			if (!ServerConnThread.socketHashMapID.get(socket).equals(((SnakeCommBuffer) gameObject).userID)) {
				objectOutputStream = ServerConnThread.socketHashMapOStream.get(socket);
				Serversenderthread sendGame = new Serversenderthread(socket, objectOutputStream, gameObject);
				sendGame.start();
			}

			try {
				Thread.sleep(5);   //TODO: initially 100, changed now. verify if needed
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	public static void sendToClient(Object gameObject) {
		ObjectOutputStream objectOutputStream;
		Iterator<Socket> socketIterator = ServerConnThread.socketHashMapID.keySet().iterator();
		Socket socket;
		while (socketIterator.hasNext()) {
			socket = socketIterator.next();
			if (ServerConnThread.socketHashMapID.get(socket).equals(((SnakeCommBuffer) gameObject).userID)) {
				objectOutputStream = ServerConnThread.socketHashMapOStream.get(socket);
				Serversenderthread sendGame = new Serversenderthread(socket, objectOutputStream, gameObject);
				sendGame.start();
				return;
			}
		}
	}

	//TODO: Function to return IP address.
	public String getIpAddress() {
		String ip = "";
		try {
			Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
					.getNetworkInterfaces();
			while (enumNetworkInterfaces.hasMoreElements()) {
				NetworkInterface networkInterface = enumNetworkInterfaces
						.nextElement();
				Enumeration<InetAddress> enumInetAddress = networkInterface
						.getInetAddresses();
				while (enumInetAddress.hasMoreElements()) {
					InetAddress inetAddress = enumInetAddress
							.nextElement();

					if (inetAddress.isSiteLocalAddress()) {
						ip += "ServerConnThread running at : "
								+ inetAddress.getHostAddress();
					}
				}
			}

		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ip += "Something Wrong! " + e.toString() + "\n";
		}
		return ip;
	}
}
