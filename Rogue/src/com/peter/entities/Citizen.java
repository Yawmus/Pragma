package com.peter.entities;

public class Citizen extends NPC{

	public Citizen(int level, String race, String type, String name) {
		super("c_.png", level, race, type, name);
		symbol = 'C';
	}
}