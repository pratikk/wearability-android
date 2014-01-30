package com.wearability.app;

import android.util.Log;

public class Point {
	
	private int x;
	private int y;
	
	public Point( int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Point(String text){
		char[] tempBuf = null;
		tempBuf = new char[2];
		
		try {
		text.getChars(2, 4, tempBuf, 0);
		
		
		} catch (StringIndexOutOfBoundsException e) {
			Log.e("wearability", "Could not parse point", e);
			throw e;
		}
		
		String temp2 = new String(tempBuf);
		int i = Integer.parseInt(temp2);
		this.x = i;
		this.y = 1;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	
}
