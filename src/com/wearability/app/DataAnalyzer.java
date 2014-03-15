package com.wearability.app;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

import android.util.Log;

//Note, counting reps is successful even if a small chunk of the data is given to the  algo
//Say give two seconds on first pass, then four seconds on next pass, etc. --> Works. :)

public class DataAnalyzer {
	
	//Parameters for peak values
	private static final String TAG = "wearability";

	private static final int threshold = 10;
	private static final int min_distance = 100;
	double mvc = 40;

	//Parameter for moving average
	private static final int mAvgBig = 40;
	private static final int mAvgSmall = 20;
	
	//Sampling parameter
	private static final double sampling_rate = 50;
	
	//Data containers
	private ArrayList<Double> rawData;
	private ArrayList<DataPoint> dataPoints; //processed data

	
	public DataAnalyzer(){
		this.rawData = new ArrayList<Double>();
		this.dataPoints  = new ArrayList<DataPoint>();
	}
	
	public DataAnalyzer(ArrayList<Double> rawData){
		if(rawData.size() > 1500)
		{
			this.rawData = rawData;
			ArrayList<Double> data1_rec = rectify(rawData, mean(rawData));
			
			//Do two passes of moving average
			ArrayList<Double> data1_smooth = smooth(data1_rec, mAvgBig);
			data1_smooth = smooth(data1_smooth, mAvgSmall);	
			
			dataPoints = findpeaks(data1_smooth, threshold, min_distance);	
		}
		
		else{
			this.rawData = new ArrayList<Double>();
			this.dataPoints  = new ArrayList<DataPoint>();		}
		
	}
	
	public void reset(){
		
		//Should this kill the processed data list??
		this.dataPoints.clear();

		ArrayList<Double> data1_rec = rectify(rawData, mean(rawData));
		
		ArrayList<Double> data1_smooth = smooth(data1_rec, mAvgBig);
		data1_smooth = smooth(data1_smooth, mAvgSmall);	
		
		//write_output(rawData, dataPoints, data1_smooth);
		this.dataPoints = findpeaks(data1_smooth, threshold, min_distance);
	}
	
	public void clear(){
		this.dataPoints.clear();
		this.rawData.clear();
	}
	public void addValue(double val){
		rawData.add(val);
		
		if(rawData.size()>600){
			//reset();
		}
	}

	public int getReps(){
		
		Log.d(TAG,"Calculating number of reps...");
		Log.d(TAG,"Total data points collected..." + rawData.size());
		if(rawData.size() < 600){
			return 0;
		}
		reset();
		Log.d(TAG,"Peak values..." + dataPoints.toString());
		

		return dataPoints.size();
	}
	
	public double getDuration(){
		
		return rawData.size()/sampling_rate;
	}
	public double getCadence(){
		double cadence = 0;
		int num_reps = dataPoints.size();
		
		//Make sure that there is at least one rep
		if (num_reps > 0) {
			cadence = (rawData.size()/sampling_rate)/dataPoints.size();
		} 
		else {
			cadence = 0;
		}
		return cadence;
	}
	
	public double getMeanEffort(){
		
		ArrayList<Double> data1_smooth = smooth(rawData, 840);
		
		//Second pass of smoothing with a moving average of 100 terms
		data1_smooth = smooth(data1_smooth, 100);
		
		double mean_effort = mean(data1_smooth)*100/mvc;
		
		return mean_effort;
		
	}
	
	public double getPeakEffort(){
		//Maximum effort as % of EMG
		if(dataPoints.size() > 0){
			double peak_effort = dataPoints.get(0).getyValue()*100/mvc;
			return peak_effort;
		}
		else {
			return 0;
		}

	}
	
	public double getCadence(int num_reps){
		
		double cadence;
		
		if (num_reps != 0) {
			cadence = (rawData.size()/sampling_rate)/dataPoints.size();
		}
		else {
				cadence = 0;
		}
		
		return cadence;
	}
	
	private ArrayList<Double> rectify(ArrayList<Double> rawData2, double mean_data){
				
		ArrayList<Double> data_rec = new ArrayList<Double>();
		
		for ( int i = 0; i < rawData2.size(); i++) {
	       data_rec.add(Math.abs(rawData2.get(i)-mean_data));
		}
		
		return data_rec;
	}

