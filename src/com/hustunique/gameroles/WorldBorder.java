package com.hustunique.gameroles;

import org.jbox2d.dynamics.World;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.hustunique.utils.Box2DUtils;
import com.hustunique.utils.Constant;

public class WorldBorder extends GameRole {

	MyColorRect borderLeft;// 世界左边的边界
	MyColorRect borderRight;// 世界右边的边界

	public WorldBorder(World myWorld) {
		super(myWorld);
	}

	@Override
	public void creatSelf() {
		borderLeft = Box2DUtils.creatBox(4, Constant.SCREEN_HEIGHT / 2, 5,
				Constant.SCREEN_HEIGHT / 2, true, myWorld, null);
		borderRight = Box2DUtils
				.creatBox(
						Constant.PIC_ARRAY[Constant.PICS_ID_FOR_LEV_BG[Constant.CURRENT_LEVEL]]
								.getWidth() - 4, Constant.SCREEN_HEIGHT / 2, 5,
						Constant.SCREEN_HEIGHT / 2, true, myWorld, null);
	}

	@Override
	public void drawSelf(Canvas canvas, Paint paint) {

	}

}
