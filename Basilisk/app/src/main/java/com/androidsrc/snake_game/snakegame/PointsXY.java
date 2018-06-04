package com.androidsrc.snake_game.snakegame;

import java.io.Serializable;

public class PointsXY implements Serializable {

    public int x;
    public int y;

    public PointsXY (int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
