package nl.nertniels.snakegladiator.net.visualcode;

public class PrintBlock extends Block {
	
	private String value = null;;
	private int variableId = -1;
	
	public PrintBlock(String value) {
		this.value = value;
		this.variableId = -1;
	}
	
	public PrintBlock(int valueId) {
		this.variableId = valueId;
	}

	public void run(Runner runner) {
		if(variableId < 0) System.out.println(value);
		else System.out.println(runner.getVariable(variableId));
	}
	
}
