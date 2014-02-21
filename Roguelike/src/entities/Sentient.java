package entities;

import java.util.ArrayList;
import graphics.*;

public abstract class Sentient extends Entity {
	private ArrayList<Weapon> weapons = new ArrayList<Weapon>();
	private ArrayList<Food> food = new ArrayList<Food>(); 
	public Tile location;
	
	// Add an item to the players inventory, and sorts it into the correct
	// list based on its subclass.
	public void addItem(Holdable item) {
		if (item instanceof Food)
			food.add((Food)item);
		else if (item instanceof Weapon)
			weapons.add((Weapon)item);
	}
	
	public void removeItem(Holdable item) {
		for (Holdable i : weapons) {
			if (i.name.equals(item.name))
				weapons.remove(i);
		}
		for (Holdable i : food) {
			if (i.name.equals(item.name)) {
				food.remove(i);
				break;
			}
		}
	}
	
	public ArrayList<Holdable> getInventory() {
		ArrayList<Holdable> inventory = new ArrayList<Holdable>();
		inventory.addAll(food);
		inventory.addAll(weapons);
		return inventory;
	}
	
	public ArrayList<Food> getFood() {
		return food;
	}
	
	// Static method to return the headers for each section of the inventory,
	// in the same order as those sections are appended in the getInventory list.
	public static String[] getInventoryHeaders() {
		String[] headers =  {"Food", "Weapons"};
		return headers;
	}
}
