package com.example.note_keep;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import android.content.Context;

public class Note_KeepJSONSerializer {
	private Context mContext;
	private String mFilename;
	
	public Note_KeepJSONSerializer(Context c, String f) {
		mContext = c;
		mFilename = f;
	}
	
	public ArrayList<Note> loadNotes() throws IOException, JSONException {
		ArrayList<Note> notes = new ArrayList<Note>();
		BufferedReader reader = null;
		try {
		//opening and reading file in StringBuilder
		InputStream in = mContext.openFileInput(mFilename);
		reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder jsonString = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
		// Line breaks are omitted and irrelevant
		jsonString.append(line);
		}
		// Parsing JSON with JSONTokener
		JSONArray array = (JSONArray) new JSONTokener(jsonString.toString())
		.nextValue();
		 
		//Build an array of Note objects according to data of JSONObject
		for (int i = 0; i < array.length(); i++) {
		notes.add(new Note(array.getJSONObject(i)));
		}
		} catch (FileNotFoundException e) {

		} finally {
		if (reader != null)
		reader.close();
		}
		return notes;
		}
	
	
	public void saveNotes(ArrayList<Note> notes)
			throws JSONException, IOException {
					// build array JSON
					JSONArray array = new JSONArray();
					for (Note c : notes)
						array.put(c.toJSON());
	
					// record file on disk
					Writer writer = null;
					try {
						OutputStream out = mContext
							.openFileOutput(mFilename, Context.MODE_PRIVATE);
						writer = new OutputStreamWriter(out);
						writer.write(array.toString());
					} finally {
						if (writer != null)
							writer.close();
					}
		}
}
