package com.androidsrc.snake_game.communication;

import android.app.Instrumentation;
import android.os.Bundle;
import android.os.Message;

import com.androidsrc.snake_game.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import com.androidsrc.snake_game.game.MainFragment;
import com.androidsrc.snake_game.snakegame.SnakeCommBuffer;

class Clientlistenerthread extends Thread
{
    Socket myserverSocket;
    MainActivity activity;
    static Boolean ServerOn;
    Instrumentation instrumentation;
    //SnakeGamePanel clientsnake;

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
                        System.out.println("Object read is - " + ((SnakeCommBuffer) serverObject).nextPos.x
                                + ((SnakeCommBuffer) serverObject).nextPos.y );
                        data.putSerializable("GameDataBuff", (SnakeCommBuffer)serverObject);

                        Message msg = new Message();
                        msg.setData(data);
                        MainFragment.clientHandler.sendMessage(msg);

                        //clientsnake = new SnakeGamePanel(this.activity.getApplicationContext(), false);
                        //clientsnake.clientUpdate((SnakeCommBuffer)serverObject);
                        //System.out.println("Object read is - " + ((PlayerInfo) serverObject).username);
                    }
                    if(serverObject instanceof Bundle) {
                        //data.putSerializable("Server_object_read", (PlayerInfo) serverObject);
                        //System.out.println(data);
                        SnakeCommBuffer buff = (SnakeCommBuffer)((Bundle) serverObject).getSerializable("buffer");
                        System.out.println("xfer_cl_rx_data");
                        System.out.println("Object read is - " + buff.nextPos.x
                                + buff.nextPos.y );
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