package com.androidsrc.snake_game.actors;

import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;

import com.androidsrc.snake_game.snakegame.PointsXY;

/*
 * 
 * Represents a game object that has a position and width / height on screen.
 *  
 */
public abstract class PositionedActor extends Actor {
	private PointsXY pos;
	private int height;
	private int width;

	public PositionedActor(int x, int y) {
		this.pos = new PointsXY(x, y);
	}

	public PositionedActor(int x, int y, int colour) {
		super(colour);
		this.pos = new PointsXY(x, y);
	}
	
	public PositionedActor(int x, int y, int width, int height, int colour) {
		this(x, y, colour);
		this.setDimensions(width, height);
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public void setDimensions(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public PointsXY getPoint() {
		return pos;
	}

	public void setPoint(PointsXY pos) {
		this.pos = pos;
	}

	public Rect getRect() {
		Rect rect = new Rect();
		getRectF().round(rect);
		return rect;
	}
	
	public RectF getRectF() {
		return new RectF(getX(), getY(), getX() + width, getY() + height);
	}
	
	public void setPos(int x, int y) {
		this.pos = new PointsXY(x, y);
	}

	public int getX() {
		return this.pos.x;
	}

	public void setX(int x) {
		this.pos.x = x;
	}

	public int getY() {
		return this.pos.y;
	}

	public void setY(int y) {
		this.pos.y = y;
	}
	
	public boolean intersect(SimpleMovingActor actor) {
		return getRect().intersect(actor.getRect());
	}

}
