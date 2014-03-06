package com.wearability.app;

import java.util.HashMap;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.support.v4.app.NavUtils;

public class BodyDisplay extends Activity {

	//Match musclegroup with imageview id
	HashMap <MuscleGroup, ImageView> muscleIds = new HashMap<MuscleGroup, ImageView>();
	//TODO: Delete temp map to be replaced with database table relating muscle group to activation (or work, or something)
	HashMap <MuscleGroup, Integer> muscleActivations = new HashMap<MuscleGroup, Integer>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_body_display);
		// Show the Up button in the action bar.
		setupActionBar();
		
		muscleIds.put(MuscleGroup.ABS, (ImageView) findViewById(R.id.abs));
		//TODO: Delete temp data, to be replaced with database mapping
		muscleActivations.put(MuscleGroup.ABS, 35);
		
		ImageView iv = (ImageView) findViewById(R.id.muscle_areas);
		
		shadeBody();
		
		iv.setOnTouchListener(new View.OnTouchListener() {
	        public boolean onTouch(View v, MotionEvent event) {
	        	if (event.getAction() == MotionEvent.ACTION_DOWN){
	                final int x = (int) event.getX();
	                final int y = (int) event.getY();
	                    		
	        		Intent intent = new Intent();
	        		
	        		int touchColor = getHotspotColor (R.id.muscle_areas, x, y);

	        		if (touchColor == Color.parseColor("#FF0000")) {
	        			intent.setClass(BodyDisplay.this, ChestActivity.class);
	        		} else if (touchColor == Color.parseColor("#009415")) {
	        			//Set abs green (which we would do async from data acquisition side)
//	        			ImageView abs = (ImageView) findViewById(R.id.abs);
//	        			abs.bringToFront();
//	        			Float f = abs.getAlpha();
//	        			if (abs.getAlpha() < 0.33) {
//	        				abs.setAlpha(0.33F);
//	        			} else if (abs.getAlpha() < 0.66) {
//	        				abs.setAlpha(0.66F);
//	        			} else {
//	        				abs.setAlpha(1F);
//	        			}
//	        			
	        			//Go to abs activity (where we list exercises)
	        			intent.setClass(BodyDisplay.this, AbsActivity.class);
	        		} else {
	        			intent.setClass(BodyDisplay.this, BodyDisplay.class);
	        		}
	        		startActivity(intent);
	        		return true;
	            } else {
	            	return false;
	            }
	        }
	    });
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		shadeBody();
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
		getMenuInflater().inflate(R.menu.body_display, menu);
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
	
	//Use mapping image to identify which region was selected
	public int getHotspotColor (int hotspotId, int x, int y) {
		  ImageView img = (ImageView) findViewById (hotspotId);
		  img.setDrawingCacheEnabled(true); 
		  Bitmap hotspots = Bitmap.createBitmap(img.getDrawingCache()); 
		  img.setDrawingCacheEnabled(false);
		  return hotspots.getPixel(x, y);
	}
	
	//Shade appropriate body parts according to data
	public void shadeBody() {
		ImageView iv;
		int activation;
		for (MuscleGroup mg : MuscleGroup.values()) {
			if (muscleIds.containsKey(mg) && muscleActivations.containsKey(mg)) {
				iv = muscleIds.get(mg);
				//check db for data
				//TODO: replace temp map with actual db
				activation = muscleActivations.get(mg);
				
				//data shows abs are <=33% active
				if (activation < 25) {
					iv.setAlpha(0.0F);
				} else if (activation < 50) {
					iv.setAlpha(0.33F);
				} else if (activation < 75) {
					iv.setAlpha(0.66F);
				} else {
					iv.setAlpha(1.0F);
				}				
				iv.bringToFront();			
			}
		}		
	}
}
