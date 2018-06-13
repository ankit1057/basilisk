package com.androidsrc.snake_game.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import com.androidsrc.snake_game.game.MainFragment;
import com.androidsrc.snake_game.snakegame.PointsXY;
import com.androidsrc.snake_game.snakegame.SnakeActor;
import com.androidsrc.snake_game.snakegame.SnakeCommBuffer;

import java.util.ArrayList;

public class ClientHandler extends Handler {

    Bundle messageData;

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        ArrayList<SnakeCommBuffer> enemylist = MainFragment.sgp.enemies; //TODO: If ArrayList is ref
        ArrayList<SnakeActor> enemysnakes = MainFragment.sgp.enemysnakes;
        messageData = msg.getData();
        Object clientObject = messageData.getSerializable(MainFragment.constants.DATA_KEY);
        SnakeCommBuffer cob;

        if (clientObject instanceof SnakeCommBuffer) {
            cob = (SnakeCommBuffer) clientObject;
            //System.out.println("MessageHandlerOut Client"+cob.nextPos.x+cob.nextPos.y);

            //TODO: Verify below comment from server. Caution! may reset the client!!
            //If the received username is for the current user, then that is start of the game
            //Also assumed that server sent all the enemy snake data before this
            //Note: the first data is sent in next pos, ignoring the tail pos!!
            if(cob.username.equals(MainFragment.sgp.username)) {
                //TODO: clear sgp upon restart game!
                MainFragment.sgp.snake = updateSnake(null, cob);
                //Ignoring the tail pos as it will be updated in snakeactor.
                //TODO: set server according for the above comment
            }
            else {
                //Enemy update

                //cob.snakePos.add(new PointsXY(125,125));
                //MainFragment.sgp.clientUpdate(cob);
                if (enemysnakes.isEmpty()) {
                    enemysnakes.add(updateSnake(null, cob));

                    //TODO: Remove list which is dummy
                    //enemylist.add(cob);
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
            }
        }
    }

    private SnakeActor updateSnake(SnakeActor snakeIn, SnakeCommBuffer cbuff) {

        if (snakeIn == null) {
            snakeIn = new SnakeActor(cbuff.nextPos.x, cbuff.nextPos.y, cbuff.username, cbuff.userID);
        }
        snakeIn.tailPos = cbuff.snakePos;
        snakeIn.setPoint(cbuff.nextPos);
        snakeIn.setVelocity(cbuff.velocity);

        return snakeIn;
    }

}

