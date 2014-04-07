package entities;

import graphics.ImageManager;

public class Cursor extends Entity{
	
	private Tile location;
	
	public Cursor(Tile location) {
		this.location = location;
		setImage(ImageManager.getGlobalRegistry().getTile("cursor"));
	}
	
	public String getTopEntity() {
		Entity topEntity = location.getTopEntity();
		if(topEntity != null) {
			return "Name: " + topEntity.getName() + "\nDescription: " + topEntity.getDescription();
		} else {
			return "There's nothing here!";
		}
	}
	
	public Tile getLocation() {
		return location;
	}

	public void setLocation(Tile location) {
		this.location = location;
	}
}
