package com.androidsrc.snake_game.snakegame;


import com.androidsrc.snake_game.panels.Velocity;

import java.io.Serializable;
import java.util.ArrayList;

public class SnakeCommBuffer implements Serializable {
    public String username;
    public boolean isActive;
    public ArrayList<PointsXY> snakePos;
    public PointsXY nextPos;
    //public Velocity velocity;
    public int nusers;
    public int userID;

    public SnakeCommBuffer(String uname, ArrayList<PointsXY> snakePos, PointsXY nextPos) {
    //public SnakeCommBuffer(String uname, ArrayList<PointsXY> snakePos, PointsXY nextPos, Velocity velocity) {
        this.username = uname;
        this.snakePos = snakePos;
        this.nextPos = nextPos;
        //this.velocity = velocity;
        this.isActive = true;   //TODO: Verify this later
        this.nusers = 2;        //TODO: validate this initial condt later
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public void setNusers(int nusers) {
        this.nusers = nusers;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
