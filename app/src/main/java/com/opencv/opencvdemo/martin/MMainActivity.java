package com.opencv.opencvdemo.martin;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import com.opencv.opencvdemo.R;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

/**
 * Created by Pumatus on 2018/2/23.
 */

public class MMainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
	
	private static final String TAG = MMainActivity.class.getName();
	private Mat mRgba, mIntermediateMat, mGray;
	private CameraBridgeViewBase mOpenCvCamerView;
	
	static {
		System.loadLibrary("opencv_java3");
		System.loadLibrary("opencv_java");
		System.loadLibrary("native-lib");
	}
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);
		
		getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);
		mOpenCvCamerView = findViewById(R.id.tutorial2_activity_surface_view);
		mOpenCvCamerView.setVisibility(CameraBridgeViewBase.VISIBLE);
		mOpenCvCamerView.setCvCameraViewListener(this);
		mOpenCvCamerView.setClickable(true);
		mOpenCvCamerView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Camera camera=((JavaCameraView)mOpenCvCamerView).getCamera();
//				if (camera!=null) camera.autoFocus(null);
			}
		});
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (mOpenCvCamerView != null) {
			mOpenCvCamerView.disableView();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (!OpenCVLoader.initDebug()) {
			Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
		} else {
			Log.d(TAG, "OpenCV library found inside package. Using it!");
			mOpenCvCamerView.enableView();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mOpenCvCamerView != null) {
			mOpenCvCamerView.disableView();
		}
	}
	
	@Override
	public void onCameraViewStarted(int width, int height) {
		mRgba = new Mat(height, width, CvType.CV_8UC4);
		mIntermediateMat = new Mat(height, width, CvType.CV_8UC4);
		mGray = new Mat(height, width, CvType.CV_8UC1);
	}
	
	@Override
	public void onCameraViewStopped() {
		mRgba.release();
		mIntermediateMat.release();
		mGray.release();
	}
	
	@Override
	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		mRgba = inputFrame.rgba();
		mGray = inputFrame.gray();
//		nativeProcessFrame(mGray.getNativeObjAddr(), mRgba.getNativeObjAddr());
		return mRgba;
	}
	
//	public native String stringFromJNI();
//	public native void nativeProcessFrame(long matAddrGr, long matAddrRgba);
}
