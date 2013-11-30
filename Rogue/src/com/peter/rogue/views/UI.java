package com.peter.rogue.views;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.peter.entities.Entity;
import com.peter.entities.NPC;
import com.peter.entities.Player;
import com.peter.entities.Shopkeep;
import com.peter.inventory.Chest;
import com.peter.map.Map;
import com.peter.packets.MPPlayer;
import com.peter.rogue.Global;
import com.peter.rogue.screens.Play;

public class UI{
    private Texture texture1 = new Texture(Gdx.files.internal("img/guiLeftTest.png"));
    private Texture texture2 = new Texture(Gdx.files.internal("img/guiRightTest.png"));
    public static HashMap<Vector3, Entity> screenMarks;
    private Vector2 screenCoord;
    
	public UI(Player player){
		screenMarks = new HashMap<Vector3, Entity>();
	}
	
	public void draw(Player player){
		// Draws the messages and statuses on top of everything
		for(NPC npc : Play.map.npcs.values()){
			if(npc.canDraw){
				if(npc.messageFlag){
					Global.mapShapes.begin(ShapeType.Filled);
					Global.mapShapes.setColor(0, 0f, 0, 1f);
					Global.mapShapes.rect(npc.getX(), npc.getY() - 17, Global.font.getBounds(npc.getMessage()).width, Global.font.getLineHeight());
					Global.mapShapes.end();
				}
				if(npc.statusFlag){
					Global.mapShapes.begin(ShapeType.Filled);
					if(npc.getStatus() == 0)
						Global.mapShapes.setColor(0f, 0f, .6f, 1f);
					else if(npc.getStatus() < 0)
						Global.mapShapes.setColor(.4f, 0f, 0f, 1f);
					else if(npc.getStatus() > 0)
						Global.mapShapes.setColor(0f, .4f, 0f, 1f);
					Global.mapShapes.circle(npc.getX(), npc.getY() + 20, 13);
					Global.mapShapes.end();
					Global.mapShapes.begin(ShapeType.Line);
					Global.mapShapes.setColor(0f, 0f, 0f, 1f);
					Global.mapShapes.circle(npc.getX(), npc.getY() + 20, 13);
					Global.mapShapes.end();
				}
				Play.map.getSpriteBatch().begin();
				Global.font.draw(Play.map.getSpriteBatch(), npc.getMessage(), npc.getX(), npc.getY());
				if(npc.getStatus() != null)
					if(Math.abs(npc.getStatus()) < 10)
						Global.font.draw(Play.map.getSpriteBatch(), ((Integer)(Math.abs(npc.getStatus()))).toString(), npc.getX() - 4, npc.getY() + 26);
					else
						Global.font.draw(Play.map.getSpriteBatch(), ((Integer)(Math.abs(npc.getStatus()))).toString(), npc.getX() - 7, npc.getY() + 26);
				Play.map.getSpriteBatch().end();
			}
		}
		
		for(MPPlayer mpPlayer : Play.map.players.values()){
			//System.out.println(npc.ID);
			if(mpPlayer.canDraw){
				if(mpPlayer.statusFlag){
					System.out.println("Here!!!!");
					Global.mapShapes.begin(ShapeType.Filled);
					if(mpPlayer.getStatus() == 0)
						Global.mapShapes.setColor(0f, 0f, .6f, 1f);
					else if(mpPlayer.getStatus() < 0)
						Global.mapShapes.setColor(.4f, 0f, 0f, 1f);
					else if(mpPlayer.getStatus() > 0)
						Global.mapShapes.setColor(0f, .4f, 0f, 1f);
					Global.mapShapes.circle(mpPlayer.getX(), mpPlayer.getY() + 20, 13);
					Global.mapShapes.end();
					Global.mapShapes.begin(ShapeType.Line);
					Global.mapShapes.setColor(0f, 0f, 0f, 1f);
					Global.mapShapes.circle(mpPlayer.getX(), mpPlayer.getY() + 20, 13);
					Global.mapShapes.end();
				}
				Play.map.getSpriteBatch().begin();
				Global.font.draw(Play.map.getSpriteBatch(), mpPlayer.getMessage(), mpPlayer.getX(), mpPlayer.getY());
				if(mpPlayer.getStatus() != null)
					if(Math.abs(mpPlayer.getStatus()) < 10)
						Global.font.draw(Play.map.getSpriteBatch(), ((Integer)(Math.abs(mpPlayer.getStatus()))).toString(), mpPlayer.getX() - 4, mpPlayer.getY() + 26);
					else
						Global.font.draw(Play.map.getSpriteBatch(), ((Integer)(Math.abs(mpPlayer.getStatus()))).toString(), mpPlayer.getX() - 7, mpPlayer.getY() + 26);
				Play.map.getSpriteBatch().end();
			}
		}
		
		/*if(player.messageFlag){
			Global.mapShapes.begin(ShapeType.Filled);
			Global.mapShapes.setColor(0, 0, 0, 1f);
			Global.mapShapes.rect(player.getX(), player.getY() - 17, Global.font.getBounds(player.getMessage()).width, Global.font.getLineHeight());
			Global.mapShapes.end();
		}*/
		if(player.statusFlag){
			Global.mapShapes.begin(ShapeType.Filled);
			if(player.getStatus() == 0)
				Global.mapShapes.setColor(0f, 0f, .6f, 1f);
			else if(player.getStatus() < 0)
				Global.mapShapes.setColor(.4f, 0f, 0f, 1f);
			else if(player.getStatus() > 0)
				Global.mapShapes.setColor(0f, .4f, 0f, 1f);
			Global.mapShapes.circle(player.getX(), player.getY() + 20, 13);
			Global.mapShapes.end();
		}

		Play.map.getSpriteBatch().begin();
		Global.font.draw(Play.map.getSpriteBatch(), player.getMessage(), player.getX(), player.getY());
		if(player.getStatus() != null)
			if(Math.abs(player.getStatus()) < 10)
				Global.font.draw(Play.map.getSpriteBatch(), ((Integer)(Math.abs(player.getStatus()))).toString(), player.getX() - 4, player.getY() + 26);
			else
				Global.font.draw(Play.map.getSpriteBatch(), ((Integer)(Math.abs(player.getStatus()))).toString(), player.getX() - 7, player.getY() + 26);
		
		Play.map.getSpriteBatch().end();

		Global.screenShapes.begin(ShapeType.Filled);
		Global.screenShapes.setColor(0, 0, 0, 1f);
		Global.screenShapes.rect(0, 0, Global.SCREEN_WIDTH, 100f);
		Global.screenShapes.end();
		Global.screenShapes.begin(ShapeType.Line);
		Global.screenShapes.setColor(Color.DARK_GRAY);
		Global.screenShapes.line(0, 100f, Global.SCREEN_WIDTH, 100f);
		Global.screenShapes.end();
		
		display(Global.screen, player);
		
		if(player.isMenuActive()){
			screenCoord = new Vector2(Gdx.input.getX(), (Gdx.input.getY() * -1 + Global.SCREEN_HEIGHT));
			if(player.getMenu().equals("Inventory")){
				player.getInventory().display(Global.screen, Global.font, screenCoord, player);
			}
			
			else if(player.getMenu().equals("Chest")){
				player.getInventory().setTrade(((Chest)(player.getMenuObject())));
				((Chest)(player.getMenuObject())).setTrade(player);
				player.getInventory().display(Global.screen, Global.font, screenCoord, player);
				((Chest)(player.getMenuObject())).display(Global.screen, Global.font, screenCoord);
			}
			
			else if(player.getMenu().equals("Barter")){
				player.getInventory().setTrade(((Shopkeep)(player.getMenuObject())));
				((Shopkeep)(player.getMenuObject())).setTrade(player);
				player.getInventory().display(Global.screen, Global.font, screenCoord, player);
				((Shopkeep)(player.getMenuObject())).display(Global.screen, Global.font, screenCoord);
			}
		}
		else
			player.getInventory().setTrade(null);
		
		update(Gdx.graphics.getDeltaTime());
	}
	
