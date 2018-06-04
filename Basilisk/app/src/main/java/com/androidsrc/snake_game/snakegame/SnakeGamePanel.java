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


public class SnakeGamePanel extends AbstractGamePanel {

    public boolean isServer;
	public SnakeGamePanel(Context context, boolean isServer) {
		super(context);
		this.isServer = isServer;
	}

	private SnakeActor snake;
	private AppleActor apple;
	private ScoreBoard score;
	private SnakeCommBuffer buff;
	private boolean isPaused = false;

	@Override
	public void onStart() {
		this.snake = new SnakeActor(100, 100);
		this.apple = new AppleActor(200, 50);
		this.score = new ScoreBoard(this);
		this.buff = new SnakeCommBuffer(this.snake.tailPos, this.snake.getX(),
				this.snake.getY(), this.snake.getVelocity());
	}

	@Override
	public void onTimer() {
		if (!isPaused) {
		    if (!this.isServer) {
		        //update buffer with latest value
		        this.buff.snakePos = this.snake.tailPos;
                this.buff.nextPosX = this.snake.getX();
				this.buff.nextPosY = this.snake.getY();
                this.buff.velocity = this.snake.getVelocity();
                //client send the buffer here
				//Clienthandler.sendToServer(buff);
				//System.out.println("xfer_cl_snt");

				//client wait for the processed data from server
				//receive snakeServerBuffer here
				//based on command from buf, do op
            }
            else {
		    	//server should receive all the clients new pos and then do op
				//TODO: only sending sever snake details. Iterate for all clinets after proc
				this.buff.snakePos = this.snake.tailPos;
				this.buff.nextPosX = this.snake.getX();
				this.buff.nextPosY = this.snake.getY();
				this.buff.velocity = this.snake.getVelocity();
				//Bundle bundle = new Bundle();
				//bundle.putSerializable("buffer",buff.nextPosX);
				System.out.println("xfer_sr_snt");
				//PlayerInfo xp = new PlayerInfo("send2");
				Serverhandler.sendToAll(buff);
				//server
			}
			if (this.snake.checkBoundsCollision(this)) {
				this.snake.setEnabled(false);
			}
			this.snake.move();
			if (this.apple.intersect(this.snake)) {
				this.snake.grow();
				this.score.earnPoints(50);
				this.apple.reposition(this);
			}
		}
	}

	@Override
	public void redrawCanvas(Canvas canvas) {
		if (this.snake.isEnabled()) {
			this.snake.draw(canvas);
			this.apple.draw(canvas);
			this.score.draw(canvas);
		} else {
			Paint p = getPaint();
			p.setTextSize(50);
			p.setColor(Color.BLACK);
			canvas.drawText("Game over!", 100, 100, p);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		this.snake.handleKeyInput(keyCode);
		if (keyCode == KeyEvent.KEYCODE_G) {
			this.onStart();
		}
		if (keyCode == KeyEvent.KEYCODE_P) {
			isPaused = !isPaused;
		}
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			this.snake.handleTouchInput(event);
			return true;
		}
		return false;
	}

}
