/* Class for all sentient entities, which are entities which can move around the map,
 * possess inventories and AI, and overall act as agents in the game.
 */

package entities;

import java.security.InvalidKeyException;
import java.util.HashMap;

import graphics.*;

public abstract class Sentient extends Entity {
	private Container inventory = new Container();
	private Tile location;
	
	// Add an item to the players inventory
	public void addItem(Holdable item) {
		getInventory().addItem(item);
	}
	
	// Take an item out of the player's inventory
	public Holdable removeItem(Character itemID) throws InvalidKeyException {
		Holdable item = getInventory().removeItem(itemID);
		return item;
	}
	
	// Return the inventory
	public Container getInventory() {
		return inventory;
	}
	
	public Tile getLocation() {
		return location;
	}

	public void setLocation(Tile location) {
		this.location = location;
	}
	
	// Return a list version of the inventory
	public HashMap<Character, Holdable> getInventoryList() {
		return getInventory().getAllItems();
	}
	
	// Get just the food from the inventory
	public HashMap<Character, Food> getFood() {
		return getInventory().getFood();
	}

}
