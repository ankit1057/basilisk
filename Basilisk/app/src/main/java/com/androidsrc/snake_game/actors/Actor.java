package com.androidsrc.snake_game.actors;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class Actor {
	// Defines the default paint
	private Paint paint;
	private boolean enabled;

	public Actor() {
		// Enable and make visible
		this.enabled = true;
		// Setup paint for actor
		this.paint = new Paint();
		stylePaint(this.paint, -1);
	}
	
	public Actor(int colour) {
		// Enable and make visible
		this.enabled = true;
	    // Setup paint for actor
		this.paint = new Paint();
		stylePaint(this.paint, colour);
	}
	
	public Paint getPaint() {
		return this.paint;
	}
	
	public boolean isEnabled() {
		return this.enabled;
	}
	
	public void setEnabled(boolean state) {
		this.enabled = state;
	}
	
	// Define the default paint style
	public abstract void stylePaint(Paint p, int colour);
	// Defines how the actor is drawn
	public abstract void draw(Canvas canvas);
}
