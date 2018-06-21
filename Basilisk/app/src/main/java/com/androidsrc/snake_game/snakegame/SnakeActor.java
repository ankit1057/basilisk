package com.androidsrc.snake_game.snakegame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.androidsrc.snake_game.actors.SimpleMovingActor;
import com.androidsrc.snake_game.panels.AbstractGamePanel;
import com.androidsrc.snake_game.panels.Velocity;

import java.util.ArrayList;


public class SnakeActor extends SimpleMovingActor {
	public static final int DRAW_SIZE = 25;
	public static final int STEP = 25;
	public ArrayList<PointsXY> tailPos;
	public String userName; //changes for each snake!!
	public int userID;
	private int colour;
//	private Paint paint;


	public SnakeActor(int x, int y, String uname, int colour) {
		super(x, y, DRAW_SIZE, DRAW_SIZE, colour);
		this.colour = colour;
		getVelocity().stop().setXDirection(Velocity.DIRECTION_RIGHT).setXSpeed(STEP);
		this.tailPos = new ArrayList<PointsXY>();
		this.tailPos.add(new PointsXY(x - this.getWidth(), y));
		this.tailPos.add(new PointsXY(x - this.getWidth() * 2, y));
		this.userName = uname;
		this.userID = -1; //default, invalid
//		this.paint = new Paint();
//		stylePaint(this.paint, this.colour);
	}

	@Override
	public void stylePaint(Paint p, int colour) {
		p.setColor(colour);
		p.setStyle(Style.FILL);
	}

	@Override
	public void draw(Canvas canvas) {
		getPaint().setColor(this.colour);
		canvas.drawRect(getRect(), getPaint());
		for (PointsXY p : this.tailPos) {
			Rect r = new Rect(p.x, p.y, p.x + this.getWidth(), p.y + this.getHeight());
			canvas.drawRect(r, getPaint());
		}
	}

//	public void drawSnake(Canvas canvas) {
//		this.paint.setColor(this.colour);
//		canvas.drawRect(getRect(), this.paint);
//		for (PointsXY p : tailPos) {
//			Rect r = new Rect(p.x, p.y, p.x + this.getWidth(), p.y + this.getHeight());
//			canvas.drawRect(r, this.paint);
//		}
//	}

	public void move() {
		if (this.isEnabled()) {
			int headX = getPoint().x;
			int headY = getPoint().y;
			for (int x = this.tailPos.size() - 1; x > 0; x--) {
				this.tailPos.get(x).set(this.tailPos.get(x - 1).x, this.tailPos.get(x - 1).y);
			}
			this.tailPos.get(0).set(headX, headY);
			super.move();
		}
	}

	public void grow() {
		this.tailPos.add(new PointsXY(getX(), getY()));
	}

	public boolean checkBoundsCollision(AbstractGamePanel panel, ArrayList<PointsXY> enemies) {
		int isnake, jpoint;
		int headX = this.getX();
		int headY = this.getY();

		if (headX < 0) {
			return true;
		} else if (headX >= (panel.getWidth() - this.getWidth())) {
			return true;
		} else if (headY < 0) {
			return true;
		} else if (headY >= (panel.getHeight() - this.getHeight())) {
			return true;
		}
		//system.out.println("checkboundsize"+enemies.size());
		for (isnake = 0; isnake < enemies.size(); isnake++) {
			if ((headX == enemies.get(isnake).x) &
					(headY == enemies.get(isnake).y)) {
				return true;
			}
		}

		return false;
	}

	public void handleKeyInput(int keyCode) {
		if (keyCode == KeyEvent.KEYCODE_W) {
			getVelocity().stop().setYDirection(Velocity.DIRECTION_UP).setYSpeed(STEP);
		} else if (keyCode == KeyEvent.KEYCODE_S) {
			getVelocity().stop().setYDirection(Velocity.DIRECTION_DOWN).setYSpeed(STEP);
		} else if (keyCode == KeyEvent.KEYCODE_A) {
			getVelocity().stop().setXDirection(Velocity.DIRECTION_LEFT).setXSpeed(STEP);
		} else if (keyCode == KeyEvent.KEYCODE_D) {
			getVelocity().stop().setXDirection(Velocity.DIRECTION_RIGHT).setXSpeed(STEP);
		}
	}

	public void handleTouchInput(MotionEvent event) {
		//Log.e("snakedir", "snake spped"+getVelocity().getYSpeed()+getVelocity().getXSpeed());
		if (getVelocity().getYSpeed() == 0) {
			if (event.getY() < this.getY()) {
				getVelocity().stop().setYDirection(Velocity.DIRECTION_UP).setYSpeed(STEP);
				//System.out.print("snakedir up");
			} else if (event.getY() > this.getY() && getVelocity().getYSpeed() == 0) {
				getVelocity().stop().setYDirection(Velocity.DIRECTION_DOWN).setYSpeed(STEP);
				//System.out.print("snakedir down");
			}
		} else if (getVelocity().getXSpeed() == 0) {
			if (event.getX() < this.getX()) {
				getVelocity().stop().setXDirection(Velocity.DIRECTION_LEFT).setXSpeed(STEP);
				//System.out.print("snakedir left");
			} else if (event.getX() > this.getX()) {
				getVelocity().stop().setXDirection(Velocity.DIRECTION_RIGHT).setXSpeed(STEP);
				//System.out.print("snakedir right");
			}
		}
	}
}
