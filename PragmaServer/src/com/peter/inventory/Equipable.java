package com.peter.inventory;

public class Equipable extends Item{

	public static Equipable BRONZE_SWORD = new Equipable("Bronze Sword", 3, 10, "G.png", "Sword", 2);
	public static Equipable IRON_SWORD = new Equipable("Iron Sword", 3, 10, "G.png", "Sword", 5);
	
	private int damage;
	
	public Equipable(String name, int weight, int value, String filename, String type, int damage) {
		super(name, weight, value, filename);
		this.damage = damage;
	}
	public int getDamage(){
		return damage;
	}
	public void setDamage(int damage){
		this.damage = damage;
	}
}
