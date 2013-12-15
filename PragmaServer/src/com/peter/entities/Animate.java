package com.peter.entities;

import com.badlogic.gdx.math.Vector3;

public class Animate extends Entity{
	protected Stats stats;
	protected int oldX, oldY;
	public boolean collision;
	protected float messageDelay = 0, statusDelay = 0, delay, time = 0;
	public boolean messageFlag, statusFlag;
	protected String message;
	protected Integer status;
	protected String target;
	public static Vector3 pos = new Vector3();
	
	public Animate(String type) {
		super(type);
		stats = new Stats();
		//list = new HostilityList();
		//response = new Responses(type);
		message = new String("");
		status = null;
	}
	public Animate(){}
	
	public void update(double delta){
		time += delta;
		
		if(messageDelay > 2.0){
			resetMessage();
			messageDelay = 0;
			messageFlag = false;
		}
		
		if(getMessage() != ""){
			messageFlag = true;
			messageDelay += delta;
		}

		if(statusDelay > 1.6f){
			resetStatus();
			statusDelay = 0;
			statusFlag = false;
		}
		
		if(getStatus() != null){
			statusFlag = true;
			statusDelay += delta;
		}
	}

	public Stats getStats() {
		return stats;
	}

	public void setStats(Stats stats) {
		this.stats = stats;
	}
	
	
	public String getMessage() {
		return message;
	}
	
	public void resetMessage() {
		message = "";
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(int amount){
		if(!statusFlag)
			status = amount;
	}
	public void resetStatus() {
		status = null;
	}
	

	public void setDelays(int delay) {
		this.delay = delay;
		messageDelay = delay;
		statusDelay = delay;
		
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