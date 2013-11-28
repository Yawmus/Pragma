package com.peter.entities;


public class Entity{
	
	protected String type = new String();
	protected String name;
	public Integer ID;
	private static int count = 100;
	protected float tileWidth = 32, tileHeight = 32;
	protected int timeout = 0;
	public boolean canDraw = false;
	private int x, y;
	

	public Entity(String filename, String type){
		name = new String("null");
		this.type = type;
		ID = ++count;
	}

	public String getName(){
		return name;
	}
	
	public String getType() {
		return type;
	}
	
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public void setX(int x){
		this.x = x;
	}
	public void setY(int y){
		this.y = y;
	}
	public void setPosition(int x, int y){
		this.x = x*32;
		this.y = y*32;
	}
}

