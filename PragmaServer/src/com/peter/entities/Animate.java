package com.peter.entities;

import java.util.LinkedList;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector3;
import com.peter.server.PragmaServer;

public class Animate extends Entity{
	protected Stats stats;
	protected Responses response;
	protected int oldX, oldY;
	public boolean collision;
	protected static LinkedList<String> firstNames;
	protected static LinkedList<String> lastNames;
	protected float messageDelay = 0, statusDelay = 0, delay, time = 0;
	public boolean messageFlag, statusFlag;
	protected HostilityList list;
	protected String message;
	protected Integer status;
	protected String target;
	public static Vector3 pos = new Vector3();
	public Animate attacker;
	protected Sound death;
	//private static Scanner in;
	
	static{
		/*firstNames = new LinkedList<String>();
		lastNames = new LinkedList<String>();
		in = new Scanner(Gdx.files.internal("data/firstName.txt").readString());
		while(in.hasNextLine())
			firstNames.add(new String(in.nextLine()));
		in.close();
		in = new Scanner(Gdx.files.internal("data/lastName.txt").readString());
		while(in.hasNextLine())
			lastNames.add(new String(in.nextLine()));
		in.close();*/
	}
	
	public Animate(String type) {
		super(type);
		//list = new HostilityList();
		stats = new Stats();
		//response = new Responses(type);
		message = new String("");
		status = null;
		response = new Responses(getType());
	}
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

	
	
	public String getMessage(Integer callerID){
		if(PragmaServer.map.npcs.containsKey(callerID))
			return response.call((Animate) PragmaServer.map.npcs.get(callerID), false);
		else if(PragmaServer.map.players.containsKey(callerID))
			return response.call((Animate) PragmaServer.map.players.get(callerID), false);
		return null;
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