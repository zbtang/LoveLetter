package com.hustunique.gameroles;

import org.jbox2d.dynamics.Body;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

public class MyPolygon extends MyBody {
	int color = Color.GREEN;
	float[][] points;

	public MyPolygon(Body body, float[][] points) {
		this.body = body;
		this.points = points;
	}

	@Override
	public void drawSelf(Canvas canvas, Paint paint) {
		paint.setColor(color);
		Path path = new Path();
		path.moveTo(points[0][0], points[0][1]);// 起点
		for (int i = 1; i < points.length; i++) {
			path.lineTo(points[i][0], points[i][1]);
		}
		path.close();// 封闭口
		paint.setStyle(Paint.Style.STROKE);// 设置画笔为实心
		canvas.drawPath(path, paint);
		paint.reset();

	}

}
