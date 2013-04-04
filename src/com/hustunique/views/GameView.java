package com.hustunique.views;

import java.util.ArrayList;
import java.util.List;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import com.hustunique.controls.DrawThread;
import com.hustunique.controls.LevelManager;
import com.hustunique.utils.Constant;
import com.hustunique.utils.PicLoadUtil;
import com.hustunique.utils.SoundUtils;
import com.umeng.analytics.MobclickAgent;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
	public LoveLetterActivity activity;
	Paint paint;
	public DrawThread drawThread;
	SoundUtils soundUtils;
	MyImageButton quitButton;
	MyImageButton retryButton;
	List<Fingerprint> fingerprints;
	public boolean isShowDeadDia = false;
	public LevelManager levelManager;

	short jumpSound = 0;

	public GameView(LoveLetterActivity context) {
		super(context);
		this.activity = context;
		PicLoadUtil.loadPics(activity.getResources(),
				Constant.PICID_SPECIAL[Constant.CURRENT_LEVEL]);

		levelManager = new LevelManager(this);
		levelManager.creatWorld();

		this.getHolder().addCallback(this);
		fingerprints = new ArrayList<Fingerprint>();

		if (Constant.PLAY_BACK_MUSIC) {
			soundUtils = new SoundUtils(activity);
			soundUtils.initSound();
		}
		paint = new Paint();
		paint.setAntiAlias(true);
		drawThread = new DrawThread(GameView.this);
		drawThread.start();
	}

	public void gameOver() {
		MobclickAgent.onEvent(activity, Constant.UMENG_EVE_DEAD_LEVEL,
				String.valueOf(Constant.CURRENT_LEVEL));
		isShowDeadDia = true;
		quitButton = new MyImageButton(Constant.PIC_ARRAY[12],
				Constant.PICS_INIT_POSITION[12][0],
				Constant.PICS_INIT_POSITION[12][1]);
		retryButton = new MyImageButton(Constant.PIC_ARRAY[13],
				Constant.PICS_INIT_POSITION[13][0],
				Constant.PICS_INIT_POSITION[13][1]);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		levelManager.drawSence(canvas, paint);
		if (isShowDeadDia) {
			drawDeadDia(canvas);
		}

		// 绘制指纹
		if (fingerprints.size() > 0) {
			if (!fingerprints.get(0).isLive()) {
				fingerprints.remove(0);
			}
			for (int i = 0; i < fingerprints.size(); i++) {
				fingerprints.get(i).drawSelf(canvas, paint);
			}
		}

	}

	private void drawDeadDia(Canvas canvas) {
		paint.reset();
		canvas.drawARGB(100, 0, 0, 0);
		canvas.drawBitmap(Constant.PIC_ARRAY[11],
				Constant.PICS_INIT_POSITION[11][0],
				Constant.PICS_INIT_POSITION[11][1], paint);
		quitButton.drawSelf(canvas, paint);
		retryButton.drawSelf(canvas, paint);
	}

	// private void drawMask(Canvas canvas) {
	// Bitmap maskBitmap = Bitmap.createBitmap(Constant.SCREEN_WIDTH,
	// Constant.SCREEN_HEIGHT, Config.ARGB_4444);
	// Canvas maskCanvas = new Canvas(maskBitmap);
	// maskCanvas.drawARGB(255, 255, 255, 255);
	//
	// paint.setAlpha(0);
	// paint.setXfermode(new PorterDuffXfermode(Mode.SRC));
	// paint.setAntiAlias(true);
	// // RadialGradient radialGradient = new RadialGradient(
	// // ball.body.getPosition().x * Constant.RATE,
	// // ball.body.getPosition().y * Constant.RATE, 200, Color.WHITE,
	// // Color.alpha(0), TileMode.MIRROR);
	// // paint.setShader(radialGradient);
	// // paint.setStyle(Style.STROKE);
	// // paint.setStrokeWidth(100);
	// maskCanvas.drawCircle(ball.body.getPosition().x * Constant.RATE,
	// ball.body.getPosition().y * Constant.RATE, 200, paint);
	// paint.reset();
	// canvas.drawBitmap(maskBitmap, 0, 0, paint);
	//
	// }

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			fingerprints.add(new Fingerprint(x, y));
			if (!levelManager.contactListener.isShowHustUnique
					&& (levelManager.contactListener.isOnGround || Constant.IS_FEITIAN)) {
				levelManager.spirit.ball.body.setLinearVelocity(new Vec2(0,
						Constant.UP_VELOCITY));
				if (Constant.PLAY_BACK_MUSIC&&!isShowDeadDia) {
					soundUtils.playSound(jumpSound, 0);// 播放音效
				}
			}
			if (isShowDeadDia) {
				if (retryButton.buttonRectF.contains(x, y)) {
					retry();
				} else if (quitButton.buttonRectF.contains(x, y)) {
					quit();
				}
			}

			break;
		default:
			break;
		}
		return true;
	}

	private void quit() {
		// activity.finish();
		drawThread.flag = false;
		Body bodyTemp = levelManager.myWorld.getBodyList();
		do {
			levelManager.myWorld.destroyBody(bodyTemp);
		} while ((bodyTemp = bodyTemp.getNext()) != null);
		levelManager.myWorld = null;
		System.gc();
		activity.myHandler.sendEmptyMessage(Constant.GOTO_MENU_VIEW);
	}

	private void retry() {
		drawThread.flag = false;
		Body bodyTemp = levelManager.myWorld.getBodyList();
		do {
			levelManager.myWorld.destroyBody(bodyTemp);
		} while ((bodyTemp = bodyTemp.getNext()) != null);
		levelManager.myWorld = null;
		System.gc();
		// Constant.CURRENT_LEVEL = 0;
		activity.myHandler.sendEmptyMessage(Constant.GOTO_GAME_VIEW);

	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	public void surfaceCreated(SurfaceHolder holder) {
		if (!drawThread.isAlive()) {
			drawThread.flag = true;
			drawThread.start();
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		drawThread.flag = false;
		drawThread = null;
		if (Constant.CURRENT_LEVEL != 0) {
			for (int i = 0; i < Constant.PICID_SPECIAL[Constant.CURRENT_LEVEL - 1].length; i++) {
				Constant.PIC_ARRAY[Constant.PICID_SPECIAL[Constant.CURRENT_LEVEL - 1][i]] = null;
			}
		}
		System.gc();
	}

	public void repaint() {
		SurfaceHolder holder = this.getHolder();
		Canvas canvas = holder.lockCanvas();
		try {
			synchronized (holder) {
				onDraw(canvas);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (canvas != null) {
				holder.unlockCanvasAndPost(canvas);
			}
		}
	}

	public void onPause() {

	}

	public void onResume() {

	}
}
