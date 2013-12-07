package com.peter.map;

import java.util.ArrayList;
import java.util.HashMap;

import com.peter.entities.Entity;
import com.peter.entities.NPC;
import com.peter.entities.Player;
import com.peter.packets.ChestPacket;
import com.peter.packets.ItemPacket;
import com.peter.server.Global;

public class Map{
	
	public Tile[][] tiles;
    public HashMap<Integer, Entity> database;
	public final int HEIGHT = 40, WIDTH = 80;
	public final int BASE_HALL_LENGTH = 6;
	private final int BASE_ROOM_WIDTH = 10, BASE_ROOM_HEIGHT = 6;
	protected static int floor;
	/*public HashMap<Integer, ChestPacket> chests;
	public HashMap<Integer, ItemPacket> items;
	public HashMap<Integer, NPC> npcs;*/
	public HashMap<Integer, Player> players;
	public Marks marks = new Marks();
	
	private ArrayList<Tile[][]> tileSets;
	public ArrayList<HashMap<Integer, ChestPacket>> chestSets;
	public ArrayList<HashMap<Integer, ItemPacket>> itemSets;
	public ArrayList<HashMap<Integer, NPC>> npcSets;
	
	public Map(){
		database = new HashMap<Integer, Entity>();
		players = new HashMap<Integer, Player>();
		

		tiles = new Tile[WIDTH][HEIGHT];
		tileSets = new ArrayList<Tile[][]>();
		chestSets = new ArrayList<HashMap<Integer, ChestPacket>>();
		itemSets = new ArrayList<HashMap<Integer, ItemPacket>>();
		npcSets = new ArrayList<HashMap<Integer, NPC>>();
		
		baseFloor();

		tileSets.add(tiles);
		

		itemSets.add(new HashMap<Integer, ItemPacket>());
		chestSets.add(new HashMap<Integer, ChestPacket>());
		npcSets.add(new HashMap<Integer, NPC>());
	}
	
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
		
		tiles[24][8] = Tile.DOWN;
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
	
	public Tile[][] getTileSet(int floor, int x, int y){
		if(tileSets.size() > floor)
				return tileSets.get(floor);
		
		Tile[][] tempTileSet = new Tile[WIDTH][HEIGHT];
		generateFloor(tempTileSet, x, y);
		tileSets.add(tempTileSet);
		
		return tempTileSet;
	}
	private boolean generateFloor(Tile[][] tileSet, int x, int y){
		for(int i=0; i<WIDTH; i++)
			for(int j=0; j<HEIGHT; j++)
				tileSet[i][j] = Tile.BLANK;
		generateRoom(tileSet, x, y, BASE_ROOM_WIDTH, BASE_ROOM_HEIGHT);
		tileSet[x][y] = Tile.UP;
		return false;
	}
	
	private boolean scanRoom(Tile[][] tileSet, int x, int y, int width, int height){
		for(int i=(x-width/2 + 1); i<(x+width/2); i++)
			for(int j=(y-height/2 + 1); j<(y+height/2); j++)
				if(!tileSet[i][j].equals(Tile.BLANK))
					return false;
		return true;
	}
	
