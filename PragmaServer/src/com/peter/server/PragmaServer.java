package com.peter.server;

import java.util.HashMap;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.peter.entities.Animate;
import com.peter.entities.Citizen;
import com.peter.entities.NPC;
import com.peter.entities.Shopkeep;
import com.peter.map.Map;
import com.peter.packets.AddNPCPacket;
import com.peter.packets.AddPlayerPacket;
import com.peter.packets.AddTradeItemPacket;
import com.peter.packets.AttackPacket;
import com.peter.packets.ChestPacket;
import com.peter.packets.ItemPacket;
import com.peter.packets.MapPacket;
import com.peter.packets.MessagePacket;
import com.peter.packets.NPCPacket;
import com.peter.packets.Player;
import com.peter.packets.PlayerPacket;
import com.peter.packets.RemoveItemPacket;
import com.peter.packets.RemoveNPCPacket;
import com.peter.packets.RemovePlayerPacket;
import com.peter.packets.RemoveTradeItemPacket;

public class PragmaServer{

	public static Server server;
	
	//Ports to listen on
	private static int udpPort = 23989, tcpPort = 23989;
	public final int HEIGHT = 40, WIDTH = 80;
	public static int count = 1000;
	public static Map map = new Map();
	private static Integer removeID = 0;
	
	public static void main(String[] args) throws Exception {
		ItemPacket temp;
		ChestPacket temp2;
		NPC temp3;
		
		map.items.put(++count, new ItemPacket("Ring", 30, 8, count));
		temp = map.items.get(count);
		map.marks.put(temp.ID, temp.x, temp.y);

		map.items.put(++count, new ItemPacket("Hat", 32, 8, count));
		temp = map.items.get(count);
		map.marks.put(temp.ID, temp.x, temp.y);

		map.chests.put(++count, new ChestPacket("Chest", 32, 7, count));
		temp2 = map.chests.get(count);
		map.marks.put(temp2.ID, temp2.x, temp2.y);

		temp2.items.add(new ItemPacket("Breast Plate"));
		
		//map.npcs.put(++count, new NPC())
		for(int i=0; i<3; i++){
			temp3 = new Citizen(/*"c_.png"*/);
			map.npcs.put(++count, temp3);
			temp3.ID = count;
			temp3 = map.npcs.get(count);
			temp3.setPosition(Global.rand(30, 28), Global.rand(9, 5));
			map.marks.put(temp3.ID, temp3.getX(), temp3.getY());
		}
		for(int i=0; i<1; i++){
			temp3 = new Shopkeep(/*"s_.png", "Shopkeep"*/);
			map.npcs.put(++count, temp3);
			temp3.ID = count;
			temp3 = map.npcs.get(count);
			temp3.setPosition(Global.rand(30, 28), Global.rand(9, 5));
			map.marks.put(temp3.ID, temp3.getX(), temp3.getY());
			((Shopkeep) map.npcs.get(count)).items.add(new ItemPacket("Breast Plate"));
		}
		
		connect();
	}
	