	/*Returns a smooth signal
	*data1_rec: the rectified data to be smoothed
	*k: the number of terms in the moving average*/
	private ArrayList<Double> smooth(ArrayList<Double> rawData2,int k){

		if(rawData2.size() < 600){
			return rawData2;
		}
		if (k > rawData.size()){
			k = rawData.size()/2;
		}
		ArrayList<Double> data_smooth = new ArrayList<Double>();
		
		//Get first term
		data_smooth.add(rawData2.get(0));
		for( int i=1; i<k; i++){
			data_smooth.set(0, data_smooth.get(0)+rawData2.get(i));
		}
		data_smooth.set(0, data_smooth.get(0)/k);
		
		//Get subsequent terms
		for( int i=1; i < rawData2.size() - k; i++){
			data_smooth.add(i, data_smooth.get(i-1) - rawData2.get(i-1)/k + rawData2.get(i+k-1)/k);
		}
		
		/*Get subsequent terms
				for( int i=1; i < data_smooth.size(); i++){
					data_smooth.set(i, data_smooth.get(i-1) - rawData2.get(i-1)/k+rawData2.get(i+k-1)/k);
				}
				*/
		return data_smooth;
	}
	
	/*Returns the number of reps by counting maximas
	*data1_smooth: the rectified and smoothed data (use a big window and a smaller window for smoothing)
	*threshold: the threshold above which to count the maximums
	*min_distance: the minimum distance (in data points) between maximas*/
    private ArrayList<DataPoint> findpeaks(ArrayList<Double> data1_smooth,int threshold, int min_distance){
    			
    	//Initialization
		ArrayList<DataPoint> dataset = new ArrayList<DataPoint>();
		double slope = 0;
		double previous_slope = 0;
		
		//Calculate local extremas using first difference operator
		slope = data1_smooth.get(1)-data1_smooth.get(0);
		for( int i=2; i<data1_smooth.size(); i++){
			previous_slope = slope;
			slope = data1_smooth.get(i) - data1_smooth.get(i-1);
			
			if(slope*previous_slope < 0){
				//Checks that it is a maximum above a threshold
				if(data1_smooth.get(i-1)>threshold){
				dataset.add(new DataPoint(i-1,data1_smooth.get(i-1)));
				}
			}
		}
		
		//Ensure that there is data
		if (dataset.size() != 0){
			
			//Distance between peaks
			Collections.sort((List<DataPoint>)dataset, new Comparator<DataPoint>() {
		        
		        public int compare(DataPoint  p1, DataPoint  p2)
		        {
		            if (p1.getyValue() > p2.getyValue()) {
		            	return -1;
		            } else {
		            	return 1;
		            }
		        }
		    });
			
			boolean state=true;
			boolean remove=false;
			int j = 0;
			ListIterator<DataPoint> listIterator = dataset.listIterator();
			
			listIterator.next();
			while(state==true && dataset.size()>1){
				for(int i=0; i<=j; i++){
					if(Math.abs(dataset.get(i).getxValue()-dataset.get(j+1).getxValue())<min_distance){
						remove=true;
					}
				}
				if(remove==true){
					listIterator.next();
					listIterator.remove();
					remove = false;
				} else{
					listIterator.next();
					j=j+1;
				}
				if(j==dataset.size()-1){
					state=false;
				}
				
			}
		} else{
			//Nothing so far, if it still breaks, add something. 
			}
	return dataset;
}
    
    public static double mean(ArrayList<Double> rawData2){
    	
		//Calculate the mean
    	double mean = 0;
	 	for(int i=0; i<rawData2.size(); i++){
	 		mean=mean+rawData2.get(i);
	 	}
		mean=mean/rawData2.size();
    	
		return mean;
	}
    
    /*Determines if the exercise is a left or right biceps curl
     * if true, left biceps dominates, else right biceps dominates
	 * smoothdata_left: smoothed left biceps data
	 * smoothdata_right: smoothed right biceps data
	 */
    //Test this function to ensure robustness
    //Extend to figuring out the difference between four exercises? 
    public static boolean test_left_bicep(ArrayList<Double> smoothdata_left, ArrayList<Double> smoothdata_right){
    	
    	//Get mean effort of left and right biceps
    	double mean_left = mean(smoothdata_left);
    	double mean_right = mean(smoothdata_right);
    	
    	boolean left=true;
    	
    	//Set the state of biceps curl 
    	if(mean_left>mean_right){
    		left=true;
    	} else{
    		left=false;
    	}
    	return left;
    }
    

