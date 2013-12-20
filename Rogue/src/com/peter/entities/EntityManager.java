package com.peter.entities;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.peter.inventory.Chest;
import com.peter.inventory.Item;
import com.peter.packets.AddNPCPacket;
import com.peter.packets.AddPlayerPacket;
import com.peter.packets.AddTradeItemPacket;
import com.peter.packets.ChestPacket;
import com.peter.packets.ItemPacket;
import com.peter.rogue.Global;
import com.peter.rogue.Rogue;
import com.peter.rogue.network.PacketToObject;
import com.peter.rogue.screens.Play;
import com.peter.rogue.views.UI;

public class EntityManager{
	
    private UI ui;
	private Vector3 mapCoord;
    public static Player player;
    
	public static Queue<AddPlayerPacket> playerQueue;
	public static Queue<AddNPCPacket> NPCQueue;
	public static Queue<ItemPacket> itemQueue;
	public static Queue<ChestPacket> chestQueue;
	public static Queue<AddTradeItemPacket> tradeItemQueue;
    
    public EntityManager(Play play){
    	player = new Player("at.png");
    	
    	playerQueue = new LinkedList<AddPlayerPacket>();
    	NPCQueue = new LinkedList<AddNPCPacket>();
    	itemQueue = new LinkedList<ItemPacket>();
    	chestQueue = new LinkedList<ChestPacket>();
    	tradeItemQueue = new LinkedList<AddTradeItemPacket>();
    }
    
	public void draw(SpriteBatch spriteBatch){

		while(!itemQueue.isEmpty()){
			Item newItem = PacketToObject.itemConverter(itemQueue.peek());
			Play.map.marks.put(newItem.ID, (int) newItem.getX(), (int) newItem.getY());
			Play.map.items.put(newItem.ID, newItem);
			Play.map.database.put(itemQueue.peek().ID, newItem);
			itemQueue.remove();
		}
		while(!NPCQueue.isEmpty()){
			NPC newNPC;
			if(NPCQueue.peek().group.equals("Citizen"))
				newNPC = new Citizen(NPCQueue.peek().race, NPCQueue.peek().type, NPCQueue.peek().name);
			else if(NPCQueue.peek().group.equals("Shopkeep")){
				newNPC = new Shopkeep(NPCQueue.peek().race, NPCQueue.peek().type, NPCQueue.peek().name);
				for(int i=0; i<NPCQueue.peek().items.size(); i++)
					((Shopkeep) newNPC).add(PacketToObject.itemConverter(NPCQueue.peek().items.get(i)));
			}
			else if(NPCQueue.peek().group.equals("Monster"))
				newNPC = new Monster(NPCQueue.peek().race, NPCQueue.peek().type, NPCQueue.peek().name);
			else{
				newNPC = null;
				System.out.println("[CLIENT] npc conversion failed!");
			}
			newNPC.race = NPCQueue.peek().race;
			newNPC.type = NPCQueue.peek().type;
			newNPC.name = NPCQueue.peek().name;
			newNPC.ID = NPCQueue.peek().ID;
			newNPC.setPosition(NPCQueue.peek().x/32, NPCQueue.peek().y/32);
			Play.map.npcs.put(NPCQueue.peek().ID, newNPC);
			Play.map.database.put(NPCQueue.peek().ID, newNPC);
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
			Play.map.database.put(chestQueue.peek().ID, newChest);
			chestQueue.remove();
		}
		while(!playerQueue.isEmpty()){
			MPPlayer newPlayer = new MPPlayer("at.png");
			newPlayer.ID = playerQueue.peek().ID;
			newPlayer.setX(playerQueue.peek().x);
			newPlayer.setY(playerQueue.peek().y);
			newPlayer.setPictureURL(playerQueue.peek().picture);
			newPlayer.setName(playerQueue.peek().name);
			System.out.println(playerQueue.peek().floor);
			System.out.println(Play.map.players.size());
			while(playerQueue.peek().floor > Play.map.players.size()-1)
				Play.map.players.add(new HashMap<Integer, MPPlayer>());
			System.out.println(Play.map.players.size());
			Play.map.players.get(playerQueue.peek().floor).put(playerQueue.peek().ID, newPlayer);
			if(playerQueue.peek().floor == Play.map.getFloor()){
				Play.map.database.put(playerQueue.peek().ID, newPlayer);
				Play.map.marks.put(playerQueue.peek().ID, playerQueue.peek().x, playerQueue.peek().y);
			}
			playerQueue.remove();
		}
		while(!tradeItemQueue.isEmpty()){
			if(Play.map.chests.containsKey(tradeItemQueue.peek().ID))
				Play.map.chests.get(tradeItemQueue.peek().ID).add(PacketToObject.itemConverter(tradeItemQueue.peek().item));
			else if(Play.map.npcs.containsKey(tradeItemQueue.peek().ID))
				((Shopkeep) Play.map.npcs.get(tradeItemQueue.peek().ID)).add(PacketToObject.itemConverter(tradeItemQueue.peek().item));
			tradeItemQueue.remove();
		}

		Play.map.getSpriteBatch().begin();
		Play.map.draw();
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
		ui.draw(player);
		
		mapCoord = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		Global.camera.unproject(mapCoord);
		
		if(!player.isMenuActive()){
			player.setInformation(Play.map.cursor(Play.map.marks.get((int) mapCoord.x, (int) mapCoord.y), (int) mapCoord.x, (int) mapCoord.y));
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
		
		Global.screen.begin();
		Global.font.draw(Global.screen, Rogue.VERSION + "  " + player.ID, 0, Global.SCREEN_HEIGHT);
		Global.screen.end();
		
		if(player.stats.getHitpoints() <= 0){
			Global.gameOver = true;
			Rogue.clientWrapper.client.stop();
			Rogue.clientWrapper.client.close();
			//player.death.play();
		}
    }
    
    public void init(){
		player.setPosition(28, 7);
		Play.map.marks.put(player.ID, (int) player.getX(), (int) player.getY());
		Play.map.database.put(player.ID, player);
		
		ui = new UI(player);
    	Global.multiplexer.addProcessor(player);
		Gdx.input.setInputProcessor(Global.multiplexer);
		
		AddPlayerPacket packet = new AddPlayerPacket();
		packet.name = player.getName();
		packet.picture = player.getPictureURL();
		packet.x = (int) player.getX();
		packet.y = (int) player.getY();
		packet.floor = Play.map.getFloor();
		packet.ID = player.ID;
		Rogue.clientWrapper.client.sendTCP(packet);
	}
    
    public void dispose(){
		ui.dispose();
		Global.mapShapes.dispose();
		Global.screenShapes.dispose();
		Global.gothicFont.dispose();
		Global.font.dispose();
    }
}