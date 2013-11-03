package com.peter.rogue.inventory;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.peter.rogue.Global;
import com.peter.rogue.entities.Entity;

public class Chest extends Entity {
	
	private ArrayList<Item> items;
	public static final int HEIGHT = 200, WIDTH = 300;
	
	public Chest(){
		super("c1.png", "Chest");
		this.name = "Chest";
		this.items = new ArrayList<Item>();
		for(int i=0; i<5; i++){
			if(Global.rand(3, 0) == 0)
				items.add(Food.MEAT);
			else
				items.add(Food.BREAD);
		}
	}
	
	public void add(Item item){
		items.add(item);
	}
	public void display(SpriteBatch spriteBatch, BitmapFont font){
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