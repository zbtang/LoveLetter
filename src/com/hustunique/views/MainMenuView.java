package com.hustunique.views;

import java.util.ArrayList;
import java.util.List;

import com.hustunique.controls.MainMenuViewDrawThread;
import com.hustunique.utils.Constant;
import com.hustunique.utils.PicLoadUtil;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainMenuView extends SurfaceView implements SurfaceHolder.Callback {
	MainMenuViewDrawThread mainMenuViewDrawThread;
	LoveLetterActivity jbox2DtestActivity;
	Paint paint;

	MyImageButton musicImageButton;// 音乐播放按钮
	MyImageButton helpImageButton;// 帮助按钮
	MyImageButton aboutImageButton;// 关于按钮
	MyImageButton playImageButton;// 开始按钮
	List<Fingerprint> fingerprints;

	int touchTimes = 0;
	static final int maxTouchTimes = 10;
	long preTouchTime;

	public int oceanXOffset;// 海洋的x方向偏移

	boolean isTouch = false;// 是否触摸屏幕
	boolean isDrawHelp = false;
	boolean isDrawAbout = false;
	// 触摸位置
	int touchPositionX;
	int touchPositionY;

	int step = 40;// 按钮渐变时的循环时间
	int stepTemp = step;
	boolean isIncrese = false;

	public MainMenuView(LoveLetterActivity context) {
		super(context);
		this.jbox2DtestActivity = context;
		this.getHolder().addCallback(this);
		PicLoadUtil.loadPics(getResources(), Constant.PICID_MENU);

		playImageButton = new MyImageButton(
				Constant.PIC_ARRAY[Constant.MAIN_MENU_PISC_ID[3]],
				Constant.PICS_INIT_POSITION[Constant.MAIN_MENU_PISC_ID[3]][0],
				Constant.PICS_INIT_POSITION[Constant.MAIN_MENU_PISC_ID[3]][1]);
		aboutImageButton = new MyImageButton(
				Constant.PIC_ARRAY[Constant.MAIN_MENU_PISC_ID[5]],
				Constant.PICS_INIT_POSITION[Constant.MAIN_MENU_PISC_ID[5]][0],
				Constant.PICS_INIT_POSITION[Constant.MAIN_MENU_PISC_ID[5]][1]);
		helpImageButton = new MyImageButton(
				Constant.PIC_ARRAY[Constant.MAIN_MENU_PISC_ID[4]],
				Constant.PICS_INIT_POSITION[Constant.MAIN_MENU_PISC_ID[4]][0],
				Constant.PICS_INIT_POSITION[Constant.MAIN_MENU_PISC_ID[4]][1]);
		musicImageButton = new MyImageButton(
				Constant.PIC_ARRAY[(!Constant.PLAY_BACK_MUSIC ? Constant.MAIN_MENU_PISC_ID[8]
						: Constant.MAIN_MENU_PISC_ID[9])],
				Constant.PICS_INIT_POSITION[28][0],
				Constant.PICS_INIT_POSITION[28][1]);

		paint = new Paint();
		paint.setAntiAlias(true);
		fingerprints = new ArrayList<Fingerprint>();

	}

	@Override
	protected void onDraw(Canvas canvas) {
		drawBackGroundAndTitle(canvas);
		drawButton(canvas);
		drawMovableOcean(canvas, oceanXOffset);
		// if (isTouch) {
		// drawCircle(canvas);
		// }
		if (isDrawAbout) {
			drawAbout(canvas);
		}
		if (isDrawHelp) {
			drawHelp(canvas);
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

	private void drawHelp(Canvas canvas) {
		canvas.drawARGB(100, 0, 0, 0);
		canvas.drawBitmap(
				Constant.PIC_ARRAY[50],
				Constant.PICS_INIT_POSITION[11][0] + Constant.SCREEN_WIDTH / 11,
				Constant.PICS_INIT_POSITION[11][1] - Constant.SCREEN_HEIGHT
						/ 13, paint);
	}

	private void drawAbout(Canvas canvas) {
		canvas.drawARGB(100, 0, 0, 0);
		canvas.drawBitmap(
				Constant.PIC_ARRAY[49],
				Constant.PICS_INIT_POSITION[11][0] + Constant.SCREEN_WIDTH / 11,
				Constant.PICS_INIT_POSITION[11][1] - Constant.SCREEN_HEIGHT
						/ 13, paint);
	}

	// private void drawCircle(Canvas canvas) {
	// paint.setColor(Color.WHITE);
	// paint.setStyle(Style.STROKE);
	// paint.setStrokeWidth(40);
	// RadialGradient radialGradient = new RadialGradient(touchPositionX,
	// touchPositionY, 20, Color.argb(50, 255, 255, 255), Color.WHITE,
	// TileMode.MIRROR);
	//
	// paint.setShader(radialGradient);
	// canvas.drawCircle(touchPositionX, touchPositionY, 60, paint);
	// paint.reset();
	// }

	private void drawMovableOcean(Canvas canvas, int xOffset) {// MAIN_MENU_PISC_ID数组中的第三个必须寸海洋
																// 图片的数组下标
		canvas.drawBitmap(
				Constant.PIC_ARRAY[Constant.MAIN_MENU_PISC_ID[2]],
				xOffset
						% Constant.PIC_ARRAY[Constant.MAIN_MENU_PISC_ID[2]]
								.getWidth(),
				Constant.PICS_INIT_POSITION[Constant.MAIN_MENU_PISC_ID[2]][1],
				paint);
		canvas.drawBitmap(
				Constant.PIC_ARRAY[Constant.MAIN_MENU_PISC_ID[2]],
				(xOffset % Constant.PIC_ARRAY[Constant.MAIN_MENU_PISC_ID[2]]
						.getWidth())
						+ Constant.PIC_ARRAY[Constant.MAIN_MENU_PISC_ID[2]]
								.getWidth() - 1,// 由于像素不是特别连贯，所以减去了一个像素
				Constant.PICS_INIT_POSITION[Constant.MAIN_MENU_PISC_ID[2]][1],
				paint);
	}

	private void drawButton(Canvas canvas) {
		musicImageButton.drawSelf(canvas, paint);
		playImageButton.drawSelf(canvas, paint);
		aboutImageButton.drawSelf(canvas, paint);
		helpImageButton.drawSelf(canvas, paint);
		// if (isIncrese) {
		// stepTemp++;
		// } else {
		// stepTemp--;
		// }
		// if (stepTemp == -step || stepTemp == step) {
		// if (isIncrese) {
		// isIncrese = false;
		// } else {
		// isIncrese = true;
		// }
		// }
		// paint.setColor(Color.WHITE);
		// BlurMaskFilter blurMaskFilter = new BlurMaskFilter(15 + stepTemp * 10
		// / step, Blur.OUTER);
		// paint.setMaskFilter(blurMaskFilter);
		// canvas.drawRoundRect(playImageButton.buttonRectF, 60, 60, paint);
		paint.reset();

	}

	private void drawBackGroundAndTitle(Canvas canvas) {
		for (int i = 0; i < 2; i++) {// 因为MAIN—MENU——PIC——ID数组中的前两个存储的分别是背景和标题图片的数组下标
			canvas.drawBitmap(
					Constant.PIC_ARRAY[Constant.MAIN_MENU_PISC_ID[i]],
					Constant.PICS_INIT_POSITION[Constant.MAIN_MENU_PISC_ID[i]][0],
					Constant.PICS_INIT_POSITION[Constant.MAIN_MENU_PISC_ID[i]][1],
					paint);
		}
	}

	public void repaint() {
		SurfaceHolder holder = this.getHolder();
		Canvas canvas = holder.lockCanvas();// 锁住画布
		try {
			synchronized (holder) {// 锁住holder
				onDraw(canvas);// 绘制
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (canvas != null) {
				holder.unlockCanvasAndPost(canvas);
			}
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		System.out.println("surfaceview changed");
	}

	public void surfaceCreated(SurfaceHolder holder) {
		Constant.MAIN_MENU_DRAW_THREAD_FLAG = true;
		if (mainMenuViewDrawThread == null) {
			mainMenuViewDrawThread = new MainMenuViewDrawThread(this);
			mainMenuViewDrawThread.start();
		}

		System.out.println("surfaceview created");
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		Constant.MAIN_MENU_DRAW_THREAD_FLAG = false;
		mainMenuViewDrawThread = null;
		for (int i = 0; i < Constant.PICID_MENU.length; i++) {
			Constant.PIC_ARRAY[Constant.PICID_MENU[i]] = null;
		}
		System.gc();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		touchPositionX = (int) event.getX();
		touchPositionY = (int) event.getY();
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			isTouch = true;
			fingerprints.add(new Fingerprint(touchPositionX, touchPositionY));
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			isTouch = false;
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (playImageButton.buttonRectF.contains(touchPositionX,
					touchPositionY)) {
				startGame();
			} else if (helpImageButton.buttonRectF.contains(touchPositionX,
					touchPositionY)) {
				help();
			} else if (aboutImageButton.buttonRectF.contains(touchPositionX,
					touchPositionY)) {
				about();
			} else if (musicImageButton.buttonRectF.contains(touchPositionX,
					touchPositionY)) {
				musicSwitch();
			} else {
				if (touchTimes == 0) {
					preTouchTime = System.currentTimeMillis();
					touchTimes++;
				} else {
					if ((System.currentTimeMillis() - preTouchTime) < 1000) {
						touchTimes++;
						preTouchTime = System.currentTimeMillis();
						if (touchTimes > maxTouchTimes) {
							jbox2DtestActivity.myHandler
									.sendEmptyMessage(Constant.SHOW_FEITIAN);
							Constant.IS_FEITIAN = true;
							touchTimes = 0;
						}
					} else {
						preTouchTime = System.currentTimeMillis();
						touchTimes = 0;
					}
				}
			}

		}
		return true;
	}

	private void musicSwitch() {
		Constant.PLAY_BACK_MUSIC = !Constant.PLAY_BACK_MUSIC;
		musicImageButton
				.setButtonBitmap(Constant.PIC_ARRAY[(!Constant.PLAY_BACK_MUSIC ? Constant.MAIN_MENU_PISC_ID[8]
						: Constant.MAIN_MENU_PISC_ID[9])]);
		if (Constant.PLAY_BACK_MUSIC) {
			jbox2DtestActivity.musicOn();
		} else {
			jbox2DtestActivity.musicOff();
		}
	}

	private void about() {
		if (!isDrawHelp) {
			isDrawAbout = true;
		}
	}

	private void help() {
		if (!isDrawAbout) {
			isDrawHelp = true;
		}

	}

	private void startGame() {
		jbox2DtestActivity.myHandler.sendEmptyMessage(Constant.GOTO_GAME_VIEW);
	}

	public void onPause() throws InterruptedException {
	}

	public void onResume() {
	}
}
