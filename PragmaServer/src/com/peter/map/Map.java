package com.peter.map;

import java.util.HashMap;

import com.peter.entities.Entity;
import com.peter.entities.NPC;
import com.peter.packets.ChestPacket;
import com.peter.packets.ItemPacket;
import com.peter.packets.Player;
import com.peter.server.Global;

public class Map{
	
	public Tile[][] tiles;
    public HashMap<Integer, Entity> database;
	public final int HEIGHT = 40, WIDTH = 80;
	public final int ROOM_HEIGHT = 6, ROOM_WIDTH = 10, HALL_LENGTH = 6;
	protected static int floor;
	public HashMap<Integer, ChestPacket> chests;
	public HashMap<Integer, ItemPacket> items;
	public HashMap<Integer, NPC> npcs;
	public HashMap<Integer, Player> players;
	public Marks marks = new Marks();
	
	public Map(){
		database = new HashMap<Integer, Entity>();
		items = new HashMap<Integer, ItemPacket>();
		chests = new HashMap<Integer, ChestPacket>();
		npcs = new HashMap<Integer, NPC>();
		players = new HashMap<Integer, Player>();
		

		tiles = new Tile[WIDTH][HEIGHT];
		baseFloor();
	}/*
	public Entity get(Integer ID){
		if(players.get(ID) != null)
			return players.get(ID);
		else if(npcs.get(ID) != null)
			return npcs.get(ID);
		else if(chests.get(ID) != null)
			return chests.get(ID);
		else if(items.get(ID) != null)
			return items.get(ID);
		return null;
	}*/
	// ------------- Getters -------------
	/*public String getMark(float x, float y){
		if(y < 0 || x < 0 || y/32 >= HEIGHT || x/32 >= WIDTH)
			return "";
		else
			return marker[(int)(x/32)][(int)(y/32)];
	}
	
	public Entity get(Integer ID){
		if(ID != -1)
			return database.get(ID);
		return null;
	}
	public Entity get(float x, float y){
		return database.get(getMark(x, y));
	}
	
	public void put(String ID, Entity entity){
		database.put(ID, entity);
	}*/
	
	// ------------- Setters -------------
	/*public void setMark(String ID, float x, float y){
		marker[(int)(x/32)][(int)(y/32)] = ID;
	}*/
	
	/*public void remove(Integer ID){
		setMark(-1, (int)database.get(ID).getX()/32, (int)database.get(ID).getY()/32);
		database.remove(ID);
	}*/
	public Tile getTile(float x, float y){
		if(y < 0 || x < 0 || y/32 >= HEIGHT || x/32 >= WIDTH)
			return Tile.BLANK;
		return tiles[(int)(x/32)][(int)(y/32)];
	}
	
	
	private void baseFloor(){
		int seaX = WIDTH-9, seaY = 1;
		for(int x=0; x<WIDTH; x++)
			for(int y=0; y<HEIGHT; y++)
				if(y == 0 || y == HEIGHT-1 || x == 0 || x == WIDTH-1)
					tiles[x][y] = Tile.WALL;
				else
					tiles[x][y] = Tile.GROUND;
		
		createRoom(seaX-11, HEIGHT-12, seaX-3, HEIGHT-3, Tile.DOOR);
		createRoom(seaX-11, 4, seaX-3, 12, Tile.DOOR);
		createRoom(12, 9, 20, 14, Tile.DOOR);


		for(int x=WIDTH/2-1; x<=WIDTH/2+1; x++)
			for(int y=HEIGHT/2-1; y<=HEIGHT/2+1; y++)
				tiles[x][y] = Tile.WATER;
		
		for(int x=seaX; x<WIDTH-1; x++)
			for(int y=seaY; y<HEIGHT-1; y++)
				tiles[x][y] = Tile.WATER;
		
		for(int y=0; y<HEIGHT-1; y++)
			tiles[WIDTH-1][y] = Tile.BLANK;
		
		tiles[24][10] = Tile.DOWN;
	}
	
	private void createRoom(int x, int y, int dx, int dy, Tile type){
		for(int i=x; i<=dx; i++)
			for(int j=y; j<=dy; j++)
				if(j == y || j == dy || i == x || i == dx)
					tiles[i][j] = Tile.WALL;
				else
					tiles[i][j] = Tile.GROUND;
		switch(Global.rand(4, 0)){
		case 0:
			tiles[x][(y + dy)/2] = type;
			break;
		case 1:
			tiles[x + (dx - x)][(y + dy)/2] = type;
			break;
		case 2:
			tiles[(x + dx)/2][y] = type;
			break;
		case 3:
			tiles[(x + dx)/2][y + (dy - y)] = type;
			break;
		}
	}
	
