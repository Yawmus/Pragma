package com.peter.inventory;

import com.peter.server.Global;

public class Item {
	public static final String GOLD = new String("Treasure$Gold");
	public static final String DIAMOND = new String("Treasure$Gem$Diamond");
	public static final String RUBY = new String("Treasure$Gem$Ruby");
	public static final String TOPAZ = new String("Treasure$Gem$Topaz");
	public static final String SPESSARTITE = new String("Treasure$Gem$Spessartite");

	public static final String BREAD = new String("Food$Bread");
	public static final String MEAT = new String("Food$Meat");
	
	public static final String HELMET = new String("Wearable$Head$Helmet");
	public static final String HAT = new String("Wearable$Head$Hat");
	public static final String BREAST_PLATE = new String("Wearable$Body$Breast Plate");
	public static final String SHOES = new String("Wearable$Feet$Shoes");
	public static final String WOODEN_RING = new String("Wearable$Ring$Wooden Ring");
	
	public static String getDrop(int rand){
		if(rand < 4)
			return BREAD;
		else if(rand < 8)
			return MEAT;
		else if(rand < 15)
			return GOLD;
		else if(rand < 20)
			switch(Global.rand(6, 0)){
			case 0:
				return DIAMOND;
			case 1:
			case 2:
				return RUBY;
			case 3:
			case 4:
				return TOPAZ;
			case 5:
				return SPESSARTITE;
			}
		else if(rand < 22)
			return SHOES;
		else if(rand < 24)
			return WOODEN_RING;
		else if(rand < 26)
			return HAT;
		return null;
	}
}
