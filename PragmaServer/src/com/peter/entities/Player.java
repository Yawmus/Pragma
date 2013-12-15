package com.peter.entities;

public class Player extends Animate{

	private String picture;
	
	public Player(){
		super("Player");
		stats.setHitpoints(20);
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getPicture(){
		return picture;
	}
}