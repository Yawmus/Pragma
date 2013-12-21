package com.peter.entities;

import com.peter.server.Global;

public class Citizen extends NPC{
	
	public static final String Drunk = "Drunk";
	public static final String Ditcher = "Ditcher";
	public static final String Gardener = "Gardener";
	public static final String Serf = "Serf";
	
	public static String randType(){
		switch(Global.rand(5,  0)){
		case 0:
			return Drunk;
		case 1:
			return Ditcher;
		case 2:
			return Gardener;
		case 3:
			return Serf;
		}
		return null;
	}
	
    public Citizen(int floor, String type){
    	super(floor, "Citizen", "Human", type);
    	list.addRace("Worm");

        stats.setExperience(2);
        stats.setMaxHitpoints(20);
        stats.setHitpoints(20);
    }
    
    public Citizen(int floor){
    	super(floor, "Citizen", "Human", randType());
    	list.addRace("Worm");

        stats.setExperience(2);
        stats.setMaxHitpoints(20);
        stats.setHitpoints(20);
    }
}