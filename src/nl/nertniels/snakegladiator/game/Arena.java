package nl.nertniels.snakegladiator.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
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
	
	private Point[] walls;
	
	public boolean updatable;
	private Main main;
	
	public Arena(int arenaWidth, int arenaHeight, Snake[] snakes, Main main) {
		image = new BufferedImage(arenaWidth, arenaHeight, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		
		width = arenaWidth;
		height = arenaHeight;
		
		
		this.snakes = snakes;
		this.main = main;
		
		walls = new Point[width*2+height*2-4];
		
		for(int i = 0; i < width; i++) {
			walls[i] = new Point(i, 0);
			walls[i+width] = new Point(i, height-1);
		}
		for(int i = 0; i < height-2; i++) {
			walls[i+width*2] = new Point(0, i+1);
			walls[i+width*2+height-2] = new Point(width-1, i+1);
		}
		
	}
	
	public void update() {
		
		
		if(!updatable) return;
		for(int i = 0; i < snakes.length; i++) {
			snakes[i].update(null);
//			System.out.println("Client: " + snakes[i].head.x + ", " + snakes[i].head.y);
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
	
	public void render(Graphics g, int x, int y, int renderWidth, int renderHeight) {
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = Color.black.getRGB();
		}
		for(int i = 0; i < snakes.length; i++) {
			snakes[i].render(pixels, image.getWidth(), image.getHeight());
		}
		
		for(int i = 0; i < walls.length; i++) {
			pixels[walls[i].y*width+walls[i].x] = Color.gray.getRGB();
		}
		
		g.drawImage(image, x, y, renderWidth, renderHeight, null);
	}
	
	public void updateSnakeDataById(int id, int direction, boolean grow, boolean die) {
		snakes[id].direction = direction;
		snakes[id].grow(grow);
		snakes[id].die(die);
	}
	
	@Override
	public String toString() {
		String out =  "Arena " + width + "*" + height;
		for(int i = 0; i < snakes.length; i++) {
			out += "\n"+snakes[i];
		}
		return out;
	}

	public Snake getSnakeById(int id) {
		return snakes[id];
	}
	
}
