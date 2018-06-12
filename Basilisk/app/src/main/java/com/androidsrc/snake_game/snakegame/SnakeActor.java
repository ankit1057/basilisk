package com.androidsrc.snake_game.snakegame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.androidsrc.snake_game.actors.SimpleMovingActor;
import com.androidsrc.snake_game.panels.AbstractGamePanel;
import com.androidsrc.snake_game.panels.Velocity;

import java.util.ArrayList;


public class SnakeActor extends SimpleMovingActor {
	public static final int DRAW_SIZE = 25;
	public static final int STEP = 25;
	public static ArrayList<PointsXY> tailPos;

	public SnakeActor(int x, int y) {
		super(x, y, DRAW_SIZE, DRAW_SIZE);
		getVelocity().stop().setXDirection(Velocity.DIRECTION_RIGHT).setXSpeed(STEP);
		tailPos = new ArrayList<PointsXY>();
		tailPos.add(new PointsXY(x - this.getWidth(), y));
		tailPos.add(new PointsXY(x - this.getWidth() * 2, y));
	}

	@Override
	public void stylePaint(Paint p) {
		p.setColor(Color.GREEN);
		p.setStyle(Style.FILL);
	}

	@Override
	public void draw(Canvas canvas) {
		getPaint().setColor(Color.GREEN);
		canvas.drawRect(getRect(), getPaint());
		for (PointsXY p : tailPos) {
			Rect r = new Rect(p.x, p.y, p.x + this.getWidth(), p.y + this.getHeight());
			canvas.drawRect(r, getPaint());
		}
	}

	public void move() {
		if (this.isEnabled()) {
			int headX = getPoint().x;
			int headY = getPoint().y;
			for (int x = tailPos.size() - 1; x > 0; x--) {
				tailPos.get(x).set(tailPos.get(x - 1).x, tailPos.get(x - 1).y);
			}
			tailPos.get(0).set(headX, headY);
			super.move();
		}
	}

	public void grow() {
		this.tailPos.add(new PointsXY(getX(), getY()));
	}

	public boolean checkBoundsCollision(AbstractGamePanel panel, ArrayList<SnakeCommBuffer> enemies) {
		int isnake, jpoint;
		SnakeCommBuffer snakeNow;
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

		for (isnake = 0; isnake < enemies.size(); isnake++) {
			snakeNow = enemies.get(isnake);
			for (jpoint = 0; jpoint < snakeNow.snakePos.size(); jpoint++) {
				if ((headX == snakeNow.snakePos.get(jpoint).x) &
						(headY == snakeNow.snakePos.get(jpoint).y)) {
					return true;
				}
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
		if (getVelocity().getYSpeed() == 0) {
			if (event.getY() < this.getY()) {
				getVelocity().stop().setYDirection(Velocity.DIRECTION_UP).setYSpeed(STEP);
			} else if (event.getY() > this.getY() && getVelocity().getYSpeed() == 0) {
				getVelocity().stop().setYDirection(Velocity.DIRECTION_DOWN).setYSpeed(STEP);
			}
		} else if (getVelocity().getXSpeed() == 0) {
			if (event.getX() < this.getX()) {
				getVelocity().stop().setXDirection(Velocity.DIRECTION_LEFT).setXSpeed(STEP);
			} else if (event.getX() > this.getX()) {
				getVelocity().stop().setXDirection(Velocity.DIRECTION_RIGHT).setXSpeed(STEP);
			}
		}
	}
}
