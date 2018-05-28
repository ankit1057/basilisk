package com.androidsrc.snake_game;

import android.app.Instrumentation;
import android.os.Bundle;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

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
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}