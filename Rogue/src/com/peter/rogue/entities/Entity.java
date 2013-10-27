package com.peter.rogue.entities;

import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.peter.rogue.map.Map;

public class Entity extends Sprite{
	
	private static MapEntry[][] map;
	protected static MapEntry nullEntry;
	protected MapEntry entry;
	private String type = new String();
	protected boolean animate;
	protected String name;
	protected float tileWidth = 32, tileHeight = 32;
    protected static BitmapFont font = new BitmapFont();
    protected int rand1, rand2;
	protected boolean pickedUp = false;
    
    static{
		map = new MapEntry[Map.WIDTH][Map.HEIGHT];
		for(int i=0; i<Map.WIDTH; i++)
    		for(int j=0; j<Map.HEIGHT; j++){
    			nullEntry = new MapEntry("null", null);
    			map[i][j] = nullEntry;
    		}
    }

	public Entity(String filename, String type){
		super(new Sprite(new Texture(Gdx.files.internal("img/" + filename))));
		this.type = type;
		name = new String("null");
		animate = false;
		entry = new MapEntry(UUID.randomUUID().toString(), this);
	}

	@Override
	public void setPosition(float x, float y){
		setX(x * tileWidth);
		setY(y * tileHeight);
	}

	public String getID() {
		return this.entry.ID;
	}
	
	public void setID(String ID) {
		this.entry.ID = ID;
	}
	
	public String getName(){
		return name;
	}
	
	public MapEntry getEntry() {
		return entry;
	}

	public String getMapID(float x, float y) {
		if(y < 0 || x < 0 || y/32 >= Map.HEIGHT || x/32 >= Map.WIDTH)
			return "null";
		return Entity.map[(int)(x/32)][(int)(y/32)].ID;
	}
	
	public Entity getMapObject(float x, float y) {
		return Entity.map[(int)(x/32)][(int)(y/32)].entity;
	}

	public void setMap(float x, float y, MapEntry entry) {
		Entity.map[(int)(x/32)][(int)(y/32)] = entry;
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

class MapEntry{
	protected String ID;
	protected Entity entity;
	public MapEntry(String ID, Entity entity){
		this.ID = ID;
		this.entity = entity;
	}
}