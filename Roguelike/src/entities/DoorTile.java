package entities;

import javax.swing.ImageIcon;

public class DoorTile extends Tile {

	private ImageIcon closed;
	private ImageIcon opened;
	
	/**
	 * Creates a door tile intially closed
	 * @param closed
	 * @param opened
	 */
	public DoorTile(ImageIcon closed, ImageIcon opened){
		this(closed, opened, false);
	}
	
	/**
	 * Creates a door tile intitially opened.
	 * @param closed
	 * @param opened
	 * @param open
	 */
	public DoorTile(ImageIcon closed, ImageIcon opened, boolean open){
		this.setPassable(open); 
		
		this.closed = closed;
		this.opened = opened;
	}
	
	
	/**
	 * Opens the door by making it passable.
	 * Remember to update door tile
	 */
	public void openDoor(){
		this.setPassable(true);
	}
	
	/**
	 * Opens the door by making it impassable.
	 * Remember to update door tile
	 */
	public void closeDoor(){
		this.setPassable(false);
	}
	

	public void setClosedImage(ImageIcon closed) {
		this.closed = closed;
	}

	public void setOpenedImage(ImageIcon opened) {
		this.opened = opened;
	}
	
	@Override
	public ImageIcon getBackground(){
		if (this.isPassable() == true){
			return opened;
		}
		else{
			return closed;
		}
	}
}
