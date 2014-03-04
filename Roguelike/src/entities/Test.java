/* Simple class for testing the various entity related classes
 * 
 */

package entities;

import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Scanner;

import graphics.*;
import serialization.*;

import java.io.*;
import java.lang.Integer;

public class Test {
	static Tile tile;
	static Tile tile1;
	static Tile tile2;
	static Player player;
	
	public static void main(String[] args) {
		ArrayList<Food> foods = new ArrayList<Food>();
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader("itemdata.txt"));
			String line = in.readLine();
			if (!headersMatch(line)) {
				System.out.println("Error: Food section is improperly defined in the headers");
				System.exit(0);
			}
			// Check headers
			String food = in.readLine();
			while (food != null) {
				Food newFood = Food.createFoodFromReader(food);
				if (newFood != null)
					foods.add(newFood);
				food = in.readLine();
			}
		} catch (IOException e) {
			System.out.println("Error reading the item file");
			System.exit(0);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				System.out.println("Couldn't close item database file");
			}
		}

		// Add a single tile to stand on, and one of each food to that tile
		ItemDuplicator duplicator = new ItemDuplicator();
		tile1 = new Tile();
		tile1.addItem((Holdable)duplicator.duplicate(foods.get(0)));
		tile1.addItem((Holdable)duplicator.duplicate(foods.get(1)));
		tile1.addItem((Holdable)duplicator.duplicate(foods.get(1)));
		
		tile2 = new Tile();
		tile2.addItem((Holdable)duplicator.duplicate(foods.get(1)));
		tile2.addItem((Holdable)duplicator.duplicate(foods.get(2)));
		tile2.addItem((Holdable)duplicator.duplicate(foods.get(0)));
		
		tile = tile1;
		
		/*
		for (Food f : foods) {
			tile.addItem((Holdable)duplicator.duplicate(f));
			tile.addItem((Holdable)duplicator.duplicate(f));
		}
		*/
		// Create the player and place them on our tile
		player = new Player();
		tile.setOccupant(player);
		System.out.println("You are standing in tile 1");
		// Read user input from the console
		Scanner reader = new Scanner(System.in);
		System.out.println("Welcome!");
		char c = reader.next().charAt(0);
		while (c != 'q') {
			reactToChar(c);
			c = reader.next().charAt(0);
		}
		reader.close();
		System.out.println("Goodbye.");
	}
	
	// Make sure that the headers of the food section match what we expect
	private static boolean headersMatch(String input) {
		String[] inHeaders = input.split(",");
		String[] headers = {"Name", "Cost", "Weight", "Red", "Blue", "Green", 
				"Nutrition", "TurnsToEat", "EatMsg", "Special"};
		if (inHeaders.length != headers.length) 
			return false;
		for (int i = 0; i < headers.length; i++) {
			if (!inHeaders[i].equals(headers[i]))
				return false;
		}
		return true;
	}
	
	// React to user input
	private static void reactToChar(char c) {
		Scanner reader = new Scanner(System.in);
		
		// Pick up
		if (c == 'p') {
			System.out.println("Pick up what? (type 'q' to quit)");
			tile.displayItems();
			char input = reader.next().charAt(0);
			Holdable newItem = null;
			while (input != 'q') {
				try {
					Holdable item = tile.getItem((Character) input);
					if (item.isStackable()) {
						System.out.println("How many do you want to pick up? (#, all, or q to quit)");
						item.display();
						String countInput = reader.next();
						while (countInput.charAt(0) != 'q') {
							if (countInput.matches("\\d*")) {
								int count = Integer.parseInt(countInput);
								if (count > item.stackSize())
									System.out.println("There aren't that many " + item.getName() + " here.");
								else if (count < 1) 
									System.out.println("You have to pick up at least 1.");
								else {
									newItem = tile.removeItem(input, count);
									break;
								}
							} else if (countInput.equalsIgnoreCase("all")) {
								newItem = tile.removeItem(input, item.stackSize());
								break;
							}
							countInput = reader.next();
							if (countInput.charAt(0) == 'q')
								input = 'q';
						}
					} else {
						newItem = tile.removeItem((Character)input);
					}
					if (newItem != null) {
						player.addItem(newItem);
						System.out.println("Picked up " + newItem.properName() + " off the floor.");
						break;
					} else {
						System.out.println("Didn't pick anything up.");
					}
				} catch (InvalidKeyException e) {
					input = reader.next().charAt(0);
					continue;
				}
			}
			
		// Drop
		} else if (c == 'd') {
			System.out.println("Drop what? (type 'q' to quit)");
			player.displayInventory();
			char input = reader.next().charAt(0);
			Holdable newItem = null;
			while (input != 'q') {
				try {
					Holdable item = player.getItem((Character) input);
					if (item.isStackable()) {
						System.out.println("How many do you want to drop? (#, all, or q to quit)");
						item.display();
						String countInput = reader.next();
						while (countInput.charAt(0) != 'q') {
							if (countInput.matches("\\d*")) {
								int count = Integer.parseInt(countInput);
								if (count > item.stackSize())
									System.out.println("You don't have that many " + item.getName() + ".");
								else if (count < 1) 
									System.out.println("You have to drop at least 1.");
								else {
									newItem = player.removeItem(input, count);
									break;
								}
							} else if (countInput.equalsIgnoreCase("all")) {
								newItem = player.removeItem(input, item.stackSize());
								break;
							}
							countInput = reader.next();
							if (countInput.charAt(0) == 'q')
								input = 'q';
						}
					} else {
						newItem = player.removeItem((Character)input);
					}
					if (newItem != null) {
						tile.addItem(newItem);
						System.out.println("Dropped " + newItem.properName() + " onto the floor.");
						break;
					} else {
						System.out.println("Didn't drop anything.");
					}
				} catch (InvalidKeyException e) {
					input = reader.next().charAt(0);
					continue;
				}
			}
			
		// Throw
		} else if (c == 't') {
			System.out.println("Throw what? (type 'q' to quit)");
			player.displayInventory();
			char input = reader.next().charAt(0);
			Holdable newItem = null;
			while (input != 'q') {
				try {
					Holdable item = player.getItem((Character) input);
					if (item.isStackable()) {
						System.out.println("How many do you want to throw? (#, all, or q to quit)");
						item.display();
						String countInput = reader.next();
						while (countInput.charAt(0) != 'q') {
							if (countInput.matches("\\d*")) {
								int count = Integer.parseInt(countInput);
								if (count > item.stackSize())
									System.out.println("You don't have that many " + item.getName() + ".");
								else if (count < 1) 
									System.out.println("You have to throw at least 1.");
								else {
									newItem = player.removeItem(input, count);
									break;
								}
							} else if (countInput.equalsIgnoreCase("all")) {
								newItem = player.removeItem(input, item.stackSize());
								break;
							}
							countInput = reader.next();
							if (countInput.charAt(0) == 'q')
								input = 'q';
						}
					} else {
						newItem = player.removeItem((Character)input);
					}
					if (newItem != null) {
						System.out.println(item.throwMsg());
						System.out.println("The " + item.getName() + " has vanished into the ether");
						break;
					} else {
						System.out.println("Didn't throw anything.");
					}
				} catch (InvalidKeyException e) {
					input = reader.next().charAt(0);
					continue;
				}
			}

		// Eat
		} else if (c == 'e') {
			System.out.println("Eat what? (type 'q' to quit)");
			player.displayInventory();
			char input = reader.next().charAt(0);
			Holdable newItem = null;
			while (input != 'q') {
				try {
					Holdable item = player.getItem((Character) input);
					if (item.isStackable()) {
						System.out.println("How many do you want to eat? (#, all, or q to quit)");
						item.display();
						String countInput = reader.next();
						while (countInput.charAt(0) != 'q') {
							if (countInput.matches("\\d*")) {
								int count = Integer.parseInt(countInput);
								if (count > item.stackSize())
									System.out.println("You don't have that many " + item.getName() + ".");
								else if (count < 1) 
									System.out.println("You have to eat at least 1.");
								else {
									newItem = player.removeItem(input, count);
									break;
								}
							} else if (countInput.equalsIgnoreCase("all")) {
								newItem = player.removeItem(input, item.stackSize());
								break;
							}
							countInput = reader.next();
							if (countInput.charAt(0) == 'q')
								input = 'q';
						}
					} else {
						newItem = player.removeItem((Character)input);
					}
					if (newItem != null) {
						System.out.println(item.eatMsg());
						break;
					} else {
						System.out.println("Didn't throw anything.");
					}
				} catch (InvalidKeyException e) {
					input = reader.next().charAt(0);
					continue;
				}
			}

		// Inventory
		} else if (c == 'i') {
			player.displayInventory();
			
		// Look
		} else if (c == 'l') {
			player.getLocation().displayItems();
			
		// Move
		} else if (c == 'm') {
			if(player.getLocation() == tile1) {
				tile.removeOccupant();
				tile = tile2;
				System.out.println("You move to tile 2");
			} else if(player.getLocation() == tile2) {
				tile.removeOccupant();
				tile = tile1;
				System.out.println("You move to tile 1");
			}
			tile.setOccupant(player);
			
		// Help
		} else if (c == 'h') {
			System.out.println("Press m to move to another tile");
			System.out.println("Press p to pick up an item");
			System.out.println("Press d to drop an item");
			System.out.println("Press t to throw an item");
			System.out.println("Press e to eat a food item");
			System.out.println("Press i to view your inventory");
			System.out.println("Press l to look at the items in the tile");
			System.out.println("Press q to quit");
		}
	}
}
