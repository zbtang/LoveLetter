package com.hustunique.gameroles;

import org.jbox2d.dynamics.World;

import com.hustunique.utils.Box2DUtils;
import com.hustunique.utils.Constant;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Bugs extends GameRole {
	// 第十三关的bugs
	MyPolygon[] bugs;
	int timePointer = 0;

	public Bugs(World myWorld) {
		super(myWorld);
	}

	@Override
	public void creatSelf() {
		bugs = new MyPolygon[Constant.BUGS_POSI.length];
		for (int i = 0; i < Constant.BUGS_POSI.length; i++) {
			bugs[i] = Box2DUtils.creatPolygon(0, 0, Constant.BUGS_POSI[i],
					true, myWorld);
		}
	}

	@Override
	public void drawSelf(Canvas canvas, Paint paint) {
		timePointer++;
		switch (timePointer / Constant.BUG_TIME_SHOW) {
		case 0:
			drawBugOneByOne(0, canvas, paint);
			break;
		case 1:
			drawBugOneByOne(1, canvas, paint);
			break;
		case 2:
			drawBugOneByOne(2, canvas, paint);
			break;
		case 3:
			drawBugOneByOne(3, canvas, paint);
			break;
		default:
			break;
		}
	}

	private void drawBugOneByOne(int currentCase, Canvas canvas, Paint paint) {
		for (int i = 0; i < bugs.length; i++) {
			if (i == currentCase || i == currentCase + 1) {
				bugs[i].body.getShapeList().m_isSensor = false;
				continue;
			}
			bugs[i].body.getShapeList().m_isSensor = true;
		}
		int currentTimeState = timePointer % Constant.BUG_TIME_SHOW;
		paint.setAlpha(255 - 240 * currentTimeState / Constant.BUG_TIME_SHOW);
		canvas.drawBitmap(Constant.PIC_ARRAY[Constant.BUGS_ID[currentCase]],
				Constant.BUGS_PICS_POSI[currentCase][0],
				Constant.BUGS_PICS_POSI[currentCase][1], paint);
		paint.reset();
		canvas.drawBitmap(
				Constant.PIC_ARRAY[Constant.BUGS_ID[currentCase + 1]],
				Constant.BUGS_PICS_POSI[currentCase + 1][0],
				Constant.BUGS_PICS_POSI[currentCase + 1][1], paint);

	}

}
