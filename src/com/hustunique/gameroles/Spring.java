package com.hustunique.gameroles;

import org.jbox2d.dynamics.World;

import com.hustunique.utils.Box2DUtils;
import com.hustunique.utils.Constant;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Spring extends GameRole {
	// 第二十二关的gate
	public MyPolygon gate;

	public Spring(World myWorld) {
		super(myWorld);
	}

	@Override
	public void creatSelf() {
		gate = Box2DUtils.creatPolygon(0, 0, Constant.GATE_POSI, true, myWorld);
		gate.body.getShapeList().m_isSensor = true;
	}

	@Override
	public void drawSelf(Canvas canvas, Paint paint) {

	}

}
