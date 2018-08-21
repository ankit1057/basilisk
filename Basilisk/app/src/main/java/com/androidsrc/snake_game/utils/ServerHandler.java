package com.androidsrc.snake_game.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.androidsrc.snake_game.communication.PlayerInfo;
import com.androidsrc.snake_game.communication.ServerConnThread;
import com.androidsrc.snake_game.game.HostFragment;
import com.androidsrc.snake_game.game.MainFragment;
import com.androidsrc.snake_game.snakegame.SnakeActor;
import com.androidsrc.snake_game.snakegame.SnakeCommBuffer;

import java.util.ArrayList;

public class ServerHandler extends Handler {

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        Bundle messageData = msg.getData();
        Object gameObject = messageData.getSerializable(Constants.DATA_KEY);
        ArrayList<SnakeActor> enemysnakes = MainFragment.sgp.enemysnakes;
        SnakeCommBuffer cob;

        if (gameObject instanceof PlayerInfo) {
            PlayerInfo playerInfo = (PlayerInfo) gameObject;
            //Added to the device list for the server to know
            HostFragment.deviceList.add((PlayerInfo)gameObject);
            //PlayerListFragment.mAdapter.notifyItemInserted(PlayerListFragment.deviceList.size() - 1);
        }
        if (gameObject instanceof SnakeCommBuffer) {
            //TODO: Update the server from the slave data, also check for dead snakes
            cob = (SnakeCommBuffer) gameObject;

            //TODO: Technically empty and new will not come as it will be update in init game
            if (enemysnakes.isEmpty()) {
                enemysnakes.add(updateSnake(null, cob));
            } else {
                boolean isNew = true;
                for (int i = 0; i < enemysnakes.size(); i++) {
                    if (enemysnakes.get(i).userName.equals(cob.username)) {
                        enemysnakes.set(i, updateSnake(enemysnakes.get(i), cob));
                        //enemylist.set(i, cob);
                        isNew = false;
                    }
                }

                if (isNew) { //new user
                    enemysnakes.add(updateSnake(null, cob));
                    //enemylist.add(cob);
                }
            }

            //Update one client data to other clients
            ServerConnThread.sendToAll(gameObject);
        }

    }

    private SnakeActor updateSnake(SnakeActor snakeIn, SnakeCommBuffer cbuff) {

        if (snakeIn == null) {
            snakeIn = new SnakeActor(cbuff.nextPos.x, cbuff.nextPos.y,
                    cbuff.username, MainFragment.constants.colorLUT.get(cbuff.userID));
        }
        snakeIn.tailPos = cbuff.snakePos;
        snakeIn.setPoint(cbuff.nextPos);
        snakeIn.setVelocity(cbuff.velocity);

        return snakeIn;
    }
}
