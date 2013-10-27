package com.peter.rogue.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.peter.rogue.Global;

public class NPC extends Animate {

	protected int move;
	
	public NPC(String filename, String type) {
		super(filename, type);
		name = firstNames.get(Global.rand(firstNames.size(), 0)) + " " + lastNames.get(Global.rand(lastNames.size(), 0));
		wait -= Global.rand(100, 0) * .01f;
	}

	public void draw(SpriteBatch spriteBatch){
		super.draw(spriteBatch);
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
		if(Global.renderer.getTile(getX(), getY()).isBlocked()){
			setX(oldX);
			setY(oldY);
		}
		
		if(getMapID(getX(), getY()) != "null" && getMapID(getX(), getY()) != getID()){
			if(!getMapObject(getX(), getY()).getType().equals("Chest") && !getMapObject(getX(), getY()).getType().equals("Item"))
				Global.data.sendMessage(getMapID(getX(), getY()), this);
			setX(oldX);
			setY(oldY);
		}
		
		setMap(oldX, oldY, nullEntry);
		setMap(getX(), getY(), this.entry);
		oldX = getX();
		oldY = getY();
	}
}
