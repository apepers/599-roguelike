/* Class for representing containers, which can be inventories, bags, boxes, etc.
 * Internally keeps track of the different subtypes of items separately.
 * Is an item itself so that we can carry containers like bags, as well as recursively
 * have containers within the misc field of a container.
 */

package entities;

import java.util.ArrayList;

public class Container extends Holdable {
	private ArrayList<Weapon> weapons = new ArrayList<Weapon>();
	private ArrayList<Food> foods = new ArrayList<Food>();
	private ArrayList<Holdable> misc = new ArrayList<Holdable>();
	public int size = 0;
	
	// Display all items in the container under headers matching their
	// subtype
	@Override
	public void display() {
		if (weapons.size() > 0) {
			System.out.println("Weapons:");
			for (Weapon w : weapons) {
				System.out.println(w.name);
			}
		}
		if (foods.size() > 0) {
			System.out.println("Consumables:");
			for (Food f : foods) {
				System.out.println(f.name);
			}
		}
		if (misc.size() > 0) {
			System.out.println("Other:");
			for (Holdable m : misc) {
				m.display();
			}
		}
	}
	
	public ArrayList<Weapon> getWeapons() {
		return weapons;
	}
	
	public ArrayList<Food> getFood() {
		return foods;
	}
	
	public ArrayList<Holdable> getMisc() {
		return misc;
	}
	
	// Get all of the items in the container into one list
	public ArrayList<Holdable> getAllItems() {
		ArrayList<Holdable> allItems = new ArrayList<Holdable>(misc);
		allItems.addAll(weapons);
		allItems.addAll(foods);
		return allItems;
	}
	
	// Add an item to the container, putting it into the matching internal list
	public void addItem(Holdable item) {
		//if (item.stackable) {
		//	addStackedItem(item);
		//} else {
			if (item instanceof Weapon) 
				weapons.add((Weapon) item);
			else if (item instanceof Food)
				foods.add((Food) item);
			else
				misc.add(item);
			size++;
		//}
	}
	
	/*public void addStackedItem(Holdable item) {
		boolean alreadyExists = false;
		if (item instanceof Weapon) {
			for (Weapon w : weapons) {
				if (item.sameItem(w)) {
					w.combineStack(item);
					alreadyExists = true;
				}
			}
			if (!alreadyExists) 
				weapons.add((Weapon)item);
		} else if (item instanceof Food) {
			for (Food f : foods) {
				if (item.sameItem(f)) {
					f.combineStack(item);
					alreadyExists = true;
				}
			}			
			if (!alreadyExists)
				foods.add((Food) item);
		} else {
			for (Holdable h : misc) {
				if (item.sameItem(h)) {
					h.combineStack(h);
					alreadyExists = true;
				}
			}
			if (!alreadyExists) 
				misc.add(item);
		}
	}*/
	
	// Take out an item from the correct list. Note that this might need a proper
	// equals/hashcode to work reliably
	public Holdable removeItem(Holdable item) {
		//if (item.stackable) {
			// Query user for how many items in the stack to drop
			//return removeStackedItem(item, 1);
		//} else {
			if (item instanceof Weapon) 
				weapons.remove((Weapon) item);
			else if (item instanceof Food)
				foods.remove((Food) item);
			else
				misc.remove(item);
			size--;
			return item;
		//}
	}
	
	/*public Holdable removeStackedItem(Holdable item, int c) {
		Holdable result = null;
		if (item instanceof Weapon) {
			for (Weapon w : weapons) {
				if (w.sameItem(item)) {
					result = w.reduceStack(c);
					if (w.stackSize() == 0) 
						weapons.remove((Weapon) w);
				}
			}
		} else if (item instanceof Food) {
			for (Food f : foods) {
				if (f.sameItem(item)) {
					result = f.reduceStack(c);
					if (f.stackSize() == 0)
						foods.remove((Food) f);
				}
			}
		} else {
			for (Holdable h : misc) {
				if (h.sameItem(item)) {
					result = h.reduceStack(c);
					if (h.stackSize() == 0)
						misc.remove(h);
				}
			}
		}
		return result;
	}*/
}
