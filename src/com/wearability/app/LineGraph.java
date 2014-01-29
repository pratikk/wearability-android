package com.wearability.app;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.graphics.Color;
import android.content.Context;

public class LineGraph {
	
	private GraphicalView view;
	private TimeSeries dataset = new TimeSeries("signal");
	
	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	private XYSeriesRenderer renderer = new XYSeriesRenderer();
	
	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
	
	
	public LineGraph()
	{
		mDataset.addSeries(dataset);
		
		renderer.setColor(Color.GREEN);
		renderer.setPointStyle(PointStyle.SQUARE);
		renderer.setFillPoints(true);
		
		mRenderer.addSeriesRenderer(renderer);
		mRenderer.setXTitle("Time (ms)");
		mRenderer.setYTitle("Intensity");
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
}