    /*Reads EMG data from a file (data point on a new line)
	 * filename: name of the file
	 * length: number of data points in the file
	 */
	public static double[] read_data(String filename, int length) throws IOException
	{
	    FileReader file = new FileReader(filename);
	    
	    //Number of data points in the file
	    double[] doubles = new double [length];
	    int i=0;
	    try {
	        Scanner input = new Scanner(file);
	        while(input.hasNext())
	        {
	            doubles[i] = input.nextInt();
	            i++;
	        }
	        input.close();
	    }
	    catch(Exception e)
	    {
	        e.printStackTrace();
	    }
	   
	    //System.out.println(Arrays.toString(doubles));
	    return doubles;
	}
	
	
	/*Writes raw data, rectified data, and smoothed data to a file
	 * data1: raw EMG data
	 * data1_rec: rectified EMG data
	 * data1_smooth: smoothed and rectified EMG data
	 */
	
	public String getDataset(){
		
		StringBuilder sb = new StringBuilder();
		
		ArrayList<Double> data1_rec = rectify(rawData, mean(rawData));
		
		ArrayList<Double> data1_smooth = smooth(data1_rec, mAvgBig);
		data1_smooth = smooth(data1_smooth, mAvgSmall);	
		
		sb.append("\n");
		sb.append("\n Raw data \n");
		for(int i=0; i < rawData.size(); i++){
			sb.append(rawData.get(i));
			sb.append("\n");
		}
		

		sb.append("\n");
		sb.append("Smooth data \n");
		for(int i=0; i < data1_smooth.size(); i++){
			sb.append(data1_smooth.get(i));
			sb.append("\n");
		}
		
		sb.append("\n");
		sb.append("DataPoints - X\n");
		for(int i=0; i< dataPoints.size(); i++){
			sb.append(dataPoints.get(i).getxValue());
			sb.append("\n");
		}
		
		sb.append("\n");
		sb.append("DataPoints - Y\n");
		for(int i=0; i< dataPoints.size(); i++){
			sb.append(dataPoints.get(i).getyValue());	
			sb.append("\n");
		}
		
		return sb.toString();
	}
	public static void write_output(ArrayList<Double> data, ArrayList<DataPoint> dataset, ArrayList<Double> smoothData){
	
		
		PrintWriter writer = null;
	try {
		writer = new PrintWriter("output.txt", "UTF-8");
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (UnsupportedEncodingException e) {
		e.printStackTrace();
	}
	
//	, double[] data_rec, double[] data_smooth, ArrayList<DataPoint> dataset, int num_reps, double cadence, double mean_effort, double peak_effort
	
	writer.println("Original data");
	for(int i=0; i<data.size(); i++){
		writer.println(data.get(i));	
	}
	
	writer.println("");
	writer.println("DataPoints - X");
	for(int i=0; i< dataset.size(); i++){
		writer.println(dataset.get(i).getxValue());	
	}
	
	writer.println("");
	writer.println("DataPoints - Y");
	for(int i=0; i< dataset.size(); i++){
		writer.println(dataset.get(i).getyValue());	
	}
	
//	writer.println("");
//	writer.println("Smoothed data");
//	for(int i=0; i<data_smooth.length; i++){
//		writer.println(data_smooth[i]);	
//	}
//	
//	writer.println("");
//	writer.println("Peaks - x values");
//	for (int i= 0; i < dataset.size(); i++) {
//		writer.println(dataset.get(i).getxValue());
//	}
//	
//	writer.println("");
//	writer.println("Peaks - y values");
//	for (int i= 0; i < dataset.size(); i++) {
//		writer.println(dataset.get(i).getyValue());
//	}
//	
//	writer.println("");
//	writer.println("Number of reps");
//	writer.println(num_reps);
//	
//	writer.println("");
//	writer.println("Cadence (average time/reps)");
//	writer.println(cadence);
//	
//	writer.println("");
//	writer.println("Mean effort as a % of MVC");
//	writer.println(Math.floor(mean_effort) + "%");
//	
//	writer.println("");
//	writer.println("Peak effort as a % of MVC");
//	writer.println(Math.floor(peak_effort) + "%");
	
	writer.close();
	}
	
}
