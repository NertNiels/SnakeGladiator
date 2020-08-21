package nl.nertniels.snakegladiator.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JOptionPane;

import nl.nertniels.snakegladiator.game.Arena;
import nl.nertniels.snakegladiator.game.Snake;
import nl.nertniels.snakegladiator.net.Client;
import nl.nertniels.snakegladiator.net.Server;

public class Main extends Canvas implements Runnable {
	
	public static Main MAIN;
	
	private Window window;
	public boolean running = false;
	
	private BufferedImage image = new BufferedImage(Settings.WINDOW_WIDTH/4, Settings.WINDOW_HEIGHT/4, BufferedImage.TYPE_INT_RGB);
	
	public Arena arena;
	
	private Client client;
	private Server server;
	
	private String playerName;
	private int playerId;
	
	public Main() {
		setPreferredSize(new Dimension(Settings.WINDOW_WIDTH, Settings.WINDOW_HEIGHT));
		window = new Window(this);
		this.addKeyListener(new InputHandler());
		this.requestFocus();
		Thread.currentThread().setName("Main");
		start();
		
		client.sendData("ping".getBytes());
	}
	
	public synchronized void start() {
		running = true;
		
		String serverIp = "localhost";
		int serverPort = 25566;
		
		int runServer = JOptionPane.showConfirmDialog(this, "Do you want to run the server?");
		
		if(runServer == 0) {
			server = new Server();
			server.start();
		} else if(runServer == 1) {
			serverIp = JOptionPane.showInputDialog(this, "Input the server ip.");
			serverPort = Integer.parseInt(JOptionPane.showInputDialog(this, "Input the server port."));
		} else {
			System.exit(0);
		}
		
		playerName = JOptionPane.showInputDialog(this, "Please enter your username.");
		
		client = new Client(this, serverIp, serverPort);
		client.start();
		
		new Thread(this, Settings.TITLE).start();
	}
	
	public synchronized void stop() {
		running = false;
	}
	@Override
	public void run() {
		Snake[] snakes = new Snake[2];
		snakes[0] = new Snake(0, Color.red.getRGB(), "NertNiels");
		snakes[1] = new Snake(1, Color.blue.getRGB(), "Stannanman");
		arena = new Arena(20, 20, snakes, this);
		
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000d/60d;
		
		int updates = 0;
		int frames = 0;
		
		long milTime = System.currentTimeMillis();
		double delta = 0;
		
		boolean render = false;
		while(running) {
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
	}
	
	public void update() {
		arena.update();
		
		if(!client.ready && InputHandler.SHIFT) {
			client.ready();
		}
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
		
		arena.render(g);
		
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

}
