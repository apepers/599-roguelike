/* Class representing the player
 * 
 */

package entities;

import graphics.ImageManager;

@SuppressWarnings("serial")
public class Player extends Sentient {
	private int nutrition;
	
	public Player() {
		this.setName("You!");
		this.setDescription("A normal, boring person.");
		nutrition = 1500;
		setMaxHP(15);
		setCurrentHP(15);
		setNaturalAC(0);
		setAttackBonus(2);
		setBaseDamage(4);
		setStrength(16);
		setSpeed(2);
		setDexterity(16);
		
		setImage(ImageManager.getGlobalRegistry().getTile("player"));
	}
	
	// Simple header-less way of displaying everything currently in the inventory
	public void displayInventory() {
		System.out.println("Inventory:");
		getInventory().display();
	}
	
	public void increaseNutrition(int n) {
		nutrition += n;
	}
	
	public void increaseHunger(int hunger) {
		nutrition -= hunger;
	}
	
	public int getNutrition() {
		return nutrition;
	}
	
	public String hungerText() {
		if (nutrition >= 4000)
			return "Oversatiated";
		else if (nutrition >= 2000)
			return "Satiated";
		else if (nutrition >= 1200)
			return "Not hungry";
		else if (nutrition >= 600)
			return "Hungry";
		else if (nutrition >= 0)
			return "Weak";
		else if (nutrition > -600)
			return "Fainting";
		else
			return "Starved";
	}
}
