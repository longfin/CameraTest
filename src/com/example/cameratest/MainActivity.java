package com.example.cameratest;

import java.io.IOException;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

public class MainActivity extends Activity {

	private SurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;
	private boolean previewing = false;
	protected Camera camera;

	private void initSurfaceView() {
		surfaceView = new SurfaceView(getApplicationContext());
		surfaceHolder = surfaceView.getHolder();
		addContentView(surfaceView, new LayoutParams(300, 300));
		previewing = true;
		
		surfaceHolder.addCallback(new SurfaceHolder.Callback() {
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				camera.stopPreview();
				camera.release();
				camera = null;
			}
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				surfaceView.setWillNotDraw(false);
				camera = Camera.open(CameraInfo.CAMERA_FACING_FRONT);
				if (camera != null) {
					try {
						camera.setPreviewDisplay(surfaceHolder);
					} catch (IOException e) {
						removeSurfaceView();
					}
				}
			}
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
				camera.startPreview();
			}
		});
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	private void removeSurfaceView() {
		if(surfaceView != null) {
			previewing = false;
			((ViewManager)surfaceView.getParent()).removeView(surfaceView);
			surfaceHolder = null;
			surfaceView = null;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button start = (Button) findViewById(R.id.startPreviewButton);
		Button stop = (Button) findViewById(R.id.stopPreviewButton);
		
		start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("fuck", "clicked");
				if (surfaceView == null && !previewing) {
					initSurfaceView();
				}
			}
		});
		stop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (camera != null && previewing) {
					removeSurfaceView();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
