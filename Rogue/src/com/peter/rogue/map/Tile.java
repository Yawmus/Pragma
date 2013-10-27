package com.peter.rogue.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Tile{
	/*blocked, stairway, direction
	*/
	private Texture texture;
	private boolean[] properties;
	
	public static final Tile GROUND = new Tile("tile.png", new boolean[]{false, false, false});
	public static final Tile WALL = new Tile("pound.png", new boolean[]{true, false, false});
	public static final Tile DOWN = new Tile("greater.png", new boolean[]{false, true, false});
	public static final Tile UP = new Tile("less.png", new boolean[]{false, true, true});
	
	public Tile(String filename, boolean[] properties){
		texture = new Texture(Gdx.files.internal("maps/" + filename));
		this.properties = properties;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	public boolean isBlocked(){
		return properties[0];
	}

	public boolean hasStairs() {
		return properties[1];
	}
	public boolean direction() {
		return properties[2];
	}
}
