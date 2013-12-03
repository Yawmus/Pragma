package com.peter.server;

import java.util.HashMap;
import java.util.Scanner;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.peter.entities.Citizen;
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
	public static int count = 1000;
	public static Map map = new Map();
	public static Data data = new Data();
	private static Integer removeID = 0;
	
	public static void main(String[] args) throws Exception {
		
		
		ItemPacket temp;
		ChestPacket temp2;
		NPC temp3;
		
		map.itemSets.get(0).put(++count, new ItemPacket("Ring", 30, 8, count));
		temp = map.itemSets.get(0).get(count);
		map.marks.put(temp.ID, temp.x * 32, temp.y * 32);

		map.itemSets.get(0).put(++count, new ItemPacket("Hat", 32, 8, count));
		temp = map.itemSets.get(0).get(count);
		map.marks.put(temp.ID, temp.x * 32, temp.y * 32);

		map.chestSets.get(0).put(++count, new ChestPacket("Chest", 32, 7, count));
		temp2 = map.chestSets.get(0).get(count);
		map.marks.put(temp2.ID, temp2.x * 32, temp2.y * 32);

		temp2.items.add(new ItemPacket("Breast Plate"));
		
		//map.npcs.put(++count, new NPC())
		for(int i=0; i<data.getCitizens(); i++){
			temp3 = new Citizen(/*"c_.png"*/);
			++count;
			map.npcSets.get(0).put(count, temp3);
			temp3.ID = count;
			temp3 = map.npcSets.get(0).get(count);
			temp3.setPosition(Global.rand(30, 28), Global.rand(9, 5));
			map.marks.put(temp3.ID, temp3.getX(), temp3.getY());
		}
		for(int i=0; i<data.getShopkeeps(); i++){
			temp3 = new Shopkeep(/*"s_.png", "Shopkeep"*/);
			++count;
			map.npcSets.get(0).put(count, temp3);
			temp3.ID = count;
			temp3 = map.npcSets.get(0).get(count);
			temp3.setPosition(Global.rand(30, 28), Global.rand(9, 5));
			map.marks.put(temp3.ID, temp3.getX(), temp3.getY());
			((Shopkeep) map.npcSets.get(0).get(count)).items.add(new ItemPacket("Breast Plate"));
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
		server.getKryo().register(java.util.HashMap.class);
		server.getKryo().register(java.util.ArrayList.class);
		server.getKryo().register(byte[][].class);
		server.getKryo().register(byte[].class);
		
		/*boolean success = false;
		int port;
		while(!success) // While port is unavailable
		try{
			System.out.print("Please enter the port: ");
			port = in.nextInt();
			server.bind(port, port);
			success = true;
		} catch(Exception e){
			System.out.println("Port is occupied, try a different port!");
		}*/
		server.bind(tcpPort, udpPort);
		server.start();
		server.addListener(new Listener(){
			
			//This is run when a connection is received!
			public void connected(Connection c){
				Player newPlayer = new Player();
				newPlayer.ID = c.getID();
				newPlayer.setX(28*32);
				newPlayer.setY(7*32);
				
				AddPlayerPacket packet = new AddPlayerPacket();
				packet.ID = c.getID();
				packet.x = newPlayer.getX();
				packet.y = newPlayer.getY();
				
				server.sendToAllExceptTCP(c.getID(), packet);
				
				for(Player player : map.players.values()){
					AddPlayerPacket packet2 = new AddPlayerPacket();
					packet2.ID = player.ID;
					c.sendTCP(packet2);
				}
				
				map.players.put(newPlayer.ID, newPlayer);
				
				// Sync map information
				MapPacket packet2 = new MapPacket();
				packet2.items = map.itemSets.get(0);
				packet2.chests = map.chestSets.get(0);
				
				packet2.npcs = new HashMap<Integer, AddNPCPacket>();
				for(NPC npc : map.npcSets.get(0).values()){
					packet2.npcs.put(npc.ID, new AddNPCPacket(npc.getX(), npc.getY(), npc.ID, npc.getType(), npc.getName()));
					if(npc instanceof Shopkeep){
						packet2.npcs.get(npc.ID).items = ((Shopkeep) npc).items;
					}
				}
				packet2.tiles = new byte[map.WIDTH][map.HEIGHT];
				for(int x=0; x<map.WIDTH; x++)
					for(int y=0; y<map.HEIGHT; y++)
						packet2.tiles[x][y] = ObjectToPacket.tileConverter(map.tiles[x][y]);
				
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
					else if(map.npcSets.get(packet.floor).containsKey(packet.receiverID)){
						packet.message = map.npcSets.get(packet.floor).get(packet.receiverID).getMessage(packet.callerID);
						server.sendToAllTCP(packet);
					}
					else
						System.out.println("[SERVER] failed to message something!");
				}
				else if(o instanceof PlayerPacket){
					PlayerPacket packet = (PlayerPacket) o;
					//if(map.marks.get(packet.x, packet.y) == -1 || map.marks.get(packet.x, packet.y) == c.getID()){
						map.players.get(c.getID()).setX(packet.x);
						map.players.get(c.getID()).setY(packet.y);
						map.marks.put(c.getID(), packet.x, packet.y);
						map.players.get(c.getID()).setOldX(packet.oldX);
						map.players.get(c.getID()).setOldY(packet.oldY);
						map.marks.put(-1, packet.oldX, packet.oldY);
						packet.ID = c.getID();
						server.sendToAllExceptUDP(c.getID(), packet);
					//}
					//System.out.println("received and sent an update player packet");
				}
				else if(o instanceof RemoveItemPacket){
					RemoveItemPacket packet = (RemoveItemPacket) o;
					if(map.itemSets.get(packet.floor).get(packet.ID) != null){
						map.itemSets.get(packet.floor).remove(packet.ID);
						map.marks.put(-1, packet.x, packet.y);
						server.sendToAllExceptUDP(c.getID(), packet);
					}
					else
						System.out.println("[SERVER] failed to remove item!");
				}
				else if(o instanceof AttackPacket){
					AttackPacket packet = (AttackPacket) o;
					if(map.npcSets.get(packet.floor).containsKey(packet.receiverID)){
						map.npcSets.get(packet.floor).get(packet.receiverID).getStats().mutateHitpoints(packet.amount);
						server.sendToAllUDP(packet);
						if(map.npcSets.get(packet.floor).get(packet.receiverID).getStats().getHitpoints() <= 0){
							ExperiencePacket packet2 = new ExperiencePacket();
							packet2.amount = map.npcSets.get(packet.floor).get(packet.receiverID).getStats().getExperience();
							packet2.name = map.npcSets.get(packet.floor).get(packet.receiverID).getName();
							server.sendToTCP(packet.attackerID, packet2);
							removeID = map.npcSets.get(packet.floor).get(packet.receiverID).ID;
						}
						else{
							map.npcSets.get(packet.floor).get(packet.receiverID).attacker = map.npcSets.get(packet.floor).containsKey(packet.attackerID) ? 
									map.npcSets.get(packet.floor).get(packet.attackerID) : map.players.get(packet.attackerID);
							map.npcSets.get(packet.floor).get(packet.receiverID).list.addID(packet.attackerID);
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
						server.sendToAllUDP(packet);
					}
					else
						System.out.println("[SERVER] failed to attack something!");
					
				}
				else if(o instanceof RemoveTradeItemPacket){
					RemoveTradeItemPacket packet = (RemoveTradeItemPacket) o;
					if(map.chestSets.get(packet.floor).get(packet.ID) != null){
						map.chestSets.get(packet.floor).get(packet.ID).items.remove(packet.index);
						server.sendToAllExceptUDP(c.getID(), packet);
					}
					else if(map.npcSets.get(packet.floor).get(packet.ID) != null){
						((Shopkeep) map.npcSets.get(packet.floor).get(packet.ID)).items.remove(packet.index);
						server.sendToAllExceptUDP(c.getID(), packet);
					}
				}
				else if(o instanceof AddTradeItemPacket){
					AddTradeItemPacket packet = (AddTradeItemPacket) o;
					if(map.chestSets.get(packet.floor).containsKey(packet.ID)){
						map.chestSets.get(packet.floor).get(packet.ID).items.add(packet.item);
						server.sendToAllExceptUDP(c.getID(), packet);
					}
					else if(map.npcSets.get(packet.floor).containsKey(packet.ID)){
						((Shopkeep) map.npcSets.get(packet.floor).get(packet.ID)).items.add(packet.item);
						server.sendToAllExceptUDP(c.getID(), packet);
					}
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
					MapPacket packet5 = new MapPacket();
					if(map.itemSets.size() > packet.floor)
						packet5.items = map.itemSets.get(packet.floor);
					else{
						map.itemSets.add(new HashMap<Integer, ItemPacket>());
						packet5.items = map.itemSets.get(packet.floor);
					}
					if(map.chestSets.size() > packet.floor)
						packet5.chests = map.chestSets.get(packet.floor);
					else{
						map.chestSets.add(new HashMap<Integer, ChestPacket>());
						packet5.chests = map.chestSets.get(packet.floor);
					}
					if(map.npcSets.size() > packet.floor){
						packet5.npcs = new HashMap<Integer, AddNPCPacket>();
						for(NPC npc : map.npcSets.get(packet.floor).values()){
	                        packet5.npcs.put(npc.ID, new AddNPCPacket(npc.getX(), npc.getY(), npc.ID, npc.getType(), npc.getName()));
	                        if(npc instanceof Shopkeep){
	                                packet5.npcs.get(npc.ID).items = ((Shopkeep) npc).items;
	                        }
						}
					}
					else{
						map.npcSets.add(new HashMap<Integer, NPC>());
						packet5.npcs = new HashMap<Integer, AddNPCPacket>();
					}
				
					packet5.tiles = new byte[map.WIDTH][map.HEIGHT];
					Tile[][] tileSet = map.getTileSet(packet.floor, packet.x/32, packet.y/32);
					
					for(int x=0; x<map.WIDTH; x++)
						for(int y=0; y<map.HEIGHT; y++)
							packet5.tiles[x][y] = ObjectToPacket.tileConverter(tileSet[x][y]);
					
					server.sendToTCP(c.getID(), packet5);
					

					map.players.get(c.getID()).floor = packet.floor;
				}
			}
			
			public void disconnected(Connection c){
				RemovePlayerPacket packet = new RemovePlayerPacket();
				map.players.remove(c.getID());
				map.marks.find(c.getID(), -1);
				packet.ID = c.getID();
				server.sendToAllExceptTCP(c.getID(), packet);
				System.out.println("Connection dropped.");
			}
			});
			
			System.out.println("Server is operational!");
			double lastFrame = 0, currentFrame;
			
			while(true){
				currentFrame = System.currentTimeMillis() - lastFrame;
				lastFrame = System.currentTimeMillis();
	
				if(removeID != 0){
					RemoveNPCPacket packet = new RemoveNPCPacket();
					packet.ID = map.npcSets.get(packet.floor).get(removeID).ID;
					server.sendToAllTCP(packet);
	
					ItemPacket packet2 = map.npcSets.get(packet.floor).get(removeID).getDrop();
					packet2.x = map.npcSets.get(packet.floor).get(removeID).getX()/32;
					packet2.y = map.npcSets.get(packet.floor).get(removeID).getY()/32;
					packet2.ID = ++count;
					map.itemSets.get(packet.floor).put(packet2.ID, packet2);
					map.marks.put(packet2.ID, packet2.x, packet2.y);
					server.sendToAllTCP(packet2);
					map.npcSets.get(packet2.floor).remove(removeID);
					removeID = 0;
				}
				for(int i=0; i<map.npcSets.size(); i++)
					for(NPC npc : map.npcSets.get(i).values())
						npc.update(currentFrame/1000);
			}

	}
}
