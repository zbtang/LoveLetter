package com.hustunique.controls;

import com.hustunique.utils.Constant;
import com.hustunique.views.GameView;

public class DrawThread extends Thread {
	GameView gameView;
	public boolean flag = false;

	public DrawThread(GameView gameView) {
		this.gameView = gameView;
		this.flag = true;
	}

	@Override
	public void run() {
		while (flag) {
			if (!gameView.isShowDeadDia) {
				gameView.levelManager.myWorld.step(Constant.TIME_STEP,
						Constant.ITERA);
			} else {
			}
			gameView.levelManager.sea.move();
			gameView.repaint();
			try {
				Thread.sleep(Constant.SLEEP_SPAN);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
