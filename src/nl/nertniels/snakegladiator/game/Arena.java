package nl.nertniels.snakegladiator.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import nl.nertniels.snakegladiator.main.Main;
import nl.nertniels.snakegladiator.main.Settings;

public class Arena {
	
	private BufferedImage image;
	private int[] pixels;
	private int width;
	private int height;
	
	private Snake[] snakes;
	
	public boolean updatable;
	private Main main;
	
	public Arena(int arenaWidth, int arenaHeight, Snake[] snakes, Main main) {
		image = new BufferedImage(arenaWidth, arenaHeight, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		
		width = arenaWidth;
		height = arenaHeight;
		
		
		this.snakes = snakes;
		this.main = main;
	}
	
	public void update() {
		
		
		if(!updatable) return;
		for(int i = 0; i < snakes.length; i++) {
			snakes[i].update();
		}
		
		for(int i = 0; i < snakes.length; i++) {
			for(int j = 0; j < snakes.length; j++) {
				if(i == j) continue;
				if(snakes[i].testCollide(snakes[j])) {
					System.out.println("Collision");
				}
			}
		}
		
		updatable = false;
		main.updated();
	}
	
	public void render(Graphics g) {
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = Color.black.getRGB();
		}
		for(int i = 0; i < snakes.length; i++) {
			snakes[i].render(pixels, image.getWidth(), image.getHeight());
		}
		
		g.drawImage(image, 0, 0, Settings.WINDOW_WIDTH, Settings.WINDOW_HEIGHT, null);
	}
	
	public void updateSnakeDataById(int id, int direction, boolean grow) {
		snakes[id].direction = direction;
		snakes[id].grow(grow);
	}
	
	@Override
	public String toString() {
		String out =  "Arena " + width + "*" + height;
		for(int i = 0; i < snakes.length; i++) {
			out += "\n"+snakes[i];
		}
		return out;
	}
	
}
