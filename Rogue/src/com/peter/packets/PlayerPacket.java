package com.peter.packets;

public class PlayerPacket{

	public int x, y;
	public int oldX, oldY;
	public Integer ID;
	
	public PlayerPacket(){}
	
	public void sync(PlayerPacket packet){
		x = packet.x;
		y = packet.y;
	}
}