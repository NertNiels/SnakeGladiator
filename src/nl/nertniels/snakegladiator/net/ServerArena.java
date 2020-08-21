package nl.nertniels.snakegladiator.net;

import java.util.Random;

import nl.nertniels.snakegladiator.game.Snake;

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
				tiles[i][j] = -1;
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
			s.grow(random.nextBoolean());
			out += s.update(tiles);
		}
		
		for(int i = 0; i < snakes.length; i++) {
			for(int j = 0; j < snakes.length; j++) {
				if(i == j) continue;
				if(snakes[i].testCollide(snakes[j])) {
					System.out.println("Collision");
				}
			}
		}
		
		return out;
	}
	
}
