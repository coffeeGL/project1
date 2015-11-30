package com.example.note_keep;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View; 
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;


public class TimePickerFragment extends DialogFragment{
	
	public static final String EXTRA_TIME =
			"com.example.note_keep.time";
	
			private Date mTime;
				
	/*we saved date in arguments package of TimePickerFragment, 
	and TimePickerFragment will be able to use it*/
	public static TimePickerFragment newInstance(Date date) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_TIME, date);
		TimePickerFragment fragment = new TimePickerFragment();
		fragment.setArguments(args);
				
		return fragment;
		}
				  

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mTime = (Date)getArguments().getSerializable(EXTRA_TIME);
		Calendar c = Calendar.getInstance();
		c.setTime(mTime);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
			 
			 
		View v = getActivity().getLayoutInflater()
						.inflate(R.layout.dialog_time, null);
				
	TimePicker timePicker = (TimePicker)v.findViewById(R.id.dialog_time_timePicker);
	timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {
				
	public void onTimeChanged(TimePicker view, int hour, int minute) {
					 
		   mTime= new GregorianCalendar(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, hour, minute).getTime();
		   getArguments().putSerializable(EXTRA_TIME, mTime);
					}
					});
					
				return new AlertDialog.Builder(getActivity())
					.setView(v)
					.setTitle(R.string.time_picker_title)
					.setPositiveButton(
							android.R.string.ok, 
							new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								sendResult(Activity.RESULT_OK);
							}
							})
					.create();
	}
	
	private void sendResult(int resultCode) {
		if (getTargetFragment() == null)
				return;
			Intent i = new Intent();
				i.putExtra(EXTRA_TIME, mTime);
				getTargetFragment()
				.onActivityResult(getTargetRequestCode(), resultCode, i);
				}	
	}

 
