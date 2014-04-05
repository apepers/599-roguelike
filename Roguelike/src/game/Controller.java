/*
 * Master controller for the system, the central class which processes requests and asks the appropriate
 * parts of the game for the information needed and sends it back where it has to go.
 */

package game;

import java.util.ArrayList;

import entities.*;
import graphics.ImageManager;
import serialization.ItemDuplicator;
import mapGeneration.MapGenerator;
import mapGeneration.MapInterpreter;
import mapGeneration.MapRand;
import mapGeneration.SimpleMap;
import graphics.TileDisplay;

import java.awt.Point;
import java.io.*;

public class Controller {
	private Player player;
	private ArrayList<Food> foods;
	private ArrayList<Monster> monsters;
	private ItemDuplicator duplicator;
	private Map map;
	private Messenger messenger;
	boolean gameRunning;


	private static Controller global;

	private Controller() { 

		//load the food and monster CSV files.
		foods = new ArrayList<Food>();
		monsters = new ArrayList<Monster>();
		try {
			loadFoods();
			loadMonsters();
		} catch (IOException e) {
			System.err.println("Error reading the data CSV files.");
		}

		//prepare duplicator and player
		duplicator = new ItemDuplicator();
		


	};

	public static Controller getInstance(){
		if (global == null){
			global = new Controller();
		}
		return global;
	}

	public void setup(Messenger messenger, Player p){
		
		this.messenger = messenger;
		this.player = p;
		
		//create the map.
		MapGenerator map = new SimpleMap(15,15,3,3);
		Map m = MapInterpreter.interpretMap(map, ImageManager.getInstance().getAllTileSets("map"));

		
		
		// Place player on the map
		Point spawn = m.getPlayerSpawn();
		m.getTile(spawn.x, spawn.y).setOccupant(player);
		
		
		//setup the display
		messenger.drawMap(m);
		messenger.updateStatus(playerStatus());
		
	}

	private void loadFoods() throws IOException {
		BufferedReader in = null;
		in = new BufferedReader(new FileReader("itemdata.txt"));
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
		in = new BufferedReader(new FileReader("monsterdata.txt"));
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

	public void combatTest() {
		Monster testMonster = getRandMapMonster(0);
		System.out.println("A wild " + testMonster.getName() +" appears!");
		while (!testMonster.isDead()) {
			System.out.println("The monster attacks!");
			if (this.monsterAttack(testMonster))
				System.out.println("The monster hits!");
			else
				System.out.println("The monster misses!");
			System.out.println(playerStatus());
			System.out.println("The player attacks!");
			if (playerAttack(testMonster)) 
				System.out.println("You hit!");
			else
				System.out.println("You miss!");
		}
		System.out.println("The " + testMonster.getName() + " is slain!");
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
		System.out.println("Moving up");
		if (player.getLocation().getRow() > 0) {
			Tile newTile = map.getTile(player.getLocation().getColumn(), player.getLocation().getRow() - 1);
			if (newTile.isPassable()) {
				player.setLocation(newTile);
				System.out.println("In new tile!");
			}
		}
	}

	public void movePlayerDown() {
		if (player.getLocation().getRow() < map.getWidth() - 1) {
			Tile newTile = map.getTile(player.getLocation().getColumn(), player.getLocation().getRow() + 1);
			if (newTile.isPassable())
				player.setLocation(newTile);
		}
	}

	public boolean monsterAttack(Monster monster) {
		int attackRoll = MapRand.randInt(20) + monster.getAttack();
		if (attackRoll >= player.getAC()) {
			player.takeDamage(monster.getMeleeDamage());
			return true;
		} else {
			return false;
		}
	}

	public boolean playerAttack(Monster monster) {
		int attackRoll = MapRand.randInt(20) + player.getAttack();
		if (attackRoll >= monster.getAC()) {
			monster.takeDamage(player.getMeleeDamage());
			return true;
		} else {
			return false;
		}
	}

	public String playerStatus() {
		return "Player: HP = " + player.getCurrentHP() + ", Strength = " + player.getStrength() + ", Dexterity = " + player.getDexterity();
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