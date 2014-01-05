package com.peter.entities;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.peter.inventory.Item;
import com.peter.packets.RemoveTradeItemPacket;
import com.peter.rogue.Global;
import com.peter.rogue.Rogue;
import com.peter.rogue.screens.Play;

public class Shopkeep extends NPC implements InputProcessor{
        
        public ArrayList<Item> items;
        private ArrayList<Rectangle> collisions;
        private Item hover;
        private Rectangle hoverCollision;
        private Entity trade;
    	private Character selector;
        private int hoverIndex;
        
        public static final int HEIGHT = 300, WIDTH = 335;
        public static final int ORIGIN_X = 250, ORIGIN_Y = 250;
        
        public Shopkeep(int level, String race, String type, String name) {
            super("s_.png", level, race, type, name);
                
            symbol = 'S';
            this.items = new ArrayList<Item>();
            this.collisions = new ArrayList<Rectangle>();
        }
        
        public void update(float delta){
                super.update(delta);
        }

        public void add(Item item){
                items.add(item);
                collisions.add(new Rectangle(0, 0, 150, 15));
        }
        
        public Item remove(int i){
                getCollision().remove(i);
        		hoverCollision = null;
        		hover = null;
                return items.remove(i);
        }
        
        public void display(SpriteBatch spriteBatch, BitmapFont font, Vector2 screenCoord){
                Vector3 coord = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                Global.camera.unproject(coord);
                
                Global.screenShapes.begin(ShapeType.Filled);
                Global.screenShapes.setColor(0f, 0f, 0f, 1f);
                Global.screenShapes.rect(ORIGIN_X, ORIGIN_Y, WIDTH, HEIGHT);
                Global.screenShapes.end();

                Global.screenShapes.begin(ShapeType.Filled);
                Global.screenShapes.setColor(0.1f, 0.1f, 0.1f, 1f);
                Global.screenShapes.rect(ORIGIN_X + 5, ORIGIN_Y + 5, WIDTH/2 - 6, HEIGHT - 10);
                Global.screenShapes.end();
                
                Global.screenShapes.begin(ShapeType.Line);
                Global.screenShapes.setColor(Color.DARK_GRAY);
                Global.screenShapes.rect(ORIGIN_X, ORIGIN_Y, WIDTH, HEIGHT);
                Global.screenShapes.line(ORIGIN_X + 170, ORIGIN_Y + 150, ORIGIN_X + WIDTH, ORIGIN_Y + 150);
                Global.screenShapes.line(ORIGIN_X + 170, ORIGIN_Y, ORIGIN_X + 170, HEIGHT + 250);
                Global.screenShapes.end();

                if(hover != null){
                        spriteBatch.begin();
                        spriteBatch.draw(hover.getTexture(), ORIGIN_X + 205, ORIGIN_Y + 100);
                        font.draw(spriteBatch, hover.getName(), ORIGIN_X + 245, ORIGIN_Y + 120);
                        font.draw(spriteBatch, "Value: " + hover.getValue(), ORIGIN_X + 190, ORIGIN_Y + 80);
                        font.draw(spriteBatch, "Weight: " + hover.getWeight(), ORIGIN_X + 255, ORIGIN_Y + 80);
            			font.draw(spriteBatch, "1 - buy", ORIGIN_X + 196, ORIGIN_Y + 50);
            			
                        spriteBatch.end();
                        Global.screenShapes.begin(ShapeType.Filled);
                        Global.screenShapes.setColor(.2f, .2f, .2f, 1f);
                        Global.screenShapes.rect(hoverCollision.x, hoverCollision.y, hoverCollision.width, hoverCollision.height);
                        Global.screenShapes.end();
                }
                
                spriteBatch.begin();
        		selector = 'A';
        		for(int i=0; i<items.size(); i++, selector++){
        			font.draw(spriteBatch, selector + ") " + items.get(i).getName(), ORIGIN_X + 10, (ORIGIN_Y + HEIGHT - 10) - i*15);
                        collisions.get(i).setPosition(ORIGIN_X + 10, (ORIGIN_Y + HEIGHT - 24) - i*15);
                }
                
                spriteBatch.end();

                // Item-mouse collision
                for(int i=0; i<getItems().size(); i++){
                    //Global.screenShapes.rect(getCollision().get(i).x, getCollision().get(i).y, 150, 15);
                    if(getCollision().get(i).contains(screenCoord)){

                        Global.mapShapes.begin(ShapeType.Filled);
                        Global.mapShapes.setColor(0f, 0, 0, 1f);
                        Global.mapShapes.rect(coord.x - Global.font.getBounds("buy").width - 2, coord.y, Global.font.getBounds("buy").width, Global.font.getLineHeight());
                        Global.mapShapes.end();
                        Play.map.getSpriteBatch().begin();
                        
                        Global.font.draw(Play.map.getSpriteBatch(), "buy", coord.x - Global.font.getBounds("buy").width - 2, coord.y + Global.font.getLineHeight() - 2);
                        Play.map.getSpriteBatch().end();
                        
                        if(Gdx.input.isButtonPressed(Buttons.RIGHT))
                                ;// Stuff for right mouse button
                        else if(Gdx.input.isButtonPressed(Buttons.LEFT) && Gdx.input.justTouched()){
                                if(trade != null){
                                        // In essence -> removes item from chest and gives to player
                                        if(trade instanceof Player){
                                                if(((Player) trade).getInventory().checkIsFull(getItems().get(i)))
                                                		((Player) trade).setAlert("Backpack is full!", true);
                                                else if(((Player) trade).getInventory().getWallet() - items.get(i).getValue() < 0)
                                                		((Player) trade).setAlert("Insufficient funds!", true);
                                                else{
                    									RemoveTradeItemPacket tradeItem = new RemoveTradeItemPacket();
                    									tradeItem.ID = ID;
                    									tradeItem.index = i;
                    									Rogue.clientWrapper.client.sendUDP(tradeItem);
                                                        ((Player) trade).getInventory().mutateWallet(-getItems().get(i).getValue());
                                                        ((Player) trade).getInventory().add(remove(i));
                                                }
                                        }
                                }
                        }
                        else{
                                setHover(getItems().get(i), collisions.get(i), i);
                        }
                    }
                }
                Global.screenShapes.end();
            Gdx.gl.glDisable(GL10.GL_BLEND);
        }
        
