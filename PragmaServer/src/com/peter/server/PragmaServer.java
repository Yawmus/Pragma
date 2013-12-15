package com.peter.server;

import java.util.HashMap;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.peter.entities.Citizen;
import com.peter.entities.Monster;
import com.peter.entities.NPC;
import com.peter.entities.Player;
import com.peter.entities.Shopkeep;
import com.peter.map.Data;
import com.peter.map.Map;
import com.peter.map.Tile;
import com.peter.packets.AddNPCPacket;
import com.peter.packets.AddPlayerPacket;
import com.peter.packets.AddTradeItemPacket;
import com.peter.packets.AttackPacket;
import com.peter.packets.ChestPacket;
import com.peter.packets.ExperiencePacket;
import com.peter.packets.IDPacket;
import com.peter.packets.ItemPacket;
import com.peter.packets.MapPacket;
import com.peter.packets.MessagePacket;
import com.peter.packets.NPCPacket;
import com.peter.packets.PlayerPacket;
import com.peter.packets.RemoveItemPacket;
import com.peter.packets.RemoveNPCPacket;
import com.peter.packets.RemovePlayerPacket;
import com.peter.packets.RemoveTradeItemPacket;
import com.peter.packets.RequestFloorPacket;

public class PragmaServer{

	public static Server server;
	
	//Ports to listen on
	private static int udpPort = 23989, tcpPort = 23989;   //
	public final int HEIGHT = 40, WIDTH = 80;
	public static Map map = new Map();
	public static Data data = new Data();
	private static Integer removeID = 0;
	private static Integer removeFloor = 0;
	
	public static void main(String[] args) throws Exception {
		ItemPacket temp;
		ChestPacket temp2;
		NPC temp3;
		
		Map.itemSets.get(0).put(++Global.count, new ItemPacket("Ring", 30, 8, Global.count));
		temp = Map.itemSets.get(0).get(Global.count);
		Map.markSets.get(0).put(temp.ID, temp.x * 32, temp.y * 32);

		Map.itemSets.get(0).put(++Global.count, new ItemPacket("Hat", 32, 8, Global.count));
		temp = Map.itemSets.get(0).get(Global.count);
		Map.markSets.get(0).put(temp.ID, temp.x * 32, temp.y * 32);

		Map.chestSets.get(0).put(++Global.count, new ChestPacket("Chest", 32, 7, Global.count));
		temp2 = Map.chestSets.get(0).get(Global.count);
		Map.markSets.get(0).put(temp2.ID, temp2.x * 32, temp2.y * 32);

		temp2.items.add(new ItemPacket("Breast Plate"));
		
		//map.npcs.put(++count, new NPC())
		for(int i=0; i<data.getCitizens(); i++){
			temp3 = new Citizen();
			temp3.ID = ++Global.count;
			temp3.floor = 0;
			temp3.setPosition(Global.rand(30, 28), Global.rand(9, 5));
			Map.npcSets.get(0).put(Global.count, temp3);
			Map.markSets.get(0).put(temp3.ID, temp3.getX(), temp3.getY());
		}
		for(int i=0; i<data.getMonsters(); i++){
			temp3 = new Monster(/*Monster.WORM*/"Worm", true);
			temp3.ID = ++Global.count;
			temp3.floor = 0;
			temp3.setPosition(Global.rand(30, 28), Global.rand(9, 5));
			Map.npcSets.get(0).put(Global.count, temp3);
			Map.markSets.get(0).put(temp3.ID, temp3.getX(), temp3.getY());
		}
		for(int i=0; i<data.getShopkeeps(); i++){
			temp3 = new Shopkeep();
			temp3.ID = ++Global.count;
			temp3.floor = 0;
			temp3.setPosition(Global.rand(30, 28), Global.rand(9, 5));
			Map.npcSets.get(0).put(Global.count, temp3);
			Map.markSets.get(0).put(temp3.ID, temp3.getX(), temp3.getY());
			((Shopkeep) Map.npcSets.get(0).get(Global.count)).items.add(new ItemPacket("Breast Plate"));
		}
		
		connect();
	}
	
