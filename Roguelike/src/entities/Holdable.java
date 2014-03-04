/* Class representing any item which is holdable, i.e can go into an inventory
 * and/or container. 
 */

package entities;

public abstract class Holdable extends Entity {
	private char id = 'a';
	private int weight;
	private int cost = 0;
	private boolean stackable = false;
	
	public char getID() {
		return id;
	}
	
	public void setID(char id) {
		this.id = id;
	}
	
	public String eatMsg() {
		return "You can't eat that.";
	}
	
	public String properName() {
		return this.getName();
	}
	
	public void display() {
		System.out.println(this.properName());
	}
	
	int getWeight() {
		return weight;
	}

	void setWeight(int weight) {
		this.weight = weight;
	}

	int getCost() {
		return cost;
	}

	void setCost(int cost) {
		this.cost = cost;
	}

	boolean isStackable() {
		return stackable;
	}

	void setStackable(boolean stackable) {
		this.stackable = stackable;
	}

	public boolean sameItem(Holdable h) {
		if (this.getName().equals(h.getName()) && this.getWeight() == h.getWeight() && this.getCost() == h.getCost()
				&& this.isStackable() == h.isStackable()) 
			return true;
		else
			return false;
	}
	
	// Functions for stackable items, which are overwritten to add functionality
	// in the Stackable decoration
	public void combineStack(Stackable h) { }
	public Holdable reduceStack(int c) {return this;}
	public int stackSize() {return 1;}
}
