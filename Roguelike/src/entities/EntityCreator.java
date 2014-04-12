package entities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class EntityCreator {
	
	ArrayList<String> foods;
	ArrayList<String> weapons;
	ArrayList<String> armours;
	ArrayList<String> monsters;
	HashMap<String, String> foodQuotes;
	HashMap<String, String> weaponQuotes;
	HashMap<String, String> armourQuotes;
	HashMap<String, String> monsterQuotes;
	
	public EntityCreator() {
		foods = new ArrayList<String>();
		weapons = new ArrayList<String>();
		monsters = new ArrayList<String>();
		armours = new ArrayList<String>();
		foodQuotes = new HashMap<String, String>();
		weaponQuotes = new HashMap<String, String>();
		armourQuotes = new HashMap<String, String>();
		monsterQuotes = new HashMap<String, String>();
		
		try {
			loadEntities();
			loadDescriptions();
		} catch (IOException e) {
			System.err.println("Error reading the data CSV files.");
			e.printStackTrace();
		}
		
	}

	
	/*
	 * LOAD ITEM STRINGS FROM FILE METHODS
	 */
	public void loadEntities() throws IOException {
		loadFile("Food", foods, "data\\itemdata.txt", Food.csvHeaders());
		loadFile("Weapon", weapons, "data\\weapondata.txt", Weapon.csvHeaders());
		loadFile("Armour", armours, "data\\armourdata.txt", Armour.csvHeaders());
		loadFile("Monster", monsters, "data\\monsterdata.txt", Monster.csvHeaders());
	}
	
	
	public void loadDescriptions() throws IOException {
		foodQuotes = parseDescriptionFile("data\\itemquotes.txt");
		weaponQuotes = parseDescriptionFile("data\\weaponquotes.txt");
		armourQuotes = parseDescriptionFile("data\\armourquotes.txt");
		monsterQuotes = parseDescriptionFile("data\\monsterquotes.txt");
	}
	
	
	public void loadFile(String type, ArrayList<String> list, String file, String[] headers) throws IOException {
		BufferedReader in = null;
		in = new BufferedReader(new FileReader(file));
		String line = in.readLine();
		// Check headers
		if (!headersMatch(headers, line)) {
			System.out.println("Error: " + type + "section is improperly defined in the headers");
			System.exit(0);
		}
		
		String entity = in.readLine();
		while (entity != null) {
			if (type.equals("Food")) {
				if (validFood(entity))
					list.add(entity);
			} else if (type.equals("Weapon")) {
				if (validWeapon(entity))
					list.add(entity);
			} else if (type.equals("Armour")) {
				if (validArmour(entity))
					list.add(entity);
			} else if (type.equals("Monster")) {
				if (validMonster(entity))
					list.add(entity);
			}
			
			entity = in.readLine();
		}

		in.close();
	}
	
	
	/*
	 * VALID FORMAT TEST METHODS
	 */
	private boolean validFood(String foodString) {
		Food tempFood = Food.createFoodFromReader(foodString);
		if (tempFood != null) {
			return true;
		}
		return false;
	}
	
	
	private boolean validWeapon(String weaponString) {
		Weapon tempWeapon = Weapon.createWeaponFromReader(weaponString);
		if (tempWeapon != null) {
			return true;
		}
		return false;
	}
	
	
	private boolean validArmour(String armourString) {
		Armour tempArmour = Armour.createArmourFromReader(armourString);
		if (tempArmour != null) {
			return true;
		}
		return false;
	}
	
	
	private boolean validMonster(String monsterString) {
		Monster tempMonster = Monster.createMonsterFromReader(monsterString);
		if (tempMonster != null) {
			return true;
		}
		return false;
	}
	
	
	private boolean headersMatch(String[] headers, String input) {
		String[] inHeaders = input.split(",");
		if (inHeaders.length != headers.length) 
			return false;
		for (int i = 0; i < headers.length; i++) {
			if (!inHeaders[i].equals(headers[i]))
				return false;
		}
		return true;
	}
	
	
	/*
	 * NEW ENTITY CREATION METHODS
	 */
	public Food createFood(int index) {
		Food newFood = Food.createFoodFromReader(foods.get(index));
		if (newFood != null) {
			addFoodDescription(newFood);
			return newFood;
		} else {
			System.out.println("Created Food is null");
			return null;
		}
	}
	
	
	public Food createFood(String name) {
		int index = getIndex(name, foods);
		if (index == -1) {
			System.out.println("A food named " + name + " does not exist");
			return null;
		}
		return createFood(index);
	}
	
	
	public Weapon createWeapon(int index) {
		Weapon newWeapon = Weapon.createWeaponFromReader(weapons.get(index));
		if (newWeapon != null) {
			addWeaponDescription(newWeapon);
			return newWeapon;
		} else {
			System.out.println("Created Weapon is null");
			return null;
		}
	}
	
	
	public Weapon createWeapon(String name) {
		int index = getIndex(name, weapons);
		if (index == -1) {
			System.out.println("A weapon named " + name + " does not exist");
			return null;
		}
		return createWeapon(index);
	}
	
	
	public Armour createArmour(int index) {
		Armour newArmour = Armour.createArmourFromReader(armours.get(index));
		if (newArmour != null) {
			addArmourDescription(newArmour);
			return newArmour;
		} else {
			System.out.println("Created Armour is null");
			return null;
		}
	}
	
	
	public Armour createArmour(String name) {
		int index = getIndex(name, armours);
		if (index == -1) {
			System.out.println("An armour named " + name + " does not exist");
			return null;
		}
		return createArmour(index);
	}
	
	
	public Monster createMonster(int index) {
		Monster newMonster = Monster.createMonsterFromReader(monsters.get(index));
		if (newMonster != null) {
			addMonsterDescription(newMonster);
			return newMonster;
		} else {
			System.out.println("Created Monster is null");
			return null;
		}
	}
	
	
	public Monster createMonster(String name) {
		int index = getIndex(name, monsters);
		if (index == -1) {
			System.out.println("A monster named " + name + " does not exist");
			return null;
		}
		return createMonster(index);
	}
	
	
	private int getIndex(String name, ArrayList<String> list) {
		int i = 0;
		while (i < list.size()) {
			if (list.get(i).split(",")[0].equals(name))
				return i;
			i++;
		}
		return -1;
	}
	
	
	/*
	 * GET ARRALIST SIZE METHODS
	 */
	public int numFoods() {
		return foods.size();
	}
	
	public int numWeapons() {
		return weapons.size();
	}
	
	public int numArmours() {
		return armours.size();
	}
	
	public int numMonsters() {
		return monsters.size();
	}
	
	
	/*
	 * THE STUFF BELOW HAS TO DO WITH ADDING THE DESCRIPTIONS AND QUOTES TO ENTITIES
	 */
	
	/*
	 * Parse through a given description/quotes .txt file and
	 * convert it into a HashMap with the Entity name as the key
	 */
	private HashMap<String, String> parseDescriptionFile(String filename) throws IOException {
		HashMap<String, String> descMap = new HashMap<String, String>();
		BufferedReader in = null;
		in = new BufferedReader(new FileReader(filename));
		String key = "";
		String value = "";

		String line = in.readLine();
		while (line != null) {
			//This line should always be the monster name.
			key = line.substring(2).trim().toLowerCase();
			value = "";
			line = in.readLine();
			while((line != null) && (!line.startsWith("##"))) {
				if(line.startsWith("^^")) {
					value += "\n" + line.substring(2).trim();
				} else {
					value += line + "\n";
				}
				line = in.readLine();
			}
			descMap.put(key, value);
		}
		in.close();
		return descMap;
	}
	
	
	private void addFoodDescription(Food food) {
		String name = food.getName().toLowerCase();
		if(foodQuotes.containsKey(name)) {
			food.setDescription(foodQuotes.get(name));
		} else {
			food.setDescription("Just your average, run of the mill " + name);
		}
	}
	
	
	private void addWeaponDescription(Weapon weapon) {
		String name = weapon.getName().toLowerCase();
		if(weaponQuotes.containsKey(name)) {
			weapon.setDescription(weaponQuotes.get(name));
		} else {
			weapon.setDescription("Just your average, run of the mill " + name);
		}
	}
	
	
	private void addArmourDescription(Armour armour) {
		String name = armour.getName().toLowerCase();
		if(armourQuotes.containsKey(name)) {
			armour.setDescription(armourQuotes.get(name));
		} else {
			armour.setDescription("Just your average, run of the mill " + name);
		}
	}
	
	
	private void addMonsterDescription(Monster monster) {
		String name = monster.getName().toLowerCase();
		if(monsterQuotes.containsKey(name)) {
			monster.setDescription(monsterQuotes.get(name));
		} else {
			monster.setDescription("Just your average, run of the mill " + name);
		}
	}

}
