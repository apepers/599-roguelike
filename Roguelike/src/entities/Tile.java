/* Simple placeholder class for a tile on the map
 * 
 */

package entities;

import java.awt.Image;
import java.security.InvalidKeyException;
import java.util.HashMap;


public class Tile {
	
	private Container items = new Container();	// The items in the tile
	private Sentient occupant = null;

	private Image background;
	private boolean passable;
	
	/**
	 * Creates a new tile object with a blank image key.
	 */
	public Tile() { }

	public Tile(boolean passable){
		this.passable = passable;
	}
	
	public Holdable getItem(Character itemID) {
		return items.getItem(itemID);
	}

	// Get the items in the tile, as a container
	public Container getItems() {
		return items;
	}

	public int getItemCount(){
		return items.getSize();
	}
	
	// Get a list of all the items in the tile
	public HashMap<Character, Holdable> getAllItems() {
		return items.getAllItems();
	}

	// Add an item to the pile in the tile
	public void addItem(Holdable item) {
		items.addItem(item);
	}

	// Remove the item
	public Holdable removeItem(Character itemID) throws InvalidKeyException {
		Holdable item = items.removeItem(itemID);
		return item;
	}

	// Remove a certain number of stackable items
	public Holdable removeItem(Character itemID, int count) throws InvalidKeyException {
		Holdable item = items.removeStackedItem(itemID, count);
		return item;
	}

	// Display the contents of the tile from the items list
	public void displayItems() {
		if (items.getSize() == 0)
			System.out.println("There is nothing in this tile");
		else {
			System.out.println("Here there is:");
			items.display();
		}
	}

	// Get the sentient currently standing in the square
	// Returns null if no sentient is on the square.
	public Sentient getOccupant() {
		return occupant;
	}

	/**
	 * Checks to see if the tile is currently occupied.
	 * @return
	 */
	public boolean isOccupied(){
		return occupant != null;
	}
	// Check is there is a sentient standing in the square or not
	public boolean tileFree() {
		if (occupant != null)
			return false;
		return true;
	}

	// Set a new sentient standing in the square, as long as it wasn't occupied already
	// Also sets the sentient's location to be this tile.
	public void setOccupant(Sentient _occupant) {
		if (this.tileFree()) {
			occupant = _occupant;
			occupant.setLocation(this);
		}
	}

	// Take the sentient standing in this tile out, nulling its location, and returns it.
	public Sentient removeOccupant() {
		Sentient _occupant = this.occupant;
		this.occupant = null;
		_occupant.setLocation(null);
		return _occupant;

	}

	
	/**
	 * Gets the top most appropriate image to display on
	 * the screen.
	 * 
	 * Drawing priority:
	 * 1. Sentients
	 * 2. Items
	 * 3. Background of tile
	 */
	public Image getTopImage(){
		if(occupant != null){
			return occupant.getImg();
		}
		else if (items.getSize() > 0){
			//TODO horriable way to get first item... PLEASE FIX
			return items.getItem(items.getUsedIDs().iterator().next()).getImg();
		}
		return background;
	}
	
	
	/**
	 * Gets the background image of the tile. 
	 * This is the tile that is draw on the backend.
	 * Do not use this method to get the top most
	 * entity image.
	 * @return
	 */
	public Image getBackground() {
		return background;
	}
	
	public void setBackground(Image background){
		this.background = background;
	}

	public boolean isPassable() {
		return passable;
	}

	public void setPassable(boolean passable) {
		this.passable = passable;
	}

	
	
}