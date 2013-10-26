package com.peter.rogue.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector3;
import com.peter.rogue.Global;

public class NPC extends Animate {

	protected int move;
	
	private MapProperties keys = new MapProperties();
	
	public NPC(String filename, String type) {
		super(filename, type);
		name = firstNames.get(Global.rand(firstNames.size(), 0)) + " " + lastNames.get(Global.rand(lastNames.size(), 0));
		wait -= Global.rand(100, 0) * .01f;
	}

	public void draw(SpriteBatch spriteBatch){
		super.draw(spriteBatch);
		pos = new Vector3(getX(), getY(), 0);
		Global.camera.project(pos);
		spriteBatch.end();
		
		if(messageFlag){
			Global.shapeRenderer.begin(ShapeType.Filled);
			Global.shapeRenderer.setColor(0, 0, 0, 1f);
			Global.shapeRenderer.rect(pos.x, pos.y - 17, font.getBounds(getMessage()).width, font.getLineHeight());
			Global.shapeRenderer.end();
		}
		if(statusFlag){
			Global.shapeRenderer.begin(ShapeType.Filled);
			Global.shapeRenderer.setColor(.4f, 0f, 0f, 1f);
			Global.shapeRenderer.circle(pos.x, pos.y + 20, font.getBounds(getStatus().toString()).width);
			Global.shapeRenderer.end();
		}
		
		spriteBatch.begin();
		Entity.font.draw(spriteBatch, getMessage(), getX(), getY());
		if(getStatus() != 0){
			//Entity.font.setColor(0, 0, 0, 1f);
			Entity.font.draw(spriteBatch, getStatus().toString(), getX() - 8, getY() + 26);
			//Entity.font.setColor(1f, 1f, 1f, 1f);
		}
		update(Gdx.graphics.getDeltaTime());
	}
	
	public void update(float delta){
		wait += Gdx.graphics.getDeltaTime();
		
		move = Global.rand(5, 0);
		if(wait >= .6f + delay){
			switch(move){
			case 0:
				setY(getY() + 32);
				wait = 0;
				break;
			case 1:
				setY(getY() - 32);
				wait = 0;
				break;
			case 2:
				setX(getX() - 32);
				wait = 0;
				break;
			case 3:
				setX(getX() + 32);
				wait = 0;
				break;
			case 4:
				wait = 0;
			}
			checkCollision();
		}

		if(messageDelay > 2.0){
			resetMessage();
			messageDelay = 0;
			messageFlag = false;
		}
		
		if(getMessage() != ""){
			messageFlag = true;
			messageDelay += Gdx.graphics.getDeltaTime();
		}
		
		if(statusDelay > 2.0){
			resetStatus();
			statusDelay = 0;
			statusFlag = false;
		}
		
		if(getStatus() != 0){
			statusFlag = true;
			statusDelay += Gdx.graphics.getDeltaTime();
		}
		
	}
	
	public void checkCollision(){
		keys = collisionLayer.getCell((int) (getX() / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties();
		
		if("1".equals(keys.get("blocked"))){
			setX(oldX);
			setY(oldY);
		}
		
		if(getMapID(getY(), getX()) != "null" && getMapID(getY(), getX()) != getID()){
			if(!getMapObject(getY(), getX()).getType().equals("Chest") && !getMapObject(getY(), getX()).getType().equals("Item"))
				Global.data.sendMessage(getMapID(getY(), getX()), this);
			setX(oldX);
			setY(oldY);
		}
		
		setMap((int)oldY, (int)oldX, nullEntry);
		setMap((int)getY(), (int)getX(), this.entry);
		
		oldX = getX();
		oldY = getY();
	}
	
	
}
