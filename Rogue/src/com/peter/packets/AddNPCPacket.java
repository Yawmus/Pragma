package com.peter.packets;

public class AddNPCPacket {
	public Integer ID;
	public int x, y;
	public String name, type;
	public AddNPCPacket(){};
	public AddNPCPacket(int x, int y, Integer ID, String type, String name){
		this.x = x;
		this.y = y;
		this.ID = ID;
		this.type = type;
		this.name = name;
	}
}
