package nl.nertniels.snakegladiator.net;

import java.awt.Color;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Random;

import nl.nertniels.snakegladiator.game.Snake;
import nl.nertniels.snakegladiator.main.Settings;
import nl.nertniels.snakegladiator.net.visualcode.Runner;

public class Server extends Thread {
	private DatagramSocket socket;
	private ServerArena arena;
	
	private ArrayList<Connection> connections;
	
	public boolean everyoneReady = false;
	
	private Random random;
	
	public Server() {
		this.setName("Server Thread");
		try {
			this.socket = new DatagramSocket(25566);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		random = new Random();
		
		System.out.println("Server is running at " + socket.getPort());
		
		connections = new ArrayList<Connection>();
	}
	
	public void run() {
		while(true) {
			byte[] data = new byte[1024];
			
			DatagramPacket packet = new DatagramPacket(data, data.length);
			
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			String message = new String(packet.getData()).trim();
			String type = message.substring(0, 2);
			
			
			switch(type) {
			case Packets.PING:
				sendData(Packets.PONG.getBytes(), packet.getAddress(), packet.getPort());
				break;
			case Packets.CONNECT:
				Connection connection = new Connection(packet.getAddress(), packet.getPort(), message.substring(2, message.length()), connections.size());
				connections.add(connection);
				
				sendData((Packets.CONNECT+connection.id).getBytes(), connection.address, connection.port);
				
				System.out.println("User has connected.");
				System.out.println(connection);
				break;
			case Packets.DISCONNECT:
				int id = Integer.parseInt(message.substring(2));
				Connection disconnection = getConnectionById(id);
				if(disconnection != null) {
					connections.remove(disconnection);
					System.out.println("User has disconnected.");
					System.out.println(disconnection);
				}
				break;
			case Packets.SEND_CHAT:
				break;
			case Packets.READY:
				if(arena == null) {
					getConnectionById(Integer.parseInt(message.substring(2))).ready = true;
					everyoneReady = true;
					for(int i = 0; i < connections.size(); i++) {
						everyoneReady = everyoneReady && connections.get(i).ready;
					}
					if(everyoneReady) {
						System.out.println("Everyone seems to be ready to start.");
						startArena();
						for(Connection c : connections) {
							c.ready = false;
						}
					}
				}
				break;
			case Packets.UPDATE_ARENA:
				if(arena != null) {
					
					getConnectionById(Integer.parseInt(message.substring(2))).ready = true;
					everyoneReady = true;
					for(int i = 0; i < connections.size(); i++) {
						everyoneReady = everyoneReady && connections.get(i).ready;
					}
					if(everyoneReady) {
						new Thread(new Runnable() {
							@Override
							public void run() {
								long time = System.currentTimeMillis();
								String update = arena.update();
								try {
									long timePassed = System.currentTimeMillis() - time;
									if(timePassed < Settings.ROUND_TIME_MILLIS) Thread.sleep(Settings.ROUND_TIME_MILLIS-timePassed);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								sendDataAll(update.getBytes());
							}
						}).start();
						
						for(Connection c : connections) {
							c.ready = false;
						}
						
					}
					
				}
				break;
			}
			
		}
	}
	
	public void sendData(byte[] data, InetAddress ipAddress, int port) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendDataAll(byte[] data) {
		for(Connection c : connections) {
			DatagramPacket packet = new DatagramPacket(data, data.length, c.address, c.port);
			try {
				socket.send(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Connection getConnectionById(int id) {
		for(int i = 0; i < connections.size(); i++) {
			if(connections.get(i).id == id) {
				return connections.get(i);
			}
		}
		return null;
	}
	
	private void startArena() {
		Snake[] snakes = new Snake[connections.size()];
		for(int i = 0; i < snakes.length; i++) {
			Connection c = connections.get(i);
			snakes[i] = new Snake(c.id, new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()).getRGB(), c.name);
		}
		
		arena = new ServerArena(Settings.ARENA_WIDTH, Settings.ARENA_HEIGHT, snakes);
		
		String message = Packets.START_ARENA;
		message += arena.width+"*"+arena.height+"*"+snakes.length;
		for(int i = 0; i < snakes.length; i++) {
			
			int x = 1+random.nextInt(arena.width-2);
			int y = 1+random.nextInt(arena.height-2);
			
			message += "*"+snakes[i].playerId+"*"+snakes[i].color+"*"+snakes[i].playerName+"*"+x+"*"+y;
			
			snakes[i].setLocation(x, y);
		}
		
		sendDataAll(message.getBytes());
	}
}

class Connection {
	public InetAddress address;
	public int port;
	public String name;
	public int id;
	
	public boolean ready = false;
	
	public Connection(InetAddress address, int port, String name, int id) {
		this.address = address;
		this.port = port;
		this.name = name;
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "IP: " + address.getHostAddress() + ":" + port + ", Name: " + name + ", ID: " + id;
	}
}
