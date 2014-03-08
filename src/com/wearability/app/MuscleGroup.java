package com.wearability.app;

public enum MuscleGroup {
	ABS("#009415"), CHEST("#FF0000"), BICEPS("#06FF00");
	
	private String colourCode;
	
	private MuscleGroup (String colour) {
		setColour(colour);
	}

	public String getColour() {
		return colourCode;
	}

	public void setColour(String colour) {
		this.colourCode = colour;
	}
}
