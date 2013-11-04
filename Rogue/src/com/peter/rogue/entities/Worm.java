package com.peter.rogue.entities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.peter.rogue.Global;

public class Worm extends Monster{

	private float multiplyDelay;
	private boolean fertile;
	
	public Worm(String filename) {
		super(filename, "Worm");
		
		stats.setLevel(1);
		stats.setHitpoints(20);
		stats.setDexterity(5);
		stats.setStrength(2);
		stats.setExperience(0);

		message = "*Gurgle*";

		list.addType("Citizen");
		list.addType("Shopkeep");
		list.addType("Player");
		
		multiplyDelay = Global.rand(6, 0);
		
		if(Global.rand(3, 0) == 0)
			fertile = true;
		else
			fertile = false;
	}
	
	public void draw(SpriteBatch spriteBatch){
		super.draw(spriteBatch);
		update(Gdx.graphics.getDeltaTime());
	}
	
	
	public void update(float delta){
		super.update(delta);
		
		if(fertile){
			multiplyDelay += delta;
			
			if(multiplyDelay >= 35){
				multiplyDelay = 0;
				multiply();
			}
		}
	}
	
	public void multiply(){
		npcs.add(new Worm("tilda.png"));
		npcs.get(npcs.size()-1).setPosition(Global.rand(3, (int)(this.getX()/32)-1), Global.rand(3, (int)(this.getY()/32)-1));
		if(attacker != null)
			npcs.get(npcs.size()-1).attacker = attacker;
	}
}

