package com.hustunique.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class PicLoadUtil {
	public static Bitmap loadBM(Resources res, int id) {
		return BitmapFactory.decodeResource(res, id);
	}

	// 缩放图片的方法
	public static Bitmap scaleToFitXYRatio(Bitmap bm, float xRatio, float yRatio)// 缩放图片的方法
	{
		float width = bm.getWidth(); // 图片宽度
		float height = bm.getHeight();// 图片高度
		Matrix m1 = new Matrix();
		m1.postScale(xRatio, yRatio);
		Bitmap bmResult = Bitmap.createBitmap(bm, 0, 0, (int) width,
				(int) height, m1, true);// 声明位图
		return bmResult;
	}

	public static void loadPics(Resources resources, short ids[]) {
		for (int i = 0; i < ids.length; i++) {
			Constant.PIC_ARRAY[ids[i]] = scaleToFitXYRatio(
					loadBM(resources, Constant.PICS_ID[ids[i]]),
					Constant.xMainRatio, Constant.yMainRatio);
		}
	}

	public static Bitmap getPicByID(Resources res, int ID) {
		Bitmap bitmapResult = loadBM(res, Constant.PICS_ID[ID]);
		bitmapResult = scaleToFitXYRatio(bitmapResult, Constant.xMainRatio,
				Constant.yMainRatio);
		return bitmapResult;
	}
}
