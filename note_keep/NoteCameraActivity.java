package com.example.note_keep;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;

public class NoteCameraActivity extends SingleFragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// hide the title of the window
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//hide status bar and other OS-level design
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected Fragment createFragment() {
		// TODO Auto-generated method stub
		return new NoteCameraFragment();
	}

}
