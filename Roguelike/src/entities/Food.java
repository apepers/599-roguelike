package entities;

import java.awt.Color;

public abstract class Food extends Holdable {
	public char ascii = '%';
	public Color colour = Color.white;
	int nutrition;
	int turnsToEat;
	
	public String eatMsg() {
		return "You eat the " + this.name;
	}
}
