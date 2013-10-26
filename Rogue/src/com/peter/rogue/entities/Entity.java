package com.peter.rogue.entities;

import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.peter.rogue.Global;

public class Entity extends Sprite{

	protected TiledMapTileLayer collisionLayer;
	
	private static MapEntry[][] map;
	protected static MapEntry nullEntry;
	protected MapEntry entry;
	private String type = new String();
	protected boolean animate;
	protected String name;
	protected float tileWidth, tileHeight;
    protected static BitmapFont font = new BitmapFont();
    protected int rand1, rand2;
	protected boolean pickedUp = false;
    
    static{
		map = new MapEntry[Global.HEIGHT][Global.WIDTH];
		for(int i=0; i<Global.HEIGHT; i++)
    		for(int j=0; j<Global.WIDTH; j++){
    			nullEntry = new MapEntry("null", null);
    			map[i][j] = nullEntry;
    		}
    }


	public Entity(String filename, String type){
		super(new Sprite(new Texture(Gdx.files.internal("img/" + filename))));
		collisionLayer = (TiledMapTileLayer) Global.map.getLayers().get(0);
		tileWidth = this.collisionLayer.getTileWidth();
		tileHeight = this.collisionLayer.getTileHeight();
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
	
	public TiledMapTileLayer getCollisionLayer() {
		return collisionLayer;
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

	public String getMapID(float y, float x) {
		if(y < 0 || x < 0)
			return "null";
		return Entity.map[(int)(y / 32f)][(int)(x / 32f)].ID;
	}
	
	public Entity getMapObject(float y, float x) {
		return Entity.map[(int) (y / 32f)][(int) (x / 32f)].entity;
	}

	public void setMap(int y, int x, MapEntry entry) {
		Entity.map[y / 32][x / 32] = entry;
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