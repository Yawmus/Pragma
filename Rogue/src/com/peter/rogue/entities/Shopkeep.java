package com.peter.rogue.entities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.peter.rogue.Global;
import com.peter.rogue.inventory.Food;
import com.peter.rogue.inventory.Item;

public class Shopkeep extends NPC{

	private ArrayList<Item> items;
	public static final int HEIGHT = 200, WIDTH = 300;
	
	public Shopkeep(String filename) {
		super(filename, "Shopkeep");
		
		this.items = new ArrayList<Item>();

		stats.setLevel(1);
		stats.setHitpoints(10);
		stats.setDexterity(5);
		stats.setStrength(1);
		stats.setExperience(100);
		
		items.add(Food.MEAT);
	}
	
	public void update(float delta){
		super.update(delta);
	}

	public void display(SpriteBatch spriteBatch, BitmapFont font) {
		Global.screenShapes.begin(ShapeType.Filled);
		Global.screenShapes.setColor(0f, 0f, 0f, 1f);
		Global.screenShapes.rect(270, 255, HEIGHT, WIDTH);
		Global.screenShapes.end();
		Global.screenShapes.begin(ShapeType.Line);
		Global.screenShapes.setColor(.3f, .84f, 0, 1f);
		Global.screenShapes.rect(270, 255, HEIGHT, WIDTH);
		Global.screenShapes.end();
		
		spriteBatch.begin();
		font.setScale(1f);
		for(int i=0; i<items.size(); i++){
			font.draw(spriteBatch, items.get(i).getName(),  275, 250 + HEIGHT - i * 15);
		}
		spriteBatch.end();
	}
}
