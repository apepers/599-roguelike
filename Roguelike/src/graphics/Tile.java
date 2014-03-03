/* Simple placeholder class for a tile on the map
 * 
 */

package graphics;

import java.security.InvalidKeyException;
import java.util.ArrayList;
<<<<<<< HEAD
import java.util.HashMap;
=======
>>>>>>> refs/remotes/origin/MapGeneration

import entities.*;

public class Tile {
<<<<<<< HEAD
	Container items = new Container();	// The items in the tile
	Sentient occupant = null;
=======
>>>>>>> refs/remotes/origin/MapGeneration
	
	
	private ArrayList<Holdable> items = new ArrayList<Holdable>();
	private Sentient occupant = null;

	
	
	/**
	 * Creates a new tile object with a blank image key.
	 */
	public Tile() { }
	
	public Holdable getItem(Character itemID) {
		return items.getItem(itemID);
	}
	
	// Get the items in the tile, as a container
	public Container getItems() {
		return items;
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
		if (items.size == 0)
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
			occupant.location = this;
		}
	}
	
	// Take the sentient standing in this tile out, nulling its location, and returns it.
	public Sentient removeOccupant() {
		Sentient _occupant = this.occupant;
		this.occupant = null;
		_occupant.location = null;
		return _occupant;
		
	}
	

}