	private boolean generateRoom(Tile[][] tileSet, int x, int y, int width, int height){
		if(x-width/2 < 0 || x+width/2 >= WIDTH || y-height/2 < 0 || y+height/2 >= HEIGHT){
			return false;
		}
		if(!scanRoom(tileSet, x, y, width, height)){
			return false;
		}
		for(int i=(x-width/2 + 1); i<(x+width/2); i++)
			for(int j=(y-height/2 + 1); j<(y+height/2); j++)
				if(tileSet[i][j].equals(Tile.BLANK))
					tileSet[i][j] = Tile.GROUND;
		for(int i=(x-width/2); i<(x+width/2+1); i++)
			for(int j=(y-height/2); j<(y+height/2+1); j++)
				if(i == (x-width/2) || i == (x+width/2)
				   || j == (y-height/2) || j == (y+height/2))
					if(tileSet[i][j].equals(Tile.BLANK))
						tileSet[i][j] = Tile.WALL;
		
		int newWidth = BASE_ROOM_WIDTH + Global.rand(3, -3);
		int newHeight = BASE_ROOM_HEIGHT + Global.rand(4, -3);
		int newLength = BASE_HALL_LENGTH + Global.rand(4, -3);
		System.out.println(newWidth + "  " + newHeight);
		
		switch(Global.rand(4, 0)){
		case 0: // up
			if(!generateHall(tileSet, x, y+height/2, "up", newWidth, newHeight, newLength)){
				if(!generateHall(tileSet, x, y-height/2, "down", newWidth, newHeight, newLength)){
					if(!generateHall(tileSet, x+width/2, y, "right", newWidth, newHeight, newLength)){
						if(!generateHall(tileSet, x-width/2, y, "left", newWidth, newHeight, newLength)){
							System.out.println("Here");
							tileSet[x][y] = Tile.DOWN;
						}
						else{
							if(generateRoom(tileSet, x-width/2-newWidth/2-newLength, y, newWidth, newHeight))
								tileSet[x-width/2-newLength][y] = Tile.DOOR;
						}
					}
					else{
						if(generateRoom(tileSet, x+width/2+newWidth/2+newLength, y, newWidth, newHeight))
							tileSet[x+width/2+newLength][y] = Tile.DOOR;
					}
				}
				else{
					if(generateRoom(tileSet, x, y-height/2-newHeight/2-newLength, newWidth, newHeight))
						tileSet[x][y-height/2-newLength] = Tile.DOOR;
				}
			}
			else{
				if(generateRoom(tileSet, x, y+height/2+newHeight/2+newLength, newWidth, newHeight))
					tileSet[x][y+height/2+newLength] = Tile.DOOR;
			}
			
			break;
		case 1: // down
			if(!generateHall(tileSet, x, y-height/2, "down", newWidth, newHeight, newLength)){
				if(!generateHall(tileSet, x, y+height/2, "up", newWidth, newHeight, newLength)){
					if(!generateHall(tileSet, x+width/2, y, "right", newWidth, newHeight, newLength)){
						if(!generateHall(tileSet, x-width/2, y, "left", newWidth, newHeight, newLength)){
							System.out.println("Here");
							tileSet[x][y] = Tile.DOWN;
						}
						else{
							if(generateRoom(tileSet, x-width/2-newWidth/2-newLength, y, newWidth, newHeight))
								tileSet[x-width/2-newLength][y] = Tile.DOOR;
						}
					}
					else{
						if(generateRoom(tileSet, x+width/2+newWidth/2+newLength, y, newWidth, newHeight))
							tileSet[x+width/2+newLength][y] = Tile.DOOR;
					}
				}
				else{
					if(generateRoom(tileSet, x, y+height/2+newHeight/2+newLength, newWidth, newHeight))
						tileSet[x][y+height/2+newLength] = Tile.DOOR;
				}
			}
			else{
				if(generateRoom(tileSet, x, y-height/2-newHeight/2-newLength, newWidth, newHeight))
					tileSet[x][y-height/2-newLength] = Tile.DOOR;
			}
			break;
		case 2: // right
			if(!generateHall(tileSet, x+width/2, y, "right", newWidth, newHeight, newLength)){
				if(!generateHall(tileSet, x-width/2, y, "left", newWidth, newHeight, newLength)){
					if(!generateHall(tileSet, x, y-height/2, "down", newWidth, newHeight, newLength)){
						if(!generateHall(tileSet, x, y+height/2, "up", newWidth, newHeight, newLength)){
							System.out.println("Here");
							tileSet[x][y] = Tile.DOWN;
						}
						else{
							if(generateRoom(tileSet, x, y+height/2+newHeight/2+newLength, newWidth, newHeight))
								tileSet[x][y+height/2+newLength] = Tile.DOOR;
						}
					}
					else{
						if(generateRoom(tileSet, x, y-height/2-newHeight/2-newLength, newWidth, newHeight))
							tileSet[x][y-height/2-newLength] = Tile.DOOR;
					}
				}
				else{
					if(generateRoom(tileSet, x-width/2-newWidth/2-newLength, y, newWidth, newHeight))
						tileSet[x-width/2-newLength][y] = Tile.DOOR;
				}
			}
			else{
				if(generateRoom(tileSet, x+width/2+newWidth/2+newLength, y, newWidth, newHeight))
					tileSet[x+width/2+newLength][y] = Tile.DOOR;
			}
			break;

		case 3: // left
			if(!generateHall(tileSet, x-width/2, y, "left", newWidth, newHeight, newLength)){
				if(!generateHall(tileSet, x+width/2, y, "right", newWidth, newHeight, newLength)){
					if(!generateHall(tileSet, x, y-height/2, "down", newWidth, newHeight, newLength)){
						if(!generateHall(tileSet, x, y+height/2, "up", newWidth, newHeight, newLength)){
							System.out.println("Here");
							tileSet[x][y] = Tile.DOWN;
						}
						else{
							if(generateRoom(tileSet, x, y+height/2+newHeight/2+newLength, newWidth, newHeight))
								tileSet[x][y+height/2+newLength] = Tile.DOOR;
						}
					}
					else{
						if(generateRoom(tileSet, x, y-height/2-newHeight/2-newLength, newWidth, newHeight))
							tileSet[x][y-height/2-newLength] = Tile.DOOR;
					}
				}
				else{
					if(generateRoom(tileSet, x+width/2+newWidth/2+newLength, y, newWidth, newHeight))
						tileSet[x+width/2+newLength][y] = Tile.DOOR;
				}
			}
			else{
				if(generateRoom(tileSet, x-width/2-newWidth/2-newLength, y, newWidth, newHeight))
					tileSet[x-width/2-newLength][y] = Tile.DOOR;
			}
			break;
		}
		return true;
	}
	
