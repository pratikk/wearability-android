package com.wearability.app;

public enum MuscleGroup {
	ABS("009415"), CHEST("FF0000"), BICEPS("06FF00"), LOWERBACK("0036FF");
	
	private String colourCode;
	
	private MuscleGroup (String colour) {
		colourCode = colour;
	}
}
