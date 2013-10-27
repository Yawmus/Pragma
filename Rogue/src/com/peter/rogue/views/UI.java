package com.peter.rogue.views;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.peter.rogue.Global;
import com.peter.rogue.entities.NPC;
import com.peter.rogue.entities.Player;
import com.peter.rogue.map.Map;

public class UI{
    private BitmapFont gothicFont;
    private BitmapFont font;
    
	public UI(){
		font = new BitmapFont();
		gothicFont = new BitmapFont(Gdx.files.internal("fonts/Cardinal.fnt"), Gdx.files.internal("fonts/Cardinal.png"), false);
	}
	
	public void draw(Player player, LinkedList<NPC> npcs){
		
		// Draws the messages and statuses on top of everything
		for(int i=0; i<npcs.size(); i++){
			NPC.pos = new Vector3(npcs.get(i).getX(), npcs.get(i).getY(), 0);
			Global.camera.project(NPC.pos);
			Global.renderer.getSpriteBatch().end();
			
			if(npcs.get(i).messageFlag){
				Global.shapeRenderer.begin(ShapeType.Filled);
				Global.shapeRenderer.setColor(0, 0, 0, 1f);
				Global.shapeRenderer.rect(NPC.pos.x, NPC.pos.y - 17, font.getBounds(npcs.get(i).getMessage()).width, font.getLineHeight());
				Global.shapeRenderer.end();
			}
			if(npcs.get(i).statusFlag){
				Global.shapeRenderer.begin(ShapeType.Filled);
				Global.shapeRenderer.setColor(.4f, 0f, 0f, 1f);
				Global.shapeRenderer.circle(NPC.pos.x, NPC.pos.y + 20, font.getBounds(npcs.get(i).getStatus().toString()).width);
				Global.shapeRenderer.end();
			}
			Global.renderer.getSpriteBatch().begin();
			font.draw(Global.renderer.getSpriteBatch(), npcs.get(i).getMessage(), npcs.get(i).getX(), npcs.get(i).getY());
			if(npcs.get(i).getStatus() != 0){
				font.draw(Global.renderer.getSpriteBatch(), npcs.get(i).getStatus().toString(), npcs.get(i).getX() - 8, npcs.get(i).getY() + 26);
			}
		}
		
		Global.renderer.getSpriteBatch().end();
		Player.pos = new Vector3(player.getX(), player.getY(), 0);
		Global.camera.project(Player.pos);
		if(player.messageFlag){
			Global.shapeRenderer.begin(ShapeType.Filled);
			Global.shapeRenderer.setColor(0, 0, 0, 1f);
			Global.shapeRenderer.rect(Player.pos.x, Player.pos.y - 17, font.getBounds(player.getMessage()).width, font.getLineHeight());
			Global.shapeRenderer.end();
		}
		
		if(player.statusFlag){
			Global.shapeRenderer.begin(ShapeType.Filled);
			Global.shapeRenderer.setColor(0, 0, 0, 1f);
			Global.shapeRenderer.rect(Player.pos.x, Player.pos.y, font.getBounds(player.getMessage()).width, font.getLineHeight());
			Global.shapeRenderer.end();
		}

		Global.renderer.getSpriteBatch().begin();
		font.draw(Global.renderer.getSpriteBatch(), player.getMessage(), player.getX(), player.getY());
		
		
		Global.renderer.getSpriteBatch().end();
		Global.shapeRenderer.begin(ShapeType.Filled);
		Global.shapeRenderer.setColor(0, 0, 0, 1f);
		Global.shapeRenderer.rect(0, 0, Global.SCREEN_WIDTH, 100f);
		Global.shapeRenderer.end();
		Global.shapeRenderer.begin(ShapeType.Line);
		Global.shapeRenderer.setColor(1f, .84f, 0, 1f);
		Global.shapeRenderer.line(0, 100f, Global.SCREEN_WIDTH, 100f);
		Global.shapeRenderer.end();
		
		// If near edge of map then don't update respective axis
		if(player.getX() > Global.SCREEN_WIDTH/2 - 32*3 && player.getX() < Map.WIDTH*32 - 18*32)
			Global.camera.position.x = player.getX() + player.getWidth() / 2;
		if(player.getY() > Global.SCREEN_HEIGHT/2 - 32*6 && player.getY() < Map.HEIGHT*32 - 9*32)
			Global.camera.position.y = player.getY() + player.getHeight() / 2;
		Global.camera.update();
		
		Global.renderer.setView(Global.camera);
		Global.renderer.getSpriteBatch().begin();
		display(Global.renderer.getSpriteBatch(), player);
		Global.renderer.getSpriteBatch().end();
		
		if(player.isMenuActive()){
			if(player.getMenu().equals("Inventory"))
				player.getInventory().display(Global.renderer.getSpriteBatch(), font);
			
			else if(player.getMenu().equals("Chest")){
				player.getInventory().display(Global.renderer.getSpriteBatch(), font);
				player.getMenuObject().display(Global.renderer.getSpriteBatch(), font);
			}
		}
		update(Gdx.graphics.getDeltaTime());
	}
	
