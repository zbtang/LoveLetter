package com.hustunique.controls;

import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.ContactListener;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.ContactPoint;
import org.jbox2d.dynamics.contacts.ContactResult;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.hustunique.gameroles.Bugs;
import com.hustunique.gameroles.Car;
import com.hustunique.gameroles.Gear;
import com.hustunique.gameroles.Heads;
import com.hustunique.gameroles.HustUnique;
import com.hustunique.gameroles.Letters;
import com.hustunique.gameroles.MyBody;
import com.hustunique.gameroles.NextIcon;
import com.hustunique.gameroles.Sea;
import com.hustunique.gameroles.Spirit;
import com.hustunique.gameroles.Spring;
import com.hustunique.gameroles.WorldBorder;
import com.hustunique.utils.Constant;
import com.hustunique.views.GameView;

public class LevelManager {
	String logTag = "ContactListener";

	GameView gameView;

	AABB worldAabb;
	public MyContactListener contactListener;
	public World myWorld;
	public Spirit spirit;// 小球精灵
	WorldBorder border;
	Sea sea;
	Letters letters;
	Car car;
	Gear gear;
	Heads heads;
	Bugs bugs;
	Spring spring;
	NextIcon nextIcon;
	public HustUnique hustUnique;

	int zimuYOffset = Constant.SCREEN_HEIGHT * 5 / 6;

	public LevelManager(GameView gameView) {
		this.gameView = gameView;
	}

	public void creatWorld() {
		// 创建世界包围盒
		worldAabb = new AABB();
		worldAabb.lowerBound.set(-100f, -100f);
		worldAabb.upperBound.set(200, 200);
		// 设置重力加速度
		Vec2 grativity = new Vec2(0f, Constant.GRATIVITY);
		boolean doSleep = true;// 设置是否可以休眠
		myWorld = new World(worldAabb, grativity, doSleep);

		spirit = new Spirit(myWorld);
		spirit.creatSelf();

		if (Constant.CURRENT_LEVEL != 12) {
			letters = new Letters(myWorld);
			letters.creatSelf();
		}
		if (Constant.CURRENT_LEVEL == 5) {
			car = new Car(myWorld);
			car.creatSelf();
		}
		if (Constant.CURRENT_LEVEL == 6) {
			gear = new Gear(myWorld);
			gear.creatSelf();
		}
		if (Constant.CURRENT_LEVEL == 8) {
			heads = new Heads(myWorld);
			heads.creatSelf();
		}
		if (Constant.CURRENT_LEVEL == 12) {
			bugs = new Bugs(myWorld);
			bugs.creatSelf();
		}
		if (Constant.CURRENT_LEVEL == 21) {
			spring = new Spring(myWorld);
			spring.creatSelf();
		}

		if (Constant.CURRENT_LEVEL == 27) {
			hustUnique = new HustUnique(myWorld, spirit);
			hustUnique.creatSelf();

		} else {
			nextIcon = new NextIcon(myWorld);
			nextIcon.creatSelf();
		}
		sea = new Sea(myWorld);
		sea.creatSelf();

		border = new WorldBorder(myWorld);
		border.creatSelf();

		contactListener = new MyContactListener();
		myWorld.setContactListener(contactListener);
	}

	public void gotoNextLevel() {
		gameView.drawThread.flag = false;
		Body bodyTemp = myWorld.getBodyList();
		do {
			myWorld.destroyBody(bodyTemp);
		} while ((bodyTemp = bodyTemp.getNext()) != null);
		myWorld = null;
		System.gc();
		if (Constant.CURRENT_LEVEL < Constant.BODY_POINT.length - 1) {
			Constant.CURRENT_LEVEL += 1;
			gameView.activity.myHandler
					.sendEmptyMessage(Constant.GOTO_GAME_VIEW);
		}
	}

