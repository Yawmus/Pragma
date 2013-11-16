package com.peter.rogue.entities;

public class Stats{
	private int level;
	private int dexterity;
	private int experience;
	private int maxExperience;
	private int strength;
	private int hitpoints;
	private int maxHitpoints;
	private int points;
	
	public Stats(){
		setLevel(1);
		setDexterity(5);
		setExperience(0);
		setStrength(5);
		setHitpoints(10);
		setMaxHitpoints(20);
		setMaxExperience(100);
		points = 0;
	}

	public int getHitpoints() {
		return hitpoints;
	}

	public void mutateHitpoints(int amount){
		this.hitpoints += amount;
	}
	
	public void addExperience(String type){
		if(type == "Worm")
			mutateExperience(5);
		else if(type == "Citizen")
			mutateExperience(1);
		else if(type == "Shopkeep")
			mutateExperience(1);
	}
	
	public void mutateExperience(int experience){
		this.experience += experience;
		if(this.experience >= maxExperience){
			mutateLevel(1);
			maxExperience += maxExperience*(level+1);
		}
	}
	
	public void setHitpoints(int hitpoints) {
		this.hitpoints = hitpoints;
	}

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public int getExperience() {
		return experience;
	}
	public void setMaxExperience(int maxExperience) {
		this.maxExperience = maxExperience;
	}
	
	public int getMaxExperience() {
		return maxExperience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}

	public int getDexterity() {
		return dexterity;
	}

	public void setDexterity(int dexterity) {
		this.dexterity = dexterity;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	public void mutateLevel(int amount){
		this.level += amount;
		mutatePoints(5);
	}

	public int getMaxHitpoints() {
		return maxHitpoints;
	}

	public void setMaxHitpoints(int maxHitpoints) {
		this.maxHitpoints = maxHitpoints;
	}
	
	public int getPoints(){
		return points;
	}
	
	public void mutatePoints(int amount){
		points += amount;
	}
}