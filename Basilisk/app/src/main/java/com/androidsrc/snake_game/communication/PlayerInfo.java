package com.androidsrc.snake_game.communication;

import java.io.Serializable;

public class PlayerInfo implements Serializable {
    public String username;
    public int userid;
    //public String message;

    public PlayerInfo(String username) {
        this.userid = -1; //Default, not valid
        this.username = username;
        //this.message = null; //Default, not valid
    }
}
