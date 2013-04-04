package com.hustunique.gameroles;

import org.jbox2d.dynamics.World;

import com.hustunique.utils.Box2DUtils;
import com.hustunique.utils.Constant;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Heads extends GameRole {
	// 第八关的三个头
	MyColorCircle head1;
	MyColorCircle head2;
	MyColorCircle head3;
	MyColorCircle head4;

	public Heads(World myWorld) {
		super(myWorld);
	}

	@Override
	public void creatSelf() {
		head1 = Box2DUtils.creatCircle(61 * Constant.xMainRatio,
				164 * Constant.yMainRatio, 46 * Constant.xMainRatio, false,
				true, myWorld, null);
		head2 = Box2DUtils.creatCircle(229 * Constant.xMainRatio,
				164 * Constant.yMainRatio, 46 * Constant.xMainRatio, false,
				true, myWorld, null);
		head3 = Box2DUtils.creatCircle(396 * Constant.xMainRatio,
				159 * Constant.yMainRatio, 46 * Constant.xMainRatio, false,
				true, myWorld, null);
		head4 = Box2DUtils.creatCircle(562 * Constant.xMainRatio,
				159 * Constant.yMainRatio, 46 * Constant.xMainRatio, false,
				true, myWorld, null);
	}

	@Override
	public void drawSelf(Canvas canvas, Paint paint) {

	}

}
