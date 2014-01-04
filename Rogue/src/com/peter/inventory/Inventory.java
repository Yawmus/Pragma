package com.peter.inventory;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.peter.entities.Entity;
import com.peter.entities.Player;
import com.peter.entities.Shopkeep;
import com.peter.packets.AddTradeItemPacket;
import com.peter.packets.ItemPacket;
import com.peter.packets.RemoveTradeItemPacket;
import com.peter.rogue.Global;
import com.peter.rogue.Rogue;
import com.peter.rogue.screens.Play;

public class Inventory implements InputProcessor{
	
	private Backpack backpack = new Backpack();
	private ArrayList<Item> items;
	private ArrayList<Rectangle> collisions;
	private Rectangle[] pointCollisions;
	public Gear gear;
	private int weight;
	private int wallet;
	private Item hover;
	private int hoverIndex;
	private Character selector;
	private Rectangle hoverCollision;
	public static final int BOX1_WIDTH = 150, BOX2_WIDTH = 165, HEIGHT = 300, WIDTH = 500;
	public static final int ORIGIN_X = 670, ORIGIN_Y = 250, STATS = 5;
	private Entity trade;
	private Player player;
	
	public Inventory(Player player){
		this.player = player;
		
		backpack = Backpack.SMALL;
		items = new ArrayList<Item>();
		collisions = new ArrayList<Rectangle>();
		gear = new Gear(ORIGIN_X + BOX1_WIDTH + BOX2_WIDTH, ORIGIN_Y, player);
		pointCollisions = new Rectangle[STATS];
		for(int i=0; i<STATS; i++){
			pointCollisions[i] = new Rectangle();
			pointCollisions[i].setSize(8);
		}
		wallet = 0;
		add(new Wearable(Wearable.BREAST_PLATE));
		add(new Wearable(Wearable.BREAST_PLATE));
		add(new Wearable(Wearable.SHOES));
		add(new Food(Food.BREAD));
		add(new Food(Food.BREAD));
	}
	
	public boolean checkIsFull(Item item){
		if(item.getWeight() + weight > backpack.getCapacity())
			return true;
		return false;
	}
	
