package com.peter.entities;

public class Citizen extends NPC{

	public Citizen(String race, String type, String name) {
		super("c_.png", race, type, name);
		stats.setLevel(1);
		stats.setHitpoints(10);
		stats.setDexterity(5);
		stats.setStrength(1);
		stats.setExperience(100);
	}
}