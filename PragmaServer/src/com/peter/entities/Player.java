package com.peter.entities;

public class Player extends Entity{

	private String picture;
	public short[] color;
	
	public Player(){
		super("Human", "Player");
		color = new short[4];
		stats.setHitpoints(20);
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getPicture(){
		return picture;
	}
}