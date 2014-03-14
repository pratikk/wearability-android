package com.wearability.app;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ExerciseList extends ListActivity {
	
    String[] listItems = {"Left Bicep Curl", "Right Bicep Curl", "Left Tricep extension", "Right Tricep extension",}; 


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exercise_list);
//        setListAdapter(new ArrayAdapter(this,  android.R.layout.simple_list_item_1, listItems));
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);

                TextView textView=(TextView) view.findViewById(android.R.id.text1);

                /*YOUR CHOICE OF COLOR*/
                textView.setTextColor(Color.WHITE);

                return view;
            }
        };
        setListAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.exercise_list, menu);
		return true;
	}
	
	public void startExercise(String exerciseType){
		Intent intent = new Intent(this, ProgressActivity.class);
		intent.putExtra("E_TYPE_DATA",exerciseType);
		startActivity(intent);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		
		String selection = l.getItemAtPosition(position).toString();
		Toast.makeText(this, selection, Toast.LENGTH_LONG).show();
		startExercise(selection);
		super.onListItemClick(l, v, position, id);
	}

	
}
