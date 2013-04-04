package com.hustunique.gameroles;

import org.jbox2d.dynamics.World;

import com.hustunique.utils.Box2DUtils;
import com.hustunique.utils.Constant;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Sea extends GameRole implements Movable {
	public MyColorRect deadLine;// 海平面上的死亡线
	public int oceanXOffset = 0;// 海洋的偏移距离

	public Sea(World myWorld) {
		super(myWorld);
	}

	@Override
	public void creatSelf() {
		deadLine = Box2DUtils.creatBox(Constant.SCREEN_WIDTH / 2,
				Constant.SCREEN_HEIGHT - Constant.oceanSurfaceLevel,
				Constant.SCREEN_WIDTH / 2 - 4, 2, true, myWorld, null);
		deadLine.body.getShapeList().m_isSensor = true;
	}

	@Override
	public void drawSelf(Canvas canvas, Paint paint) {
		canvas.drawBitmap(
				Constant.PIC_ARRAY[Constant.MAIN_MENU_PISC_ID[2]],
				oceanXOffset
						% Constant.PIC_ARRAY[Constant.MAIN_MENU_PISC_ID[2]]
								.getWidth(),
				Constant.PICS_INIT_POSITION[Constant.MAIN_MENU_PISC_ID[2]][1],
				paint);
		canvas.drawBitmap(
				Constant.PIC_ARRAY[Constant.MAIN_MENU_PISC_ID[2]],
				(oceanXOffset % Constant.PIC_ARRAY[Constant.MAIN_MENU_PISC_ID[2]]
						.getWidth())
						+ Constant.PIC_ARRAY[Constant.MAIN_MENU_PISC_ID[2]]
								.getWidth() - 1,// 由于像素不是特别连贯，所以减去了一个像素
				Constant.PICS_INIT_POSITION[Constant.MAIN_MENU_PISC_ID[2]][1],
				paint);
	}

	public void move() {
		oceanXOffset -= Constant.OCEAN_MOVE_STEP;
	}

}
