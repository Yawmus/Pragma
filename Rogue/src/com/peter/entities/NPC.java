package com.peter.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.peter.rogue.Global;

public class NPC extends Animate {

	protected int move;
	protected boolean canMove;
	
	public NPC(String filename, String type) {
		super(filename, type);
		name = "Bob"/*firstNames.get(Global.rand(firstNames.size(), 0)) + " " + lastNames.get(Global.rand(lastNames.size(), 0))*/;
		delay -= Global.rand(100, 0) * .01f;
		canMove = true;
		delay = 2.6f;
	}

	public void draw(SpriteBatch spriteBatch){
		super.draw(spriteBatch);
		super.update(Gdx.graphics.getDeltaTime());
	}
	
}
