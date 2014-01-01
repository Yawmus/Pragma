package com.peter.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Entity extends Sprite{
	
	protected String name;
	public Integer ID;
	protected int timeout = 0;
	public boolean canDraw = false;
	public char symbol;

	public Entity(String filename, String name){
		super(new Sprite(new Texture(Gdx.files.internal("img/" + filename))));
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
		if(name.contains("$")){
			int count = 0;
			for(int i=0; i<name.length(); i++){
				if(name.charAt(i) == '$')
					count++;
			}
			return name.split("\\$")[count];
		}
		else
			return name;
	}

	public void setName(String name){
		this.name = name;
	}
}