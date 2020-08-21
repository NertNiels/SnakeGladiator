package nl.nertniels.snakegladiator.net.visualcode;

public class Condition {

	private int value1;
	private int value2;
	
	private int type; // 0: ==, 1: !=, 2: >, 3: <, 4: >=, 5: <=
	
	public Condition(int value1, int value2, int type) {
		this.value1 = value1;
		this.value2 = value2;
		this.type = type;
	}
	
	public boolean run(Runner runner) {
		Object variable1 = runner.getVariable(value1);
		Object variable2 = runner.getVariable(value2);
		if(variable1 instanceof Float && variable2 instanceof Float) {
			float number1 = (float) variable1;
			float number2 = (float) variable2;
			if(type == 0) return number1 == number2;
			if(type == 1) return number1 != number2;
			if(type == 2) return number1 >  number2;
			if(type == 3) return number1 <  number2;
			if(type == 4) return number1 >= number2;
			if(type == 5) return number1 <= number2;
		} else {
			if(type == 0) return variable1 == variable2;
			if(type == 1) return variable1 != variable2;
		}
		return false;
	}
	
}
