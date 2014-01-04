package com.peter.entities;

import java.util.ArrayList;

import com.peter.inventory.Item;
import com.peter.packets.ItemPacket;
import com.peter.server.Global;

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
        	items.add(new ItemPacket(Item.BREAD, ++Global.count, floor));
        	items.add(new ItemPacket(Item.MEAT, ++Global.count, floor));
        	break;
        case "Blacksmith":
        	break;
        case "Fletcher":
        	break;
        case "Armorer":
        	items.add(new ItemPacket(Item.BREAST_PLATE, ++Global.count, floor));
        	items.add(new ItemPacket(Item.HELMET, ++Global.count, floor));
        	break;
        }
        stats.setExperience(5);
        stats.setMaxHitpoints(20);
        stats.setHitpoints(20);
        
        //list.addType("Worm");
    }
}