	public void add(Item item){
		if(item.getName().equals("Gold"))
			wallet += item.getValue();
		else if(!checkIsFull(item)){
			weight += item.getWeight();
			collisions.add(new Rectangle());
			collisions.get(collisions.size()-1).setSize(130, 14);
			items.add(item);
		}
		else
			player.setAlert("Backpack full!", true);
	}
	public Item remove(int i){
		weight -= items.get(i).getWeight();
		collisions.remove(i);
		hoverCollision = null;
		hover = null;
		return items.remove(i);
	}
	public Item move(int i){
		hoverCollision = null;
		hover = null;
		return items.remove(i);
	}
	public void move(Item item){
		collisions.add(new Rectangle());
		collisions.get(collisions.size()-1).setSize(130, 15);
		items.add(item);
	}
	public void display(SpriteBatch spriteBatch, BitmapFont font, Vector2 screenCoord){
		Vector3 coord = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		Global.camera.unproject(coord);
		
		Global.screenShapes.begin(ShapeType.Filled);
		Global.screenShapes.setColor(0f, 0f, 0f, 1f);
		Global.screenShapes.rect(ORIGIN_X, ORIGIN_Y, WIDTH, HEIGHT);
		Global.screenShapes.setColor(0.1f, 0.1f, 0.1f, 1f);
		Global.screenShapes.rect(ORIGIN_X + 5, ORIGIN_Y + 5, BOX1_WIDTH - 10, HEIGHT - 10);
		Global.screenShapes.end();
		
		Global.screenShapes.begin(ShapeType.Line);
		Global.screenShapes.setColor(Color.DARK_GRAY);
		Global.screenShapes.rect(ORIGIN_X, ORIGIN_Y, WIDTH, HEIGHT);
		Global.screenShapes.line(ORIGIN_X + BOX1_WIDTH + BOX2_WIDTH, ORIGIN_Y + HEIGHT, ORIGIN_X + BOX1_WIDTH + BOX2_WIDTH, ORIGIN_Y);
		Global.screenShapes.line(ORIGIN_X + BOX1_WIDTH, ORIGIN_Y + 155, ORIGIN_X + BOX1_WIDTH + BOX2_WIDTH, ORIGIN_Y + 155);
		Global.screenShapes.line(ORIGIN_X + BOX1_WIDTH + BOX2_WIDTH, ORIGIN_Y + HEIGHT - 60, ORIGIN_X + WIDTH, ORIGIN_Y + HEIGHT - 60);
		Global.screenShapes.line(ORIGIN_X + BOX1_WIDTH, ORIGIN_Y, ORIGIN_X + 150, HEIGHT + 250);
		Global.screenShapes.end();

		if(hover != null){
			spriteBatch.begin();
			spriteBatch.draw(hover.getTexture(), ORIGIN_X + 185, ORIGIN_Y + 100);
			font.draw(spriteBatch, hover.getName(), ORIGIN_X + 225, ORIGIN_Y + 120);
			font.draw(spriteBatch, "Value: " + hover.getValue(), ORIGIN_X + 170, ORIGIN_Y + 80);
			font.draw(spriteBatch, "Weight: " + hover.getWeight(), ORIGIN_X + 235, ORIGIN_Y + 80);
			
			font.draw(spriteBatch, "1 - drop", ORIGIN_X + 175, ORIGIN_Y + 50);
			font.draw(spriteBatch, "2 - throw", ORIGIN_X + 238, ORIGIN_Y + 50);
			if(hover instanceof Food)
				font.draw(spriteBatch, "3 - eat", ORIGIN_X + 175, ORIGIN_Y + 30);
			else if(hover instanceof Wearable || hover instanceof Equipable)
				font.draw(spriteBatch, "3 - wear", ORIGIN_X + 175, ORIGIN_Y + 30);
			if(trade instanceof Chest)
				font.draw(spriteBatch, "4 - move", ORIGIN_X + 238, ORIGIN_Y + 30);
			else if(trade instanceof Shopkeep)
				font.draw(spriteBatch, "4 - sell", ORIGIN_X + 238, ORIGIN_Y + 30);
			
			spriteBatch.end();
			
			if(hoverCollision != null){
				Global.screenShapes.begin(ShapeType.Filled);
				Global.screenShapes.setColor(.2f, .2f, .2f, 1f);
				Global.screenShapes.rect(hoverCollision.x, hoverCollision.y, hoverCollision.width, hoverCollision.height);
				Global.screenShapes.end();
			}
		}
		
		spriteBatch.begin();
		font.draw(spriteBatch, "    ?????: " + 30, ORIGIN_X + 190, ORIGIN_Y + HEIGHT-15);
		font.draw(spriteBatch, " Strength: " + player.getStats().getStrength(), ORIGIN_X + 190, ORIGIN_Y + HEIGHT-35);
		font.draw(spriteBatch, "    Health: " + player.getStats().getMaxHitpoints(), ORIGIN_X + 190, ORIGIN_Y + HEIGHT-55);
		font.draw(spriteBatch, " Defense: " + player.getStats().getDefense(), ORIGIN_X + 190, ORIGIN_Y + HEIGHT-75);
		font.draw(spriteBatch, "Dexterity:  " + player.getStats().getDexterity(), ORIGIN_X + 190, ORIGIN_Y + HEIGHT-95);
		font.draw(spriteBatch, "  Points: " + player.getStats().getPoints(), ORIGIN_X + 200, ORIGIN_Y + HEIGHT-115);

		if(player.getStats().getPoints() > 0)
			for(int i=0; i<STATS; i++){
				font.draw(spriteBatch, "+", ORIGIN_X + 285, ORIGIN_Y + 205 + i*20);
				pointCollisions[i].setPosition(ORIGIN_X + 285, ORIGIN_Y + 195 + i*20);
			}
		
		font.draw(spriteBatch, "Hunger   " + (int)(player.getHunger()*100) + "%", ORIGIN_X + WIDTH - 135, ORIGIN_Y + HEIGHT - 15);
		font.draw(spriteBatch, "Wallet       " + wallet, ORIGIN_X + WIDTH - 135, ORIGIN_Y + HEIGHT - 30);
		font.draw(spriteBatch, "Weight " + weight + "/" + backpack.getCapacity(), ORIGIN_X + BOX1_WIDTH + 10, ORIGIN_Y + HEIGHT - 30);
		//font.draw(spriteBatch, "Thirst        " + "90%", ORIGIN_X + WIDTH - 120, ORIGIN_Y + HEIGHT - 30);
		//font.draw(spriteBatch, "Wallet  " + wallet, ORIGIN_X + BOX1_WIDTH + 10, ORIGIN_Y + HEIGHT - 15);
		
		gear.draw(spriteBatch);
		
		selector = 'a';
		for(int i=0; i<items.size(); i++, selector++){
			font.draw(spriteBatch, selector + ") " + items.get(i).getName(), ORIGIN_X + 10, (ORIGIN_Y + HEIGHT - 10) - i*15);
			collisions.get(i).setPosition(ORIGIN_X + 10, (ORIGIN_Y + HEIGHT - 24) - i*15);
		}

		spriteBatch.end();
		
		for(int i=0; i<STATS && player.getStats().getPoints() != 0; i++){
			if(pointCollisions[i].contains(screenCoord)){
				if(Gdx.input.isButtonPressed(Buttons.LEFT) && Gdx.input.justTouched()){
					switch(i){
					case 0:
						player.getStats().setDexterity(player.getStats().getDexterity() + 1);
						break;
					case 1:
						player.getStats().mutateDefense(1);
						break;
					case 2:
						player.getStats().setMaxHitpoints(player.getStats().getMaxHitpoints() + 1);
						player.getStats().mutateHitpoints(1);
						break;
					case 3:
						player.getStats().setStrength(player.getStats().getStrength() + 1);
						break;
					case 4:
						// Stub
						break;
					}
					player.getStats().mutatePoints(-1);
				}
			}
		}
		
		// Uncomment shape rendering for item collision debug
		/*Gdx.gl.glEnable(GL10.GL_BLEND);
        Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		Global.screenShapes.begin(ShapeType.Filled);
		Global.screenShapes.setColor(0f, 1f, 1f, .4f);
		*/

		if(gear.check(screenCoord, coord) != null)
			setHover(gear.check(screenCoord, coord));
		// Item-mouse collision
		for(int i=0; i<getItems().size(); i++){
			//Global.screenShapes.rect(collisions.get(i).x, collisions.get(i).y, 130, 15);
			if(collisions.get(i).contains(screenCoord)){
				setHover(getItems().get(i), collisions.get(i), i);
				if(items.get(i) instanceof Food){
					Global.mapShapes.begin(ShapeType.Filled);
					Global.mapShapes.setColor(0f, 0, 0, 1f);
					Global.mapShapes.rect(coord.x + 2, coord.y, Global.font.getBounds("eat").width, Global.font.getLineHeight());
					Global.mapShapes.end();
					Play.map.getSpriteBatch().begin();
					
					Global.font.draw(Play.map.getSpriteBatch(), "eat", coord.x + 2, coord.y + Global.font.getLineHeight() - 2);
					Play.map.getSpriteBatch().end();
					
					if(Gdx.input.isButtonPressed(Buttons.RIGHT) && Gdx.input.justTouched()){
						player.mutateHunger(.1f);
						remove(i);
					}
				}
				else if(items.get(i) instanceof Wearable || items.get(i) instanceof Equipable){
					Global.mapShapes.begin(ShapeType.Filled);
					Global.mapShapes.setColor(0f, 0, 0, 1f);
					Global.mapShapes.rect(coord.x + 2, coord.y, Global.font.getBounds("wear").width, Global.font.getLineHeight());
					Global.mapShapes.end();
					Play.map.getSpriteBatch().begin();
					
					Global.font.draw(Play.map.getSpriteBatch(), "wear", coord.x + 2, coord.y + Global.font.getLineHeight() - 2);
					Play.map.getSpriteBatch().end();
					
					if(Gdx.input.isButtonPressed(Buttons.RIGHT) && Gdx.input.justTouched())
						gear.wear((Wearable) move(i));
				}
				if(trade != null){
					if(trade instanceof Shopkeep){
						Global.mapShapes.begin(ShapeType.Filled);
						Global.mapShapes.setColor(0f, 0, 0, 1f);
						Global.mapShapes.rect(coord.x - Global.font.getBounds("sell").width - 2, coord.y, Global.font.getBounds("sell").width, Global.font.getLineHeight());
						Global.mapShapes.end();
						Play.map.getSpriteBatch().begin();
						
						Global.font.draw(Play.map.getSpriteBatch(), "sell", coord.x - Global.font.getBounds("sell").width - 2, coord.y + Global.font.getLineHeight() - 2);
						Play.map.getSpriteBatch().end();
					}
					else if(trade instanceof Chest){
						Global.mapShapes.begin(ShapeType.Filled);
						Global.mapShapes.setColor(0f, 0, 0, 1f);
						Global.mapShapes.rect(coord.x - Global.font.getBounds("move").width - 2, coord.y, Global.font.getBounds("move").width, Global.font.getLineHeight());
						Global.mapShapes.end();
						Play.map.getSpriteBatch().begin();
						
						Global.font.draw(Play.map.getSpriteBatch(), "move", coord.x - Global.font.getBounds("move").width - 2, coord.y + Global.font.getLineHeight() - 2);
						Play.map.getSpriteBatch().end();
					}
				}
				else{
					Global.mapShapes.begin(ShapeType.Filled);
					Global.mapShapes.setColor(0f, 0, 0, 1f);
					Global.mapShapes.rect(coord.x - Global.font.getBounds("drop").width - 2, coord.y, Global.font.getBounds("drop").width, Global.font.getLineHeight());
					Global.mapShapes.end();
					Play.map.getSpriteBatch().begin();
					
					Global.font.draw(Play.map.getSpriteBatch(), "drop", coord.x - Global.font.getBounds("drop").width - 2, coord.y + Global.font.getLineHeight() - 2);
					Play.map.getSpriteBatch().end();
				}
				if(Gdx.input.isButtonPressed(Buttons.LEFT) && Gdx.input.justTouched()){
					// In essence -> sells the item, then adds it to shopkeep's inventory
					if(trade instanceof Shopkeep){
						AddTradeItemPacket tradeItem = new AddTradeItemPacket();
						tradeItem.ID = trade.getID();
						tradeItem.item = new ItemPacket(items.get(i).getName(), items.get(i).ID, Play.map.getFloor());
						Rogue.clientWrapper.client.sendUDP(tradeItem);
						wallet += getItems().get(i).getValue();
						((Shopkeep) trade).add(remove(i));
					}
					// In essence -> adds item to chest and removes from inventory
					else if(trade instanceof Chest){
						AddTradeItemPacket tradeItem = new AddTradeItemPacket();
						tradeItem.ID = trade.getID();
						tradeItem.item = new ItemPacket(items.get(i).getName(), items.get(i).ID, Play.map.getFloor());
						Rogue.clientWrapper.client.sendUDP(tradeItem);
						((Chest) trade).add(remove(i));
					}
					else
						remove(i);
				}
			}
		}
		/*Global.screenShapes.end();
	    Gdx.gl.glDisable(GL10.GL_BLEND);*/
		
	}
	
