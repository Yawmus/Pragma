package com.peter.packets;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.peter.entities.Animate;


public class MPPlayer extends Animate{
	
	public MPPlayer(String filename, String type, String name) {
		super(filename, type);
		this.name = name;
	}
	public void draw(SpriteBatch spriteBatch){
		super.draw(spriteBatch);
	}
}
