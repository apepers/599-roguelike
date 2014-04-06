package entities;

public class Monster extends Sentient {
	private int difficulty;
	
	public Monster() { };
	
	public static String[] csvHeaders() {
		String[] headers = {"Name", "HP", "NaturalArmour", "Strength", "Dexterity", "AttackBonus", "BaseMeleeDamage", "BaseMeleeDescription", "Difficulty", "Special"};
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
			monster.setAttackBonus(Integer.parseInt(values[5]));
			monster.setBaseDamage(Integer.parseInt(values[6]));
			monster.setBaseMeleeDescription(values[7]);
			monster.setDifficulty(Integer.parseInt(values[8]));
			if (values.length == 10 && values[9] != "") {
				String[] specials = values[9].split(" ");
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
}
