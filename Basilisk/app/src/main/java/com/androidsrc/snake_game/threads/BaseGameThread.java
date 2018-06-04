package com.androidsrc.snake_game.threads;

import android.view.SurfaceHolder;

import com.androidsrc.snake_game.panels.AbstractGamePanel;

public abstract class BaseGameThread extends Thread {
	// Store references to the game panel and holder
	protected SurfaceHolder surfaceHolder;
	protected AbstractGamePanel gamePanel;

	// flag to hold game state
	protected boolean running;

	public void setRunning(boolean running) {
		this.running = running;
	}

	public boolean isRunning() {
		return this.running;
	}
	
	public BaseGameThread(SurfaceHolder surfaceHolder, AbstractGamePanel gamePanel) {
		super();
		this.surfaceHolder = surfaceHolder;
		this.gamePanel = gamePanel;
	}
	
	public abstract void run();
}
