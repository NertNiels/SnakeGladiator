package nl.nertniels.snakegladiator.net;

import java.util.Random;

import nl.nertniels.snakegladiator.game.Snake;

public class ServerArena {

	private Snake[] snakes;
	
	public int width;
	public int height;
	
	private Random random;
	
	public ServerArena(int width, int height, Snake[] snakes) {
		this.width = width;
		this.height = height;
		
		this.snakes = snakes;
		random = new Random();
	}
	
	public String update() {
		String out = Packets.UPDATE_ARENA;
		
		for(Snake s : snakes) {
			s.direction = random.nextInt(4);
			s.grow(random.nextBoolean());
			out += s.update();
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
