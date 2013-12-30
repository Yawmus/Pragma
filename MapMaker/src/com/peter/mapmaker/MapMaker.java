package com.peter.mapmaker;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class MapMaker extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel panel = new JPanel();
	private JButton button = new JButton("Click");
	private Entry[][] map = new Entry[WIDTH][HEIGHT];
	private JRadioButton test;
	private static final int MAP_X = 8, MAP_Y = 32;
	private static final int WIDTH = 10, HEIGHT = 10;
	private static final int SIZE = 32;
	
	MapMaker(){
		super("Map Maker!");
		setBounds(500, 300, 800, 500);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    for(int i=0; i<WIDTH; i++)
	    	for(int j=0; j<HEIGHT; j++){
	    		map[i][j] = new Entry();
	    		map[i][j].collision.x = (i * SIZE);
	    		map[i][j].collision.y = (j * SIZE);
	    		map[i][j].collision.width = SIZE;
	    		map[i][j].collision.height = SIZE;
	    	}
	    test = new JRadioButton("Hi");
	    Container container = this.getContentPane();
	    container.add(panel);
	    panel.add(test);
	    test.requestFocus();
	    panel.addMouseListener(new MouseListener(){
	    	@Override
	    	public void mouseClicked(MouseEvent event){
	    	    			
	    	}

	    	@Override
	    	public void mousePressed(MouseEvent event) {
	    		for(int i=0; i<WIDTH; i++)
	    	    	for(int j=0; j<HEIGHT; j++)
	    	    		if(map[i][j].collision.contains(event.getX(), event.getY())){
	    	    			map[i][j].data = 'f';
	    	    			System.out.println("Changed");
	    	    			repaint(panel.getGraphics());
	    	    		}
	    	}

	    	@Override
	    	public void mouseReleased(MouseEvent event) {
	    		
	    	}

	    	@Override
	    	public void mouseEntered(MouseEvent event) {
	    		
	    	}

	    	@Override
	    	public void mouseExited(MouseEvent event) {
	    		
	    	}
	    });
	    
	    setVisible(true);
	}
	public void paint(Graphics g) {
	    g.setColor(Color.BLACK);
	    for(int x=0; x<WIDTH; x++)
	    	for(int y=0; y<HEIGHT; y++){
	    		g.drawRect(map[x][y].collision.x + MAP_X, map[x][y].collision.y + MAP_Y, SIZE, SIZE);
	    	    System.out.println(map[x][y].data);
	    	}
	}
	public void repaint(Graphics g){
	    for(int x=0; x<WIDTH; x++)
	    	for(int y=0; y<HEIGHT; y++){
	    		if(map[x][y].data != ' '){
	    			g.drawString(map[x][y].data.toString(), map[x][y].collision.x + MAP_X, map[x][y].collision.y + MAP_Y);
	    		}
	    	}
	}
	
	public static void main(String[] args){
		new MapMaker();
		System.out.println("Hi");
	}
}

class Entry{
	public Rectangle collision;
	public Character data;
	
	public Entry(){
		collision = new Rectangle();
		data = ' ';
	}
}
