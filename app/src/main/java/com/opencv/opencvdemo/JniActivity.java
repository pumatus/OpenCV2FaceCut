package com.opencv.opencvdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by Pumatus on 2018/2/22.
 */

public class JniActivity extends AppCompatActivity implements View.OnClickListener {
	
	private ImageView imageView;
	private Button showBtn, processBtn;
	
	static {
		System.loadLibrary("native-lib");
	}
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jni);
		imageView = findViewById(R.id.imageView);
		showBtn = findViewById(R.id.show);
		processBtn = findViewById(R.id.process);
		showBtn.setOnClickListener(this);
		processBtn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if (v == showBtn) {
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
			imageView.setImageBitmap(bitmap);
		} else {
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
//			getEdge(bitmap);
			imageView.setImageBitmap(bitmap);
		}
	}
	
	//获得Canny边缘
//	native void getEdge(Object bitmap);
}
