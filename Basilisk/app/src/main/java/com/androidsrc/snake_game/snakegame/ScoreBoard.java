package com.androidsrc.snake_game.snakegame;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.androidsrc.snake_game.actors.PositionedActor;
import com.androidsrc.snake_game.panels.AbstractGamePanel;


public class ScoreBoard extends PositionedActor {
	private int score;

	public ScoreBoard(AbstractGamePanel context) {
		super(context.getWidth() - 150, 30);
		this.score = 0;
	}

	@Override
	public void stylePaint(Paint p, int colour) {
		//TODO: colour is not used, handle appropirately
		p.setTextSize(20);
	}
	
	public void earnPoints(int points) {
		score += points;
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawText("Score: " + score, getX(), getY(), getPaint());
	}

}
