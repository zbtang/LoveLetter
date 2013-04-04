package com.hustunique.gameroles;

import org.jbox2d.dynamics.World;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class GameRole {
	World myWorld;

	public GameRole(World myWorld) {
		this.myWorld = myWorld;
	}

	public abstract void creatSelf();

	public abstract void drawSelf(Canvas canvas, Paint paint);
}
