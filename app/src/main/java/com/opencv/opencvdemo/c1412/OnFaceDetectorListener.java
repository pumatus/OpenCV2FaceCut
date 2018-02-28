package com.opencv.opencvdemo.c1412;

/**
 * Created by Pumatus on 2018/2/24.
 * 加载OpenCV的回调
 */

public interface OnFaceDetectorListener {
	void onLoadSuccess();
	
	void onLoadFail();
	
	void onOtherError();
}
