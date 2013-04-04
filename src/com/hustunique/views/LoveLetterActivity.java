package com.hustunique.views;


import org.jbox2d.common.Vec2;

import com.hustunique.utils.Constant;
import com.hustunique.utils.PicLoadUtil;
import com.umeng.analytics.MobclickAgent;

import dalvik.system.VMRuntime;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

enum CurrentView {
	GAME_VIEW, MAIN_VIEW
}

public class LoveLetterActivity extends Activity {

	public CurrentView currentView;// 当前的界面d
	public GameView gameView;
	MainMenuView mainMenuView;
	public static MediaPlayer mPlayer;

	// 重力感应器
	SensorManager sensorManager;
	Sensor sensor;
	SensorEventListener sensorEventListener;
	boolean isPingban = false;

	public  Handler myHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Constant.GOTO_GAME_VIEW:
				gotoGameView();
				break;
			case Constant.GOTO_MENU_VIEW:
				gotoMainMenu();
				break;
			case Constant.SHOW_FEITIAN:
				Toast.makeText(LoveLetterActivity.this,
						"够啦，够啦。恭喜您已进入无敌飞天模式，但小心别飞出天外去啊～", 6000).show();
				break;
			default:
				break;
			}
		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// Debug.startMethodTracing("onCreat");
		super.onCreate(savedInstanceState);
		initWindow();// 设置全屏，宽屏
		initData();// 设置屏幕宽度和高度并且初始化重力感应器
		// 对内存的优化处理
		VMRuntime.getRuntime().setMinimumHeapSize(Constant.CWJ_HEAP_SIZE);
		VMRuntime.getRuntime().setTargetHeapUtilization(
				Constant.TARGET_HEAP_UTILIZATION);

		Constant.changeRatio();// 动态自适应屏幕的方法，对各种资源的尺寸的适配
		PicLoadUtil.loadPics(this.getResources(), Constant.PICID_STATIC);// 加载图片，并进行缩放
		gotoMainMenu();

		// Debug.stopMethodTracing();
	}

	public void gotoMainMenu() {

		mainMenuView = new MainMenuView(this);
		setContentView(mainMenuView);
		currentView = CurrentView.MAIN_VIEW;

		if (Constant.PLAY_BACK_MUSIC) {
			musicOn();
		}

	}

	public void musicOn() {
		if (mPlayer == null || !mPlayer.isPlaying()) {
			mPlayer = MediaPlayer.create(this, R.raw.backmusic);
			mPlayer.setLooping(true);
			mPlayer.start();
		}
	}

	public void musicOff() {
		if (mPlayer != null && mPlayer.isPlaying()) {
			mPlayer.stop();
			mPlayer.release();
			mPlayer = null;
		}
	}

	public void gotoGameView() {
		gameView = new GameView(this);
		setContentView(gameView);
		currentView = CurrentView.GAME_VIEW;
		if (Constant.PLAY_BACK_MUSIC) {
			musicOn();
		}
	}

	private void initData() {
		// 初始化
		Constant.CURRENT_LEVEL = 0;
		Constant.PIC_ARRAY = new Bitmap[Constant.PICS_ID.length];
		Constant.IS_FEITIAN = false;
		// uMeng debug mode
		MobclickAgent.setDebugMode(true);
		MobclickAgent.onError(this);// catch the FC

		// 获取屏幕尺寸
		Display display = getWindowManager().getDefaultDisplay();
		if (display.getWidth() > display.getHeight()) {
			Constant.SCREEN_WIDTH = display.getWidth();
			Constant.SCREEN_HEIGHT = display.getHeight();
		} else {
			Constant.SCREEN_WIDTH = display.getHeight();
			Constant.SCREEN_HEIGHT = display.getWidth();
		}// 保证获得的宽是较为长的那一边

		// 初始化重力感应器
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		// 处理监听器
		sensorEventListener = new MySensorEventListener();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 注册监听器
		sensorManager.registerListener(sensorEventListener, sensor,
				SensorManager.SENSOR_DELAY_GAME);
		MobclickAgent.onResume(this);
		switch (currentView) {
		case GAME_VIEW:
			gameView.onResume();
			break;
		case MAIN_VIEW:
			mainMenuView.onResume();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 取消监听器
		sensorManager.unregisterListener(sensorEventListener);
		MobclickAgent.onPause(this);
		switch (currentView) {
		case GAME_VIEW:
			gameView.onPause();
			break;
		case MAIN_VIEW:
			try {
				mainMenuView.onPause();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mPlayer != null && mPlayer.isPlaying()) {
			mPlayer.stop();
			mPlayer = null;
		}
	}

	@Override
	public void onBackPressed() {
		if (currentView == CurrentView.GAME_VIEW) {
			gotoMainMenu();
		} else if (currentView == CurrentView.MAIN_VIEW) {
			if (mainMenuView.isDrawAbout) {
				mainMenuView.isDrawAbout = false;
			} else if (mainMenuView.isDrawHelp) {
				mainMenuView.isDrawHelp = false;
			} else {
				this.finish();
			}
		}
	}

	private void initWindow() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置为全屏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// 设置为横屏
	}

	class MySensorEventListener implements SensorEventListener {

		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}

		public void onSensorChanged(SensorEvent event) {
			float x = event.values[SensorManager.DATA_X];
			float y = event.values[SensorManager.DATA_Y];
			float z = event.values[SensorManager.DATA_Z];
			changeBallV(x, y, z);
		}

		private void changeBallV(float x, float y, float z) {
			if (currentView != CurrentView.GAME_VIEW) {
				return;
			} else if (gameView.levelManager.contactListener.isShowHustUnique) {
				return;
			}
			if (isPingban) {
				if (currentView == CurrentView.GAME_VIEW) {
					// 降低灵敏度
					if (x < Constant.SENSOR_YUZHI && x > -Constant.SENSOR_YUZHI) {
						return;
					}

					Vec2 addedGritivity = new Vec2(
							-x * Constant.VELOCITY_RATE,
							gameView.levelManager.spirit.ball.body.m_linearVelocity.y);
					gameView.levelManager.spirit.ball.body
							.setLinearVelocity(addedGritivity);
				}
			} else {
				if (currentView == CurrentView.GAME_VIEW) {
					// 降低灵敏度
					if (y < Constant.SENSOR_YUZHI && y > -Constant.SENSOR_YUZHI) {
						return;
					}
					Vec2 addedGritivity = new Vec2(
							y * Constant.VELOCITY_RATE,
							gameView.levelManager.spirit.ball.body.m_linearVelocity.y);
					gameView.levelManager.spirit.ball.body
							.setLinearVelocity(addedGritivity);
				}
			}

		}
	}
}