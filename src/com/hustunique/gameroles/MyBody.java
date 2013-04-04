package com.hustunique.gameroles;

import org.jbox2d.dynamics.Body;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class MyBody {
	public Body body;

	public abstract void drawSelf(Canvas canvas, Paint paint);
}
