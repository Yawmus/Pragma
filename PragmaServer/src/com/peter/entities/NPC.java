package com.peter.entities;

import java.util.ArrayList;
import java.util.Stack;

import com.peter.packets.AttackPacket;
import com.peter.packets.ItemPacket;
import com.peter.packets.MessagePacket;
import com.peter.packets.NPCPacket;
import com.peter.server.Global;
import com.peter.server.PragmaServer;

public class NPC extends Animate {

	protected int move;
	protected boolean canMove;
	private Stack<Node> moves;
	private ItemPacket drop;
	
	public NPC(String type){
		super(type);
		name = "Bob"/*firstNames.get(Global.rand(firstNames.size(), 0)) + " " + lastNames.get(Global.rand(lastNames.size(), 0))*/;
		//time -= Global.rand(15, 0) * .8f;
		System.out.println(time);
		canMove = true;
		moves = new Stack<Node>();
		drop = Global.rand(2, 0) == 1 ? new ItemPacket("Gold") : new ItemPacket("Gem");
		delay = 2.6f + Global.rand(100, 0) * .005f;
	}
	
	public ItemPacket getDrop(){
		return drop;
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
		if(PragmaServer.map.getTile(getX(), getY()).isBlocked())
			collision = true;
		if(PragmaServer.map.marks.get(getX(), getY()) != -1 && PragmaServer.map.marks.get(getX(), getY()) != ID){
			if(PragmaServer.map.items.containsKey(PragmaServer.map.marks.get(getX(), getY())) || PragmaServer.map.chests.containsKey(PragmaServer.map.marks.get(getX(), getY()))){
			}
			else if(PragmaServer.map.npcs.containsKey(PragmaServer.map.marks.get(getX(), getY()))){
					System.out.println("hit another npc!");
					MessagePacket packet = new MessagePacket();
					packet.callerID = ID;
					packet.receiverID = PragmaServer.map.npcs.get(PragmaServer.map.marks.get(getX(),  getY())).ID;
					packet.message = PragmaServer.map.npcs.get(PragmaServer.map.marks.get(getX(),  getY())).getMessage(packet.callerID);
					PragmaServer.server.sendToAllTCP(packet);
			}
			else{
				if(PragmaServer.map.players.get(PragmaServer.map.marks.get(getX(), getY())) != null)
					attack(PragmaServer.map.players.get(PragmaServer.map.marks.get(getX(), getY())));
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
			PragmaServer.map.marks.put(-1, oldX, oldY);
			PragmaServer.map.marks.put(ID, (int)getX(), (int)getY());
			PragmaServer.server.sendToAllExceptUDP(ID, packet);
		}
		oldX = getX();
		oldY = getY();
	}
	
	protected void attack(Animate entity){
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
		packet.amount = amount;
		PragmaServer.server.sendToAllUDP(packet);
		entity.getStats().mutateHitpoints(amount);
		entity.setStatus(amount);
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
				if(!(PragmaServer.map.getTile(i, j).isBlocked() ||  PragmaServer.map.chests.containsKey(PragmaServer.map.marks.get(i, j))
						|| PragmaServer.map.items.containsKey(PragmaServer.map.marks.get(i, j))
						|| PragmaServer.map.npcs.containsKey(PragmaServer.map.marks.get(i, j)) || (i == parent.x && j == parent.y))){
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
