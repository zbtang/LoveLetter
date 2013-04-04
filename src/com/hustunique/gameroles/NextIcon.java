package com.hustunique.gameroles;

import org.jbox2d.dynamics.World;

import com.hustunique.utils.Box2DUtils;
import com.hustunique.utils.Constant;

import android.graphics.Canvas;
import android.graphics.Paint;

public class NextIcon extends GameRole {
	public MyColorRect next;// 进入下一关的标志

	public NextIcon(World myWorld) {
		super(myWorld);
	}

	@Override
	public void creatSelf() {
		next = Box2DUtils.creatBox(
				Constant.NEXT_POSITION[Constant.CURRENT_LEVEL][0],
				Constant.NEXT_POSITION[Constant.CURRENT_LEVEL][1],
				Constant.NEXT_HALF_WIDTH, Constant.NEXT_HALF_HEIGHT, true,
				myWorld, Constant.PIC_ARRAY[Constant.NEXT_PIC_ID]);
		next.body.getShapeList().m_isSensor = true;
	}

	@Override
	public void drawSelf(Canvas canvas, Paint paint) {
		next.drawSelf(canvas, paint);
	}

}
