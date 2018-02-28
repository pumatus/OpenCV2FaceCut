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
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

/**
 * Created by Pumatus on 2018/2/24.
 */

public class CutActivity extends AppCompatActivity implements OnClickListener {
	
	private ImageView imageView;
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
		imageView = findViewById(R.id.img_cut);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test2);
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
	 * opencv实现人脸识别
	 */
	public void detectFace(Bitmap bitmap, Context context) throws Exception
	{
		
		Log.e("detectFace", "Running DetectFace ... ");
		
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
		
		// 在图片中检测人脸
		MatOfRect faceDetections = new MatOfRect();
		faceDetector.detectMultiScale(image, faceDetections);
		Log.e("detectFace ", String.format("Detected %s faces", faceDetections.toArray().length));
		
		Rect[] rects = faceDetections.toArray();
//		if(rects != null && rects.length > 1){
//			throw new RuntimeException("超过一个脸");
//		}
		// 在每一个识别出来的人脸周围画出一个方框
		Rect rect = rects[0];
		int maxRectArea = 0 * 0;
		Rect maxRect = null;
		int facenum = 0;
		for (Rect rectFor : rects) {
			//加了有绿色的边框
//			Imgproc.rectangle(image, new Point(rectFor.x - 2, rectFor.y - 2),
//					new Point(rectFor.x + rectFor.width, rectFor.y + rectFor.height),
//					new Scalar(0, 255, 0), 3);
			++facenum;
			//找出最大的面积
			int tmp = rectFor.width * rectFor.height;
			if (tmp >= maxRectArea) {
				maxRectArea = tmp;
				maxRect = rectFor;
			}
		}
		if (facenum != 0) {
			Bitmap bitmap1 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
			Utils.matToBitmap(image, bitmap1);
			Bitmap bitmap2 = saveImage(image, maxRect, bitmap1);
			imageView.setImageBitmap(bitmap2);
		}
		
//		Bitmap bitmap1 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
//		Utils.matToBitmap(image, bitmap1);
		
		//截取的区域：参数,坐标X,坐标Y,截图宽度,截图长度
//		Rect rect1 = new Rect(rect.x, rect.y, rect.width, rect.height);
//		Rect rect1 = new Rect(50, 50, 100, 100);
//		Mat sub = image.submat(rect1);   //Mat sub = new Mat(image,rect);
//		Mat mat = new Mat();
//		Size size = new Size(50, 50);
//		Imgproc.resize(sub, mat, size);//将人脸进行截图并保存
////		Imgcodecs.imwrite(outFile, mat);
//		Utils.matToBitmap(mat, bitmap1);
		
//		Bitmap bitmap2 = cupBitmap(bitmap1, rect.x, rect.y, rect.width, rect.height);           //需要开线程抠图
//		Bitmap bitmap2 = saveImage(image, rect, bitmap1);
//
//		imageView.setImageBitmap(bitmap2);
//		Imgcodecs.imwrite(outFile, image);
//		Log.e("TAGTAG  ", String.format("人脸识别成功，人脸图片文件为： %s", outFile));
	}
	
	/**
	 * 特征保存
	 */
	public static Bitmap saveImage(Mat image, Rect rect, Bitmap bitmap) {
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
		return rectBitmap;
	}
	
	private Bitmap cupBitmap(Bitmap bitmap,int x,int y,int width,int height) {
		Mat img = new Mat();
//缩小图片尺寸
		// Bitmap bm = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth(),bitmap.getHeight(),true);
//bitmap->mat
		Utils.bitmapToMat(bitmap, img);
//转成CV_8UC3格式
		Imgproc.cvtColor(img, img, Imgproc.COLOR_RGBA2RGB);
//设置抠图范围的左上角和右下角
		Rect rect = new Rect(x,y,width,height);
//生成遮板
		Mat firstMask = new Mat();
		Mat bgModel = new Mat();
		Mat fgModel = new Mat();
		Mat source = new Mat(1, 1, CvType.CV_8U, new Scalar(Imgproc.GC_PR_FGD));
		Imgproc.grabCut(img, firstMask, rect, bgModel, fgModel,5, Imgproc.GC_INIT_WITH_RECT);
		Core.compare(firstMask, source, firstMask, Core.CMP_EQ);

//抠图
		Mat foreground = new Mat(img.size(), CvType.CV_8UC3, new Scalar(255, 255, 255));
		img.copyTo(foreground, firstMask);

//mat->bitmap
		Bitmap bitmap1 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Utils.matToBitmap(foreground,bitmap1);
		return bitmap1;
	}
	
}
