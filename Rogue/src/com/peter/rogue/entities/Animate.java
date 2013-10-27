package com.peter.rogue.entities;

import java.util.LinkedList;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.peter.rogue.Global;

public class Animate extends Entity{
	protected Stats stats;
	private Response response;
	protected float oldX, oldY;
	protected static Scanner in;
	protected static LinkedList<String> firstNames;
	protected static LinkedList<String> lastNames;
	protected float wait = 0, messageDelay = 0, statusDelay = .6f, delay = 1;
	protected String message;
	public boolean messageFlag;
	protected Integer status;
	public boolean statusFlag;
	protected boolean hostile;
	protected String target;
	public static Vector3 pos = new Vector3();
	
	static{
		firstNames = new LinkedList<String>();
		lastNames = new LinkedList<String>();
		in = new Scanner(Gdx.files.internal("data/firstName.txt").readString());
		while(in.hasNextLine())
			firstNames.add(new String(in.nextLine()));
		in.close();
		in = new Scanner(Gdx.files.internal("data/lastName.txt").readString());
		while(in.hasNextLine())
			lastNames.add(new String(in.nextLine()));
		in.close();
	}
	
	public Animate(String filename, String type) {
		super(filename, type);
		animate = true;
		stats = new Stats();
		response = new Response(type);
		message = new String("");
		status = new Integer(0);
	}
	
	@Override
	public void setPosition(float x, float y){
		setX(x * tileWidth);
		setY(y * tileHeight);
		oldX = getX();
		oldY = getY();
	}

	public Stats getStats() {
		return stats;
	}

	public void setStats(Stats stats) {
		this.stats = stats;
	}

	public boolean isHostile() {
		return hostile;
	}

	public void setHostility(boolean hostile) {
		this.hostile = hostile;
	}
	
	public void remove(){
		setMap(getX(), getY(), nullEntry);
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(Animate caller){
		if(!messageFlag)
			message = response.call(caller);
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
		status = 0;
	}
}

class Response{
	
	private String receiver;
	//private LinkedList<LinkedList<String>> responses;
	private int dice;
	
	public Response(String type) {
		//responses = new LinkedList<LinkedList<String>>();
		//in = new Scanner(Gdx.files.internal("data/firstName.txt").readString());
		//if(type == "Player")
		this.receiver = type;
	}

	public String call(Animate caller){
		if(receiver == "Player"){
			dice = Global.rand(8, 0);
			switch(dice){
			case 0:
				return "Watch it bitch.";
			case 1:
				return "Keep to yourself, bud.";
			case 2:
				return "Touch me again and I'll cut you.";
			case 3:
				return "Get out of my face!";
			case 4:
				return "You're going to regret that.";
			case 5:
				return "I'll gut you like a fish.";
			case 6:
				return "Back off motherfucker!";
			case 7:
				return "Shove it up your ass.";
			}
		}
		if(receiver == "Shopkeep" || receiver == "Citizen"){
			dice = Global.rand(4, 0);
			if(caller.getType() == "worm")
				switch(dice){
				case 0:
					return "Eww, a " + caller.getType() + "!";
				case 1:
					return "Back vile beast!";
				case 2:
					return "How revolting!";
				case 3:
					return "Do you want to come home with me?";
				}
			if(caller.isHostile())
				return "Ouch!";
			switch(dice){
			case 0:
				return "Hello " + caller.getType() + "!";
			case 1:
				return "Greetings " + caller.getType() + "!";
			case 2:
				return "How goes it " + caller.getType() + "?";
			case 3:
				return "Nice day isn't it?";
			}
		}
		if(receiver == "Worm"){
			dice = Global.rand(2, 0);
			switch(dice){
			case 0:
				return "*Gurgle*";
			case 1:
				return "*Screech*";
			}
		}
		
		return "I don't have a response programmed";
	}
}
