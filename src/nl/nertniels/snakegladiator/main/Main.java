package nl.nertniels.snakegladiator.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;

import nl.nertniels.snakegladiator.game.Arena;
import nl.nertniels.snakegladiator.game.Snake;
import nl.nertniels.snakegladiator.net.Client;
import nl.nertniels.snakegladiator.net.Server;

public class Main extends Canvas implements Runnable {
	
	public static Main MAIN;
	
	private InputHandler inputHandler;
	private Window window;
	public static boolean RUNNING = false;
	
	private BufferedImage image = new BufferedImage(Settings.WINDOW_WIDTH/4, Settings.WINDOW_HEIGHT/4, BufferedImage.TYPE_INT_RGB);
	
	public Arena arena;
	public Chat chat;
	
	public Client client;
	private Server server;
	
	private String playerName;
	private int playerId;
	
	public Main() {
		setPreferredSize(new Dimension(Settings.WINDOW_WIDTH, Settings.WINDOW_HEIGHT));
		window = new Window(this);
		
		inputHandler = new InputHandler();
		this.addKeyListener(inputHandler);
		this.addMouseListener(inputHandler);
		this.requestFocus();
		Thread.currentThread().setName("Main");
		start();
		
		client.sendData("ping".getBytes());
	}
	
	public synchronized void start() {
		RUNNING = true;
		
		String serverIp = "localhost";
		int serverPort = 25566;
		
		int runServer = JOptionPane.showConfirmDialog(this, "Do you want to run the server?");
		
		if(runServer == 0) {
			server = new Server();
			server.start();
		} else if(runServer == 1) {
			serverIp = JOptionPane.showInputDialog(this, "Input the server ip.", "localhost");
			serverPort = Integer.parseInt(JOptionPane.showInputDialog(this, "Input the server port.", "25566"));
		} else {
			System.exit(0);
		}
		
		playerName = "";
		while(playerName.isEmpty()) {
			playerName = JOptionPane.showInputDialog(this, "Please enter your username.");
		}
		playerName = playerName.toUpperCase();
		
		client = new Client(this, serverIp, serverPort);
		client.start();
		
		new Thread(this, Settings.TITLE).start();
	}
	
	public synchronized void stop() {
		RUNNING = false;
	}
	@Override
	public void run() {
		Snake[] snakes = new Snake[1];
		snakes[0] = new Snake(0, Color.blue.getRGB(), "NertNiels");
		arena = new Arena(20, 20, snakes, this);
		
		chat = new Chat();
		
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000d/60d;
		
		int updates = 0;
		int frames = 0;
		
		long milTime = System.currentTimeMillis();
		double delta = 0;
		
		boolean render = false;
		while(RUNNING) {
			long now = System.nanoTime();
			delta += (now-lastTime)/nsPerTick;
			lastTime = now;
			
			while(delta >= 1) {
				updates++;
				update();
				delta -= 1;
				render = true;
			}
			
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(render) {
				frames++;
				render();
				render = false;
			}
			
			if(System.currentTimeMillis() - milTime >= 1000) {
				milTime += 1000;
				System.out.println("FPS: " + frames + ", TPS: " + updates + (client.pingTime == -1 ? "" : (", PING: " + client.pingTime+"ms")));

				client.ping();
				frames = 0;
				updates = 0;
			}
		}
		
		System.exit(0);
	}
	
	public void update() {
		if(arena != null) arena.update();
		
		if(!client.ready && InputHandler.SHIFT) {
			client.ready();
		}
		
		chat.update();
	}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		if(getWidth()/2 > getHeight() && arena != null) arena.render(g, getWidth()-getHeight(), 0, getHeight(), getHeight());
		else if(arena != null) arena.render(g, getWidth()/2, getHeight()/2-getWidth()/4, getWidth()/2, getWidth()/2);
		
		chat.render(g);
		
		g.dispose();
		bs.show();
	}
	
	public void updated() {
		client.doneUpdating();
	}
	
	public String getPlayerName() {
		return playerName;
	}
	
	public int getPlayerId() {
		return playerId;
	}
	
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	
	public static void main(String[] args) {
		MAIN = new Main();
	}
	
	public void toggleFullScreen() {
		window.toggleFullScreen();
	}

}