	public Texture getBackpack(){
		return backpack.getTexture();
	}
	public boolean isItem(){
		if(hover == null)
			return false;
		return true;
	}
	public void setHover(Item hover, Rectangle collision, int i){
		this.hover = hover;
		this.hoverCollision = collision;
		this.hoverIndex = i;
	}
	
	public void setHover(Item hover){
		this.hover = hover;
	}
	public void setHoverCollision(Rectangle hoverCollision){
		this.hoverCollision = hoverCollision;
	}
	
	public Item getHover(){
		return hover;
	}
	
	public ArrayList<Item> getItems(){
		return items;
	}

	public int getWallet() {
		return wallet;
	}

	public void setWallet(int wallet) {
		this.wallet = wallet;
	}
	
	public void mutateWallet(int amount) {
		this.wallet += amount;
	}

	// These are assigned to whomever you are trading with
	public Entity getTrade() {
		return trade;
	}

	public void setTrade(Entity trade) {
		this.trade = trade;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if(keycode >= Keys.A && keycode <= Keys.Z)
			return true;
		
		switch(keycode){
		case Keys.NUM_1:
			if(hover != null){
				Item temp = remove(hoverIndex);
				ItemPacket packet = new ItemPacket(temp.getFullName(), temp.ID, Play.map.getFloor());
				packet.x = (int) (player.getX()/32);
				packet.y = (int) (player.getY()/32);
				Rogue.clientWrapper.client.sendTCP(packet);
				return true;
			}
			break;
		case Keys.NUM_2:
			if(hover != null){
				player.setMenu("Throw");
				player.setAlert("Not implemented yet!", false);
				return true;
			}
			break;
		case Keys.NUM_3:
			if(hover instanceof Food){
				player.mutateHunger(.1f);
				remove(hoverIndex);
				return true;
			}
			else if(hover instanceof Wearable){
				gear.wear((Wearable) move(hoverIndex));
				return true;
			}
			break;
		case Keys.NUM_4:
			if(player.getMenu().equals("Barter") && hover != null){
				AddTradeItemPacket tradeItem = new AddTradeItemPacket();
				tradeItem.ID = trade.getID();
				tradeItem.item = new ItemPacket(hover.getFullName(), hover.ID, Play.map.getFloor());
				Rogue.clientWrapper.client.sendUDP(tradeItem);
				mutateWallet(hover.getValue());
				((Shopkeep) trade).add(remove(hoverIndex));
				return true;
			}
			else if(player.getMenu().equals("Chest") && hover != null){
				AddTradeItemPacket tradeItem = new AddTradeItemPacket();
				tradeItem.ID = trade.getID();
				tradeItem.item = new ItemPacket(hover.getFullName(), hover.ID, Play.map.getFloor());
				Rogue.clientWrapper.client.sendUDP(tradeItem);
				((Chest) trade).add(remove(hoverIndex));
				return true;
			}
			break;
		case Keys.NUMPAD_1:
		case Keys.NUMPAD_2:
		case Keys.NUMPAD_3:
		case Keys.NUMPAD_4:
		case Keys.NUMPAD_5:
		case Keys.NUMPAD_6:
		case Keys.NUMPAD_7:
		case Keys.NUMPAD_8:
		case Keys.NUMPAD_9:
		case Keys.ESCAPE:
			player.setMenu("");
			Global.multiplexer.removeProcessor(this);
			return true;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		if(character >= 97 && character - 97 < items.size()){
			if(trade instanceof Shopkeep)
				((Shopkeep) trade).setHover(null);
			if(trade instanceof Chest)
				((Chest) trade).setHover(null);
			setHover(items.get(character - 97), collisions.get(character - 97), character - 97);
		}
		else if((character == 27 || (character >= 49 && character <= 57)) && (trade instanceof Shopkeep || trade instanceof Chest)){
			player.setMenu("");
			Global.multiplexer.removeProcessor(this);
		}
			
		return true;
	}

	@Override // button - 0 for left, 1 for right
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
