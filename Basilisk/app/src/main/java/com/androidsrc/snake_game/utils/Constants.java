package com.androidsrc.snake_game.utils;

import android.graphics.Color;

import java.util.ArrayList;

public class Constants {

    public static ArrayList<Integer> colorLUT;

    public Constants() {
        colorLUT = new ArrayList<Integer>();
        colorLUT.add(new Integer(Color.RED));       //0 - Apples
        colorLUT.add(new Integer(Color.GREEN));     //1 - User1
        colorLUT.add(new Integer(Color.BLUE));
        colorLUT.add(new Integer(Color.YELLOW));
        colorLUT.add(new Integer(Color.CYAN));
        colorLUT.add(new Integer(Color.MAGENTA));   //5 - User5
        colorLUT.add(new Integer(Color.WHITE));     //6 - Default User
    }
}

//TODO: Max colour 5, assuming max players 5 else others will have white
//		switch (colourID) {
//			case 0: Colour = Color.GREEN; break;
//			case 1: Colour = Color.YELLOW; break;
//			case 2: Colour = Color.BLUE; break;
//			case 3: Colour = Color.MAGENTA; break;
//			case 4: Colour = Color.CYAN; break;
//			default:Colour = Color.WHITE;
//
//		}

//    public static final int UPDATE_GAME_NAME = 1;
//    public static final int GAME_PLAY = 2;
//    public static final int PLAYER_LIST_UPDATE = 3;
//    public static final int NEW_GAME = 4;
//    public static final int DEAL_CARD = 5;
//    public static final String ACTION_KEY = "action";
//    public static final String DATA_KEY = "data";

//    public static boolean isPlayerActive(String userName, Game gameObject) {
//        for (int i = 0; i < gameObject.getPlayers().size(); i++) {
//            Player play = gameObject.getPlayers().get(i);
//            if (play.username.equals(userName) && play.isActive) {
//                return true;
//            }
//        }
//        return false;
//    }
//}

