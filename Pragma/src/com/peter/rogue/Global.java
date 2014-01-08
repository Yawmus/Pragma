package com.peter.rogue;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Global {
	
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    
    private static FreeTypeFontGenerator gothicFontGen;
    public static BitmapFont gothicFont10, gothicFont20, gothicFont30;
    
	public static OrthographicCamera camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	public static ShapeRenderer screenShapes = new ShapeRenderer();
	public static ShapeRenderer mapShapes = new ShapeRenderer();
    public static SpriteBatch screen = new SpriteBatch();
    public static boolean gameOver = false;
    public static BitmapFont font = new BitmapFont();
    //public static BitmapFont gothicFont = new BitmapFont(Gdx.files.internal("fonts/Cardinal.fnt"), Gdx.files.internal("fonts/Cardinal.png"), false);
	public static InputMultiplexer multiplexer = new InputMultiplexer();
    public static String IP = "localhost";
    
    private static Random generator = new Random();
    
    static{
    	gothicFontGen = new FreeTypeFontGenerator(Gdx.files.internal("font/Cardinal.ttf"));
    	gothicFont10 = gothicFontGen.generateFont(10);
    	gothicFont20 = gothicFontGen.generateFont(20);
    	gothicFont30 = gothicFontGen.generateFont(30);
    }
    
    public static int rand(int range, int origin) {
    	return generator.nextInt(range) + origin;
    }
}