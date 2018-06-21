package com.androidsrc.snake_game.communication;

import android.os.Bundle;
import android.os.Message;

import com.androidsrc.snake_game.MainActivity;
import com.androidsrc.snake_game.game.HostFragment;
import com.androidsrc.snake_game.game.MainFragment;
import com.androidsrc.snake_game.snakegame.SnakeCommBuffer;
import com.androidsrc.snake_game.utils.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

class Serverlistenerthread extends Thread {
    Socket myclientSocket;
    MainActivity activity;
    static Constants constants;
    ObjectInputStream objectInputStream;
    InputStream inputStream = null;
    Object gameObject;

    public Serverlistenerthread() {
        super();
    }

    Serverlistenerthread(Socket s, ObjectInputStream objectInputStream) {
        this.myclientSocket = s;
        this.constants = MainFragment.constants;
        this.objectInputStream = objectInputStream;
    }

    public void run() {
        while (this.myclientSocket.isConnected()) {
            try {
                Bundle data = new Bundle();
                gameObject = this.objectInputStream.readObject();

                if (gameObject != null) {
                    if (gameObject instanceof PlayerInfo) {
                        PlayerInfo plinfo = (PlayerInfo) gameObject;
                        //update the hashmap name from client
                        ServerConnThread.socketHashMapUName.put(this.myclientSocket, plinfo.username);
                        //update the plinfo userid from hashmap
                        plinfo.userid = ServerConnThread.socketHashMapID.get(this.myclientSocket);

                        data.putSerializable(constants.DATA_KEY, plinfo);
                        data.putInt(constants.ACTION_KEY, constants.PLAYER_LIST_UPDATE);
                    } else if (gameObject instanceof SnakeCommBuffer) {

                        //system.out.println("xfer_cl_tx_data" + ((SnakeCommBuffer) gameObject).snakePos.get(0).x +
                        //        ((SnakeCommBuffer) gameObject).snakePos.get(0).y + ((SnakeCommBuffer) gameObject).snakePos.get(1).x +
                        //        ((SnakeCommBuffer) gameObject).snakePos.get(1).y + "Size" + ((SnakeCommBuffer) gameObject).snakePos.size());

                        data.putSerializable(constants.DATA_KEY, (SnakeCommBuffer) gameObject);
                    }
                    else if (gameObject instanceof String) {
                        data.putSerializable(constants.MSG_KEY, (String) gameObject);
                    }
                    Message msg = new Message();
                    msg.setData(data);
                    HostFragment.serverHandler.sendMessage(msg);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }
}
