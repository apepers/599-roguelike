package entities;

import java.awt.Color;

public abstract class Weapon extends Holdable {
	private char ascii = '[';
	private Color colour = Color.white;
	
	public char getAscii() {
		return ascii;
	}
	
	public void setAscii(char ascii) {
		this.ascii = ascii;
	}
	
	public Color getColour() {
		return colour;
	}
	
	public void setColour(Color colour) {
		this.colour = colour;
	}
	
}
