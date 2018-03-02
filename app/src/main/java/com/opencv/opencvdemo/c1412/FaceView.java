package com.opencv.opencvdemo.c1412;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import com.opencv.opencvdemo.R;

/**
 * Created by Pumatus on 2018/2/24.
 * Under consideration, or priority to use Opencv
 */

public class FaceView extends android.support.v7.widget.AppCompatImageView {
	
	public static final String TAG = FaceView.class.getName();
	
	private int imageWidth, imageHeight;
	private FaceDetector faceDetector;
	private int maxFace = 3;
	private Bitmap faceImage;
	
	//存储识别的脸
	private FaceDetector.Face[] mFaces = new FaceDetector.Face[maxFace];
	//真实检测到的人脸数
	private int mFaceFaces;
	private float myEyesDistance;
	
	public FaceView(Context context) {
		super(context);
		init();
	}
	
	public FaceView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	private void init() {
		BitmapFactory.Options options = new Options();
		options.inPreferredConfig = Config.RGB_565;
		//Put here need to detect the face of the picture
		faceImage = BitmapFactory.decodeResource(getResources(), R.drawable.test1, options);
		imageWidth = faceImage.getWidth();
		imageHeight = faceImage.getHeight();
		//create face detector
		faceDetector = new FaceDetector(imageWidth, imageHeight, maxFace);
		mFaceFaces = faceDetector.findFaces(faceImage, mFaces);
		Log.e(TAG, "人脸数" + mFaceFaces);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint mPint = new Paint();
		mPint.setColor(Color.GREEN);
		mPint.setStyle(Style.STROKE);
		mPint.setStrokeWidth(2);
		//Start the picture frame
		for (int i = 0; i < mFaceFaces; i++) {
			FaceDetector.Face face = mFaces[i];
			PointF pointF = new PointF();
			face.getMidPoint(pointF);
			//Get the center of the face and eye distance parameters
			myEyesDistance = face.eyesDistance();
			canvas.drawRect(pointF.x - myEyesDistance, pointF.y - myEyesDistance,
					pointF.x + myEyesDistance, (float) (pointF.y + myEyesDistance * 1.5), mPint);
		}
	}
}
