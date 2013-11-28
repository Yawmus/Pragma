package com.peter.entities;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.peter.inventory.Chest;
import com.peter.inventory.Item;
import com.peter.packets.AddNPCPacket;
import com.peter.packets.AddPlayerPacket;
import com.peter.packets.AddTradeItemPacket;
import com.peter.packets.ChestPacket;
import com.peter.packets.ItemPacket;
import com.peter.packets.MPPlayer;
import com.peter.rogue.Global;
import com.peter.rogue.Rogue;
import com.peter.rogue.screens.Play;
import com.peter.rogue.views.UI;

public class EntityManager{
	
    private UI ui;/*
	private Vector3 mapCoord;
    private int randX, randY;*/
    public static Player player;
    
	private ArrayList<NPC> npcs;
	//public static HashMap<Integer, Chest> chests;
	public static Queue<AddPlayerPacket> playerQueue;
	public static Queue<AddNPCPacket> NPCQueue;
	public static Queue<ItemPacket> itemQueue;
	public static Queue<ChestPacket> chestQueue;
	public static Queue<AddTradeItemPacket> tradeItemQueue;
    
    public EntityManager(Play play){
    	player = new Player("at.png");

		/*items = new ArrayList<Item>();
		npcs = new ArrayList<NPC>();
		chests = new HashMap<Integer, Chest>();*/
    	playerQueue = new LinkedList<AddPlayerPacket>();
    	NPCQueue = new LinkedList<AddNPCPacket>();
    	itemQueue = new LinkedList<ItemPacket>();
    	chestQueue = new LinkedList<ChestPacket>();
    	tradeItemQueue = new LinkedList<AddTradeItemPacket>();
    }
    
	public void draw(SpriteBatch spriteBatch){

		Play.map.getSpriteBatch().begin();
		Play.map.draw();

		/*for(int i=0; i<chests.size(); i++){
			if(chests.get(i).canDraw)
				chests.get(i).draw(spriteBatch);
		}

		for(int i=0; i<npcs.size(); i++)
			if(!npcs.get(i).canDraw)
				npcs.get(i).update(Gdx.graphics.getDeltaTime());
			else
				npcs.get(i).draw(spriteBatch);*/
		
		while(!itemQueue.isEmpty()){
			Item newItem = PacketToObject.itemConverter(itemQueue.peek());
			Play.map.marks.put(newItem.ID, (int)newItem.getX(), (int)newItem.getY());
			Play.map.items.put(newItem.ID, newItem);
			itemQueue.remove();
		}
		while(!NPCQueue.isEmpty()){
			NPC newNPC;
			//if(NPCQueue.peek().type.equals("Citizen"))
				newNPC = new Citizen("c_.png");
			newNPC.setPosition(NPCQueue.peek().x/32, NPCQueue.peek().y/32);
			Play.map.npcs.put(NPCQueue.peek().ID, newNPC);
			Play.map.marks.put(NPCQueue.peek().ID, NPCQueue.peek().x, NPCQueue.peek().y);
			NPCQueue.remove();
		}
		while(!chestQueue.isEmpty()){
			Chest newChest = new Chest();
			for(int i=0; i<chestQueue.peek().items.size(); i++)
				newChest.add(PacketToObject.itemConverter(chestQueue.peek().items.get(i)));
			newChest.ID = chestQueue.peek().ID;
			newChest.setPosition(chestQueue.peek().x, chestQueue.peek().y);
			Play.map.marks.put(newChest.ID, (int)newChest.getX(), (int)newChest.getY());
			Play.map.chests.put(newChest.ID, newChest);
			chestQueue.remove();
		}
		while(!playerQueue.isEmpty()){
			MPPlayer newPlayer = new MPPlayer("at.png", "Player", "Online guy");
			Play.map.players.put(playerQueue.peek().ID, newPlayer);
			Play.map.marks.put(playerQueue.peek().ID, playerQueue.peek().x, playerQueue.peek().y);
			playerQueue.remove();
		}
		while(!tradeItemQueue.isEmpty()){
			Play.map.chests.get(tradeItemQueue.peek().ID).add(PacketToObject.itemConverter(tradeItemQueue.peek().item));
			tradeItemQueue.remove();
		}

		for(Item item : Play.map.items.values())
			item.draw(spriteBatch);
		for(NPC npc : Play.map.npcs.values())
			npc.draw(spriteBatch);
		for(Chest chest : Play.map.chests.values())
			chest.draw(spriteBatch);
		for(MPPlayer mpPlayer : Play.map.players.values())
			mpPlayer.draw(spriteBatch);
		
		player.draw(spriteBatch);
		Play.map.getSpriteBatch().end();

		// If near edge of map then don't update respective axis
		if(player.getX() > Global.SCREEN_WIDTH/2 - 32*3 && player.getX() < Play.map.WIDTH*32 - 18*32)
			Global.camera.position.x = player.getX() + player.getWidth() / 2;
		if(player.getY() > Global.SCREEN_HEIGHT/2 - 32*6 && player.getY() < Play.map.HEIGHT*32 - 9*32)
			Global.camera.position.y = player.getY() + player.getHeight() / 2;
		Global.camera.update();
		Global.mapShapes.setProjectionMatrix(Global.camera.combined);
		spriteBatch.setProjectionMatrix(Global.camera.combined);
	    Play.map.setView(Global.camera);

		player.light();
		ui.draw(player, npcs);
		
		
		/*mapCoord = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		Global.camera.unproject(mapCoord);
		
		if(!player.isMenuActive()){
			player.setInformation(Entity.map.cursor(Entity.map.getMark(mapCoord.x, mapCoord.y), mapCoord.x, mapCoord.y));
			Global.mapShapes.begin(ShapeType.Filled);
			Global.mapShapes.setColor(0, 0, 0, 1f);
			Global.mapShapes.rect(mapCoord.x, mapCoord.y, Global.font.getBounds(player.getInformation()).width, Global.font.getLineHeight());
			Global.mapShapes.end();
			Play.map.getSpriteBatch().begin();
			Global.font.draw(spriteBatch, player.getInformation(), mapCoord.x, mapCoord.y + Global.font.getLineHeight() - 2);
			Play.map.getSpriteBatch().end();
		}
		else
			player.setInformation("");
		
*/
		Global.screen.begin();
		Global.font.draw(Global.screen, Rogue.VERSION, 0, Global.SCREEN_HEIGHT);
		Global.screen.end();
    }
    
