package nl.nertniels.snakegladiator.net.visualcode;

public class IfBlock extends Block {
	
	Condition condition;
	
	private int endLineIf;
	
	public IfBlock(Condition condition, int endLineIf) {
		this.condition = condition;
		this.endLineIf = endLineIf;
	}

	public void run(Runner runner) {
		boolean out = condition.run(runner);
		if(!condition.run(runner)) {
			runner.currentLine = endLineIf;
		} 
	}
	
}
