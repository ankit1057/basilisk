package com.androidsrc.snake_game.communication;

import com.androidsrc.snake_game.MainActivity;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

class ClientsenderThread extends Thread {
    Socket myserverSocket;
    MainActivity activity;
    Object message;
    ObjectOutputStream objectOutputStream;
    public static boolean isActive = true;

    public ClientsenderThread() {
        super();
    }

    ClientsenderThread(Socket s, ObjectOutputStream objectOutputStream, Object message) {
        this.myserverSocket = s;
        this.message = message;
        this.objectOutputStream = objectOutputStream;
    }

    public void run() {
        //OutputStream outputStream;
        //ObjectOutputStream objectOutputStream;
        if (this.myserverSocket.isConnected()) {
            try {
                if (isActive) {
                    //outputStream = myserverSocket.getOutputStream();
                    //objectOutputStream = new ObjectOutputStream(outputStream);
                    this.objectOutputStream.writeObject(message);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}

