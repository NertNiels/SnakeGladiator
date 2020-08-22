package nl.nertniels.snakegladiator.net;

import java.util.Random;

import nl.nertniels.snakegladiator.game.Snake;
import nl.nertniels.snakegladiator.main.Main;

public class ServerArena {

	private Snake[] snakes;
	
	public int width;
	public int height;
	
	private Random random;
	
	private int[][] tiles;
	
	public ServerArena(int width, int height, Snake[] snakes) {
		this.width = width;
		this.height = height;
		
		this.snakes = snakes;
		random = new Random();
		
		tiles = new int[width][height];
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				tiles[i][j] = (i == 0 || i == width - 1 || j == 0 || j == height-1) ? -2 : -1;
			}
		}
	}
	
	public String update() {
		String out = Packets.UPDATE_ARENA;
		
		for(Snake s : snakes) {
			int xTileAhead = s.head.x+Snake.DIRECTIONS[s.direction].x;
			int yTileAhead = s.head.y+Snake.DIRECTIONS[s.direction].y;
			int tileAhead = (xTileAhead < 0 || xTileAhead >= width || yTileAhead < 0 || yTileAhead >= height) ? -2 : tiles[xTileAhead][yTileAhead];
			
			int xTileLeft = s.head.x+Snake.DIRECTIONS[s.direction].y;
			int yTileLeft = s.head.y-Snake.DIRECTIONS[s.direction].x;
			int tileLeft = (xTileLeft < 0 || xTileLeft >= width || yTileLeft < 0 || yTileLeft >= height) ? -2 : tiles[xTileLeft][yTileLeft];
			
			int xTileRight = s.head.x-Snake.DIRECTIONS[s.direction].y;
			int yTileRight = s.head.y+Snake.DIRECTIONS[s.direction].x;
			int tileRight = (xTileRight < 0 || xTileRight >= width || yTileRight < 0 || yTileRight >= height) ? -2 : tiles[xTileRight][yTileRight];
			
			s.runner.setTileVariables(tileAhead, tileLeft, tileRight);
			s.runner.run();
		}
		
		for(Snake s : snakes) {
//			s.direction = random.nextInt(4);
			if(s.dead) continue;
			s.grow(random.nextBoolean());
			s.update(tiles);
		}
		
		for(int i = 0; i < snakes.length; i++) {
			if(snakes[i].dead) continue;
			for(int j = 0; j < snakes.length; j++) {
				if(snakes[i].testCollide(snakes[j])) {
					System.out.println("The snake has collided with a snake!");
					snakes[i].die(true);
					
				}
			}
			
			if(tiles[snakes[i].head.x][snakes[i].head.y] == -2) {
				System.out.println("The snake has collided with a wall!");
				snakes[i].die(true);
			}
			

			out += snakes[i].getPacketString();
		}
		
		return out;
	}
	
	
	
}
