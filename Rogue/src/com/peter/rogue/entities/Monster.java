package com.peter.rogue.entities;

public class Monster extends NPC{
	
	public Monster(String filename, String type) {
		super(filename, type);
		name = type;
		

	}
	public void update(float delta){
		super.update(delta);
	}
}
