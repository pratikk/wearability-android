package com.wearability.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class StartActivity extends Activity {

	Context context = this;
	TSDatabaseHelper TSDB = new TSDatabaseHelper(context);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		TSDB.getDatabaseName();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.action_history:
	        startExerciseSelector();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	
	public void startBodyView(View view){
		Intent intent = new Intent(this, BodyDisplay.class);
		startActivity(intent);
	}
	
	public void startExerciseSelector(){
		Intent intent = new Intent(this, ExerciseList.class);
		startActivity(intent);
	}
	
}
