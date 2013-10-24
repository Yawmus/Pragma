package com.peter.rogue.entities;

public class Monster extends NPC{
	
	public Monster(String filename, String type) {
		super(filename, type);
		hostile = true;
		message = "*Gurgle*";
	}
}
