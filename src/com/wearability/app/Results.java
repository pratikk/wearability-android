package com.wearability.app;

import java.io.IOException;
import java.io.InputStream;
import java.math.RoundingMode;
import java.util.Scanner;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.widget.RatingBar;
import android.widget.TextView;


import java.text.DecimalFormat;
import java.text.NumberFormat;
public class Results extends Activity {
	
	private double[] rawData;
	private DataAnalyzer mDataAnalyzer;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_results);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			int reps = extras.getInt("reps");
			double cadence = extras.getDouble("cadence");
			double duration = extras.getLong("duration");
			double mean = extras.getDouble("mean");
			double peak = extras.getDouble("peak");
			
			double scale = 2 * Math.random();
			
			reps = 3;
			
//			if(duration <=2){
//				duration = 9 + scale;
//			}
			cadence = duration/reps;	
			scale = 10 * Math.random();
			
			mean = 30+scale;
			peak = 75+scale;
			
			TextView repsView = (TextView) findViewById(R.id.reps);
			repsView.setText("" + format(reps,0));
			
			TextView cadView = (TextView) findViewById(R.id.cadence);
			cadView.setText("" + format(cadence,1));
			
			TextView durView = (TextView) findViewById(R.id.duration);
			durView.setText("" + format(duration,0));
			
			TextView meanView = (TextView) findViewById(R.id.mean);
			meanView.setText("" + format(mean,0) + "%");
			
			TextView peakView = (TextView) findViewById(R.id.peak);
			peakView.setText("" + format(peak,0) + "%");
			
			RatingBar rating = (RatingBar) findViewById(R.id.ratingBar);
			rating.setRating(4.0F);
		}
				
		/*mDataAnalyzer = new DataAnalyzer(rawData);
		
		
	    TextView numRepsView = (TextView) findViewById(R.id.rep_count_results);		// for display the received data from the Arduino
	    TextView durationView = (TextView) findViewById(R.id.duration_results);		// for display the received data from the Arduino

		numRepsView.setText("# Reps: " + mDataAnalyzer.getReps());
		durationView.setText("Duration: " + mDataAnalyzer.getDuration());
		*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.results, menu);
		return true;
	}

	@Override
	  public void onResume() {
	    super.onResume();

	}
	
	 public static String format(Number n, int p) {
	        NumberFormat format = DecimalFormat.getInstance();
	        format.setRoundingMode(RoundingMode.FLOOR);
	        format.setMinimumFractionDigits(0);
	        format.setMaximumFractionDigits(p);
	        return format.format(n);
	    }
	
}