	/*private boolean generateFloor(int x, int y){
		generateRoom(x, y);
		return false;
	}
	
	private boolean generateRoom(int x, int y){
		if(x-ROOM_WIDTH/2 < 0 || x+ROOM_WIDTH/2 >= WIDTH || y-ROOM_HEIGHT/2 < 0 || y+ROOM_HEIGHT/2 >= WIDTH){
			return false;
		}
		for(int i=(x-ROOM_WIDTH/2 + 1); i<(x+ROOM_WIDTH/2); i++)
			for(int j=(y-ROOM_HEIGHT/2 + 1); j<(y+ROOM_HEIGHT/2); j++)
				tiles[i][j] = Tile.GROUND;
		for(int i=(x-ROOM_WIDTH/2); i<(x+ROOM_WIDTH/2+1); i++)
			for(int j=(y-ROOM_HEIGHT/2); j<(y+ROOM_HEIGHT/2+1); j++)
				if(i == (x-ROOM_WIDTH/2) || i == (x+ROOM_WIDTH/2)
				   || j == (y-ROOM_HEIGHT/2) || j == (y+ROOM_HEIGHT/2))
				tiles[i][j] = Tile.WALL;
		if(!generateHall(x, y+ROOM_HEIGHT/2, "up"))
			if(!generateHall(x-ROOM_WIDTH/2, y, "left"))
				if(!generateHall(x, y-ROOM_HEIGHT/2, "down"))
					if(!generateHall(x+ROOM_WIDTH/2, y, "right"))
						return false;
					else{
						if(generateRoom(x+ROOM_WIDTH+HALL_LENGTH, y))
							tiles[x+ROOM_WIDTH/2+HALL_LENGTH][y] = Tile.DOOR;
					}
				else{
					if(generateRoom(x, y-ROOM_HEIGHT-HALL_LENGTH))
						tiles[x][y-ROOM_HEIGHT/2-HALL_LENGTH] = Tile.DOOR;
				}
			else{
				if(generateRoom(x-ROOM_WIDTH-HALL_LENGTH, y))
					tiles[x-ROOM_WIDTH/2-HALL_LENGTH][y] = Tile.DOOR;
			}
		else{
			if(generateRoom(x, y+ROOM_HEIGHT+HALL_LENGTH))
				tiles[x][y+ROOM_HEIGHT/2+HALL_LENGTH] = Tile.DOOR;
		}
		return true;
	}
	
	private boolean generateHall(int x, int y, String direction){
		boolean flag = true;
		tiles[x][y] = Tile.DOOR;
		if(direction.equals("left")){
			x--;
			for(int i=x; i>x-HALL_LENGTH; i--)
				if(i<0 || tiles[i][y] != Tile.BLANK)
					flag = false;
			if(x-HALL_LENGTH - ROOM_WIDTH >= 0 && flag){
				for(int i=x; i>x-HALL_LENGTH; i--)
					tiles[i][y] = Tile.GROUND;
			}
			else
				tiles[++x][y] = Tile.WALL;
		}
		else if(direction.equals("right")){
			x++;
			for(int i=x; i<x+HALL_LENGTH; i++)
				if(i>=WIDTH || tiles[i][y] != Tile.BLANK)
					flag = false;
			if(x+HALL_LENGTH + ROOM_WIDTH < WIDTH && flag)
				for(int i=x; i<x+HALL_LENGTH; i++)
					tiles[i][y] = Tile.GROUND;
			else
				tiles[--x][y] = Tile.WALL;
		}
		else if(direction.equals("up")){
			y++;
			for(int j=y; j<y+HALL_LENGTH; j++)
				if(j>=HEIGHT || tiles[x][j] != Tile.BLANK)
					flag = false;
			if(y+HALL_LENGTH + ROOM_HEIGHT < HEIGHT && flag)
				for(int j=y; j<y+HALL_LENGTH; j++)
					tiles[x][j] = Tile.GROUND;
			else
				tiles[x][--y] = Tile.WALL;
		}
		else if(direction.equals("down")){
			y--;
			for(int j=y; j>y-HALL_LENGTH; j--)
				if(j<0 || tiles[x][j] != Tile.BLANK)
					flag = false;
			if(y-HALL_LENGTH - ROOM_HEIGHT >= 0 && flag)
				for(int j=y; j>y-HALL_LENGTH; j--)
					tiles[x][j] = Tile.GROUND;
			else
				tiles[x][++y] = Tile.WALL;
		}
		return flag;
	}*/
	
}