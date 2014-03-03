/* Class for all sentient entities, which are entities which can move around the map,
 * possess inventories and AI, and overall act as agents in the game.
 */

package entities;

import java.security.InvalidKeyException;
import java.util.HashMap;

import graphics.*;

public abstract class Sentient extends Entity {
	public Container inventory = new Container();
	public Tile location;
	
	// Add an item to the players inventory
	public void addItem(Holdable item) {
		inventory.addItem(item);
	}
	
	// Take an item out of the player's inventory
	public Holdable removeItem(Character itemID) throws InvalidKeyException {
		Holdable item = inventory.removeItem(itemID);
		return item;
	}
	
	// Return the inventory
	public Container getInventory() {
		return inventory;
	}
	
	// Return a list version of the inventory
	public HashMap<Character, Holdable> getInventoryList() {
		return inventory.getAllItems();
	}
	
	// Get just the food from the inventory
	public HashMap<Character, Food> getFood() {
		return inventory.getFood();
	}
}
