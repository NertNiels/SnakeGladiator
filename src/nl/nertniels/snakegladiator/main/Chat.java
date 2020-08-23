package nl.nertniels.snakegladiator.main;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import nl.nertniels.snakegladiator.game.Snake;
import nl.nertniels.snakegladiator.net.Packets;

public class Chat {
	
	private ArrayList<Message> messages;
	public boolean opened;
	
	private int x;
	private int y;
	private int width;
	private int height;
	
	public Rectangle input;
	public boolean focus;
	public String inputText = "";

	public Chat() {
		messages = new ArrayList<Message>();
		opened = false;
		
		input = new Rectangle();
	}
	
	public void update() {
		
	}
	
	public void render(Graphics g) {
		g.setFont(Settings.CHAT_FONT);
		FontMetrics fm = g.getFontMetrics();
		int offset = fm.getHeight();
		
		x = 20;
		y = 20;
		width = Main.MAIN.getWidth()/2-40;
		height = Main.MAIN.getHeight()-40;

		input.setLocation(x, Main.MAIN.getHeight()-20-fm.getHeight());
		input.setSize(width, fm.getHeight());
		
		g.setColor(Color.DARK_GRAY);
		g.fillRect(x,Main.MAIN.getHeight()-20-fm.getHeight(),width,fm.getHeight());
		
		int size = focus ? messages.size() : (messages.size() < 3 ? messages.size() : 3);
		for(int i = 0; i < size; i++) {
			Message m = messages.get(i);
			
			g.setColor(m.color);
			g.drawString(m.text, x, Main.MAIN.getHeight()-30-offset);
			offset += fm.getHeight();

		}
		g.setColor(Color.white);
		g.drawString(inputText, x, Main.MAIN.getHeight()-20);
	}
	
	public void send() {
		if(inputText.isEmpty()) return;
		String packet = Packets.SEND_CHAT+inputText+String.format("%02d", Main.MAIN.getPlayerId());
		Main.MAIN.client.sendData(packet.getBytes());
		inputText = "";
	}
	
	public void backspace() {
		if(inputText.length() == 0) return;
		inputText = inputText.substring(0, inputText.length()-1);
	}

	public void addMessage(String chatText, int chatId) {
		if(chatId < 0) {
			Message message = new Message("Server: " + chatText, chatId, Color.red);
			messages.add(0, message);
		} else {
			Snake player = Main.MAIN.arena.getSnakeById(chatId);
			Message message = new Message(player.playerName + ": " + chatText, chatId, new Color(player.color));
			messages.add(0, message);
		}
	}
	
}

class Message {
	
	public String text;
	public int playerId;
	public Color color;
	public int displayTime;
	
	public Message(String text, int playerId, Color color) {
		this.text = text;
		this.playerId = playerId;
		this.color = color;
		
		displayTime = Settings.CHAT_DISPLAY_TIME;
	}
}
