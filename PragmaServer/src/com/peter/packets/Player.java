package com.peter.packets;

import com.esotericsoftware.kryonet.Connection;
import com.peter.entities.Animate;

public class Player extends Animate{

	public Player(){
		super("Player");
	}
	public Connection c;
}