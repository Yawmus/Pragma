package com.peter.entities;


public class Animate extends Entity{
	protected Stats stats;
	protected int oldX, oldY;
	public boolean collision;
	protected float messageDelay = 0, statusDelay = 0, delay, time = 0;
	
	public Animate(String type) {
		super(type);
		stats = new Stats();
		//list = new HostilityList();
		//response = new Responses(type);
	}
	public Animate(){}
	
	public void update(double delta){
		time += delta;
	}

	public Stats getStats() {
		return stats;
	}

	public void setStats(Stats stats) {
		this.stats = stats;
	}

	public void setPosition(int x, int y){
		this.x = x*32;
		this.y = y*32;
		oldX = this.x;
		oldY = this.y;
	}
	public void setOldX(int oldX){
		this.oldX = oldX;
	}
	
	public void setOldY(int oldY){
		this.oldY = oldY;
	}
}