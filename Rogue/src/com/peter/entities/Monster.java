package com.peter.entities;

public class Monster extends NPC{
	
	public Monster(String filename, String type) {
		super(filename, type);
		name = type;
	}
}
