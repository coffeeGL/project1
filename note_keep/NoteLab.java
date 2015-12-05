package com.example.note_keep;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.util.Log;

public class NoteLab {
	private static final String TAG = "NoteLab";
	private static final String FILENAME = "notes.json";
	
	private ArrayList<Note> mNotes;
	private Note_KeepJSONSerializer mSerializer;
	private static NoteLab sNoteLab;
	private Context mAppContext;
	
	 
	private NoteLab(Context appContext) {
	mAppContext = appContext;
	//mNotes = new ArrayList<Note>();
	mSerializer = new Note_KeepJSONSerializer(mAppContext, FILENAME);
	try {
		mNotes = mSerializer.loadNotes();
		} catch (Exception e) {
		mNotes = new ArrayList<Note>();
		Log.e(TAG, "Error loading notes: ", e);
		}
	}
	
	public static NoteLab get(Context c) {
	if (sNoteLab == null) {
	sNoteLab = new NoteLab(c.getApplicationContext());
	}	
	return sNoteLab;
	}
	
	//add new object note
	public void addNote(Note c) {
		mNotes.add(c);
		}
	//delete object
	public void deleteNote(Note c) {
		mNotes.remove(c);
		}
	
	public boolean saveNotes() {
		try {
		mSerializer.saveNotes(mNotes);
		Log.d(TAG, "notes saved to file");
		return true;
		} catch (Exception e) {
		Log.e(TAG, "Error saving notes: ", e);
		return false;
		}
		}
	
	public ArrayList<Note> getNotes() {
		return mNotes;
		}
		public Note getNote(UUID id) {
		for (Note c : mNotes) {
		if (c.getId().equals(id))
		return c;
		}
		return null;
		}
		
	

}
