package com.peter.rogue.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.peter.rogue.Global;
import com.peter.rogue.entities.Player;
import com.peter.rogue.map.Map;

public class UI{
    private BitmapFont gothicFont;
    private BitmapFont font;
    
	public UI(){
		font = new BitmapFont();
		gothicFont = new BitmapFont(Gdx.files.internal("fonts/Cardinal.fnt"), Gdx.files.internal("fonts/Cardinal.png"), false);
	}
	
	public void draw(Map renderer, Player player){
		renderer.getSpriteBatch().end();
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
		
		renderer.setView(Global.camera);
		renderer.getSpriteBatch().begin();
		display(renderer.getSpriteBatch(), player);
		renderer.getSpriteBatch().end();
		
		if(player.isMenuActive()){
			if(player.getMenu().equals("Inventory"))
				player.getInventory().display(renderer.getSpriteBatch(), font);
			
			else if(player.getMenu().equals("Chest")){
				player.getInventory().display(renderer.getSpriteBatch(), font);
				player.getMenuObject().display(renderer.getSpriteBatch(), font);
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
