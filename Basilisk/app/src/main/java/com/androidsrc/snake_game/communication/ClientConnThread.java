package com.androidsrc.snake_game.communication;

import android.content.Context;

import com.androidsrc.snake_game.MainActivity;
import com.androidsrc.snake_game.R;
import com.androidsrc.snake_game.game.MainFragment;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;

public class ClientConnThread {
    static int numberofsockets = 1;
    MainActivity activity;
    boolean ServerOn;
    String dstAddress;
    int dstPort = 8080;
    static int clientthreadcount = 0 ;
    static Socket clientsendersocket;
    static HashMap<String, Socket> sockethashmap = new HashMap<String, Socket>();
    static HashMap<Socket,Boolean> activesocketsinfo = new HashMap<Socket, Boolean>();
    Object testmessage;
    static Context context;


    public ClientConnThread(Context mycontext, String ip) {
        dstAddress = ip;
        Thread clienthandler = new Thread(new clienthandlerthread());
        clienthandler.start();
        context = mycontext;
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
                //if the socket is closed reset all the parameters to default
                clientthreadcount = 0;
                numberofsockets = 0;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private class clienthandlerthread extends Thread {
        private PlayerInfo playerInfo;

        @Override
        public void run() {
            //primary check to see if the socket is already open or not
            while (numberofsockets <= 1) {
                try {
                    clientsendersocket = new Socket(dstAddress, dstPort);
                    //Connection is ok. Update the sockets info hashmap and limiting parameters
                    numberofsockets++; //don't have more than one server socket open for the client in the snake_game
                    sockethashmap.put("Server", clientsendersocket);
                    ServerOn = true;
                    activesocketsinfo.put(clientsendersocket, ServerOn);
                    playerInfo = new PlayerInfo(MainFragment.userName.getText().toString());
                    while (ServerOn) {
                        if (clientthreadcount < 2) {
                            testmessage = context.getString(R.string.clientConnReq);
                            ClientsenderThread cliThread = new ClientsenderThread(clientsendersocket, playerInfo); //TODO: check this later
                            Clientlistenerthread cliThread2 = new Clientlistenerthread(context, clientsendersocket);
                            //Number of threads
                            clientthreadcount = 2;
                            final int nbThreads = Thread.getAllStackTraces().keySet().size();
//                            activity.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    activity.msg.append("\n Client thread created for:" + clientsendersocket.getInetAddress() + " Number of threads now : " + nbThreads);
//                                }
//                            });
                            cliThread.start();
                            cliThread2.start();
                        }
                    }
                } catch (IOException e) {
                    // Exception that socket has a problem. Update the acitvesockets hashmap
                    ServerOn = false;
                    activesocketsinfo.put(clientsendersocket, ServerOn);
                    e.printStackTrace();
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
    public static void sendToServer(Object gameObject) {
        Socket socket = sockethashmap.get("Server");
        ClientsenderThread sendtoserver = new ClientsenderThread(socket, gameObject);
        sendtoserver.start();
    }
}