package com.peter.rogue.entities;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.peter.rogue.Global;
import com.peter.rogue.data.LevelData;
import com.peter.rogue.inventory.Chest;
import com.peter.rogue.inventory.Food;
import com.peter.rogue.inventory.Head;
import com.peter.rogue.views.UI;

public class EntityManager {

    private ArrayList<Entity> objects;
    private LevelData data;
    private int randX, randY;
    private Player player;
    private UI ui = new UI();
    
    
    public EntityManager(){
    	data = new LevelData();
    	objects = new ArrayList<Entity>();

		player = new Player("at.png");
		player.setPosition(18, 7);

		objects.add(new Chest());
		objects.get(objects.size()-1).setPosition(4, 4);
		
		objects.add(new Chest());
		objects.get(objects.size()-1).setPosition(6, 4);
		
		objects.add(Food.BREAD);
		objects.get(objects.size()-1).setPosition(8, 18);
		
		objects.add(Head.HAT);
		objects.get(objects.size()-1).setPosition(9, 18);
    }
    
	public void draw(){
		
		Entity.map.getSpriteBatch().begin();
		for(int i=0; i<objects.size(); i++){
			if(objects.get(i).isPickedUp())
				objects.remove(i);
			else if(objects.get(i).canDraw)
				objects.get(i).draw(Entity.map.getSpriteBatch());
		}
		
		for(int i=0; i<NPC.npcs.size(); i++)
			if(NPC.npcs.get(i).canDraw)
				NPC.npcs.get(i).draw(Entity.map.getSpriteBatch());
			else
				NPC.npcs.get(i).update(Gdx.graphics.getDeltaTime());

		Entity.map.getSpriteBatch().end();
		player.light();
		Entity.map.getSpriteBatch().begin();
		player.draw(Entity.map.getSpriteBatch());
		Entity.map.getSpriteBatch().end();
		ui.draw(player, NPC.npcs);
		
		Animate.pos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		Global.camera.unproject(Animate.pos);

		if(!Entity.map.getMark(Animate.pos.x, Animate.pos.y).equals("") && Entity.map.get(Animate.pos.x, Animate.pos.y).canDraw){
			player.setInformation(Entity.map.cursor(Entity.map.getMark(Animate.pos.x, Animate.pos.y)));
			Global.camera.project(Animate.pos);
			Global.screenShapes.begin(ShapeType.Filled);
			Global.screenShapes.setColor(0, 0, 0, 1f);
			Global.screenShapes.rect(Animate.pos.x, Animate.pos.y, Entity.font.getBounds(player.getInformation()).width, Entity.font.getLineHeight());
			Global.screenShapes.end();
			Global.screen.begin();
			Entity.font.draw(Global.screen, player.getInformation(), Animate.pos.x, Animate.pos.y + Entity.font.getLineHeight() - 2);
			Global.screen.end();
		}
		else
			player.setInformation("");
		
		
    }
    
    public void init(){
    	for(int i=0; i<data.getCitizens(); i++){
			randX = Global.rand(13, 3);
			randY = Global.rand(7, 3);
			NPC.npcs.add(new Citizen("c_.png"));
			NPC.npcs.get(NPC.npcs.size()-1).setPosition(randX, randY);
		}
    	for(int i=0; i<data.getShopkeeps(); i++){
			randX = Global.rand(13, 3);
			randY = Global.rand(7, 3);
			NPC.npcs.add(new Shopkeep("s_.png"));
			NPC.npcs.get(NPC.npcs.size()-1).setPosition(randX, randY);
		}
    	for(int i=0; i<data.getMonsters(); i++){
			randX = Global.rand(13, 3);
			randY = Global.rand(7, 3);
			NPC.npcs.add(new Worm("tilda.png"));
			NPC.npcs.get(NPC.npcs.size()-1).setPosition(randX, randY);
    	}
    	
		Gdx.input.setInputProcessor(player);
	}
    
    public void purge(){
    	Entity.map.purge(player.getID());
		objects.clear();
		
		init();
    }
    
    public void dispose(){
		for(int i=0; i<NPC.npcs.size(); i++)
			NPC.npcs.get(i).getTexture().dispose();
		player.getTexture().dispose();
		ui.dispose();
		Global.mapShapes.dispose();
		Global.screenShapes.dispose();
    }
}
