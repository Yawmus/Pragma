package com.peter.map;

public class Tile{
	/*blocked, stairway, door, direction, canSee
	*/
	private String name;
	private boolean[] properties;

	public static final Tile BLANK = new Tile("Blank", new boolean[]{true, false, false, false, false});
	public static final Tile GROUND = new Tile("Ground", new boolean[]{false, false, false, false, true});
	public static final Tile WALL = new Tile("Wall", new boolean[]{true, false, false, false, false});
	public static final Tile DOOR = new Tile("Door", new boolean[]{false, false, true, false, false});
	public static final Tile WATER = new Tile("Water", new boolean[]{true, false, false, false, true});
	public static final Tile DOWN = new Tile("Downstairs", new boolean[]{false, true, false, false, true});
	public static final Tile UP = new Tile("Upstairs", new boolean[]{false, true, false, true, true});
	public static final Tile GRASS = new Tile("Grass", new boolean[]{false, false, false, false, true});
	
	public Tile(String name, boolean[] properties){
		this.name = name;
		this.properties = properties;
	}
	
	public Tile(Tile tile){
		this.properties = tile.properties;
		this.name = tile.name;
	}

	public String getName(){
		return name;
	}
	
	public boolean isBlocked(){
		return properties[0];
	}
	public boolean hasStairs() {
		return properties[1];
	}
	public boolean isDoor(){
		return properties[2];
	}
	public boolean direction() {
		return properties[3];
	}

	public boolean canSee() {
		return properties[4];
	}

}
