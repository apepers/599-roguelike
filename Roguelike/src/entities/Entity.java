package entities;

import java.awt.Color;

public abstract class Entity {
	private char ascii;
	private Color colour;
	public String name;
	
	public char getAscii() {
		return ascii;
	}
	
	public Color getColour() {
		return colour;
	}
	
	public String throwMsg() {
		return "The " + name + " is thrown normally.";
	}
}
