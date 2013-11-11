package com.peter.rogue.entities;

import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.peter.rogue.map.Map;

public class Entity extends Sprite{
	
	protected String type = new String();
	protected String name;
	protected String ID;
	protected float tileWidth = 32, tileHeight = 32;
	protected int timeout = 0;
	public static Map map = new Map();
	public boolean canDraw = false;

	public Entity(String filename, String type){
		super(new Sprite(new Texture(Gdx.files.internal("img/" + filename))));
		name = new String("null");
		this.type = type;
		ID = UUID.randomUUID().toString();
	}
	
	public void draw(SpriteBatch spriteBatch){
		super.draw(spriteBatch);
		canDraw = false;
	}

	public String getID() {
		return ID;
	}
	
	public String getName(){
		return name;
	}
	
	public String getType() {
		return type;
	}

}

