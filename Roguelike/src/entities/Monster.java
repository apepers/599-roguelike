package entities;

import graphics.ImageManager;

public class Monster extends Sentient {
	private int difficulty;
	
	public Monster() { };
	
	public static String[] csvHeaders() {
		String[] headers = {"Name", "HP", "NaturalArmour", "Strength", "Dexterity", "Speed", "AttackBonus", "BaseMeleeDamage", "BaseMeleeDescription", "SightRange", "Difficulty", "Special"};
		return headers;
	}
	
	// Messy file parser, should be replaced by a way to read
	// a comma-delimited list much more smoothly
	public static Monster createMonsterFromReader(String monsterString) {
		String[] values = monsterString.split(",");
		Monster monster = new Monster();
		try {
			monster.setName(values[0]);
			monster.setMaxHP(Integer.parseInt(values[1]));
			monster.setCurrentHP(Integer.parseInt(values[1]));
			monster.setNaturalAC(Integer.parseInt(values[2]));
			monster.setStrength(Integer.parseInt(values[3]));
			monster.setDexterity(Integer.parseInt(values[4]));
			monster.setSpeed(Integer.parseInt(values[5]));
			monster.setAttackBonus(Integer.parseInt(values[6]));
			monster.setBaseDamage(Integer.parseInt(values[7]));
			monster.setBaseMeleeDescription(values[8]);
			monster.setSightRange(Integer.parseInt(values[9]));
			monster.setDifficulty(Integer.parseInt(values[10]));
			monster.setImage(ImageManager.getGlobalRegistry().getTile("monster"));
			if (values.length == 12 && values[11] != "") {
				String[] specials = values[11].split(" ");
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
	
	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}
	
	@Override
	public int getAttack() {
		return getAttackBonus() + difficulty;
	}
	
	// When there is AI, the monster will have a next action saved and this will return its cost
	public float getActionCost() {
		return 20 / this.getSpeed();
	}
	
}


