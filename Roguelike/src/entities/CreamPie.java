package entities;

import java.awt.Color;

public class CreamPie extends Food {
	public CreamPie() {
		cost = 10;
		nutrition = 100;
		turnsToEat = 1;
		weight = 10;
		name = "cream pie";
		colour = Color.yellow;
	}
	
	@Override
	public String throwMsg() {
		return "The cream pie hits the ground with a splat!";
		// Would have special code for if it hit a person
	}
}
