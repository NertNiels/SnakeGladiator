package nl.nertniels.snakegladiator.net.visualcode;

public class JumpBlock extends Block {

	private int lineToJump;
	
	public JumpBlock(int lineToJump) {
		this.lineToJump = lineToJump;
	}
	
	public void run(Runner runner) {
		runner.currentLine = lineToJump;
	}
	
}
