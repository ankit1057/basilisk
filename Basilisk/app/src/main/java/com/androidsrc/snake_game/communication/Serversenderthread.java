package com.androidsrc.snake_game.communication;

import com.androidsrc.snake_game.MainActivity;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

class Serversenderthread extends Thread {
    Socket myclientSocket;
    MainActivity activity;
    Object message;
    static int testcount = 0;

    public Serversenderthread() {
        super();
    }

    Serversenderthread(Socket s, MainActivity activity, Object message) {
        myclientSocket = s;
        this.activity = activity;
        this.message = message;
    }

    //function used to actively update the snake_game state
    public static void updatestate(int count) {
        testcount = count;
    }

    public void run() {
        if (myclientSocket.isConnected()) {
            OutputStream outputStream;
            ObjectOutputStream objectOutputStream;
            try {
                outputStream = myclientSocket.getOutputStream();
                objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(message);
              /*  if (message instanceof Game) {
                    PlayerListFragment.gameObject = (Game) message;
                }*/
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
