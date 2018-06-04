package com.androidsrc.snake_game.communication;

import android.app.Instrumentation;
import android.os.Bundle;

import com.androidsrc.snake_game.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

import com.androidsrc.snake_game.snakegame.SnakeCommBuffer;

class Clientlistenerthread extends Thread
{
    Socket myserverSocket;
    MainActivity activity;
    static Boolean ServerOn;
    Instrumentation instrumentation;

    public Clientlistenerthread()
    {
        super();
    }

    Clientlistenerthread(Socket s, MainActivity activity)
    {
        myserverSocket = s;
        this.activity = activity;
    }

    @Override
    public void run() {
        try {
            while (true) {
                ObjectInputStream objectInputStream;
                InputStream inputStream = null;
                inputStream = myserverSocket.getInputStream();
                objectInputStream = new ObjectInputStream(inputStream);
                Bundle data = new Bundle();
                Object serverObject = (Object) objectInputStream.readObject();
                if (serverObject != null) {
                    if(serverObject instanceof PlayerInfo) {
                        //data.putSerializable("Server_object_read", (PlayerInfo) serverObject);
                        //System.out.println(data);
                        System.out.println("Object read is - " + ((PlayerInfo) serverObject).username );
                        //System.out.println("Object read is - " + ((PlayerInfo) serverObject).username);
                    }
                    if(serverObject instanceof SnakeCommBuffer) {
                        //data.putSerializable("Server_object_read", (PlayerInfo) serverObject);
                        //System.out.println(data);
                        System.out.println("xfer_cl_rx_data");
                        System.out.println("Object read is - " + ((SnakeCommBuffer) serverObject).nextPosX
                                + ((SnakeCommBuffer) serverObject).nextPosY );
                        //System.out.println("Object read is - " + ((PlayerInfo) serverObject).username);
                    }
                    if(serverObject instanceof Bundle) {
                        //data.putSerializable("Server_object_read", (PlayerInfo) serverObject);
                        //System.out.println(data);
                        SnakeCommBuffer buff = (SnakeCommBuffer)((Bundle) serverObject).getSerializable("buffer");
                        System.out.println("xfer_cl_rx_data");
                        System.out.println("Object read is - " + buff.nextPosX
                                + buff.nextPosY );
                        //System.out.println("Object read is - " + ((PlayerInfo) serverObject).username);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}