	public static void connect() throws Exception {
		System.out.println("Creating the server...");
		
		server = new Server();
		server.getKryo().register(PlayerPacket.class);
		server.getKryo().register(NPCPacket.class);
		server.getKryo().register(ExperiencePacket.class);
		server.getKryo().register(AttackPacket.class);
		server.getKryo().register(AddNPCPacket.class);
		server.getKryo().register(RemoveNPCPacket.class);
		server.getKryo().register(MessagePacket.class);
		server.getKryo().register(AddPlayerPacket.class);
		server.getKryo().register(RemovePlayerPacket.class);
		server.getKryo().register(MapPacket.class);
		server.getKryo().register(RequestFloorPacket.class);
		server.getKryo().register(ItemPacket.class);
		server.getKryo().register(ChestPacket.class);
		server.getKryo().register(RemoveItemPacket.class);
		server.getKryo().register(RemoveTradeItemPacket.class);
		server.getKryo().register(AddTradeItemPacket.class);
		server.getKryo().register(IDPacket.class);
		server.getKryo().register(java.util.HashMap.class);
		server.getKryo().register(java.util.ArrayList.class);
		server.getKryo().register(short[][].class);
		server.getKryo().register(short[].class);
		server.getKryo().register(byte[][].class);
		server.getKryo().register(byte[].class);
		
		server.bind(tcpPort, udpPort);
		server.start();
		server.addListener(new Listener(){
			
			//This is run when a connection is received!
			public void connected(Connection c){
				IDPacket packet3 = new IDPacket();
				packet3.ID = c.getID();
				c.sendTCP(packet3);

				// Sync map information
				MapPacket packet2 = new MapPacket();
				packet2.items = Map.itemSets.get(0);
				packet2.chests = Map.chestSets.get(0);
				
				packet2.npcs = new HashMap<Integer, AddNPCPacket>();
				for(NPC npc : Map.npcSets.get(0).values()){
					packet2.npcs.put(npc.ID, new AddNPCPacket(npc.getX(), npc.getY(), npc.ID, npc.getType(), npc.getName()));
					if(npc instanceof Shopkeep){
						packet2.npcs.get(npc.ID).items = ((Shopkeep) npc).items;
					}
				}
				packet2.marks = new short[Map.WIDTH][Map.HEIGHT];
				for(int i=0; i<Map.WIDTH; i++)
					for(int j=0; j<Map.HEIGHT; j++)
						packet2.marks[i][j] = (short)((int)Map.markSets.get(0).getMarker()[i][j]);
				
				packet2.tiles = new byte[Map.WIDTH][Map.HEIGHT];
				for(int x=0; x<Map.WIDTH; x++)
					for(int y=0; y<Map.HEIGHT; y++)
						packet2.tiles[x][y] = ObjectToPacket.tileConverter(Map.tileSets.get(0)[x][y]);
				
				server.sendToTCP(c.getID(), packet2);
				
				System.out.println("[SERVER] connection received.");
			}
			
			public void received(Connection c, Object o){
				if(o instanceof MessagePacket){
					MessagePacket packet = (MessagePacket) o;
					if(map.players.containsKey(packet.receiverID)){
						System.out.println("[" + map.players.get(packet.receiverID).getName() + "] " + packet.message);
						server.sendToAllExceptUDP(c.getID(), packet);
					}
					else if(Map.npcSets.get(packet.floor).containsKey(packet.receiverID)){
						packet.message = Map.npcSets.get(packet.floor).get(packet.receiverID).getMessage(packet.callerID);
						server.sendToAllTCP(packet);
					}
					else
						System.out.println("[SERVER] failed to message something!");
				}
				else if(o instanceof PlayerPacket){
					PlayerPacket packet = (PlayerPacket) o;
					map.players.get(c.getID()).setX(packet.x);
					map.players.get(c.getID()).setY(packet.y);
					Map.markSets.get(packet.floor).put(c.getID(), packet.x, packet.y);
					map.players.get(c.getID()).setOldX(packet.oldX);
					map.players.get(c.getID()).setOldY(packet.oldY);
					Map.markSets.get(packet.floor).put(-1, packet.oldX, packet.oldY);
					packet.ID = c.getID();
					for(Player player : map.players.values())
						if(player.floor == packet.floor && player.ID != c.getID())
							server.sendToTCP(player.ID, packet);
				}
				else if(o instanceof RemoveItemPacket){
					RemoveItemPacket packet = (RemoveItemPacket) o;
					if(Map.itemSets.get(packet.floor).get(packet.ID) != null){
						Map.itemSets.get(packet.floor).remove(packet.ID);
						Map.markSets.get(packet.floor).put(-1, packet.x, packet.y);
						server.sendToAllExceptUDP(c.getID(), packet);
					}
					else
						System.out.println("[SERVER] failed to remove item!");
				}
				else if(o instanceof AttackPacket){
					AttackPacket packet = (AttackPacket) o;
					if(Map.npcSets.get(packet.floor).containsKey(packet.receiverID)){
						Map.npcSets.get(packet.floor).get(packet.receiverID).getStats().mutateHitpoints(packet.amount);
						for(Player player : map.players.values())
							if(player.floor == packet.floor)
								server.sendToTCP(player.ID, packet);
						if(Map.npcSets.get(packet.floor).get(packet.receiverID).getStats().getHitpoints() <= 0){
							ExperiencePacket packet2 = new ExperiencePacket();
							packet2.amount = Map.npcSets.get(packet.floor).get(packet.receiverID).getStats().getExperience();
							packet2.name = Map.npcSets.get(packet.floor).get(packet.receiverID).getName();
							server.sendToTCP(packet.attackerID, packet2);
							removeID = Map.npcSets.get(packet.floor).get(packet.receiverID).ID;
							removeFloor = packet.floor;
						}
						else{
							Map.npcSets.get(packet.floor).get(packet.receiverID).attacker = Map.npcSets.get(packet.floor).containsKey(packet.attackerID) ? 
									Map.npcSets.get(packet.floor).get(packet.attackerID) : map.players.get(packet.attackerID);
							Map.npcSets.get(packet.floor).get(packet.receiverID).list.addID(packet.attackerID);
						}
					}
					else if(map.players.containsKey(packet.receiverID)){
						map.players.get(packet.receiverID).getStats().mutateHitpoints(packet.amount);
						if(map.players.get(packet.receiverID).getStats().getHitpoints() <= 0){
							ExperiencePacket packet2 = new ExperiencePacket();
							packet2.amount = 20;
							packet2.name = map.players.get(packet.receiverID).getName();
							server.sendToTCP(packet.attackerID, packet2);
						}
						for(Player player : map.players.values())
							if(player.floor == packet.floor)
								server.sendToTCP(player.ID, packet);
					}
					else
						System.out.println("[SERVER] failed to attack something!");
					
				}
				else if(o instanceof RemoveTradeItemPacket){
					RemoveTradeItemPacket packet = (RemoveTradeItemPacket) o;
					if(Map.chestSets.get(packet.floor).get(packet.ID) != null){
						Map.chestSets.get(packet.floor).get(packet.ID).items.remove(packet.index);
						server.sendToAllExceptUDP(c.getID(), packet);
					}
					else if(Map.npcSets.get(packet.floor).get(packet.ID) != null){
						((Shopkeep) Map.npcSets.get(packet.floor).get(packet.ID)).items.remove(packet.index);
						server.sendToAllExceptUDP(c.getID(), packet);
					}
				}
				else if(o instanceof AddTradeItemPacket){
					AddTradeItemPacket packet = (AddTradeItemPacket) o;
					if(Map.chestSets.get(packet.floor).containsKey(packet.ID)){
						Map.chestSets.get(packet.floor).get(packet.ID).items.add(packet.item);
						server.sendToAllExceptUDP(c.getID(), packet);
					}
					else if(Map.npcSets.get(packet.floor).containsKey(packet.ID)){
						((Shopkeep) Map.npcSets.get(packet.floor).get(packet.ID)).items.add(packet.item);
						server.sendToAllExceptUDP(c.getID(), packet);
					}
				}
				else if(o instanceof AddPlayerPacket){
					for(Player player : map.players.values()){
						AddPlayerPacket packet = new AddPlayerPacket();
						packet.ID = player.ID;
						packet.x = player.getX();
						packet.y = player.getY();
						packet.name = player.getName();
						packet.picture = player.getPicture();
						server.sendToTCP(c.getID(), packet);
					}
					
					AddPlayerPacket packet2 = (AddPlayerPacket) o;
					Player newPlayer = new Player();
					newPlayer.ID = packet2.ID;
					newPlayer.setX(packet2.x);
					newPlayer.setY(packet2.y);
					newPlayer.setPicture(packet2.picture);
					newPlayer.setName(packet2.name);
					
					map.players.put(packet2.ID, newPlayer);
					server.sendToAllExceptTCP(packet2.ID, packet2);
					
					
				}
				else if(o instanceof RequestFloorPacket){
					RequestFloorPacket packet = (RequestFloorPacket) o;
					
					// Removes the player from the players on the previous floor
					RemovePlayerPacket packet2 = new RemovePlayerPacket();
					packet2.ID = c.getID();
					packet2.x = map.players.get(c.getID()).getX();
					packet2.y = map.players.get(c.getID()).getY();
					
					for(Player player : map.players.values())
						if(player.floor == map.players.get(c.getID()).floor && player.ID != c.getID())
							server.sendToTCP(player.ID, packet2);
					
					
					// Adds the new player to any existing players on the floor and vice versa
					AddPlayerPacket packet3 = new AddPlayerPacket();
					packet3.ID = c.getID();
					packet3.x = map.players.get(c.getID()).getX();
					packet3.y = map.players.get(c.getID()).getY();
					
					for(Player player : map.players.values()){
						if(player.floor == packet.floor && player.ID != c.getID()){
							server.sendToTCP(player.ID, packet3);
							AddPlayerPacket packet4 = new AddPlayerPacket();
							packet4.ID = player.ID;
							packet4.x = player.getX();
							packet4.y = player.getY();
							server.sendToTCP(c.getID(), packet4);
						}
					}
					
					// Syncs the items, chests, npcs and tiles on the map
					map.players.get(c.getID()).floor = packet.floor;
					MapPacket packet5 = new MapPacket();
					
					packet5.tiles = new byte[Map.WIDTH][Map.HEIGHT];
					Tile[][] tileSet = map.generateFloor(packet.floor, packet.x/32, packet.y/32);
					
					packet5.items = Map.itemSets.get(packet.floor);
					packet5.chests = Map.chestSets.get(packet.floor);
					packet5.npcs = new HashMap<Integer, AddNPCPacket>();
					for(NPC npc : Map.npcSets.get(packet.floor).values()){
                        packet5.npcs.put(npc.ID, new AddNPCPacket(npc.getX(), npc.getY(), npc.ID, npc.getType(), npc.getName()));
                        if(npc instanceof Shopkeep){
                                packet5.npcs.get(npc.ID).items = ((Shopkeep) npc).items;
                        }
					}
					packet5.marks = new short[Map.WIDTH][Map.HEIGHT];
					for(int i=0; i<Map.WIDTH; i++)
						for(int j=0; j<Map.HEIGHT; j++)
							packet5.marks[i][j] = (short)((int)Map.markSets.get(packet.floor).getMarker()[i][j]);
					
					for(int x=0; x<Map.WIDTH; x++)
						for(int y=0; y<Map.HEIGHT; y++)
							packet5.tiles[x][y] = ObjectToPacket.tileConverter(tileSet[x][y]);
					
					server.sendToTCP(c.getID(), packet5);
					

					map.players.get(c.getID()).floor = packet.floor;
				}

			}
			
			
			public void disconnected(Connection c){
				RemovePlayerPacket packet = new RemovePlayerPacket();
				map.players.remove(c.getID());
				for(int i=0; i<Map.markSets.size(); i++)
					Map.markSets.get(i).find(c.getID(), -1);
				packet.ID = c.getID();
				server.sendToAllExceptTCP(c.getID(), packet);
				System.out.println("[SERVER] Connection dropped.");
			}
			});
			
			System.out.println("Server is operational!");
			double lastFrame = 0, currentFrame;
			
			while(true){
				currentFrame = System.currentTimeMillis() - lastFrame;
				lastFrame = System.currentTimeMillis();
	
				if(removeID != 0){
					RemoveNPCPacket packet = new RemoveNPCPacket();
					packet.ID = removeID;
					packet.floor = removeFloor;
					for(Player player : map.players.values())
						if(player.floor == removeFloor)
							server.sendToTCP(player.ID, packet);

					if(Map.npcSets.get(removeFloor).get(removeID).getDrop() != null){ // Drops nothing
						ItemPacket packet2 = Map.npcSets.get(removeFloor).get(removeID).getDrop();
						packet2.x = Map.npcSets.get(removeFloor).get(removeID).getX()/32;
						packet2.y = Map.npcSets.get(removeFloor).get(removeID).getY()/32;
						packet2.ID = ++Global.count;
						packet2.floor = removeFloor;
						Map.itemSets.get(removeFloor).put(packet2.ID, packet2);
						Map.markSets.get(removeFloor).put(packet2.ID, packet2.x, packet2.y);
						for(Player player : map.players.values())
							if(player.floor == removeFloor)
								server.sendToTCP(player.ID, packet2);
					}
					Map.npcSets.get(removeFloor).remove(removeID);
					removeID = 0;
				}
				try{
					for(int i=0; i<Map.npcSets.size(); i++)
						for(NPC npc : Map.npcSets.get(i).values())
							npc.update(currentFrame/1000);
				} catch(Exception e){
					System.out.println("Should only be displayed if new floor");
				}
			}
	}
}
