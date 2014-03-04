package entities;

import java.awt.Color;

@SuppressWarnings("serial")
public abstract class Entity implements java.io.Serializable {
	private char ascii;
	private Color colour;
	private String name;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
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
