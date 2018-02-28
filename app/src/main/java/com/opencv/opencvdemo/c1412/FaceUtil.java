package com.opencv.opencvdemo.c1412;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import java.io.File;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * Created by Pumatus on 2018/2/24.
 */

public class FaceUtil {
	
	private static final String TAG = FaceUtil.class.getName();
	
	private FaceUtil() {
	
	}
	
	public static boolean saveImage(Context context, Mat image, Rect rect, String fileName) {
		// 原图置灰
		Mat grayMat = new Mat();
		Imgproc.cvtColor(image, grayMat, Imgproc.COLOR_BGR2GRAY);
		// 把检测到的人脸重新定义大小后保存成文件
		Mat sub = grayMat.submat(rect);
		Mat mat = new Mat();
		Size size = new Size(100, 100);
		Imgproc.resize(sub, mat, size);
		return Imgcodecs.imwrite(getFilePath(context, fileName), mat);
	}
	
	public static boolean deleteImage(Context context, String fileName) {
		// 文件名不能为空
		if (TextUtils.isEmpty(fileName)) {
			return false;
		}
		// 文件路径不能为空
		String path = getFilePath(context, fileName);
		if (path != null) {
			File file = new File(path);
			return file.exists() && file.delete();
		} else {
			return false;
		}
	}
	
	public static Bitmap getImage(Context context, String fileName) {
		String filePath = getFilePath(context, fileName);
		if (TextUtils.isEmpty(filePath)) {
			return null;
		} else {
			return BitmapFactory.decodeFile(filePath);
		}
	}
	
	private static String getFilePath(Context context, String fileName) {
		if (TextUtils.isEmpty(fileName)) {
			return null;
		}
		return context.getApplicationContext().getFilesDir().getPath() + fileName + ".jpg";
	}
}