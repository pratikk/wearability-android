package com.wearability.app;

import org.achartengine.GraphicalView;
import android.app.Activity;
import android.os.Bundle;


public class DynamicGraphActivity extends Activity {
	
	private static GraphicalView view;
	private LineGraph line = new LineGraph();
	private static Thread thread;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		thread = new Thread() {
			public void run()
			{
				for (int i = 0; i < 15; i++)
				{
					try{
						Thread.sleep(2000);
					} catch (InterruptedException e){
						e.printStackTrace();
					}
					Point p = MockData.getDataFromReceiver(i);
					line.addNewPoints(p);
					view.repaint();
					
				}
			}
		};
		thread.start();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		view = line.getView(this);
		setContentView(view);
		
	}
}
