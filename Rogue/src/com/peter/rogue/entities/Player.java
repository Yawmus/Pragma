package com.peter.rogue.entities;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector3;
import com.peter.rogue.Global;
import com.peter.rogue.inventory.Chest;
import com.peter.rogue.inventory.Inventory;
import com.peter.rogue.inventory.Item;

public class Player extends Animate implements InputProcessor {
	
	private float zoom = 1f;
	private Texture picture;
	private String info;
	private String menu = "null";
	private boolean menuActive = false;
	private MapProperties keys = new MapProperties();
	private Inventory inventory = new Inventory();
	private Entity menuObject;
	private int wallet;
	private int viewDistance;
	
	public Player(String filename, String type){
		super(filename, type);
		messageFlag = false;
		name = "Adelaide";
		picture = new Texture(Gdx.files.internal("img/adelaide.png"));
		info = new String();
		viewDistance = 228;
		setWallet(0);
		hostile = false;
		stats.setLevel(1);
		stats.setHitpoints(10);
		stats.setDexterity(5);
		stats.setStrength(5);
		stats.setExperience(100);
	}

	public void draw(SpriteBatch spriteBatch){
		super.draw(spriteBatch);
		
		spriteBatch.end();
		pos = new Vector3(getX(), getY(), 0);
		Global.camera.project(pos);
		/*Global.shapeRenderer.begin(ShapeType.Line);
		Global.shapeRenderer.setColor(1f, 0, 0, .1f);
		Global.shapeRenderer.circle(pos.x + getWidth()/2, pos.y + getHeight()/2, viewDistance);
		Global.shapeRenderer.end();*/
		
		if(messageFlag){
			Global.shapeRenderer.begin(ShapeType.Filled);
			Global.shapeRenderer.setColor(0, 0, 0, 1f);
			Global.shapeRenderer.rect(pos.x, pos.y - 17, font.getBounds(getMessage()).width, font.getLineHeight());
			Global.shapeRenderer.end();
		}
		
		if(statusFlag){
			Global.shapeRenderer.begin(ShapeType.Filled);
			Global.shapeRenderer.setColor(0, 0, 0, 1f);
			Global.shapeRenderer.rect(pos.x, pos.y, font.getBounds(getMessage()).width, font.getLineHeight());
			Global.shapeRenderer.end();
		}

		spriteBatch.begin();
		Entity.font.draw(spriteBatch, getMessage(), getX(), getY());
		update(Gdx.graphics.getDeltaTime());
	}
	
	public Inventory getInventory(){
		return inventory;
	}
	
	public Texture getPicture(){
		return picture;
	}
	
	public void update(float delta){
		wait += Gdx.graphics.getDeltaTime();
		
		if(wait >= .35){
			if(Gdx.input.isKeyPressed(Keys.A)){
				setX(getX() - 32);
				wait = 0;
			}
			if(Gdx.input.isKeyPressed(Keys.D)){
				setX(getX() + 32);
				wait = 0;
			}
			if(Gdx.input.isKeyPressed(Keys.S)){
				setY(getY() - 32);
				wait = 0;
			}
			if(Gdx.input.isKeyPressed(Keys.W)){
				setY(getY() + 32);
				wait = 0;
			}
			if(wait == 0f)
				menuActive = false;
			checkCollision();
		}

		if(messageDelay > 2.0){
			resetMessage();
			messageDelay = 0;
			messageFlag = false;
		}
		
		if(getMessage() != ""){
			messageFlag = true;
			messageDelay += Gdx.graphics.getDeltaTime();
		}
		
		if(statusDelay > 2.0){
			resetMessage();
			messageDelay = 0;
			messageFlag = false;
		}
		
		if(getStatus() != 0){
			messageFlag = true;
			messageDelay += Gdx.graphics.getDeltaTime();
		}
	}
	
	public void checkCollision(){
		keys = collisionLayer.getCell((int) (getX() / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties();

		if("1".equals(keys.get("blocked"))){
			setX(oldX);
			setY(oldY);
		}
		
		else if("1".equals(keys.get("money"))){
			setWallet(Integer.parseInt(getMapID(getY(), getX())));
		}
		else if("1".equals(keys.get("stairs"))){
			if("up".equals(keys.get("direction"))){
				System.out.println("Hi");
				
			}
			else if("down".equals(keys.get("direction"))){
				System.out.println("Hi");
				
			}
		}
		if(getMapID(getY(), getX()) != "null" && getMapID(getY(), getX()) != getID()){
			if(getMapObject(getY(), getX()).getType().equals("Item"))
				inventory.add((Item) getMapObject(getY(), getX()));
			else if(getMapObject(getY(), getX()).getType().equals("Chest")){
				setMenu("Chest");
				setMenuObject((Chest)getMapObject(getY(), getX()));
				setX(oldX);
				setY(oldY);
			}
			else
				if(isHostile())
					attack((Animate) getMapObject(getY(), getX()));
				else
					bump((Animate)getMapObject(getY(), getX()));
		}
		
		setMap((int)oldY, (int)oldX, nullEntry);
		setMap((int)getY(), (int)getX(), this.entry);
		oldX = getX();
		oldY = getY();
	}
	
	private void attack(Animate entity) {
		int amount = -1*stats.getStrength();
		entity.getStats().mutateHitpoints(amount);
		Global.data.sendMessage(getMapID(getY(), getX()), this);
		Global.data.sendStatus(getMapID(getY(), getX()), amount);
		setX(oldX);
		setY(oldY);
	}
	private void bump(Animate entity) {
		Global.data.sendMessage(getMapID(getY(), getX()), this);
		setX(oldX);
		setY(oldY);
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
		case Keys.ESCAPE:
			System.exit(0);
		}
		return false;
	}


	@Override
	public boolean scrolled(int amount) {
		zoom += (.1f / amount);
		return true;
	}

	// Mouse scroll zoom (may break)
	public float getZoom() {
		return zoom;
	}
	
	public void setInformation(String info){
		if(info != "null")
			this.info = info;
		else
			this.info = "";
	}
	
	public String getInformation(){
		return info;
	}
	
	public void setMenu(String request){
		menu = request;
		menuObject = null;
		menuActive = !menuActive;
	}
	
	public String getMenu(){
		return menu;
	}
	
	public boolean isMenuActive(){
		return menuActive;
	}
	
	public void setMenuObject(Chest chest){
		menuObject = chest;
	}
	public Chest getMenuObject(){
		return (Chest)menuObject;
	}
	
	public int getViewDistance() {
		return viewDistance;
	}

	public int getWallet() {
		return wallet;
	}

	public void setWallet(int wallet) {
		this.wallet = wallet;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
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
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}
}
