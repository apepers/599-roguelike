package entities;

import graphics.ImageManager;

public class Cursor extends Entity{
	
	private Tile location;
	
	public Cursor(Tile location) {
		this.location = location;
		setImage(ImageManager.getGlobalRegistry().getTile("cursor"));
	}
	
	public Entity getTopEntity() {
		Entity topEntity = location.getTopEntity();
		if(topEntity != null) {
			return topEntity;
			//return "Name: " + topEntity.getName() + "\nDescription: \n" + topEntity.getDescription();
		} else {
			return null;
			//return "There's nothing here!";
		}
	}
	
	public Tile getLocation() {
		return location;
	}

	public void setLocation(Tile location) {
		this.location = location;
	}
}
