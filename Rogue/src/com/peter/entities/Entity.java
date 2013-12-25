package com.peter.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Entity extends Sprite{
	
	protected String type, name;
	public Integer ID;
	protected int timeout = 0;
	public boolean canDraw = false;

	public Entity(String filename, String type, String name){
		super(new Sprite(new Texture(Gdx.files.internal("img/" + filename))));
		this.type = type;
		this.name = name;
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

