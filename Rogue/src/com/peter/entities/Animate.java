package com.peter.entities;

import java.util.LinkedList;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector3;

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
	protected Animate attacker;
	protected Sound death;
	
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
	
	public Animate(String filename, String type) {
		super(filename, type);
		list = new HostilityList();
		stats = new Stats();
		response = new Responses(type);
		message = new String("");
		status = null;
	}
	public void update(float delta){
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
	@Override
	public void setPosition(float x, float y){
		setX(x * tileWidth);
		setY(y * tileHeight);

		oldX = (int) getX();
		oldY = (int) getY();
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

	/*protected void attack(Animate entity){
		int amount = 0;
		if(this.getStats().getStrength() == 0)
			amount = Global.rand(3, 0);
		else
			amount = Global.rand(this.getStats().getStrength(), 0);
		amount -= entity.getStats().getDefense();
		if(amount < 0)
			amount = 0;
		amount *= -1;
		entity.getStats().mutateHitpoints(amount);
		
		if(this instanceof Player)
			entity.attacker = this;
		entity.setStatus(amount);
		bump(entity);
		if(entity.getStats().getHitpoints() <= 0){
			Entity.map.setMark("", entity.getX(), entity.getY());
			stats.addExperience(entity.type);
			if(entity instanceof NPC){
				Play.map.npcs.remove(entity);
				Play.map.remove(entity.getID());
				Play.map.items.add(((NPC) entity).getDrop());
				Play.map.items.get(Play.map.items.size()-1).setPosition(entity.getX()/32, entity.getY()/32);
			}
			else if(entity instanceof Player){
				Global.gameOver = true;
				entity.death.play();
			}
		}
	}*/

	protected void bump(Animate entity){
		entity.setMessage(this);
		collision = true;
	}
	
	public void setMessage(Animate entity){
		if(entity instanceof Player && ((Player)(entity)).isHostile() && !list.check(entity))
			list.addID(entity.getID());
		if(!messageFlag)
			// Uncomment for player responses
			/*if(type.equals("Player"))
				message = response.call(entity, entity.list.check(this));
			else*/
				message = response.call(entity, list.check(entity));
	}
	

	public void setDelays(int delay) {
		this.delay = delay;
		messageDelay = delay;
		statusDelay = delay;
		
	}
	
	public void setOldX(int oldX){
		this.oldX = oldX;
	}
	
	public void setOldY(int oldY){
		this.oldY = oldY;
	}
}