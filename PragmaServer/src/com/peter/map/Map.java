package com.peter.map;

import java.util.ArrayList;
import java.util.HashMap;

import com.peter.entities.Citizen;
import com.peter.entities.Entity;
import com.peter.entities.NPC;
import com.peter.entities.Player;
import com.peter.packets.ChestPacket;
import com.peter.packets.ItemPacket;
import com.peter.server.Global;

public class Map{
	
    public HashMap<Short, Entity> database;
    public static final int HEIGHT = 40;
	public static final int WIDTH = 80;
	protected static int floor;
	/*public HashMap<Integer, ChestPacket> chests;
	public HashMap<Integer, ItemPacket> items;
	public HashMap<Integer, NPC> npcs;*/
	public HashMap<Integer, Player> players;
	public static ArrayList<Marks> markSets;
	
	public static ArrayList<Tile[][]> tileSets;
	public static ArrayList<HashMap<Integer, ChestPacket>> chestSets;
	public static ArrayList<HashMap<Integer, ItemPacket>> itemSets;
	public static ArrayList<HashMap<Integer, NPC>> npcSets;
	
	public Map(){
		database = new HashMap<Short, Entity>();
		players = new HashMap<Integer, Player>();

		tileSets = new ArrayList<Tile[][]>();
		chestSets = new ArrayList<HashMap<Integer, ChestPacket>>();
		itemSets = new ArrayList<HashMap<Integer, ItemPacket>>();
		npcSets = new ArrayList<HashMap<Integer, NPC>>();
		markSets = new ArrayList<Marks>();


		tileSets.add(new Tile[WIDTH][HEIGHT]);
		baseFloor();

		itemSets.add(new HashMap<Integer, ItemPacket>());
		chestSets.add(new HashMap<Integer, ChestPacket>());
		npcSets.add(new HashMap<Integer, NPC>());
		markSets.add(new Marks());
	}
	
	public Tile getTile(int floor, float x, float y){
		if(y < 0 || x < 0 || y/32 >= HEIGHT || x/32 >= WIDTH)
			return Tile.BLANK;
		return tileSets.get(floor)[(int)(x/32)][(int)(y/32)];
	}
	
	
	private void baseFloor(){
		int seaX = WIDTH-9, seaY = 1;
		for(int x=0; x<WIDTH; x++)
			for(int y=0; y<HEIGHT; y++)
				if(y == 0 || y == HEIGHT-1 || x == 0 || x == WIDTH-1)
					tileSets.get(0)[x][y] = Tile.WALL;
				else
					tileSets.get(0)[x][y] = Tile.GROUND;
		
		createRoom(seaX-11, HEIGHT-12, seaX-3, HEIGHT-3, Tile.DOOR);
		createRoom(seaX-11, 4, seaX-3, 12, Tile.DOOR);
		createRoom(12, 9, 20, 14, Tile.DOOR);


		for(int x=WIDTH/2-1; x<=WIDTH/2+1; x++)
			for(int y=HEIGHT/2-1; y<=HEIGHT/2+1; y++)
				tileSets.get(0)[x][y] = Tile.WATER;
		
		for(int x=seaX; x<WIDTH-1; x++)
			for(int y=seaY; y<HEIGHT-1; y++)
				tileSets.get(0)[x][y] = Tile.WATER;
		
		for(int y=0; y<HEIGHT-1; y++)
			tileSets.get(0)[WIDTH-1][y] = Tile.BLANK;
		
		tileSets.get(0)[24][8] = Tile.DOWN;
	}
	
	private void createRoom(int x, int y, int dx, int dy, Tile type){
		for(int i=x; i<=dx; i++)
			for(int j=y; j<=dy; j++)
				if(j == y || j == dy || i == x || i == dx)
					tileSets.get(0)[i][j] = Tile.WALL;
				else
					tileSets.get(0)[i][j] = Tile.GROUND;
		switch(Global.rand(4, 0)){
		case 0:
			tileSets.get(0)[x][(y + dy)/2] = type;
			break;
		case 1:
			tileSets.get(0)[x + (dx - x)][(y + dy)/2] = type;
			break;
		case 2:
			tileSets.get(0)[(x + dx)/2][y] = type;
			break;
		case 3:
			tileSets.get(0)[(x + dx)/2][y + (dy - y)] = type;
			break;
		}
	}
	
	public Tile[][] generateFloor(int floor, int x, int y){
		if(tileSets.size() > floor)
			return tileSets.get(floor);
		Tile[][] tileSet = new Tile[WIDTH][HEIGHT];
		Map.npcSets.add(new HashMap<Integer, NPC>());
		Map.chestSets.add(new HashMap<Integer, ChestPacket>());
		Map.itemSets.add(new HashMap<Integer, ItemPacket>());
		Map.markSets.add(new Marks());
		
		for(int i=0; i<WIDTH; i++)
			for(int j=0; j<HEIGHT; j++)
				tileSet[i][j] = Tile.BLANK;
		Room room = new Room(x, y, 10, 6);
		generateRoom(tileSet, floor, room);
		tileSet[x][y] = Tile.UP;
		tileSets.add(tileSet);
		return tileSet;
	}
	
	private boolean scanRoom(Tile[][] tileSet, Room room){
		for(int i=(room.x-room.width/2 + 1); i<(room.x+room.width/2); i++)
			for(int j=(room.y-room.height/2 + 1); j<(room.y+room.height/2); j++)
				if(!tileSet[i][j].equals(Tile.BLANK))
					return false;
		return true;
	}
	
