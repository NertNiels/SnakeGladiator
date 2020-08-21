package nl.nertniels.snakegladiator.net.visualcode;

public class DirectionBlock extends Block {

	private int direction;
	
	public DirectionBlock(int direction) {
		this.direction = direction;
	}
	
	public void run(Runner runner) {
		runner.setDirection(direction);
	}
	
}
