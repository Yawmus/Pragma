package com.peter.rogue.inventory;

import com.peter.rogue.entities.Entity;

public class Item extends Entity{
	private int weight;
	private int value;
	private String filename;
	private boolean pickedUp;
	
	public Item(String name, int weight, int value, String filename){
		super(filename, "Item");
		this.name = name;
		this.weight = weight;
		this.value = value;
		this.filename = filename;
	}
	/*
	public Item(Item item){
		super(item.filename, "Item");
		this.name = item.name;
		this.weight = item.weight;
		this.value = item.value;
		this.filename = item.filename;
	}*/
	
	@Override
	public void setPosition(float x, float y){
		setX(x * tileWidth);
		setY(y * tileHeight);

		pickedUp = false;
		map.put(ID, this);
		map.setMark(ID, getX(), getY());
	}
	
	public int getWeight() {
		return weight;
	}
	public int getValue(){
		return value;
	}
	public String getFilename() {
		return filename;
	}
	


	public void pickedUp(boolean pickedUp){
		this.pickedUp = pickedUp;
	}
	public boolean isPickedUp(){
		return pickedUp;
	}
}