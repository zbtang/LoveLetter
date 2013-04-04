package com.hustunique.controls;

import com.hustunique.utils.Constant;
import com.hustunique.views.MainMenuView;

public class MainMenuViewDrawThread extends Thread {
	MainMenuView mainMenuView;

	public MainMenuViewDrawThread(MainMenuView mainMenuView) {
		this.mainMenuView = mainMenuView;
	}

	@Override
	public void run() {
		while (Constant.MAIN_MENU_DRAW_THREAD_FLAG) {

			moveOcean();
			mainMenuView.repaint();
			try {
				Thread.sleep(Constant.SLEEP_SPAN);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	// 主菜单中的海洋的移动
	private void moveOcean() {
		mainMenuView.oceanXOffset -= Constant.OCEAN_MOVE_STEP;
	}
}
