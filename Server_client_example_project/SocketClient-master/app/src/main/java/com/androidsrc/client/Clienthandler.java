package com.androidsrc.client;

import android.view.View;
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

public class Clienthandler {
    MainActivity activity;
    boolean ServerOn = true;
    String dstAddress;
    int dstPort = 8080;
    TextView ipaddress;
    static int testdatacount =0 ;
    static int clientthreadcount = 0 ;
    static Socket clientsendersocket;
    static HashMap<String, Socket> sockethashmap = new HashMap<String, Socket>();


    public Clienthandler(MainActivity activity, String ip) {
        this.activity = activity;
        dstAddress = ip;
        Thread clienthandler = new Thread(new clienthandlerthread());
        clienthandler.start();
    }

    public int getPort() {
        return dstPort;
    }

    public static Socket getsocket(String key) {
        return sockethashmap.get(key);
    }

    public static void onDestroy() {
        if (clientsendersocket != null) {
            try {
                clientsendersocket.close();
                clientthreadcount = 0;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private class clienthandlerthread extends Thread {
        @Override
        public void run() {
            try {
                clientsendersocket = new Socket(dstAddress,dstPort);
                while(ServerOn) {
                    // For each client, we will start a service thread to
                    // service the client requests. This is to demonstrate a
                    // Multi-Threaded server. Starting a thread also lets our
                    // MultiThreadedSocketServer accept multiple connections simultaneously.
                    // Start a Service thread
                    if(clientthreadcount < 1) {
                        ClientsenderThread cliThread = new ClientsenderThread(clientsendersocket);
                        sockethashmap.put("Server",clientsendersocket);
                        //Number of threads
                        clientthreadcount = 1;
                        final int nbThreads = Thread.getAllStackTraces().keySet().size();
                        activity.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                activity.response.append("\n Client thread created for:" + clientsendersocket.getInetAddress() + " Number of threads now : " + nbThreads);
                            }
                        });
                        cliThread.start();
                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    class ClientsenderThread extends Thread
    {
        Socket myserverSocket;
        boolean m_bRunThread = true;

        public ClientsenderThread()
        {
            super();
        }

        ClientsenderThread(Socket s)
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
                in = new BufferedReader(new InputStreamReader(myserverSocket.getInputStream()));
                out = new PrintWriter(new OutputStreamWriter(myserverSocket.getOutputStream()));
                String serverdata = "";
                // At this point, we can read for input and reply with appropriate output.
                // Run in a loop until m_bRunThread is set to false
                while(m_bRunThread)
                {
                    // read incoming stream
                    try
                    {
                        final String clientCommand = in.readLine();
                        if(clientCommand != null) {
                            System.out.println("Server Says :" + clientCommand);
                            activity.runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    activity.response.append("\n Server Says :" + clientCommand);
                                }
                            });
                            serverdata = clientCommand;
                        }
                    }
                    catch(NullPointerException npe)
                    {
                        System.out.println("Server Says fuck you. No data on ip stream");
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
                        System.out.print("Stopping client thread  : ");
                    } else if(serverdata.equalsIgnoreCase("end")) {
                        // Special command. Quit this thread and Stop the Server
                        m_bRunThread = false;
                        System.out.print("Stopping client thread : ");
                        ServerOn = false;
                    } else {
                        // Process it
                        //out.println("Server Says : " + serverdata);
                        //out.flush();
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
                    System.out.println("Socket closed...Stopped");
                }
                catch(IOException ioe)
                {
                    ioe.printStackTrace();
                }
            }
        }

    }
    public static void sendtestdata()
    {
        testdatacount++;
        Socket myserverSocket = sockethashmap.get("Server");
        PrintWriter outtest = null;
        try{
            outtest = new PrintWriter(new OutputStreamWriter(myserverSocket.getOutputStream()));
            outtest.println("Test message number : " + testdatacount);
            outtest.flush();

        }
        catch(Exception e)
        {
            e.printStackTrace();
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