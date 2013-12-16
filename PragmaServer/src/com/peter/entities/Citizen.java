package com.peter.entities;

public class Citizen extends NPC{
	
    public Citizen() {
    	super("Citizen");
    	list.addType("Worm");

        stats.setExperience(2);
        stats.setMaxHitpoints(20);
        stats.setHitpoints(20);
    }
}