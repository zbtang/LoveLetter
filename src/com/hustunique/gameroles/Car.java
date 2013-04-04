package com.hustunique.gameroles;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.PrismaticJoint;
import org.jbox2d.dynamics.joints.PrismaticJointDef;

import com.hustunique.utils.Box2DUtils;
import com.hustunique.utils.Constant;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Car extends GameRole {
	MyPolygon carInLevel6;// 第六关的传送车
	MyPolygon ropeInLevel6;// 第六关中间限制传送车运动的物体
	PrismaticJointDef prismaticJointDef;// 第六关中间的移动关节的描述
	PrismaticJoint prismaticJoint;// 第六关中的移动关节

	public Car(World myWorld) {
		super(myWorld);
	}

	@Override
	public void creatSelf() {
		carInLevel6 = Box2DUtils.creatPolygon(0, 0, Constant.CAR_POSITION_IN_6,
				false, myWorld);
		ropeInLevel6 = Box2DUtils.creatPolygon(0, 0, Constant.ROPE_POSITION,
				true, myWorld);
		prismaticJointDef = new PrismaticJointDef();
		prismaticJointDef.initialize(carInLevel6.body, ropeInLevel6.body,
				carInLevel6.body.getWorldCenter(), new Vec2(1, 0));
		prismaticJointDef.lowerTranslation = -10f;
		prismaticJointDef.upperTranslation = 10f;
		prismaticJointDef.enableLimit = true;
		prismaticJointDef.maxMotorForce = 6000f;
		prismaticJointDef.motorSpeed = -Constant.CAR_SPEED;
		prismaticJointDef.enableMotor = true;
		prismaticJoint = (PrismaticJoint) myWorld
				.createJoint(prismaticJointDef);
	}

	@Override
	public void drawSelf(Canvas canvas, Paint paint) {
		canvas.drawBitmap(Constant.PIC_ARRAY[19],
				carInLevel6.body.getPosition().x * Constant.RATE
						+ Constant.PICS_INIT_POSITION[19][0],
				carInLevel6.body.getPosition().y * Constant.RATE
						+ Constant.PICS_INIT_POSITION[19][1], paint);
		if (prismaticJoint.getJointTranslation() < Constant.CAR_RIGHT_LIMIT) {
			prismaticJoint.setMotorSpeed(Constant.CAR_SPEED);
		} else if (prismaticJoint.getJointTranslation() > Constant.CAR_LEFT_LIMIT) {
			prismaticJoint.setMotorSpeed(-Constant.CAR_SPEED);
		}
	}

}
