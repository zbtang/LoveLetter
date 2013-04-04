package com.hustunique.gameroles;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import com.hustunique.utils.Box2DUtils;
import com.hustunique.utils.Constant;

import android.graphics.Canvas;
import android.graphics.Paint;

public class HustUnique extends GameRole {
	public MyPolygon hustUnique;
	Spirit spirit;
	int alphaIn28 = 0;
	public boolean isEnd = false;

	public HustUnique(World myWorld, Spirit spirit) {
		super(myWorld);
		this.spirit = spirit;
	}

	@Override
	public void creatSelf() {
		hustUnique = Box2DUtils.creatPolygon(0, 0, Constant.HUST_UNIQUE, true,
				myWorld);
		hustUnique.body.getShapeList().m_isSensor = true;
	}

	@Override
	public void drawSelf(Canvas canvas, Paint paint) {
		alphaIn28++;
		if (alphaIn28 < 255) {
			paint.setAlpha(alphaIn28);
			paint.setAntiAlias(true);
		} else if (alphaIn28 == 255) {
			spirit.ball.body.setXForm(new Vec2(Constant.BALL_POSI[0] / Constant.RATE,
					Constant.BALL_POSI[1] / Constant.RATE), 0);
		} else if (alphaIn28 == 333) {
			isEnd = true;
		}
		canvas.drawBitmap(Constant.PIC_ARRAY[48], Constant.HUST_PIC_POSI[0],
				Constant.HUST_PIC_POSI[1], paint);

		paint.reset();
	}

}
