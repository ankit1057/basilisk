package com.androidsrc.snake_game.snakegame;


import com.androidsrc.snake_game.panels.Velocity;

import java.io.Serializable;
import java.util.ArrayList;

public class SnakeCommBuffer implements Serializable {
    public ArrayList<PointsXY> snakePos;
    public int nextPosX, nextPosY;
    public Velocity velocity;

    public SnakeCommBuffer(ArrayList<PointsXY> snakePos, int nextPosX, int nextPosY, Velocity velocity) {
        this.snakePos = snakePos;
        this.nextPosX = nextPosX;
        this.nextPosY = nextPosY;
        this.velocity = velocity;
    }
}
