package com.peter.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Entity extends Sprite{
	
	protected String type = new String();
	protected String name;
	public Integer ID;
	protected float tileWidth = 32, tileHeight = 32;
	protected int timeout = 0;
	public boolean canDraw = false;

	public Entity(String filename, String type){
		super(new Sprite(new Texture(Gdx.files.internal("img/" + filename))));
		this.type = type;
	}
	
	public void draw(SpriteBatch spriteBatch){
		super.draw(spriteBatch);
		canDraw = false;
	}

	public Integer getID() {
		return ID;
	}


	public void setID(Integer ID) {
		this.ID = ID;
	}
	
	public String getName(){
		return name;
	}
	
	public String getType() {
		return type;
	}

	public void setName(String name){
		this.name = name;
	}
}

