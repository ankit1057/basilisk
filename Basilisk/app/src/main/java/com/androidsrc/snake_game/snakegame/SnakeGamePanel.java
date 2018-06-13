package com.androidsrc.snake_game.snakegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.androidsrc.snake_game.MainActivity;
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
	public static ArrayList<SnakeCommBuffer> enemies; //used to store details about other snakes
    public SnakeActor snake;
	public static ArrayList<SnakeActor> enemysnakes;
    private AppleActor apple;
    private ScoreBoard score;
    private static SnakeCommBuffer buff;
    private static boolean isPaused = false;

    //public static ClientHandler clientConnTd;

	public SnakeGamePanel(String uname, Context context, boolean isServ) {
		super(context);
		isServer = isServ;
		username = uname;
		enemies = new ArrayList<SnakeCommBuffer>();
		enemysnakes = new ArrayList<SnakeActor>();
	}


	@Override
	public void onStart() {

//		if(isServer) {
//			snake = new SnakeActor(100, 100, username, MainFragment.constants.colorLUT.get(2));
//		}

//		snake2 = new SnakeActor(300, 300, username, MainFragment.constants.colorLUT.get(3));
		apple = new AppleActor(300, 50, MainFragment.constants.colorLUT.get(4));
		score = new ScoreBoard(this);
		buff = new SnakeCommBuffer(username, snake.tailPos, snake.getPoint(),
		snake.getVelocity());

//		if (!isServer) {
//            if (clientConnTd == null) {
//                clientConnTd = new ClientHandler();
//            }
//        }
	}

	@Override
	public void onTimer() {
		if (!isPaused) {
			if (isUpdateIter) {
				isUpdateIter = false; //for the next iteration


				for(int i=0; i<enemysnakes.size();i++) {
					if (snake.checkBoundsCollision(this, enemysnakes.get(i).tailPos)) {
						snake.setEnabled(false);
						break;
					}
				}


				snake.move();
				if (apple.intersect(snake)) {
					snake.grow();
					score.earnPoints(50);
					apple.reposition(this);
					System.out.println("SnakeLenNow :" + snake.tailPos.size());
				}
			} else {
				isUpdateIter = true; //for the next iteration
				if (!isServer) {
					//update buffer with latest value
					buff.snakePos = snake.tailPos;
					buff.nextPos = snake.getPoint();
					buff.velocity = snake.getVelocity();
					//client send the buffer here
					//ClientConnThread.sendToServer(buff);
					//System.out.println("xfer_cl_snt");

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
					buff.snakePos = snake.tailPos;
					buff.nextPos = snake.getPoint();
					buff.velocity = snake.getVelocity();
					//Bundle bundle = new Bundle();
					//bundle.putSerializable("buffer",buff.nextPosX);
					System.out.println("xfer_sr_snt");
					//PlayerInfo xp = new PlayerInfo("send2");
					ServerConnThread.sendToAll(buff); //TODO: Change it back to buff
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
	}

	@Override
	public void redrawCanvas(Canvas canvas) {
		if (snake.isEnabled()) {
			snake.draw(canvas);

			for(int i=0; i<enemysnakes.size();i++) {
				enemysnakes.get(i).draw(canvas);
			}

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

	public void serverUpdateNoPlayers(int nuser) {
		nusers = nuser;
	}
}
