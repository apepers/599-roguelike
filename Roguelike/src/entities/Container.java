/* Class for representing containers, which can be inventories, bags, boxes, etc.
 * Internally keeps track of the different subtypes of items separately.
 * Is an item itself so that we can carry containers like bags, as well as recursively
 * have containers within the misc field of a container.
 */

package entities;

import java.security.InvalidKeyException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Container extends Holdable {
	private HashMap<Character, Weapon> weapons = new HashMap<Character, Weapon>();
	private HashMap<Character, Food> foods = new HashMap<Character, Food>();
	private HashMap<Character, Holdable> misc = new HashMap<Character, Holdable>();
	public int size = 0;
	
	// Display all items in the container under headers matching their
	// subtype
	@Override
	public void display() {
		if (size == 0) {
			System.out.println("There's nothing here!");
			return;
		}
		if (weapons.size() > 0) {
			System.out.println("Weapons:");
			Iterator<Entry<Character, Weapon>> iter = weapons.entrySet().iterator();
			while(iter.hasNext()) {
				Map.Entry<Character, Weapon> entry = (Map.Entry<Character, Weapon>)iter.next();
				System.out.println(entry.getKey() + " - " + entry.getValue().name);
			}
		}
		if (foods.size() > 0) {
			System.out.println("Consumables:");
			Iterator<Entry<Character, Food>> iter = foods.entrySet().iterator();
			while(iter.hasNext()) {
				Map.Entry<Character, Food> entry = (Map.Entry<Character, Food>)iter.next();
				System.out.println(entry.getKey() + " - " + entry.getValue().name);
			}
		}
		if (misc.size() > 0) {
			System.out.println("Consumables:");
			Iterator<Entry<Character, Holdable>> iter = misc.entrySet().iterator();
			while(iter.hasNext()) {
				Map.Entry<Character, Holdable> entry = (Map.Entry<Character, Holdable>)iter.next();
				System.out.println(entry.getKey() + " - " + entry.getValue().name);
			}
		}
		/*
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
		*/
	}
	
	public HashMap<Character, Weapon> getWeapons() {
		return weapons;
	}
	
	public HashMap<Character, Food> getFood() {
		return foods;
	}
	
	public HashMap<Character, Holdable> getMisc() {
		return misc;
	}
	
	// Get all of the items in the container into one list
	public HashMap<Character, Holdable> getAllItems() {
		HashMap<Character, Holdable> allItems = new HashMap<Character, Holdable>(misc);
		allItems.putAll(weapons);
		allItems.putAll(foods);
		return allItems;
	}
	
	public Set<Character> getUsedIDs() {
		Set<Character> usedIDs = getAllItems().keySet();
		return usedIDs;
	}
	
	public Character assignID(Holdable item) {
		Set<Character> usedIDs = getUsedIDs();
		Character itemID = item.getID();
		while(usedIDs.contains(itemID)) {
			itemID++;
		}
		item.setID(itemID);
		return itemID;
	}
	
	// Add an item to the container, putting it into the matching internal list
	public void addItem(Holdable item) {
		Character itemID = assignID(item);
		if (item instanceof Weapon) 
			weapons.put(itemID, (Weapon) item);
		else if (item instanceof Food)
			foods.put(itemID, (Food) item);
		else
			misc.put(itemID, item);
		size++;
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
	public Holdable removeItem(Character itemID) throws InvalidKeyException {
		//if (item.stackable) {
			// Query user for how many items in the stack to drop
			//return removeStackedItem(item, 1);
		//} else {
		Holdable item;
		if(weapons.containsKey(itemID)) {
			item = weapons.get(itemID);
			weapons.remove(itemID);
		} else if (foods.containsKey(itemID)) {
			item = foods.get(itemID);
			foods.remove(itemID);
		} else if (misc.containsKey(itemID)) {
			item = misc.get(itemID);
			misc.remove(itemID);
		} else {
			throw new InvalidKeyException();
		}
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
