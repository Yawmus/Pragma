package com.peter.rogue.inventory;

public class Food extends Item{

	public static final Food BREAD = new Food("Bread", 1, 2, "f.png");
	public static final Food MEAT = new Food("Meat", 1, 4, "f.png");
	
	private Food(String name, int weight, int value, String filename) {
		super(name, weight, value, filename);
	}

}
