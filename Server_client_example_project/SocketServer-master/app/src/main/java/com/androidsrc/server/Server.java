package com.androidsrc.server;

import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;

public class Server {
	MainActivity activity;
	ServerSocket myserverSocket;
	static final int socketServerPORT = 8080;
	boolean ServerOn = true;
	int clientnumber = 0;
	HashMap<String, Socket> clientsockethashmap = new HashMap<String, Socket>();

	public Server(MainActivity activity) {
		this.activity = activity;
		Thread socketServerThread = new Thread(new SocketServerThread());
		socketServerThread.start();
	}

	public int getPort() {
		return socketServerPORT;
	}

	public void onDestroy() {
		if (myserverSocket != null) {
			try {
				myserverSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private class SocketServerThread extends Thread {
		@Override
		public void run() {
			try {
				myserverSocket = new ServerSocket(socketServerPORT);
				while(ServerOn)
				{
					try
					{
						// Accept incoming connections.
						clientnumber++;
						Socket clientSocket = myserverSocket.accept();
						// accept() will block until a client connects to the server.
						// If execution reaches this point, then it means that a client
						// socket has been accepted.
						// For each client, we will start a service thread to
						// service the client requests. This is to demonstrate a
						// Multi-Threaded server. Starting a thread also lets our
						// MultiThreadedSocketServer accept multiple connections simultaneously.
						// Start a Service thread
						ClientServiceThread cliThread = new ClientServiceThread(clientSocket);
						clientsockethashmap.put("Client"+ clientnumber ,clientSocket);
						//Number of threads
						final int nbThreads =  Thread.getAllStackTraces().keySet().size();
						activity.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								activity.msg.append("\n Number of threads now : " + nbThreads);
							}
						});
						cliThread.start();

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
					System.out.println("Server Stopped");
				}
				catch(Exception ioe)
				{
					System.out.println("Problem stopping server socket");
					System.exit(-1);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	class ClientServiceThread extends Thread
	{
		Socket myClientSocket;
		boolean m_bRunThread = true;

		public ClientServiceThread()
		{
			super();
		}

		ClientServiceThread(Socket s)
		{
			myClientSocket = s;
		}

		public void run()
		{
			// Obtain the input stream and the output stream for the socket
			// A good practice is to encapsulate them with a BufferedReader
			// and a PrintWriter as shown below.
			BufferedReader in = null;
			PrintWriter out = null;
			// Print out details of this connection
			System.out.println("Accepted Client Address - " + myClientSocket.getInetAddress().getHostName());
			try
			{
				in = new BufferedReader(new InputStreamReader(myClientSocket.getInputStream()));
				out = new PrintWriter(new OutputStreamWriter(myClientSocket.getOutputStream()));
				String clientdata = "";
				// At this point, we can read for input and reply with appropriate output.
				// Run in a loop until m_bRunThread is set to false
				while(m_bRunThread)
				{
					// read incoming stream
					try
					{
						final String clientCommand = in.readLine();
						if(clientCommand != null) {
							System.out.println("Client Says :" + clientCommand);
							activity.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									activity.msg.append("\n Client Says :" + clientCommand);
								}
							});
							clientdata = clientCommand;
						}
					}
					catch(NullPointerException npe)
					{
						System.out.println("Client Says fuck you");
					}
					if(!ServerOn)
					{
						// Special command. Quit this thread
						System.out.print("Server has already stopped");
						out.println("Server has already stopped");
						out.flush();
						m_bRunThread = false;
					}
					if(clientdata.equalsIgnoreCase("quit")) {
						// Special command. Quit this thread
						m_bRunThread = false;
						System.out.print("Stopping client thread for client : ");
					} else if(clientdata.equalsIgnoreCase("end")) {
						// Special command. Quit this thread and Stop the Server
						m_bRunThread = false;
						System.out.print("Stopping client thread for client : ");
						ServerOn = false;
					} else {
						// Process it
						out.println("Server Says : " + clientdata);
						out.flush();
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				// Clean up
				try
				{
					in.close();
					out.close();
					myClientSocket.close();
					System.out.println("...Stopped");
				}
				catch(IOException ioe)
				{
					ioe.printStackTrace();
				}
			}
		}
	}

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
						ip += "Server running at : "
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
