package com.androidsrc.snake_game;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

class ClientsenderThread extends Thread {
    Socket myserverSocket;
    MainActivity activity;
    Object message;
    public static boolean isActive = true;

    public ClientsenderThread() {
        super();
    }

    ClientsenderThread(Socket s, Object message) {
        myserverSocket = s;
        this.message = message;
    }

    public void run() {
        OutputStream outputStream;
        ObjectOutputStream objectOutputStream;
        if (myserverSocket.isConnected()) {
            try {
                if (isActive) {
                    outputStream = myserverSocket.getOutputStream();
                    objectOutputStream = new ObjectOutputStream(outputStream);
                    objectOutputStream.writeObject(message);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}

