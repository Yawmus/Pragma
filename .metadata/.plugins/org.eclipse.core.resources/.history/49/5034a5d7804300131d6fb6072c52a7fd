package com.peter.rogue.lighting;

import com.badlogic.gdx.math.Vector2;

public class Rect 
{
	private Point upperLeft;
	private Point upperRight;
	private Point lowerLeft;
	private Point lowerRight;
	
	private Line top;
	private Line right;
	private Line bottom;
	private Line left;
	
	private float width;
	private float height;
	
	public Rect(){
		
	}
	
	public Rect(Point newLowerLeft, float newHeight, float newWidth){
		lowerLeft = newLowerLeft;
		height = newHeight;
		width = newWidth;
		
		lowerRight = new Point(lowerLeft.getX() + width, lowerLeft.getY());
		upperLeft = new Point(lowerLeft.getX(), lowerLeft.getY() + height);
		upperRight = new Point(lowerLeft.getX() + width, lowerLeft.getY() + height);
		
		top = new Line(upperLeft, upperRight);
		bottom = new Line(lowerLeft, lowerRight);
		left = new Line(lowerLeft, upperLeft);
		right =  new Line(lowerRight, upperRight);
	}
	
	public boolean overlaps(Rect other){
		return false;
	}
	
	public void setXY(float x, float y){
		upperLeft = new Point(x, y);
		upperRight = new Point(upperLeft.getX() + width, upperLeft.getY());
		lowerLeft = new Point(upperLeft.getX(), upperLeft.getY() + height);
		lowerRight = new Point(upperLeft.getX() + width, upperLeft.getY() + height);
	}
	public boolean does_line_intersect(Line other){
		
		if(top.is_intersected(other) || bottom.is_intersected(other) ||
		   left.is_intersected(other) || right.is_intersected(other))
		{
			return true;
		}else
		{
			return false;
		}
	}
	
	/*
	 * List of GET functions.
	 */
	public float getX()
	{
		return lowerLeft.getX();
	}
	public float getY()
	{
		return lowerRight.getY();
	}
	public Vector2 getPositionV(){
		return new Vector2(upperLeft.getX(), upperLeft.getY());
	}
	public Point getUpperLeft(){
		return upperLeft;
	}
	public Point getUpperRight(){
		return upperRight;
	}
	public Point getLowerLeft(){
		return lowerLeft;
	}
	public Point getLowerRight(){
		return lowerRight;
	}
	
	public Line getLineTop(){
		return top;
	}
	public Line getLineRight(){
		return right;
	}
	public Line getLineBottom(){
		return bottom;
	}
	public Line getLineLeft(){
		return left;
	}
	
	public float get_width(){
		return width;
	}
	public float get_height(){
		return height;
	}
}
