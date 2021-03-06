/* Class representing the player
 * 
 */

package entities;

import java.awt.Color;
import java.util.ArrayList;
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
	private ArrayList<Holdable> equippedMisc;
	
	private int textCollected = 0;
	
	private boolean drunk;
	private int drunkCounter;
	private int tempStrengthCounter;
	private int futuresightCounter;
	private int tempSight;
	private int hpCounter;
	
	private boolean wizardUsed;
	
	public Player() {
		this.setName("You!");
		nutrition = 2500;
		setMaxHP(15);
		setCurrentHP(15);
		setNaturalAC(0);
		setAttackBonus(2);
		setBaseDamage(4);
		setStrength(16);
		setSpeed(2);
		setDexterity(16);
		setSightRange(8);
		setInSight(true);
		setBaseMeleeDescription("hit");
		xp = 0;
		level = 1;
		strIncrement = 0;
		dexIncrement = 0;
		setEquippedWeapon(null);
		setEquippedArmour(null);
		drunkCounter = 0;
		tempStrengthCounter = 0;
		futuresightCounter = 0;
		hpCounter = 2;
		wizardUsed = false;
		
		setEquippedMisc(new ArrayList<Holdable>());
		
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
		String oldText = hungerText();
		nutrition -= hunger;
		if (!hungerText().equals(oldText)) {
			Controller.getInstance().getMessenger().println("You now feel " + hungerText());
		}
	}
	
	public void increaseCurrentHP(int increase) {
		setCurrentHP(getCurrentHP() + increase);
		if (getCurrentHP() > getMaxHP())
			setCurrentHP(getMaxHP());
	}
	
	public void incrementStrength() {
		strIncrement++;
		if (strIncrement >= 10) {
			strIncrement -= 10;
			setStrength(getStrength() + 1);
			Controller.getInstance().getMessenger().println("You feel stronger!");
		}
	}
	
	public void incrementDexterity() {
		dexIncrement++;
		if (dexIncrement >= 10) {
			dexIncrement -= 10;
			setDexterity(getDexterity() + 1);
			Controller.getInstance().getMessenger().println("You feel more nimble!");
		}
		if (getDexterity() - 16 >= getSpeed() * 3 - 2 && getSpeed() <= 7) {
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
		if (!wizardUsed) {
			Iterator<Entry<Character, Holdable>> iter = this.getInventory().getAllItems().entrySet().iterator();
			while(iter.hasNext()) {
				Map.Entry<Character, Holdable> entry = (Map.Entry<Character, Holdable>)iter.next();
				score += entry.getValue().getCost();
			}
			score += getDexterity();
			score += getStrength();
			score += this.level * 500;
			score += textCollected * 1000;
		}
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
			return "Not Hungry";
		else if (nutrition >= 600)
			return "Hungry";
		else if (nutrition >= 0)
			return "Weak";
		else if (nutrition > -600)
			return "Starving";
		else
			return "Starved";
	}
	
	public Color hungerColor() {
		if (nutrition >= 4000)
			return new Color(0, 255, 0);
		else if (nutrition >= 2000)
			return new Color(153, 255, 51);
		else if (nutrition >= 1200)
			return new Color(255, 255, 51);
		else if (nutrition >= 600)
			return new Color(204, 102, 0);
		else if (nutrition >= 0)
			return new Color(204, 0, 0);
		else if (nutrition > -600)
			return new Color(153, 0, 0);
		else
			return new Color(51, 0, 0);
	}
	
	public Color hpColor() {
		if (this.getCurrentHP() >= this.getMaxHP() * (2.0 / 3.0))
			return Color.GREEN;
		else if (this.getCurrentHP() >= this.getMaxHP() / 3)
			return Color.YELLOW;
		else
			return Color.RED;
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
		if (equippedArmour != null)
			equippedArmour.equipEffect();
	}
	
	public void addEquippedMisc(Holdable h) {
		equippedMisc.add(h);
		h.applyAbilities(this);
	}
	
	public void removeEquippedMisc(Holdable h) {
		equippedMisc.remove(h);
		h.deapplyAbilites(this);
	}

	public ArrayList<Holdable> getEquippedMisc() {
		return equippedMisc;
	}

	public void setEquippedMisc(ArrayList<Holdable> equippedMisc) {
		this.equippedMisc = equippedMisc;
	}

	public int getTextCollected() {
		return textCollected;
	}

	public void setTextCollected(int textCollected) {
		this.textCollected = textCollected;
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
	
	public void setFuturesight(boolean bool) {
		if (bool) {
			tempSight = getSightRange();
			setSightRange(0);
			Controller.getInstance().revealMap(true);
			futuresightCounter = 25;
		}
	}

	public int getFuturesightCounter() {
		return futuresightCounter;
	}

	public void setFuturesightCounter(int futuresightCounter) {
		this.futuresightCounter = futuresightCounter;
	}
	
	public int getHpCounter() {
		return hpCounter;
	}

	public void setHpCounter(int hpCounter) {
		this.hpCounter = hpCounter;
	}
	
	public void setWizardUsed(boolean used) {
		wizardUsed = used;
	}
	
	public boolean getWizardUsed() {
		return wizardUsed;
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
			return MapRand.randInt(equippedWeapon.getMinDamage(), equippedWeapon.getMaxDamage()) + getAbilityBonus(getStrength() + comboBonus()); 
		} else
			return MapRand.randInt(this.getBaseDamage()) + getAbilityBonus(getStrength());
	}
	
	/*
	 * Figure out any damage bonuses due to weapon/armour matchups
	 */
	private int comboBonus() {
		int combo = 0;
		if (equippedArmour.isPowered() && equippedWeapon.isPowered())
			combo += 4;
		return combo;
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
		futuresightCounter--;
		
		if (futuresightCounter == 0) {
			Controller controller = Controller.getInstance();
			setSightRange(tempSight);
			controller.revealMap(false);
			controller.fieldOfView(true);
			controller.getMessenger().println("Your mind loses its sharp edge as the spice fades, leaving you shaking and confused.");
		} /*else if (futuresightCounter == 24) {
			Controller.getInstance().getMessenger().println("The immediate is blurred. Only the future is clear.");
		}*/
		
		hpCounter--;
		if (hpCounter == 0) {
			increaseCurrentHP(1);
			hpCounter = 2;
		}
	}
}
