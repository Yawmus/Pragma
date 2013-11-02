package com.peter.rogue.lighting;

public class Triangle 
{
	public Point a;
	public Point b;
	public Point c;
	
	private Line sideA;
	private Line sideB;
	private Line sideC;
	
	public Triangle(Line a, Line b, Line c)
	{
		sideA = a;
		sideB = b;
		sideC = c;
	}
	
	public Triangle(Point a, Point b, Point c)
	{
		this.a = a;
		this.b = b;
		this.c = c;
		
		sideA = new Line(a, b);
		sideB = new Line(b, c);
		sideC = new Line(a, c);
	}
}
