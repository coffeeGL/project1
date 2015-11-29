package com.example.note_keep;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class NoteFragment extends Fragment {
	public static final String EXTRA_NOTE_ID =
			"com.example.note_keep.note_id";
	private Note mNote;
	private EditText mTitleField;
	private Button mDateButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	//mDiary = new Diary();
	UUID noteId = (UUID)getArguments().getSerializable(EXTRA_NOTE_ID);
	mNote = NoteLab.get(getActivity()).getNote(noteId);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
	Bundle savedInstanceState) {
	View v = inflater.inflate(R.layout.fragment_note, parent, false);
	mTitleField = (EditText)v.findViewById(R.id.note_title);
	mTitleField.setText(mNote.getTitle());
	mTitleField.addTextChangedListener(new TextWatcher() {
	public void onTextChanged(
	CharSequence c, int start, int before, int count) {
	mNote.setTitle(c.toString());
	}
	public void beforeTextChanged(
	CharSequence c, int start, int count, int after) {
	// code will be here later
	}
	public void afterTextChanged(Editable c) {
	// and here too
	}
	});
	
	mDateButton = (Button)v.findViewById(R.id.note_date);
	Date date = mNote.getDate();
	SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy h:mm a");
	String dateString = sdf.format(date);   
	mDateButton.setText(dateString);
	mDateButton.setEnabled(false);
	
	return v;
	}
	
	public static NoteFragment newInstance(UUID noteId) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_NOTE_ID, noteId);
		NoteFragment fragment = new NoteFragment();
		fragment.setArguments(args);
		return fragment;
		}

}
