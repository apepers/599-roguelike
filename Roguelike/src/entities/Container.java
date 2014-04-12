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

@SuppressWarnings("serial")
public class Container extends Holdable {
	private HashMap<Character, Weapon> weapons = new HashMap<Character, Weapon>();
	private HashMap<Character, Food> foods = new HashMap<Character, Food>();
	private HashMap<Character, Armour> armours = new HashMap<Character, Armour>();
	private HashMap<Character, Holdable> misc = new HashMap<Character, Holdable>();
	private int size = 0;
	
	// Get an array of strings which contain all item descriptions in the form "id - proper name"
	public String[] getItemTexts() {
		String[] itemText = new String[weapons.size() + armours.size() + foods.size() + misc.size()];
		int itemCount = 0;
		if (weapons.size() > 0) {
			Iterator<Entry<Character, Weapon>> iter = weapons.entrySet().iterator();
			while(iter.hasNext()) {
				Map.Entry<Character, Weapon> entry = (Map.Entry<Character, Weapon>)iter.next();
				itemText[itemCount] = entry.getKey() + " - " + entry.getValue().inventoryName();
				itemCount++;
			}
		}
		if (armours.size() > 0) {
			Iterator<Entry<Character, Armour>> iter = armours.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<Character, Armour> entry = (Map.Entry<Character, Armour>)iter.next();
				itemText[itemCount] = entry.getKey() + " - " + entry.getValue().inventoryName();
				itemCount++;
			}
		}
		if (foods.size() > 0) {
			Iterator<Entry<Character, Food>> iter = foods.entrySet().iterator();
			while(iter.hasNext()) {
				Map.Entry<Character, Food> entry = (Map.Entry<Character, Food>)iter.next();
				itemText[itemCount] = entry.getKey() + " - " + entry.getValue().properName();
				itemCount++;
			}
		}
		if (misc.size() > 0) {
			Iterator<Entry<Character, Holdable>> iter = misc.entrySet().iterator();
			while(iter.hasNext()) {
				Map.Entry<Character, Holdable> entry = (Map.Entry<Character, Holdable>)iter.next();
				itemText[itemCount] = entry.getKey() + " - " + entry.getValue().properName();
				itemCount++;
			}
		}
		return itemText;
	}
	
	public String[] getWeaponTexts() {
		String[] weaponText = new String[weapons.size()];
		int itemCount = 0;
		if (weapons.size() > 0) {
			Iterator<Entry<Character, Weapon>> iter = weapons.entrySet().iterator();
			while(iter.hasNext()) {
				Map.Entry<Character, Weapon> entry = (Map.Entry<Character, Weapon>)iter.next();
				weaponText[itemCount] = entry.getKey() + " - " + entry.getValue().inventoryName();
				itemCount++;
			}			
		}
		return weaponText;
	}
	
	public String[] getArmourTexts() {
		String[] armourText = new String[armours.size()];
		int itemCount = 0;
		if (armours.size() > 0) {
			Iterator<Entry<Character, Armour>> iter = armours.entrySet().iterator();
			while(iter.hasNext()) {
				Map.Entry<Character, Armour> entry = (Map.Entry<Character, Armour>)iter.next();
				armourText[itemCount] = entry.getKey() + " - " + entry.getValue().inventoryName();
				itemCount++;
			}			
		}
		return armourText;
	}
	
	public String[] getFoodsTexts() {
		String[] foodText = new String[foods.size()];
		int itemCount = 0;
		if (foods.size() > 0) {
			Iterator<Entry<Character, Food>> iter = foods.entrySet().iterator();
			while(iter.hasNext()) {
				Map.Entry<Character, Food> entry = (Map.Entry<Character, Food>)iter.next();
				foodText[itemCount] = entry.getKey() + " - " + entry.getValue().properName();
				itemCount++;
			}
		}
		return foodText;
	}
	
	public String[] getMiscTexts() {
		String[] miscText = new String[misc.size()];
		int itemCount = 0;
		if (misc.size() > 0) {
			Iterator<Entry<Character, Holdable>> iter = misc.entrySet().iterator();
			while(iter.hasNext()) {
				Map.Entry<Character, Holdable> entry = (Map.Entry<Character, Holdable>)iter.next();
				miscText[itemCount] = entry.getKey() + " - " + entry.getValue().properName();
				itemCount++;
			}
		}
		return miscText;
	}
	
	public int getSize() {
		return size;
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
	// Get a specific item from its key
	public Holdable getItem(Character itemID) {
		HashMap<Character, Holdable> allItems = this.getAllItems();
		return allItems.get(itemID);
	}
	
	public HashMap<Character, Weapon> getWeapons() {
		return weapons;
	}
	
	public HashMap<Character, Armour> getArmours() {
		return armours;
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
		allItems.putAll(armours);
		allItems.putAll(foods);
		return allItems;
	}
	
	public Set<Character> getUsedIDs() {
		Set<Character> usedIDs = getAllItems().keySet();
		return usedIDs;
	}
	
	// Get all of the ID's concatenated into one string, i.e "abcd"
	public String getIDString() {
		Set<Character> ids = getUsedIDs();
		String IDString = "";
		for (Character c : ids) {
			IDString += c;
		}
		return IDString;
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
		if (item.isStackable()) {	// Follow the different rules for stackable items
			addStackedItem(item);
		} else {
			Character itemID = assignID(item);
			if (item instanceof Weapon) 
				weapons.put(itemID, (Weapon) item);
			else if (item instanceof Armour)
				armours.put(itemID, (Armour) item);
			else if (item instanceof Food)
				foods.put(itemID, (Food) item);
			else
				misc.put(itemID, item);
			size++;
		}
	}
	
	// Add a stackable item, which means checking if there is already the same type of item in
	// the relevant list in which case it's count is incremented instead of a new item being added.
	public void addStackedItem(Holdable item) {
		boolean alreadyExists = false;
		Character itemID = assignID(item);
		if (item instanceof Weapon) {
			Iterator<Entry<Character, Weapon>> iter = weapons.entrySet().iterator();
			while(iter.hasNext()) {
				Map.Entry<Character, Weapon> entry = (Map.Entry<Character, Weapon>)iter.next();
				if (item.sameItem(entry.getValue())) {
					entry.getValue().combineStack((Stackable)item);
					alreadyExists = true;
				}
			}
			if (!alreadyExists) {
				weapons.put(itemID, (Weapon)item);
				size++;
			}
		} else if (item instanceof Armour) {
			Iterator<Entry<Character, Armour>> iter = armours.entrySet().iterator();
			while(iter.hasNext()) {
				Map.Entry<Character, Armour> entry = (Map.Entry<Character, Armour>)iter.next();
				if (item.sameItem(entry.getValue())) {
					entry.getValue().combineStack((Stackable)item);
					alreadyExists = true;
				}
			}
			if (!alreadyExists) {
				weapons.put(itemID, (Weapon)item);
				size++;
			}
		} else if (item instanceof Food) {
			Iterator<Entry<Character, Food>> iter = foods.entrySet().iterator();
			while(iter.hasNext()) {
				Map.Entry<Character, Food> entry = (Map.Entry<Character, Food>)iter.next();
				if (item.sameItem(entry.getValue())) {
					entry.getValue().combineStack((Stackable)item);
					alreadyExists = true;
				}
			}
			if (!alreadyExists) {
				foods.put(itemID, (Food) item);
				size++;
			}
		} else {
			Iterator<Entry<Character, Holdable>> iter = misc.entrySet().iterator();
			while(iter.hasNext()) {
				Map.Entry<Character, Holdable> entry = (Map.Entry<Character, Holdable>)iter.next();
				if (item.sameItem(entry.getValue())) {
					entry.getValue().combineStack((Stackable)item);
					alreadyExists = true;
				}
			}
			if (!alreadyExists) {
				misc.put(itemID, item);
				size++;
			}
		}
	}
	
	// Remove a number of stacked items specified by the count
	public Holdable removeStackedItem(Character itemID, int count) throws InvalidKeyException {
		Holdable item;
		Holdable returnItem;
		if(weapons.containsKey(itemID)) {
			item = weapons.get(itemID);
			// If the item is stackable then just reduce the stack count
			returnItem = item.reduceStack(count);
			// Only if the stack has hit 0 do we actually remove the item
			if (item.stackSize() == 0) {
				weapons.remove(itemID);
				size--;
			}
		} else if (armours.containsKey(itemID)) {
			item = armours.get(itemID);
			returnItem = item.reduceStack(count);
			if (item.stackSize() == 0) {
				armours.remove(itemID);
				size--;
			}
		} else if (foods.containsKey(itemID)) {
			item = foods.get(itemID);
			returnItem = item.reduceStack(count);
			if (item.stackSize() == 0) {
				foods.remove(itemID);
				size--;
			}
		} else if (misc.containsKey(itemID)) {
			item = misc.get(itemID);
			returnItem = item.reduceStack(count);
			if (item.stackSize() == 0) {
				misc.remove(itemID);
				size--;
			}
		} else {
			throw new InvalidKeyException();
		}
		return returnItem;
	}
	
	// Take out an item from the correct list. 
	public Holdable removeItem(Character itemID) throws InvalidKeyException {
		Holdable item;
		if(weapons.containsKey(itemID)) {
			item = weapons.get(itemID);
			weapons.remove(itemID);
			size--;
		} else if (armours.containsKey(itemID)) {
			item = armours.get(itemID);
			armours.remove(itemID);
			size--;
		} else if (foods.containsKey(itemID)) {
			item = foods.get(itemID);
			foods.remove(itemID);
			size--;
		} else if (misc.containsKey(itemID)) {
			item = misc.get(itemID);
			misc.remove(itemID);
			size--;
		} else {
			throw new InvalidKeyException();
		}
		return item;
	}
}
