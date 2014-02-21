/* Simple placeholder class for a tile on the map
 * 
 */

package graphics;

import java.util.ArrayList;
import entities.*;

public class Tile {
	Container items = new Container();	// The items in the tile
	Sentient occupant = null;
	
	public Tile() { }
	
	// Get the items in the tile, as a container
	public Container getItems() {
		return items;
	}
	
	// Get a list of all the items in the tile
	public ArrayList<Holdable> getAllItems() {
		return items.getAllItems();
	}
	
	// Add an item to the pile in the tile
	public void addItem(Holdable item) {
		items.addItem(item);
	}
	
	// Remove the item
	public void removeItem(Holdable item) {
		items.removeItem(item);
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
