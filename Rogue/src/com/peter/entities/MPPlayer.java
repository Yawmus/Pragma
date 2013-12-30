package com.peter.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class MPPlayer extends Animate{
	
	private String pictureURL;
	
	public MPPlayer(String filename) {
		super(filename, "Player", "Human", null);
		symbol = '@';
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
