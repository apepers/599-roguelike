/* Class for all sentient entities, which are entities which can move around the map,
 * possess inventories and AI, and overall act as agents in the game.
 */

package entities;

import java.security.InvalidKeyException;
import java.util.HashMap;

import mapGeneration.MapRand;

@SuppressWarnings("serial")
public abstract class Sentient extends Entity {
	private Container inventory = new Container();
	private Tile location;
	private int maxHP;
	private int currentHP;
	private int naturalAC;
	private int attackBonus;
	private int baseDamage;
	private String baseMeleeDescription;
	private int strength;
	private int dexterity;
	
	public Holdable getItem(Character itemID) {
		return getInventory().getItem(itemID);
	}
	
	// Add an item to the players inventory
	public void addItem(Holdable item) {
		getInventory().addItem(item);
	}
	
	// Take an item out of the player's inventory
	public Holdable removeItem(Character itemID) throws InvalidKeyException {
		Holdable item = getInventory().removeItem(itemID);
		return item;
	}
	
	public Holdable removeItem(Character itemID, int count) throws InvalidKeyException {
		Holdable item = getInventory().removeStackedItem(itemID, count);
		return item;
	}
	
	// Return the inventory
	public Container getInventory() {
		return inventory;
	}
	
	public int getAC() {
		return 10 + naturalAC + dexterity % 10;
	}
	
	public int getMeleeDamage() {
		return MapRand.randInt(baseDamage) + strength % 10; 
	}
	
	public int getAttack() {
		return attackBonus;
	}
	
	public void takeDamage(int damage) {
		currentHP -= damage;
	}
	
	public boolean isDead() {
		return (currentHP <= 0);
	}
	
	public Tile getLocation() {
		return location;
	}

	public void setLocation(Tile location) {
		this.location = location;
	}
	
	public int getMaxHP() {
		return maxHP;
	}

	public void setMaxHP(int maxHP) {
		this.maxHP = maxHP;
	}

	public int getCurrentHP() {
		return currentHP;
	}

	public void setCurrentHP(int currentHP) {
		this.currentHP = currentHP;
	}

	public int getNaturalAC() {
		return naturalAC;
	}

	public void setNaturalAC(int naturalAC) {
		this.naturalAC = naturalAC;
	}

	public int getAttackBonus() {
		return attackBonus;
	}

	public void setAttackBonus(int attackBonus) {
		this.attackBonus = attackBonus;
	}

	public int getBaseDamage() {
		return baseDamage;
	}

	public void setBaseDamage(int baseDamage) {
		this.baseDamage = baseDamage;
	}

	public String getBaseMeleeDescription() {
		return baseMeleeDescription;
	}

	public void setBaseMeleeDescription(String baseMeleeDescription) {
		this.baseMeleeDescription = baseMeleeDescription;
	}

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public int getDexterity() {
		return dexterity;
	}

	public void setDexterity(int dexterity) {
		this.dexterity = dexterity;
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
