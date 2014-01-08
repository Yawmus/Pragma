package com.peter.rogue;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.peter.rogue.network.ClientWrapper;
import com.peter.rogue.screens.Death;
import com.peter.rogue.screens.Play;

public class Rogue extends Game{
	
	public static final String TITLE = "Pragma", VERSION = "v0.8.3.8-alpha";

	Play play;
	public static ClientWrapper clientWrapper;
	
	@Override
	public void create() {

		Global.camera.setToOrtho(false);    
		/*System.out.print("IP?: ");
		String ip = in.nextLine();
		if(!ip.equals(""))
			Global.IP = ip;*/
		Global.IP = "209.213.46.189";
		/*NameGenerator name = new NameGenerator();
		for(int i=0; i<25; i++)
			System.out.println(name.generateName());*/
		setScreen(new Play(this));
		
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void render() {
		super.render();
		
		if(Global.gameOver){
			setScreen(new Death(this));
		    if(Gdx.input.isKeyPressed(Keys.ENTER)){
		    	Global.gameOver = false;
		    	setScreen(new Play(this));
		    }
		}
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}
}
