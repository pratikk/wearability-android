package com.wearability.app;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_body_display);
		// Show the Up button in the action bar.
		setupActionBar();
		
		ImageView iv = (ImageView) findViewById(R.id.muscle_areas);
		
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
	        			ImageView abs = (ImageView) findViewById(R.id.abs);
	        			abs.bringToFront();
	        			Float f = abs.getAlpha();
	        			if (abs.getAlpha() < 0.33) {
	        				abs.setAlpha(0.33F);
	        			} else if (abs.getAlpha() < 0.66) {
	        				abs.setAlpha(0.66F);
	        			} else {
	        				abs.setAlpha(1F);
	        			}
	        			
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
	
//	public void onTouchEvent(View view, MotionEvent event){
//		
//		if (event.getAction() == MotionEvent.ACTION_DOWN){
//            final int x = (int) event.getX();
//            final int y = (int) event.getY();
//                		
//    		Intent intent = new Intent();
//    		
//    		int touchColor = getHotspotColor (R.id.muscle_areas, x, y);
//
//    		if (touchColor == Color.parseColor("#FF0000")) {
//    			intent.setClass(this, ChestActivity.class);
//    		} else {
//    			intent.setClass(this, BodyDisplay.class);
//    		}
//    		startActivity(intent);
//
//        }
//	}
	
	// EXAMPLE CODE 
//	public boolean onTouch (View v, MotionEvent ev) {
//		 final int action = ev.getAction();
//		 // (1) 
//		 final int evX = (int) ev.getX();
//		 final int evY = (int) ev.getY();
//		 Object nextImage;
//		switch (action) {
//			 case MotionEvent.ACTION_DOWN :
//				 /*if (currentResource == R.drawable.muscles) {
//					 nextImage = R.drawable.p2_ship_pressed;
//				 } */
//				 //This is for the ship to start motion, instead we will overlay with semi-transparent green layer?
//				 break;
//			 case MotionEvent.ACTION_UP :
//			   // On the UP, we do the click action.
//			   // The hidden image (image_areas) has three different hotspots on it.
//			   // The colors are red, blue, and yellow.
//			   // Use image_areas to determine which region the user touched.
//			   // (2)
//				 int touchColor = getHotspotColor (R.id.muscle_areas, evX, evY);
//			   // Compare the touchColor to the expected values. 
//			   // Switch to a different image, depending on what color was touched.
//			   // Note that we use a Color Tool object to test whether the 
//			   // observed color is close enough to the real color to
//			   // count as a match. We do this because colors on the screen do 
//			   // not match the map exactly because of scaling and
//			   // varying pixel density.
//				 int tolerance = 25;
//				 nextImage = R.drawable.p2_ship_default;
//			   // (3)
//				 if (closeMatch (Color.RED, touchColor, tolerance)) {
//			      // Do the action associated with the RED region
//					 nextImage = R.drawable.p2_ship_alien;
//				 } else {
//					 //...
//				 }
//				 break;
//		  } // end switch
//		  if (nextImage > 0) {
//		    imageView.setImageResource (nextImage);
//		    imageView.setTag (nextImage);
//		  }
//		  return true;
//	  }
	
	public int getHotspotColor (int hotspotId, int x, int y) {
		  ImageView img = (ImageView) findViewById (hotspotId);
		  img.setDrawingCacheEnabled(true); 
		  Bitmap hotspots = Bitmap.createBitmap(img.getDrawingCache()); 
		  img.setDrawingCacheEnabled(false);
		  return hotspots.getPixel(x, y);
	}

}
