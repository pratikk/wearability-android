package com.wearability.app;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class BicepsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_biceps);
		// Show the Up button in the action bar.
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.biceps, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onResume()
	{
	   super.onResume();
	   incrementRep();
	}

	
	public void incrementRep() {
		TextView tv = (TextView) findViewById(R.id.repCount);
		int curRep = Integer.parseInt(tv.getText().toString());
		curRep ++;
		tv.setText("" + curRep);
		if (curRep < 2) {
			BodyDisplay.setActivation(MuscleGroup.BICEPS, 0);
		} else if (curRep < 4) {
			BodyDisplay.setActivation(MuscleGroup.BICEPS, 25);
		} else if (curRep < 6) {
			BodyDisplay.setActivation(MuscleGroup.BICEPS, 50);
		} else if (curRep < 8) {
			BodyDisplay.setActivation(MuscleGroup.BICEPS, 75);
		} else {
			BodyDisplay.setActivation(MuscleGroup.BICEPS, 100);
		}
		
	}
	
	public void showSummary(View view){
		Intent intent = new Intent(this, SummaryActivity.class);
		startActivity(intent);
	}

}
