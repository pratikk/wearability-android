package com.wearability.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;


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
		    int cadence = extras.getInt("cadence");
		    int duration = extras.getInt("duration");
		    double mean = extras.getDouble("mean");
		    double peak = extras.getDouble("peak");
		    
		    TextView repsView = (TextView) findViewById(R.id.reps);
		    repsView.setText("" + reps);
		    
		    TextView cadView = (TextView) findViewById(R.id.cadence);
		    cadView.setText("" + cadence);
		    
		    TextView durView = (TextView) findViewById(R.id.duration);
		    durView.setText("" + duration);
		    
		    TextView meanView = (TextView) findViewById(R.id.mean);
		    meanView.setText("" + mean + "%");
		    
		    TextView peakView = (TextView) findViewById(R.id.peak);
		    peakView.setText("" + peak + "%");
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
}
