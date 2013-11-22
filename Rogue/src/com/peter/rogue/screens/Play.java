package com.peter.rogue.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.peter.rogue.Global;
import com.peter.rogue.Rogue;
import com.peter.rogue.entities.Entity;
import com.peter.rogue.entities.EntityManager;

public class Play implements Screen{

	//private Rogue game;
	
	private FPSLogger fps;
	
    private EntityManager manager;
	Table table2 = new Table();
    
    
	public Play(Rogue game){
	//	this.game = game;
		fps = new FPSLogger();
		fps.log();
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

	    fps.log();
	    
		manager.draw();
		
		Gdx.gl.glDisable(GL10.GL_BLEND);
	}

	@Override
	public void resize(int width, int height) {
		Global.camera.viewportWidth = width;
		Global.camera.viewportHeight = height;
	}
	
	@Override
	public void show() {
		manager = new EntityManager();
		manager.init();

		Global.camera.setToOrtho(false);        
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
        Entity.map.getSpriteBatch().dispose();
		manager.dispose();
	}

}
