package com.hustunique.gameroles;

import org.jbox2d.dynamics.Body;

import com.hustunique.utils.Constant;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class MyColorCircle extends MyBody {
	float radius;
	Bitmap bitmap;

	int i = -50;
	int step = 4;

	public MyColorCircle(Body body, float radius, Bitmap bitmap) {
		this.body = body;
		this.radius = radius;
		this.bitmap = bitmap;
	}

	@Override
	public void drawSelf(Canvas canvas, Paint paint) {
		paint.setColor(Color.RED);
		float x = body.getPosition().x * Constant.RATE;
		float y = body.getPosition().y * Constant.RATE;
		// canvas.drawCircle(x, y, radius, paint);
		// paint.setStyle(Paint.Style.STROKE);
		// paint.setStrokeWidth(1);
		// paint.setColor(Color.RED);
		if (i < step) {
			canvas.drawBitmap(bitmap, x - radius, y - radius, paint);
		} else if (i < step * 2) {
			canvas.drawBitmap(Constant.PIC_ARRAY[1], x - radius, y - radius,
					paint);
		} else if (i < step * 3) {
			canvas.drawBitmap(Constant.PIC_ARRAY[10], x - radius, y - radius,
					paint);
		} else if (i < step * 4) {
			canvas.drawBitmap(Constant.PIC_ARRAY[1], x - radius, y - radius,
					paint);
		} else {
			canvas.drawBitmap(bitmap, x - radius, y - radius, paint);
			i = -50;
		}
		i++;
		paint.reset();
	}

}
