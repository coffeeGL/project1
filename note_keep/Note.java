package com.example.note_keep;

import java.util.Date;
import java.util.UUID;

 

public class Note {
	
	private UUID mId;
	private String mTitle;
	private Date mDate;
	private Date mTime;//new
	
	public Note() {
	// Генерирование уникального идентификатора
	mId = UUID.randomUUID();
	mDate = new Date();
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
		return mDate;
		}
	
	public void setTime(Date date) {
		mTime = date;
		}

}