    public void init(){

		player.setPosition(28, 7);
		
		ui = new UI(player);
    	Global.multiplexer.addProcessor(player);
		Gdx.input.setInputProcessor(Global.multiplexer);
		
		/*Entity.map.chests.add(new Chest());
		Entity.map.chests.get(Entity.map.chests.size()-1).setPosition(4, 4);

		Entity.map.chests.add(new Chest());
		Entity.map.chests.get(Entity.map.chests.size()-1).setPosition(6, 4);

		Entity.map.items.add(new Food(Food.BREAD));
		Entity.map.items.get(Entity.map.items.size()-1).setPosition(8, 32);
		
		Entity.map.items.add(new Wearable(Wearable.HAT));
		Entity.map.items.get(Entity.map.items.size()-1).setPosition(9, 32);
		
		Entity.map.items.add(new Wearable(Wearable.HAT));
		Entity.map.items.get(Entity.map.items.size()-1).setPosition(9, 31);
		
		Entity.map.items.add(new Wearable(Wearable.RING));
		Entity.map.items.get(Entity.map.items.size()-1).setPosition(9, 34);
		
		for(int i=0; i<Entity.map.getData().getCitizens(); i++){
			randX = Global.rand(13, 3);
			randY = Global.rand(7, 3);
			Entity.map.npcs.add(new Citizen("c_.png"));
			Entity.map.npcs.get(Entity.map.npcs.size()-1).setPosition(randX, randY);
		}
		
		randX = Global.rand(13, 3);
		randY = Global.rand(7, 3);
		Entity.map.npcs.add(Shopkeep.Bartender);
		Entity.map.npcs.get(Entity.map.npcs.size()-1).setPosition(randX, randY);
		
		randX = Global.rand(13, 3);
		randY = Global.rand(7, 3);
		Entity.map.npcs.add(Shopkeep.Shopkeep);
		Entity.map.npcs.get(Entity.map.npcs.size()-1).setPosition(randX, randY);
			
    	for(int i=0; i<Entity.map.getData().getMonsters(); i++){
			randX = Global.rand(13, 3);
			randY = Global.rand(7, 3);
			Entity.map.npcs.add(new Worm("tilda.png"));
			Entity.map.npcs.get(Entity.map.npcs.size()-1).setPosition(randX, randY);
    	}*/
	}
    
    public void dispose(){
		ui.dispose();
		Global.mapShapes.dispose();
		Global.screenShapes.dispose();
		Global.gothicFont.dispose();
		Global.font.dispose();
    }
}
