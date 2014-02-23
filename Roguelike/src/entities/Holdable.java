/* Class representing any item which is holdable, i.e can go into an inventory
 * and/or container. 
 */

package entities;

public abstract class Holdable extends Entity {
	private char id = 'a';
	int weight;
	int cost = 0;
	//boolean stackable = false;
	
	public char getID() {
		return id;
	}
	
	public void setID(char id) {
		this.id = id;
	}
	
	public String eatMsg() {
		return "You can't eat that.";
	}
	
	public void display() {
		System.out.println(name);
	}
	
	/*public boolean sameItem(Holdable h) {
		if (this.name.equals(h.name) && this.weight == h.weight && this.cost == h.cost
				&& this.stackable == h.stackable) 
			return true;
		else
			return false;
	}*/
	
	// Functions for stackable items, which are overwritten to add functionality
	// in the Stackable decoration
	/*public void combineStack(Holdable h) { }
	public Holdable reduceStack(int c) {return null;}
	public int stackSize() {return 1;}*/
}
