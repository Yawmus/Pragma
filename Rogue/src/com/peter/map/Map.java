package com.peter.map;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.peter.entities.Animate;
import com.peter.entities.Entity;
import com.peter.entities.MPPlayer;
import com.peter.entities.Monster;
import com.peter.entities.NPC;
import com.peter.inventory.Chest;
import com.peter.inventory.Item;
import com.peter.rogue.Global;
import com.peter.rogue.network.PacketToObject;

public class Map implements MapRenderer{
	public Tile[][] tiles;
	public byte[][] init;
	public final int HEIGHT = 40, WIDTH = 80;
	public final int ROOM_HEIGHT = 6, ROOM_WIDTH = 10, HALL_LENGTH = 6;
	protected SpriteBatch spriteBatch;
	protected Rectangle viewBounds;
	protected TextureRegion region;
	protected int floor;
	public Marks marks;
	public ArrayList<String[][]> visibleSets;
	public boolean initialize = true;

	public ArrayList<HashMap<Integer, MPPlayer>> players;
	public HashMap<Integer, NPC> npcs;
	public HashMap<Integer, Item> items;
	public HashMap<Integer, Chest> chests;
	public HashMap<Integer, Entity> database;
	
	public Map(){
		spriteBatch = new SpriteBatch();
		viewBounds = new Rectangle();
		region = new TextureRegion();
		tiles = new Tile[WIDTH][HEIGHT];
		init = new byte[WIDTH][HEIGHT];
		//visible = new String[WIDTH][HEIGHT];
		players = new ArrayList<HashMap<Integer, MPPlayer>>();
		npcs = new HashMap<Integer, NPC>();
		items = new HashMap<Integer, Item>();
		chests = new HashMap<Integer, Chest>();
		database = new HashMap<Integer, Entity>();
		
		visibleSets = new ArrayList<String[][]>();
		visibleSets.add(new String[WIDTH][HEIGHT]);
		
		marks = new Marks();
		players.add(new HashMap<Integer, MPPlayer>());
		
		for(int x=0; x<WIDTH; x++)
			for(int y=0; y<HEIGHT; y++)
				visibleSets.get(0)[x][y] = "notVisited";
		
		floor = 0;
	}
	public Entity get(Integer ID){
		if(database.containsKey(ID))
			return database.get(ID);
		return null;
	}

	public void draw(){
		if(initialize){
			for(int x=0; x<WIDTH; x++)
				for(int y=0; y<HEIGHT; y++)
					tiles[x][y] = PacketToObject.tileConverter(init[x][y]);
			initialize = false;
		}
		for(int x=(int) (Global.camera.position.x/32 -  Gdx.graphics.getWidth()/64) > 0 ? (int) (Global.camera.position.x/32 - Gdx.graphics.getWidth()/64) : 0;
		        x<WIDTH && x<(int) (Global.camera.position.x/32 + Gdx.graphics.getWidth()/64) +1; x++)
			for(int y=(int) (Global.camera.position.y/32 -  Gdx.graphics.getHeight()/64) + 2 > 0 ? (int) (Global.camera.position.y/32 - Gdx.graphics.getHeight()/64) + 2 : 0;
			        y<HEIGHT && y<(int) (Global.camera.position.y/32 + Gdx.graphics.getHeight()/64) + 1; y++){
				if(visibleSets.get(floor)[x][y].equals("visited")){
					spriteBatch.draw(tiles[x][y].getTexture(), 32 * x, 32 * y);
					visibleSets.get(floor)[x][y] = "hasVisited";
				}
				else if(visibleSets.get(floor)[x][y].equals("hasVisited"))
					spriteBatch.draw(tiles[x][y].getVisiedTexture(), 32 * x, 32 * y);
				else
					spriteBatch.draw(Tile.BLANK.getTexture(), 32 * x, 32 * y);
			}

		
		for(Item item : items.values())
			if(item.canDraw)
				item.draw(spriteBatch);
		for(NPC npc : npcs.values())
			if(npc.canDraw)
				npc.draw(spriteBatch);
			else
				npc.update(Gdx.graphics.getDeltaTime());
		for(Chest chest : chests.values())
			if(chest.canDraw)
				chest.draw(spriteBatch);
		for(MPPlayer mpPlayer : players.get(floor).values())
			if(mpPlayer.canDraw)
				mpPlayer.draw(spriteBatch);
			else
				mpPlayer.update(Gdx.graphics.getDeltaTime());
		
	}
	
	// ------------- Getters -------------
	public Tile getTile(float x, float y){
		if(y < 0 || x < 0 || y/32 >= HEIGHT || x/32 >= WIDTH)
			return null;
		return tiles[(int)(x/32)][(int)(y/32)];
	}

	public Rectangle getViewBounds () {
		return viewBounds;
	}
	
	public int getFloor(){
		return floor;
	}
	
	public void mutateFloor(int amount){
		floor += amount;
		if(floor >= visibleSets.size()){
			visibleSets.add(new String[WIDTH][HEIGHT]);
			for(int x=0; x<WIDTH; x++)
				for(int y=0; y<HEIGHT; y++)
					visibleSets.get(floor)[x][y] = "notVisited";
		}
	}
	
	public SpriteBatch getSpriteBatch () {
		return spriteBatch;
	}
	
	// ------------- Setters -------------
	@Override
	public void setView (OrthographicCamera camera) {
		spriteBatch.setProjectionMatrix(camera.combined);
		float width = camera.viewportWidth * camera.zoom;
		float height = camera.viewportHeight * camera.zoom;
		viewBounds.set(camera.position.x - width / 2, camera.position.y - height / 2, width, height);
	}
	@Override
	public void setView (Matrix4 projection, float x, float y, float width, float height) {
		spriteBatch.setProjectionMatrix(projection);
		viewBounds.set(x, y, width, height);
	}
	
	public String cursor(Integer ID, int x, int y){
		if(get(ID) != null && get(ID).canDraw)
			if(get(ID) instanceof Monster)
				if(get(ID).getName() != null)
					return ((Animate) get(ID)).getName() + ", level " + ((Animate) get(ID)).getStats().getLevel();
				else
					return ((Animate) get(ID)).getRace() + ", level " + ((Animate) get(ID)).getStats().getLevel();
			else if(get(ID) instanceof NPC)
				if(((Animate) get(ID)).getType() != null)
					return get(ID).getName() + ", " + ((Animate) get(ID)).getType().toLowerCase();
				else
					return get(ID).getName();
			else
				return get(ID).getName();
		else if(getTile(x, y) != null && !visibleSets.get(floor)[(int)(x/32)][(int)(y/32)].equals("notVisited"))
			return getTile(x, y).getName();
		return "";
	}
	
	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(int[] layers) {
		// TODO Auto-generated method stub
		
	}

	public void setVisible(float x, float y, String visible) {
		this.visibleSets.get(floor)[(int)(x/32)][(int)(y/32)] = visible;
	}
}