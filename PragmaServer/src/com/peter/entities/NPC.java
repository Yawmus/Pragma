package com.peter.entities;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

import com.peter.inventory.Item;
import com.peter.map.Map;
import com.peter.packets.AttackPacket;
import com.peter.packets.ItemPacket;
import com.peter.packets.MessagePacket;
import com.peter.packets.NPCPacket;
import com.peter.server.Global;
import com.peter.server.PragmaServer;

public class NPC extends Entity {

	protected int move;
	protected boolean canMove;
	private Stack<Node> moves;
	protected ItemPacket drop;
	public Entity attacker;
	public HostilityList list;
	protected Responses response;

	protected static LinkedList<String> firstNames;
	protected static LinkedList<String> lastNames;
	private static Scanner in;
	
	protected String group;
	
	static{
		firstNames = new LinkedList<String>();
		lastNames = new LinkedList<String>();
		
		try {
			in = new Scanner(new File("C:/Users/Yawmus/Desktop/pragmaFiles/firstName.txt"));
			while(in.hasNextLine())
				firstNames.add(new String(in.nextLine()));
			in.close();
			in = new Scanner(new File("C:/Users/Yawmus/Desktop/pragmaFiles/lastName.txt"));
			
			while(in.hasNextLine())
				lastNames.add(new String(in.nextLine()));
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public NPC(int floor, String group, String race, String type){
		super(race, type);
		this.floor = floor;
		this.group = group;
		if(group != "Monster")
			name = firstNames.get(Global.rand(firstNames.size(), 0)) + " " + lastNames.get(Global.rand(lastNames.size(), 0));
		//time -= Global.rand(15, 0) * .8f;
		canMove = true;
		moves = new Stack<Node>();
		drop = new ItemPacket(Item.getDrop((int) Global.rand(50, -20)), ++Global.count, floor);
		delay = 2.6f + Global.rand(100, 0) * .005f;
		list = new HostilityList();
		response = new Responses(getGroup());
	}
	
	public ItemPacket getDrop(){
		return drop;
	}

	public String getGroup(){
		return group;
	}
	
	public void update(double delta){
		super.update(delta);
		move = Global.rand(5, 0);
		if(time >= delay && canMove){
			if(attacker != null){
				open.add(new Node(getX(), getY()));
				findPath(new Node(attacker.getX(), attacker.getY()), new Node(getX(), getY()));
				open.clear();
				closed.clear();
				if(!moves.empty()){
					setX(moves.peek().x);
					setY(moves.peek().y);
					checkCollision();
					moves.clear();
					time = 1;
				}
				else
					attacker = null;
			}
			
			else{
				switch(move){
				case 0:
					setY(getY() + 32);
					time = 0;
					checkCollision();
					break;
				case 1:
					setY(getY() - 32);
					time = 0;
					checkCollision();
					break;
				case 2:
					setX(getX() - 32);
					time = 0;
					checkCollision();
					break;
				case 3:
					setX(getX() + 32);
					time = 0;
					checkCollision();
					break;
				case 4:
					time = 0;
				}
			}
		}
	}

	public void checkCollision(){
		collision = false;
		//list.displayTypes();
		//  System.out.println(type + "     " + floor + " - " + x + ", " + y); /*BUG TESTING FOR NPC COLLISION*/
		if(PragmaServer.map.getTile(floor, getX(), getY()).isBlocked())
			collision = true;
		else if(PragmaServer.map.getTile(floor, getX(), getY()).getName() == "Door" && group.equals("Shopkeep"))
			collision = true;
		if(Map.markSets.get(floor).get(getX(), getY()) != -1 && Map.markSets.get(floor).get(getX(), getY()) != ID){
			if(Map.chestSets.get(floor).containsKey(Map.markSets.get(floor).get(getX(), getY()))){
				collision = true;
			}
			else if(Map.npcSets.get(floor).containsKey(Map.markSets.get(floor).get(getX(), getY()))){
				if(list.check(Map.npcSets.get(floor).get(Map.markSets.get(floor).get(getX(), getY()))))
					attack(Map.npcSets.get(floor).get(Map.markSets.get(floor).get(getX(), getY())));
				else{
					MessagePacket packet = new MessagePacket();
					packet.callerID = ID;
					packet.floor = floor;
					packet.receiverID = Map.npcSets.get(floor).get(Map.markSets.get(floor).get(getX(),  getY())).ID;
					packet.message = Map.npcSets.get(floor).get(Map.markSets.get(floor).get(getX(),  getY())).getMessage(packet.callerID);
					PragmaServer.server.sendToAllTCP(packet);
				}
			}
			else if(PragmaServer.map.players.containsKey(Map.markSets.get(floor).get(getX(), getY())))
				if(list.check(PragmaServer.map.players.get(Map.markSets.get(floor).get(getX(), getY())))){
					attack(PragmaServer.map.players.get(Map.markSets.get(floor).get(getX(), getY())));
				}
			collision = true;
		}
		
		if(collision){
			setX(oldX);
			setY(oldY);
		}
		else{
			NPCPacket packet = new NPCPacket(getX(), getY(), ID);
			packet.oldX = oldX;
			packet.oldY = oldY;
			Map.markSets.get(floor).put(-1, oldX, oldY);
			Map.markSets.get(floor).put(ID, (int)getX(), (int)getY());
			PragmaServer.server.sendToAllExceptUDP(ID, packet);
		}
		oldX = getX();
		oldY = getY();
	}
	
	public String getMessage(Integer callerID){
		if(Map.npcSets.get(floor).containsKey(callerID))
			return response.call((Entity) Map.npcSets.get(floor).get(callerID), list.check(Map.npcSets.get(floor).get(callerID)));
		else if(PragmaServer.map.players.containsKey(callerID))
			return response.call(PragmaServer.map.players.get(callerID), list.check(PragmaServer.map.players.get(callerID)));
		return null;
	}
	
	protected void attack(Entity entity){
		int amount = 0;
		if(this.getStats().getStrength() == 0)
			amount = Global.rand(3, 0);
		else
			amount = Global.rand(this.getStats().getStrength(), 0);
		amount -= entity.getStats().getDefense();
		if(amount < 0)
			amount = 0;
		amount *= -1;
		
		AttackPacket packet = new AttackPacket();
		packet.attackerID = ID;
		packet.receiverID = entity.ID;
		packet.floor = floor;
		packet.amount = amount;
		for(Player player : PragmaServer.map.players.values())
			if(player.floor == floor)
				PragmaServer.server.sendToTCP(player.ID, packet);
		entity.getStats().mutateHitpoints(amount);
		
		if(entity instanceof NPC){
			if(entity.getStats().getHitpoints() <= 0){
				PragmaServer.removeID = entity.ID;
				PragmaServer.removeFloor = floor;
				attacker = null;
			}
			else{
				((NPC) entity).list.addID(ID);
				((NPC) entity).attacker = this;
			}
		}
	}
	
	private ArrayList<Node> open = new ArrayList<Node>();
	private ArrayList<Node> closed = new ArrayList<Node>(); // 0 up. 1 down. 2 left. 3 right.
	int tempX, tempY;
	Node smallest;
	boolean flag = false;
	
	public void findPath(Node target, Node parent){
		
		// Calculates the H, G and F values
		for(int i=0; i<open.size(); i++){
			
			// Calculates the H values
			tempX = open.get(i).x;
			tempY = open.get(i).y;
			open.get(i).H = 0;
			while(tempX != target.x || tempY != target.y){
				if(target.x == tempX){
					if(target.y > tempY){
						tempY += 32;
					}
					else{
						tempY -= 32;
					}
				}
				else if(target.x > tempX){
					tempX += 32;
				}
				else{
					tempX -= 32;
				}
				open.get(i).H++;
			}
			open.get(i).H *= 10;

			// Calculates the G values
			if(open.get(i).x != parent.x && open.get(i).y != parent.y)
				open.get(i).G = 14 + parent.G;
			else
				open.get(i).G = 10 + parent.G;
			
			// Calculates the F values
			open.get(i).F = open.get(i).G + open.get(i).H;
			
			//font.draw(map.getSpriteBatch(), "X", target.x + 10, target.y + 22);
			//font.draw(map.getSpriteBatch(), Integer.toString(open.get(i).G), open.get(i).x + 10, open.get(i).y + 22);
			/*Global.mapShapes.begin(ShapeType.Line);
			Global.mapShapes.setColor(Color.RED);
			Global.mapShapes.line(open.get(i).x + 14, open.get(i).y + 18, open.get(i).parent.x + 14, open.get(i).parent.y + 18);
			Global.mapShapes.end();*/
		}
		
		// Find the smallest F and close it
		smallest = open.get(0);
		for(int i=0; i<open.size(); i++)
			if(smallest.F > open.get(i).F)
				smallest = open.get(i);

		open.remove(parent);
		closed.add(parent);
		
		// Gets the first square and all of the options around it that arn't blocked or already in open
		for(int i=parent.x-32; i<=parent.x+32; i+=32)
			for(int j=parent.y-32; j<=parent.y+32; j+=32)
				if(!(PragmaServer.map.getTile(floor, i, j).isBlocked() || (Map.markSets.get(floor).get(i, j) != -1 && Map.npcSets.get(floor).containsKey(Map.markSets.get(floor)) 
					&& list.check(Map.npcSets.get(floor).get(Map.markSets.get(floor).get(i, j))) || (i == parent.x && j == parent.y)))){
					flag = false;
					for(int q=0; q<closed.size(); q++)
						if(closed.get(q).x == i && closed.get(q).y == j){
							flag = true;
							break;
						}
					if(!flag){
						flag = false;
						for(int q=0; q<open.size(); q++)
							if(open.get(q).x == i && open.get(q).y == j){
								flag = true;
								if(open.get(q).x != parent.x && open.get(q).y != parent.y)
									tempX = 14;
								else
									tempX = 10;
								if(open.get(q).G > tempX + parent.G){
									open.get(q).parent = parent;
									open.get(q).G = (int)tempX + parent.G;
									open.get(q).F = open.get(q).G + open.get(q).H;
								}
								break;
							}
						if(!flag)
							open.add(new Node(i, j, parent));
					}
				}

		if(!closed.isEmpty() && closed.get(closed.size()-1).x == target.x && closed.get(closed.size()-1).y == target.y){
			//map.getSpriteBatch().end();
			//Global.mapShapes.begin(ShapeType.Line);
			//Global.mapShapes.setColor(Color.RED);
			while(parent.parent != null){
				//Global.mapShapes.line(parent.x + 14, parent.y + 18, parent.parent.x + 14, parent.parent.y + 18);
				moves.push(parent);
				parent = parent.parent;
			}
			
			if(moves.size() > 15)
				attacker = null;
			
			//Global.mapShapes.end();
			//map.getSpriteBatch().begin();
			return;
		}
		else if(open.isEmpty())
			return;
		findPath(target, smallest);
	}
}

class Node{
	int x, y;
	int F, G, H;
	String ID;
	Node parent;
	
	public Node(int x, int y){
		this.x = x;
		this.y = y;
		F = H = G = 0;
		parent = null;
	}
	
	public Node(int x, int y, Node parent){
		this.x = x;
		this.y = y;
		this.parent = parent;
		F = H = G = 0;
	}
}
