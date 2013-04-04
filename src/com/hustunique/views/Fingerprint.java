package com.hustunique.views;

import com.hustunique.utils.Constant;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;

public class Fingerprint {
	int color = Color.WHITE;
	int currentAge = 0;
	int positionX;
	int positionY;
	RadialGradient radialGradient;

	public Fingerprint(int positionX, int positionY) {
		this.positionX = positionX;
		this.positionY = positionY;
		radialGradient = new RadialGradient(positionX, positionY,
				Constant.FINGER_PRINT_GRADIENT_RADIUS, Color.argb(50, 255, 255,
						255), Color.WHITE, TileMode.MIRROR);
	}

	public boolean isLive() {
		return currentAge <= Constant.FINGER_PRINT_LIFE;
	}

	private int getAlpha() {
		return 200 * (Constant.FINGER_PRINT_LIFE - currentAge)
				/ Constant.FINGER_PRINT_LIFE;
	}

	public void drawSelf(Canvas canvas, Paint paint) {
		if (!isLive()) {
			return;
		}
		paint.setAntiAlias(true);
		paint.setColor(color);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(Constant.FINGER_PRINT_STROKE_WIDTH);
		paint.setShader(radialGradient);
		paint.setAlpha(getAlpha());
		canvas.drawCircle(positionX, positionY, Constant.FINGER_PRINT_RADIUS,
				paint);
		paint.reset();

		currentAge++;
	}
}
