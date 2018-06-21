package com.androidsrc.snake_game.communication;

import android.app.Instrumentation;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.androidsrc.snake_game.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

import com.androidsrc.snake_game.R;
import com.androidsrc.snake_game.game.JoinGameFragment;
import com.androidsrc.snake_game.game.MainFragment;
import com.androidsrc.snake_game.snakegame.SnakeCommBuffer;

class Clientlistenerthread extends Thread
{
    Socket myserverSocket;
    MainActivity activity;
    static Boolean ServerOn;
    Instrumentation instrumentation;
    Context context;
    //SnakeGamePanel clientsnake;

    ObjectInputStream objectInputStream;
    InputStream inputStream = null;
    Object serverObject;


    public Clientlistenerthread(Context mycontext)
    {
        super();
    }

    Clientlistenerthread(Context mycontext, Socket s, ObjectInputStream objectInputStream)
    {
        this.context = mycontext;
        this.myserverSocket = s;
        this.objectInputStream = objectInputStream;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Bundle data = new Bundle();
                serverObject = (Object) this.objectInputStream.readObject();
                if (serverObject != null) {
                    if(serverObject instanceof PlayerInfo) {
                        //data.putSerializable("Server_object_read", (PlayerInfo) serverObject);
                        ////system.out.println(data);
                        //system.out.println("Object read is - " + ((PlayerInfo) serverObject).username );
                        ////system.out.println("Object read is - " + ((PlayerInfo) serverObject).username);
                    }
                    else if(serverObject instanceof SnakeCommBuffer) {
                        //data.putSerializable("Server_object_read", (PlayerInfo) serverObject);
                        ////system.out.println(data);
                        //system.out.println("xfer_cl_rx_data" + ((SnakeCommBuffer) serverObject).snakePos.get(0).x +
                        //        ((SnakeCommBuffer) serverObject).snakePos.get(0).y + ((SnakeCommBuffer) serverObject).snakePos.get(1).x +
                        //        ((SnakeCommBuffer) serverObject).snakePos.get(1).y + "size" + ((SnakeCommBuffer) serverObject).snakePos.size());

                        data.putSerializable(MainFragment.constants.DATA_KEY, (SnakeCommBuffer)serverObject);

                        Message msg = new Message();
                        msg.setData(data);
                        MainFragment.clientHandler.sendMessage(msg);

                        //clientsnake = new SnakeGamePanel(this.activity.getApplicationContext(), false);
                        //clientsnake.clientUpdate((SnakeCommBuffer)serverObject);
                        ////system.out.println("Object read is - " + ((PlayerInfo) serverObject).username);
                    }
                    else if(serverObject instanceof Bundle) {
                        //data.putSerializable("Server_object_read", (PlayerInfo) serverObject);
                        ////system.out.println(data);
                        SnakeCommBuffer buff = (SnakeCommBuffer)((Bundle) serverObject).getSerializable("buffer");
                        //system.out.println("xfer_cl_bndle");
                        //system.out.println("Object read is - " + buff.nextPos.x
                        //        + buff.nextPos.y );
                        ////system.out.println("Object read is - " + ((PlayerInfo) serverObject).username);
                    }
                    else if(serverObject instanceof String) {
                        final String message = (String) serverObject;

                        //TODO: check the received string before processing
                        if(message.equals(context.getString(R.string.clientConnAck))) {
                            JoinGameFragment.isClientConnected = true;
                        }

                        //TODO: Remove this later
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                            }
                        });
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