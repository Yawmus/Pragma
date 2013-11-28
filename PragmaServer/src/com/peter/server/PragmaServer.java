package com.peter.server;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.peter.entities.Citizen;
import com.peter.entities.NPC;
import com.peter.map.Map;
import com.peter.packets.AddNPCPacket;
import com.peter.packets.AddPlayerPacket;
import com.peter.packets.AddTradeItemPacket;
import com.peter.packets.ChestPacket;
import com.peter.packets.ItemPacket;
import com.peter.packets.MapPacket;
import com.peter.packets.MessagePacket;
import com.peter.packets.NPCPacket;
import com.peter.packets.Player;
import com.peter.packets.PlayerPacket;
import com.peter.packets.RemoveItemPacket;
import com.peter.packets.RemovePlayerPacket;
import com.peter.packets.RemoveTradeItemPacket;

public class PragmaServer{

	public static Server server;
	
	//Ports to listen on
	private static int udpPort = 23989, tcpPort = 23989;
	public final int HEIGHT = 40, WIDTH = 80;
	public static int count = 1000;
	public static Map map = new Map();
	
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
			temp3 = new Citizen("c_.png");
			map.npcs.put(++count, temp3);
			temp3.ID = count;
			temp3 = map.npcs.get(count);
			temp3.setPosition(/*Global.rand(13, 3), Global.rand(7, 3)*/ 28, 8);
			map.marks.put(temp3.ID, temp3.getX(), temp3.getY());
		}
		
		connect();
	}
	
	public static void connect() throws Exception {
		System.out.println("Creating the server...");
		
		server = new Server();
		server.getKryo().register(PlayerPacket.class);
		server.getKryo().register(NPCPacket.class);
		server.getKryo().register(AddNPCPacket.class);
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
				Player player = new Player();
				player.x = player.y = player.oldX = player.oldY = 0;
				player.c = c;
				
				AddPlayerPacket packet = new AddPlayerPacket();
				packet.ID = c.getID();
				packet.x = 28*32;
				packet.y = 7*32;
				server.sendToAllExceptTCP(c.getID(), packet);
				
				for(Player p : map.players.values()){
					AddPlayerPacket packet2 = new AddPlayerPacket();
					packet2.ID = p.c.getID();
					c.sendTCP(packet2);
				}
				
				map.players.put(c.getID(), player);
				
				// Sync map information
				MapPacket packet2 = new MapPacket();
				packet2.items = map.items;
				packet2.chests = map.chests;
				packet2.npcs = new HashMap<Integer, AddNPCPacket>();
				for(NPC npc : map.npcs.values())
					packet2.npcs.put(npc.ID, new AddNPCPacket(npc.getX(), npc.getY(), npc.ID, npc.getType(), npc.getName()));
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
					System.out.println(packet.message);
				}
				else if(o instanceof PlayerPacket){
					PlayerPacket packet = (PlayerPacket) o;
					//if(map.marks.get(packet.x, packet.y) == -1 || map.marks.get(packet.x, packet.y) == c.getID()){
						map.players.get(c.getID()).x = packet.x;
						map.players.get(c.getID()).y = packet.y;
						map.marks.put(c.getID(), packet.x, packet.y);
						map.players.get(c.getID()).oldX = packet.oldX;
						map.players.get(c.getID()).oldY = packet.oldY;
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
				else if(o instanceof RemoveTradeItemPacket){
					RemoveTradeItemPacket packet = (RemoveTradeItemPacket) o;
					map.chests.get(packet.ID).items.remove(packet.index);
					server.sendToAllExceptUDP(c.getID(), packet);
				}
				else if(o instanceof AddTradeItemPacket){
					AddTradeItemPacket packet = (AddTradeItemPacket) o;
					map.chests.get(packet.ID).items.add(packet.item);
					server.sendToAllExceptUDP(c.getID(), packet);
				}
				else
					System.out.println("Missed!");
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
		/*while(true){
			for(NPC npc : map.npcs.values())
				npc.update(Gdx.graphics.getDeltaTime());
		}*/
	}
}
