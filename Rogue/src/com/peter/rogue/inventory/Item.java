package com.peter.rogue.inventory;

import com.peter.rogue.entities.Entity;

public class Item extends Entity{
	private int weight;
	public Item(String name, int weight, String filename){
		super(filename, "Item");
		this.name = name;
		this.weight = weight;
	}
	public int getWeight() {
		return weight;
	}
	@Override
	public void setPosition(float y, float x){
		setY(y * 32);
		setX(x * 32);
		pickedUp = false;
		setMap(getX(), getY(), this.entry);
	}
}