package com.wearability.app;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter.LengthFilter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ExerciseProgress extends Activity {

	//Constants
	private static final String TAG = "wearability";
	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private static final String address = "00:06:66:07:B5:FF";
	
	TextView txtArduino;
	Handler h;
	
	boolean firstClick;

	
	//Bluetooth
	final int RECEIVE_MESSAGE = 1;		// Status  for Handler
	private BluetoothAdapter mBluetoothAdapter = null;
	private BluetoothSocket btSocket = null;
	private StringBuilder sb = new StringBuilder();
	
	private StringBuilder sb2 = new StringBuilder();
  
	private ManageThread mManageThread;
	
	private ConnectThread mConnectThread;
 
	private ArrayList<Double> mRawData;
	private DataAnalyzer mDataAnalyzer;
	
	private boolean BTState;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exercise_progress);
		
		setupActionBar();
	    //txtArduino = (TextView) findViewById(R.id.rep_counter);		// for display the received data from the Arduino

	    mDataAnalyzer = new DataAnalyzer();
	    
	    firstClick = true;
	    
	    h = new Handler() {
	    	@Override
			public void handleMessage(android.os.Message msg) {
	    		switch (msg.what) {
	            case RECEIVE_MESSAGE:													// if receive massage
	            	byte[] readBuf = (byte[]) msg.obj;
	            	String strIncom = new String(readBuf, 0, msg.arg1);					// create string from bytes array
	            	
	            	sb2.append(strIncom + "--");
	            	sb.append(strIncom);												// append string
	            	//PKTEST
	            	while(sb.length() != 0) {
	            		boolean goodValue = true;
		            	int endOfLineIndex = sb.indexOf("\r\n");							// determine the end-of-line
		            	if (endOfLineIndex == -1) {
		            		goodValue = false;
//		            		endOfLineIndex = sb.indexOf("\r");
//		            		if (endOfLineIndex == -1) {
//		            			endOfLineIndex = sb.indexOf("\n");
//		            		}
		            	} else {		//Good value, check if \r or \n will sneak in 
		            		if (sb.indexOf("\r") < endOfLineIndex) {
		            			endOfLineIndex = sb.indexOf("\r");
		            			goodValue = false;
		            		} else if (sb.indexOf("\n") < endOfLineIndex) {
		            			endOfLineIndex = sb.indexOf("\n");
		            			goodValue = false;
		            		}
		            	}
		            	
		            	if (endOfLineIndex > 0) { 											// if end-of-line,
		            		String sbprint = sb.substring(0, endOfLineIndex);				// extract string
		                    if (goodValue) {	//should be + 2, +1 I think
		                    	sb.delete(0, endOfLineIndex + 4);										// and clear including carriage returns
		                    } else {
		                    	sb.delete(0, endOfLineIndex + 2);										// and clear including carriage returns
		                    }
		                    
		                	if (sbprint.length() == 4 && goodValue) {	
		                		//txtArduino.setText(mDataAnalyzer.getReps() + " reps");
		                		mDataAnalyzer.addValue(parseValue(sbprint));
		                		}
		                		
		                	//btnOff.setEnabled(true);
		                	//btnOn.setEnabled(true); 
		                } else if (endOfLineIndex == 0) {
		                	sb.delete(0, 2);
		                } else {
		            		//Log.d(TAG, "The number of points: " + strIncom + "...");
		                	break;
		            	}
	            	}
	            	//Log.d(TAG, "...String:"+ sb.toString() +  "Byte:" + msg.arg1 + "...");
	            	break;
	    		}
	        };
		};
	    
	    
	    
	}

	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.exercise_progress, menu);
		return true;
	}
	
	@Override
	  public void onPause() {
	    super.onPause();
	 
	    Log.d(TAG, "...In onPause()...");
	    if (mManageThread != null){
	    	mManageThread.cancel();	
	    }
	    if (mConnectThread != null){
	    	mConnectThread.cancel();	
	    }
	   
	    
	  }
	   
	  @Override
	  public void onResume() {
	    super.onResume();
	 
	    
	    Log.d(TAG, "...onResume - try connecting...");
	    
	  
	    mConnectThread = new ConnectThread();
	    mConnectThread.start();
	    
	  }
	 
	
	public void goToResults(View view){
	    //txtArduino = (TextView) findViewById(R.id.rep_counter);		// for display the received data from the Arduino
		//txtArduino.setText("Total samples: "+mDataAnalyzer.getDuration() + "\n" +"Total"+ mDataAnalyzer.getReps());
		//sendEmail(sb2.toString() + mDataAnalyzer.getDataset());
		//sendEmail(mDataAnalyzer.getDataset());
		//writeStringAsFile(mDataAnalyzer.getDataset(), "filetextdata");
		//Intent intent = new Intent(this, Results.class);
		//intent.putExtra("E_TYPE_DATA",);
		//startActivity(intent);
	}
	
	public void sendEmail(String sendThis){
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822");
		i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"marsimard@gmail.com","chris@menezes.ca"});
		i.putExtra(Intent.EXTRA_SUBJECT, "android data");
		i.putExtra(Intent.EXTRA_TEXT   , sendThis);
		try {
		    startActivity(Intent.createChooser(i, "Send mail..."));
		} catch (android.content.ActivityNotFoundException ex) {
		    Toast.makeText(ExerciseProgress.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	public void clearData(View v){
		Log.d(TAG,"clear data");
		
	}
	
	public void clickHandler(View view) {
		if(!BTState){
			Toast.makeText(ExerciseProgress.this, "Bluetooth not paired", Toast.LENGTH_SHORT).show();
		}
		else{
			if (firstClick) {
				ImageView iv = (ImageView)findViewById(R.id.timerBtn);
				iv.setImageResource(R.drawable.stopbutton);
				firstClick = false;
				
			} else {
				Intent intent = new Intent(this, Results.class);
				intent.putExtra("reps", mDataAnalyzer.getReps());
				intent.putExtra("peak", mDataAnalyzer.getPeakEffort());
				intent.putExtra("mean", mDataAnalyzer.getMeanEffort());
				intent.putExtra("cadence", mDataAnalyzer.getCadence());
				intent.putExtra("duration", mDataAnalyzer.getDuration());
				startActivity(intent);
				firstClick = true;
			}
		}
		
	}
	
	private void errorExit(String title, String message){
	    Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
	    finish();
	  }
	
	 private int parseValue(String text){
		  char[] tempBuf = null;
		  tempBuf = new char[3];	
		  
		   //Log.d(TAG, "...Attempting to parse " + text + "...");

			try {
			text.getChars(0, 2, tempBuf, 0);
			
			
			} catch (StringIndexOutOfBoundsException e) {
				Log.e("wearability", "Could not parse point", e);
				throw e;
			}
			
			String temp2 = new String(tempBuf);
			//Log.d(TAG, "...This should be an int: " + temp2 + "...");
			
		    int toReturn = Integer.parseInt(temp2.trim(), 16);
		    
		    return toReturn;
			
	  }
	 
	 public void writeStringAsFile(final String fileContents, String fileName) {
	        Context context = this;
	        try {
	            FileWriter out = new FileWriter(new File(context.getFilesDir().getPath(), fileName));
	            out.write(fileContents);
	            out.close();
	        } catch (IOException e) {
				Log.d(TAG, "...Fail writing :( " + e + "...");

	        }
	    }

	 private void checkBTState() {

		 // Check for Bluetooth support and then check to make sure it is turned on
	    // Emulator doesn't support Bluetooth and will return null
	    if(mBluetoothAdapter==null) { 
	    	errorExit("Fatal Error", "Bluetooth not support");
	    } 
	    else {
	    	if (mBluetoothAdapter.isEnabled()) {
	    		Log.d(TAG, "...Bluetooth ON...");
	    	}
	    	else {
	    		//Prompt user to turn on Bluetooth
	    		Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	    		startActivityForResult(enableBtIntent, 1);
	      }
	    }
	  }
 
	 
	 private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
	      if(Build.VERSION.SDK_INT >= 10){
	          try {
	              final Method  m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
	              return (BluetoothSocket) m.invoke(device, MY_UUID);
	          } catch (Exception e) {
	              Log.e(TAG, "Could not create Insecure RFComm Connection",e);
	          }
	      }
	      return  device.createRfcommSocketToServiceRecord(MY_UUID);
	  }

  private class ManageThread extends Thread {
	    private InputStream mmInStream;
	    private OutputStream mmOutStream;
	    private DataInputStream dinput;

	    private BluetoothSocket mmSocket;
	    
	    public ManageThread(BluetoothSocket socket) {
	        InputStream tmpIn = null;
	        OutputStream tmpOut = null;
	 
	        Log.d(TAG,"Starting manageThread");

	        mmSocket = socket;
	        // Get the input and output streams, using temp objects because
	        // member streams are final
	        try {
	            tmpIn = mmSocket.getInputStream();
	            tmpOut = mmSocket.getOutputStream();
	            dinput = new DataInputStream(tmpIn);
	            
	        } catch (IOException e) { 
	        	Log.d(TAG, "IOException: " + e.getMessage() + " --- ");
	        }
	 
	        mmInStream = tmpIn;
	        mmOutStream = tmpOut;
	    }
	 
	    
	    @Override
		public void run() {
	    	BTState = true;
	        byte[] buffer = new byte[6];  // buffer store for the stream
	        int bytes; // bytes returned from read()

	        // Keep listening to the InputStream until an exception occurs
	        while (true) {
	        	try {
	                // Read from the InputStream
	                //bytes = mmInStream.read(buffer);		// Get number of bytes and message in "buffer"
	                dinput.readFully(buffer, 0, 6);
                    h.obtainMessage(RECEIVE_MESSAGE, 6, -1, buffer.clone()).sendToTarget();		// Send to message queue Handler
	            } catch (IOException e) {
	            	
	            	Log.d(TAG, "...Error data read: " + e.getMessage() + "...");
	                break;
	            }
	        }
	    }
	    
	    public void cancel()
        {
	    	BTState = false;
	        Log.d(TAG,"Cancel manageThread");

            if (mmOutStream != null)
            {
                try {mmOutStream.close();} catch (Exception e) { Log.e(TAG, "close() of outputstream failed", e); }
                mmOutStream = null;
            }

            if (mmInStream != null)
            {
                try {mmInStream.close();} catch (Exception e) { Log.e(TAG, "close() of inputstream failed", e); }
                mmInStream = null;
            }

            if (mmSocket != null)
            {
                try {mmSocket.close();} catch (Exception e) { Log.e(TAG, "close() of connect socket failed", e); }
                mmSocket = null;
            }
        }
	    
	    
	}
  private class ConnectThread extends Thread {
	    private  BluetoothSocket mmSocket;
	    private BluetoothDevice mmDevice;
	 
	    public ConnectThread() {

	    	
	        Log.d(TAG,"Starting connectThread");

	    	 if (mManageThread != null){
		  	    	mManageThread.cancel();
		  	    	Thread moribund = mManageThread;
		  	    	mManageThread = null;
		  	    	moribund.interrupt();
		  	    }
		  	    if (mConnectThread != null){
		  	    	mConnectThread.cancel();
		  	    	Thread moribund = mConnectThread;
		  	    	mConnectThread = null;
		  	    	moribund.interrupt();   
		  	    }
	    	  
		  	    getSocket();

	    }
	 
	    public void getSocket(){
	    	mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();		// get Bluetooth adapter
		    checkBTState();
	    	BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
	        // Use a temporary object that is later assigned to mmSocket,
	        // because mmSocket is final
	        BluetoothSocket tmp = null;
	        mmDevice = device;
	 
	        // Get a BluetoothSocket to connect with the given BluetoothDevice
	        try {
	            // MY_UUID is the app's UUID string, also used by the server code
	            tmp = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
	        } catch (IOException e) { }
	        mmSocket = tmp;
	    }
	    
	    void wrapSocketConnect(){
	    	if (mmSocket == null){
	    		getSocket();
	    	}
	    	try {
	    		mBluetoothAdapter.cancelDiscovery();
				mmSocket.connect();
			} catch (IOException connectException) {
	            // Unable to connect; close the socket and get out
	        	Log.d(TAG,"..Call to socket.Connect failed" + connectException);
			}
	    }
	    
	    public void run() {
	        // Cancel discovery because it will slow down the connection
	        
	 
	        int numFails = 0;
	        int MAX_FAILS = 3;
	        boolean success = false;
	        	        
	        while(numFails < MAX_FAILS && success == false){
	        	wrapSocketConnect();
	        	if(!mmSocket.isConnected()) {
	        		numFails++;
	        	}
	        	else{
	        		success = true;
	        	}
	        	
	        }
	              
	        if(success == false){
	        	 mBluetoothAdapter.disable();
	 	        try {
	 				Thread.sleep(500);
	 			} catch (InterruptedException e) {
	 				Log.d(TAG, "couldn't sleep thread!");
	 			}                  
	            getSocket();
	 	        mBluetoothAdapter.cancelDiscovery();
	 	        wrapSocketConnect();
	        }
	       
	        if(!mmSocket.isConnected()){
	        	
		        try {
	                mmSocket.close();
	            } catch (IOException closeException) { 
	            	Log.d(TAG,"..Could not close socket" + closeException);
	            }
	            return;
		        }
	        else{
	        	 // Do work to manage the connection (in a separate thread)
		        if (mManageThread != null){
		        	mManageThread.cancel();
		        }
		    	mManageThread = new ManageThread( mmSocket);
		    	mManageThread.start();
		        ExerciseProgress.this.runOnUiThread(new Runnable() {
		        	  public void run() {
		        		  Toast.makeText(ExerciseProgress.this, "Connected to BT", Toast.LENGTH_SHORT).show();
		        		  }
		        	});
		        }
	 
	       
	    }
	 
	    /** Will cancel an in-progress connection, and close the socket */
	    public void cancel() {
	        Log.d(TAG,"Cancel connectThread");

	    	 if (mmSocket != null)
	            {
	                try {mmSocket.close();} catch (Exception e) { Log.e(TAG, "close() of connect socket failed", e); }
	                mmSocket = null;
	            }
	    }
	}
}
