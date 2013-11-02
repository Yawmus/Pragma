package com.peter.rogue.entities;

import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.peter.rogue.map.Map;

public class Entity extends Sprite{
	
	protected String type = new String();
	protected boolean animate;
	protected String name;
	protected String ID;
	protected float tileWidth = 32, tileHeight = 32;
    protected static BitmapFont font;
    protected int rand1, rand2;
	protected boolean pickedUp;
	protected int timeout = 0;
	public static Map map = new Map();
	public boolean canDraw = false;

	public Entity(String filename, String type){
		super(new Sprite(new Texture(Gdx.files.internal("img/" + filename))));
		name = new String("null");
		this.type = type;
		animate = false;
		ID = UUID.randomUUID().toString();
		pickedUp = false;
		font = new BitmapFont();
	}
	
	public void draw(SpriteBatch spriteBatch){
		super.draw(spriteBatch);
		canDraw = false;
	}
	// Should only be called for inanimate objects
	@Override
	public void setPosition(float x, float y){
		setX(x * tileWidth);
		setY(y * tileHeight);
		
		map.put(ID, this);
		map.setMark(ID, getX(), getY());
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
	public boolean isAnimate(){
		return animate;
	}

	public void pickedUp(boolean pickedUp){
		this.pickedUp = pickedUp;
	}
	public boolean isPickedUp(){
		return pickedUp;
	}

}

