package com.wearability.app;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;

public class ChestActivity extends Activity {

	static final String[] EXERCISES = new String[] {
		"Close-Grip Pushup", "Wide-Grip Pushup", "Bench Press", "Chest Flye"
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chest);
		
		//ListView lv = (ListView) findViewById(R.id.chestExercises);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chest, menu);
		return true;
	}

}
