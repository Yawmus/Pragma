package com.peter.entities;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.peter.inventory.Chest;
import com.peter.inventory.Inventory;
import com.peter.inventory.Item;
import com.peter.map.Tile;
import com.peter.packets.PlayerPacket;
import com.peter.packets.RemoveItemPacket;
import com.peter.rogue.Global;
import com.peter.rogue.screens.Play;

public class Player extends Animate implements InputProcessor {
	
	private Texture picture;
	private String info;
	private String menu = "null";
	private boolean menuActive = false;
	private Inventory inventory = new Inventory();
	private ArrayList<Ray> rays = new ArrayList<Ray>();
	private Entity menuObject;
	private int viewDistance;
	private boolean hostile;
	private float hunger;
	public PlayerPacket packet;
	//public ClientWrapper clientWrapper2 = new ClientWrapper();
	
	public Player(String filename){
		super(filename, "Player");
		messageFlag = false;
		name = "Adelaide";
		picture = new Texture(Gdx.files.internal("img/adelaide.png"));
		death = Gdx.audio.newSound(Gdx.files.internal("sound/death.wav"));
		packet = new PlayerPacket();
		info = new String();
		viewDistance = 7;
		hostile = false;
		stats.setDexterity(0);
		stats.setMaxExperience(20);
		stats.setExperience(0);
		stats.setLevel(1);
		stats.setStrength(0);
		stats.setHitpoints(20);
		stats.setMaxHitpoints(20);
		hunger = 1.01f;
		delay = .2f;
		for(int i=0; i<100; i++){
			rays.add(new Ray(new Vector3(0, 0, 0), new Vector3(0, 0, 0)));
		}
	}

	public void draw(SpriteBatch spriteBatch){
		super.draw(spriteBatch);
		update(Gdx.graphics.getDeltaTime());
		canDraw = true;
	}
	
	public Inventory getInventory(){
		return inventory;
	}
	
	public Texture getPicture(){
		return picture;
	}
	
	public void update(float delta){
		super.update(delta);
		hunger -= delta/8000;
		if(time >= delay){
			if(Gdx.input.isKeyPressed(Keys.A)){
				setX(getX() - 32);
				time = 0;
				setMenuActive(false);
				checkCollision();
			}
			if(Gdx.input.isKeyPressed(Keys.D)){
				setX(getX() + 32);
				time = 0;
				setMenuActive(false);
				checkCollision();
			}
			if(Gdx.input.isKeyPressed(Keys.S)){
				setY(getY() - 32);
				time = 0;
				setMenuActive(false);
				checkCollision();
			}
			if(Gdx.input.isKeyPressed(Keys.W)){
				setY(getY() + 32);
				time = 0;
				setMenuActive(false);
				checkCollision();
			}

			
		}
	}
	
	public void checkCollision(){
		collision = false;
		if(Play.map.getTile(getX(), getY()).isBlocked())
			collision = true;
		else if(Play.map.getTile(getX(), getY()).hasStairs() && !Play.map.getTile(oldX, oldY).hasStairs()){
			if(Play.map.getTile(getX(), getY()).direction()){
			}
		}/*
		if(isHostile())
			attack((Animate) map.get(getX(), getY()));
		else{
			if(map.get(getX(), getY()) instanceof Shopkeep){
				setMenu("Barter");
				setMenuObject((Shopkeep)map.get(getX(), getY()));
			}
			bump((Animate) map.get(getX(), getY()));
		}
		}*/
		if(!(Play.map.marks.get((int)getX(), (int)getY()) == -1 || Play.map.marks.get((int)getX(), (int)getY()) == ID)){
			if(Play.map.npcs.get(Play.map.marks.get((int)getX(), (int)getY())) instanceof NPC){
				collision = true;
			}
			else if(Play.map.items.get(Play.map.marks.get((int)getX(), (int)getY())) instanceof Item){
				Item temp = Play.map.items.get(Play.map.marks.get((int)getX(), (int)getY()));
				if(!inventory.checkIsFull(temp)){
					Play.map.items.remove(temp.ID);
					Play.map.marks.put(-1, (int) temp.getX(), (int) temp.getY());
					RemoveItemPacket item = new RemoveItemPacket();
					item.ID = temp.ID;
					item.x = (int) temp.getX();
					item.y = (int) temp.getY();
					Play.clientWrapper.client.sendUDP(item);
					inventory.add(temp);
				}
				else
					collision = true;
			}
			else if(Play.map.chests.get(Play.map.marks.get((int)getX(), (int)getY())) instanceof Chest){
				setMenu("Chest");
				setMenuObject((Chest) Play.map.chests.get(Play.map.marks.get((int)getX(), (int)getY())));
				collision = true;
			}
			else
				collision = true;
		}
		if(collision){
			setX(oldX);
			setY(oldY);
		}
		else{
			packet.oldX = (int) oldX;
			packet.oldY = (int) oldY;
			Play.map.marks.put(-1, oldX, oldY);
			packet.x = (int) getX();
			packet.y = (int) getY();
			Play.map.marks.put(ID, (int)getX(), (int)getY());
			Play.clientWrapper.client.sendUDP(packet);
		}
		oldX = (int) getX();
		oldY = (int) getY();
	}
	
    
    public void light(){
		//Global.mapShapes.begin(ShapeType.Line);
		//Global.mapShapes.setColor(Color.YELLOW);
		
		for(int i=0; i<rays.size(); i++){
			rays.get(i).set(new Vector3(getX()+getWidth()/2, getY()+getHeight()/2, 0), 
					        new Vector3((float)(32 * getViewDistance()*Math.cos((2*Math.PI*i)/rays.size()) + getX()+getWidth()/2), 
					        		    (float)(32 * getViewDistance()*Math.sin((2*Math.PI*i)/rays.size()) + getY()+getHeight()/2), 0f));
    		Play.map.getSpriteBatch().begin();
			rays.get(i).direction.set(intersect(rays.get(i)));
			Play.map.getSpriteBatch().end();
    		
			//Global.mapShapes.line(rays.get(i).origin, rays.get(i).direction);
		}
		
		//Global.mapShapes.end();
    }
    
