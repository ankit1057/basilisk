package com.androidsrc.snake_game.snakegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.androidsrc.snake_game.MainActivity;
import com.androidsrc.snake_game.communication.ClientConnThread;
import com.androidsrc.snake_game.communication.ServerConnThread;
import com.androidsrc.snake_game.game.MainFragment;
import com.androidsrc.snake_game.panels.AbstractGamePanel;

import java.util.ArrayList;
//import com.androidsrc.snake_game.utils.ClientHandler;


public class SnakeGamePanel extends AbstractGamePanel {

    public static boolean isServer;
    public static boolean isUpdateIter;
    public static String username;
	public static int nusers;
	public ArrayList<SnakeCommBuffer> enemies; //used to store details about other snakes
    public SnakeActor snake;
	public ArrayList<SnakeActor> enemysnakes;
    private AppleActor apple;
    private ScoreBoard score;
    private SnakeCommBuffer buff;
    private static boolean isPaused = false;
    private static boolean isOver = false;

    //public static ClientHandler clientConnTd;

	public SnakeGamePanel(String uname, Context context, boolean isServ) {
		super(context);
		this.isServer = isServ;
		this.username = uname;
		this.enemies = new ArrayList<SnakeCommBuffer>();
		this.enemysnakes = new ArrayList<SnakeActor>();
	}


	@Override
	public void onStart() {

//		if(isServer) {
//			snake = new SnakeActor(100, 100, username, MainFragment.constants.colorLUT.get(2));
//		}

//		snake2 = new SnakeActor(300, 300, username, MainFragment.constants.colorLUT.get(3));
		this.apple = new AppleActor(300, 50, MainFragment.constants.colorLUT.get(0));
		this.score = new ScoreBoard(this);
		this.buff = new SnakeCommBuffer(this.username, this.snake.tailPos, this.snake.getPoint());
									//, this.snake.getVelocity());

//		if (!isServer) {
//            if (clientConnTd == null) {
//                clientConnTd = new ClientHandler();
//            }
//        }
	}

	@Override
	public void onTimer() {
		if (!this.isPaused) {
			if (this.isUpdateIter) {
				this.isUpdateIter = false; //for the next iteration

				//system.out.println("enemysize"+this.enemysnakes.size());
				for(int i=0; i<this.enemysnakes.size();i++) {
					if (this.snake.checkBoundsCollision(this, this.enemysnakes.get(i).tailPos)) {
						this.snake.setEnabled(false);
						break;
					}
				}


				this.snake.move();
				if (this.apple.intersect(this.snake)) {
					this.snake.grow();
					this.score.earnPoints(50);
					this.apple.reposition(this);
					//system.out.println("SnakeLenNow :" + this.snake.tailPos.size());
				}
			} else {
				this.isUpdateIter = true; //for the next iteration
				if(!this.isOver) {
					this.isOver = !(snake.isEnabled());
					if (!this.isServer) {
						//update buffer with latest value
						this.buff.snakePos = snake.tailPos;
						this.buff.nextPos = snake.getPoint();
						//this.buff.velocity = snake.getVelocity();
						this.buff.setActive(this.isOver);
						//client send the buffer here
						ClientConnThread.sendToServer(this.buff);

						////system.out.println("xfer_cl_snt");

						//					if(enemies.size() > 0) {
						//						snake2.tailPos = enemies.get(0).snakePos;
						//						snake2.setPoint(enemies.get(0).nextPos);
						//					}
						//snake2.getVelocity(enemies.get(0).velocity);

						//client wait for the processed data from server
						//receive snakeServerBuffer here
						//based on command from buf, do op
						//snake.move();
					} else {
						//server should receive all the clients new pos and then do op
						//TODO: only sending sever snake details. Iterate for all clinets after proc
						this.buff.snakePos = this.snake.tailPos;
						this.buff.nextPos = this.snake.getPoint();
						//this.buff.velocity = this.snake.getVelocity();
						this.buff.setActive(this.isOver);
						//Bundle bundle = new Bundle();
						//bundle.putSerializable("buffer",buff.nextPosX);
						//system.out.println("xfer_sr_snt");
						//PlayerInfo xp = new PlayerInfo("send2");
						ServerConnThread.sendToAll(this.buff); //TODO: Change it back to buff
					}
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
	}

	@Override
	public void redrawCanvas(Canvas canvas) {
		if (this.snake.isEnabled()) {
			this.snake.draw(canvas);

			for(int i=0; i<this.enemysnakes.size();i++) {
				if(this.enemysnakes.get(i).isEnabled()) {
					this.enemysnakes.get(i).draw(canvas);
				}
			}

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
			onStart();
		}
		if (keyCode == KeyEvent.KEYCODE_P) {
			this.isPaused = !this.isPaused;
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

	public void serverUpdateNoPlayers(int nuser) {
		this.nusers = nuser;
	}
}
