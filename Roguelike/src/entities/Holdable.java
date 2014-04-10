/* Class representing any item which is holdable, i.e can go into an inventory
 * and/or container. 
 */

package entities;

@SuppressWarnings("serial")
public class Holdable extends Entity {
	private char id = 'a';
	private int weight;
	private int cost = 0;
	private boolean stackable = false;
	
	public Holdable() {
		
	}
	
	public static String[] csvHeaders() {
		String[] headers = {"Name", "Cost", "Weight", "Special"};
		return headers;
	}
	
	// Messy file parser just for demonstration, should be replaced by a way to read
	// a comma-delimited list much more smoothly
	public static Holdable createHoldableFromReader(String itemString) {
		String[] values = itemString.split(",");
		Holdable item = new Holdable();
		try {
			item.setName(values[0]);
			item.setCost(Integer.parseInt(values[1]));
			item.setWeight(Integer.parseInt(values[2]));
			
			if (values.length == 4 && values[3] != "") {
				String[] specials = values[3].split(" ");
				item = Holdable.applySpecialTraits(item, specials);
			}
		} catch (Exception e) {
			System.out.println("Error reading misc object");
			if (item.getName() != null)
				System.out.println(item.getName() + " has some incorrect parameter.");
			return null;
		}
		return item;
	}
	
	// Apply the decorators as matched with the strings
	public static Holdable applySpecialTraits(Holdable item, String[] traits) {
		for (String trait : traits) {
			
		}
		return item;
	}
	
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
	
	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public boolean isStackable() {
		return stackable;
	}

	void setStackable(boolean stackable) {
		this.stackable = stackable;
	}
	
	public void applyAbilities(Player player) {
		
	}
	
	public void deapplyAbilites(Player player) {
		
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
