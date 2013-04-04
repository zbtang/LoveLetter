package com.hustunique.gameroles;

import org.jbox2d.dynamics.World;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.hustunique.utils.Box2DUtils;
import com.hustunique.utils.Constant;

public class Spirit extends GameRole implements Movable {

	public MyColorCircle ball;// 跳动的小球
	

	public Spirit(World myWorld) {
		super(myWorld);
	}

	@Override
	public void creatSelf() {
		ball = Box2DUtils.creatCircle(
				Constant.POSITION_AND_SPEED_OF_BALL[Constant.CURRENT_LEVEL][0],
				Constant.POSITION_AND_SPEED_OF_BALL[Constant.CURRENT_LEVEL][1],
				Constant.BALL_RADIUS, true, false, myWorld,
				Constant.PIC_ARRAY[0]);
		ball.body.allowSleeping(false);
	}

	public void move() {
		//spirit 的移动已经在world.step()方法中了
	}

	@Override
	public void drawSelf(Canvas canvas, Paint paint) {
		ball.drawSelf(canvas, paint);
	}

}
