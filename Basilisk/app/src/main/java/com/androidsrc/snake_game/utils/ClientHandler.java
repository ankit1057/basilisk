package com.androidsrc.snake_game.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import com.androidsrc.snake_game.game.MainFragment;
import com.androidsrc.snake_game.snakegame.PointsXY;
import com.androidsrc.snake_game.snakegame.SnakeCommBuffer;

import java.util.ArrayList;

public class ClientHandler extends Handler {


    Bundle messageData;

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        ArrayList<SnakeCommBuffer> enemylist = MainFragment.sgp.enemies; //TODO: If ArrayList is ref
        messageData = msg.getData();
        Object clientObject = messageData.getSerializable("GameDataBuff");
        SnakeCommBuffer cob;

        if (clientObject instanceof SnakeCommBuffer) {
            cob = (SnakeCommBuffer) clientObject;
            System.out.println("MessageHandlerOut Client"+cob.nextPos.x+cob.nextPos.y);

            //cob.snakePos.add(new PointsXY(125,125));
            //MainFragment.sgp.clientUpdate(cob);
            if (enemylist.isEmpty()) {
                enemylist.add(cob);
            }
            else {
                boolean isNew = true;
                for (int i = 0; i < enemylist.size(); i++) {
                    if (enemylist.get(i).username.equals(cob.username)) {
                        enemylist.set(i, cob);
                        isNew = false;
                    }
                }

                if(isNew) { //new user
                    enemylist.add(cob);
                }
            }
        }
    }

}

