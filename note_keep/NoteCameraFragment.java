package com.example.note_keep;


import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
	public static final String EXTRA_PHOTO_FILENAME =
			"com.example.note_keep.photo_filename";
	
	private Camera mCamera;
	private SurfaceView mSurfaceView;
	private View mProgressContainer;
	
	private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
		public void onShutter() {
			// Отображение индикатора прогресса
			mProgressContainer.setVisibility(View.VISIBLE);
			}
	};
	
	private Camera.PictureCallback mJpegCallback = new Camera.PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			// Создание имени файла
			String filename = UUID.randomUUID().toString() + ".jpg";
			// Сохранение данных jpeg на диске
			FileOutputStream os = null;
			boolean success = true;
			try {
				os = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
				os.write(data);
			} catch (Exception e) {
				Log.e(TAG, "Error writing to file " + filename, e);
				success = false;
			} finally {
				try {
					if (os != null)
						os.close();
			} catch (Exception e) {
				Log.e(TAG, "Error closing file " + filename, e);
				success = false;
				}
			}
			if (success) {
				//Log.i(TAG, "JPEG saved at " + filename);
				// Имя файла фотографии записывается в интент результата
				if (success) {
				Intent i = new Intent();
				i.putExtra(EXTRA_PHOTO_FILENAME, filename);
				getActivity().setResult(Activity.RESULT_OK, i);
				} else {
					getActivity().setResult(Activity.RESULT_CANCELED);
				}
			}
			getActivity().finish();
			}
		};
		
	
	
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
		
		mProgressContainer = v.findViewById(R.id.camera_progressContainer);
		mProgressContainer.setVisibility(View.INVISIBLE);
		
		Button makePictureButton = (Button)v.findViewById(R.id.camera_makePicture);
		makePictureButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (mCamera != null) {
					mCamera.takePicture(mShutterCallback, null, mJpegCallback);
					}
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
			s = getBestSupportedSize(parameters.getSupportedPictureSizes(), w, h);
			parameters.setPictureSize(s.width, s.height);
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