	public void update(float delta){
	}
	
	public void display(SpriteBatch spriteBatch, Player player){
		spriteBatch.begin();
		spriteBatch.draw(texture1, 0, 0);
		spriteBatch.draw(texture2, Global.SCREEN_WIDTH - 179, 0);
		
		spriteBatch.draw(player.getPicture(),  200, 15);
		
		Global.gothicFont.setScale(1f);
		Global.gothicFont.draw(spriteBatch, player.getName(), 280, 90);
		Global.gothicFont.setScale(.7f);
		if(player.getStats().getPoints() > 0){
			Global.gothicFont.setColor(Color.GREEN);
			Global.gothicFont.draw(spriteBatch, "Level: " + player.getStats().getLevel(), 280, 50);
			Global.gothicFont.setColor(Color.WHITE);
		}
		else
			Global.gothicFont.draw(spriteBatch, "Level: " + player.getStats().getLevel(), 280, 50);
		Global.gothicFont.draw(spriteBatch, "Hitpoints: " + player.getStats().getHitpoints() + "/" + player.getStats().getMaxHitpoints(), 430, 50);
		Global.gothicFont.draw(spriteBatch, "Experience:  " + player.getStats().getExperience(), 430, 80);
		Global.gothicFont.draw(spriteBatch, "Demeanor: ", 670, 80);
		if(player.isHostile())
			Global.gothicFont.draw(spriteBatch, "Hostile", 755, 80);
		else
			Global.gothicFont.draw(spriteBatch, "Friendly", 755, 80);
		Global.gothicFont.draw(spriteBatch, "Floor: " + Map.getFloor(), 670, 50);
		
		spriteBatch.end();
	}
	
	public void dispose(){
	}

}
