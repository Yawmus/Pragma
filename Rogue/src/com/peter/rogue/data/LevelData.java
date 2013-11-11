package com.peter.rogue.data;

public class LevelData {
    private int citizens, shopkeeps, monsters;
    
	public LevelData(){
		
		citizens = 4;
		shopkeeps = 1;
		monsters = 2;
	}
	
	public int getCitizens() {
		return citizens;
	}
	public int getShopkeeps() {
		return shopkeeps;
	}
	public int getMonsters() {
		return monsters;
	}
	public int getNPCTotal() {
		return monsters + citizens + shopkeeps;
	}

	
}
