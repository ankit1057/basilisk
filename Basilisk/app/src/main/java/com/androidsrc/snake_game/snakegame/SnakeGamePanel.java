package com.androidsrc.snake_game.snakegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.androidsrc.snake_game.communication.Clienthandler;
import com.androidsrc.snake_game.communication.PlayerInfo;
import com.androidsrc.snake_game.communication.Serverhandler;
import com.androidsrc.snake_game.panels.AbstractGamePanel;
//import com.androidsrc.snake_game.utils.ClientHandler;


public class SnakeGamePanel extends AbstractGamePanel {

    public boolean isServer;
    private static SnakeActor snake;
    private static AppleActor apple;
    private static ScoreBoard score;
    private static SnakeCommBuffer buff;
    private static boolean isPaused = false;

    //public static ClientHandler clientHandler;

	public SnakeGamePanel(Context context, boolean isServ) {
		super(context);
		isServer = isServ;
	}


	@Override
	public void onStart() {
		snake = new SnakeActor(100, 100);
		apple = new AppleActor(200, 50);
		score = new ScoreBoard(this);
		buff = new SnakeCommBuffer(snake.tailPos, snake.getX(),
		snake.getY(), snake.getVelocity());
//		if (!isServer) {
//            if (clientHandler == null) {
//                clientHandler = new ClientHandler();
//            }
//        }
	}

	@Override
	public void onTimer() {
		if (!isPaused) {
		    if (!isServer) {
		        //update buffer with latest value
		        buff.snakePos = snake.tailPos;
                buff.nextPosX = snake.getX();
				buff.nextPosY = snake.getY();
                buff.velocity = snake.getVelocity();
                //client send the buffer here
				//Clienthandler.sendToServer(buff);
				//System.out.println("xfer_cl_snt");

				//client wait for the processed data from server
				//receive snakeServerBuffer here
				//based on command from buf, do op
                //snake.move();
            }
            else {
		    	//server should receive all the clients new pos and then do op
				//TODO: only sending sever snake details. Iterate for all clinets after proc
				buff.snakePos = snake.tailPos;
				buff.nextPosX = snake.getX();
				buff.nextPosY = snake.getY();
				buff.velocity = snake.getVelocity();
				//Bundle bundle = new Bundle();
				//bundle.putSerializable("buffer",buff.nextPosX);
				System.out.println("xfer_sr_snt");
				//PlayerInfo xp = new PlayerInfo("send2");
				Serverhandler.sendToAll(buff);
				//server
			}
//			if (snake.checkBoundsCollision(this)) {
//				snake.setEnabled(false);
//			}
//			snake.move();
//			if (apple.intersect(snake)) {
//				snake.grow();
//				score.earnPoints(50);
//				apple.reposition(this);
//			}
		}
	}

	@Override
	public void redrawCanvas(Canvas canvas) {
		if (snake.isEnabled()) {
			snake.draw(canvas);
			apple.draw(canvas);
			score.draw(canvas);
		} else {
			Paint p = getPaint();
			p.setTextSize(50);
			p.setColor(Color.BLACK);
			canvas.drawText("Game over!", 100, 100, p);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		snake.handleKeyInput(keyCode);
		if (keyCode == KeyEvent.KEYCODE_G) {
			onStart();
		}
		if (keyCode == KeyEvent.KEYCODE_P) {
			isPaused = !isPaused;
		}
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			snake.handleTouchInput(event);
			return true;
		}
		return false;
	}

    public void clientUpdate(SnakeCommBuffer rxbuff) {

	    buff = rxbuff;
        snake.setX(rxbuff.nextPosX);
        snake.setY(rxbuff.nextPosY);
        snake.move();
//        if (snake.checkBoundsCollision(this)) {
//            snake.setEnabled(false);
//        }
//        snake.move();
//        if (apple.intersect(snake)) {
//            snake.grow();
//            score.earnPoints(50);
//            apple.reposition(this);
//        }
    }
}
