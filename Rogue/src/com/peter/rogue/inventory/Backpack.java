package com.peter.rogue.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Backpack extends Sprite{

	private String name;
	private int capacity;
	
	public static final Backpack SMALL = new Backpack("Small backpack", "G_.png", 6);
	public static final Backpack LARGE = new Backpack("Large backpack", "backpack.png", 12);
	protected Backpack(){};
	protected Backpack(String name, String filename, int capacity) {
		setName(name);
		this.capacity = capacity;
		setTexture(new Texture("img/" + filename));
	}
	
	public int getCapacity(){
		return capacity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
