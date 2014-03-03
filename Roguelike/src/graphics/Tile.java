package graphics;

import java.util.ArrayList;

import entities.*;

public class Tile {
	
	
	private ArrayList<Holdable> items = new ArrayList<Holdable>();
	private Sentient occupant = null;

	
	
	/**
	 * Creates a new tile object with a blank image key.
	 */
	public Tile() { }
	
	// Get the items in the tile
	public ArrayList<Holdable> getItems() {
		return items;
	}
	
	// Add an item to the pile in the tile
	public void addItem(Holdable item) {
		items.add(item);
	}
	
	// Remove the item at the specified index, if the index is within range
	public void removeItem(int index) {
		if (index < items.size())
			items.remove(index);
	}
	
	// Display the contents of the tile from the items list
	public void displayItems() {
		if (items.size() == 0)
			System.out.println("There is nothing in this tile");
		else {
			System.out.println("Here there is:");
			for (Holdable item : items) {
				System.out.println(item.name);
			}
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
