package entities;

import java.awt.Color;

public class Monster extends Sentient {
	public Monster() { };
	
	public static String[] csvHeaders() {
		String[] headers = {"Name", "Special"};
		return headers;
	}
	
	// Messy file parser, should be replaced by a way to read
	// a comma-delimited list much more smoothly
	public static Monster createMonsterFromReader(String monsterString) {
		String[] values = monsterString.split(",");
		Monster monster = new Monster();
		try {
			monster.setName(values[0]);
			if (values.length == 2 && values[1] != "") {
				String[] specials = values[1].split(" ");
				monster = Monster.applySpecialTraits(monster, specials);
			}
		} catch (Exception e) {
			System.out.println("Error reading monster object");
			if (monster.getName() != null) 
				System.out.println(monster.getName() + " has some incorrect parameter.");
			return null;
		}
		return monster;
	}
	
	// Apply the decorators as matched with the strings, currently there aren't any for monsters
	public static Monster applySpecialTraits(Monster monster, String[] traits) {
		for (String trait : traits) {
		}
		return monster;
	}
}
