package com.peter.rogue.map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;

public class Map implements MapRenderer{
	protected static Tile[][] tiles;
	public static final int HEIGHT = 40, WIDTH = 40;
	public static final int ROOM_HEIGHT = 6, ROOM_WIDTH = 10, HALL_LENGTH = 6;
	protected SpriteBatch spriteBatch;
	protected Rectangle viewBounds;
	protected TextureRegion region;
	protected static int floor;
	
	public Map(){
		spriteBatch = new SpriteBatch();
		viewBounds = new Rectangle();
		region = new TextureRegion();
		tiles = new Tile[WIDTH][HEIGHT];
		floor = 0;
		baseFloor();
	}
	
	public static void load(int direction, float x, float y){
		floor -= direction;
		clear();
		if(floor == -1){
			generateFloor((int)x/32, (int)y/32);
			tiles[(int)x/32][(int)y/32] = Tile.UP;
		}
		else if(floor == 0){
			baseFloor();
		}
		else if(floor == 1){
			
		}
	}
	
	private static void baseFloor(){
		for(int x=0; x<WIDTH; x++)
			for(int y=0; y<HEIGHT; y++)
				if(y == 0 || y == HEIGHT-1 || x == 0 || x == WIDTH-1)
					tiles[x][y] = Tile.WALL;
				else
					tiles[x][y] = Tile.GROUND;
		tiles[10][13] = Tile.DOWN;
	}
	
	private static boolean generateFloor(int x, int y){
		System.out.println(x + ", " + y);
		generateRoom(x, y);
		return false;
	}
	
	private static boolean generateRoom(int x, int y){
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
			System.out.println("Here");
			if(generateRoom(x, y+ROOM_HEIGHT+HALL_LENGTH))
				tiles[x][y+ROOM_HEIGHT/2+HALL_LENGTH] = Tile.DOOR;
		}
		return true;
			
	}
	
	private static boolean generateHall(int x, int y, String direction){
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
	}
	
	private static void clear(){
		for(int i=0; i<WIDTH; i++)
			for(int j=0; j<HEIGHT; j++)
				tiles[i][j] = Tile.BLANK;
	}
	
	public void draw(){
		for(int x=0; x<WIDTH; x++)
			for(int y=0; y<HEIGHT; y++){
				spriteBatch.draw(tiles[x][y].getTexture(), 32 * x, 32 * y);
			}
	}
	
	public Tile getTile(float x, float y){
		if(y < 0 || x < 0)
			return null;
		return tiles[(int)(x/32)][(int)(y/32)];
	}

	public Rectangle getViewBounds () {
		return viewBounds;
	}
	
	public static int getFloor(){
		return floor;
	}
	
	@Override
	public void setView (OrthographicCamera camera) {
		spriteBatch.setProjectionMatrix(camera.combined);
		float width = camera.viewportWidth * camera.zoom;
		float height = camera.viewportHeight * camera.zoom;
		viewBounds.set(camera.position.x - width / 2, camera.position.y - height / 2, width, height);
	}
	
	public SpriteBatch getSpriteBatch () {
		return spriteBatch;
	}

	@Override
	public void setView (Matrix4 projection, float x, float y, float width, float height) {
		spriteBatch.setProjectionMatrix(projection);
		viewBounds.set(x, y, width, height);
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}
	

	/*beginRender();
	for (MapLayer layer : map.getLayers()) {
		if (layer.isVisible()) {
			if (layer instanceof TiledMapTileLayer) {
				renderTileLayer((TiledMapTileLayer)layer);
			} else {
				for (MapObject object : layer.getObjects()) {
					renderObject(object);
				}
			}
		}
	}
	*/

	@Override
	public void render(int[] layers) {
		// TODO Auto-generated method stub
		
	}
}
