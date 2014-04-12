package entities;

import game.Controller;
import graphics.ImageManager;

import javax.swing.ImageIcon;

public class Armour extends Holdable {
	private int AC;
	
	public static String[] csvHeaders() {
		String[] headers = {"Name", "Cost", "Weight", "AC", "Special"};
		return headers;
	}
	
	// Messy file parser just for demonstration, should be replaced by a way to read
	// a comma-delimited list much more smoothly
	public static Armour createArmourFromReader(String armourString) {
		String[] values = armourString.split(",");
		Armour armour = new Armour();
		try {
			armour.setName(values[0]);
			armour.setCost(Integer.parseInt(values[1]));
			armour.setWeight(Integer.parseInt(values[2]));
			armour.setAC(Integer.parseInt(values[3]));
		
			if (values.length == 5 && values[4] != "") {
				String[] specials = values[4].split(" ");
				armour = Armour.applySpecialTraits(armour, specials);
			}
		} catch (Exception e) {
			System.out.println("Error reading armour object");
			if (armour.getName() != null)
				System.out.println(armour.getName() + " has some incorrect parameter.");
			return null;
		}
		return armour;
	}

	// Apply the decorators as matched with the strings
	public static Armour applySpecialTraits(Armour armour, String[] traits) {
		for (String trait : traits) {
			if (trait.trim().equals("Teleport")) {
				armour = new Teleport(armour);
			}
		}
		return armour;
	}
	
	@Override
	public ImageIcon getImg(){
		return ImageManager.getInstance().getTileSet("items").getTile(this.getName());
	}
	
	public int getAC() {
		return AC;
	}

	public void setAC(int aC) {
		AC = aC;
	}

	public String inventoryName() {
		Armour equippedArmour = Controller.getInstance().getPlayer().getEquippedArmour();
		if (equippedArmour != null && equippedArmour.equals(this))
			return this.getName() + " (currently equipped)";
		else
			return this.getName();
	}
	
	public void equipEffect() { };
}
