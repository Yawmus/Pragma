package com.peter.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.peter.rogue.Global;

public class NPC extends Animate {

	protected int move;
	protected boolean canMove;
	public int level;
	
	public NPC(String filename, int level, String race, String type, String name) {
		super(filename, race, type, name);
		delay -= Global.rand(100, 0) * .01f;
		canMove = true;
		delay = 2.6f;
		this.name = name;
		this.level = level;
	}

	public void draw(SpriteBatch spriteBatch){
		super.draw(spriteBatch);
		super.update(Gdx.graphics.getDeltaTime());
	}
}