	private boolean generateHall(Tile[][] tileSet, int x, int y, String direction, int newWidth, int newHeight, int length){
		if(direction.equals("left")){
			tileSet[x][y] = Tile.DOOR;
			x--;
			for(int i=x; i>x-length; i--)
				if(i<0 || tileSet[i][y] != Tile.BLANK){
					tileSet[++x][y] = Tile.WALL;
					return false;
				}
			if(x-length - newWidth >= 0){
				for(int i=x; i>x-length; i--)
					tileSet[i][y] = Tile.GROUND;
			}
			else{
				tileSet[++x][y] = Tile.WALL;
				return false;
			}
		}
		else if(direction.equals("right")){
			tileSet[x][y] = Tile.DOOR;
			x++;
			for(int i=x; i<x+length; i++)
				if(i>=WIDTH || tileSet[i][y] != Tile.BLANK){
					tileSet[--x][y] = Tile.WALL;
					return false;
				}
			if(x+length + newWidth < WIDTH)
				for(int i=x; i<x+length; i++)
					tileSet[i][y] = Tile.GROUND;
			else{
				tileSet[--x][y] = Tile.WALL;
				return false;
			}
		}
		else if(direction.equals("up")){
			tileSet[x][y] = Tile.DOOR;
			y++;
			for(int j=y; j<y+length; j++)
				if(j>=HEIGHT || tileSet[x][j] != Tile.BLANK){
					tileSet[x][--y] = Tile.WALL;
					return false;
				}
			if(y+length + newHeight < HEIGHT)
				for(int j=y; j<y+length; j++)
					tileSet[x][j] = Tile.GROUND;
			else{
				tileSet[x][--y] = Tile.WALL;
				return false;
			}
		}
		else if(direction.equals("down")){
			tileSet[x][y] = Tile.DOOR;
			y--;
			for(int j=y; j>y-length; j--)
				if(j<0 || tileSet[x][j] != Tile.BLANK){
					tileSet[x][++y] = Tile.WALL;
					return false;
				}
			if(y-length - newHeight >= 0)
				for(int j=y; j>y-length; j--)
					tileSet[x][j] = Tile.GROUND;
			else{
				tileSet[x][++y] = Tile.WALL;
				return false;
			}
		}
		return true;
	}
	
}