	private boolean generateRoom(Tile[][] tileSet, int floor, Room room){
		if(room.x-room.width/2 < 0 || room.x+room.width/2 >= WIDTH || room.y-room.height/2 < 0 || room.y+room.height/2 >= HEIGHT)
			return false;
		if(!scanRoom(tileSet, room))
			return false;
		Room room2 = new Room();
		
		room.write(tileSet, floor);
		int times = 0;
		while(times < 4){
			Hall hall = new Hall();
			switch(Global.rand(4, 0)){
			case 0: // up
				hall.x = room.x + Global.rand(room.width-2, -(room.width/2-1));
				hall.y = room.y + room.height/2;
				hall.dx = room.x + Global.rand(room.width-1, -(room.width/2-1));
				hall.dy = room.y + room.height/2 + Global.rand(5, 0);
				room2.x = hall.dx + Global.rand(3, -1);
				room2.y = hall.dy + room2.height/2;
				if(generateRoom(tileSet, floor, room2))
					hall.write(tileSet, "up");
				break;
			case 1: // down
				hall.x = room.x + Global.rand(room.width-2, -(room.width/2-1));
				hall.y = room.y - room.height/2;
				hall.dx = room.x + Global.rand(room.width-1, -(room.width/2-1));
				hall.dy = room.y - room.height/2 + Global.rand(5, 0);
				room2.x = hall.dx + Global.rand(3, -1);
				room2.y = hall.dy - room2.height/2;
				if(generateRoom(tileSet, floor, room2))
					hall.write(tileSet, "down");
				break;
			case 2: // right
				hall.x = room.x + room.width/2;
				hall.y = room.y + Global.rand(room.height-2, -(room.height/2-1));
				hall.dx = room.x + room.width/2 + Global.rand(5, 0);
				hall.dy = room.y + Global.rand(room.height-2, -(room.height/2-1));
				room2.x = hall.dx + room2.width/2;
				room2.y = hall.dy + Global.rand(3, -1);
				if(generateRoom(tileSet, floor, room2))
					hall.write(tileSet, "right");
				break;
			case 3: // left
				hall.x = room.x - room.width/2;
				hall.y = room.y + Global.rand(room.height-2, -(room.height/2-1));
				hall.dx = room.x - room.width/2 + Global.rand(5, 0);
				hall.dy = room.y + Global.rand(room.height-2, -(room.height/2-1));
				room2.x = hall.dx - room2.width/2;
				room2.y = hall.dy + Global.rand(3, -1);
				if(generateRoom(tileSet, floor, room2))
					hall.write(tileSet, "left");
				break;
			}
			times++;
		}
		return true;
	}
}


class Room{
	public int x, y, width, height;
	public static final int BASE_ROOM_WIDTH = 10, BASE_ROOM_HEIGHT = 6;
	
	public Room(){
		this.width = BASE_ROOM_WIDTH + Global.rand(3, -3);
		this.height = BASE_ROOM_HEIGHT + Global.rand(5, -2);
	}
	
	public Room(int x, int y){
		this.x = x;
		this.y = y;
		this.width = BASE_ROOM_WIDTH + Global.rand(3, -3);
		this.height = BASE_ROOM_HEIGHT + Global.rand(4, -2);
	}
	public Room(int x, int y, int width, int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void write(Tile[][] tileSet, int floor){
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
		if(Global.rand(3, 0) == 0){
			NPC temp = new Citizen();
			++Global.count;
			Map.npcSets.get(floor).put(Global.count, temp);
			temp.ID = Global.count;
			temp.floor = floor;
			System.out.println(temp.ID);
			temp.setPosition(x, y);
			Map.markSets.get(floor).put(temp.ID, temp.getX(), temp.getY());
		}
	}
}

class Hall{
	public int x, y, dx, dy;
	public Hall(){
	}
	public void write(Tile[][] tileSet, String direction){
		int tempX = x, tempY = y;
		while(tempX != dx || tempY != dy){
			if(tempX == dx)
				if(dy - tempY > 0)
					tileSet[tempX][tempY++] = Tile.GROUND;
				else
					tileSet[tempX][tempY--] = Tile.GROUND;
			else if(tempY == dy)
				if(dx - tempX > 0)
					tileSet[tempX++][tempY] = Tile.GROUND;
				else
					tileSet[tempX--][tempY] = Tile.GROUND;
			else if(Math.abs(dx - tempX) < Math.abs(dy - tempY))
				if(dy - tempY > 0)
					tileSet[tempX][tempY++] = Tile.GROUND;
				else
					tileSet[tempX][tempY--] = Tile.GROUND;
			else
				if(dx - tempX > 0)
					tileSet[tempX++][tempY] = Tile.GROUND;
				else
					tileSet[tempX--][tempY] = Tile.GROUND;
		}
		tileSet[dx][dy] = Tile.GROUND;
		if(Global.rand(3, 0) == 0)
			if(direction.equals("up") || direction.equals("down")){
				if(tileSet[dx-1][dy] != Tile.GROUND && tileSet[dx+1][dy] != Tile.GROUND)
					tileSet[dx][dy] = Tile.DOOR;
			}
			else if(direction.equals("right") || direction.equals("left")){
				if(tileSet[dx][dy-1] != Tile.GROUND && tileSet[dx][dy+1] != Tile.GROUND)
					tileSet[dx][dy] = Tile.DOOR;
			}
	}
}