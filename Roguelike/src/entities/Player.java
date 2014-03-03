/* Class representing the player
 * 
 */

package entities;

public class Player extends Sentient {
	public Player() {
	}
	
	// Simple header-less way of displaying everything currently in the inventory
	public void displayInventory() {
		System.out.println("Inventory:");
		inventory.display();
	}
}
