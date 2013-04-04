package com.hustunique.gameroles;

import org.jbox2d.dynamics.Body;

import com.hustunique.utils.Constant;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class MyColorRect extends MyBody {
	float halfWidth;
	float halfHeight;
	Bitmap bitmap;

	public MyColorRect(Body body, float halfWidth, float halfHeight,
			Bitmap bitmap) {
		this.body = body;
		this.halfWidth = halfWidth;
		this.halfHeight = halfHeight;
		this.bitmap = bitmap;
	}

	@Override
	public void drawSelf(Canvas canvas, Paint paint) {
		if (bitmap==null) {
			return;
		}
		paint.setColor(Color.BLUE);
		canvas.drawBitmap(bitmap,
				Constant.NEXT_POSITION[Constant.CURRENT_LEVEL][0]
						- Constant.NEXT_HALF_WIDTH,
				Constant.NEXT_POSITION[Constant.CURRENT_LEVEL][1]
						- Constant.NEXT_HALF_HEIGHT, paint);
		paint.reset();
	}

}
