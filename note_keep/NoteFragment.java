package com.example.note_keep;
 
import java.text.SimpleDateFormat;
import java.util.Date;
//import java.util.GregorianCalendar;
import java.util.UUID;
import android.app.Activity;
//import android.app.AlarmManager;
import android.app.AlertDialog;
//import android.app.PendingIntent;
//import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NoteFragment extends Fragment {
	
	private static final String TAG = "NoteFragment";
	public static final String EXTRA_NOTE_ID =
			"com.example.note_keep.note_id";
	private static final String DIALOG_DATE = "date";
	private static final String DIALOG_TIME = "time";
	private static final String DIALOG_IMAGE = "image";
	private static final int REQUEST_DATE = 0;
	private static final int REQUEST_TIME = 1;
	private static final int REQUEST_PHOTO = 2;
	private Note mNote;
	private EditText mTitleField;
	private Button mDateButton;
	private Button mTimeButton;
	private CheckBox mDoneCheckBox;
	private TextView mLastChangeDateTextView; 
	private ImageView mPhotoView;
	 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	UUID noteId = (UUID)getArguments().getSerializable(EXTRA_NOTE_ID);
	mNote = NoteLab.get(getActivity()).getNote(noteId);
	setHasOptionsMenu(true);
	}
	
	//fill command menu, which defined in fragment_note_list.xml
		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			super.onCreateOptionsMenu(menu, inflater);
			inflater.inflate(R.menu.fragment_note, menu);
		}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
				case android.R.id.home:
					if (NavUtils.getParentActivityName(getActivity()) != null) {
						NavUtils.navigateUpFromSameTask(getActivity());
					}
					return true;
					
				case R.id.menu_item_make_picture:
					Intent i = new Intent(getActivity(), NoteCameraActivity.class);
					startActivityForResult(i, REQUEST_PHOTO);
					return true;
					
			    
				case R.id.menu_item_export_note:
				    Intent p = new Intent(Intent.ACTION_SEND);
					p.setType("text/plain");
					p.putExtra(Intent.EXTRA_TEXT, getNoteExport());
					p.putExtra(Intent.EXTRA_SUBJECT,
					getString(R.string.note_export_subject));
					p = Intent.createChooser(p, getString(R.string.send_note));
					startActivity(p);
					return true;
				 
					
				case R.id.menu_item_delete_note:
					AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
					builder.setMessage("Удалить заметку?") 
					.setCancelable(false)
					.setPositiveButton("Удалить заметку",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									Toast.makeText(getContext(), "Заметка удалена", Toast.LENGTH_SHORT).show();
						            NoteLab noteLab = NoteLab.get(getActivity());
						            noteLab.deleteNote(mNote);
						            Intent i = new Intent(getActivity(), NoteListActivity.class);
						            startActivity(i);
						            
								}
							})
					.setNegativeButton("Отмена",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
								}
							});
					AlertDialog alert = builder.create();
					alert.show();
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
	
	public void updateLastChangeDate() {
		 
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH: mm");
		String dateString = sdf.format(mNote.getLastChangeDate());  
		mLastChangeDateTextView.setText(dateString);
		
		}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
	Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_note, parent, false);
		v.isClickable();
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
				mNote.setLastChageDate(new Date());
				updateLastChangeDate(); 
			}
	});
		
	//delete?	
	//here we delete the note as a full screen view with the help of alert dialog
		v.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
	        public boolean onLongClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
				builder.setMessage("Удалить заметку?") 
				.setCancelable(false)
				.setPositiveButton("Удалить заметку",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Toast.makeText(getContext(), "Заметка удалена", Toast.LENGTH_SHORT).show();
					            NoteLab noteLab = NoteLab.get(getActivity());
					            noteLab.deleteNote(mNote);
					            Intent i = new Intent(getActivity(), NoteListActivity.class);
					            startActivity(i);
					            
							}
						})
				.setNegativeButton("Отмена",
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
		//delete?
	
	
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
		mNote.setLastChageDate(new Date());
		updateLastChangeDate(); 
	}
	});
	
	
	
	mLastChangeDateTextView = (TextView)v.findViewById(R.id.last_change);
	updateLastChangeDate();
	
	mPhotoView = (ImageView)v.findViewById(R.id.camera_imageView);
	mPhotoView.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
			Photo p = mNote.getPhoto();
			if (p == null)
				return;
		
				FragmentManager fm = getActivity()
					.getSupportFragmentManager();
				String path = getActivity()
					.getFileStreamPath(p.getFilename()).getAbsolutePath();
				ImageFragment.newInstance(path)
					.show(fm, DIALOG_IMAGE);
				}
			});
	
	return v;
	}
	
	private void showPhoto() {
	
		Photo p = mNote.getPhoto();
		BitmapDrawable b = null;
		if (p != null) {
			String path = getActivity()
				.getFileStreamPath(p.getFilename()).getAbsolutePath();
			b = PictureUtils.getScaledDrawable(getActivity(), path);
		}
		mPhotoView.setImageDrawable(b);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		showPhoto();
	}
	
	@Override
	public void onStop() {
		super.onStop();
		PictureUtils.cleanImageView(mPhotoView);
	}
	
	public String getNoteExport() {
		String doneString = null;
		if (mNote.isDone()) {
		doneString = getString(R.string.note_report_done);
		} else {
		doneString = getString(R.string.note_report_undone);
		}
		String dateFormat = "EEE, MMM dd";
		String dateString = DateFormat.format(dateFormat, mNote.getDate()).toString();
		 
		String report = getString(R.string.note_report,
		mNote.getTitle(), dateString, doneString);
		return report;
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
			mNote.setLastChageDate(new Date());
			updateLastChangeDate(); 
		}
		else if (requestCode == REQUEST_TIME) {
			Date date = (Date)data
			.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
			mNote.setTime(date);
			updateTime(); //call method to change time
			mNote.setLastChageDate(new Date());
			updateLastChangeDate(); 
		}
		else if (requestCode == REQUEST_PHOTO) {
		// Создание нового объекта Photo и связывание его с Note
		String filename = data.getStringExtra(NoteCameraFragment.EXTRA_PHOTO_FILENAME);
		if (filename != null) {
		//Log.i(TAG, "filename: " + filename);
			Photo p = new Photo(filename);
			mNote.setPhoto(p);
			showPhoto();
			//Log.i(TAG, "Note: " + mNote.getTitle() + " has a photo");
		}
	    }
	}
	}

