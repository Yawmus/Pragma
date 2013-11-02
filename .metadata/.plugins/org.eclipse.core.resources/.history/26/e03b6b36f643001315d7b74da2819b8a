package com.peter.rogue.inventory;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.peter.rogue.Global;
import com.peter.rogue.entities.Entity;

public class Chest extends Entity {
	
	private LinkedList<Item> items;
	public static final int HEIGHT = 200, WIDTH = 300;
	
	public Chest(){
		super("c1.png", "Chest");
		this.name = "Chest";
		this.items = new LinkedList<Item>();
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
		Global.shapeRenderer.begin(ShapeType.Filled);
		Global.shapeRenderer.setColor(0f, 0f, 0f, 1f);
		Global.shapeRenderer.rect(Global.SCREEN_WIDTH/6, Global.SCREEN_HEIGHT/3, HEIGHT, WIDTH);
		Global.shapeRenderer.end();
		Global.shapeRenderer.begin(ShapeType.Line);
		Global.shapeRenderer.setColor(.3f, .84f, 0, 1f);
		Global.shapeRenderer.rect(Global.SCREEN_WIDTH/6, Global.SCREEN_HEIGHT/3, HEIGHT, WIDTH);
		Global.shapeRenderer.end();

		spriteBatch.begin();
		for(int i=0; i<items.size(); i++){
			font.draw(spriteBatch, items.get(i).getName(),  Global.camera.position.x/2, (Global.camera.position.y + HEIGHT/2) - i * 15);
		}
		spriteBatch.end();
	}
}