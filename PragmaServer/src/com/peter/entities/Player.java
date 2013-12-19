package com.peter.entities;

public class Player extends Entity{

	private String picture;
	
	public Player(){
		super("Human", "Player");
		stats.setHitpoints(20);
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getPicture(){
		return picture;
	}
}