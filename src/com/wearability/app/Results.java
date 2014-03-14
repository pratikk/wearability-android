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
