package com.example.note_keep;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class NoteListFragment extends ListFragment {
private ArrayList<Note> mNotes;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	getActivity().setTitle(R.string.notes_title);
	mNotes = NoteLab.get(getActivity()).getNotes();
	NoteAdapter adapter = new NoteAdapter(mNotes);
			setListAdapter(adapter);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
	Note c = ((NoteAdapter)getListAdapter()).getItem(position);
	Intent i = new Intent(getActivity(), NoteActivity.class);
	i.putExtra(NoteFragment.EXTRA_NOTE_ID, c.getId());
	startActivity(i);
	}
	
	private class NoteAdapter extends ArrayAdapter<Note> {
		public NoteAdapter(ArrayList<Note> notes) {
		super(getActivity(), 0, notes);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
		// Если мы не получили представление, заполняем его
		if (convertView == null) {
		convertView = getActivity().getLayoutInflater()
		.inflate(R.layout.list_item_note, null);
		}
		// Настройка представления для объекта Crime
		Note c = getItem(position);
		TextView titleTextView =
		(TextView)convertView.findViewById(R.id.note_list_item_titleTextView);
		titleTextView.setText(c.getTitle());
		TextView dateTextView =
		(TextView)convertView.findViewById(R.id.note_list_item_dateTextView);
		Date date = c.getDate();
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy h:mm a");
		String dateString = sdf.format(date);   
		dateTextView.setText(dateString);
		return convertView;
		}
		}
}
