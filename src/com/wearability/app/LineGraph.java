package com.wearability.app;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.graphics.Color;
import android.content.Context;

public class LineGraph {
	

	private GraphicalView view;
	private XYSeries dataset = new XYSeries("signal");
	private XYSeries threshold = new XYSeries("threshold");
	
	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	private XYSeriesRenderer renderer = new XYSeriesRenderer();
	private XYSeriesRenderer renderer2 = new XYSeriesRenderer();
	
	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
	
	//Keep track of running total for mean
	private float total_activ;
	
	
	
	public LineGraph()
	{
		
		mDataset.addSeries(dataset);
		
		renderer.setColor(Color.BLUE);
		renderer.setPointStyle(PointStyle.POINT);
		renderer.setFillPoints(true);
		
		mRenderer.setPanEnabled(false,false);
		mRenderer.setApplyBackgroundColor(true);
		mRenderer.setBackgroundColor(Color.parseColor("#fff3f3f3"));
		mRenderer.setMarginsColor(Color.parseColor("#fff3f3f3"));
		
		mRenderer.addSeriesRenderer(renderer);
		mRenderer.setXTitle("Time (ms)");
		mRenderer.setYTitle("Intensity");
	}
	
	public LineGraph(boolean weighted)
	{
		mDataset.addSeries(dataset);
		mDataset.addSeries(threshold);
		
		renderer.setColor(Color.GREEN);
		renderer.setPointStyle(PointStyle.SQUARE);
		renderer.setFillPoints(true);
		
		renderer2.setColor(Color.RED);
		
		mRenderer.addSeriesRenderer(renderer);
		mRenderer.addSeriesRenderer(renderer2);
		mRenderer.setXTitle("Time (ms)");
		mRenderer.setYTitle("Intensity");
		mRenderer.setYAxisMax(280);	//Change later if needed
	}
	

	public GraphicalView getView(Context context)
	{
		view = ChartFactory.getLineChartView(context, mDataset, mRenderer);
		return view;
	}
	
	public void addNewPoints(Point p)
	{
		dataset.add(p.getX(), p.getY());
	}
	
	public void addRectifiedPoint(Point p)
	{
		total_activ += p.getY();
		float rectified = Math.abs(p.getY() - (total_activ/(dataset.getItemCount() + 1)));
		dataset.add(p.getX(), rectified);
	}
	//rectify = abs(Data - mean(data))
	public void addWeightedPoint(LineGraph lg, Point p) {
		final int NUM_POINTS = 100;	//170 ms = 34 points
		if (lg.dataset.getItemCount() >= NUM_POINTS) {		//Enough points for weighted avg
			int w_avg = 0; //see if floats work
			for (int i = lg.dataset.getItemCount() - NUM_POINTS; i < lg.dataset.getItemCount(); i++) {
				w_avg += lg.dataset.getY(i);	//getX() doesn't matter
			}
			w_avg = w_avg / NUM_POINTS;
			dataset.add(p.getX(), w_avg);
			threshold.add(p.getX(), 260);
		} else {
		}
	}
	
	public int getLastX()
	{
		return dataset.getItemCount();
	}
}
