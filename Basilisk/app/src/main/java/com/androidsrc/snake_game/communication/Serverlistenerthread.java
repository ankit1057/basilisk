package com.androidsrc.snake_game.communication;

import android.os.Bundle;

import com.androidsrc.snake_game.MainActivity;
import com.androidsrc.snake_game.snakegame.SnakeCommBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

class Serverlistenerthread extends Thread {
    Socket myclientSocket;
    MainActivity activity;

    public Serverlistenerthread() {
        super();
    }

    Serverlistenerthread(Socket s, MainActivity activity) {
        myclientSocket = s;
        this.activity = activity;
    }

    public void run() {
        while (myclientSocket.isConnected()) {
            ObjectInputStream objectInputStream;
            try {
                InputStream inputStream = null;
                inputStream = myclientSocket.getInputStream();
                objectInputStream = new ObjectInputStream(inputStream);
                Object gameObject;
                Bundle data = new Bundle();
                gameObject = objectInputStream.readObject();
                if(gameObject instanceof PlayerInfo) {
                    //data.putSerializable("Server_object_read", (PlayerInfo) serverObject);
                    //System.out.println(data);
                    System.out.println("Object read is - " + ((PlayerInfo) gameObject).username );
                }
                else if(gameObject instanceof SnakeCommBuffer) {
                    //data.putSerializable("Server_object_read", (PlayerInfo) serverObject);
                    //System.out.println(data);
                    System.out.println("Object SnakeCommBuffer read is - " + ((SnakeCommBuffer) gameObject).velocity.getXSpeed());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }
}
