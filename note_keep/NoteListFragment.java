package com.example.note_keep;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.example.note_keep.R;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class NoteListFragment extends ListFragment {
private ArrayList<Note> mNotes;
private ListView lnote;
private ListAdapter adapter; 
private Button emptyButton;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true); //here, we say that the instance of NoteListFragment should receive callbacks of command menu
		getActivity().setTitle(R.string.notes_title);
		mNotes = NoteLab.get(getActivity()).getNotes();
		NoteAdapter adapter = new NoteAdapter(mNotes);
			setListAdapter(adapter);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		((NoteAdapter)getListAdapter()).notifyDataSetChanged();
	}
	
	
	//fill command menu, which defined in fragment_note_list.xml
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_note_list, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
				case R.id.menu_item_new_note:
					Note note = new Note();
					NoteLab.get(getActivity()).addNote(note);
					Intent i = new Intent(getActivity(), NotePagerActivity.class);
					i.putExtra(NoteFragment.EXTRA_NOTE_ID, note.getId());
					startActivityForResult(i, 0);
					return true;
				//if we want to start repeating notifications as reminders to open app and check notes
				case R.id.action_startnotif:
					Long alerTime = new GregorianCalendar().getTimeInMillis()+10*1000;
					Intent alertIntent = new Intent(getActivity(), AlertReceiver.class);
					AlarmManager alarmManager = (AlarmManager)(getActivity().getSystemService( Context.ALARM_SERVICE ));
					
					alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alerTime, 20*1000,
							PendingIntent.getBroadcast(getActivity(), 1, alertIntent, 
									0));
					return true;
				//if we want to stop repeating notifications	
				case R.id.action_stopnotif:
					Intent alertIntent2 = new Intent(getActivity(), AlertReceiver.class);
					AlarmManager alarmManager2 = (AlarmManager)(getActivity().getSystemService( Context.ALARM_SERVICE ));
					if(alarmManager2 != null){
					alarmManager2.cancel(PendingIntent.getBroadcast(getActivity(), 1, alertIntent2, 
									0));}
					return true;
				    
				
				default:
					return super.onOptionsItemSelected(item);
			}
			}
	
	//create a context menu
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
	{
	getActivity().getMenuInflater().inflate(R.menu.note_list_item_context, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		int position = info.position;
		NoteAdapter adapter = (NoteAdapter)getListAdapter();
		Note note = adapter.getItem(position);
			switch (item.getItemId()) {
				case R.id.menu_item_delete_note:
					NoteLab.get(getActivity()).deleteNote(note);
					adapter.notifyDataSetChanged();
					return true;
				}
			return super.onContextItemSelected(item);
	}
	
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Note c = ((NoteAdapter)getListAdapter()).getItem(position);
		// Start NotePagerActivity with the Note object
		Intent i = new Intent(getActivity(), NotePagerActivity.class);
		i.putExtra(NoteFragment.EXTRA_NOTE_ID, c.getId());
		startActivity(i);
	}
	
	
	@TargetApi(20) 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
	Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.list_view_empty_view, parent, false);
		lnote=(ListView)v.findViewById(android.R.id.list);
		lnote.setEmptyView(v.findViewById(android.R.id.empty));
	    lnote.setAdapter(adapter);
	    
	    
	    emptyButton = (Button)v.findViewById(android.R.id.empty);
	    emptyButton.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	            
	        	Note note = new Note();
				NoteLab.get(getActivity()).addNote(note);
				Intent i = new Intent(getActivity(), NotePagerActivity.class);
				i.putExtra(NoteFragment.EXTRA_NOTE_ID, note.getId());
				startActivity(i);
	        }
	    });
	    
	    
	    ListView listView = (ListView)v.findViewById(android.R.id.list); 
	    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
	    	// context menu for old versions
	    	registerForContextMenu(listView);
	    	} else {
	    	// context panel for IceCreamSandwich and new versions
	    	listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
	    	listView.setMultiChoiceModeListener(new MultiChoiceModeListener(){
	    		private int nr = 0;
	    		
	    		public void onItemCheckedStateChanged(ActionMode mode, int position,
	    				long id, boolean checked) {
	    			 if (checked)
	                     nr++;
	                 else
	                     nr--;
	                 mode.setTitle(nr + " выбран.");
	    		}
	    		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
	    				MenuInflater inflater = mode.getMenuInflater();
	    				inflater.inflate(R.menu.note_list_item_context, menu);
	    				return true;
	    		}
	    		
	    		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
	    			return false;
	    			}
	    		
	    		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
	    			switch (item.getItemId()) {
	    				case R.id.menu_item_delete_note:
	    					NoteAdapter adapter = (NoteAdapter)getListAdapter();
	    					NoteLab noteLab = NoteLab.get(getActivity());
	    					for (int i = adapter.getCount() - 1; i >= 0; i--) {
	    					if (getListView().isItemChecked(i)) {
	    						noteLab.deleteNote(adapter.getItem(i));
	    					}
	    					}
	    					mode.finish();
	    					adapter.notifyDataSetChanged();
	    					return true;
	    				default:
	    				return false;
	    				}
	    				}
	    		
	    		public void onDestroyActionMode(ActionMode mode) {
	    			   nr=0;
	    			}  
	    				});
	    	}   
	    return v;
	}
	 
	
	private class NoteAdapter extends ArrayAdapter<Note> {
		public NoteAdapter(ArrayList<Note> notes) {
		super(getActivity(), 0, notes);
		}
		
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
		 
		if (convertView == null) {
		convertView = getActivity().getLayoutInflater()
		.inflate(R.layout.list_item_note, null);
		}
		 
		Note c = getItem(position);

		TextView titleTextView =
		(TextView)convertView.findViewById(R.id.note_list_item_titleTextView);
		titleTextView.setText(c.getTitle());
				
		TextView dateTextView =
		(TextView)convertView.findViewById(R.id.note_list_item_dateTextView);
		Date date = c.getDate();
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		String dateString = sdf.format(date);   
		dateTextView.setText(dateString);
		
		TextView timeTextView =
		(TextView)convertView.findViewById(R.id.note_list_item_timeTextView);
		Date time = c.getTime();
		SimpleDateFormat df = new SimpleDateFormat("HH: mm");
		String timeString = df.format(time);   
		timeTextView.setText(timeString);
		
		CheckBox doneCheckBox =
		(CheckBox)convertView.findViewById(R.id.note_list_item_doneCheckBox);
		doneCheckBox.setChecked(c.isDone());
		
		return convertView;
		}
		}
	
	
}
