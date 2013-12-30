package com.peter.packets;

public class PlayerPacket{

	public int x, y;
	public int oldX, oldY;
	public int floor;
	public Integer ID;
	
	public PlayerPacket(){}
	
	public void sync(PlayerPacket packet){
		x = packet.x;
		y = packet.y;
	}
}