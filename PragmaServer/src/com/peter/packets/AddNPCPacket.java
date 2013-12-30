package com.peter.packets;

import java.util.ArrayList;

public class AddNPCPacket {
	public Integer ID;
	public ArrayList<ItemPacket> items;
	public int x, y, level;
	public String group, race, type, name;
	public AddNPCPacket(){};
	public AddNPCPacket(int x, int y, int level, Integer ID, String group, String race, String type, String name){
		items = new ArrayList<ItemPacket>();
		this.x = x;
		this.y = y;
		this.level = level;
		this.ID = ID;
		this.group = group;
		this.race = race;
		this.type = type;
		this.name = name;
		
	}
}
