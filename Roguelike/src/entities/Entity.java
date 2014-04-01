package entities;

import java.awt.Color;
import java.awt.Image;

@SuppressWarnings("serial")
public abstract class Entity implements java.io.Serializable {
	private char ascii;
	private Color colour;
	private String name;
	
	private Image img;
	
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
	
	public Image getImg(){
		return img;
	}
	
	protected void setImage(Image img){
		this.img = img;
	}
}
