package nl.nertniels.snakegladiator.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

import nl.nertniels.snakegladiator.game.Arena;
import nl.nertniels.snakegladiator.game.Snake;
import nl.nertniels.snakegladiator.main.Main;

public class Client extends Thread {

	private InetAddress ipAddress;
	private DatagramSocket socket;
	private Main main;
	
	private int serverPort;
	
	private long lastTimePing;
	public boolean ready = false;
	public long pingTime = -1;
	
	
	public Client(Main main, String serverIp, int serverPort) {
		this.setName("Client Thread");
		this.main = main;
		this.serverPort = serverPort;
		try {
			this.socket = new DatagramSocket();
			this.ipAddress = InetAddress.getByName(serverIp);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		sendData((Packets.CONNECT+main.getPlayerName()).getBytes());
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
			case Packets.PONG:
				pingTime = System.currentTimeMillis() - lastTimePing;
				break;
			case Packets.CONNECT:
				main.setPlayerId(Integer.parseInt(message.substring(2)));
				System.out.println("Server assigned player id " + main.getPlayerId() + " to this client.");
				break;
			case Packets.SEND_CHAT:
				String chatText = message.substring(2, message.length()-2);
				int chatId = Integer.parseInt(message.substring(message.length()-2));
				
				Main.MAIN.chat.addMessage(chatText, chatId);
				
				break;
			case Packets.START_ARENA:
				String[] messageData = message.split("[*]");
				int width = Integer.parseInt(messageData[0].substring(2));
				int height = Integer.parseInt(messageData[1]);
				
				Snake[] snakes = new Snake[Integer.parseInt(messageData[2])];
				
				int s = 0;
				for(int i = 3; i < messageData.length; i += 5) {
					int startId = Integer.parseInt(messageData[i]);
					int startColor = Integer.parseInt(messageData[i+1]);
					String startName = messageData[i+2];
					int x = Integer.parseInt(messageData[i+3]);
					int y = Integer.parseInt(messageData[i+4]);
					
					snakes[s] = new Snake(startId, startColor, startName);
					snakes[s].setLocation(x, y);
					s++;
				}
				
				main.arena = new Arena(width, height, snakes, main);
				
				sendData((Packets.UPDATE_ARENA+main.getPlayerId()).getBytes());
				break;
			case Packets.UPDATE_ARENA:
				if(main.arena == null) break;
				String[] updateData = message.split("[*]");
				for(int i = 1; i < updateData.length; i+=4) {
					int updateId = Integer.parseInt(updateData[i]);
					int updateDirection = Integer.parseInt(updateData[i+1]);
					boolean updateGrow = Boolean.parseBoolean(updateData[i+2]);
					boolean updateDie = Boolean.parseBoolean(updateData[i+3]);
					
					main.arena.updateSnakeDataById(updateId, updateDirection, updateGrow, updateDie);
					main.arena.updatable = true;
				}
				break;
			case Packets.STOP_ARENA:
				if(main.arena == null) break;
				main.arena = null;
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				sendData((Packets.READY+main.getPlayerId()).getBytes());
				break;
			}
		}
	}
	
	public void sendData(byte[] data) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, serverPort);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void ping() {
		lastTimePing = System.currentTimeMillis();
		sendData(Packets.PING.getBytes());
	}
	
	public void ready() {
		sendData((Packets.READY+main.getPlayerId()).getBytes());
		ready = true;
	}
	
	public void doneUpdating() {
		sendData((Packets.UPDATE_ARENA+main.getPlayerId()).getBytes());
	}
	
}
