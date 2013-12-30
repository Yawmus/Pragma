package com.peter.entities;

import java.util.ArrayList;

import com.peter.packets.ItemPacket;

public class Shopkeep extends NPC{

    public static final String Bartender = "Bartender";
    public static final String Blacksmith = "Blacksmith";
    public static final String Fletcher = "Fletcher";
    public static final String Armorer = "Armorer";

	public ArrayList<ItemPacket> items;
    
    public Shopkeep(int floor, String type){
        super(floor, "Shopkeep", "Human", type);
        this.items = new ArrayList<ItemPacket>();
        switch(type){
        case "Bartender":
        	items.add(new ItemPacket("Bread"));
        	items.add(new ItemPacket("Meat"));
        	break;
        case "Blacksmith":
        	break;
        case "Fletcher":
        	break;
        case "Armorer":
        	items.add(new ItemPacket("Breast Plate"));
        	items.add(new ItemPacket("Helmet"));
        	break;
        }
        stats.setExperience(5);
        stats.setMaxHitpoints(20);
        stats.setHitpoints(20);
        
        //list.addType("Worm");
    }
}