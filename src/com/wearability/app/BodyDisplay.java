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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class BodyDisplay extends Activity {

	//Match musclegroup with imageview id
	HashMap <MuscleGroup, ImageView> muscleIds = new HashMap<MuscleGroup, ImageView>();
	//TODO: Delete temp map to be replaced with database table relating muscle group to activation (or work, or something)
	static HashMap <MuscleGroup, Integer> muscleActivations = new HashMap<MuscleGroup, Integer>();
	
	boolean toCalibrate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_body_display);
		// Show the Up button in the action bar.
		setupActionBar();
		
		/* TODO: Fix calibration process (later)
		toCalibrate = true;
		
		//Create calibration alpha animations
		AlphaAnimation animation1 = new AlphaAnimation(0.0f, 1.0f);
	    animation1.setDuration(1000);
	    animation1.setStartOffset(5000);

	    AlphaAnimation animation2 = new AlphaAnimation(1.0f, 0.0f);
	    animation2.setDuration(1000);
	    animation2.setStartOffset(5000);
	    
	    animation1.setAnimationListener(new AnimationListener(){

	        @Override
	        public void onAnimationEnd(Animation arg0) {
	            // start animation2 when animation1 ends (continue)
	            textView.startAnimation(animation2);
	        }

	        @Override
	        public void onAnimationRepeat(Animation arg0) {
	            // TODO Auto-generated method stub

	        }

	        @Override
	        public void onAnimationStart(Animation arg0) {
	            // TODO Auto-generated method stub

	        }

	    });
	    */
		
		//TODO: Has to be a better way to go about setting these muscle groups - iterate through enum, use inspection??
		muscleIds.put(MuscleGroup.ABS, (ImageView) findViewById(R.id.abs));
		muscleIds.put(MuscleGroup.BICEPS, (ImageView) findViewById(R.id.biceps));
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

	        		if (touchColor == Color.parseColor(MuscleGroup.CHEST.getColour())) {
	        			intent.setClass(BodyDisplay.this, ChestActivity.class);
	        		} else if (touchColor == Color.parseColor(MuscleGroup.ABS.getColour())) {
	        			intent.setClass(BodyDisplay.this, MainActivity.class);
	        		} else if (touchColor == Color.parseColor(MuscleGroup.BICEPS.getColour())) {
	        			intent.setClass(BodyDisplay.this, BicepsActivity.class);
	        		}
	        		try {
		        		startActivity(intent);
		        		return true;
	        		} catch (android.content.ActivityNotFoundException anf) {
	        			return false;
	        		}
	            } else {
	            	return false;
	            }
	        }
	    });
	}
	
//	@Override
//	protected void onResume() {
//		super.onResume();
//		if (toCalibrate) {
//			calibrate();
//		} else {
//			shadeBody();
//		}
//	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			if (toCalibrate) {
				calibrate();
			} else {
				shadeBody();
			}
		}
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
				
				iv.setAlpha(activation/100F);
				iv.bringToFront();			
			}
		}		
	}
	
	public static void setActivation(MuscleGroup mg, int alpha) {
		muscleActivations.put(mg, alpha);
	}
	
	public static int getActivation(MuscleGroup mg) {
		return muscleActivations.get(mg);
	}
	
	//Calibrate with MVC of person
	public void calibrate() {
		/*TODO: Uncomment, and fix as expansion to MVP
		//Show instructions
		TextView tv = (TextView) findViewById(R.id.instructions);
		tv.setAlpha(1.0F);
		
		ImageView iv;
		for (MuscleGroup mg : MuscleGroup.values()) {
			if (muscleIds.containsKey(mg)) {
				iv = muscleIds.get(mg);
				//while(calibration info not received)
				for (int i = 0; i < 5; i++) {
					iv.bringToFront();
					iv.setAlpha(1.0F);
					iv.setAlpha(0.0F);
				}
			}
		}
		
		tv.setAlpha(0.0F);
		toCalibrate = false;
		*/
	}
}
