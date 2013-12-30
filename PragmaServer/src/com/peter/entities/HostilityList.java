package com.peter.entities;

import java.util.LinkedList;

public class HostilityList{
	LinkedList<String> race;
	LinkedList<Integer> ID;
	
	public HostilityList(){
		race = new LinkedList<String>();
		ID = new LinkedList<Integer>();
	}
	
	public boolean check(Entity entity){
		for(int i=0; i<race.size(); i++){
			if(race.get(i).equals(entity.getRace()))
				return true;
		}
		for(int i=0; i<ID.size(); i++){
			if(ID.get(i).equals(entity.ID))
				return true;
		}
		return false;
	}
	public void addRace(String type){
		if(!this.race.contains(type))
			this.race.add(type);
	}
	public void addID(Integer ID){
		if(!this.ID.contains(ID))
			this.ID.add(ID);
	}
}