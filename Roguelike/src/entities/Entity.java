package entities;

import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;

@SuppressWarnings("serial")
public abstract class Entity implements java.io.Serializable {
	private String name;
	private String description = "Nothing too interesting";
	
	private ImageIcon img;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public String throwMsg() {
		return "The " + name + " is thrown normally.";
	}
	
	public ImageIcon getImg(){
		return img;
	}
	
	protected void setImage(ImageIcon img){
		this.img = img;
	}
}
