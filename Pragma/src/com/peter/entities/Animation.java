package com.peter.entities;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Animation {
	public ArrayList<Texture> frames;
	
	public Animation(){
		frames = new ArrayList<Texture>();
	}
	
	public void load(String type, int x, int y){
		if(type.equals("death")){
			frames.add(new Texture(Gdx.files.internal("anim/frame.png")));
			frames.add(new Texture(Gdx.files.internal("anim/frame2.png")));
			frames.add(new Texture(Gdx.files.internal("anim/frame3.png")));
			//(int) Play.map.npcs.get(packet.ID).getX(), (int) Play.map.npcs.get(packet.ID).getY()
		}
		//frames.add(frame);
	}
	
	public void play(SpriteBatch spriteBatch, int x, int y){
		for(int i=0; i<frames.size(); i++)
			spriteBatch.draw(frames.get(i), x, y);
		
	}
}
