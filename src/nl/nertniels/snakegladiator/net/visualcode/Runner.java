package nl.nertniels.snakegladiator.net.visualcode;

import nl.nertniels.snakegladiator.game.Snake;

public class Runner {

	private Block[] blocks;
	
	private Object[] variables;
	
	public int currentLine = 0;
	
	private Snake snake;
	
	public Runner(Snake snake) {
		this.snake = snake;
		
		variables = new Object[4];
		variables[3] = -1f;
		
		blocks = new Block[7];
		blocks[0] = new IfBlock(new Condition(0, 3, 1), 6);
		blocks[1] = new DirectionBlock(1);
		blocks[2] = new IfBlock(new Condition(1, 3, 1), 6);
		blocks[3] = new DirectionBlock(-2);
		blocks[4] = new JumpBlock(6);
		blocks[5] = new DirectionBlock(0);
	}
	
	public void run() {
		for(currentLine = 0; currentLine < blocks.length; currentLine++) {
			blocks[currentLine].run(this);
		}
	}
	
	public Object getVariable(int id) {
		return variables[id];
	}
	
	public void setDirection(int direction) {
		snake.direction += direction;
		if(snake.direction < 0) snake.direction += 4;
		snake.direction %= 4;
	}
	
	public void setTileVariables(int tileAhead, int tileLeft, int tileRight) {
		variables[0] = (float)tileAhead;
		variables[1] = (float)tileLeft;
		variables[2] = (float)tileRight;
		
		System.out.println(tileAhead + ", " + tileLeft + ", " + tileRight);
	}
	
}
