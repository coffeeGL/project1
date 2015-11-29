package com.example.note_keep;

import java.util.ArrayList;
import java.util.UUID;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class NotePagerActivity extends FragmentActivity {
	private ViewPager mViewPager;
	private ArrayList<Note> mNotes;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	mViewPager = new ViewPager(this);
	mViewPager.setId(R.id.viewPager);
	setContentView(mViewPager);
	
	//here we get data set from NoteLab - ArrayList container of Note objects
	mNotes = NoteLab.get(this).getNotes(); 
	//get an instance of FragmentManager for activity
	FragmentManager fm = getSupportFragmentManager();
	
	//the adapter assigns an anonymous instance of Fragment - StatePagerAdapter
	mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {	
		@Override
		public int getCount() {
				return mNotes.size(); //returns the current number of elements in the list
			}
		
		//receives Note instance of the specified position in the data set
		//uses its ID to create and return a properly configured instance of NoteFragment
		@Override
		public Fragment getItem(int pos) {
		Note note = mNotes.get(pos);
	    return NoteFragment.newInstance(note.getId());
	     }
	});
	
	/*mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
	    	
		public void onPageScrollStateChanged(int state) { }
		
		public void onPageScrolled(int pos, float posOffset, int posOffsetPixels) {
		}
		//we get the position of the selected note and show it as the title on the actionbar
		public void onPageSelected(int pos) {
		Note note = mNotes.get(pos);
		setTitle("Заметка" + pos);
		}
		});*/
	
	    
	    //assigns the source item for NotePager
		UUID noteId = (UUID)getIntent().getSerializableExtra(NoteFragment.EXTRA_NOTE_ID);
			for (int i = 0; i < mNotes.size(); i++) {
			if (mNotes.get(i).getId().equals(noteId)) {
				mViewPager.setCurrentItem(i);
				break;
			}
		}
	
	}
}
	


