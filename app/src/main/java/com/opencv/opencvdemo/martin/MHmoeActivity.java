package com.opencv.opencvdemo.martin;

import android.Manifest;
import android.Manifest.permission;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Pumatus on 2018/2/23.
 */

public class MHmoeActivity extends AppCompatActivity {
	
	private static final int REQUEST_PERMISSION = 233;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (checkPermission(permission.CAMERA, REQUEST_PERMISSION)) {
			init();
		}
	}
	
	private void init() {
		startActivity(new Intent(this, MMainActivity.class));
		finish();
	}
	
	private boolean checkPermission(String permission, int requestCode) {
		if (VERSION.SDK_INT >= VERSION_CODES.M) {
			if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
				if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
					Toast.makeText(this,
							"Camera and SDCard access is required, please grant the permission in settings.",
							Toast.LENGTH_LONG).show();
					finish();
				} else {
					ActivityCompat.requestPermissions(this,
							new String[]{permission, Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
				}
				return false;
			} else {
				return true;
			}
		}
		return true;
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
			@NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case REQUEST_PERMISSION:
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					init();
				} else {
					Toast.makeText(this,
							"Camera and SDCard access is required, please grant the permission in settings.",
							Toast.LENGTH_LONG).show();
					finish();
				}
				break;
			default:
				finish();
				break;
		}
	}
}