	public void update(float delta){
	}
	
	public void display(SpriteBatch spriteBatch, Player player){
		spriteBatch.draw(player.getPicture(),  Global.camera.position.x - Global.SCREEN_WIDTH/2 + 45, Global.camera.position.y - Global.SCREEN_HEIGHT/2 + 20);
		
		gothicFont.setScale(1f);
		gothicFont.draw(spriteBatch, player.getName(), Global.camera.position.x - Global.SCREEN_WIDTH/2 + 125, Global.camera.position.y - Global.SCREEN_HEIGHT/2 + 90);
		gothicFont.setScale(.7f);
		gothicFont.draw(spriteBatch, "Level: " + player.getStats().getLevel(), Global.camera.position.x - Global.SCREEN_WIDTH/2 + 125, Global.camera.position.y - Global.SCREEN_HEIGHT/2 + 50);
		gothicFont.draw(spriteBatch, "Hitpoints: " + player.getStats().getHitpoints() + "/" + player.getStats().getMaxHitpoints(), Global.camera.position.x - Global.SCREEN_WIDTH/4, Global.camera.position.y - Global.SCREEN_HEIGHT/2 + 50);
		gothicFont.draw(spriteBatch, "Strenght:  " + player.getStats().getStrength(), Global.camera.position.x - Global.SCREEN_WIDTH/4, Global.camera.position.y - Global.SCREEN_HEIGHT/2 + 80);
		gothicFont.draw(spriteBatch, "Dexterity:  " + player.getStats().getDexterity(), Global.camera.position.x - Global.SCREEN_WIDTH/4 + 150, Global.camera.position.y - Global.SCREEN_HEIGHT/2 + 50);
		gothicFont.draw(spriteBatch, "Experience: " + player.getStats().getExperience(), Global.camera.position.x - Global.SCREEN_WIDTH/4 + 150, Global.camera.position.y - Global.SCREEN_HEIGHT/2 + 80);
		gothicFont.draw(spriteBatch, "Demeanor: ", Global.camera.position.x - Global.SCREEN_WIDTH/4 + 350, Global.camera.position.y - Global.SCREEN_HEIGHT/2 + 80);
		if(player.isHostile())
			gothicFont.draw(spriteBatch, "Hostile", Global.camera.position.x - Global.SCREEN_WIDTH/4 + 440, Global.camera.position.y - Global.SCREEN_HEIGHT/2 + 80);
		else
			gothicFont.draw(spriteBatch, "Friendly", Global.camera.position.x - Global.SCREEN_WIDTH/4 + 440, Global.camera.position.y - Global.SCREEN_HEIGHT/2 + 80);
		gothicFont.draw(spriteBatch, "Floor: " + Map.getFloor(), Global.camera.position.x - Global.SCREEN_WIDTH/4 + 350, Global.camera.position.y - Global.SCREEN_HEIGHT/2 + 50);
		spriteBatch.draw(player.getInventory().getBackpack(),  Global.camera.position.x + Global.SCREEN_WIDTH/2 - 100, Global.camera.position.y - Global.SCREEN_HEIGHT/2 + 20);
	}
	
	public void dispose(){
		gothicFont.dispose();
	}

}
