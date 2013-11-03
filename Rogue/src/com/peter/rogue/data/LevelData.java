package com.peter.rogue.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import com.peter.rogue.entities.Entity;
import com.peter.rogue.entities.NPC;
import com.peter.rogue.map.Map;
import com.peter.rogue.map.Tile;

public class LevelData {
    private int citizens, shopkeeps, monsters;
    
	public LevelData(){
		
		citizens = 4;
		shopkeeps = 1;
		monsters = 3;
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
