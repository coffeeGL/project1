package com.example.note_keep;
import com.example.note_keep.AlertReceiver;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class NoteFragment extends Fragment {
	
	public static final String EXTRA_NOTE_ID =
			"com.example.note_keep.note_id";
	private static final String DIALOG_DATE = "date";
	private static final String DIALOG_TIME = "time";
	private static final int REQUEST_DATE = 0;
	private static final int REQUEST_TIME = 1;
	private Note mNote;
	private EditText mTitleField;
	private Button mDateButton;
	private Button mTimeButton;
	private CheckBox mDoneCheckBox;
	private Button alertButton; //new
	private Context context;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	UUID noteId = (UUID)getArguments().getSerializable(EXTRA_NOTE_ID);
	mNote = NoteLab.get(getActivity()).getNote(noteId);
	setHasOptionsMenu(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
				case android.R.id.home:
					if (NavUtils.getParentActivityName(getActivity()) != null) {
						NavUtils.navigateUpFromSameTask(getActivity());
					}
					return true;
				default:
					return super.onOptionsItemSelected(item);
			}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		NoteLab.get(getActivity()).saveNotes();
	}
	
	public void updateDate() {
		 
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		String dateString = sdf.format(mNote.getDate());  
		mDateButton.setText(dateString);
		
		}
	
	public void updateTime() {
		
		SimpleDateFormat sd = new SimpleDateFormat("HH: mm");
		String dateString = sd.format(mNote.getTime());
		mTimeButton.setText(dateString);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
	Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_note, parent, false);
		v.isClickable();//new
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			if (NavUtils.getParentActivityName(getActivity()) != null) {
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}
		mTitleField = (EditText)v.findViewById(R.id.note_title);
		mTitleField.setText(mNote.getTitle());
		mTitleField.addTextChangedListener(new TextWatcher() {
	
			public void onTextChanged(
				CharSequence c, int start, int before, int count) {
				mNote.setTitle(c.toString());
			}
	
			public void beforeTextChanged(
				CharSequence c, int start, int count, int after) {
				 
			}
			
			public void afterTextChanged(Editable c) {
				 
			}
	});
	//new here we delete the note as a full screen view with the help of alert dialog
		v.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
	        public boolean onLongClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
				builder.setMessage("������� �������?") 
				.setCancelable(false)
				.setPositiveButton("��",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Toast.makeText(getContext(), "������� �������", Toast.LENGTH_SHORT).show();
					            NoteLab noteLab = NoteLab.get(getActivity());
					            noteLab.deleteNote(mNote);
					            Intent i = new Intent(getActivity(), NoteListActivity.class);
					            startActivityForResult(i, 0);
					            
							}
						})
				.setNegativeButton("NO",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
				AlertDialog alert = builder.create();
				alert.show();
				return true;
			}
			
		});
		
	//new
	
	mDateButton = (Button)v.findViewById(R.id.note_date);
	Date date = mNote.getDate();
	updateDate();
	mDateButton.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
		FragmentManager fm = getActivity()
		.getSupportFragmentManager(); 
		DatePickerFragment dialog = DatePickerFragment
				.newInstance(mNote.getDate());
		dialog.setTargetFragment(NoteFragment.this, REQUEST_DATE);
		dialog.show(fm, DIALOG_DATE);
		}
		});
	
	
	mTimeButton = (Button)v.findViewById(R.id.note_time);
	updateTime();
	mTimeButton.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
			FragmentManager fr = getActivity()
			.getSupportFragmentManager(); 
			TimePickerFragment dialog = TimePickerFragment
					.newInstance(mNote.getTime());
			dialog.setTargetFragment(NoteFragment.this, REQUEST_TIME);
			dialog.show(fr, DIALOG_TIME);
			}
		});
	
	mDoneCheckBox = (CheckBox)v.findViewById(R.id.note_done);
	mDoneCheckBox.setChecked(mNote.isDone());
	mDoneCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
	{
		mNote.setDone(isChecked);
	}
	});
	
	alertButton = (Button)v.findViewById(R.id.alarm_me); //new
	alertButton.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v){
			
			Long alerTime = new GregorianCalendar().getTimeInMillis()+60*1000;
			Intent alertIntent = new Intent(getActivity(), AlertReceiver.class);
			AlarmManager alarmManager = (AlarmManager)(getActivity().getSystemService( Context.ALARM_SERVICE ));
			
			alarmManager.set(AlarmManager.RTC_WAKEUP, alerTime,
					PendingIntent.getBroadcast(getActivity(), 1, alertIntent, 
							PendingIntent.FLAG_UPDATE_CURRENT));
		}	
		
	});
	//new
	
	return v;
	}
	

	public static NoteFragment newInstance(UUID noteId) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_NOTE_ID, noteId);
		NoteFragment fragment = new NoteFragment();
		fragment.setArguments(args);
		return fragment;
		}
	
	
	//reaction on received data from dialog windows
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) return;
		if (requestCode == REQUEST_DATE) {
			Date date = (Date)data
			.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			mNote.setDate(date);
			updateDate(); //call method to change date   
		}
		else if (requestCode == REQUEST_TIME) {
			Date date = (Date)data
			.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
			mNote.setTime(date);
			updateTime(); //call method to change time   
		}
	}

}
