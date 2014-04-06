package entities;

import java.awt.Color;
import java.awt.Image;

@SuppressWarnings("serial")
public abstract class Entity implements java.io.Serializable {
	private String name;
	
	private Image img;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
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
