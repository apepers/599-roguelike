/*
 * Master controller for the system, the central class which processes requests and asks the appropriate
 * parts of the game for the information needed and sends it back where it has to go.
 */

package game;

import java.util.ArrayList;
import java.util.Random;

import entities.*;
import serialization.ItemDuplicator;

import java.io.*;

public class Controller {
	private Player player;
	ArrayList<Food> foods;
	private ItemDuplicator duplicator;
	private Map map;
	private Messenger messenger;
	boolean gameRunning;
	
	public Controller() { };
	
	public boolean setup(Map m) {
		foods = new ArrayList<Food>();
		try {
			loadFoods();
		} catch (IOException e) {
			System.err.println("Error reading foods CSV file.");
			return false;
		}
		duplicator = new ItemDuplicator();
		player = new Player();
/*
		// Would randomly add items to the map
		for (Food f : foods) {
			tile.addItem((Holdable)duplicator.duplicate(f));
			tile.addItem((Holdable)duplicator.duplicate(f));
		}*/
		map = m;
		Random rand = new Random();
		Tile tile;
		do {
			int width = rand.nextInt(map.getWidth());
			System.out.println(width);
			int height = rand.nextInt(map.getHeight());
			System.out.println(height);
			tile = map.getTile(width, height);
		} while (!tile.isPassable());
		// Place player on the map
		tile.setOccupant(player);
		messenger = new Messenger(this, player);
		return true;
	}
	
	private void loadFoods() throws IOException {
		BufferedReader in = null;
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

		in.close();
	}
	
	// Make sure that the headers of the food section match what we expect
	private boolean headersMatch(String input) {
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
	
	public void endGame() {
		gameRunning = false;
		// Handle any serialization or other game ending logic
		System.exit(0);	// Could this be done more smoothly? Not sure
	}
	
	public Messenger getMessenger() {
		return messenger;
	}
	
	public String playerEat(Food food) {
		player.reduceHunger(food.getNutrition());
		return food.eatMsg();
	}
	
	public void movePlayerUp() {
		System.out.println("Moving up");
		Tile newTile = map.getTile(player.getLocation().getColumn(), player.getLocation().getRow() - 1);
		if (newTile.isPassable()) {
			player.setLocation(newTile);
			System.out.println("In new tile!");
		}
	}
	
	public void movePlayerDown() {
		Tile newTile = map.getTile(player.getLocation().getColumn(), player.getLocation().getRow() + 1);
		if (newTile.isPassable())
			player.setLocation(newTile);
	}
}