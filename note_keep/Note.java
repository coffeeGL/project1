package com.example.note_keep;

import java.util.Date;
import java.util.UUID;

 

public class Note {
	
	private UUID mId;
	private String mTitle; //text of the note
	private Date mDate;  //day of deadline
	private Date mTime; //time of deadline
	
	public Note() {
	// generate a unique id
	mId = UUID.randomUUID();
	mDate = new Date();
	mTime = new Date();
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

}
