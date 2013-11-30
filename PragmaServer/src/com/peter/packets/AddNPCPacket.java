package com.peter.packets;

import java.util.ArrayList;

public class AddNPCPacket {
	public Integer ID;
	public ArrayList<ItemPacket> items;
	public int x, y;
	public String name, type;
	public AddNPCPacket(){};
	public AddNPCPacket(int x, int y, Integer ID, String type, String name){
		items = new ArrayList<ItemPacket>();
		this.x = x;
		this.y = y;
		this.ID = ID;
		this.type = type;
		this.name = name;
	}
}
