/*
 * Master controller for the system, the central class which processes requests and asks the appropriate
 * parts of the game for the information needed and sends it back where it has to go.
 */

package game;

import java.util.ArrayList;
import java.util.Random;

import entities.*;
import serialization.ItemDuplicator;
import mapGeneration.MapRand;

import java.io.*;

public class Controller {
	private Player player;
	private Cursor cursor;
	ArrayList<Food> foods;
	ArrayList<Monster> monsters;
	private ItemDuplicator duplicator;
	private Map map;
	private Messenger messenger;
	boolean gameRunning;
	
	public Controller() { };
	
	public boolean setup(Map m) {
		foods = new ArrayList<Food>();
		monsters = new ArrayList<Monster>();
		try {
			loadFoods();
			loadMonsters();
		} catch (IOException e) {
			System.err.println("Error reading the data CSV files.");
			e.printStackTrace();
			return false;
		}
		duplicator = new ItemDuplicator();
		player = new Player();
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
		in = new BufferedReader(new FileReader("src\\itemdata.txt"));
		String line = in.readLine();
		if (!headersMatch(Food.csvHeaders(), line)) {
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
	
	private void loadMonsters() throws IOException {
		BufferedReader in = null;
		in = new BufferedReader(new FileReader("src\\monsterdata.txt"));
		String line = in.readLine();
		if (!headersMatch(Monster.csvHeaders(), line)) {
			System.out.println("Error: Monster CSV file is improperly defined in the headers");
			System.exit(0);
		}
		String monster = in.readLine();
		while (monster != null) {
			Monster newMonster = Monster.createMonsterFromReader(monster);
			if (newMonster != null) {
				monsters.add(newMonster);
			}
			monster = in.readLine();
		}
		in.close();
	}
	
	// Make sure that the headers of the food section match what we expect
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
		if (player.getLocation().getRow() > 0) {
			Tile newTile = map.getTile(player.getLocation().getColumn(), player.getLocation().getRow() - 1);
			if (newTile.isPassable()) 
				player.setLocation(newTile);
			else
				System.out.println("Collision");
			
		} else {
			System.out.println("At top boundary");
		}
	}
	
	public void movePlayerDown() {
		if (player.getLocation().getRow() < map.getHeight() - 1) {
			Tile newTile = map.getTile(player.getLocation().getColumn(), player.getLocation().getRow() + 1);
			if (newTile.isPassable())
				player.setLocation(newTile);
			else
				System.out.println("Collision");
		}
		else {
			System.out.println("At bottom boundary");
		}
	}
	
	public void movePlayerLeft() {
		if (player.getLocation().getColumn() > 0) {
			Tile newTile = map.getTile(player.getLocation().getColumn() - 1, player.getLocation().getRow());
			if (newTile.isPassable())
				player.setLocation(newTile);
			else
				System.out.println("Collision");
		} else {
			System.out.println("At left boundary");
		}
	}

	
	public void movePlayerRight() {
		if (player.getLocation().getColumn() < map.getWidth() - 1) {
			Tile newTile = map.getTile(player.getLocation().getColumn() + 1, player.getLocation().getRow());
			if (newTile.isPassable())
				player.setLocation(newTile);
			else
				System.out.println("Collision");
		} else {
			System.out.println("At right boundary");
		}
	}
	
	
	public void createCursor() {
		cursor = new Cursor(player.getLocation());
	}
	
	
	public String select() {
		return cursor.getTopEntity();
	}
	
	
	public void deleteCursor() {
		cursor = null;
	}
	
	
	public void moveCursorUp() {
		if (cursor.getLocation().getRow() > 0) {
			Tile newTile = map.getTile(cursor.getLocation().getColumn(), cursor.getLocation().getRow() - 1);
			cursor.setLocation(newTile);
		}
	}
	
	public void moveCursorDown() {
		if (cursor.getLocation().getRow() < map.getHeight() - 1) {
			Tile newTile = map.getTile(cursor.getLocation().getColumn(), cursor.getLocation().getRow() + 1);
			cursor.setLocation(newTile);
		}
	}
	
	public void moveCursorLeft() {
		if (cursor.getLocation().getColumn() > 0) {
			Tile newTile = map.getTile(cursor.getLocation().getColumn() - 1, cursor.getLocation().getRow());
			cursor.setLocation(newTile);
		}
	}

	
	public void moveCursorRight() {
		if (cursor.getLocation().getColumn() < map.getWidth() - 1) {
			Tile newTile = map.getTile(cursor.getLocation().getColumn() + 1, cursor.getLocation().getRow());
			cursor.setLocation(newTile);
		}
	}
	
	// Return a random item for the map, given the current depth in the station
	// Currently just returns one of our foods randomly.
	public Holdable getRandMapItem(int mapIndex) {
		int randomIndex = MapRand.randInt(foods.size() - 1);
		return (Holdable)duplicator.duplicate(foods.get(randomIndex));
	}
	
	public Monster getRandMapMonster(int mapIndex) {
		int randomIndex = MapRand.randInt(monsters.size() - 1);
		return (Monster)duplicator.duplicate(monsters.get(randomIndex));
	}
}