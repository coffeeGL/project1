package com.example.note_keep;


import java.io.IOException;
import java.util.List;

import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class NoteCameraFragment extends Fragment {
	private static final String TAG = "NoteCameraFragment";
	private Camera mCamera;
	private SurfaceView mSurfaceView;
	
	@Override
	public void onResume() {
		super.onResume();
		 
		mCamera = Camera.open(0); //open the 1st available camera
	}
	
	//here we make camera free
	@Override
	public void onPause() {
		super.onPause();
		if (mCamera != null) {
			mCamera.release();
			mCamera = null;
		}
	}
	
	
	
	@Override
	@SuppressWarnings("deprecation")
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_note_camera, parent, false);
		Button makePictureButton = (Button)v.findViewById(R.id.camera_makePicture);
		makePictureButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				getActivity().finish();
				}
		});
		mSurfaceView = (SurfaceView)v.findViewById(R.id.camera_surfaceView);
		SurfaceHolder holder = mSurfaceView.getHolder();
		 
		//The method getType () and the constant SURFACE_TYPE_PUSH_BUFFERS considered 
		//obsolete, but they are required to images preview function from camera Camera 
		//which works on old devices
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		holder.addCallback(new SurfaceHolder.Callback() {
			public void surfaceCreated(SurfaceHolder holder) {
			 
			//order camera to use the specified surface as area of the preview
			try {
				if (mCamera != null) {
					mCamera.setPreviewDisplay(holder);
					}
					} catch (IOException exception) {
					Log.e(TAG, "Error setting up preview display", exception);
					}
			}
		
		   public void surfaceDestroyed(SurfaceHolder holder) {
			 
			//A further displaying on the surface is impossible, stop the preview.
			if (mCamera != null) {
				mCamera.stopPreview();
				}
			}
		   
		public void surfaceChanged(SurfaceHolder holder, int format, int w, int h)
		{
			if (mCamera == null) return;
			//The size of the surface has changed; 
			//update the area size of the camera preview
			Camera.Parameters parameters = mCamera.getParameters();
			Size s = getBestSupportedSize(parameters.getSupportedPreviewSizes(), w, h);
			parameters.setPreviewSize(s.width, s.height);
			mCamera.setParameters(parameters);
			try {
				mCamera.startPreview();
			} catch (Exception e) {
				Log.e(TAG, "Could not start preview", e);
				mCamera.release();
				mCamera = null;
				}
			}
		});
		
		return v;
			}
	
	//A simple algorithm to obtain the largest available size
	private Size getBestSupportedSize(List<Size> sizes, int width, int height) {
		Size bestSize = sizes.get(0);
		int largestArea = bestSize.width * bestSize.height;
		for (Size s : sizes) {
			int area = s.width * s.height;
			if (area > largestArea) {
				bestSize = s;
				largestArea = area;
			}
			}
		return bestSize;
		}
		

}
