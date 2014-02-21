package entities;

public abstract class Holdable extends Entity {
	int weight;
	int cost = 0;
	
	public String eatMsg() {
		return "You can't eat that.";
	}
}
