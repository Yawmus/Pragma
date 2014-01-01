package com.peter.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.peter.entities.Player;
import com.peter.rogue.Global;
import com.peter.rogue.screens.Play;

public class Gear{
	private Item[] items;
	// 0 - head, 1 - body, 2 - arms, 3 - legs, 4 - feet, 5 - hand, 6 - ring
	private Texture[] unused;
	private String[] names;
	private final int SLOTS = 7;
	private int originX, originY;
	private Rectangle[] collisions;
	
	public Gear(int originX, int originY){
		collisions = new Rectangle[SLOTS];
		unused = new Texture[SLOTS];
		items = new Wearable[SLOTS];
		names = new String[SLOTS];
		
		this.originX = originX;
		this.originY = originY;
		
		unused[0] = new Texture(Gdx.files.internal("img/head.png"));
		unused[1] = new Texture(Gdx.files.internal("img/body.png"));
		unused[2] = new Texture(Gdx.files.internal("img/arms.png"));
		unused[3] = new Texture(Gdx.files.internal("img/legs.png"));
		unused[4] = new Texture(Gdx.files.internal("img/feet.png"));
		unused[5] = new Texture(Gdx.files.internal("img/hand.png"));
		unused[6] = new Texture(Gdx.files.internal("img/ring.png"));
		
		for(int i=0; i<SLOTS; i++){
			collisions[i] = new Rectangle();
			collisions[i].setSize(32, 32);
		}

		collisions[0].setPosition(originX + 80, originY + 165);
		collisions[1].setPosition(originX + 80, originY + 125);
		collisions[2].setPosition(originX + 40, originY + 125);
		collisions[3].setPosition(originX + 80, originY + 85);
		collisions[4].setPosition(originX + 80, originY + 45);
		collisions[5].setPosition(originX + 120, originY + 125);
		collisions[6].setPosition(originX + 40, originY + 165);
		
		names[0] = "Head";
		names[1] = "Body";
		names[2] = "Arms";
		names[3] = "Legs";
		names[4] = "Feet";
		names[5] = "Hand";
		names[6] = "Ring";
	}
	public Item check(Vector2 screenCoord, Vector3 coord, Player player) {
		for(int i=0; i<SLOTS; i++)
			if(collisions[i].contains(screenCoord) && this.items[i] != null){
				Global.mapShapes.begin(ShapeType.Filled);
				Global.mapShapes.setColor(0f, 0, 0, 1f);
				Global.mapShapes.rect(coord.x, coord.y, Global.font.getBounds("remove").width, Global.font.getLineHeight());
				Global.mapShapes.end();
				
				Play.map.getSpriteBatch().begin();
				Global.font.draw(Play.map.getSpriteBatch(), "remove", coord.x, coord.y + Global.font.getLineHeight() - 2);
				Play.map.getSpriteBatch().end();
				
				if(Gdx.input.isButtonPressed(Buttons.RIGHT) && Gdx.input.justTouched()){
					player.getStats().mutateDefense(-((Wearable) (items[i])).getDefense());
					player.getInventory().move(items[i]);
					this.items[i] = null;
				}
				return this.items[i] != null ? this.items[i] : null;
			}
		return null;			
	}
	public void draw(SpriteBatch spriteBatch) {
		spriteBatch.draw(items[0] != null ? items[0].getTexture() : unused[0], originX + 80, originY + 165);
		spriteBatch.draw(items[1] != null ? items[1].getTexture() : unused[1], originX + 80, originY + 125);
		spriteBatch.draw(items[2] != null ? items[2].getTexture() : unused[2], originX + 40, originY + 125);
		spriteBatch.draw(items[3] != null ? items[3].getTexture() : unused[3], originX + 80, originY + 85);
		spriteBatch.draw(items[4] != null ? items[4].getTexture() : unused[4], originX + 80, originY + 45);
		spriteBatch.draw(items[5] != null ? items[5].getTexture() : unused[5], originX + 120, originY + 125);
		spriteBatch.draw(items[6] != null ? items[6].getTexture() : unused[6], originX + 40, originY + 165);
	}
	
	public void wear(Wearable item, Player player){
		for(int i=0; i<SLOTS; i++)
			if(names[i] == item.getType()){
				if(items[i] != null){
					player.getStats().mutateDefense(-((Wearable) (items[i])).getDefense());
					player.getInventory().move(items[i]);
				}
				items[i] = item;
				player.getStats().mutateDefense(((Wearable) (items[i])).getDefense());
			}
	}
}
