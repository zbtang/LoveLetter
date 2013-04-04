package com.hustunique.views;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class MyImageButton {
	Bitmap buttonBitmap;
	RectF buttonRectF;
	int positionX;
	int positionY;
	public MyImageButton(Bitmap buttonBackground, int positionX, int positionY) {
		this.buttonBitmap = buttonBackground;
		this.positionX = positionX;
		this.positionY = positionY;
		buttonRectF = new RectF(positionX, positionY, positionX
				+ buttonBackground.getWidth(), positionY
				+ buttonBitmap.getHeight());
	}

	public void setButtonBitmap(Bitmap buttonBitmap) {
		this.buttonBitmap = buttonBitmap;
	}

	public void drawSelf(Canvas canvas,Paint paint) {
		if (buttonBitmap!=null) {
			canvas.drawBitmap(buttonBitmap, positionX, positionY, paint);
		}
	}
	public Bitmap getButtonBitmap() {
		return buttonBitmap;
	}
	public RectF getButtonRectF() {
		return buttonRectF;
	}
	public int getPositionX() {
		return positionX;
	}
	public int getPositionY() {
		return positionY;
	}
}
