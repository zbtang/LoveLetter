package com.hustunique.utils;

import com.hustunique.views.LoveLetterActivity;
import com.hustunique.views.R;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.SparseIntArray;

public class SoundUtils {
	SoundPool soundPool;
	SparseIntArray soundPoolIntArray;
	LoveLetterActivity jbox2DtestActivity;

	public SoundUtils(LoveLetterActivity activity) {
		this.jbox2DtestActivity = activity;
	}

	public void initSound() {
		soundPool = new SoundPool(6,// 最多同时播放的声音个数
				AudioManager.STREAM_MUSIC,// 音频类型
				100);// 声音品质
		soundPoolIntArray = new SparseIntArray();
		soundPoolIntArray.put(0,
				soundPool.load(jbox2DtestActivity, R.raw.jump, 1));
	}

	public void playSound(int sound, int loop) {
		AudioManager audioManager = (AudioManager) jbox2DtestActivity
				.getSystemService(Context.AUDIO_SERVICE);
		float streamVolumeMax = audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);// 最大音量
		float streamVolumeCurrent = audioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);// 当前音量
		float volume = streamVolumeCurrent / streamVolumeMax;
		soundPool.play(soundPoolIntArray.get(sound)// 声音资源ID
				, volume// 左声道音量
				, volume// 右声道音量
				, 1// 优先级
				, loop// 循环次数
				, 1f);// 回放速度0.5f~2f之间
	}
}