    public Vector3 intersect(Ray ray){
    	float x, y, splits = 8; // Perfect number for splits and viewDistance to not have wall nonsense
    	for(int i=1; i<=splits; i++){
    		x = ray.origin.x + (((ray.direction.x - ray.origin.x)*i)/splits);
    		y = ray.origin.y + (((ray.direction.y - ray.origin.y)*i)/splits);
    		Tile tile = Play.map.getTile(x, y);
    		Play.map.setVisible(x, y, "visited");
	    	if(!tile.canSee()){
	    		return new Vector3(ray.origin.x + (((ray.direction.x - ray.origin.x)*i)/splits), 
	    				           ray.origin.y + (((ray.direction.y - ray.origin.y)*i)/splits), 0);
	    	}
	    	// Render entities when in sight
	    	/*else if(!(Play.map.getMark(x, y).equals(ID) || Play.map.getMark(x, y).equals("")))
	    		Play.map.get(x, y).canDraw = true;*/
    	}
    	return ray.direction;
    }
	
	
	@Override
	public boolean keyDown(int keycode) {
		switch(keycode){
		case Keys.G:
			setMenu("Inventory");
			break;
		case Keys.F:
			hostile = !hostile;
			break;
		case Keys.TAB:
			Global.camera.zoom += .4;
			break;
		case Keys.ESCAPE:
			System.exit(0);
			break;
		case Keys.SHIFT_LEFT:
		case Keys.SHIFT_RIGHT:
			delay = .5f;
			break;
		}
		
		return true;
	}


	@Override
	public boolean keyTyped(char character) {
		return false;
	}
	
	
	@Override
	public boolean scrolled(int amount) {
		return true;
	}
	
	public void setInformation(String info){
		this.info = info;
	}
	
	public String getInformation(){
		return info;
	}
	
	public void setMenu(String request){
		menu = request;
		menuObject = null;
		menuActive = !menuActive;
	}
	
	public void setMenuActive(boolean menuActive){
		this.menuActive = menuActive;
	}
	
	public String getMenu(){
		return menu;
	}
	
	public boolean isMenuActive(){
		return menuActive;
	}
	
	public void setMenuObject(Entity object){
		menuObject = object;
	}
	
	public Entity getMenuObject(){
		return menuObject;
	}
	
	public int getViewDistance() {
		return viewDistance;
	}
	
	public boolean isHostile() {
		return hostile;
	}

	public void setHostility(boolean hostile) {
		this.hostile = hostile;
	}
	

	@Override
	public boolean keyUp(int keycode) {
		switch(keycode){
		case Keys.TAB:
			Global.camera.zoom -= .4;
			break;
		case Keys.SHIFT_LEFT:
		case Keys.SHIFT_RIGHT:
			delay = .2f;
			break;
		}
		return false;
	}


	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (button == Buttons.LEFT) {
			return true;
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int x, int y) {
		return true;
	}

	public float getHunger() {
		return hunger;
	}

	public void mutateHunger(float amount) {
		if(amount + this.hunger > 1.01f)
			this.hunger = 1.01f;
		else
			this.hunger += amount;
	}
}