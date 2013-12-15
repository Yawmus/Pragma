package com.peter.packets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.peter.entities.Animate;


public class MPPlayer extends Animate{
	
	private String pictureURL;
	
	public MPPlayer(String filename, String type, String name) {
		super(filename, type);
		this.name = name;
		stats.setDexterity(0);
		stats.setMaxExperience(20);
		stats.setExperience(0);
		stats.setLevel(1);
		stats.setStrength(0);
		stats.setHitpoints(20);
		stats.setMaxHitpoints(20);
	}
	public void draw(SpriteBatch spriteBatch){
		super.draw(spriteBatch);
		super.update(Gdx.graphics.getDeltaTime());
	}
	public void setMessage(String message){
		if(!messageFlag)
			this.message = message;
	}
	public void setPictureURL(String pictureURL) {
		this.pictureURL = pictureURL;
	}
	public String getPictureURL(){
		return pictureURL;
	}
}
