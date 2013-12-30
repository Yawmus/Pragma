package com.peter.entities;

import com.badlogic.gdx.graphics.Color;

public class Monster extends NPC{
	
	public Monster(int level, String race, String type, String name) {
		super("w.png", level, race, type, name);
		if(level > 10)
			setColor(Color.RED);
		else if(level > 5)
			setColor(Color.PINK);
		else
			setColor(new Color(.96f, .94f, .69f, 1));
	}
}