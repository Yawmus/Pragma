package com.peter.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.peter.inventory.Chest;
import com.peter.inventory.Inventory;
import com.peter.inventory.Item;
import com.peter.map.Tile;
import com.peter.packets.AttackPacket;
import com.peter.packets.PlayerPacket;
import com.peter.packets.RemoveItemPacket;
import com.peter.packets.RemovePlayerPacket;
import com.peter.packets.RequestFloorPacket;
import com.peter.rogue.Global;
import com.peter.rogue.Rogue;
import com.peter.rogue.network.Chat;
import com.peter.rogue.screens.Play;

public class Player extends Animate implements InputProcessor {
	
	private Texture picture;
	private String info;
	private String menu = "";
	private boolean menuActive = false;
	private Inventory inventory = new Inventory(this);
	private ArrayList<Ray> rays = new ArrayList<Ray>();
	private Entity menuObject;
	private int viewDistance;
	private boolean hostile, error;
	private String pictureURL;
	private float hunger;
	public PlayerPacket packet;
	public boolean showPlayers = false;
	private String alert;
	private float alertDelay = 0;
	private Color color;
	private ArrayList<Entity> visible;
	protected Stats stats;
	private Chat chat;
	
	public Player(String filename){
		super(filename, "Player", "Human", null);
		symbol = '@';
		stats = new Stats();
		messageFlag = false;
		visible = new ArrayList<Entity>();
		name = "Adelaide";
		pictureURL = "img/adelaide.png";
		picture = new Texture(Gdx.files.internal("img/adelaide.png"));
		deathSound = Gdx.audio.newSound(Gdx.files.internal("sound/death.wav"));
		packet = new PlayerPacket();
		info = alert = new String();
		viewDistance = 7;
		hostile = false;
		chat = new Chat(this);
		stats.setDexterity(0);
		stats.setMaxExperience(20);
		stats.setExperience(0);
		stats.setLevel(1);
		stats.setStrength(10);
		stats.setHitpoints(20);
		stats.setMaxHitpoints(20);
		hunger = 1.01f;
		delay = .2f;
		color = new Color(Color.WHITE);
		/*switch(Global.rand(8, 0)){
		case 0:
			color = new Color(.7f, .7f, .7f, 1f); // gray
			break;
		case 1:
			color = new Color(.6f, .3f, .8f, 1f); // purple
			break;
		case 2:
			color = new Color(.7f, 1f, 1f, 1f); // aqua
			break;
		case 3:
			color = new Color(1f, .8f, .4f, 1f); // oj
			break;
		case 4:
			color = new Color(1f, 1f, .5f, 1f); // yella
			break;
		case 5:
			color = new Color(0f, 1f, .5f, 1f); // yella
			break;
		case 6:
			color = new Color(1f, 0f, .5f, 1f); // yella
			break;
		case 7:
			color = new Color(1f, .5f, 0f, 1f); // yella
			break;
			
		}*/
		setColor(color);
		
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


	public Stats getStats() {
		return stats;
	}

	public void setStats(Stats stats) {
		this.stats = stats;
	}
	
	public void update(float delta){
		super.update(delta);

		if(alertDelay > 2){
			setAlert("", true);
			alertDelay = 0;
		}
		
		if(getAlertMessage() != ""){
			alertDelay += delta;
		}
		hunger -= delta/6000;
		
		if(time >= delay && !getMenu().equals("Chat")){
			if(Gdx.input.isKeyPressed(Keys.NUMPAD_9) || (getMenu().equals("") && Gdx.input.isKeyPressed(Keys.U))){
					setY(getY() + 32);
					setX(getX() + 32);
					time = 0;
					setMenuActive(false);
					checkCollision();
			}
			else if(Gdx.input.isKeyPressed(Keys.NUMPAD_8) || (getMenu().equals("") && (Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.K)))){
					setY(getY() + 32);
					time = 0;
					setMenuActive(false);
					checkCollision();
			}
			else if(Gdx.input.isKeyPressed(Keys.NUMPAD_7) || (getMenu().equals("") && Gdx.input.isKeyPressed(Keys.Y))){
					setY(getY() + 32);
					setX(getX() - 32);
					time = 0;
					setMenuActive(false);
					checkCollision();
			}
			else if(Gdx.input.isKeyPressed(Keys.NUMPAD_6) || (getMenu().equals("") && (Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.L)))){
					setX(getX() + 32);
					time = 0;
					setMenuActive(false);
					checkCollision();
			}
			else if(Gdx.input.isKeyPressed(Keys.NUMPAD_4) || (getMenu().equals("") && (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.H)))){
					setX(getX() - 32);
					time = 0;
					setMenuActive(false);
					checkCollision();
			}
			else if(Gdx.input.isKeyPressed(Keys.NUMPAD_3) || (getMenu().equals("") && Gdx.input.isKeyPressed(Keys.N))){
					setX(getX() + 32);
					setY(getY() - 32);
					time = 0;
					setMenuActive(false);
					checkCollision();
			}
			else if(Gdx.input.isKeyPressed(Keys.NUMPAD_2) || (getMenu().equals("") && (Gdx.input.isKeyPressed(Keys.DOWN) || Gdx.input.isKeyPressed(Keys.J)))){
					setY(getY() - 32);
					time = 0;
					setMenuActive(false);
					checkCollision();
			}
			else if(Gdx.input.isKeyPressed(Keys.NUMPAD_1) || (getMenu().equals("") && Gdx.input.isKeyPressed(Keys.B))){
					setX(getX() - 32);
					setY(getY() - 32);
					time = 0;
					setMenuActive(false);
					checkCollision();
			}
		}
	}
	
	public void checkCollision(){
		collision = false;
		menuActive = false;
		setMenu("");
		if(Play.map.getTile(getX(), getY()).isBlocked())
			collision = true;
		else if(Play.map.getTile(getX(), getY()).get() != null){
			Item temp = Play.map.getTile(getX(), getY()).get();
			if(!inventory.checkIsFull(temp)){
				RemoveItemPacket item = new RemoveItemPacket();
				item.ID = temp.ID;
				item.floor = Play.map.getFloor();
				item.x = (int) (temp.getX()/32);
				item.y = (int) (temp.getY()/32);
				Rogue.clientWrapper.client.sendTCP(item);
				inventory.add(temp);
				Play.map.getTile(getX(), getY()).remove(temp.ID);
			}
			else{
				setAlert("Backpack is full!", true);
			}
		}
		else if(Play.map.getTile(getX(), getY()).hasStairs() && !Play.map.getTile(oldX, oldY).hasStairs()){

			packet.oldX = (int) oldX;
			packet.oldY = (int) oldY;
			Play.map.marks.put(-1, oldX, oldY);
			packet.x = (int) oldX;
			packet.y = (int) oldY;
			packet.floor = Play.map.getFloor();
			Rogue.clientWrapper.client.sendTCP(packet);

			
			RequestFloorPacket packet2 = new RequestFloorPacket();
			packet2.prevFloor = Play.map.getFloor();
			if(Play.map.getTile(getX(), getY()).direction())
				Play.map.mutateFloor(-1);
			else
				Play.map.mutateFloor(1);

			if(!(Play.map.players.size() > Play.map.getFloor()))
				Play.map.players.add(new HashMap<Integer, MPPlayer>());
			
			packet2.floor = Play.map.getFloor();
			
			packet2.x = (int) getX();
			packet2.y = (int) getY();
			packet2.ID = ID;
			Rogue.clientWrapper.client.sendTCP(packet2);
		}
		
		if(!(Play.map.marks.get((int)getX(), (int)getY()) == -1 || Play.map.marks.get((int)getX(), (int)getY()) == ID)){
			if(Play.map.get(Play.map.marks.get((int)getX(), (int)getY())) instanceof NPC){
				if(isHostile())
					attack((Animate) Play.map.get(Play.map.marks.get((int) getX(), (int) getY())));
				else{
					if(Play.map.get(Play.map.marks.get((int) getX(), (int) getY())) instanceof Shopkeep){
						menuActive = !menuActive;
						setMenu("Barter");
						Global.multiplexer.getProcessors().removeValue(inventory, true);
						Global.multiplexer.addProcessor(0, inventory);
						Global.multiplexer.addProcessor(0, (Shopkeep) Play.map.get(Play.map.marks.get((int)getX(), (int)getY())));
						setMenuObject((Shopkeep) Play.map.get(Play.map.marks.get((int) getX(), (int) getY())));
					}
					requestMessage(Play.map.npcs.get(Play.map.marks.get((int) getX(), (int) getY())));
				}
				collision = true;
			}
			else if(Play.map.get(Play.map.marks.get((int)getX(), (int)getY())) instanceof MPPlayer){
				if(isHostile())
					attack((Animate) Play.map.get(Play.map.marks.get((int) getX(), (int) getY())));
				collision = true;
			}
			
			else if(Play.map.get(Play.map.marks.get((int)getX(), (int)getY())) instanceof Chest){
				menuActive = !menuActive;
				setMenu("Chest");
				Global.multiplexer.getProcessors().removeValue(inventory, true);
				Global.multiplexer.addProcessor(0, inventory);
				Global.multiplexer.addProcessor(0, (Chest) Play.map.get(Play.map.marks.get((int)getX(), (int)getY())));
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
			packet.floor = Play.map.getFloor();
			Rogue.clientWrapper.client.sendTCP(packet);
		}
		oldX = (int) getX();
		oldY = (int) getY();
	}
	
	protected void attack(Animate entity){
		int amount = 0;
		if(stats.getStrength() == 0)
			amount = Global.rand(3, 0);
		else
			amount = Global.rand(stats.getStrength(), 0);
		/*if(entity instanceof MPPlayer)
			amount -= entity.defense;*/ //Fix this to have defense modifier
		if(amount < 0)
			amount = 0;
		amount *= -1;
		AttackPacket packet = new AttackPacket();
		packet.attackerID = ID;
		packet.receiverID = entity.ID;
		packet.floor = Play.map.getFloor();
		packet.amount = amount;
		Rogue.clientWrapper.client.sendTCP(packet);
	}
	
    public void light(){
    	visible.clear();
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
		
		Collections.sort(visible, new Comparator<Entity>(){
		     public int compare(Entity entity1, Entity entity2){
		    	 int index = 0;
		    	 if(entity1.getName().equals(entity2.getName()))
		    		 return 1;
		    	 while(entity1.getName().charAt(index) - entity2.getName().charAt(index) == 0)
		    		 index++;
		         return entity1.getName().charAt(index) - entity2.getName().charAt(index);
		     }
		});
		//Global.mapShapes.end();
    }
    
    public Vector3 intersect(Ray ray){
    	Entity temp = null;
    	float x, y, splits = 8; // Perfect number for splits and viewDistance to not have wall nonsense
    	for(int i=1; i<=splits; i++){
    		x = ray.origin.x + (((ray.direction.x - ray.origin.x)*i)/splits);
    		y = ray.origin.y + (((ray.direction.y - ray.origin.y)*i)/splits);
    		Tile tile = Play.map.getTile(x, y);
    		Play.map.setVisible(x, y, "visited");
	    	
    		
	    	// Render entities when in sight
	    	if(!(Play.map.marks.get((int) x, (int) y).equals(ID) || Play.map.marks.get((int) x, (int) y) == -1)){
	    		temp = Play.map.get(Play.map.marks.get((int) x, (int) y));
	    		if(temp != null && temp.canDraw == false){
	    			Play.map.get(Play.map.marks.get((int) x, (int) y)).canDraw = true;
	    			visible.add(Play.map.get(Play.map.marks.get((int) x, (int) y)));
	    		}
	    	}
	    	else if(tile.get() != null){
	    		if(tile.get().canDraw == false){
	    			visible.add(tile.get());
	    			tile.get().canDraw = true;
	    			
	    		}
    		}
	    	else if(!tile.canSee()){
	    		return new Vector3(ray.origin.x + (((ray.direction.x - ray.origin.x)*i)/splits), 
	    				           ray.origin.y + (((ray.direction.y - ray.origin.y)*i)/splits), 0);
	    	}
    	}
    	return ray.direction;
    }
	public ArrayList<Entity> getVisible(){
		return visible;
	}
	
	public void setInformation(String info){
		this.info = info;
	}
	
	public String getInformation(){
		return info;
	}
	
	public void setMenu(String request){
		if(request == "")
			menuActive = false;
		else
			menuActive = true;
		menu = request;
		menuObject = null;
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
    

	public float getHunger() {
		return hunger;
	}

	public void mutateHunger(float amount) {
		if(amount + this.hunger > 1.01f)
			this.hunger = 1.01f;
		else
			this.hunger += amount;
	}
	
	public boolean hasAlert(){
		if(alert.isEmpty())
			return false;
		return true;
	}
	public String getAlertMessage(){
		return alert;
	}
	public void setAlert(String alert, boolean error){
		setError(error);
		this.alert = alert;
	}
	private void setError(boolean error){
		this.error = error;
	}
	public boolean isError(){
		return error;
	}
	public Chat getChat(){
		return chat;
	}

	public String getPictureURL() {
		return pictureURL;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		switch(keycode){
		case Keys.P:
			if(!getMenu().equals("Chat") && !getMenu().equals("Inventory") && !getMenu().equals("Barter") && !getMenu().equals("Chest"))
				showPlayers = true;
			break;
		case Keys.ENTER:
			setMenu("Chat");
			Global.multiplexer.addProcessor(0, chat);
			break;
		case Keys.I:
			setMenu("Inventory");
			Global.multiplexer.addProcessor(0, inventory);
			break;
		case Keys.D:
			hostile = !hostile;
			break;
		case Keys.SHIFT_LEFT:
		case Keys.SHIFT_RIGHT:
			delay = .5f;
			break;
		case Keys.Q:
			if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)){
				RemovePlayerPacket packet = new RemovePlayerPacket();
				packet.ID = ID;
				packet.x = (int) getX();
				packet.y = (int) getY();
				packet.floor = Play.map.getFloor();
				Rogue.clientWrapper.client.sendTCP(packet);
				
				Rogue.clientWrapper.client.stop();
				Rogue.clientWrapper.client.close();
				Gdx.app.exit();
			}
			break;
		case Keys.SLASH:
			if(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT)){
				setMenu("Help");
			}
			break;
		case Keys.ESCAPE:
			inventory.setHover(null);
			setMenu("");
			break;
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch(keycode){
		case Keys.P:
			showPlayers = false;
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
	public boolean scrolled(int amount) {
		return true;
	}

	@Override
	public boolean mouseMoved(int x, int y) {
		return true;
	}
}