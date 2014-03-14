package com.wearability.app;

public class DataPoint {
	private double xValue;
	private double yValue;
	
	public DataPoint(double X, double Y) {
		setxValue(X);
		setyValue(Y);
	}

	public double getxValue() {
		return xValue;
	}

	public void setxValue(double xValue) {
		this.xValue = xValue;
	}

	public double getyValue() {
		return yValue;
	}

	public void setyValue(double yValue) {
		this.yValue = yValue;
	}
	
	
	
}
