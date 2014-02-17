package entities;

import java.awt.Color;

public class Ration extends Food {
	public Ration() {
		cost = 45;
		nutrition = 800;
		turnsToEat = 5;
		weight = 20;
		name = "food ration";
		colour = new Color(50, 50, 50);
	}
}
