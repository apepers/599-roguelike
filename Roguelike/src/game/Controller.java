/*
 * Master controller for the system, the central class which processes requests and asks the appropriate
 * parts of the game for the information needed and sends it back where it has to go.
 */

package game;

import java.util.ArrayList;

import entities.*;
import graphics.ImageManager;
import serialization.ItemDuplicator;
import mapGeneration.BSTMap;
import mapGeneration.MapGenerator;
import mapGeneration.MapInterpreter;
import mapGeneration.MapRand;
import mapGeneration.SimpleMap;

import java.awt.Point;
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
			e.printStackTrace();
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
		createMap();
	}
	
	/**
	 * Creates the map for the entire game. Does all linking
	 * between maps and sets the player's spawn when starting.
	 */
	private void createMap(){
		MapGenerator map = new SimpleMap(20,15,3,3);
		Map m = MapInterpreter.interpretMap(map, ImageManager.getInstance().getAllTileSets("map"));

		this.map = m;
		
		// Place player on the map
		Point spawn = m.getPlayerSpawn();
		m.getTile(spawn.x, spawn.y).setOccupant(player);
		
		
		//setup the display
		messenger.drawMap(m);
		messenger.updateStatus(playerStatus());
		messenger.centerMap(spawn);
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

	public void combatTest() {
		Monster testMonster = getRandMapMonster(0);
		System.out.println("A wild " + testMonster.getName() +" appears!");
		while (!testMonster.isDead()) {
			System.out.println("The monster attacks!");
			if (this.monsterAttack(testMonster))
				System.out.println("The monster " + testMonster.getBaseMeleeDescription() + " you!");
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
		if (player.getLocation().getRow() > 0) {
			movePlayer(0, -1);
		}
	}

	public void movePlayerDown() {
		if (player.getLocation().getRow() < map.getHeight() - 1) {
			movePlayer(0, 1);
		}
	}

	public void movePlayerRight(){
		if (player.getLocation().getColumn() < map.getWidth() - 1) {
			movePlayer(1, 0);
		}
	}
	
	public void movePlayerLeft(){
		if (player.getLocation().getColumn() > 0) {
			movePlayer(-1, 0);
		}
	}
	
	/**
	 * Moves the player in any of the specified directions
	 * Player position is then updated on screen and in game state.
	 * @param deltaX
	 * @param deltaY
	 */
	private void movePlayer(int deltaX, int deltaY){
		Point oldPt = new Point(player.getLocation().getColumn(), player.getLocation().getRow());
		Point newPt = new Point(oldPt.x + deltaX, oldPt.y + deltaY);
		
		Tile nextTile = map.getTile(newPt.x, newPt.y);
		if (nextTile.isPassable() && !nextTile.isOccupied()){
			player.setLocation(nextTile);
			map.getTile(oldPt.x, oldPt.y).removeOccupant();
			map.getTile(newPt.x, newPt.y).setOccupant(player);
			
			//update the tile
			messenger.updateTile(oldPt);
			messenger.updateTile(newPt);
			messenger.centerMap(newPt);
		}
	}
	
	public void createCursor() {
		cursor = new Cursor(player.getLocation());
		Point point = new Point(cursor.getLocation().getColumn(), cursor.getLocation().getRow());
		messenger.drawImage(cursor.getImg(), point);
	}
	
	
	public String select() {
		return cursor.getTopEntity();
	}
	
	
	public void deleteCursor() {
		messenger.updateTile(cursor.getLocation().getColumn(), cursor.getLocation().getRow());
		cursor = null;
	}
	
	
	public void moveCursorUp() {
		if (cursor.getLocation().getRow() > 0) {
			moveCursor(0,-1);
		}
	}
	
	public void moveCursorDown() {
		if (cursor.getLocation().getRow() < map.getHeight() - 1) {
			moveCursor(0,1);
		}
	}
	
	public void moveCursorLeft() {
		if (cursor.getLocation().getColumn() > 0) {
			moveCursor(-1,0);
		}
	}

	
	public void moveCursorRight() {
		if (cursor.getLocation().getColumn() < map.getWidth() - 1) {
			moveCursor(1,0);
		}
	}
	
	
	/**
	 * Moves the cursor in any of the specified directions
	 * Cursor position is then updated on screen and in game state.
	 * @param deltaX
	 * @param deltaY
	 */
	private void moveCursor(int deltaX, int deltaY){
		Point oldPt = new Point(cursor.getLocation().getColumn(), cursor.getLocation().getRow());
		Point newPt = new Point(oldPt.x + deltaX, oldPt.y + deltaY);
		
		Tile nextTile = map.getTile(newPt.x, newPt.y);
		cursor.setLocation(nextTile);
		
		messenger.updateTile(oldPt);
		messenger.drawImage(cursor.getImg(), newPt);
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