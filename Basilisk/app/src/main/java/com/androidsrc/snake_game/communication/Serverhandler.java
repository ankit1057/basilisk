package com.androidsrc.snake_game.communication;


import com.androidsrc.snake_game.MainActivity;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;


public class Serverhandler {
	static MainActivity activity;
	ServerSocket myserverSocket;
	static final int socketServerPORT = 8080;
	boolean ServerOn = false;
	boolean allplayersjoined = false;
	static int num_players = 3;
	public String username = "player1";
	public static HashMap<Socket , String> clientsockethashmap = new HashMap();
	static Object testmessage = "Hi from snake_game";


	public Serverhandler(MainActivity activity) {
		this.activity = activity;
		Thread socketServerThread = new Thread(new SocketServerThread());
		socketServerThread.start();
	}

	public int getPort() {
		return socketServerPORT;
	}

	//TODO: Ondestroy kill all the sockets connected to the server
	public static void onDestroy() {
		Iterator<Socket> socketIterator = Serverhandler.clientsockethashmap.keySet().iterator();
		Socket socket;
		while (socketIterator.hasNext()) {
			socket = socketIterator.next();
			if (Serverhandler.clientsockethashmap.get(socket) != null) {
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
							username = username + "1";
							Serverlistenerthread serverthread = new Serverlistenerthread(clientSocket,activity);
							serverthread.start();
							Serversenderthread serverthread2 = new Serversenderthread(clientSocket,activity, testmessage);
							serverthread2.start();
							//TODO: sockethashmap can store the player names. null can be replaced with playernames
							clientsockethashmap.put(clientSocket,username );
							//Number of threads 
							final int nbThreads = Thread.getAllStackTraces().keySet().size();
							activity.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									activity.msg.append("\n Socket accepted from client number :" + clientSocket.getInetAddress() + "Number of threads now : " + nbThreads);
								}
							});
							if (clientsockethashmap.size() == num_players) {
								allplayersjoined = true;
							}
						}

					}
					catch(IOException ioe)
					{
						System.out.println("Exception encountered on accept. Ignoring. Stack Trace :");
						ioe.printStackTrace();
					}
				}
				try
				{
					myserverSocket.close();
					ServerOn = false;
					System.out.println("Serverhandler Stopped");
				}
				catch(Exception ioe)
				{
					System.out.println("Problem stopping snake_game socket");
					System.exit(-1);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void sendToAll(Object message) {
		Iterator<Socket> socketIterator = Serverhandler.clientsockethashmap.keySet().iterator();
		Socket socket;
		while (socketIterator.hasNext()) {
			socket = socketIterator.next();
			if (Serverhandler.clientsockethashmap.get(socket) != null) {
				Serversenderthread sendtoall = new Serversenderthread(socket,activity, message);
				sendtoall.start();
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
						ip += "Serverhandler running at : "
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
