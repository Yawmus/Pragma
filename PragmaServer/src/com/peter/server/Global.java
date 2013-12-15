package com.peter.server;

import java.util.Random;

public class Global {
	
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    
	public static Integer count = 1000;
    
    public static String IP = "localhost";
	
    private static Random generator = new Random();
    
    public static int rand(int range, int origin) {
    	return generator.nextInt(range) + origin;
    }
}