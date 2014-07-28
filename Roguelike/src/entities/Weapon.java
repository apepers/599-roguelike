package entities;

import game.Controller;
import graphics.ImageManager;

import javax.swing.ImageIcon;

@SuppressWarnings("serial")
public class Weapon extends Holdable {
	private int minDamage;
	private int maxDamage;
	private String damageMsg;
	private boolean powered;
	
	public static String[] csvHeaders() {
		String[] headers = {"Name", "Cost", "Weight", "MinDamage", "MaxDamage", "DamageMsg", "Special"};
		return headers;
	}
	
	// Messy file parser just for demonstration, should be replaced by a way to read
	// a comma-delimited list much more smoothly
	public static Weapon createWeaponFromReader(String weaponString) {
		String[] values = weaponString.split(",");
		Weapon weapon = new Weapon();
		try {
			weapon.setName(values[0]);
			weapon.setCost(Integer.parseInt(values[1]));
			weapon.setWeight(Integer.parseInt(values[2]));
			weapon.setMinDamage(Integer.parseInt(values[3]));
			weapon.setMaxDamage(Integer.parseInt(values[4]));
			weapon.setDamageMsg(values[5].replaceAll("\\[comma\\]", ","));
		
			if (values.length == 7 && values[6] != "") {
				String[] specials = values[6].split(" ");
				weapon = Weapon.applySpecialTraits(weapon, specials);
			}
		} catch (Exception e) {
			System.out.println("Error reading weapon object");
			if (weapon.getName() != null)
				System.out.println(weapon.getName() + " has some incorrect parameter.");
			return null;
		}
		return weapon;
	}
	
	// Apply the decorators as matched with the strings
	public static Weapon applySpecialTraits(Weapon weapon, String[] traits) {
		for (String trait : traits) {
			String t = trait.trim();
			if (t.equals("Powered")) {
				weapon = new PoweredWeapon(weapon);
			}
		}
		return weapon;
	}
	
	@Override
	public ImageIcon getImg(){
		return ImageManager.getInstance().getTileSet("items").getTile(this.getName());
	}

	public int getMinDamage() {
		return minDamage;
	}

	public void setMinDamage(int minDamage) {
		this.minDamage = minDamage;
	}

	public int getMaxDamage() {
		return maxDamage;
	}

	public void setMaxDamage(int maxDamage) {
		this.maxDamage = maxDamage;
	}

	public String getDamageMsg() {
		return damageMsg;
	}

	public void setDamageMsg(String damageMsg) {
		this.damageMsg = damageMsg;
	}
	
	public boolean isPowered() {
		return powered;
	}

	public void setPowered(boolean powered) {
		this.powered = powered;
	}

	public String inventoryName() {
		Weapon equippedWeapon = Controller.getInstance().getPlayer().getEquippedWeapon();
		if (equippedWeapon != null && equippedWeapon.equals(this))
			return this.getName() + " (currently equipped)";
		else
			return this.getName();
	}
}
