/* Class representing the player
 * 
 */

package entities;

@SuppressWarnings("serial")
public class Player extends Sentient {
	public Player() {
	}
	
	// Simple header-less way of displaying everything currently in the inventory
	public void displayInventory() {
		System.out.println("Inventory:");
		getInventory().display();
	}
}
