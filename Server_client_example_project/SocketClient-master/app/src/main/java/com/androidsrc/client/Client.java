package com.androidsrc.client;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import android.os.AsyncTask;
import android.widget.TextView;

public class Client extends AsyncTask<Void, Void, Void> {
    MainActivity activity;
	String dstAddress;
	int dstPort;
    int cnt =0;
	boolean ServerOn;
    Socket clientsocket;
	String response = "";
	String testreply = "Client Handshake. Hello";
    String testreply2 = "hello from client for some data in input stream";
	TextView textResponse;

	Client(String addr, int port,TextView textResponse) {
		dstAddress = addr;
		dstPort = port;
		this.textResponse=textResponse;
	}

	@Override
	protected Void doInBackground(Void... arg0) {

        Socket socket = null;
            try {
                socket = new Socket(dstAddress, dstPort);
                ServerOn = true;
                OutputStream outputStream;
                outputStream = socket.getOutputStream();
                PrintStream printStream = new PrintStream(outputStream);
                printStream.print(testreply);
                printStream.close();
                clientsocket = socket;
                Clientreplythread cliThread = new Clientreplythread(clientsocket);
                cliThread.start();

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "IOException: " + e.toString();
            } finally {
                if (socket == null) {
            try {
                socket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
            return null;
        }


	@Override
	protected void onPostExecute(Void result) {
		textResponse.setText(response);
		super.onPostExecute(result);
	}

    class Clientreplythread extends Thread
    {
        Socket myserverSocket;
        boolean m_bRunThread = true;

        public Clientreplythread()
        {
            super();
        }

        Clientreplythread(Socket s)
        {
            myserverSocket = s;
        }

        public void run()
        {
            // Obtain the input stream and the output stream for the socket
            // A good practice is to encapsulate them with a BufferedReader
            // and a PrintWriter as shown below.
            BufferedReader in = null;
            PrintWriter out = null;
            // Print out details of this connection
            System.out.println("Accepted Server Address - " + myserverSocket.getInetAddress().getHostName());
            try
            {
                try {
                    if(myserverSocket.getInputStream() != null) {
                        in = new BufferedReader(new InputStreamReader(myserverSocket.getInputStream()));
                        out = new PrintWriter(new OutputStreamWriter(myserverSocket.getOutputStream()));
                    }
                }
                catch(NullPointerException e)
                {
                    e.printStackTrace();
                }
                String serverdata = "";
                // At this point, we can read for input and reply with appropriate output.

                // Run in a loop until m_bRunThread is set to false
                while(m_bRunThread)
                {
                    // read incoming stream
                    try
                    {
                        final String serverCommand = in.readLine();
                        if(serverCommand != null)
                        {
                            serverdata = serverCommand;
                        }
                    }
                    catch(NullPointerException npe)
                    {
                        System.out.println("Server Says fuck you");
                    }
                    if(!ServerOn)
                    {
                        // Special command. Quit this thread
                        System.out.print("Server has already stopped");
                        out.println("Server has already stopped");
                        out.flush();
                        m_bRunThread = false;

                    }
                    if(serverdata.equalsIgnoreCase("quit")) {
                        // Special command. Quit this thread
                        m_bRunThread = false;
                        System.out.print("Stopping client thread for client : ");
                    } else if(serverdata.equalsIgnoreCase("end")) {
                        // Special command. Quit this thread and Stop the Server
                        m_bRunThread = false;
                        System.out.print("Stopping client thread for client : ");
                        ServerOn = false;
                    } else {
                        // Process it
                        out.println("Client Says : " + serverdata);
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
                    myserverSocket.close();
                    System.out.println("...Stopped");
                }
                catch(IOException ioe)
                {
                    ioe.printStackTrace();
                }
            }
        }
        public void sendtestdata()
        {
            cnt++;
            PrintWriter outtest = null;
            try{
                outtest = new PrintWriter(new OutputStreamWriter(myserverSocket.getOutputStream()));
                outtest.println("Test message" + cnt);
                outtest.flush();

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

}