	public static void connect() throws Exception {
		System.out.println("Creating the server...");
		
		server = new Server();
		server.getKryo().register(PlayerPacket.class);
		server.getKryo().register(NPCPacket.class);
		server.getKryo().register(AttackPacket.class);
		server.getKryo().register(AddNPCPacket.class);
		server.getKryo().register(RemoveNPCPacket.class);
		server.getKryo().register(MessagePacket.class);
		server.getKryo().register(AddPlayerPacket.class);
		server.getKryo().register(RemovePlayerPacket.class);
		server.getKryo().register(MapPacket.class);
		server.getKryo().register(ItemPacket.class);
		server.getKryo().register(ChestPacket.class);
		server.getKryo().register(RemoveItemPacket.class);
		server.getKryo().register(RemoveTradeItemPacket.class);
		server.getKryo().register(AddTradeItemPacket.class);
		server.getKryo().register(java.util.HashMap.class);
		server.getKryo().register(java.util.ArrayList.class);
		server.getKryo().register(byte[][].class);
		server.getKryo().register(byte[].class);
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
				packet2.items = map.items;
				packet2.chests = map.chests;
				
				packet2.npcs = new HashMap<Integer, AddNPCPacket>();
				for(NPC npc : map.npcs.values()){
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
				
				System.out.println("Connection received.");
			}
			
			//This is run when we receive a packet.
			public void received(Connection c, Object o){
				if(o instanceof MessagePacket){
					MessagePacket packet = (MessagePacket) o;
					System.out.println("[" + map.players.get(packet.ID).getName() + "] " + packet.message);
					server.sendToAllExceptUDP(c.getID(), packet);
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
					if(map.items.get(packet.ID) != null){
						map.items.remove(packet.ID);
						map.marks.put(-1, packet.x, packet.y);
						server.sendToAllExceptUDP(c.getID(), packet);
					}
					else
						System.out.println("[SERVER] failed to remove item!");
				}
				else if(o instanceof AttackPacket){
					AttackPacket packet = (AttackPacket) o;
					if(map.npcs.containsKey(packet.receiverID)){
						((Animate) map.npcs.get(packet.receiverID)).attacker = map.npcs.containsKey(packet.attackerID) ? 
								map.npcs.get(packet.attackerID) : map.players.get(packet.attackerID);
						map.npcs.get(packet.receiverID).getStats().mutateHitpoints(packet.amount);
						server.sendToAllUDP(packet);
						if(map.npcs.get(packet.receiverID).getStats().getHitpoints() <= 0)
							removeID = map.npcs.get(packet.receiverID).ID;
					}
					else if(map.players.containsKey(packet.receiverID)){
						map.players.get(packet.receiverID).getStats().mutateHitpoints(packet.amount);
						server.sendToAllUDP(packet);
					}
					else
						System.out.println("[SERVER] failed to attack something!");
					
				}
				else if(o instanceof RemoveTradeItemPacket){
					RemoveTradeItemPacket packet = (RemoveTradeItemPacket) o;
					System.out.println("Removed!");
					if(map.chests.get(packet.ID) != null){
						map.chests.get(packet.ID).items.remove(packet.index);
						server.sendToAllExceptUDP(c.getID(), packet);
					}
					else if(map.npcs.get(packet.ID) != null){
						((Shopkeep) map.npcs.get(packet.ID)).items.remove(packet.index);
						server.sendToAllExceptUDP(c.getID(), packet);
					}
				}
				else if(o instanceof AddTradeItemPacket){
					AddTradeItemPacket packet = (AddTradeItemPacket) o;
					if(map.chests.containsKey(packet.ID)){
						map.chests.get(packet.ID).items.add(packet.item);
						server.sendToAllExceptUDP(c.getID(), packet);
					}
					else if(map.npcs.containsKey(packet.ID)){
						((Shopkeep) map.npcs.get(packet.ID)).items.add(packet.item);
						server.sendToAllExceptUDP(c.getID(), packet);
					}
				}
			}
			
			public void disconnected(Connection c){
				map.players.remove(c.getID());
				RemovePlayerPacket packet = new RemovePlayerPacket();
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
				packet.ID = map.npcs.get(removeID).ID;
				server.sendToAllTCP(packet);

				ItemPacket packet2 = map.npcs.get(removeID).getDrop();
				packet2.x = map.npcs.get(removeID).getX()/32;
				packet2.y = map.npcs.get(removeID).getY()/32;
				packet2.ID = ++count;
				map.items.put(packet2.ID, packet2);
				map.marks.put(packet2.ID, packet2.x, packet2.y);
				server.sendToAllTCP(packet2);
				map.npcs.remove(removeID);
				removeID = 0;
			}
			for(NPC npc : map.npcs.values())
				npc.update(currentFrame/1000);
		}
	}
}
