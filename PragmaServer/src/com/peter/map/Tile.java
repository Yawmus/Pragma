package com.peter.map;

public class Tile{
	/*blocked, stairway, door, direction, canSee
	*/
	private String name;
	private boolean[] properties;

	public static final Tile BLANK = new Tile("", new boolean[]{true, false, false, false, false});
	public static final Tile GROUND = new Tile("", new boolean[]{false, false, false, false, true});
	public static final Tile WALL = new Tile("", new boolean[]{true, false, false, false, false});
	public static final Tile DOOR = new Tile("Door", new boolean[]{false, false, true, false, false});
	public static final Tile WATER = new Tile("", new boolean[]{true, false, false, false, true});
	public static final Tile DOWN = new Tile("Downstairs", new boolean[]{false, true, false, false, true});
	public static final Tile UP = new Tile("Upstairs", new boolean[]{false, true, false, true, true});
	public static final Tile ONE = new Tile("Inn", new boolean[]{false, false, true, false, true});
	
	public Tile(String name, boolean[] properties){
		this.name = name;
		this.properties = properties;
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
