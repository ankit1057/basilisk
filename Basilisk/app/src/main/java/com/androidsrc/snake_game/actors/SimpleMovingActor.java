package com.androidsrc.snake_game.actors;


import com.androidsrc.snake_game.panels.Velocity;


public abstract class SimpleMovingActor extends PositionedActor {
	private Velocity velocity;

	public SimpleMovingActor(int x, int y, int colour) {
		super(x, y, colour);
		this.velocity = new Velocity();
	}
	
	public SimpleMovingActor(int x, int y, int width, int height, int colour) {
		this(x, y, colour);
		this.setDimensions(width, height);
	}
	
	// Called to move position based on the velocity
	public void move() {
		if (this.isEnabled()) {
			getPoint().x += (this.velocity.getXSpeed() * this.velocity.getXDirection());
			getPoint().y += (this.velocity.getYSpeed() * this.velocity.getYDirection());
		}
	}
	
	public Velocity getVelocity() {
		return this.velocity;
	}

	public void setVelocity(Velocity velocity) {
		this.velocity = velocity;
	}
}
