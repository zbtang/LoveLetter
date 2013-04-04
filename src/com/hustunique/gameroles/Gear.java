package com.hustunique.gameroles;

import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

import com.hustunique.utils.Box2DUtils;
import com.hustunique.utils.Constant;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Gear extends GameRole {
	MyPolygon bigChiLun;// 第七关中的大齿轮
	MyPolygon smallChiLun;// 第七关中的大齿轮内圆
	MyColorCircle bigZhiJia;// 第七关中的小齿轮
	MyColorCircle smallZhiJia;// 第七关中的小齿轮内圆
	// 第七关的旋转关节
	RevoluteJointDef bigRevoluteJointDef;
	RevoluteJoint bigRevoluteJoint;
	RevoluteJoint smallRevoluteJoint;
	RevoluteJointDef smallRevoluteJointDef;

	public Gear(World myWorld) {
		super(myWorld);
	}

	@Override
	public void creatSelf() {
		bigChiLun = Box2DUtils.creatPolygon(0, 0, Constant.BIG_CHILUN_POSI,
				false, myWorld);
		bigZhiJia = Box2DUtils.creatCircle(Constant.PICS_INIT_POSITION[21][0],
				Constant.PICS_INIT_POSITION[21][1], Constant.BIG_CHILUN_RADIUS,
				false, true, myWorld, null);
		bigZhiJia.body.getShapeList().m_isSensor = true;
		bigRevoluteJointDef = new RevoluteJointDef();
		bigRevoluteJointDef.initialize(bigChiLun.body, bigZhiJia.body,
				bigZhiJia.body.getPosition());
		bigRevoluteJointDef.enableLimit = false;
		bigRevoluteJointDef.maxMotorTorque = 100000;
		bigRevoluteJointDef.motorSpeed = (float) (-Math.PI / 3);
		bigRevoluteJointDef.enableMotor = true;
		bigRevoluteJoint = (RevoluteJoint) myWorld
				.createJoint(bigRevoluteJointDef);

		smallChiLun = Box2DUtils.creatPolygon(0, 0, Constant.SMALL_CHILUN_POSI,
				false, myWorld);
		smallZhiJia = Box2DUtils.creatCircle(
				Constant.PICS_INIT_POSITION[22][0],
				Constant.PICS_INIT_POSITION[22][1],
				Constant.SMALL_CHILUN_RADIUS, false, true, myWorld, null);
		smallZhiJia.body.getShapeList().m_isSensor = true;
		smallRevoluteJointDef = new RevoluteJointDef();
		smallRevoluteJointDef.initialize(smallChiLun.body, smallZhiJia.body,
				smallZhiJia.body.getPosition());
		smallRevoluteJointDef.enableLimit = false;
		smallRevoluteJointDef.maxMotorTorque = 100000;
		smallRevoluteJointDef.motorSpeed = (float) (Math.PI / 3);
		smallRevoluteJointDef.enableMotor = true;
		smallRevoluteJoint = (RevoluteJoint) myWorld
				.createJoint(smallRevoluteJointDef);
	}

	@Override
	public void drawSelf(Canvas canvas, Paint paint) {
		drawRevoluteBitmap(canvas, paint, bigZhiJia.body.getPosition().x
				* Constant.RATE,
				bigZhiJia.body.getPosition().y * Constant.RATE,
				-bigRevoluteJoint.getJointAngle(), Constant.PIC_ARRAY[21]);
		drawRevoluteBitmap(canvas, paint, smallZhiJia.body.getPosition().x
				* Constant.RATE, smallZhiJia.body.getPosition().y
				* Constant.RATE, -smallRevoluteJoint.getJointAngle(),
				Constant.PIC_ARRAY[22]);
	}

	private void drawRevoluteBitmap(Canvas canvas, Paint paint, float x,
			float y, float jointAngle, Bitmap bitmap) {
		canvas.save();
		// Matrix translationMatrix=new Matrix();
		// Matrix rotateMatrix=new Matrix();
		// Matrix complexMatrix=new Matrix();
		// translationMatrix.setTranslate(x, y);
		// rotateMatrix.setRotate(jointAngle);
		// complexMatrix.setConcat(rotateMatrix, translationMatrix);
		canvas.translate(x, y);
		canvas.rotate((float) (jointAngle * 180f / Math.PI));
		canvas.drawBitmap(bitmap, -bitmap.getWidth() / 2,
				-bitmap.getHeight() / 2, paint);
		canvas.restore();
	}
}