	public void drawSence(Canvas canvas, Paint paint) {
		if (canvas == null) {
			return;
		}
		canvas.clipRect(0, 0, Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGHT);
		drawLevelBackground(canvas, paint);
		spirit.drawSelf(canvas, paint);
		switch (Constant.CURRENT_LEVEL) {
		case 5:
			car.drawSelf(canvas, paint);
			break;
		case 6:
			gear.drawSelf(canvas, paint);
			break;
		case 12:
			bugs.drawSelf(canvas, paint);
			break;
		default:
			break;
		}
		if (Constant.CURRENT_LEVEL != 27) {
			nextIcon.drawSelf(canvas, paint);
		}
		if (contactListener.isShowHustUnique) {
			hustUnique.drawSelf(canvas, paint);
		}
		sea.drawSelf(canvas, paint);

		if (hustUnique != null && hustUnique.isEnd) {
			drawZimu(canvas, paint);
		}
	}

	private void drawZimu(Canvas canvas, Paint paint) {
		if (zimuYOffset + Constant.PIC_ARRAY[51].getHeight() > Constant.SCREEN_HEIGHT * 3 / 4) {
			zimuYOffset--;
		}
		canvas.drawARGB(100, 0, 0, 0);
		canvas.save();
		canvas.clipRect(0, Constant.SCREEN_HEIGHT / 4, Constant.SCREEN_WIDTH,
				Constant.SCREEN_HEIGHT * 3 / 4);
		canvas.drawBitmap(Constant.PIC_ARRAY[51], 0, zimuYOffset, paint);
		canvas.restore();
	}

	private void drawLevelBackground(Canvas canvas, Paint paint) {
		canvas.drawBitmap(
				Constant.PIC_ARRAY[Constant.PICS_ID_FOR_LEV_BG[Constant.CURRENT_LEVEL]],
				Constant.xOffset, Constant.yOffset, paint);
	}

	public class MyContactListener implements ContactListener {

		public boolean isOnGround = false;// 判断小球现在是否接触了地面
		public boolean isShowHustUnique = false;

		boolean haveBody(ContactPoint arg0, MyBody myBody) {
			if (myBody != null) {
				return arg0.shape1.getBody() == myBody.body
						|| arg0.shape2.getBody() == myBody.body;
			}
			return false;
		}

		public void add(ContactPoint arg0) {
			// Log.i(logTag, "add");

			if (haveBody(arg0, spirit.ball)) {
				isOnGround = true;
				if (haveBody(arg0, sea.deadLine)) {
					gameView.gameOver();
					return;
				}
				if (Constant.CURRENT_LEVEL != 27
						&& haveBody(arg0, nextIcon.next)) {
					gotoNextLevel();
					return;

				}
				if (Constant.CURRENT_LEVEL == 21 && haveBody(arg0, spring.gate)) {
					changeBallPositon();
					return;
				}
				if (Constant.CURRENT_LEVEL == 27 && !hustUnique.isEnd
						&& haveBody(arg0, hustUnique.hustUnique)) {
					isShowHustUnique = true;

					myWorld.setGravity(new Vec2(0, 0));
					spirit.ball.body.setLinearVelocity(new Vec2(0, 0));

					Log.w("zuihou", "世界停止了");
					return;
				}

			}
		}

		protected void changeBallPositon() {
			spirit.ball.body.setLinearVelocity(new Vec2(0, -25));

		}

		public void persist(ContactPoint arg0) {
			// Log.i(logTag, "persist");
			if (!isOnGround && haveBody(arg0, spirit.ball)) {
				isOnGround = true;
			}
		}

		public void remove(ContactPoint arg0) {
			// Log.i(logTag, "remove");

			if (arg0.shape1.getBody() == spirit.ball.body
					|| arg0.shape2.getBody() == spirit.ball.body) {
				isOnGround = false;
			}
		}

		public void result(ContactResult arg0) {
			// Log.i(logTag, "result");
		}
	}
}
