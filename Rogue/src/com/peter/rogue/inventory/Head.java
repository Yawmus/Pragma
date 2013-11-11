package com.peter.rogue.inventory;

public class Head extends Item{

	public static final Head HELMET = new Head("Helmet", 5, 17, "at.png");
	public static final Head HAT = new Head("Hat", 2, 6, "at.png");
	
	public Head(String name, int weight, int value, String filename) {
		super(name, weight, value, filename);
	}

}
