package entities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class EntityCreator {
	
	ArrayList<String> foods;
	ArrayList<String> weapons;
	ArrayList<String> armours;
	ArrayList<String> monsters;
	
	public EntityCreator() {
		foods = new ArrayList<String>();
		weapons = new ArrayList<String>();
		monsters = new ArrayList<String>();
		armours = new ArrayList<String>();
		
		try {
			loadEntities();
		} catch (IOException e) {
			System.err.println("Error reading the data CSV files.");
			e.printStackTrace();
		}
		
	}

	
	/*
	 * LOAD ITEM STRINGS FROM FILE METHODS
	 */
	public void loadEntities() throws IOException {
		loadFile("Food", foods, "data" + File.separator + "itemdata.txt", Food.csvHeaders());
		loadFile("Weapon", weapons, "data" + File.separator + "weapondata.txt", Weapon.csvHeaders());
		loadFile("Armour", armours, "data" + File.separator + "armourdata.txt", Armour.csvHeaders());
		loadFile("Monster", monsters, "data" + File.separator + "monsterdata.txt", Monster.csvHeaders());
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
}
