package com.androidsrc.snake_game.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import com.androidsrc.snake_game.game.MainFragment;
import com.androidsrc.snake_game.snakegame.PointsXY;
import com.androidsrc.snake_game.snakegame.SnakeCommBuffer;

public class ClientHandler extends Handler {


    Bundle messageData;

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        messageData = msg.getData();
        Object clientObject = messageData.getSerializable("GameDataBuff");
        SnakeCommBuffer cob;

        if (clientObject instanceof SnakeCommBuffer) {
            cob = (SnakeCommBuffer) clientObject;
            System.out.println("MessageHandlerOut Client"+cob.nextPosX+cob.nextPosY);
            cob.snakePos.add(new PointsXY(125,125));
            MainFragment.sgp.clientUpdate(cob);
        }
    }

}

