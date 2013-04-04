package com.hustunique.gameroles;

import org.jbox2d.dynamics.World;

import com.hustunique.utils.Box2DUtils;
import com.hustunique.utils.Constant;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Letters extends GameRole {

	MyPolygon myPolygon;// 各种字的多边形

	public Letters(World myWorld) {
		super(myWorld);
	}

	@Override
	public void creatSelf() {
		myPolygon = Box2DUtils.creatPolygon(0, 0,
				Constant.BODY_POINT[Constant.CURRENT_LEVEL], true, myWorld);
	}

	@Override
	public void drawSelf(Canvas canvas, Paint paint) {

	}

}
