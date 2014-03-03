package entities;

import java.util.ArrayList;
import java.util.Scanner;

import graphics.*;

import java.io.*;
import java.lang.Integer;
import java.awt.Color;

public class Test {
	static Tile tile;
	static Player player;
	
	public static void main(String[] args) {
		ArrayList<Food> foods = new ArrayList<Food>();
		try {
			BufferedReader in = new BufferedReader(new FileReader("itemdata.txt"));
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
		}

		tile = new Tile();
		for (Food f : foods) {
			tile.addItem(f);
		}
		player = new Player();
		tile.setOccupant(player);
		Scanner reader = new Scanner(System.in);
		char c = reader.next().charAt(0);
		while (c != 'q') {
			reactToChar(c);
			c = reader.next().charAt(0);
		}
		System.out.println("Goodbye.");
	}
	
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
	
	private static void reactToChar(char c) {
		if (c == 'p') {
			ArrayList<Holdable> tileItems = player.location.getItems();
			if (tileItems.size() > 0) {
				Holdable newItem = tileItems.get(0);
				player.location.removeItem(0);
				player.addItem(newItem);
				System.out.println("Picked up " + newItem.name + " off the floor.");
			} else {
				System.out.println("There is nothing here to pick up");
			}
		} else if (c == 'd') {
			ArrayList<Holdable> playerItems = player.getInventory();
			if (playerItems.size() > 0) {
				Holdable dropItem = playerItems.get(0);
				player.removeItem(dropItem);
				player.location.addItem(dropItem);
				System.out.println("Dropped " + dropItem.name);
			} else {
				System.out.println("You aren't carrying anything to drop");
			}
		} else if (c == 't') {
			ArrayList<Holdable> playerItems = player.getInventory();
			if (playerItems.size() > 0) {
				Holdable throwItem = playerItems.get(0);
				player.removeItem(throwItem);
				System.out.println(throwItem.throwMsg());
				System.out.println("The " + throwItem.name + " has vanished into the ether");
			}
		} else if (c == 'e') {
			ArrayList<Food> playerFood = player.getFood();
			if (playerFood.size() > 0) {
				Food eatItem = playerFood.get(0);
				player.removeItem(eatItem);
				System.out.println(eatItem.eatMsg());
			}
		} else if (c == 'i') {
			player.displayInventory();
		} else if (c == 'l') {
			player.location.displayItems();
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
