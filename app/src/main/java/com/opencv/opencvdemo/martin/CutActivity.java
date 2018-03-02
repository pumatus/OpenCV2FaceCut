package com.opencv.opencvdemo.martin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import com.opencv.opencvdemo.R;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

/**
 * Created by Pumatus on 2018/2/24.
 * 人脸检测 人脸抠图 Rect填充 保存局部图片
 */

public class CutActivity extends AppCompatActivity implements OnClickListener {
	
	private ImageView imageViewCut;
	private ImageView imageViewFace;
	private Bitmap bitmap;
	
	static {
		System.loadLibrary("opencv_java3");
		System.loadLibrary("opencv_java");
		System.loadLibrary("native-lib");
	}
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main3);
		
		Button button = findViewById(R.id.btn_cut);
		button.setOnClickListener(this);
		imageViewCut = findViewById(R.id.img_cut);
		imageViewFace = findViewById(R.id.img_face);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test1);
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_cut) {
			try {
				detectFace(bitmap, CutActivity.this);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public native long grabCutFromJNI(long m, int l, int t, int r, int b);
	
	/**
	 * opencv实现人脸检测
	 */
	public void detectFace(Bitmap bitmap, Context context) throws Exception {
		Log.e("detectFace", "Running DetectFace ... ");
		
		//LPB Face classifier
		InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);
		File cascadeDir = context.getApplicationContext().getDir("cascade", Context.MODE_PRIVATE);
		File cascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
		FileOutputStream os = new FileOutputStream(cascadeFile);
		byte[] buffer = new byte[4096];
		int bytesRead;
		while ((bytesRead = is.read(buffer)) != -1) {
			os.write(buffer, 0, bytesRead);
		}
		is.close();
		os.close();
		CascadeClassifier faceDetector = new CascadeClassifier(cascadeFile.getAbsolutePath());
		if (faceDetector.empty()) {
			Log.e("faceDetector ", "级联分类器加载失败");
		}
		
		Mat image = new Mat();
		Utils.bitmapToMat(bitmap, image);
		Imgproc.cvtColor(image, image, Imgproc.COLOR_RGBA2RGB);
		
		// Start testing faces in the picture
		MatOfRect faceDetections = new MatOfRect();
		faceDetector.detectMultiScale(image, faceDetections);
		Log.e("detectFace ", String.format("Detected %s faces", faceDetections.toArray().length));
		Rect[] rects = faceDetections.toArray();
//		if(rects != null && rects.length > 1){
//			throw new RuntimeException("超过一个脸");
//		}
		
		// 在每一个识别出来的人脸周围画出一个方框
//		Rect rect = rects[0];
//			Imgproc.rectangle(image, new Point(rect.x - 2, rect.y - 2),
//					new Point(rect.x + rect.width, rect.y + rect.height),
//					new Scalar(0, 255, 0), 3);
		
		int maxRectArea = 0;
		Rect maxRect = null;
		int facenum = 0;
		for (Rect rectFor : rects) {
			// Adding green border
//			Imgproc.rectangle(image, new Point(rectFor.x - 2, rectFor.y - 2),
//					new Point(rectFor.x + rectFor.width, rectFor.y + rectFor.height),
//					new Scalar(0, 255, 0), 3);
			++facenum;
			// Find the largest area
			int tmp = rectFor.width * rectFor.height;
			if (tmp >= maxRectArea) {
				maxRectArea = tmp;
				maxRect = rectFor;
			}
		}
		if (facenum != 0) {
			Bitmap bitmap1 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
			Utils.matToBitmap(image, bitmap1);
			cutFaceImage(image, maxRect, bitmap1);
			showBitmap(maxRect, bitmap1);
		}
	}
	
	/**
	 * 特征保存
	 */
	public void cutFaceImage(Mat image, Rect rect, Bitmap bitmap) {
		// 原图置灰
//		Mat grayMat = new Mat();
//		Imgproc.cvtColor(image, grayMat, Imgproc.COLOR_BGR2GRAY);
////		 把检测到的人脸重新定义大小后保存成文件
//		Mat sub = grayMat.submat(rect);
//		Mat mat = new Mat();
////		Size size = new Size(100, 100);
////		Imgproc.resize(sub, mat, size);
//		Bitmap rectBitmap = Bitmap.createBitmap(sub.cols(), sub.rows(), Bitmap.Config.ARGB_8888);
//		Utils.matToBitmap(mat, rectBitmap);
		
		Rect rect1 = new Rect(rect.x, rect.y, rect.width, rect.height);
		Mat rectMat = new Mat(image, rect1);  // 从原始图像拿
		Bitmap rectBitmap = Bitmap.createBitmap(rectMat.cols(), rectMat.rows(), Bitmap.Config.ARGB_8888);
		Utils.matToBitmap(rectMat, rectBitmap);
		imageViewCut.setImageBitmap(rectBitmap);
	}
	
	//显示局部图片
	private void showBitmap(Rect rects, Bitmap bitmapyt) {
		Bitmap b = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Mat matyt = new Mat();
		Utils.bitmapToMat(bitmapyt, matyt);
		Imgproc.rectangle(matyt, rects.tl(), rects.br(), new Scalar(0, 255, 0), -1);
		Utils.matToBitmap(matyt, b);
		imageViewFace.setImageBitmap(b);
	}
	
}