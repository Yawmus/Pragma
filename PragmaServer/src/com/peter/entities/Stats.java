package com.peter.entities;

public class Stats{
	private int level;
	private int dexterity;
	private int experience;
	private int maxExperience;
	private int strength;
	private int hitpoints;
	private int maxHitpoints;
	private int defense;

	public int getHitpoints() {
		return hitpoints;
	}

	public void mutateHitpoints(int amount){
		this.hitpoints += amount;
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

	public int getMaxHitpoints() {
		return maxHitpoints;
	}

	public void setMaxHitpoints(int maxHitpoints) {
		this.maxHitpoints = maxHitpoints;
	}

	public int getDefense() {
		return defense;
	}

	public void setDefense(int defense) {
		this.defense = defense;
	}
	public void mutateDefense(int amount) {
		this.defense += amount;
	}
}