/* Class representing the player
 * 
 */

package entities;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import mapGeneration.MapRand;
import game.Controller;
import graphics.ImageManager;

@SuppressWarnings("serial")
public class Player extends Sentient {
	private int nutrition;
	private int xp;
	private int level;
	private int strIncrement;
	private int dexIncrement;
	private Weapon equippedWeapon;
	private Armour equippedArmour;
	
	private boolean drunk;
	private int drunkCounter;
	private int tempStrengthCounter;
	
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
		level = 1;
		strIncrement = 0;
		dexIncrement = 0;
		setEquippedWeapon(null);
		setEquippedArmour(null);
		drunkCounter = 0;
		tempStrengthCounter = 0;
		
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
	
	public void increaseCurrentHP(int increase) {
		setCurrentHP(getCurrentHP() + increase);
		if (getCurrentHP() > getMaxHP())
			setCurrentHP(getMaxHP());
	}
	
	public void incrementStrength() {
		strIncrement++;
		if (strIncrement >= 15) {
			strIncrement -= 15;
			setStrength(getStrength() + 1);
			Controller.getInstance().getMessenger().println("You feel stronger!");
		}
	}
	
	public void incrementDexterity() {
		dexIncrement++;
		if (dexIncrement >= 5) {
			dexIncrement -= 5;
			setDexterity(getDexterity() + 1);
			Controller.getInstance().getMessenger().println("You feel more nimble!");
		}
		if (getDexterity() - 16 >= getSpeed() * 2 - 2) {
			setSpeed(getSpeed() + 1);
			Controller.getInstance().getMessenger().println("You feel quicker!");
		}
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
	
	public int getScore() {
		int score = 0;
		Iterator<Entry<Character, Holdable>> iter = this.getInventory().getAllItems().entrySet().iterator();
		while(iter.hasNext()) {
			Map.Entry<Character, Holdable> entry = (Map.Entry<Character, Holdable>)iter.next();
			score += entry.getValue().getCost();
		}
		score += getDexterity();
		score += getStrength();
		score += this.level * 500;
		return score;
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
		if (xp >= level * 1000) {
			Controller.getInstance().getMessenger().println("You have levelled up!");
			this.setMaxHP(getMaxHP() + 15);
			this.increaseCurrentHP(15);
			setAttackBonus(getAttackBonus() + 1);
			setStrength(getStrength() + 1);
			setDexterity(getDexterity() + 1);
			level++;
		}
	}
	
	public int getXP() {
		return xp;
	}
	
	public Weapon getEquippedWeapon() {
		return equippedWeapon;
	}

	public void setEquippedWeapon(Weapon equippedWeapon) {
		this.equippedWeapon = equippedWeapon;
	}

	public Armour getEquippedArmour() {
		return equippedArmour;
	}

	public void setEquippedArmour(Armour equippedArmour) {
		this.equippedArmour = equippedArmour;
	}

	public boolean isDrunk() {
		return drunk;
	}

	public void setDrunk(boolean drunk) {
		this.drunk = drunk;
		if (drunk)
			drunkCounter = 25;
	}
	
	public void setTempStrength(boolean temp) {
		if (temp) {
			setStrength(getStrength() + 4);
			tempStrengthCounter = 20;
		}
	}

	public int getACBonus() {
		if (equippedArmour != null)
			return this.getNaturalAC() + equippedArmour.getAC();
		else
			return this.getNaturalAC();
	}
	
	@Override
	public int getMeleeDamage() {
		if (equippedWeapon != null) {
			return MapRand.randInt(equippedWeapon.getMinDamage(), equippedWeapon.getMaxDamage()) + getAbilityBonus(getStrength()); 
		} else
			return MapRand.randInt(this.getBaseDamage()) + getAbilityBonus(getStrength());
	}
	
	private int getAbilityBonus(int score) {
		return (score / 2) - 5;
	}
	
	@Override
	public int getAC() {
		return 10 + getACBonus() + getAbilityBonus(getDexterity());
	}
	
	@Override
	public String getPronoun() {
		return "you";
	}
	
	public void checkCounters() {
		drunkCounter--;
		if (drunkCounter == 0) {
			setDrunk(false);
			Controller.getInstance().getMessenger().println("You feel a little more stable now.");
		}
		tempStrengthCounter--;
		if (tempStrengthCounter == 0) {
			setStrength(getStrength() - 4);
			Controller.getInstance().getMessenger().println("You remember that you are not a Klingon, but a puny weak human. How disappointing.");
		}
	}
}