        public ArrayList<Rectangle> getCollision() {
                return collisions;
        }
        public void setCollisions(ArrayList<Rectangle> collisions) {
                this.collisions = collisions;
        }
        public void setHover(Item hover){
    		this.hover = hover;
    	}
        
        public boolean isItem(){
                if(hover == null)
                        return false;
                return true;
        }
        public void setHover(Item hover, Rectangle collision, int hoverIndex){
                this.hover = hover;
                this.hoverCollision = collision;
                this.hoverIndex = hoverIndex;
        }
        public Item getHover(){
        	return hover;
        }
        public int getHoverIndex(){
        	return hoverIndex;
        }
        
        public ArrayList<Item> getItems(){
                return items;
        }
        public Entity getTrade() {
    		return trade;
    	}

        public void setTrade(Entity trade) {
                this.trade = trade;
        }

		@Override
		public boolean keyDown(int keycode) {
			if(keycode >= Keys.A && keycode <= Keys.Z && (keycode == Keys.SHIFT_LEFT || keycode == Keys.SHIFT_RIGHT))
				return true;
			
			if(keycode == Keys.NUM_1)
				System.out.println("WASSUP");
			
			switch(keycode){
			case Keys.NUM_1:
				if(hover != null){
					if(((Player) trade).getInventory().checkIsFull(hover))
						((Player) trade).setAlert("Backpack is full!", true);
					else if(((Player) trade).getInventory().getWallet() - hover.getValue() < 0)
						((Player) trade).setAlert("Insufficient funds!", true);
					else{
						RemoveTradeItemPacket tradeItem = new RemoveTradeItemPacket();
						tradeItem.ID = ID;
						tradeItem.index = hoverIndex;
						Rogue.clientWrapper.client.sendUDP(tradeItem);
						((Player) trade).getInventory().mutateWallet(-hover.getValue());
						((Player) trade).getInventory().add(remove(hoverIndex));
					}
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
				((Player) trade).setMenu("");
				((Player) trade).getInventory().setTrade(null);
				Global.multiplexer.removeProcessor(((Player) trade).getInventory());
				trade = null;
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
			if(character >= 'A' && character - 'A' < items.size()){
				((Player) trade).getInventory().setHover(null);
				setHover(items.get(character - 'A'), collisions.get(character - 'A'), character - 'A');
			}
			return false;
		}

		@Override
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