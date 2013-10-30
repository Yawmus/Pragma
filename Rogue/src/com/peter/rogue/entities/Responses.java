package com.peter.rogue.entities;

import com.peter.rogue.Global;

public class Responses{
	
	private String receiver;
	private int dice;
	
	public Responses(String type) {
		this.receiver = type;
	}

	public String call(Animate caller, boolean hostile){
		if(receiver == "Player"){
			return Player(caller, hostile);
		}
		if(receiver == "Shopkeep")
			return Shopkeep(caller, hostile);
		if(receiver == "Citizen")
			return Citizen(caller, hostile);
		
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
	
	private String Player(Animate caller, boolean hostile){
		dice = Global.rand(8, 0);
		if(hostile)
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
		return "";
	}
	
	private String Shopkeep(Animate caller, boolean hostile){
		dice = Global.rand(4, 0);
		if(hostile)
			switch(dice){
			case 0:
				return "Heavens!";
			case 1:
				return "Help!";
			case 2:
				return "Guards!";
			case 3:
				return "By the nine!";
			}
		if(caller.getType() == "Worm")
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
		switch(dice){
		case 0:
			return "I have the best prices in town!";
		case 1:
			return "Greetings " + caller.getType() + "!";
		case 2:
			return "Have you taken a look at my fine wares?";
		case 3:
			return "Looks like you need to retire.";
		}
		return "";
	}
	
	private String Citizen(Animate caller, boolean hostile){
		dice = Global.rand(4, 0);
		if(hostile)
			switch(dice){
			case 0:
				return "Arrgghh!";
			case 1:
				return "Help!";
			case 2:
				return "Guards!";
			case 3:
				return "Swine!";
			}
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
		return "";
	}
}



