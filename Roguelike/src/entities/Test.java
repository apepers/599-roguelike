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
		tile = new Tile();
		ItemDuplicator duplicator = new ItemDuplicator();
		for (Food f : foods) {
			tile.addItem((Holdable)duplicator.duplicate(f));
			tile.addItem((Holdable)duplicator.duplicate(f));
		}
		// Create the player and place them on our tile
		player = new Player();
		tile.setOccupant(player);
		// Read user input from the console
		Scanner reader = new Scanner(System.in);
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
		// Pick up
		Scanner reader = new Scanner(System.in);
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
			while (input != 'q') {
				try {
					Holdable item = player.removeItem((Character)input);
					tile.addItem(item);
					System.out.println("Dropped " + item.getName() + " on the floor.");
					break;
				} catch (InvalidKeyException e) {
					input = reader.next().charAt(0);
					continue;
				}
			}
			
			/*
			HashMap<Character, Holdable> playerItems = player.getInventoryList();
			// Drop the first item in the inventory
			if (playerItems.size() > 0) {
				Holdable dropItem = playerItems.get(0);
				player.removeItem(dropItem);
				player.location.addItem(dropItem);
				System.out.println("Dropped " + dropItem.name);
			} else {
				System.out.println("You aren't carrying anything to drop");
			}
			*/
		// Throw
		} else if (c == 't') {
			System.out.println("Throw what? (type 'q' to quit)");
			player.displayInventory();
			char input = reader.next().charAt(0);
			while (input != 'q') {
				try {
					Holdable item = player.removeItem((Character)input);
					System.out.println(item.throwMsg());
					System.out.println("The " + item.getName() + " has vanished into the ether");
					break;
				} catch (InvalidKeyException e) {
					input = reader.next().charAt(0);
					continue;
				}
			}
			/*
			HashMap<Character, Holdable> playerItems = player.getInventoryList();
			// Throw the first item in the inventory
			if (playerItems.size() > 0) {
				Holdable throwItem = playerItems.get(0);
				player.removeItem(throwItem);
				System.out.println(throwItem.throwMsg());
				System.out.println("The " + throwItem.name + " has vanished into the ether");
			}
			*/
		// Eat
		} else if (c == 'e') {
			System.out.println("Eat what? (type 'q' to quit)");
			player.displayInventory();
			char input = reader.next().charAt(0);
			while (input != 'q') {
				try {
					Holdable item = player.removeItem((Character)input);
					System.out.println(item.eatMsg());
					break;
				} catch (InvalidKeyException e) {
					input = reader.next().charAt(0);
					continue;
				}
			}
			/*
			HashMap<Character, Food> playerFood = player.getFood();
			// Eat the first food in the inventory
			if (playerFood.size() > 0) {
				Food eatItem = playerFood.get(0);
				player.removeItem(eatItem);
				System.out.println(eatItem.eatMsg());
			}
			*/
		// Inventory
		} else if (c == 'i') {
			player.displayInventory();
		// Look
		} else if (c == 'l') {
			player.getLocation().displayItems();
		// Help
		} else if (c == 'h') {
			System.out.println("Press p to pick up the first listed item");
			System.out.println("Press d to drop your first listed item");
			System.out.println("Press t to throw your first listed item");
			System.out.println("Press e to eat your first listed food item");
			System.out.println("Press i to view your inventory");
			System.out.println("Press l to look at the items in the tile");
			System.out.println("Press q to quit");
		}
	}
}
