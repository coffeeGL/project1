package com.example.note_keep;

import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

 
 

public class Note {
	
	private static final String JSON_ID = "id";
	private static final String JSON_TITLE = "title";
	private static final String JSON_DATE = "date";
	private static final String JSON_TIME = "time";
	private static final String JSON_DONE = "done";
	private static final String JSON_LASTCHANGE = "lastchangedate";
	private static final String JSON_PHOTO = "photo";
	
	private UUID mId;
	private String mTitle; //text of the note
	private Date mDate;  //day of deadline
	private Date mTime; //time of deadline
	private boolean mDone;
	private Date mLastChangeDate; //date of the last change of the note
	private Photo mPhoto;
	
	public Note() {
	// generate a unique id
	mId = UUID.randomUUID();
	mDate = new Date();
	mTime = new Date();
	mLastChangeDate = new Date(); 
	}
	
	public Note(JSONObject json) throws JSONException {
		mId = UUID.fromString(json.getString(JSON_ID));
		mTitle = json.getString(JSON_TITLE); 
		mDate = new Date(json.getLong(JSON_DATE));
		mTime = new Date(json.getLong(JSON_TIME));
		mDone = json.getBoolean(JSON_DONE);
		mLastChangeDate = new Date(json.getLong(JSON_LASTCHANGE));
		if (json.has(JSON_PHOTO))
			mPhoto = new Photo(json.getJSONObject(JSON_PHOTO));
		}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(JSON_ID, mId.toString());
		json.put(JSON_TITLE, mTitle);
		json.put(JSON_DATE, mDate.getTime());
		json.put(JSON_TIME, mTime.getTime());
		json.put(JSON_DONE, mDone);
		json.put(JSON_LASTCHANGE, mLastChangeDate.getTime());
		if (mPhoto != null)
			json.put(JSON_PHOTO, mPhoto.toJSON());
		return json;
		}
	
	
	@Override
	public String toString() {
	return mTitle;
	}
	
	public UUID getId() {
		return mId;
		}
	
	public String getTitle() {
		return mTitle;
	}
	
	public void setTitle(String title) {
		mTitle = title;
		}
	
	public Date getDate() {
		return mDate;
		}
	
	public void setDate(Date date) {
		mDate = date;
		}
	
	public Date getTime() {
		return mTime;
		}
	
	public void setTime(Date date) {
		mTime = date;
		}
	
	public boolean isDone() {
		return mDone;
		}
		public void setDone(boolean done) {
		mDone = done;
		}
		
		
	public Date getLastChangeDate() {
			return mLastChangeDate;
		}
		
	public void setLastChageDate(Date date) {
			mLastChangeDate = date;
		}
	
	public Photo getPhoto() {
		return mPhoto;
	}
		
	public void setPhoto(Photo p) {
		mPhoto = p;
		}
        
}
