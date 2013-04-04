package com.hustunique.utils;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.collision.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import android.graphics.Bitmap;

import com.hustunique.gameroles.MyColorCircle;
import com.hustunique.gameroles.MyColorRect;
import com.hustunique.gameroles.MyPolygon;

public class Box2DUtils {
	public static MyColorRect creatBox(float x, float y, float halfWidth,
			float halfHeight, boolean isStatic, World world, Bitmap bitmap) {
		PolygonDef shape = new PolygonDef();
		if (isStatic) {
			shape.density = 0;
		} else {
			shape.density = 1.0f;
		}
		shape.friction = 1f;// 设置摩擦系数
		shape.restitution = 0.1f;// 设置反弹系数
		shape.setAsBox(halfWidth / Constant.RATE, halfHeight / Constant.RATE);
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(x / Constant.RATE, y / Constant.RATE);
		Body body = world.createBody(bodyDef);
		body.createShape(shape);
		body.setMassFromShapes();
		return new MyColorRect(body, halfWidth, halfHeight, bitmap);
	}

	public static MyColorCircle creatCircle(float x, float y, float radius,
			boolean isBullet,boolean isStatic, World world, Bitmap bitmap) {
		CircleDef shape = new CircleDef();
		
		if (isStatic) {
			shape.density = 0;
		} else {
			shape.density = 2.0f;
		}
		shape.friction = 0.5f;// 设置摩擦系数
		shape.radius = radius / Constant.RATE;
		shape.restitution = 0.0f;// 设置反弹系数
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(x / Constant.RATE, y / Constant.RATE);
		bodyDef.isBullet = isBullet;
		Body body = world.createBody(bodyDef);
		body.createShape(shape);
		body.setMassFromShapes();
		return new MyColorCircle(body, radius, bitmap);
	}

	// public static MyPolygon creatPolygon(float x,// x坐标
	// float y,// y坐标
	// float[][] vData,// 顶点坐标
	// boolean isStatic,// 是否为静止的
	// World world// 世界
	// ) {
	// // 创建多边形描述对象
	// PolygonDef shape = new PolygonDef();
	// // 设置密度
	// if (isStatic) {
	// shape.density = 0;
	// } else {
	// shape.density = 2.0f;
	// }
	// // 设置摩擦系数
	// shape.friction = 0.8f;
	// // 设置能量损失率（反弹）
	// shape.restitution = 0.5f;
	// for (float[] fa : vData) {
	// shape.addVertex(new Vec2(fa[0] / Constant.RATE, fa[1]
	// / Constant.RATE));
	// }
	//
	// // 创建刚体描述对象
	// BodyDef bodyDef = new BodyDef();
	// // 设置位置
	// bodyDef.position.set(x / Constant.RATE, y / Constant.RATE);
	// // 在世界中创建刚体
	// Body bodyTemp = world.createBody(bodyDef);
	// // 指定刚体形状
	// bodyTemp.createShape(shape);
	// bodyTemp.setMassFromShapes();
	// return new MyPolygon(bodyTemp, vData);
	//
	// }

	public static MyPolygon creatPolygon(float x,// x坐标
			float y,// y坐标
			float[][][] vData,// 顶点坐标
			boolean isStatic,// 是否为静止的
			World world// 世界
	) {
		// 创建刚体描述对象
		BodyDef bodyDef = new BodyDef();
		// 设置位置
		bodyDef.position.set(x / Constant.RATE, y / Constant.RATE);
		// 在世界中创建刚体
		Body bodyTemp = world.createBody(bodyDef);

		for (int i = 0; i < vData.length; i++) {

			// 创建多边形描述对象
			PolygonDef shape = new PolygonDef();
			// 设置密度
			if (isStatic) {
				shape.density = 0;
			} else {
				shape.density = 2.0f;
			}
			// 设置摩擦系数
			shape.friction = 0.8f;
			// 设置能量损失率（反弹）
			shape.restitution = 0.1f;
			for (float[] fa : vData[i]) {
				shape.addVertex(new Vec2(fa[0] / Constant.RATE, fa[1]
						/ Constant.RATE));
			}
			// 指定刚体形状
			bodyTemp.createShape(shape);
		}

		bodyTemp.setMassFromShapes();
		return new MyPolygon(bodyTemp, vData[0]);

	}
}
