package com.opencv.opencvdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.WindowManager.LayoutParams;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2{
	
	// Used to load the 'native-lib' library on application startup.
	static {
		System.loadLibrary("native-lib");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main);
		javaCameraView = findViewById(R.id.javaCameraView);
		javaCameraView.setVisibility(SurfaceView.VISIBLE);
		javaCameraView.setCvCameraViewListener(this);
		
	}
	
	private JavaCameraView javaCameraView;
	private BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
			switch (status) {
				case LoaderCallbackInterface.SUCCESS: {
					javaCameraView.enableView();
				}
				break;
				default:
					super.onManagerConnected(status);
				break;
			}
		}
	};
	
	@Override
	public void onCameraViewStarted(int width, int height) {
	
	}
	
	@Override
	public void onCameraViewStopped() {
	
	}
	
	@Override
	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		return inputFrame.rgba();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (javaCameraView != null) {
			javaCameraView.disableView();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (!OpenCVLoader.initDebug()) {
			OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, baseLoaderCallback);
		} else {
			baseLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
		}
	}
}
