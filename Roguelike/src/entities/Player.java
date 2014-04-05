/* Class representing the player
 * 
 */

package entities;

@SuppressWarnings("serial")
public class Player extends Sentient {
	private int hunger;
	
	public Player() {
		hunger = 0;
		setMaxHP(15);
		setCurrentHP(15);
		setNaturalAC(0);
		setAttackBonus(2);
		setBaseDamage(4);
		setStrength(16);
		setDexterity(16);
	}
	
	// Simple header-less way of displaying everything currently in the inventory
	public void displayInventory() {
		System.out.println("Inventory:");
		getInventory().display();
	}
	
	public void reduceHunger(int nutrition) {
		hunger -= nutrition;
		if (hunger < 0)
			hunger = 0;
	}
	
	public int getHunger() {
		return hunger;
	}
}
