package entities;

public class Lembas extends Food {
	public Lembas() {
		cost = 45;
		nutrition = 800;
		turnsToEat = 2;
		weight = 5;
		name = "lembas";
	}
	
	@Override
	public String eatMsg() {
		return "You eat the lembas. It is delicious!";
	}
}
