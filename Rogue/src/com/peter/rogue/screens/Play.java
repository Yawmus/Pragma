package com.peter.rogue.screens;

import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL10;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import com.peter.entities.EntityManager;
import com.peter.map.Map;
import com.peter.rogue.Global;
import com.peter.rogue.Rogue;
import com.peter.rogue.network.ClientWrapper;

public class Play extends Listener implements Screen{

	//private Rogue game;
	
	private FPSLogger fps;
	
    private EntityManager manager;
    public static Scanner in;
	public static Map map;
	public static ClientWrapper clientWrapper;
    
    
	public Play(Rogue game){
		fps = new FPSLogger();
		fps.log();
		in = new Scanner(System.in);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

	    //fps.log();

		manager.draw(map.getSpriteBatch());

	}

	@Override
	public void resize(int width, int height) {
		Global.camera.viewportWidth = width;
		Global.camera.viewportHeight = height;
	}
	
	@Override
	public void show() {
		manager = new EntityManager(this);
		
		/*Global.camera.setToOrtho(false);    
		System.out.print("IP?: ");
		String ip = in.nextLine();
		if(!ip.equals(""))
			Global.IP = ip;*/

		map = new Map();
		clientWrapper = new ClientWrapper();
		manager.init();

		
		Log.set(Log.LEVEL_DEBUG);
	}
	

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
        Global.mapShapes.dispose();
        Global.screenShapes.dispose();
        map.getSpriteBatch().dispose();
		manager.dispose();
	}

}
