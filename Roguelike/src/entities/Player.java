/* Class representing the player
 * 
 */

package entities;

import graphics.ImageManager;

@SuppressWarnings("serial")
public class Player extends Sentient {
	private int nutrition;
	private int xp;
	
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
		setBaseMeleeDescription("hit");
		xp = 0;
		
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
	
	@Override
	public boolean isDead() {
		return (this.getCurrentHP() <= 0 || this.hungerText().equals("Starved"));
	}
	
	public String causeOfDeath() {
		if (this.getCurrentHP() <= 0) 
			return "You were slain by " + this.getKiller().getPronoun() + ".";
		else if (this.hungerText().equals("Starved"))
			return "You starved to death.";
		else
			return "You died of unknown causes.";
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
	
	public void giveXP(int amount) {
		xp += amount;
	}
	
	public int getXP() {
		return xp;
	}
	
	@Override
	public String getPronoun() {
		return "you";
	}
}
